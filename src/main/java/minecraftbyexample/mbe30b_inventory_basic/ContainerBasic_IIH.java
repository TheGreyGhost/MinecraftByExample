package minecraftbyexample.mbe30b_inventory_basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * The container is used to link the client side gui to the server side inventory and it is where
 * you add the slots to your gui. It can also be used to sync server side data with the client but
 * that will be covered in a later tutorial
 *
 * Vanilla Containers use IInventory to communicate with the parent TileEntity:
 *  markDirty(); isUsableByPlayer(); openInventory(); closeInventory();
 * For this example, we only need markDirty() and isUsableByPlayer().  I've chosen to implement these as callback functions
 *   (lambdas) because I think it's clearer than providing an Optional<TileEntityBasic>, but it could easily be done that
 *   way as well, because the functions are only needed on the server side, when the TileEntity is available.
 *   On the client side, there is no TileEntity available.
 */
public class ContainerBasic_IIH extends Container {

  public static ContainerBasic_IIH createContainerServerSide(int windowID, PlayerInventory playerInventory, ItemStackHandler chestContents,
                                                             Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
                                                             Notify markDirtyNotificationLambda) {
    ContainerBasic_IIH retval = new ContainerBasic_IIH(windowID, playerInventory, chestContents);
    retval.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
    retval.markDirtyNotificationLambda = markDirtyNotificationLambda;
    return retval;
  }

  public static ContainerBasic_IIH createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
    //  don't need extraData for this example; if you want you can use it to provide extra information from the server, that you can use
    //  when creating the client container
    //  eg String detailedDescription = extraData.readString(128);
//    ChestContents_IIH chestContents = ChestContents_IIH.createForClientSideContainer(TileEntityInventoryBasic_IIH.NUMBER_OF_SLOTS);

    // on the client side there is no parent TileEntity to communicate with, so we:
    // 1) use a dummy inventory; and
    // 2) use the default "do nothing" lambda functions for canPlayerAccessInventory and markDirty

    ItemStackHandler chestContents = new ItemStackHandler(TileEntityInventoryBasic_IIH.NUMBER_OF_SLOTS);
    return new ContainerBasic_IIH(windowID, playerInventory, chestContents);
  }

	// must assign a slot number to each of the slots used by the GUI.
	// For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)

	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int TE_INVENTORY_SLOT_COUNT = TileEntityInventoryBasic_IIH.NUMBER_OF_SLOTS;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

  public static final int TILE_INVENTORY_YPOS = 20;  // the ContainerScreenBasic needs to know these so it can tell where to draw the Titles
  public static final int PLAYER_INVENTORY_YPOS = 51;

  /**
   * Creates a container suitable for server side or client side
   * @param windowID ID of the container
   * @param playerInventory the inventory of the player
   * @param chestContents the inventory stored in the chest
   */
	private ContainerBasic_IIH(int windowID, PlayerInventory playerInventory, ItemStackHandler chestContents) {
	  super(StartupCommon.containerTypeContainerBasic_IIH, windowID);
    if (StartupCommon.containerTypeContainerBasic_IIH == null)
      throw new IllegalStateException("Must initialise containerBasicContainerType before constructing a ContainerBasic!");

    this.chestContents = chestContents;

		final int SLOT_X_SPACING = 18;
    final int SLOT_Y_SPACING = 18;
		final int HOTBAR_XPOS = 8;
		final int HOTBAR_YPOS = 109;
		// Add the players hotbar to the gui - the [xpos, ypos] location of each item
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlot(new Slot(playerInventory, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}

		final int PLAYER_INVENTORY_XPOS = 8;
		// Add the rest of the player's inventory to the gui
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlot(new Slot(playerInventory, slotNumber,  xpos, ypos));
			}
		}

		if (TE_INVENTORY_SLOT_COUNT != chestContents.getSlots()) {
			LOGGER.warn("Mismatched slot count in ContainerBasic(" + TE_INVENTORY_SLOT_COUNT
												  + ") and TileInventory (" + chestContents.getSlots()+")");
		}
		final int TILE_INVENTORY_XPOS = 8;
		// Add the tile inventory container to the gui
		for (int x = 0; x < TE_INVENTORY_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlot(new SlotItemHandlerMBE(chestContents, slotNumber, TILE_INVENTORY_XPOS + SLOT_X_SPACING * x, TILE_INVENTORY_YPOS));
		}
	}

	// Vanilla calls this method every tick to make sure the player is still able to access the inventory, and if not closes the gui
  // Called on the SERVER side only
	@Override
	public boolean canInteractWith(PlayerEntity playerEntity)
	{
    // This is typically a check that the player is within 8 blocks of the container.
    //  Some containers perform it using just the block placement:
    //  return isWithinUsableDistance(this.iWorldPosCallable, playerIn, Blocks.MYBLOCK); eg see BeaconContainer
    //  where iWorldPosCallable is a lambda that retrieves the blockstate at a particular world blockpos
    // for other containers, it defers to the IInventory provided to the Container (i.e. the TileEntity) which does the same
    //  calculation
    // return this.furnaceInventory.isUsableByPlayer(playerEntity);
    // Sometimes it perform an additional check (eg for EnderChests - the player owns the chest)

    // in this case - ask our parent tileEntity
    return canPlayerAccessInventoryLambda.test(playerEntity);
	}

	// This is where you specify what happens when a player shift clicks a slot in the gui
	//  (when you shift click a slot in the TileEntity Inventory, it moves it to the first available position in the hotbar and/or
	//    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
	//    position in the TileEntity inventory)
	// At the very least you must override this and return ItemStack.EMPTY or the game will crash when the player shift clicks a slot
	// returns ItemStack.EMPTY if the source slot is empty, or if none of the the source slot item could be moved
	//   otherwise, returns a copy of the source stack
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerEntity, int sourceSlotIndex)
	{
		Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)){
				return ItemStack.EMPTY;  // EMPTY_ITEM
			}
		} else if (sourceSlotIndex >= TE_INVENTORY_FIRST_SLOT_INDEX && sourceSlotIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
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

		sourceSlot.onTake(playerEntity, sourceStack);
		return copyOfSourceStack;
	}

	// pass the close container message to the parent inventory (not strictly needed for this example)
	//  see ContainerChest and TileEntityChest - used to animate the lid when no players are accessing the chest any more
	@Override
	public void onContainerClosed(PlayerEntity playerIn)
	{
		super.onContainerClosed(playerIn);
	}

    // ----- Methods used to inform the parent tile entity that something has happened to the contents
    //  you can make direct calls to the parent if you like, I've used lambdas because I think it shows the separation
    //   of responsibilities more clearly.

    @FunctionalInterface
    public interface Notify {   // Some folks use Runnable, but I prefer not to use it for non-thread-related tasks
      void invoke();
    }

    public void markDirty() {
      markDirtyNotificationLambda.invoke();
    }

    public void openInventory(PlayerEntity player) {
      openInventoryNotificationLambda.invoke();
    }

    public void closeInventory(PlayerEntity player) {
      closeInventoryNotificationLambda.invoke();
    }

    // the function that the container should call in order to decide if the
    // given player can access the container's Inventory or not.  Only valid server side
    //  default is "true".
    private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x-> true;

    // the function that the container should call in order to tell the parent TileEntity that the
    // contents of its inventory have been changed.
    // default is "do nothing"
    private Notify markDirtyNotificationLambda = ()->{};

    // the function that the container should call in order to tell the parent TileEntity that the
    // container has been opened by a player (eg so that the chest can animate its lid being opened)
    // default is "do nothing"
    private Notify openInventoryNotificationLambda = ()->{};

    // the function that the container should call in order to tell the parent TileEntity that the
    // container has been closed by a player
    // default is "do nothing"
    private Notify closeInventoryNotificationLambda = ()->{};

    private ItemStackHandler chestContents;
  private static final Logger LOGGER = LogManager.getLogger();
}
