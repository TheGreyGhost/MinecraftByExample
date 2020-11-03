package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * ContainerFurnace is used to link the client side gui to the server side inventory.  It collates the various different
 * inventories into one place (using Slots)
 * It is also used to send server side data such as progress bars to the client for use in guis
 *
 * Vanilla automatically detects changes in the server side Container (the Slots and the trackedInts) and
 * sends them to the client container.
 */
public class ContainerFurnace extends Container {

  public static ContainerFurnace createContainerServerSide(int windowID, PlayerInventory playerInventory,
                                                           FurnaceZoneContents inputZoneContents,
                                                           FurnaceZoneContents outputZoneContents,
                                                           FurnaceZoneContents fuelZoneContents,
                                                           FurnaceStateData furnaceStateData) {
    return new ContainerFurnace(windowID, playerInventory,
            inputZoneContents, outputZoneContents, fuelZoneContents, furnaceStateData);
  }

  public static ContainerFurnace createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
    //  don't need extraData for this example; if you want you can use it to provide extra information from the server, that you can use
    //  when creating the client container
    //  eg String detailedDescription = extraData.readString(128);
    FurnaceZoneContents inputZoneContents = FurnaceZoneContents.createForClientSideContainer(INPUT_SLOTS_COUNT);
    FurnaceZoneContents outputZoneContents = FurnaceZoneContents.createForClientSideContainer(OUTPUT_SLOTS_COUNT);
    FurnaceZoneContents fuelZoneContents = FurnaceZoneContents.createForClientSideContainer(FUEL_SLOTS_COUNT);
    FurnaceStateData furnaceStateData = new FurnaceStateData();

    // on the client side there is no parent TileEntity to communicate with, so we:
    // 1) use dummy inventories and furnace state data (tracked ints)
    // 2) use "do nothing" lambda functions for canPlayerAccessInventory and markDirty
    return new ContainerFurnace(windowID, playerInventory,
                                inputZoneContents, outputZoneContents, fuelZoneContents, furnaceStateData);
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
  private static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
  private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
	private static final int FIRST_FUEL_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int FIRST_INPUT_SLOT_INDEX = FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT;
	private static final int FIRST_OUTPUT_SLOT_INDEX = FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;

  // gui position of the player inventory grid
  public static final int PLAYER_INVENTORY_XPOS = 8;
  public static final int PLAYER_INVENTORY_YPOS = 125;

  // slot number is the slot number within each component;
  // i.e. invPlayer slots 0 - 35 (hotbar 0 - 8 then main inventory 9 to 35)
  // and furnace: inputZone slots 0 - 4, outputZone slots 0 - 4, fuelZone 0 - 3

	public ContainerFurnace(int windowID, PlayerInventory invPlayer,
            FurnaceZoneContents inputZoneContents,
            FurnaceZoneContents outputZoneContents,
            FurnaceZoneContents fuelZoneContents,
            FurnaceStateData furnaceStateData) {
    super(StartupCommon.containerTypeContainerFurnace, windowID);
    if (StartupCommon.containerTypeContainerFurnace == null)
      throw new IllegalStateException("Must initialise containerTypeContainerFurnace before constructing a ContainerFurnace!");
    this.inputZoneContents = inputZoneContents;
    this.outputZoneContents = outputZoneContents;
    this.fuelZoneContents = fuelZoneContents;
    this.furnaceStateData = furnaceStateData;
    this.world = invPlayer.player.world;

    trackIntArray(furnaceStateData);    // tell vanilla to keep the furnaceStateData synchronised between client and server Containers

		final int SLOT_X_SPACING = 18;
		final int SLOT_Y_SPACING = 18;
		final int HOTBAR_XPOS = 8;
		final int HOTBAR_YPOS = 183;
		// Add the players hotbar to the gui - the [xpos, ypos] location of each item
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlot(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}

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
  //  Code copied & refactored from vanilla furnace AbstractFurnaceContainer
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex)
	{
		Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
    ItemStack sourceItemStack = sourceSlot.getStack();
    ItemStack sourceStackBeforeMerge = sourceItemStack.copy();
    boolean successfulTransfer = false;

    SlotZone sourceZone = SlotZone.getZoneFromIndex(sourceSlotIndex);

    switch (sourceZone) {
      case OUTPUT_ZONE: // taking out of the output zone - try the hotbar first, then main inventory.  fill from the end.
        successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
        if (!successfulTransfer) {
          successfulTransfer = mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, true);
        }
        if (successfulTransfer) {  // removing from output means we have just crafted an item -> need to inform
          sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
        }
        break;

      case INPUT_ZONE:
      case FUEL_ZONE: // taking out of input zone or fuel zone - try player main inv first, then hotbar.  fill from the start
        successfulTransfer = mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, false);
        if (!successfulTransfer) {
          successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
        }
        break;

      case PLAYER_HOTBAR:
      case PLAYER_MAIN_INVENTORY: // taking out of inventory - find the appropriate furnace zone
        if (!TileEntityFurnace.getSmeltingResultForItem(world, sourceItemStack).isEmpty()) { // smeltable -> add to input
          successfulTransfer = mergeInto(SlotZone.INPUT_ZONE, sourceItemStack, false);
        }
        if (!successfulTransfer && TileEntityFurnace.getItemBurnTime(world, sourceItemStack) > 0) { //burnable -> add to fuel from the bottom slot first
          successfulTransfer = mergeInto(SlotZone.FUEL_ZONE, sourceItemStack, true);
        }
        if (!successfulTransfer) {  // didn't fit into furnace; try player main inventory or hotbar
          if (sourceZone == SlotZone.PLAYER_HOTBAR) { // main inventory
            successfulTransfer = mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, false);
          } else {
            successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
          }
        }
        break;

      default:
        throw new IllegalArgumentException("unexpected sourceZone:" + sourceZone);
    }
    if (!successfulTransfer) return ItemStack.EMPTY;

		// If source stack is empty (the entire stack was moved) set slot contents to empty
		if (sourceItemStack.isEmpty()) {
			sourceSlot.putStack(ItemStack.EMPTY);
		} else {
			sourceSlot.onSlotChanged();
		}

		// if source stack is still the same as before the merge, the transfer failed somehow?  not expected.
    if (sourceItemStack.getCount() == sourceStackBeforeMerge.getCount()) {
      return ItemStack.EMPTY;
    }
		sourceSlot.onTake(player, sourceItemStack);
    return sourceStackBeforeMerge;
	}

  /**
   * Try to merge from the given source ItemStack into the given SlotZone.
   * @param destinationZone the zone to merge into
   * @param sourceItemStack the itemstack to merge from
   * @param fillFromEnd if true: try to merge from the end of the zone instead of from the start
   * @return true if a successful transfer occurred
   */
	private boolean mergeInto(SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
	  return mergeItemStack(sourceItemStack, destinationZone.firstIndex, destinationZone.lastIndexPlus1, fillFromEnd);
  }

  // -------- methods used by the ContainerScreen to render parts of the display

  /**
   * Returns the amount of fuel remaining on the currently burning item in the given fuel slot.
   * @fuelSlot the number of the fuel slot (0..3)
   * @return fraction remaining, between 0.0 - 1.0
   */
  public double fractionOfFuelRemaining(int fuelSlot) {
    if (furnaceStateData.burnTimeInitialValues[fuelSlot] <= 0 ) return 0;
    double fraction = furnaceStateData.burnTimeRemainings[fuelSlot] / (double)furnaceStateData.burnTimeInitialValues[fuelSlot];
    return MathHelper.clamp(fraction, 0.0, 1.0);
  }

  /**
   * return the remaining burn time of the fuel in the given slot
   * @param fuelSlot the number of the fuel slot (0..3)
   * @return seconds remaining
   */
  public int secondsOfFuelRemaining(int fuelSlot)	{
    if (furnaceStateData.burnTimeRemainings[fuelSlot] <= 0 ) return 0;
    return furnaceStateData.burnTimeRemainings[fuelSlot] / 20; // 20 ticks per second
  }

  /**
   * Returns the amount of cook time completed on the currently cooking item.
   * @return fraction remaining, between 0 - 1
   */
  public double fractionOfCookTimeComplete() {
    if (furnaceStateData.cookTimeForCompletion == 0) return 0;
    double fraction = furnaceStateData.cookTimeElapsed / (double)furnaceStateData.cookTimeForCompletion;
    return MathHelper.clamp(fraction, 0.0, 1.0);
  }

  // --------- Customise the different slots (in particular - what items they will accept)


	// SlotFuel is a slot for fuel items
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
  private FurnaceStateData furnaceStateData;

  private World world; //needed for some helper methods
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Helper enum to make the code more readable
   */
  private enum SlotZone {
    FUEL_ZONE(FIRST_FUEL_SLOT_INDEX, FUEL_SLOTS_COUNT),
    INPUT_ZONE(FIRST_INPUT_SLOT_INDEX, INPUT_SLOTS_COUNT),
    OUTPUT_ZONE(FIRST_OUTPUT_SLOT_INDEX, OUTPUT_SLOTS_COUNT),
    PLAYER_MAIN_INVENTORY(PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_SLOT_COUNT),
    PLAYER_HOTBAR(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOT_COUNT);

    SlotZone(int firstIndex, int numberOfSlots) {
      this.firstIndex = firstIndex;
      this.slotCount = numberOfSlots;
      this.lastIndexPlus1 = firstIndex + numberOfSlots;
    }

    public final int firstIndex;
    public final int slotCount;
    public final int lastIndexPlus1;

    public static SlotZone getZoneFromIndex(int slotIndex) {
      for (SlotZone slotZone : SlotZone.values()) {
        if (slotIndex >= slotZone.firstIndex && slotIndex < slotZone.lastIndexPlus1) return slotZone;
      }
      throw new IndexOutOfBoundsException("Unexpected slotIndex");
    }
  }
}
