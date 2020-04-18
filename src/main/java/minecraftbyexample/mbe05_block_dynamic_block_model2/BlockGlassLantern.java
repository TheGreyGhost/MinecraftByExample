package minecraftbyexample.mbe05_block_dynamic_block_model2;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockVariants uses a model which
 * - doesn't occupy the entire 1x1x1m space,
 * - is made up of two pieces,
 * - uses a CUTOUT texture (with seethrough holes)
 * - has variants (can face in four directions, and can be four different colours)
 * - can be waterlogged (filled with water) similar to a vanilla sign or fence
 * We can walk over it without colliding.
 * Note that the method for implementing block with variants has changed a lot since 1.12.  See here for more info:
 * https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
 *
 * The basic rules for properly implementing variant blocks are:
 * 1) For each variant which has a different item, create a unique block instance.
 *    For example - different coloured beds are YELLOW_BED, RED_BED, GREEN_BED etc
 *    They all share the same BedBlock class; the colour for each instance is provided to the constructor
 * 2) For variants which affect the block in the world, but not the corresponding held item, use a blockstate property
 *    For example - the direction that the bed is facing (north, east, south, west)
 *
 * For background information on block see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 * For a couple of the methods below the Forge guys have marked it as deprecated.  But you still need to override those
 * "deprecated" block methods.  What they mean is "when you want to find out what is a block's getRenderType(),
 * don't call block.getRenderType(), call blockState.getRenderType() instead".
 * If that doesn't make sense to you yet, don't worry.  Just ignore the "deprecated method" warning.
 */
public class BlockGlassLantern extends Block
{
  public BlockGlassLantern()
  {
    super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.LANTERN).notSolid().lightValue(15)
    );    // match the vanilla lantern properties; the lightValue is turned on/off by getLightValue()

    BlockState defaultBlockState = this.stateContainer.getBaseState().with(HANGING, false).with(LIT, false);
    this.setDefaultState(defaultBlockState);
  }

  // render using a BakedModel (mbe03_block_variants.json --> mbe03_block_variants_model.json)
  // not strictly required because the default (super method) is BlockRenderType.MODEL;
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  /**
   * Defines the properties needed for the BlockState
   * @param builder
   */
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(HANGING, LIT);
  }


  public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
  public static final BooleanProperty LIT = BlockStateProperties.LIT;

  // for this model, we're making the shape match the block model exactly
  private static final Vec3d BASE_MIN_CORNER = new Vec3d(5.0, 0.0, 5.0);
  private static final Vec3d BASE_MAX_CORNER = new Vec3d(11.0, 7.0, 11.0);

  private static final Vec3d LID_MIN_CORNER = new Vec3d(6.0, 7.0, 6.0);
  private static final Vec3d LID_MAX_CORNER = new Vec3d(10.0, 9.0, 10.0);

  private static final VoxelShape NON_HANGING_BASE_SHAPE =
          Block.makeCuboidShape(BASE_MIN_CORNER.x, BASE_MIN_CORNER.y, BASE_MIN_CORNER.z, BASE_MAX_CORNER.x, BASE_MAX_CORNER.y, BASE_MAX_CORNER.z);
  private static final VoxelShape NON_HANGING_LID_SHAPE =
          Block.makeCuboidShape(LID_MIN_CORNER.x, LID_MIN_CORNER.y, LID_MIN_CORNER.z, LID_MAX_CORNER.x, LID_MAX_CORNER.y, LID_MAX_CORNER.z);
  private static final VoxelShape NON_HANGING_SHAPE = VoxelShapes.or(NON_HANGING_BASE_SHAPE, NON_HANGING_LID_SHAPE);

  private static final double HANGING_YOFFSET = 1.0;
  private static final VoxelShape HANGING_SHAPE = NON_HANGING_SHAPE.withOffset(0, HANGING_YOFFSET, 0);

  /**
   * Set the lantern to a hanging lantern or resting-on-the-ground lantern depending on how the player places it
   * @param context
   * @return
   */
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    for (Direction direction : context.getNearestLookingDirections()) {
      if (direction.getAxis() == Direction.Axis.Y) {
        BlockState blockstate = this.getDefaultState().with(HANGING, Boolean.valueOf(direction == Direction.UP));
        if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
          return blockstate;
        }
      }
    }

    return null; //? should never get here
  }

  // returns the shape of the block:
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other `shapes` to control the interaction of the block with its environment and with the player.
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return state.get(HANGING) ? HANGING_SHAPE : NON_HANGING_SHAPE;
  }

  /**
   * Amount of light emitted- none if lantern is off.
   */
  @Override
  public int getLightValue(BlockState state) {
    return state.get(LIT) ? super.getLightValue(state) : 0;
  }

  /**
   * Can the lantern be placed here?  i.e. if hanging - solid block above.  if not hanging - solid block below
   * @param state
   * @param worldIn
   * @param pos
   * @return
   */
  @Override
  public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
    Direction direction = attachmentFace(state);
    return Block.hasEnoughSolidSide(worldIn, pos.offset(direction), direction.getOpposite());
  }

  /**
   * Which face is the lantern connected?  Hanging (top face) or resting on the ground (bottom face)
   * @param blockState
   * @return
   */
  protected static Direction attachmentFace(BlockState blockState) {
    return blockState.get(HANGING) ? Direction.UP : Direction.DOWN;
  }

  /**
   * Check to see whether the neighbour block has changed and we can't hang / rest on the ground any more
   */
  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    boolean lanternIsNotSupported = false;
    boolean checkRequired = attachmentFace(stateIn) == facing;

    if (checkRequired) {
      lanternIsNotSupported = !stateIn.isValidPosition(worldIn, currentPos);
    }

    if (lanternIsNotSupported) return Blocks.AIR.getDefaultState();
    return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
    return false;
  }

  /**
   * What happens when a piston block pushes this?
   * @param state
   * @return
   */
  @Override
  public PushReaction getPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }

}
