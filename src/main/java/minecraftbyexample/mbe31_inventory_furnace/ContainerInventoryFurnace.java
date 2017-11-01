package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
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
 * you add the slots holding items. It is also used to send server side data such as progress bars to the client
 * for use in guis
 */
public class ContainerInventoryFurnace extends Container {

	// Stores the tile entity instance for later use
	private TileInventoryFurnace tileInventoryFurnace;

	// These store cache values, used by the server to only update the client side tile entity when values have changed
	private int [] cachedFields;

	// must assign a slot index to each of the slots used by the GUI.
	// For this container, we can see the furnace fuel, input, and output slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container using addSlotToContainer(), it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 39 = fuel slots (tileEntity 0 - 3)
	//  40 - 44 = input slots (tileEntity 4 - 8)
	//  45 - 49 = output slots (tileEntity 9 - 13)

	private final int HOTBAR_SLOT_COUNT = 9;
	private final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	public final int FUEL_SLOTS_COUNT = 4;
	public final int INPUT_SLOTS_COUNT = 5;
	public final int OUTPUT_SLOTS_COUNT = 5;
	public final int FURNACE_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	// slot index is the unique index for all slots in this container i.e. 0 - 35 for invPlayer then 36 - 49 for tileInventoryFurnace
	private final int VANILLA_FIRST_SLOT_INDEX = 0;
	private final int FIRST_FUEL_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private final int FIRST_INPUT_SLOT_INDEX = FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT;
	private final int FIRST_OUTPUT_SLOT_INDEX = FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;

	// slot number is the slot number within each component; i.e. invPlayer slots 0 - 35, and tileInventoryFurnace slots 0 - 14
	private final int FIRST_FUEL_SLOT_NUMBER = 0;
	private final int FIRST_INPUT_SLOT_NUMBER = FIRST_FUEL_SLOT_NUMBER + FUEL_SLOTS_COUNT;
	private final int FIRST_OUTPUT_SLOT_NUMBER = FIRST_INPUT_SLOT_NUMBER + INPUT_SLOTS_COUNT;

	public ContainerInventoryFurnace(InventoryPlayer invPlayer, TileInventoryFurnace tileInventoryFurnace) {
		this.tileInventoryFurnace = tileInventoryFurnace;

		final int SLOT_X_SPACING = 18;
		final int SLOT_Y_SPACING = 18;
		final int HOTBAR_XPOS = 8;
		final int HOTBAR_YPOS = 183;
		// Add the players hotbar to the gui - the [xpos, ypos] location of each item
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlotToContainer(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}

		final int PLAYER_INVENTORY_XPOS = 8;
		final int PLAYER_INVENTORY_YPOS = 125;
		// Add the rest of the players inventory to the gui
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlotToContainer(new Slot(invPlayer, slotNumber,  xpos, ypos));
			}
		}

		final int FUEL_SLOTS_XPOS = 53;
		final int FUEL_SLOTS_YPOS = 96;
		// Add the tile fuel slots
		for (int x = 0; x < FUEL_SLOTS_COUNT; x++) {
			int slotNumber = x + FIRST_FUEL_SLOT_NUMBER;
			addSlotToContainer(new SlotFuel(tileInventoryFurnace, slotNumber, FUEL_SLOTS_XPOS + SLOT_X_SPACING * x, FUEL_SLOTS_YPOS));
		}

		final int INPUT_SLOTS_XPOS = 26;
		final int INPUT_SLOTS_YPOS = 24;
		// Add the tile input slots
		for (int y = 0; y < INPUT_SLOTS_COUNT; y++) {
			int slotNumber = y + FIRST_INPUT_SLOT_NUMBER;
			addSlotToContainer(new SlotSmeltableInput(tileInventoryFurnace, slotNumber, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS+ SLOT_Y_SPACING * y));
		}

		final int OUTPUT_SLOTS_XPOS = 134;
		final int OUTPUT_SLOTS_YPOS = 24;
		// Add the tile output slots
		for (int y = 0; y < OUTPUT_SLOTS_COUNT; y++) {
			int slotNumber = y + FIRST_OUTPUT_SLOT_NUMBER;
			addSlotToContainer(new SlotOutput(tileInventoryFurnace, slotNumber, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS + SLOT_Y_SPACING * y));
		}
	}

	// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileInventoryFurnace.isUsableByPlayer(player);
	}

	// This is where you specify what happens when a player shift clicks a slot in the gui
	//  (when you shift click a slot in the TileEntity Inventory, it moves it to the first available position in the hotbar and/or
	//    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
	//    position in the TileEntity inventory - either input or fuel as appropriate for the item you clicked)
	// At the very least you must override this and return EMPTY_ITEM or the game will crash when the player shift clicks a slot
	// returns EMPTY_ITEM if the source slot is empty, or if none of the source slot items could be moved.
	//   otherwise, returns a copy of the source stack
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
	{
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into one of the furnace slots
			// If the stack is smeltable try to merge merge the stack into the input slots
			if (!TileInventoryFurnace.getSmeltingResultForItem(sourceStack).isEmpty()){  //isEmptyItem
				if (!mergeItemStack(sourceStack, FIRST_INPUT_SLOT_INDEX, FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT, false)){
					return ItemStack.EMPTY;  //EMPTY_ITEM;
				}
			}	else if (TileInventoryFurnace.getItemBurnTime(sourceStack) > 0) {
				if (!mergeItemStack(sourceStack, FIRST_FUEL_SLOT_INDEX, FIRST_FUEL_SLOT_INDEX + FUEL_SLOTS_COUNT, true)) {
					// Setting the boolean to true places the stack in the bottom slot first
					return ItemStack.EMPTY;  //EMPTY_ITEM;
				}
			}	else {
				return ItemStack.EMPTY;  //EMPTY_ITEM;
			}
		} else if (sourceSlotIndex >= FIRST_FUEL_SLOT_INDEX && sourceSlotIndex < FIRST_FUEL_SLOT_INDEX + FURNACE_SLOTS_COUNT) {
			// This is a furnace slot so merge the stack into the players inventory: try the hotbar first and then the main inventory
			//   because the main inventory slots are immediately after the hotbar slots, we can just merge with a single call
			if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;  //EMPTY_ITEM;
			}
		} else {
			System.err.print("Invalid slotIndex:" + sourceSlotIndex);
			return ItemStack.EMPTY;  //EMPTY_ITEM;
		}

		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {  //getStackSize()
			sourceSlot.putStack(ItemStack.EMPTY);  // Empty Item
		} else {
			sourceSlot.onSlotChanged();
		}

		sourceSlot.onTake(player, sourceStack);  // onPickupFromSlot()
		return copyOfSourceStack;
	}

	/* Client Synchronization */

	// This is where you check if any values have changed and if so send an update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match the ID in sendProgressBarUpdate with the value in
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
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
          listener.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}

	// Called when a progress bar update is received from the server. The two values (id and data) are the same two
	// values given to sendProgressBarUpdate.  In this case we are using fields so we just pass them to the tileEntity.
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileInventoryFurnace.setField(id, data);
	}

	// SlotFuel is a slot for fuel items
	public class SlotFuel extends Slot {
		public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileInventoryFurnace.isItemValidForFuelSlot(stack);
		}
	}

	// SlotSmeltableInput is a slot for input items
	public class SlotSmeltableInput extends Slot {
		public SlotSmeltableInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileInventoryFurnace.isItemValidForInputSlot(stack);
		}
	}

	// SlotOutput is a slot that will not accept any items
	public class SlotOutput extends Slot {
		public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileInventoryFurnace.isItemValidForOutputSlot(stack);
		}
	}
}
