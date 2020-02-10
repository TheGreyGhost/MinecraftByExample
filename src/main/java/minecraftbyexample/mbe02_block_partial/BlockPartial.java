package minecraftbyexample.mbe02_block_partial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 * <p>
 * BlockPartial uses a model which doesn't occupy the entire 1x1x1m space, and is made up of two pieces.
 * We can walk over it without colliding.
 * For background information on blocks see here http://greyminecraftcoder.blogspot.com/2020/02/blocks-1144.html
 * For a couple of the methods below the Forge guys have marked it as deprecated.  But you still need to override those
 * "deprecated" block methods.  What they mean is "when you want to find out what is a block's getRenderType(),
 * don't call block.getRenderType(), call blockState.getRenderType() instead".
 * If that doesn't make sense to you yet, don't worry.  Just ignore the "deprecated method" warning.
 */
public class BlockPartial extends Block {

  private static VoxelShape INSIDE;
  private static VoxelShape FULL_CUBE;
  private static VoxelShape XMIN_PANEL;
  private static VoxelShape YMIN_PANEL;
  protected static VoxelShape SHAPE;

  public BlockPartial() {
    super(Block.Properties.create(Material.ROCK)
    );

    INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    FULL_CUBE = VoxelShapes.fullCube();
    XMIN_PANEL = makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    YMIN_PANEL = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    SHAPE = VoxelShapes.combineAndSimplify(FULL_CUBE, XMIN_PANEL, IBooleanFunction.ONLY_FIRST);
    SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(),
            VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D),
                           new VoxelShape[] { makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D),
                                              makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
                                              INSIDE
                                            }
                          ), IBooleanFunction.ONLY_FIRST);
    // other properties that might be useful for partial blocks:
    // .doesNotBlockMovement()

  }

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  // not strictly required because the default (super method) is SOLID.

  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.SOLID;
  }

  // render using a BakedModel (mbe02_block_partial.json --> mbe02_block_partial_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public BlockRenderType getRenderType(BlockState blockState) {
    return BlockRenderType.MODEL;
  }

//  // by returning a null collision bounding box we stop the player from colliding with it
//  @Override
//  public AxisAlignedBB getCollisionBoundingBox(BlockState state, Blo worldIn, BlockPos pos) {
//    return NULL_AABB;
//  }

//
//  @Deprecated
//  public boolean isSolid(BlockState state) {
//    return this.blocksMovement && this.getRenderLayer() == BlockRenderLayer.SOLID;
//  }
//
//  @Deprecated
//  @OnlyIn(Dist.CLIENT)
//  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
//    return false;
//  }

  @Deprecated
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Deprecated
  public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return SHAPE;
  }

  @Deprecated
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return SHAPE;
  }


}
