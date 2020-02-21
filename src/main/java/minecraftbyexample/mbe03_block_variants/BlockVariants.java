package minecraftbyexample.mbe03_block_variants;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

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
public class BlockVariants extends Block implements IWaterLoggable
{
  public BlockVariants(EnumColour blockColour)
  {
    super(Block.Properties.create(Material.ROCK).doesNotBlockMovement() // we don't want this to block movement through the block
            // other typically useful properties: hardnessAndResistance(), harvestLevel(), harvestTool()

    );
    this.blockColour = blockColour;

    BlockState defaultBlockState = this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false);
    this.setDefaultState(defaultBlockState);
  }

  private EnumColour blockColour;  // not strictly needed for this example because each colour variant has its own registry name and corresponding model

  // render using a BakedModel (mbe03_block_variants.json --> mbe03_block_variants_model.json)
  // not strictly required because the default (super method) is BlockRenderType.MODEL;
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  /**
   * when the block is placed into the world, calculates the correct BlockState based on which direction the player is facing and whether there is already water
   *   in this block or not
   *   Copied from StandingSignBlock
   * @param blockItemUseContext
   * @return
   */
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
    World world = blockItemUseContext.getWorld();
    BlockPos blockPos = blockItemUseContext.getPos();

    IFluidState fluidLevelOfCurrentBlock = world.getFluidState(blockPos);
    boolean blockContainsWater = fluidLevelOfCurrentBlock.getFluid() == Fluids.WATER;  // getFluid returns EMPTY if no fluid

    Direction direction = blockItemUseContext.getPlacementHorizontalFacing();  // north, east, south, or west
    float playerFacingDirectionAngle = blockItemUseContext.getPlacementYaw(); //if you want more directions than just NESW, you can use the yaw instead.
        // likewise the pitch is also available for up/down placement.

    BlockState blockState = getDefaultState().with(FACING, direction).with(WATERLOGGED, blockContainsWater);
    return blockState;
  }

  /**
   * Defines the properties needed for the BlockState
   * @param builder
   */
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, WATERLOGGED);
  }

  // returns the shape of the block:
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other �shapes� to control the interaction of the block with its environment and with the player.
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    Direction direction = state.get(FACING);
    VoxelShape voxelShape = POST_SHAPES.get(direction);
    return voxelShape != null ? voxelShape : VoxelShapes.fullCube();  // should always find it... just being defensive
    // you can also use direction.getHorizontalIndex() if you want, instead of a map.
  }

  // the position of the post changes depending on the direction that the sign is facing.

  private static final Vec3d POST_MIN_CORNER_N = new Vec3d(7.0, 0.0, 7.0);
  private static final Vec3d POST_MAX_CORNER_N = new Vec3d(8.0, 14.0, 8.0);
  private static final VoxelShape POST_SHAPE_N = Block.makeCuboidShape(POST_MIN_CORNER_N.getX(), POST_MIN_CORNER_N.getY(), POST_MIN_CORNER_N.getZ(),
                                                                       POST_MAX_CORNER_N.getX(), POST_MAX_CORNER_N.getY(), POST_MAX_CORNER_N.getZ());

  private static final Vec3d POST_MIN_CORNER_E = new Vec3d(8.0, 0.0, 7.0);
  private static final Vec3d POST_MAX_CORNER_E = new Vec3d(9.0, 14.0, 8.0);
  private static final VoxelShape POST_SHAPE_E = Block.makeCuboidShape(POST_MIN_CORNER_E.getX(), POST_MIN_CORNER_E.getY(), POST_MIN_CORNER_E.getZ(),
                                                                       POST_MAX_CORNER_E.getX(), POST_MAX_CORNER_E.getY(), POST_MAX_CORNER_E.getZ());

  private static final Vec3d POST_MIN_CORNER_S = new Vec3d(8.0, 0.0, 8.0);
  private static final Vec3d POST_MAX_CORNER_S = new Vec3d(9.0, 14.0, 9.0);
  private static final VoxelShape POST_SHAPE_S = Block.makeCuboidShape(POST_MIN_CORNER_S.getX(), POST_MIN_CORNER_S.getY(), POST_MIN_CORNER_S.getZ(),
          POST_MAX_CORNER_S.getX(), POST_MAX_CORNER_S.getY(), POST_MAX_CORNER_S.getZ());

  private static final Vec3d POST_MIN_CORNER_W = new Vec3d(7.0, 0.0, 8.0);
  private static final Vec3d POST_MAX_CORNER_W = new Vec3d(8.0, 14.0, 9.0);
  private static final VoxelShape POST_SHAPE_W = Block.makeCuboidShape(POST_MIN_CORNER_W.getX(), POST_MIN_CORNER_W.getY(), POST_MIN_CORNER_W.getZ(),
                                                                       POST_MAX_CORNER_W.getX(), POST_MAX_CORNER_W.getY(), POST_MAX_CORNER_W.getZ());

  private static final Map<Direction, VoxelShape> POST_SHAPES =
        ImmutableMap.of(Direction.NORTH,POST_SHAPE_N,   Direction.EAST,POST_SHAPE_E,   Direction.SOUTH,POST_SHAPE_S,   Direction.WEST,POST_SHAPE_W);


  private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
      // Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
  private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  //----some methods to help handle the waterlogging correctly -----------

  /**
   * Is there water in this block or not?
   * @param state
   * @return
   */
  @Override
  public IFluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
  }

  /**
   * Try to fill water into this block
   * @param world
   * @param blockPos
   * @param blockState
   * @param fluidState
   * @return true for success, false for failure
   */
  @Override
  public boolean receiveFluid(IWorld world, BlockPos blockPos, BlockState blockState, IFluidState fluidState) {
    if (world.isRemote()) return false; // only perform on the server
    // if block is waterlogged already, or the fluid isn't water, return without doing anything.
    if (fluidState.getFluid() != Fluids.WATER) return false;
    if (blockState.get(WATERLOGGED)) return false;

    final int BLOCK_UPDATE_FLAG = 1;
    final int SEND_UPDATE_TO_CLIENT_FLAG = 2;
    world.setBlockState(blockPos, blockState.with(BlockStateProperties.WATERLOGGED, true),
            BLOCK_UPDATE_FLAG + SEND_UPDATE_TO_CLIENT_FLAG);
    world.getPendingFluidTicks().scheduleTick(blockPos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
    return true;
  }

  /**
   * Try to use a bucket to remove waterlogging from this block
   * @param world
   * @param blockPos
   * @param blockState
   * @return Fluids.WATER for successful removal, Fluids.EMPTY if no water present
   */
  @Override
  public Fluid pickupFluid(IWorld world, BlockPos blockPos, BlockState blockState) {
    final int BLOCK_UPDATE_FLAG = 1;
    final int SEND_UPDATE_TO_CLIENT_FLAG = 2;

    // if block is waterlogged, remove the water from the block and return water to the caller
    if (blockState.get(WATERLOGGED)) {
      world.setBlockState(blockPos, blockState.with(WATERLOGGED, false),
              BLOCK_UPDATE_FLAG + SEND_UPDATE_TO_CLIENT_FLAG);
      return Fluids.WATER;
    } else {
      return Fluids.EMPTY;
    }
  }


  //----------------

  // create a new enum for our four colours, with some supporting methods to get human-readable names.
  public enum EnumColour implements IStringSerializable
  {
    BLUE("blue"),
    RED("red"),
    GREEN("green"),
    YELLOW("yellow");

    @Override
    public String toString()
    {
      return this.name;
    }
    public String getName()
    {
      return this.name;
    }

    private final String name;

    private EnumColour(String i_name)
    {
      this.name = i_name;
    }
  }
}
