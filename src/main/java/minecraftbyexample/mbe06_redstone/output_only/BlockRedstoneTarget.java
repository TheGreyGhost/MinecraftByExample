package minecraftbyexample.mbe06_redstone.output_only;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import minecraftbyexample.usefultools.SetBlockStateFlag;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 24/11/2015
 *
 * BlockRedstoneTarget is designed to be hung on a wall.  When an arrow is fired into the target, it emits strong
 *   power into the wall.  The power level depends on which ring of the target the arrow is stuck in:
 *     15 for the bullseye, decreasing for every ring down to 0 for no arrow (or stuck in the wood)
 * Unlike the vanilla lever, it does not emit weak power.
 */
public class BlockRedstoneTarget extends Block
{
  public BlockRedstoneTarget()
  {
    super(Block.Properties.create(Material.WOOD));
  }

  //----- methods related to redstone

  /**
   * This block can provide power
   * @return
   */
  @Override
  public boolean canProvidePower(BlockState iBlockState)
  {
    return true;
  }

  /** How much weak power does this block provide to the adjacent block?  In this example - none.
   * See https://greyminecraftcoder.blogspot.com/2020/05/redstone-1152.html for more information
   * @param blockAccess
   * @param pos the position of this block
   * @param blockState the blockstate of this block
   * @param directionFromNeighborToThis eg EAST means that this is to the EAST of the block which is asking for weak power
   * @return The power provided [0 - 15]
   */
  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction directionFromNeighborToThis) {
    return 0;
  }

  /**
   *  Asks the target block how much power it is providing to a neighbour
   * @param blockReader
   * @param pos the position of this block
   * @param blockState the blockstate of this block
   * @param directionFromNeighborToThis eg EAST means that this is to the EAST of the block which is asking for strong power
   * @return The power provided [0 - 15]
   */
  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockReader, BlockPos pos, Direction directionFromNeighborToThis) {
    Direction directionThatBackIsPointing = blockState.get(DIRECTION_THAT_BACK_IS_POINTING);
    Direction directionFromThisToNeighbor = directionFromNeighborToThis.getOpposite();
    // only provide strong power through the back of the target.  If the direction that the back is pointing is east,
    //   this means that it provides power to the block which lies to the east.
    // When this method is called by the adjacent block which lies to the east, the value of the directionFromNeighborToThis
    //    is WEST.

    if (directionFromThisToNeighbor != directionThatBackIsPointing) return 0;

    // The amount of power provided is related to how close the arrow hit to the bullseye.
    //  Bullseye = 15; Outermost ring = 3.

    int arrowDistanceFromCentre = blockState.get(ARROW_DISTANCE_FROM_CENTRE);
    if (arrowDistanceFromCentre == NO_ARROW) return 0;

    final int POWER_AT_INNERMOST_RING = 15;
    final int POWER_AT_OUTERMOST_RING = 3;
    double strongPower = UsefulFunctions.interpolate_with_clipping(arrowDistanceFromCentre,
            INNERMOST_RING_DISTANCE, OUTERMOST_RING_DISTANCE,
            POWER_AT_INNERMOST_RING, POWER_AT_OUTERMOST_RING);

    return (int)strongPower;
  }

  // ---- methods to control placement of the target (must be on a solid wall)
  // copied and adapted from WallSignBlock

  /**Is this a valid position for the target (need a solid wall to hang onto)
   * @param state
   * @param world
   * @param pos
   * @return
   */
  @Override
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    Direction directionFromThisToNeighbour = state.get(DIRECTION_THAT_BACK_IS_POINTING);
    Direction faceOfNeighbourThatTargetIsMountedOn = directionFromThisToNeighbour.getOpposite();

    BlockPos neighbourBlockPos = pos.offset(directionFromThisToNeighbour);
    BlockState neighbourBlockState = world.getBlockState(neighbourBlockPos);
    return neighbourBlockState.isSolidSide(world, neighbourBlockPos, faceOfNeighbourThatTargetIsMountedOn);
  }

  // when the block is placed into the world, calculates the correct BlockState based on which direction the player is looking
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    BlockState blockstate = this.getDefaultState();
    World world = context.getWorld();
    BlockPos blockpos = context.getPos();
    Direction [] nearestLookingDirections = context.getNearestLookingDirections();

    for (Direction direction : nearestLookingDirections) {
      if (direction.getAxis().isHorizontal()) {
        blockstate = blockstate.with(DIRECTION_THAT_BACK_IS_POINTING, direction);
        if (blockstate.isValidPosition(world, blockpos)) {
          return blockstate;
        }
      }
    }

    return null;
  }

  // ---- methods to handle changes in state and inform neighbours when necessary

  // The trigger to turn on the power is when an arrow strikes the target and calls onProjectileCollision
  // There is no trigger to turn off the power when the arrow disappears or it removed by the player; so instead we
  //   use a scheduled tick to check once per second whether the arrow is still present or not.

  /**
   * Called when a projectile strikes the block
   * In this case - we check if an arrow has collided.
   * @param world
   * @param hit
   * @param state
   * @param projectile
   */
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, Entity projectile) {
    if (world.isRemote) return;  // on client - do nothing
    if (!(projectile instanceof AbstractArrowEntity)) return;  // only for arrows
    if (hit.getType() != RayTraceResult.Type.BLOCK) return;  // just being defensive; this should always be true

    BlockPos blockPos = hit.getPos();
    Direction hitFace = hit.getFace();
    Direction directionOfBack = state.get(DIRECTION_THAT_BACK_IS_POINTING);
    Direction directionOfFront = directionOfBack.getOpposite();
    if (hitFace != directionOfFront) return;          // only count hits on the front face

    // when a new arrow hits, remove all others which are already embedded:
    // search for all arrow entities which are colliding with the target, and remove them.

    VoxelShape voxelShape = state.getCollisionShape(world, hit.getPos());
    AxisAlignedBB targetAABB = voxelShape.getBoundingBox();
    AxisAlignedBB targetAABBinWorld = targetAABB.offset(blockPos);
    List<AbstractArrowEntity> embeddedArrows = world.getEntitiesWithinAABB(AbstractArrowEntity.class, targetAABBinWorld);

    for (AbstractArrowEntity embeddedEntity : embeddedArrows) {
      if (embeddedEntity.getEntityId() != projectile.getEntityId()) {
        embeddedEntity.remove();
      }
    }

    // update our blockstate to reflect which ring the arrow is stuck into.
    int arrowDistanceFromCentre = findArrowDistanceFromCentre(directionOfFront, targetAABBinWorld, hit.getHitVec());
    BlockState newBlockState = state.with(ARROW_DISTANCE_FROM_CENTRE, arrowDistanceFromCentre);
    final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
    world.setBlockState(blockPos, newBlockState, FLAGS);
    informNeighborsOfPowerChange(newBlockState, world, blockPos);

    // start periodically checking whether the arrow has disappeared or been removed
    world.getPendingBlockTicks().scheduleTick(blockPos, this, this.tickRate(world));
  }

  /**
   * Perform a scheduled update for this block
   * Checks to see if all arrows have been removed, and if so turn off the power.
   * @param world
   * @param pos
   * @param state
   * @param rand
   */
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    if (!hasAtLeastOneArrow(state)) return;

    // search for all arrow entities which are colliding with the target

    VoxelShape voxelShape = state.getCollisionShape(world, pos);
    AxisAlignedBB targetAABB = voxelShape.getBoundingBox();
    AxisAlignedBB targetAABBinWorld = targetAABB.offset(pos);
    List<AbstractArrowEntity> embeddedArrows = world.getEntitiesWithinAABB(AbstractArrowEntity.class, targetAABBinWorld);

    if (embeddedArrows.isEmpty()) {
      BlockState newBlockState = state.with(ARROW_DISTANCE_FROM_CENTRE, NO_ARROW);
      final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
      world.setBlockState(pos, newBlockState, FLAGS);
      informNeighborsOfPowerChange(newBlockState, world, pos);
    } else {
      world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
    }
  }

  /**
   * How often should we tick this block?
   */
  public int tickRate(IWorldReader worldIn) {
    final int TICKS_PER_SECOND = 20;
    final int TICK_PERIOD_IN_SECONDS = 1;
    return TICK_PERIOD_IN_SECONDS * TICKS_PER_SECOND;
  }

  /**
   * A neighbour has updated their state.  Check if the supporting wall that we're mounted on has been removed.
   */
  @Override
  public BlockState updatePostPlacement(BlockState thisBlockState, Direction directionFromThisToNeighbor, BlockState neighborState,
                                        IWorld world, BlockPos thisBlockPos, BlockPos neighborBlockpos) {
    Direction directionThatBackIsPointing = thisBlockState.get(DIRECTION_THAT_BACK_IS_POINTING);
    if (directionFromThisToNeighbor == directionThatBackIsPointing && !thisBlockState.isValidPosition(world, thisBlockPos))
      return Blocks.AIR.getDefaultState();
    return super.updatePostPlacement(thisBlockState, directionFromThisToNeighbor, neighborState, world, thisBlockPos, neighborBlockpos);
  }

  // Handle a change of block state
  // Copied from LeverBlock
  @Override
  public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
    if (isMoving || (newState.getBlock() == oldState.getBlock())) return;
    if (hasAtLeastOneArrow(oldState)) {
      informNeighborsOfPowerChange(oldState, world, pos);
    }
    super.onReplaced(oldState, world, pos, newState, isMoving);
  }

  private void informNeighborsOfPowerChange(BlockState blockState, World world, BlockPos blockPos) {
    Direction directionThatBackIsPointing = blockState.get(DIRECTION_THAT_BACK_IS_POINTING);

    // inform my neighbours that I have changed state
    world.notifyNeighborsOfStateChange(blockPos, this);

    // since I am giving strong power to the neighbouring wall, inform the wall's neighbours of a change in strong power
    Direction directionOfNeighbouringWall = directionThatBackIsPointing;
    world.notifyNeighborsOfStateChange(blockPos.offset(directionOfNeighbouringWall), this);
  }

  //----- methods related to the block's appearance (see MBE01_BLOCK_SIMPLE and MBE02_BLOCK_PARTIAL)

  private static Map<Direction, VoxelShape> SHAPES;
  static {
    SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.SOUTH, Block.makeCuboidShape( 0.0D, 0.0D, 15.0D,  16.0D, 16.0D, 16.0D),
            Direction.NORTH, Block.makeCuboidShape( 0.0D, 0.0D,  0.0D,  16.0D, 16.0D,  1.0D),
            Direction.WEST,  Block.makeCuboidShape( 0.0D, 0.0D,  0.0D,   1.0D, 16.0D, 16.0D),
            Direction.EAST,  Block.makeCuboidShape(15.0D, 0.0D,  0.0D,  16.0D, 16.0D, 16.0D)   )    );
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    Direction facing = state.get(DIRECTION_THAT_BACK_IS_POINTING);
    VoxelShape targetShape = SHAPES.get(facing);
    if (targetShape == null) throw new AssertionError("Unexpected facing direction:" + facing);
    return targetShape;
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  /** Based on the location where the arrow hit, determine the distance to the centre
   * @return the closest distance to the centre (eg 0 = centremost ring , 6 = outermost ring); or NO_ARROW for none.
   */
  private int findArrowDistanceFromCentre(Direction directionThatFrontIsPointing, AxisAlignedBB targetAABBinWorld, Vec3d hitLocation)
  {
    final int MISS_VALUE = NO_ARROW;

    Vec3d targetCentre = new Vec3d((targetAABBinWorld.minX + targetAABBinWorld.maxX) / 2.0,
            (targetAABBinWorld.minY + targetAABBinWorld.maxY) / 2.0,
            (targetAABBinWorld.minZ + targetAABBinWorld.maxZ) / 2.0
    );

    Vec3d hitRelativeToCentre = hitLocation.subtract(targetCentre);

    // Which ring did it hit?  Calculate it as the biggest deviation of y and (x and z) from the centre.

    double xDeviationPixels = 0;
    double yDeviationPixels = Math.abs(hitRelativeToCentre.y * 16.0);
    double zDeviationPixels = 0;

    if (directionThatFrontIsPointing == Direction.EAST || directionThatFrontIsPointing == Direction.WEST) {
      zDeviationPixels = Math.abs(hitRelativeToCentre.z * 16.0);
    } else {
      xDeviationPixels = Math.abs(hitRelativeToCentre.x * 16.0);
    }

    double closestDistance = Double.MAX_VALUE; //arbitrary large value
    double maxDeviationPixels = Math.max(yDeviationPixels, Math.max(xDeviationPixels, zDeviationPixels));
    if (maxDeviationPixels < closestDistance) {
      closestDistance = maxDeviationPixels;
    }

    if (closestDistance == Double.MAX_VALUE) return MISS_VALUE;
    int ringHit = MathHelper.floor(closestDistance);
    return (ringHit <= OUTERMOST_RING_DISTANCE) ? ringHit : MISS_VALUE;
  }

  // ---------methods related to storing information about the block (which way it's facing, the power level)

  // BlockRedstoneTarget has two properties:
  //  1) The direction that the back is pointing - eg EAST means that the back is pointing east, because the target is
  //      mounted on a wall which lies to the east of the target.  The red and white rings are hence pointing west.
  //  2) The distance from the best arrow to the centre of the target.
  //
  private static final DirectionProperty DIRECTION_THAT_BACK_IS_POINTING = HorizontalBlock.HORIZONTAL_FACING;
  // Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST

  private static final int INNERMOST_RING_DISTANCE = 0;    // INNERMOST_RING_DISTANCE = centre of target
  private static final int OUTERMOST_RING_DISTANCE = 6;    // OUTERMOST_RING_DISTANCE = outermost edge of target
  private static final int NO_ARROW = OUTERMOST_RING_DISTANCE + 1;    // NO_ARROW = there are no arrows stuck in the target
  private static final IntegerProperty ARROW_DISTANCE_FROM_CENTRE = IntegerProperty.create("arrow_distance", INNERMOST_RING_DISTANCE, NO_ARROW);

  // necessary to define which properties your block uses
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(ARROW_DISTANCE_FROM_CENTRE).add(DIRECTION_THAT_BACK_IS_POINTING);
  }

  private boolean hasAtLeastOneArrow(BlockState blockState) {
    int arrowDistanceFromCentre = blockState.get(ARROW_DISTANCE_FROM_CENTRE);
    return (arrowDistanceFromCentre != NO_ARROW);
  }

}
