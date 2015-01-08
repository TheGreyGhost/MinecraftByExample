package minecraftbyexample.mbe31_inventory_smelting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * TileInventorySmelting is an advanced sided inventory that works like a vanilla furnace except that it has 5 input and output slots,
 * 4 fuel slots and cooks at twice the speed.
 * The slots are used sequentially rather than in parallel, i.e. the first slot cooks, then the second, then the third, etc
 * The code is heavily based on TileEntityFurnace.
 */
public class TileInventorySmelting extends TileEntity implements ISidedInventory, IUpdatePlayerListBox {
	// Create and initialize the itemStacks variable that will store store the itemStacks
	final int FUEL_SLOTS_COUNT = 4;
	final int INPUT_SLOTS_COUNT = 5;
	final int OUTPUT_SLOTS_COUNT = 5;
	final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	final int FIRST_FUEL_SLOT = 0;
	final int FIRST_INPUT_SLOT = FUEL_SLOTS_COUNT;
	final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;

	ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];

	/**The number of ticks remaining on the current piece of fuel*/
	public int burnTimeRemaining;
	/**The max number of ticks the currently burning item can burn*/
	public int currentItemBurnTime;
	/**The number of ticks the current item has been cooking*/
	public int cookTime;
	/**The number of ticks required to cook an item*/
	public final int maxCookTime = 100;

	// This method is called every tick to update the tile entity (i.e.
	// - see if the fuel has run out, and if so turn the furnace "off"
	// - see if any of the items have finished smelting
	@Override
	public void update() {

		// If there is nothing to smelt or there is no room in the output, reset cookTime and return
		if (canSmelt())
		{
			// If burnTimeRemaining == 0 try to refuel
			tryRefuel();

			// If burnTimeRemaining > 0 increment cookTime otherwise decrement cookTime by 2
			if (burnTimeRemaining > 0) ++cookTime;
			else cookTime -= 2;
			if (cookTime < 0) cookTime = 0;

			// If cookTime has reached maxCookTime smelt the item and reset cookTime
			if (cookTime >= maxCookTime)
			{
				smeltItem();
				cookTime = 0;
			}
		}
		else
		{
			cookTime = 0;
		}

		// Because this furnace cooks twice as fast as a vanilla furnace we decrement burnTimeRemaining by 2
		// so that it burns fuel twice as fast
		if (burnTimeRemaining > 0) burnTimeRemaining -= 2;
		if (burnTimeRemaining < 0) burnTimeRemaining = 0;
	}

	// checks if burnTimeRemaining = 0 and tries to consume a new piece of fuel if one is available
	private void tryRefuel(){

		if (burnTimeRemaining > 0) return;

		// Iterate over the 4 fuel slots
		for (int i = FIRST_FUEL_SLOT; i < FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT; i++) {
			if (itemStacks[i] != null && getItemBurnTime(itemStacks[i]) > 0) {
				// If the stack in this slot is not null and is fuel set burnTimeRemaining & currentItemBurnTime to the
				// items burn time and decrease the stack size
				burnTimeRemaining = currentItemBurnTime = getItemBurnTime(itemStacks[i]);
				--itemStacks[i].stackSize;

				// If the stack size now equals 0 set the slot contents to the items container item. This is for fuel
				// items such as lava buckets so that the bucket is not consumed. If the item dose not have
				// a container item getContainerItem returns null which sets the slot contents to null
				if (itemStacks[i].stackSize == 0)
				{
					itemStacks[i] = itemStacks[i].getItem().getContainerItem(itemStacks[i]);
				}
				return;
			}
		}
	}

	// todo - integrate into a single method using practice run
	// checks that there is an item to be smelted in one of the input slots and that there is room for the result in the output slots
	private boolean canSmelt()
	{
		ItemStack result = null;

		// sets input to the first smeltable stack in the input slots
		for (int i = FIRST_INPUT_SLOT; i < FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT; i++) {
			if (itemStacks[i] != null) {
				result = getSmeltingResultForItem(itemStacks[i]);
				if (result != null) break;
			}
		}
		// return false if there are no smeltable items
		if (result == null) return false;

		// checks that there is room for the result in the output slots
		for (int i = FIRST_OUTPUT_SLOT; i < FIRST_OUTPUT_SLOT + OUTPUT_SLOTS_COUNT; i++) {
			ItemStack itemStack = itemStacks[i];
			if (itemStack == null) return true;
			if (itemStack.getItem() == result.getItem() && (!itemStack.getHasSubtypes() || itemStack.getMetadata() == itemStack.getMetadata())
																								  && ItemStack.areItemStackTagsEqual(itemStack, result) ) {
				int resultSize = itemStacks[i].stackSize + result.stackSize;
				if (resultSize <= getInventoryStackLimit() && resultSize <= itemStacks[i].getMaxStackSize()) return true;
			}
		}

		return false;
	}


	private void smeltItem(){
		ItemStack result = null;

		// sets result to the smelting result of the first smeltable item
		for (int i = FIRST_INPUT_SLOT; i < FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT; i++)	{
			if (itemStacks[i] != null && getSmeltingResultForItem(itemStacks[i]) != null) {
				// Use .copy() to avoid altering the recipe
				result = getSmeltingResultForItem(itemStacks[i]).copy();
				--itemStacks[i].stackSize;
				if (itemStacks[i].stackSize <= 0) itemStacks[i] = null;
				break;
			}
		}

		if (result == null) return;

		// Add the result to the output slots
		for (int i = 5; i < 10; i++)
		{
			if (itemStacks[i] == null)
			{
				itemStacks[i] = result;
				return;
			}
			if (itemStacks[i].isItemEqual(result))
			{
				int resultSize = itemStacks[i].stackSize + result.stackSize;
				if (resultSize <= getInventoryStackLimit() && resultSize <= itemStacks[i].getMaxStackSize())
				{
					itemStacks[i].stackSize += result.stackSize;
					return;
				}
			}
		}
	}

	// returns the smelting result for the given stack. Returns null if the given stack can not be smelted
	public static ItemStack getSmeltingResultForItem(ItemStack stack) { return FurnaceRecipes.instance().getSmeltingResult(stack); }

	// returns the number of ticks the given item will burn. Returns 0 if the given item is not a valid fuel
	public static int getItemBurnTime(ItemStack stack)
	{
		return TileEntityFurnace.getItemBurnTime(stack);  // just use the vanilla values
	}

	// Gets the number of slots in the inventory
	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

	// Gets the stack in the given slot
	@Override
	public ItemStack getStackInSlot(int i) {
		return itemStacks[i];
	}

	// Reduces the size of the stack in the given slot
	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack itemstack = getStackInSlot(slot);

		if (itemstack != null) {
			if (itemstack.stackSize <= count) {
				setInventorySlotContents(slot, null);
			} else {
				itemstack = itemstack.splitStack(count);
				if (itemstack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return itemstack;
	}

	// @TGG I dont recall what this is for
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack item = getStackInSlot(i);
		if (item != null) setInventorySlotContents(i, null);
		return item;
	}

	// Sets the stack in the given slot
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		itemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	// This is the maximum number if items allowed in each slot
	// This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
	// inserting items via the gui
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	// Return true if the given player is able to use this block. In this case it checks that the player isn't too far away
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.4) < 64;
	}

	// Return true if the given stack is allowed to go in the given slot
	// This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
	// inserting items via the gui
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		// Return true if the slot is one of the input slots and the item has a smelting result
		if (i < 5 && getSmeltingResultForItem(itemstack) != null) return true;
		// Return true if the slot is one of the fuel slots and the item is a valid fuel
		if (i > 9 && getItemBurnTime(itemstack) > 0) return true;

		// If nether of those conditions are true return false
		return false;
	}

	// This is where you save any data that you don't want to loose when the tile entity unloads
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound); // The super call is required to save and load the tiles location

		// Save the stored item stacks
		NBTTagCompound[] tag = new NBTTagCompound[itemStacks.length];

		for (int i = 0; i < itemStacks.length; i++)
		{
			tag[i] = new NBTTagCompound();

			if (itemStacks[i] != null)
			{
				tag[i] = itemStacks[i].writeToNBT(tag[i]);
			}

			compound.setTag("Item" + i, tag[i]);
		}

		// Save everything else
		compound.setInteger("CookTime", cookTime);
		compound.setInteger("CurrentItemBurnTime", currentItemBurnTime);
		compound.setInteger("BurnTimeRemaining", burnTimeRemaining);
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound); // The super call is required to save and load the tiles location

		// Load the stored item stacks
		NBTTagCompound[] tag = new NBTTagCompound[itemStacks.length];

		for (int i = 0; i < itemStacks.length; i++)
		{
			tag[i] = compound.getCompoundTag("Item" + i);
			itemStacks[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}

		// Load everything else
		cookTime = compound.getInteger("CookTime");
		currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
		burnTimeRemaining = compound.getInteger("BurnTimeRemaining");
	}

	// This is where you specify which slots are available from what sides
	// This is setup to imitate the vanilla furnace
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// If side == top return the input slots
		if (side == EnumFacing.UP) return new int[] {0, 1, 2, 3, 4};
		// If side == bottom return the output and fuel slots. The fuel slots are made accessible so it is possible to
		// extract things such as empty buckets
		if (side == EnumFacing.DOWN) return new int[] {5, 6, 7, 8, 9, 10, 11, 12, 13};
		// If none of the above are true side must equal one of the 4 sides of the block so return the fuel slots
		return new int[] {10, 11, 12, 13};
	}

	// This is where you specify what items can be inserted into what slots from what sides
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	// This is where you specify what items can be extracted from what slots on what sides
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// If direction == bottom and the slot is one of the output slots return true
		if (direction == EnumFacing.DOWN && index > 4 && index < 10) return true;
		// If direction == bottom, the slot is one of the fuel slots and the item being extracted is not fuel return true
		// This allows things such as empty buckets to be extracted from the fuel slots
		if (direction == EnumFacing.DOWN && index > 9 && index < 14 && getItemBurnTime(stack) == 0) return true;
		return false;
	}

	// The following methods are not used in this example but are part of IInventory so they must be implemented

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}


	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		ItemStack[] items = new ItemStack[9];
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}
}
