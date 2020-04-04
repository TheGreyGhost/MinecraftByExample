package minecraftbyexample.mbe30_inventory_basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * User: brandon3055 & TGG
 * Date: 06/01/2015
 *
 * This is a simple tile entity that can store 9 ItemStacks
 */
public class TileEntityInventoryBasic extends TileEntity implements INamedContainerProvider {
	// Create and initialize the item variable that will store store the item
	public static final int NUMBER_OF_SLOTS = 9;

	public TileEntityInventoryBasic()
	{
    super(StartupCommon.tileEntityTypeMBE30);
    chestContents = ChestContents.createForTileEntity(NUMBER_OF_SLOTS,
            this::canPlayerAccessInventory, this::markDirty);
	}

	// Return true if the given player is able to use this block. In this case it checks that
	// 1) the world tileentity hasn't been replaced in the meantime, and
	// 2) the player isn't too far away from the centre of the block
	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	private static final String CHESTCONTENTS_INVENTORY_TAG = "contents";

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, it saves the chestContents, which contains the ItemStacks stored in the chest
	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound)
	{
		super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
    CompoundNBT inventoryNBT = chestContents.serializeNBT();
    parentNBTTagCompound.put(CHESTCONTENTS_INVENTORY_TAG, inventoryNBT);
		return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in write
	@Override
	public void read(CompoundNBT parentNBTTagCompound)
	{
		super.read(parentNBTTagCompound); // The super call is required to save and load the tiles location
    CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(CHESTCONTENTS_INVENTORY_TAG);
    chestContents.deserializeNBT(inventoryNBT);
    if (chestContents.getSizeInventory() != NUMBER_OF_SLOTS)
      throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
	}

  /**
   * When this tile entity is destroyed, drop all of its contents into the world
   * @param world
   * @param blockPos
   */
	public void dropAllContents(World world, BlockPos blockPos) {
    InventoryHelper.dropInventoryItems(world, blockPos, chestContents);
  }

	// -------------  The following two methods are used to make the TileEntity perform as a NamedContainerProvider, i.e.
  //  1) Provide a name used when displaying the container, and
  //  2) Creating an instance of container on the server, and linking it to the inventory items stored within the TileEntity

  /**
   *  standard code to look up what the human-readable name is.
   *  Can be useful when the tileentity has a customised name (eg "David's footlocker")
    */
	@Override
	public ITextComponent getDisplayName() {
    return new TranslationTextComponent("container.mbe30_inventory_basic");
	}

  /**
   * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
   * @param windowID
   * @param playerInventory
   * @param playerEntity
   * @return
   */
  @Nullable
  @Override
  public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return ContainerBasic.createContainerServerSide(windowID, playerInventory, chestContents);
  }

  private final ChestContents chestContents; // holds the ItemStacks in the Chest
}
