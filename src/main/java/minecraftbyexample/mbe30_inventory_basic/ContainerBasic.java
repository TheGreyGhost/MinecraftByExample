package minecraftbyexample.mbe30_inventory_basic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * The container is used to link the client side gui to the server side inventory and it is where
 * you add the slots to your gui. It can also be used to sync server side data with the client but
 * that will be covered in a latter tutorial
 */
public class ContainerBasic extends Container {

	// Stores the tile entity instance for later use
	private TileInventoryBasic tile;

	public ContainerBasic(InventoryPlayer invPlayer, TileInventoryBasic tile) {
		this.tile = tile;

		// Add the players hotbar to the gui
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 109));
		}

		// Add the rest of the players inventory to the gui
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 51 + y * 18));
			}
		}

		// Add the tile inventory to the gui
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(tile, x, 8 + 18 * x, 20));
		}

	}

	// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tile.isUseableByPlayer(player);
	}

	// This is where you specify what happens when a player shift clicks a slot in the gui
	// At the very least you must override this and return null or the game will crash when the player shift clicks a slot
	// Note: the int is the slot index. Each slot is automatically assigned an index when it is added to the container
	// In this case the players inventory slots were added first so slots 0 to 35 are the players inventory and
	// 36 to 44 are the tiles inventory
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
				// Merge the stack into the tile inventory
				if (!mergeItemStack(stack, 36, 45, false)){
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
}
