package minecraftbyexample.mbe03_block_variants;

import minecraftbyexample.usefultools.SetBlockStateFlag;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockRenderType;
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
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * * Block3DWeb uses a model which
 *  - doesn't occupy the entire 1x1x1m space,
 *  - is made up of a central core, plus up to six "strands" radiating out from the core
 *  - can be waterlogged (filled with water) similar to a vanilla sign or fence
 *  We can walk over it without colliding, it slows the player like web does
 *
 * If the block is adjacent to another Block3DWeb, or to a solid surface, it joins to it with a strand of web.
 * The block has six true/false properties, one for each direction, to specify whether there is a strand in that direction
 *   or not.
 * These properties are then passed to the renderer, which uses the `multipart` blockstates format to draw each strand
 *   depending on whether the corresponding property is true or not.
 *
 * For example:
 * If the block to the east of the Block3DWeb is a solid stone block, then EAST property is true.
 * When rendering, the corresponding multistate property will draw the model for the web strand that connects to the east
 * mbe03b_block_3dweb_registry_name.json:
 *     {   "when": { "east": "true" },
 *       "apply": { "model": "minecraftbyexample:block/mbe03b_3dweb_east_model"}
 *     },
 *
 */
public class Block3DWeb extends Block implements IWaterLoggable {
  public Block3DWeb()
  {
    super(Block.Properties.create(Material.WEB).doesNotBlockMovement());                     // ensures the player can walk through the block
    BlockState defaultBlockState = this.stateContainer.getBaseState()
            .with(UP, false).with(DOWN, false)
            .with(EAST, false).with(WEST, false)
            .with(NORTH, false).with(SOUTH, false)
            .with(WATERLOGGED, false);
    this.setDefaultState(defaultBlockState);
    initialiseShapeCache();
  }

  // make colliding players stick in the web like normal web
  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    entityIn.setMotionMultiplier(state, new Vec3d(0.25D, (double)0.05F, 0.25D));
  }

  // render using an IBakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }


  // ----------- methods to update the properties based on neighbours --------

  /**
   * when the block is placed into the world, calculates the correct BlockState based on whether there is already water
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

    BlockState blockState = getDefaultState().with(WATERLOGGED, blockContainsWater);
    blockState = setConnections(world, blockPos, blockState);
    return blockState;
  }

  /**
   * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
   * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
   * returns its solidified counterpart.
   * Also schedules a water tick if the block is waterlogged.
   */
  public BlockState updatePostPlacement(BlockState blockState, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    if(blockState.get(WATERLOGGED).booleanValue()) {
      world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    switch (facing) {  // Only update the specified direction.  Uses a switch for clarity but probably a map or similar is better for real code
      case UP:
        blockState = blockState.with(UP, canWebAttachToNeighbourInThisDirection(world, currentPos, facing));
        break;
      case DOWN:
        blockState = blockState.with(DOWN, canWebAttachToNeighbourInThisDirection(world, currentPos, facing));
        break;
      case EAST:
        blockState = blockState.with(EAST, canWebAttachToNeighbourInThisDirection(world, currentPos, facing));
        break;
      case WEST:
        blockState = blockState.with(WEST, canWebAttachToNeighbourInThisDirection(world, currentPos, facing));
        break;
      case NORTH:
        blockState = blockState.with(NORTH, canWebAttachToNeighbourInThisDirection(world, currentPos, facing));
        break;
      case SOUTH:
        blockState = blockState.with(SOUTH, canWebAttachToNeighbourInThisDirection(world, currentPos, facing));
        break;
      default:
        LOGGER.error("Unexpected facing:" + facing);
    }
    return blockState;
  }

  private BlockState setConnections(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState) {
    return blockState
            .with(UP, canWebAttachToNeighbourInThisDirection(iBlockReader, blockPos, Direction.UP))
            .with(DOWN, canWebAttachToNeighbourInThisDirection(iBlockReader, blockPos, Direction.DOWN))
            .with(WEST, canWebAttachToNeighbourInThisDirection(iBlockReader, blockPos, Direction.WEST))
            .with(EAST, canWebAttachToNeighbourInThisDirection(iBlockReader, blockPos, Direction.EAST))
            .with(NORTH, canWebAttachToNeighbourInThisDirection(iBlockReader, blockPos, Direction.NORTH))
            .with(SOUTH, canWebAttachToNeighbourInThisDirection(iBlockReader, blockPos, Direction.SOUTH));
  }

  /**
   * Check the neighbor in the given direction to see if web can attach to it.
   * @param iBlockReader
   * @param blockPos
   * @param direction
   * @return
   */
  private boolean canWebAttachToNeighbourInThisDirection(IBlockReader iBlockReader, BlockPos blockPos, Direction direction) {
    BlockPos neighborPos = blockPos.offset(direction);
    BlockState neighborBlockState = iBlockReader.getBlockState(neighborPos);
    Block neighborBlock = neighborBlockState.getBlock();

    if (neighborBlock == Blocks.BARRIER) return false;
    if (neighborBlock == StartupCommon.block3DWeb) return true;
    if (cannotAttach(neighborBlock)) return false;
    boolean faceIsSolid = neighborBlockState.isSolidSide(iBlockReader, neighborPos, direction.getOpposite());
    return faceIsSolid;
  }

  // ------------ methods to define the properties for this block  ----

  // fillStateContainer is used to define which properties your block possess
  // A blockstate is created for each combination of properties;
  // for example two properties ON(true/false) and READY(true/false) would give rise to four variants
  //   [on=true, ready=true]
  //   [on=false, ready=true]
  //   [on=true, ready=false]
  //   [on=false, ready=false]
  /**
   * Defines the properties needed for the BlockState
   * @param builder
   */
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(UP, DOWN, WEST, EAST, NORTH, SOUTH, WATERLOGGED);
  }

  // the link properties below are used to communicate to the model renderer which of the web strands should be drawn, and whether any water should be drawn
  // Be wary of using too many properties.  A blockstate is created for every permutation of properties, so even for this simple example we have
  //  2^7 = 128 blockstates.

  // The mbe03b_block_3dweb_registry_name.json contains logic to check each of these properties by name, and draw the corresponding model components
  //   for each property which is true.

  public static final BooleanProperty UP = BlockStateProperties.UP;
  public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
  public static final BooleanProperty WEST = BlockStateProperties.WEST;
  public static final BooleanProperty EAST = BlockStateProperties.EAST;
  public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
  public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

  private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;  // is this block filled with water or not?


  // -------- methods to define the VoxelShapes of the block

  // returns the shape of the block:
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other �shapes� to control the interaction of the block with its environment and with the player
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    VoxelShape voxelShape = voxelShapeCache.get(state);
    return voxelShape != null ? voxelShape : VoxelShapes.fullCube();  // should always find it... just being defensive
  }

  // for this model, we're making the shape match the block model exactly
  private static final Vec3d CORE_MIN_CORNER = new Vec3d(5.5, 5.5, 5.5);
  private static final Vec3d CORE_MAX_CORNER = new Vec3d(10.5, 10.5, 10.5);

  private static final VoxelShape CORE_SHAPE =
          Block.makeCuboidShape(CORE_MIN_CORNER.x, CORE_MIN_CORNER.y, CORE_MIN_CORNER.z, CORE_MAX_CORNER.x, CORE_MAX_CORNER.y, CORE_MAX_CORNER.z);

  private static final VoxelShape LINK_UP_SHAPE =
          Block.makeCuboidShape(7.5, 10.5, 7.5, 8.5, 16, 8.5);
  private static final VoxelShape LINK_DOWN_SHAPE =
          Block.makeCuboidShape(7.5, 0.0, 7.5, 8.5, 5.5, 8.5);
  private static final VoxelShape LINK_WEST_SHAPE =
          Block.makeCuboidShape(0, 7.5, 7.5, 5.5, 8.5, 8.5);
  private static final VoxelShape LINK_EAST_SHAPE =
          Block.makeCuboidShape(10.5, 7.5, 7.5, 16.0, 8.5, 8.5);
  private static final VoxelShape LINK_NORTH_SHAPE =
          Block.makeCuboidShape(7.5, 7.5, 0.0, 8.5, 8.5, 5.5);
  private static final VoxelShape LINK_SOUTH_SHAPE =
          Block.makeCuboidShape(7.5, 7.5, 10.5, 8.5, 8.5, 16.0);

  /**
   * Create a cache of the VoxelShape for each blockstate (all possible combinations of links).
   * If we wanted to optimise further we could get rid of the waterlogged property first but for the purposes of this
   *   example it's not worth the added complexity
   * @return
   */
  private void initialiseShapeCache() {
    for (BlockState blockState : stateContainer.getValidStates()) {
      VoxelShape combinedShape = CORE_SHAPE;
      if (blockState.get(UP).booleanValue()) {
        combinedShape = VoxelShapes.or(combinedShape, LINK_UP_SHAPE);
      }
      if (blockState.get(DOWN).booleanValue()) {
        combinedShape = VoxelShapes.or(combinedShape, LINK_DOWN_SHAPE);
      }
      if (blockState.get(WEST).booleanValue()) {
        combinedShape = VoxelShapes.or(combinedShape, LINK_WEST_SHAPE);
      }
      if (blockState.get(EAST).booleanValue()) {
        combinedShape = VoxelShapes.or(combinedShape, LINK_EAST_SHAPE);
      }
      if (blockState.get(NORTH).booleanValue()) {
        combinedShape = VoxelShapes.or(combinedShape, LINK_NORTH_SHAPE);
      }
      if (blockState.get(SOUTH).booleanValue()) {
        combinedShape = VoxelShapes.or(combinedShape, LINK_SOUTH_SHAPE);
      }
      voxelShapeCache.put(blockState, combinedShape);
    }
  }

  private static HashMap<BlockState, VoxelShape> voxelShapeCache = new HashMap<>();

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

    final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);

    world.setBlockState(blockPos, blockState.with(BlockStateProperties.WATERLOGGED, true), FLAGS);
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
    final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);

    // if block is waterlogged, remove the water from the block and return water to the caller
    if (blockState.get(WATERLOGGED)) {
      world.setBlockState(blockPos, blockState.with(WATERLOGGED, false), FLAGS);
      return Fluids.WATER;
    } else {
      return Fluids.EMPTY;
    }
  }

}
