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
import net.minecraft.util.math.MathHelper;
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
    // for this example we use extraData for the server to tell the client how many slots for flower itemstacks the flower bag contains.
    int numberOfFlowerSlots = extraData.readInt();

    try {
      ItemStackHandlerFlowerBag itemStackHandlerFlowerBag = new ItemStackHandlerFlowerBag(numberOfFlowerSlots);

      // on the client side there is no parent ItemStack to communicate with - we use a dummy inventory
      return new ContainerFlowerBag(windowID, playerInventory, itemStackHandlerFlowerBag);
    } catch (IllegalArgumentException iae) {
      LOGGER.warn(iae);
    }
    return null;
  }

	private final ItemStackHandlerFlowerBag itemStackHandlerFlowerBag;

  // must assign a slot number to each of the slots used by the GUI.
  // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
  // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
  //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
  //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
  //  36 - 51 = TileInventory slots, which map to our bag slot numbers 0 - 15)

  private static final int HOTBAR_SLOT_COUNT = 9;
  private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
  private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
  private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
  private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

  private static final int VANILLA_FIRST_SLOT_INDEX = 0;
  private static final int BAG_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
  private static final int MAX_EXPECTED_BAG_SLOT_COUNT = 16;

  public static final int BAG_INVENTORY_YPOS = 26;  // the ContainerScreenFlowerBag needs to know these so it can tell where to draw the Titles
  public static final int PLAYER_INVENTORY_YPOS = 84;

  /**
   * Creates a container suitable for server side or client side
   * @param windowId ID of the container
   * @param playerInv the inventory of the player
   * @param itemStackHandlerFlowerBag the inventory stored in the bag
   */
  private ContainerFlowerBag(int windowId, PlayerInventory playerInv, ItemStackHandlerFlowerBag itemStackHandlerFlowerBag) {
		super(StartupCommon.containerTypeFlowerBag, windowId);
		int i;
		int j;

		this.itemStackHandlerFlowerBag = itemStackHandlerFlowerBag;

    final int SLOT_X_SPACING = 18;
    final int SLOT_Y_SPACING = 18;
    final int HOTBAR_XPOS = 8;
    final int HOTBAR_YPOS = 142;
    // Add the players hotbar to the gui - the [xpos, ypos] location of each item
    for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
      int slotNumber = x;
      addSlot(new Slot(playerInv, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
    }

    final int PLAYER_INVENTORY_XPOS = 8;
    // Add the rest of the player's inventory to the gui
    for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
      for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
        int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
        int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
        int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
        addSlot(new Slot(playerInv, slotNumber, xpos, ypos));
      }
    }

    int bagSlotCount = itemStackHandlerFlowerBag.getSlots();
    if (bagSlotCount < 1 || bagSlotCount > MAX_EXPECTED_BAG_SLOT_COUNT) {
      LOGGER.warn("Unexpected invalid slot count in ItemStackHandlerFlowerBag(" + bagSlotCount + ")");
      bagSlotCount = MathHelper.clamp(bagSlotCount, 1, MAX_EXPECTED_BAG_SLOT_COUNT);
    }

    final int BAG_SLOTS_PER_ROW = 8;
    final int BAG_INVENTORY_XPOS = 17;
    // Add the tile inventory container to the gui
    for (int bagSlot = 0; bagSlot < bagSlotCount; ++bagSlot) {
      int slotNumber = bagSlot;
      int bagRow = bagSlot / BAG_SLOTS_PER_ROW;
      int bagCol = bagSlot % BAG_SLOTS_PER_ROW;
      int xpos = BAG_INVENTORY_XPOS + SLOT_X_SPACING * bagCol;
      int ypos = BAG_INVENTORY_YPOS + SLOT_Y_SPACING * bagRow;
      addSlot(new SlotItemHandler(itemStackHandlerFlowerBag, slotNumber, xpos, ypos));
    }

//    for (i = 0; i < 2; ++i)
//			for (j = 0; j < 8; ++j) {
//				int k = j + i * 8;
//				addSlot(new SlotItemHandler(flowerBagInv, k, 17 + j * 18, 26 + i * 18));
//			}
//
//		for (i = 0; i < 3; ++i)
//			for (j = 0; j < 9; ++j)
//				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

//		for (i = 0; i < 9; ++i) {
//			addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
//		}

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
