/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2015, 6:42:40 PM (GMT)]
 */
package minecraftbyexample.mbe32_inventory_item;

import minecraftbyexample.mbe30_inventory_basic.ChestContents;
import minecraftbyexample.mbe30_inventory_basic.TileEntityInventoryBasic;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * The ContainerFlowerBag is used to manipulate the contents of the FlowerBag (ItemStackHandlerFlowerBag).
 * The master copy is on the server side, with a "dummy" copy stored on the client side
 * The GUI on the client side interacts with the dummy copy.
 * Vanilla ensures that the server and client copies remain synchronised.
 */

public class ContainerFlowerBag extends Container {

  public static ContainerFlowerBag createContainerServerSide(int windowID, PlayerInventory playerInventory, ItemStackHandlerFlowerBag bagContents) {
    return new ContainerFlowerBag(windowID, playerInventory, bagContents);
  }

  public static ContainerFlowerBag createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
    // for this example we use extraData for the server to tell the client how many flower itemstacks the flower bag contains.
    int sizeOfFlowerBag = extraData.readInt();

    try {
      ItemStackHandlerFlowerBag itemStackHandlerFlowerBag = new ItemStackHandlerFlowerBag(sizeOfFlowerBag);

      // on the client side there is no parent ItemStack to communicate with - we use a dummy inventory
      return new ContainerFlowerBag(windowID, playerInventory, itemStackHandlerFlowerBag);
    } catch (IllegalArgumentException iae) {
      LOGGER.warn(iae);
    }
    return null;
  }

	private final ItemStackHandlerFlowerBag itemStackHandlerFlowerBag;

	public ContainerFlowerBag(int windowId, PlayerInventory playerInv, ItemStackHandlerFlowerBag itemStackHandlerFlowerBag) {
		super(TYPE, windowId);
		int i;
		int j;

		this.itemStackHandlerFlowerBag = itemStackHandlerFlowerBag;

		for (i = 0; i < 2; ++i)
			for (j = 0; j < 8; ++j) {
				int k = j + i * 8;
				addSlot(new SlotItemHandler(flowerBagInv, k, 17 + j * 18, 26 + i * 18));
			}

		for (i = 0; i < 3; ++i)
			for (j = 0; j < 9; ++j)
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity player) {
		ItemStack main = player.getHeldItemMainhand();
		ItemStack off = player.getHeldItemOffhand();
		return !main.isEmpty() && main == bag || !off.isEmpty() && off == bag;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(slotIndex < 16) {
				if(!mergeItemStack(itemstack1, 16, 52, true))
					return ItemStack.EMPTY;
			} else {
				Block b = Block.getBlockFromItem(itemstack.getItem());
				int i = b instanceof BlockModFlower ? ((BlockModFlower) b).color.getId() : -1;
				if(i >= 0 && i < 16) {
					Slot slot1 = inventorySlots.get(i);
					if(slot1.isItemValid(itemstack) && !mergeItemStack(itemstack1, i, i + 1, true))
						return ItemStack.EMPTY;
				}
			}

			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

  private static final Logger LOGGER = LogManager.getLogger();

}
