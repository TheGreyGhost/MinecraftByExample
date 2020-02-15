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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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
 * For background information on block shapes see here https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
 * For a couple of the methods below the Forge guys have marked it as deprecated.  But you still need to override those
 * "deprecated" block methods.  What they mean is "when you want to find out what is a block's getRenderType(),
 * don't call block.getRenderType(), call blockState.getRenderType() instead".
 * If that doesn't make sense to you yet, don't worry.  Just ignore the "deprecated method" warning.
 */
public class BlockPartial extends Block {

    // for this model, we're making the shape match the block model exactly - see assets\minecraftbyexample\models\block\mbe02_block_partial_model.json
  private static final Vec3d BASE_MIN_CORNER = new Vec3d(2.0, 0.0, 0.0);
  private static final Vec3d BASE_MAX_CORNER = new Vec3d(14.0, 1.0, 16.0);
  private static final Vec3d PILLAR_MIN_CORNER = new Vec3d(7.0, 1.0, 6.0);
  private static final Vec3d PILLAR_MAX_CORNER = new Vec3d(9.0, 8.0, 10.0);

  private static final VoxelShape BASE = Block.makeCuboidShape(BASE_MIN_CORNER.getX(), BASE_MIN_CORNER.getY(), BASE_MIN_CORNER.getZ(),
                                                               BASE_MAX_CORNER.getX(), BASE_MAX_CORNER.getY(), BASE_MAX_CORNER.getZ());
  private static final VoxelShape PILLAR = Block.makeCuboidShape(PILLAR_MIN_CORNER.getX(), PILLAR_MIN_CORNER.getY(), PILLAR_MIN_CORNER.getZ(),
                                                                 PILLAR_MAX_CORNER.getX(), PILLAR_MAX_CORNER.getY(), PILLAR_MAX_CORNER.getZ());

  private static VoxelShape COMBINED_SHAPE = VoxelShapes.or(BASE, PILLAR);  // use this method to add two shapes together

  private static VoxelShape EMPTY_SPACE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), COMBINED_SHAPE, IBooleanFunction.ONLY_FIRST);
      // use this method if you need to make "holes"; eg in this case we are making a VoxelShape for the empty (non-solid) space in this block
      // Vanilla uses this to (eg) make a cavity in a composter block or cauldron.

  public BlockPartial() {
    super(Block.Properties.create(Material.ROCK).doesNotBlockMovement()   // we don't want this to block movement through the block
    );
  }

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  // not strictly required because the default (super method) is SOLID.

  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.SOLID;
  }

  // render using a BakedModel (mbe02_block_partial.json --> mbe02_block_partial_model.json)
  // not strictly required because the default (super method) is BlockRenderType.MODEL
  @Override
  public BlockRenderType getRenderType(BlockState blockState) {
    return BlockRenderType.MODEL;
  }

  // returns the shape of the block:
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other ‘shapes’ to control the interaction of the block with its environment and with the player.
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Deprecated
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return COMBINED_SHAPE;
  }

  // not needed for this example; see  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return super.getCollisionShape(state, worldIn, pos, context);
  }

  // not needed for this example; see  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Deprecated
  public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return super.getRenderShape(state, worldIn, pos);
  }

  // not needed for this example; see  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Deprecated
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return super.getRaytraceShape(state, worldIn, pos);
  }
}
