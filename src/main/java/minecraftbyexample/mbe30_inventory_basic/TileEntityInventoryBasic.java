package minecraftbyexample.mbe30_inventory_basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * This is a simple tile entity implementing IInventory that can store 9 item stacks
 */
public class TileEntityInventoryBasic extends TileEntity implements IItemHandler {
	// Create and initialize the item variable that will store store the item
	private final int NUMBER_OF_SLOTS = 9;
//	private ItemStack[] itemStacks;

  private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS);

	public TileEntityInventoryBasic()
	{
    super(StartupCommon.tileEntityInventoryBasic);
		clear();
	}

	/* The following are some IInventory methods you are required to override */

	// Gets the number of slots in the inventory
	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

  // returns true if all of the slots in the inventory are empty
	@Override
	public boolean isEmpty()
  {
    for (ItemStack itemstack : itemStacks) {
      if (!itemstack.isEmpty()) {  // isEmpty()
        return false;
      }
    }

    return true;
  }

  @Override
  public int getSlots() {
    return 0;
  }

  // Gets the stack in the given slot
	@Override
	public ItemStack getStackInSlot(int slotIndex) {
		return itemStacks[slotIndex];
	}

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    return null;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return null;
  }

  @Override
  public int getSlotLimit(int slot) {
    return 0;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return false;
  }

  /**
	 * Removes some of the units from itemstack in the given slot, and returns as a separate itemstack
 	 * @param slotIndex the slot number to remove the item from
	 * @param count the number of units to remove
	 * @return a new itemstack containing the units removed from the slot
	 */
	@Override
	public ItemStack decrStackSize(int slotIndex, int count) {
		ItemStack itemStackInSlot = getStackInSlot(slotIndex);
		if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;  // isEmpt();   EMPTY_ITEM

		ItemStack itemStackRemoved;
		if (itemStackInSlot.getCount() <= count) {  // getStackSize()
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(slotIndex, ItemStack.EMPTY);   // EMPTY_ITEM
		} else {
			itemStackRemoved = itemStackInSlot.splitStack(count);
			if (itemStackInSlot.getCount() == 0) { // getStackSize
				setInventorySlotContents(slotIndex, ItemStack.EMPTY);   // EMPTY_ITEM
			}
		}
	  markDirty();
		return itemStackRemoved;
	}

	// overwrites the stack in the given slotIndex with the given stack
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
		itemStacks[slotIndex] = itemstack;
		if (itemstack.isEmpty() && itemstack.getCount() > getInventoryStackLimit()) { //  isEmpty(); getStackSize()
			itemstack.setCount(getInventoryStackLimit());  //setStackSize
		}
		markDirty();
	}

	// This is the maximum number if item allowed in each slot
	// This only affects things such as hoppers trying to insert item you need to use the container to enforce this for players
	// inserting item via the gui
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	// Return true if the given player is able to use this block. In this case it checks that
	// 1) the world tileentity hasn't been replaced in the meantime, and
	// 2) the player isn't too far away from the centre of the block
	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	// Return true if the given stack is allowed to go in the given slot.  In this case, we can insert anything.
	// This only affects things such as hoppers trying to insert item you need to use the container to enforce this for players
	// inserting item via the gui
	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack) {
		return true;
	}

	private static final String INVENTORY_NBT_TAG = "contents";

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, it saves the itemstacks stored in the container
	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound)
	{
		super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
    CompoundNBT inventoryNBT = itemStackHandler.serializeNBT();
    parentNBTTagCompound.put(INVENTORY_NBT_TAG, inventoryNBT);

//		// to use an analogy with Java, this code generates an array of hashmaps
//		// The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
//		//   as slot=1, id=2353, count=1, etc
//		// Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.
//		ListNBT dataForAllSlots = new ListNBT();
//		for (int i = 0; i < this.itemStacks.length; ++i) {
//			if (!this.itemStacks[i].isEmpty())	{ //isEmpty()
//				CompoundNBT dataForThisSlot = new CompoundNBT();
//				dataForThisSlot.setByte("Slot", (byte) i);
//				this.itemStacks[i].writeToNBT(dataForThisSlot);
//				dataForAllSlots.appendTag(dataForThisSlot);
//			}
//		}
//		// the array of hashmaps is then inserted into the parent hashmap for the container
//		parentNBTTagCompound.setTag("Items", dataForAllSlots);
		// return the NBT Tag Compound
		return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in write
	@Override
	public void read(CompoundNBT parentNBTTagCompound)
	{
		super.read(parentNBTTagCompound); // The super call is required to save and load the tiles location
    CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(INVENTORY_NBT_TAG);
    itemStackHandler.deserializeNBT(inventoryNBT);
    if (itemStackHandler.getSlots() != NUMBER_OF_SLOTS)
      throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");

//
//		final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
//		ListNBT dataForAllSlots = parentNBTTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);
//
//		Arrays.fill(itemStacks, ItemStack.EMPTY);           // set all slots to empty EMPTY_ITEM
//		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
//			CompoundNBT dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
//			int slotIndex = dataForOneSlot.getByte("Slot") & 255;
//
//			if (slotIndex >= 0 && slotIndex < this.itemStacks.length) {
//				this.itemStacks[slotIndex] = new ItemStack(dataForOneSlot);
//			}
//		}
	}

	// set all slots to empty
	@Override
	public void clear() {
		Arrays.fill(itemStacks, ItemStack.EMPTY);  //empty item
	}

	// will add a key for this container to the lang file so we can name it in the GUI
	@Override
	public String getName() {
		return "container.mbe30_inventory_basic.name";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	// standard code to look up what the human-readable name is
	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName());
	}

	// -----------------------------------------------------------------------------------------------------------
	// The following methods are not needed for this example but are part of IInventory so they must be implemented

//	/**
//	 * This method removes the entire contents of the given slot and returns it.
//	 * Used by containers such as crafting tables which return any item in their slots when you close the GUI
//	 * @param slotIndex
//	 * @return
//	 */
//	@Override
//	public ItemStack removeStackFromSlot(int slotIndex) {
//		ItemStack itemStack = getStackInSlot(slotIndex);
//		if (!itemStack.isEmpty()) setInventorySlotContents(slotIndex, ItemStack.EMPTY);  //isEmpty(), EMPTY_ITEM
//		return itemStack;
//	}
//
//	@Override
//	public void openInventory(PlayerEntity player) {}
//
//	@Override
//	public void closeInventory(PlayerEntity player) {}
//
//	@Override
//	public int getField(int id) {
//		return 0;
//	}
//
//	@Override
//	public void setField(int id, int value) {}
//
//	@Override
//	public int getFieldCount() {
//		return 0;
//	}
}
