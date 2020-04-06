//package minecraftbyexample.mbe31_inventory_furnace;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraftforge.items.ItemStackHandler;
//
//import java.util.function.Predicate;
//
///**
// * Created by TGG on 4/04/2020.
// *
// * This class is used to encapsulate the contents of the furnace and provide the link between the parent TileEntity and
// *    the container.
// * 1) stores information about the items in the furnace: allows the container to manipulate the data stored in the tile entity
// * 2) provides a way for the container to ask if certain actions are permitted (eg isUsableByPlayer, isItemValidForSlot)
// * 3) provides a way for the container to notify the TileEntity that the container has changed (eg markDirty, openInventory)
// *
// * Some of the logic for smelting is also in this class:
// * 1) calculating how much burn time is in etsets
// *
// *
// * Typical usage for a TileEntity which needs to store ItemStacks:
// * 1) When constructing the TileEntity, create and store a furnaceContents using createForTileEntity()
// * 2) In your ContainerType<MyContainer>, create a furnaceContents using createForClientSideContainer() and pass it to
// *    the constructor of your client-side container.
// * 3) In your TileEntity write() and read() methods, call the serializeNBT() and deserializeNBT() methods
// * Vanilla and the container code will take care of everything else.
// *
// * NB I have split the three types of slots into different ItemStackHandlers to make it clearer how it works.  This isn't
// *   strictly necessary but you will need to be careful not to mix up the different slots and auto-filling (shift-click)
// *
// *
// */
//
//public class FurnaceContentsOLD implements IInventory {
//
//  /**
//   * Use this constructor to create a furnaceContents which is linked to its parent TileEntity.
//   * On the server, this link will be used by the Container to request information and provide notifications to the parent
//   * On the client, the link will be unused.
//   * There are additional notificationLambdas available; these two are explicitly specified because your TileEntity will
//   *   nearly always need to implement at least these two
//   * @param canPlayerAccessInventoryLambda the function that the container should call in order to decide if the given player
//   *                                       can access the container's contents not.  Usually, this is a check to see
//   *                                       if the player is closer than 8 blocks away.
//   * @param markDirtyNotificationLambda  the function that the container should call in order to tell the parent TileEntity
//   *                                     that the contents of its inventory have been changed and need to be saved.  Usually,
//   *                                     this is TileEntity::markDirty
//   * @return the new furnaceContents.
//   */
//  public static FurnaceContentsOLD createForTileEntity(Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
//                                                       Notify markDirtyNotificationLambda) {
//     return new FurnaceContentsOLD(canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
//  }
//
//  /**
//   * Use this constructor to create a furnaceContents which is not linked to any parent TileEntity; i.e. is used by
//   *   the client side container:
//   *   * does not permanently store items
//   *   * cannot ask questions/provide notifications to a parent TileEntity
//   * @return the new furnaceContents
//   */
//  public static FurnaceContentsOLD createForClientSideContainer() {
//    return new FurnaceContentsOLD();
//  }
//
//
//  // ----Methods used to load / save the contents to NBT
//
//  private final String FUEL_SLOTS_NBT = "fuelSlots";
//  private final String INPUT_SLOTS_NBT = "inputSlots";
//  private final String OUTPUT_SLOTS_NBT = "outputSlots";
//
//  /**
//   * Writes the contents of all slots to a CompoundNBT tag (used to save the contents to disk)
//   * @return the tag containing the contents of the slots
//   */
//  public CompoundNBT serializeNBT()  {
//    CompoundNBT allSlotsNBT = new CompoundNBT();
//    allSlotsNBT.put(FUEL_SLOTS_NBT, fuelSlotContents.serializeNBT());
//    allSlotsNBT.put(INPUT_SLOTS_NBT, inputSlotContents.serializeNBT());
//    allSlotsNBT.put(OUTPUT_SLOTS_NBT, outputSlotContents.serializeNBT());
//    return allSlotsNBT;
//  }
//
//  /**
//   * Fills the furnace contents from the nbt; resizes automatically to fit.  (used to load the contents from disk)
//   * @param nbt
//   */
//  public void deserializeNBT(CompoundNBT nbt) {
//    // group x,y,z together under a "testBlockPos" tag
//    CompoundNBT fuelSlotsNBT = nbt.getCompound(FUEL_SLOTS_NBT);
//    CompoundNBT inputSlotsNBT = nbt.getCompound(INPUT_SLOTS_NBT);
//    CompoundNBT outputSlotsNBT = nbt.getCompound(OUTPUT_SLOTS_NBT);
//
//    fuelSlotContents.deserializeNBT(fuelSlotsNBT);
//    inputSlotContents.deserializeNBT(inputSlotsNBT);
//    outputSlotContents.deserializeNBT(outputSlotsNBT);
//  }
//
//  //  ------------- linking methods  -------------
//  //  The following group of methods are used to establish a link between the parent TileEntity and the chest contents,
//  //    so that the container can communicate with the parent TileEntity without having to talk to it directly.
//  //  This is important because the link to the TileEntity only exists on the server side.  On the client side, the
//  //    container gets a dummy link instead- there is no link to the client TileEntity.  Linking to the client TileEntity
//  //    is prohibited because of synchronisation clashes, i.e. vanilla would attempt to synchronise the TileEntity in two
//  //    different ways at the same time: via the tileEntity server->client packets and via the container directly poking
//  //    around in the inventory contents.
//  //  I've used lambdas to make the decoupling more explicit.  You could instead
//  //  * provide an Optional TileEntity to the furnaceContents constructor (and ignore the markDirty() etc calls), or
//  //  * implement IInventory directly in your TileEntity, and construct your client-side container using an Inventory
//  //    instead of passing it a TileEntity.  (This is how vanilla does it)
//  //
//
//  /**
//   * sets the function that the container should call in order to decide if the given player can access the container's
//   *   contents not.  The lambda function is only used on the server side
//   */
//  public void setCanPlayerAccessInventoryLambda(Predicate<PlayerEntity> canPlayerAccessInventoryLambda) {
//    this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
//  }
//
//  // the function that the container should call in order to tell the parent TileEntity that the
//  // contents of its inventory have been changed.
//  // default is "do nothing"
//  public void setMarkDirtyNotificationLambda(Notify markDirtyNotificationLambda) {
//    this.markDirtyNotificationLambda = markDirtyNotificationLambda;
//  }
//
//  // the function that the container should call in order to tell the parent TileEntity that the
//  // container has been opened by a player (eg so that the chest can animate its lid being opened)
//  // default is "do nothing"
//  public void setOpenInventoryNotificationLambda(Notify openInventoryNotificationLambda) {
//    this.openInventoryNotificationLambda = openInventoryNotificationLambda;
//  }
//
//  // the function that the container should call in order to tell the parent TileEntity that the
//  // container has been closed by a player
//  // default is "do nothing"
//  public void setCloseInventoryNotificationLambda(Notify closeInventoryNotificationLambda) {
//    this.closeInventoryNotificationLambda = closeInventoryNotificationLambda;
//  }
//
//  // ---------- These methods are used by the container to ask whether certain actions are permitted
//  //  If you need special behaviour (eg a chest can only be used by a particular player) then either modify this method
//  //    or ask the parent TileEntity.
//
//  @Override
//  public boolean isUsableByPlayer(PlayerEntity player) {
//    return canPlayerAccessInventoryLambda.test(player);  // on the client, this does nothing. on the server, ask our parent TileEntity.
//  }
//
//  /**
//   * You can add custom logic for
//   * @param index
//   * @param stack
//   * @return
//   */
//  @Override
//  public boolean isItemValidForSlot(int index, ItemStack stack) {
//    ItemStackHandler itemStackHandler = getItemStackHandler(index);
//    int withinHandlerIndex = getWithinHandlerIndex(index);
//    return itemStackHandler.isItemValid(withinHandlerIndex, stack);
//  }
//
//  // ----- Methods used to inform the parent tile entity that something has happened to the contents
//  //  you can make direct calls to the parent if you like, I've used lambdas because I think it shows the separation
//  //   of responsibilities more clearly.
//
//  @FunctionalInterface
//  public interface Notify {   // Some folks use Runnable, but I prefer not to use it for non-thread-related tasks
//    void invoke();
//  }
//
//  @Override
//  public void markDirty() {
//    markDirtyNotificationLambda.invoke();
//  }
//
//  @Override
//  public void openInventory(PlayerEntity player) {
//    openInventoryNotificationLambda.invoke();
//  }
//
//  @Override
//  public void closeInventory(PlayerEntity player) {
//    closeInventoryNotificationLambda.invoke();
//  }
//
//  //---------These following methods are called by Vanilla container methods to manipulate the inventory contents ---
//
//  @Override
//  public int getSizeInventory() {
//    return fuelSlotContents.getSlots() + inputSlotContents.getSlots() + outputSlotContents.getSlots();
//  }
//
//  @Override
//  public boolean isEmpty() {
//    return isEmpty(fuelSlotContents) && isEmpty(inputSlotContents) && isEmpty(outputSlotContents);
//  }
//
//  @Override
//  public ItemStack getStackInSlot(int index) {
//    ItemStackHandler itemStackHandler = getItemStackHandler(index);
//    int withinHandlerIndex = getWithinHandlerIndex(index);
//    return itemStackHandler.getStackInSlot(withinHandlerIndex);
//  }
//
//  @Override
//  public ItemStack decrStackSize(int index, int count) {
//    ItemStackHandler itemStackHandler = getItemStackHandler(index);
//    int withinHandlerIndex = getWithinHandlerIndex(index);
//    return itemStackHandler.extractItem(withinHandlerIndex, count, false);
//  }
//
//  @Override
//  public ItemStack removeStackFromSlot(int index) {
//    ItemStackHandler itemStackHandler = getItemStackHandler(index);
//    int withinHandlerIndex = getWithinHandlerIndex(index);
//    int maxPossibleItemStackSize = itemStackHandler.getSlotLimit(withinHandlerIndex);
//    return itemStackHandler.extractItem(withinHandlerIndex, maxPossibleItemStackSize, false);
//  }
//
//  @Override
//  public void setInventorySlotContents(int index, ItemStack stack) {
//    ItemStackHandler itemStackHandler = getItemStackHandler(index);
//    int withinHandlerIndex = getWithinHandlerIndex(index);
//    itemStackHandler.setStackInSlot(withinHandlerIndex, stack);
//  }
//
//  @Override
//  public void clear() {
//    clearSlotContents(fuelSlotContents);
//    clearSlotContents(inputSlotContents);
//    clearSlotContents(outputSlotContents);
//  }
//
//  private void clearSlotContents(ItemStackHandler itemStackHandler) {
//    for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
//      itemStackHandler.setStackInSlot(i, ItemStack.EMPTY);
//    }
//  }
//
//  private boolean isEmpty(ItemStackHandler itemStackHandler) {
//    for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
//      if (!itemStackHandler.getStackInSlot(i).isEmpty()) return false;
//    }
//    return true;
//  }
//
//  // ---------
//
//  private FurnaceContentsOLD() {
//  }
//
//  private FurnaceContentsOLD(Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
//    this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
//    this.markDirtyNotificationLambda = markDirtyNotificationLambda;
//  }
//
//  // the function that the container should call in order to decide if the
//  // given player can access the container's Inventory or not.  Only valid server side
//  //  default is "true".
//  private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x-> true;
//
//  // the function that the container should call in order to tell the parent TileEntity that the
//  // contents of its inventory have been changed.
//  // default is "do nothing"
//  private Notify markDirtyNotificationLambda = ()->{};
//
//  // the function that the container should call in order to tell the parent TileEntity that the
//  // container has been opened by a player (eg so that the chest can animate its lid being opened)
//  // default is "do nothing"
//  private Notify openInventoryNotificationLambda = ()->{};
//
//  // the function that the container should call in order to tell the parent TileEntity that the
//  // container has been closed by a player
//  // default is "do nothing"
//  private Notify closeInventoryNotificationLambda = ()->{};
//
//  public static final int FUEL_SLOTS_COUNT = 4;
//  public static final int INPUT_SLOTS_COUNT = 5;
//  public static final int OUTPUT_SLOTS_COUNT = 5;
//
//  private final ItemStackHandler fuelSlotContents = new ItemStackHandler(FUEL_SLOTS_COUNT);
//  private final ItemStackHandler inputSlotContents = new ItemStackHandler(INPUT_SLOTS_COUNT);
//  private final ItemStackHandler outputSlotContents = new ItemStackHandler(OUTPUT_SLOTS_COUNT);
//
//  // Order of indices = FUEL_SLOTS then INPUT_SLOTS then OUTPUT_SLOTS
//  private int getWithinHandlerIndex(int index) {
//    if (index < 0) throw new IndexOutOfBoundsException("index out of range:"+index);
//    if (index < FUEL_SLOTS_COUNT) return index;
//    index -= FUEL_SLOTS_COUNT;
//    if (index < INPUT_SLOTS_COUNT) return index;
//    index -= INPUT_SLOTS_COUNT;
//    if (index < OUTPUT_SLOTS_COUNT) return index;
//    throw new IndexOutOfBoundsException("index out of range:"+index);
//  }
//
//  private ItemStackHandler getItemStackHandler(int index) {
//    if (index < 0) throw new IndexOutOfBoundsException("index out of range:"+index);
//    if (index < FUEL_SLOTS_COUNT) return fuelSlotContents;
//    index -= FUEL_SLOTS_COUNT;
//    if (index < INPUT_SLOTS_COUNT) return inputSlotContents;
//    index -= INPUT_SLOTS_COUNT;
//    if (index < OUTPUT_SLOTS_COUNT) return outputSlotContents;
//    throw new IndexOutOfBoundsException("index out of range:"+index);
//  }
//}
