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
import net.minecraft.nbt.CompoundNBT;
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
import java.util.function.Predicate;

/**
 * The ContainerFlowerBag is used to manipulate the contents of the FlowerBag (ItemStackHandlerFlowerBag).
 * The master copy is on the server side, with a "dummy" copy stored on the client side
 * The GUI (ContainerScreen) on the client side interacts with the dummy copy.
 * Vanilla ensures that the server and client copies remain synchronised.
 */

public class ContainerFlowerBag extends Container {

  /**
   * Creates the container to be used on the server side
   * @param windowID
   * @param playerInventory
   * @param bagContents
   * @param flowerBag the ItemStack for the flower bag; this is used for checking whether the player is still holding the bag in their hand
   * @return
   */
  public static ContainerFlowerBag createContainerServerSide(int windowID, PlayerInventory playerInventory, ItemStackHandlerFlowerBag bagContents,
                                                             ItemStack flowerBag) {
    return new ContainerFlowerBag(windowID, playerInventory, bagContents, flowerBag);
  }

  /**
   * Creates the container to be used on the client side  (contains dummy data)
   * @param windowID
   * @param playerInventory
   * @param extraData extra data sent from the server
   * @return
   */
  public static ContainerFlowerBag createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
    // for this example we use extraData for the server to tell the client how many slots for flower itemstacks the flower bag contains.
    int numberOfFlowerSlots = extraData.readInt();

    try {
      ItemStackHandlerFlowerBag itemStackHandlerFlowerBag = new ItemStackHandlerFlowerBag(numberOfFlowerSlots);

      // on the client side there is no parent ItemStack to communicate with - we use a dummy inventory
      return new ContainerFlowerBag(windowID, playerInventory, itemStackHandlerFlowerBag, ItemStack.EMPTY);
    } catch (IllegalArgumentException iae) {
      LOGGER.warn(iae);
    }
    return null;
  }

	private final ItemStackHandlerFlowerBag itemStackHandlerFlowerBag;
  private final ItemStack itemStackBeingHeld;

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
  private ContainerFlowerBag(int windowId, PlayerInventory playerInv,
                             ItemStackHandlerFlowerBag itemStackHandlerFlowerBag,
                             ItemStack itemStackBeingHeld) {
		super(StartupCommon.containerTypeFlowerBag, windowId);
		this.itemStackHandlerFlowerBag = itemStackHandlerFlowerBag;
		this.itemStackBeingHeld = itemStackBeingHeld;

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
	}

	// Check if the player is still able to access the container
  // In this case - if the player stops holding the bag, return false
  // Called on the server side only.
	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity player) {

		ItemStack main = player.getHeldItemMainhand();
		ItemStack off = player.getHeldItemOffhand();
		return (!main.isEmpty() && main == itemStackBeingHeld) ||
            (!off.isEmpty() && off == itemStackBeingHeld);
	}

  // This is where you specify what happens when a player shift clicks a slot in the gui
  //  (when you shift click a slot in the Bag Inventory, it moves it to the first available position in the hotbar and/or
  //    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
  //    position in the Bag inventory)
  // At the very least you must override this and return ItemStack.EMPTY or the game will crash when the player shift clicks a slot
  // returns ItemStack.EMPTY if the source slot is empty, or if none of the the source slot item could be moved
  //   otherwise, returns a copy of the source stack
  @Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex) {
    Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
    if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
    ItemStack sourceStack = sourceSlot.getStack();
    ItemStack copyOfSourceStack = sourceStack.copy();
    final int BAG_SLOT_COUNT = itemStackHandlerFlowerBag.getSlots();

    // Check if the slot clicked is one of the vanilla container slots
    if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
      // This is a vanilla container slot so merge the stack into the bag inventory
      if (!mergeItemStack(sourceStack, BAG_INVENTORY_FIRST_SLOT_INDEX, BAG_INVENTORY_FIRST_SLOT_INDEX + BAG_SLOT_COUNT, false)){
        return ItemStack.EMPTY;  // EMPTY_ITEM
      }
    } else if (sourceSlotIndex >= BAG_INVENTORY_FIRST_SLOT_INDEX && sourceSlotIndex < BAG_INVENTORY_FIRST_SLOT_INDEX + BAG_SLOT_COUNT) {
      // This is a bag slot so merge the stack into the players inventory
      if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
        return ItemStack.EMPTY;
      }
    } else {
      LOGGER.warn("Invalid slotIndex:" + sourceSlotIndex);
      return ItemStack.EMPTY;
    }

    // If stack size == 0 (the entire stack was moved) set slot contents to null
    if (sourceStack.getCount() == 0) {
      sourceSlot.putStack(ItemStack.EMPTY);
    } else {
      sourceSlot.onSlotChanged();
    }

    sourceSlot.onTake(player, sourceStack);
    return copyOfSourceStack;
	}

  /**
   * Because capability nbt is not actually stored in the ItemStack nbt (it is created fresh each time we need to transmit or save an nbt), detectAndSendChanges
   *   does not work for our ItemFlowerBag ItemStack.  i.e. when the contents of ItemStackHandlerFlowerBag are changed, the nbt of ItemFlowerBag ItemStack don't change,
   *   so it is not sent to the client.
   * For this reason, we need to manually detect when it has changed and mark it dirty.
   * The easiest way is just to set a counter in the nbt tag and let the vanilla code notice that the itemstack has changed.
   * The side effect is that the player's hand moves down and up (because the client thinks it is a new ItemStack) but that's not objectionable.
   * Alternatively you could copy the code from vanilla detectAndSendChanges and tweak it to find the slot for itemStackBeingHeld and send it manually.
   *
   * Of course, if your ItemStack's capability doesn't affect the rendering of the ItemStack, i.e. the Capability is not needed on the client at all, then
   *   you don't need to bother with marking it dirty.
   */
	@Override
  public void detectAndSendChanges() {
    if (itemStackHandlerFlowerBag.isDirty()) {
      CompoundNBT nbt = itemStackBeingHeld.getOrCreateTag();
      int dirtyCounter = nbt.getInt("dirtyCounter");
      nbt.putInt("dirtyCounter", dirtyCounter + 1);
      itemStackBeingHeld.setTag(nbt);
    }
    super.detectAndSendChanges();
  }

  private static final Logger LOGGER = LogManager.getLogger();

}
