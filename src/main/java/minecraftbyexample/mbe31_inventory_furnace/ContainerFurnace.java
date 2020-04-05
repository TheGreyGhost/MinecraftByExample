package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * ContainerSmelting is used to link the client side gui to the server side inventory and it is where
 * you add the slots holding item. It is also used to send server side data such as progress bars to the client
 * for use in guis
 *
 * Check out RecipeBookContainer
 */
public class ContainerFurnace extends Container {

  public static ContainerFurnace createContainerServerSide(int windowID, PlayerInventory playerInventory,
                                                           FurnaceZoneContents inputZoneContents,
                                                           FurnaceZoneContents outputZoneContents,
                                                           FurnaceZoneContents fuelZoneContents) {
    return new ContainerFurnace(windowID, playerInventory,
            inputZoneContents, outputZoneContents, fuelZoneContents);
  }

  public static ContainerFurnace createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
    //  don't need extraData for this example; if you want you can use it to provide extra information from the server, that you can use
    //  when creating the client container
    //  eg String detailedDescription = extraData.readString(128);
    FurnaceZoneContents inputZoneContents = FurnaceZoneContents.createForClientSideContainer(INPUT_SLOTS_COUNT);
    FurnaceZoneContents outputZoneContents = FurnaceZoneContents.createForClientSideContainer(OUTPUT_SLOTS_COUNT);
    FurnaceZoneContents fuelZoneContents = FurnaceZoneContents.createForClientSideContainer(FUEL_SLOTS_COUNT);

    // on the client side there is no parent TileEntity to communicate with, so we:
    // 1) use dummy inventories
    // 2) use "do nothing" lambda functions for canPlayerAccessInventory and markDirty
    return new ContainerFurnace(windowID, playerInventory,
                                inputZoneContents, outputZoneContents, fuelZoneContents);
  }

  // must assign a slot index to each of the slots used by the GUI.
	// For this container, we can see the furnace fuel, input, and output slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container using addSlotToContainer(), it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 39 = fuel slots (furnaceStateData 0 - 3)
	//  40 - 44 = input slots (furnaceStateData 4 - 8)
	//  45 - 49 = output slots (furnaceStateData 9 - 13)

	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	public static final int FUEL_SLOTS_COUNT = TileEntityFurnace.FUEL_SLOTS_COUNT;
	public static final int INPUT_SLOTS_COUNT = TileEntityFurnace.INPUT_SLOTS_COUNT;
	public static final int OUTPUT_SLOTS_COUNT = TileEntityFurnace.OUTPUT_SLOTS_COUNT;
	public static final int FURNACE_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	// slot index is the unique index for all slots in this container i.e. 0 - 35 for invPlayer then 36 - 49 for furnaceContents
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
  private static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
  private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
	private static final int FIRST_FUEL_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int FIRST_INPUT_SLOT_INDEX = FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT;
	private static final int FIRST_OUTPUT_SLOT_INDEX = FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;

	// slot number is the slot number within each component;
  // i.e. invPlayer slots 0 - 35 (hotbar 0 - 8 then main inventory 9 to 35)
  // and furnace: inputZone slots 0 - 4, outputZone slots 0 - 4, fuelZone 0 - 3

	public ContainerFurnace(int windowID, PlayerInventory invPlayer,
            FurnaceZoneContents inputZoneContents,
            FurnaceZoneContents outputZoneContents,
            FurnaceZoneContents fuelZoneContents) {
    super(StartupCommon.containerTypeContainerFurnace, windowID);
    if (StartupCommon.containerTypeContainerFurnace == null)
      throw new IllegalStateException("Must initialise containerTypeContainerFurnace before constructing a ContainerFurnace!");

		final int SLOT_X_SPACING = 18;
		final int SLOT_Y_SPACING = 18;
		final int HOTBAR_XPOS = 8;
		final int HOTBAR_YPOS = 183;
		// Add the players hotbar to the gui - the [xpos, ypos] location of each item
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlot(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}

		final int PLAYER_INVENTORY_XPOS = 8;
		final int PLAYER_INVENTORY_YPOS = 125;
		// Add the rest of the players inventory to the gui
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlot(new Slot(invPlayer, slotNumber,  xpos, ypos));
			}
		}

		final int FUEL_SLOTS_XPOS = 53;
		final int FUEL_SLOTS_YPOS = 96;
		// Add the tile fuel slots
		for (int x = 0; x < FUEL_SLOTS_COUNT; x++) {
			int slotNumber = x;
			addSlot(new SlotFuel(fuelZoneContents, slotNumber, FUEL_SLOTS_XPOS + SLOT_X_SPACING * x, FUEL_SLOTS_YPOS));
		}

		final int INPUT_SLOTS_XPOS = 26;
		final int INPUT_SLOTS_YPOS = 24;
		// Add the tile input slots
		for (int y = 0; y < INPUT_SLOTS_COUNT; y++) {
			int slotNumber = y;
			addSlot(new SlotSmeltableInput(inputZoneContents, slotNumber, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS+ SLOT_Y_SPACING * y));
		}

		final int OUTPUT_SLOTS_XPOS = 134;
		final int OUTPUT_SLOTS_YPOS = 24;
		// Add the tile output slots
		for (int y = 0; y < OUTPUT_SLOTS_COUNT; y++) {
			int slotNumber = y;
			addSlot(new SlotOutput(outputZoneContents, slotNumber, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS + SLOT_Y_SPACING * y));
		}
	}

	// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return fuelZoneContents.isUsableByPlayer(player) && inputZoneContents.isUsableByPlayer(player)
            && outputZoneContents.isUsableByPlayer(player);
	}

	// This is where you specify what happens when a player shift clicks a slot in the gui
	//  (when you shift click a slot in the TileEntity Inventory, it moves it to the first available position in the hotbar and/or
	//    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
	//    position in the TileEntity inventory - either input or fuel as appropriate for the item you clicked)
	// At the very least you must override this and return ItemStack.EMPTY or the game will crash when the player shift clicks a slot.
	// returns ItemStack.EMPTY if the source slot is empty, or if none of the source slot item could be moved.
	//   otherwise, returns a copy of the source stack
  //  copied from vanilla furnace AbstractFurnaceContainer
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex)
	{
		Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
    ItemStack sourceStack = sourceSlot.getStack();
    ItemStack sourceStackBeforeMerge = sourceStack.copy();
    boolean successfulTransfer = false;

		// Check which slot the item is being transferred out of: player inventory or furnace contents
		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
      successfulTransfer = transferFromPlayerInventoryToFurnace(sourceStack);
		} else if (sourceSlotIndex >= FIRST_FUEL_SLOT_INDEX && sourceSlotIndex < FIRST_FUEL_SLOT_INDEX + FURNACE_SLOTS_COUNT) {
      successfulTransfer = transferFromFurnaceToPlayerInventory(sourceSlotIndex, sourceSlot, sourceStack);
		} else {
			LOGGER.warn("Invalid sourceSlotIndex:" + sourceSlotIndex);
			return ItemStack.EMPTY;
		}
    if (!successfulTransfer) return ItemStack.EMPTY;

		// If source stack is empty (the entire stack was moved) set slot contents to empty
		if (sourceStack.isEmpty()) {
			sourceSlot.putStack(ItemStack.EMPTY);
		} else {
			sourceSlot.onSlotChanged();
		}

		// if source stack is still the same as before the merge, the transfer failed
    if (sourceStack.getCount() == sourceStackBeforeMerge.getCount()) {
      return ItemStack.EMPTY;
    }
		sourceSlot.onTake(player, sourceStack);
		return sourceStackBeforeMerge;            // don't ask me why it returns the original
	}

  /**
   * Attempt to transfer items out of the given ItemStack into the player's inventory.
   * Try the hotbar first and then the main inventory.
   * @param sourceSlotIndex the index of the slot where the item is coming from
   * @param sourceSlot the slot that the item is coming from
   * @param sourceItemStack the ItemStack to transfer out of.  Gets modified if successful.
   * @return true if a successful transfer occurred
   */
	private boolean transferFromFurnaceToPlayerInventory(int sourceSlotIndex, Slot sourceSlot,   ItemStack sourceItemStack) {
    ItemStack sourceStackBeforeMerge = sourceItemStack.copy();
    if (!mergeItemStack(sourceItemStack, HOTBAR_FIRST_SLOT_INDEX, HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT, false)) {
      return false;
    }

    if (!mergeItemStack(sourceItemStack, PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT, false)) {
      return false;
    }
    // if we are removing the item from the output zone of the furnace, need to call onSlotChange because we have just smelted the item
    //   and this triggers achievements
    if (sourceSlotIndex >= FIRST_FUEL_SLOT_INDEX && sourceSlotIndex < FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT) {
      sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
    }
    return true;
  }

  /**
   * Attempt to transfer items out of the given sourceItemStack into an appropriate place in the furnace
   * @param sourceItemStack the ItemStack to transfer out of.  Gets modified if successful.
   * @return true if a successful transfer occurred
   */
  private boolean transferFromPlayerInventoryToFurnace(ItemStack sourceItemStack) {

    // This is a vanilla container slot so merge the stack into one of the furnace zone slots:
    // 1) if smeltable: try the input slots
    // 2) if burnable: try the fuel slots

    if (!TileEntityFurnace.getSmeltingResultForItem(sourceItemStack).isEmpty()){
      if (mergeItemStack(sourceItemStack, FIRST_INPUT_SLOT_INDEX, FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT, false)){
        return true;
      }
    }	else if (TileEntityFurnace.getItemBurnTime(sourceItemStack) > 0) {
      // Setting the reverseDirection boolean to true places the stack in the bottom slot first
      if (mergeItemStack(sourceItemStack, FIRST_FUEL_SLOT_INDEX, FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT, true)) {
        return true;
      }
    }
    return false;
  }

  public ItemStack transferStackInSlot(PlayerEntity playerIn, Slot sourceSlot, ItemStack sourceItemStack, int index) {
    ItemStack initialStack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot == null || !slot.getHasStack()) {
      return ItemStack.EMPTY;
    }

    ItemStack sourceStack = slot.getStack();
    initialStack = sourceStack.copy();
    if (index == 2) { // output
      if (!this.mergeItemStack(sourceStack, 3, 39, true)) {
        return ItemStack.EMPTY;
      }
      slot.onSlotChange(sourceStack, initialStack);
    } else if (index == 1 || index == 0) {  // fuel, input
      if (!this.mergeItemStack(sourceStack, 3, 39, false)) {
        return ItemStack.EMPTY;
      }
    } else {
      if (this.func_217057_a(sourceStack)) {
        if (!this.mergeItemStack(sourceStack, 0, 1, false)) {
          return ItemStack.EMPTY;
        }
      } else if (this.isFuel(sourceStack)) {
        if (!this.mergeItemStack(sourceStack, 1, 2, false)) {
          return ItemStack.EMPTY;
        }
      } else if (index >= 3 && index < 30) {
        if (!this.mergeItemStack(sourceStack, 30, 39, false)) {
          return ItemStack.EMPTY;
        }
      } else if (index >= 30 && index < 39 && !this.mergeItemStack(sourceStack, 3, 30, false)) {
        return ItemStack.EMPTY;
      }
    }

    if (sourceStack.isEmpty()) {
      slot.putStack(ItemStack.EMPTY);
    } else {
      slot.onSlotChanged();
    }

    if (sourceStack.getCount() == initialStack.getCount()) {
      return ItemStack.EMPTY;
    }

    slot.onTake(playerIn, sourceStack);

    return initialStack;
  }


	/* Client Synchronization */

	// This is where you check if any values have changed and if so send an update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match the ID in sendWindowProperty with the value in
	//   updateProgressBar()
	// The progress bar values are restricted to shorts.  If you have a larger value (eg int), it's not a good idea to try and split it
	//   up into two shorts because the progress bar values are sent independently, and unless you add synchronisation logic at the
	//   receiving side, your int value will be wrong until the second short arrives.  Use a custom packet instead.
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged [] = new boolean[tileInventoryFurnace.getFieldCount()];
		if (cachedFields == null) {
			cachedFields = new int[tileInventoryFurnace.getFieldCount()];
			allFieldsHaveChanged = true;
		}
		for (int i = 0; i < cachedFields.length; ++i) {
			if (allFieldsHaveChanged || cachedFields[i] != tileInventoryFurnace.getField(i)) {
				cachedFields[i] = tileInventoryFurnace.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		// go through the list of listeners (players using this container) and update them if necessary
    for (IContainerListener listener : this.listeners) {
			for (int fieldID = 0; fieldID < tileInventoryFurnace.getFieldCount(); ++fieldID) {
				if (fieldHasChanged[fieldID]) {
					// Note that although sendWindowProperty takes 2 ints on a server these are truncated to shorts
          listener.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
        }
			}
		}
	}

	// Called when a progress bar update is received from the server. The two values (id and data) are the same two
	// values given to sendWindowProperty.  In this case we are using fields so we just pass them to the tileEntity.
	@OnlyIn(Dist.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileInventoryFurnace.setField(id, data);
	}

	// SlotFuel is a slot for fuel item
	public class SlotFuel extends Slot {
		public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileEntityFurnace.isItemValidForFuelSlot(stack);
		}
	}

	// SlotSmeltableInput is a slot for input item
	public class SlotSmeltableInput extends Slot {
		public SlotSmeltableInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileEntityFurnace.isItemValidForInputSlot(stack);
		}
	}

	// SlotOutput is a slot that will not accept any item
	public class SlotOutput extends Slot {
		public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileEntityFurnace.isItemValidForOutputSlot(stack);
		}
	}

  private FurnaceZoneContents inputZoneContents;
  private FurnaceZoneContents outputZoneContents;
  private FurnaceZoneContents fuelZoneContents;
  private static final Logger LOGGER = LogManager.getLogger();

}
