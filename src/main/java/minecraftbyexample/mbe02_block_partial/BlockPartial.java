package minecraftbyexample.mbe02_block_partial;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockPartial uses a model which doesn't occupy the entire 1x1x1m space, and is made up of two pieces.
 * We can walk over it without colliding.
 * For background information on blocks see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 * For a couple of the methods below the Forge guys have marked it as deprecated.  But you still need to override those
 *   "deprecated" block methods.  What they mean is "when you want to find out if a block is (eg) isOpaqueCube(),
 *   don't call block.isOpaqueCube(), call iBlockState.isOpaqueCube() instead".
 * If that doesn't make sense to you yet, don't worry.  Just ignore the "deprecated method" warning.

 */
public class BlockPartial extends Block
{
  public BlockPartial()
  {
    super(Material.ROCK);
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
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
  public boolean isOpaqueCube(IBlockState iBlockState) {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isFullCube(IBlockState iBlockState) {
    return false;
  }

  // render using a BakedModel (mbe02_block_partial.json --> mbe02_block_partial_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }

  // by returning a null collision bounding box we stop the player from colliding with it
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return NULL_AABB;
  }
}
