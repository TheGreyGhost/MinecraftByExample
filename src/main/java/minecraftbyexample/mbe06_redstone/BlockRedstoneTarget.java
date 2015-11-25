package minecraftbyexample.mbe06_redstone;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 24/11/2015
 *
 * BlockRedstoneTarget is designed to be hung on a wall.  When an arrow is fired into the target, it emits strong
 *   power into the wall.  The power level depends on which ring of the target the arrow is stuck in:
 *     15 for innermost circle, down to 0 for no arrow (or stuck in the wood)
 * For background information on blocks see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 */
public class BlockRedstoneTarget extends Block
{
  public BlockRedstoneTarget()
  {
    super(Material.wood);
    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
  }

  //----- methods related to redstone

//  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
//  {
//    if (!worldIn.isRemote)
//    {
//      if (((Boolean)state.getValue(POWERED)).booleanValue())
//      {
//        if (this.wooden)
//        {
//          this.checkForArrows(worldIn, pos, state);
//        }
//        else
//        {
//          worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
//          this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
//          worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
//          worldIn.markBlockRangeForRenderUpdate(pos, pos);
//        }
//      }
//    }
//  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
  {
    if (!worldIn.isRemote) {
      if (this.wooden)
      {
        if (!((Boolean)state.getValue(POWERED)).booleanValue())
        {
          this.checkForArrows(worldIn, pos, state);
        }
      }
    }
  }

  to do: find intersecting entities. schedule update to pop arrow out.

//  private void checkForArrows(World worldIn, BlockPos pos, IBlockState state)
//  {
//    this.updateBlockBounds(state);
//    List list = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ));
//    boolean flag = !list.isEmpty();
//    boolean flag1 = ((Boolean)state.getValue(POWERED)).booleanValue();
//
//    if (flag && !flag1)
//    {
//      worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)));
//      this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
//      worldIn.markBlockRangeForRenderUpdate(pos, pos);
//      worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
//    }
//
//    if (!flag && flag1)
//    {
//      worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
//      this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
//      worldIn.markBlockRangeForRenderUpdate(pos, pos);
//      worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
//    }
//
//    if (flag)
//    {
//      worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
//    }
//  }

  // ---- methods to control placement of the target (must be on a solid wall)

  // When a neighbour changes - check if the supporting wall has been demolished
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    if (!worldIn.isRemote) { // server side only
      EnumFacing enumfacing = (EnumFacing) state.getValue(PROPERTYFACING);
      EnumFacing directionOfNeighbour = enumfacing.getOpposite();
      if (!adjacentBlockIsASuitableSupport(worldIn, pos, directionOfNeighbour)) {
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
      }
    }
  }

  /**
   * Can we place the block at this location
   *
   * @param worldIn
   * @param thisBlockPos    the position of this block (not the neighbour)
   * @param faceOfNeighbour the face of the neighbour that is adjacent to this block.  If I am facing east, with a stone
   *                        block to the east of me, and I click on the westward-pointing face of the block,
   *                        faceOfNeighbour is west
   * @return true if the block can be placed here
   */
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos thisBlockPos, EnumFacing faceOfNeighbour)
  {
    System.out.println("canPlaceBlockOnSide:" + thisBlockPos + "; " + faceOfNeighbour);
    EnumFacing directionOfNeighbour = faceOfNeighbour.getOpposite();
    return adjacentBlockIsASuitableSupport(worldIn, thisBlockPos, directionOfNeighbour);
  }

  // Is the neighbouring block in the given direction suitable for mounting the target onto?
  private boolean adjacentBlockIsASuitableSupport(World world, BlockPos thisPos, EnumFacing directionOfNeighbour)
  {
    BlockPos neighbourPos = thisPos.offset(directionOfNeighbour);
    EnumFacing neighbourSide = directionOfNeighbour.getOpposite();
    boolean DEFAULT_SOLID_VALUE = false;
    return world.isSideSolid(neighbourPos, neighbourSide, DEFAULT_SOLID_VALUE);
  }

  // Create the appropriate state for the block being placed - in this case, figure out which way the target is facing
  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos thisBlockPos, EnumFacing faceOfNeighbour,
                                   float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    EnumFacing directionTargetIsPointing = faceOfNeighbour;

    return this.getDefaultState().withProperty(PROPERTYFACING, directionTargetIsPointing);
  }


  //--- methods related to the appearance of the block

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isOpaqueCube()
  {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isFullCube()
  {
    return false;
  }

  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public int getRenderType()
  {
    return 3;
  }

  // The block bounds (used for collision and for outlining the block) depend on which way the block is facing
  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
  {
    IBlockState blockState = worldIn.getBlockState(pos);
    updateBlockBounds(blockState);
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
  {
    this.updateBlockBounds(state);
    return super.getCollisionBoundingBox(worldIn, pos, state);
  }

  // update the block's bounds based on its new state
  private void updateBlockBounds(IBlockState newState)
  {
    EnumFacing facing = (EnumFacing) newState.getValue(PROPERTYFACING);

    switch (facing) {
      case NORTH: {
        this.setBlockBoundsInPixels(0, 0, 15, 16, 16, 16);
        break;
      }
      case WEST: {
        this.setBlockBoundsInPixels(15, 0, 0, 16, 16, 16);
        break;
      }
      case EAST: {
        this.setBlockBoundsInPixels(0, 0, 0, 1, 16, 16);
        break;
      }
      case SOUTH: {
        this.setBlockBoundsInPixels(0, 0, 0, 16, 16, 1);
        break;
      }
    }
  }

  private void setBlockBoundsInPixels(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
  {
    final float PIXEL_WIDTH = 1.0F / 16.0F;
    this.setBlockBounds(minX * PIXEL_WIDTH, minY * PIXEL_WIDTH, minZ * PIXEL_WIDTH,
                               maxX * PIXEL_WIDTH, maxY * PIXEL_WIDTH, maxZ * PIXEL_WIDTH);
  }
  // ---------methods related to storing information about the block (which way it's facing)

  // BlockRedstoneTarget has only one property:
  //PROPERTYFACING for which way the target points (east, west, north, south).  EnumFacing is a standard used by vanilla for a number of blocks.
  //    eg EAST means that the red and white rings on the target are pointing east
  //
  public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

  // getStateFromMeta, getMetaFromState are used to interconvert between the block's property values and
  //   the stored metadata (which must be an integer in the range 0 - 15 inclusive)
  // The property is encoded as:
  // - lower two bits = facing direction (i.e. 0, 1, 2, 3)
  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    return this.getDefaultState().withProperty(PROPERTYFACING, facing);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    EnumFacing facing = (EnumFacing)state.getValue(PROPERTYFACING);

    int facingbits = facing.getHorizontalIndex();
    return facingbits;
  }

  // this method isn't required if your properties only depend on the stored metadata.
  // it is required if:
  // 1) you are making a multiblock which stores information in other blocks eg BlockBed, BlockDoor
  // 2) your block's state depends on other neighbours (eg BlockFence)
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return state;
  }

  // necessary to define which properties your blocks use
  // will also affect the variants listed in the blockstates model file
  @Override
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] {PROPERTYFACING});
  }
}
