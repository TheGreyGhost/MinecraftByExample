package minecraftbyexample.mbe30_inventory_basic;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * BlockInventoryBasic is a simple inventory capable of storing 9 item stacks. The block itself doesn't do much more
 * then any regular block except create a tile entity when placed, open a gui when right clicked and drop tne
 * inventory's contents when harvested. The actual storage is handled by the tile entity.
 */

//Note that in 1.10.*, extending BlockContainer can cause rendering problems if you don't extend getRenderType()
// If you don't want to extend BlockContainer, make sure to add the tile entity manually,
//   using hasTileEntity() and createTileEntity().  See BlockContainer for a couple of other important methods you may
//  need to implement.

public class BlockInventoryBasic extends BlockContainer
{   
	public BlockInventoryBasic()
	{
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);     // the block will appear on the Blocks tab.
	}

  /**
   * Create the Tile Entity for this block.
   * If your block doesn't extend BlockContainer, use createTileEntity(World worldIn, IBlockState state) instead
   * @param worldIn
   * @param meta
   * @return
   */

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityInventoryBasic();
  }

  // not needed if your block implements ITileEntityProvider (in this case implemented by BlockContainer), but it
  //  doesn't hurt to include it anyway...
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	// Called when the block is right clicked
	// In this block it is used to open the blocks gui when right clicked by a player
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
																	EnumFacing side, float hitX, float hitY, float hitZ) {
		// Uses the gui handler registered to your mod to open the gui for the given gui id
		// open on the server side only  (not sure why you shouldn't open client side too... vanilla doesn't, so we better not either)
		if (worldIn.isRemote) return true;

		playerIn.openGui(MinecraftByExample.instance, GuiHandlerMBE30.getGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	// This is where you can do something when the block is broken. In this case drop the inventory's contents
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		IInventory inventory = worldIn.getTileEntity(pos) instanceof IInventory ? (IInventory)worldIn.getTileEntity(pos) : null;

		if (inventory != null){
			// For each slot in the inventory
			for (int i = 0; i < inventory.getSizeInventory(); i++){
				// If the slot is not empty
				if (!inventory.getStackInSlot(i).isEmpty())  // isEmpty
				{
					// Create a new entity item with the item stack in the slot
					EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory.getStackInSlot(i));

					// Apply some random motion to the item
					float multiplier = 0.1f;
					float motionX = worldIn.rand.nextFloat() - 0.5f;
					float motionY = worldIn.rand.nextFloat() - 0.5f;
					float motionZ = worldIn.rand.nextFloat() - 0.5f;

					item.motionX = motionX * multiplier;
					item.motionY = motionY * multiplier;
					item.motionZ = motionZ * multiplier;

					// Spawn the item in the world
					worldIn.spawnEntity(item);
				}
			}

			// Clear the inventory so nothing else (such as another mod) can do anything with the items
			inventory.clear();
		}

		// Super MUST be called last because it removes the tile entity
		super.breakBlock(worldIn, pos, state);
	}

	//---------------------------------------------------------

	// the block is smaller than a full cube, specify dimensions here
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(1/16.0F, 0, 1/16.0F, 15/16.0F, 8/16.0F, 15/16.0F);
	}

	// the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks, also by
	// (eg) wall or fence to control whether the fence joins itself to this block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

  // render using a BakedModel (mbe30_inventory_basic.json --> mbe30_inventory_basic_model.json)
  // required because the default (super method) is INVISIBLE for BlockContainers.
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }


}
