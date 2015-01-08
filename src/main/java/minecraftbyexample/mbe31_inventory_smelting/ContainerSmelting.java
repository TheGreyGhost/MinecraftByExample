package minecraftbyexample.mbe31_inventory_smelting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * ContainerSmelting is used to link the client side gui to the server side inventory and it is where
 * you add the slots to the. It is also used to send server side data such as progress bars to the client
 * for use in guis
 */
public class ContainerSmelting extends Container {

	// Stores the tile entity instance for later use
	private TileInventorySmelting tile;
	// These store cache values used to update the client side tile entity
	private int lastTickBurnTime;
	private int lastTickItemBurnTime;
	private int lastTickCookTime;



	public ContainerSmelting(InventoryPlayer invPlayer, TileInventorySmelting tile) {
		this.tile = tile;

		// Add the players hotbar to the gui
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 171));
		}

		// Add the rest of the players inventory to the gui
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 113 + y * 18));
			}
		}

		// Add the tile input slots
		for (int x = 0; x < 5; x++) {
			addSlotToContainer(new SlotSmeltable(tile, x, 55 + 18 * x, 30));
		}

		// Add the tile output slots
		for (int x = 0; x < 5; x++) {
			addSlotToContainer(new SlotOutput(tile, 5 + x, 55 + 18 * x, 84));
		}

		// Add the tile fuel slots
		for (int y = 0; y < 4; y++) {
			addSlotToContainer(new SlotFuel(tile, 10 + y, 18, 30 + 18 * y));
		}

	}

	// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tile.isUseableByPlayer(player);
	}

	// This is where you specify what happens when a player shift clicks a slot in the gui
	// Note: the int is the slot index. Each slot is automatically assigned an index when it is added to the container
	// In this case the players inventory slots were added first so slots 0 to 35 are the players inventory and
	// 36 to 49 are the tiles inventory
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i)
	{
		Slot slot = (Slot) inventorySlots.get(i);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();

			// If the slot clicked is one of the players inventory slots
			if (i < 36)
			{
				// If the stack is smeltable try to merge merge the stack into the input slots
				if (TileInventorySmelting.getSmeltingResultForItem(stack) != null){
					if (!mergeItemStack(stack, 36, 42, false)){
						return null;
					}
				}
				else if (TileInventorySmelting.getItemBurnTime(stack) > 0){
					if (!mergeItemStack(stack, 46, 50, true)){ // Setting the boolean to true places the stack in the bottom slot first
						return null;
					}
				}
				else
				{
					return null;
				}
			}
			else // else the slot must be one of the tile slots
			{
				// Merge the stack into the players inventory
				if (!mergeItemStack(stack, 0, 36, false)){
					return null;
				}
			}

			// If stack size == 0 (the entire stack was moved) set slot contents to null
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}

			slot.onPickupFromSlot(player, stack);

			return result;
		}
		return null;
	}

	/* Client Synchronization */

	// This is where you check if any values have changed and if so send an update to the client
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		// go through the list of craters (players using this container) and update them if necessary
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (tile.cookTime != lastTickCookTime)  // if cookTime has changed
			{
				icrafting.sendProgressBarUpdate(this, 0, tile.cookTime);
			}
			if (lastTickBurnTime != tile.burnTimeRemaining)  // if burnTimeRemaining has changed
			{
				icrafting.sendProgressBarUpdate(this, 1, tile.burnTimeRemaining);
			}
			if (lastTickItemBurnTime != tile.currentItemBurnTime)  // if currentItemBurnTime has changed
			{
				icrafting.sendProgressBarUpdate(this, 2, tile.currentItemBurnTime);
			}
		}

		// Note tha although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts

		// update cache's
		lastTickItemBurnTime = tile.currentItemBurnTime;
		lastTickBurnTime = tile.burnTimeRemaining;
		lastTickCookTime = tile.cookTime;
	}

	// Called when a progress bar update is received from the server. The two values (id and data) are the same two
	// values given to sendProgressBarUpdate
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		switch (id)
		{
			case 0:
				tile.cookTime = data;
				break;
			case 1:
				tile.burnTimeRemaining = data;
				break;
			case 2:
				tile.currentItemBurnTime = data;
				break;
		}
	}

	// SlotFuel is a slot that will only accept fuel items
	public class SlotFuel extends Slot {

		public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileInventorySmelting.getItemBurnTime(stack) > 0;
		}
	}

	// SlotSmeltable is a slot that will only accept smeltable items
	public class SlotSmeltable extends Slot {

		public SlotSmeltable(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileInventorySmelting.getSmeltingResultForItem(stack) != null;
		}
	}

	// SlotOutput is a slot that will not accept any items
	public class SlotOutput extends Slot {

		public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
}
