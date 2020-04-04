package minecraftbyexample.mbe30_inventory_basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by TGG on 4/04/2020.
 *
 * This class is used to encapsulate the contents of the chest and provide the link between the parent TileEntity and
 *    the container.
 * 1) stores information about the items in the chest: allows the container to manipulate the data stored in the tile entity
 * 2) provides a way for the container to ask if certain actions are permitted (eg isUsableByPlayer, isItemValidForSlot)
 * 3) provides a way for the container to notify the TileEntity that the container has changed (eg markDirty, openInventory)
 *
 * Typical usage for a TileEntity which needs to store Items:
 * 1) When constructing the TileEntity, create and store a ChestContents using createForTileEntity()
 * 2) In your ContainerType<MyContainer>, create a ChestContents using createForClientSideContainer() and pass it to
 *    the constructor of your client-side container.
 * 3) In your TileEntity write() and read() methods, call the serializeNBT() and deserializeNBT() methods
 * Vanilla and the container code will take care of everything else.
 *
 */

public class ChestContents implements IInventory {

  /**
   * Use this constructor to create a ChestContents which is linked to its parent TileEntity.
   * On the server, this link will be used by the Container to request information and provide notifications to the parent
   * On the client, the link will be unused.
   * There are additional notificationLambdas available; these two are explicitly specified because your TileEntity will
   *   nearly always need to implement at least these two
   * @param size  the max number of ItemStacks in the inventory
   * @param canPlayerAccessInventoryLambda the function that the container should call in order to decide if the given player
   *                                       can access the container's contents not.  Usually, this is a check to see
   *                                       if the player is closer than 8 blocks away.
   * @param markDirtyNotificationLambda  the function that the container should call in order to tell the parent TileEntity
   *                                     that the contents of its inventory have been changed and need to be saved.  Usually,
   *                                     this is TileEntity::markDirty
   * @return the new ChestContents.
   */
  public static ChestContents createForTileEntity(int size,
                                                     Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
                                                     Notify markDirtyNotificationLambda) {
     return new ChestContents(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
  }

  /**
   * Use this constructor to create a ChestContents which is not linked to any parent TileEntity; i.e. is used by
   *   the client side container:
   *   * does not permanently store items
   *   * cannot ask questions/provide notifications to a parent TileEntity
   * @param size  the max number of ItemStacks in the inventory
   * @return the new ChestContents
   */
  public static ChestContents createForClientSideContainer(int size) {
    return new ChestContents(size);
  }

  // ----Methods used to load / save the contents to NBT

  /**
   * Writes the chest contents to a CompoundNBT tag (used to save the contents to disk)
   * @return the tag containing the contents
   */
  public CompoundNBT serializeNBT()  {
    return chestContents.serializeNBT();
  }

  /**
   * Fills the chest contents from the nbt; resizes automatically to fit.  (used to load the contents from disk)
   * @param nbt
   */
  public void deserializeNBT(CompoundNBT nbt)   {
    chestContents.deserializeNBT(nbt);
  }

  //  ------------- linking methods  -------------
  //  The following group of methods are used to establish a link between the parent TileEntity and the chest contents,
  //    so that the container can communicate with the parent TileEntity without having to talk to it directly.
  //  This is important because the link to the TileEntity only exists on the server side.  On the client side, the
  //    container gets a dummy link instead- there is no link to the client TileEntity.  Linking to the client TileEntity
  //    is prohibited because of synchronisation clashes, i.e. vanilla would attempt to synchronise the TileEntity in two
  //    different ways at the same time: via the tileEntity server->client packets and via the container directly poking
  //    around in the inventory contents.
  //  I've used lambdas to make the decoupling more explicit.  You could instead
  //  * provide an Optional TileEntity to the ChestContents constructor (and ignore the markDirty() etc calls), or
  //  * implement IInventory directly in your TileEntity, and construct your client-side container using an Inventory
  //    instead of passing it a TileEntity.  (This is how vanilla does it)
  //

  /**
   * sets the function that the container should call in order to decide if the given player can access the container's
   *   contents not.  The lambda function is only used on the server side
   */
  public void setCanPlayerAccessInventoryLambda(Predicate<PlayerEntity> canPlayerAccessInventoryLambda) {
    this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
  }

  // the function that the container should call in order to tell the parent TileEntity that the
  // contents of its inventory have been changed.
  // default is "do nothing"
  public void setMarkDirtyNotificationLambda(Notify markDirtyNotificationLambda) {
    this.markDirtyNotificationLambda = markDirtyNotificationLambda;
  }

  // the function that the container should call in order to tell the parent TileEntity that the
  // container has been opened by a player (eg so that the chest can animate its lid being opened)
  // default is "do nothing"
  public void setOpenInventoryNotificationLambda(Notify openInventoryNotificationLambda) {
    this.openInventoryNotificationLambda = openInventoryNotificationLambda;
  }

  // the function that the container should call in order to tell the parent TileEntity that the
  // container has been closed by a player
  // default is "do nothing"
  public void setCloseInventoryNotificationLambda(Notify closeInventoryNotificationLambda) {
    this.closeInventoryNotificationLambda = closeInventoryNotificationLambda;
  }

  // ---------- These methods are used by the container to ask whether certain actions are permitted
  //  If you need special behaviour (eg a chest can only be used by a particular player) then either modify this method
  //    or ask the parent TileEntity.

  @Override
  public boolean isUsableByPlayer(PlayerEntity player) {
    return canPlayerAccessInventoryLambda.test(player);  // on the client, this does nothing. on the server, ask our parent TileEntity.
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return chestContents.isItemValid(index, stack);
  }

  // ----- Methods used to inform the parent tile entity that something has happened to the contents
  //  you can make direct calls to the parent if you like, I've used lambdas because I think it shows the separation
  //   of responsibilities more clearly.

  @FunctionalInterface
  public interface Notify {   // Some folks use Runnable, but I prefer not to use it for non-thread-related tasks
    void invoke();
  }

  @Override
  public void markDirty() {
    markDirtyNotificationLambda.invoke();
  }

  @Override
  public void openInventory(PlayerEntity player) {
    openInventoryNotificationLambda.invoke();
  }

  @Override
  public void closeInventory(PlayerEntity player) {
    closeInventoryNotificationLambda.invoke();
  }

  //---------These following methods are called by Vanilla container methods to manipulate the inventory contents ---

  @Override
  public int getSizeInventory() {
    return chestContents.getSlots();
  }

  @Override
  public boolean isEmpty() {
    for (int i = 0; i < chestContents.getSlots(); ++i) {
      if (!chestContents.getStackInSlot(i).isEmpty()) return false;
    }
    return true;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return chestContents.getStackInSlot(index);
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    return chestContents.extractItem(index, count, false);
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    int maxPossibleItemStackSize = chestContents.getSlotLimit(index);
    return chestContents.extractItem(index, maxPossibleItemStackSize, false);
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    chestContents.setStackInSlot(index, stack);
  }

  @Override
  public void clear() {
    for (int i = 0; i < chestContents.getSlots(); ++i) {
      chestContents.setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  // ---------

  private ChestContents(int size) {
    this.chestContents = new ItemStackHandler(size);
  }

  private ChestContents(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
    this.chestContents = new ItemStackHandler(size);
    this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
    this.markDirtyNotificationLambda = markDirtyNotificationLambda;
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

  private final ItemStackHandler chestContents;
}
