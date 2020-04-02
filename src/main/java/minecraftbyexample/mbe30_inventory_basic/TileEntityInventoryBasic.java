package minecraftbyexample.mbe30_inventory_basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * This is a simple tile entity implementing IInventory that can store 9 item stacks
 */
public class TileEntityInventoryBasic extends TileEntity implements INamedContainerProvider {
	// Create and initialize the item variable that will store store the item
	private final int NUMBER_OF_SLOTS = 9;
//	private ItemStack[] itemStacks;

  private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS);

	public TileEntityInventoryBasic()
	{
    super(StartupCommon.tileEntityInventoryBasic);
	}

	public ItemStackHandler getItemStackHandler() {return itemStackHandler;}


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

	private static final String INVENTORY_NBT_TAG = "contents";

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, it saves the itemstacks stored in the container
	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound)
	{
		super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
    CompoundNBT inventoryNBT = itemStackHandler.serializeNBT();
    parentNBTTagCompound.put(INVENTORY_NBT_TAG, inventoryNBT);
		return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in write
	@Override
	public void read(CompoundNBT parentNBTTagCompound)
	{
		super.read(parentNBTTagCompound); // The super call is required to save and load the tiles location
    CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(INVENTORY_NBT_TAG);
    itemStackHandler.deserializeNBT(inventoryNBT);
    if (itemStackHandler.getSlots() != NUMBER_OF_SLOTS)
      throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
	}

	// will add a key for this container to the lang file so we can name it in the GUI
	@Override
	public String getName() {
		return "container.mbe30_inventory_basic.name";
	}

	// standard code to look up what the human-readable name is
	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName());
	}

}
