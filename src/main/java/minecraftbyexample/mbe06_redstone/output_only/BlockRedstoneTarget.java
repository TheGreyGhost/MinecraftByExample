package minecraftbyexample.mbe06_redstone.output_only;

import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.List;
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
   * @param side the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */
  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return 0;
  }

  /**
   *  The target provides strong power to the block it's mounted on (hanging on)
   * @param blockAccess
   * @param pos the position of this block
   * @param blockState the blockstate of this block
   * @param side the side of the block - eg EAST means that this is to the EAST of the block which is asking for strong power
   * @return The power provided [0 - 15]
   */
  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    Direction targetFacing = blockState.get(FACING);

    // only provide strong power through the back of the target.  If the target is facing east, that means
    //   it provides power to the block which lies to the west.
    // When this method is called by the adjacent block which lies to the west, the value of the side parameter is EAST.

    if (side != targetFacing) return 0;

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

  public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {


    /**
     * Called with an entity collides with the block.
     * In this case - we check if an arrow has collided.
     * @param worldIn
     * @param pos
     * @param state
     * @param entityIn
     */
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, BlockState state, Entity entityIn)
  {
    Direction targetFacing = (Direction)state.getValue(PROPERTYFACING);

    if (!worldIn.isRemote) {
      if (entityIn instanceof AbstractArrowEntity) {
        AxisAlignedBB targetAABB = getCollisionBoundingBox(state, worldIn, pos);
        AxisAlignedBB targetAABBinWorld = targetAABB.offset(pos);
        List<AbstractArrowEntity> embeddedArrows = worldIn.getEntitiesWithinAABB(AbstractArrowEntity.class, targetAABBinWorld);

        // when a new arrow hits, remove all others which are already embedded

        for (AbstractArrowEntity embeddedEntity : embeddedArrows) {
          if (embeddedEntity.getEntityId() != entityIn.getEntityId()) {
            embeddedEntity.setDead();
          }
        }

        // notify my immediate neighbours, and also the immediate neighbours of the block I'm mounted on, because I
        //  am giving strong power to it.
        final boolean CASCADE_UPDATE = false;  // I'm not sure what this flag does, but vanilla always sets it to false
        // except for calls by World.setBlockState()
        worldIn.notifyNeighborsOfStateChange(pos, this, CASCADE_UPDATE);
        Direction directionOfNeighbouringWall = targetFacing.getOpposite();
        worldIn.notifyNeighborsOfStateChange(pos.offset(directionOfNeighbouringWall), this, CASCADE_UPDATE);
      }
    }
  }

  /**
   * Perform a scheduled update for this block
   * @param worldIn
   * @param pos
   * @param state
   * @param rand
   */
  @Override
  public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand)
  {
    // depending on what your block does, you may need to implement updateTick and schedule updateTicks using
    //         worldIn.scheduleUpdate(pos, this, 4);
    // For vanilla examples see BlockButton, BlockRedstoneLight
    // nothing required for this example
  }

  /** For all the arrows stuck in the target, find the one which is the closest to the centre.
   *
   * @param worldIn
   * @param pos
   * @param state
   * @return the closest distance to the centre (eg 0->1 = centremost ring , 6 = outermost ring); or <0 for none.
   */
  private int findBestArrowRing(World worldIn, BlockPos pos, BlockState state)
  {
    final int MISS_VALUE = -1;
    Direction targetFacing = (Direction)state.getValue(PROPERTYFACING);
    AxisAlignedBB targetAABB = getCollisionBoundingBox(state, worldIn, pos);
    AxisAlignedBB targetAABBinWorld = targetAABB.offset(pos);
    List<AbstractArrowEntity> embeddedArrows = worldIn.getEntitiesWithinAABB(AbstractArrowEntity.class, targetAABBinWorld);
    if (embeddedArrows.isEmpty()) return MISS_VALUE;

    double closestDistance = Float.MAX_VALUE;
    for (AbstractArrowEntity entity : embeddedArrows) {
      if (!entity.isDead && entity instanceof AbstractArrowEntity) {
        AbstractArrowEntity entityArrow = (AbstractArrowEntity) entity;
        Vec3d hitLocation = getArrowIntersectionWithTarget(entityArrow, targetAABBinWorld);
        if (hitLocation != null) {
          Vec3d targetCentre = new Vec3d((targetAABBinWorld.minX + targetAABBinWorld.maxX) / 2.0,
                                              (targetAABBinWorld.minY + targetAABBinWorld.maxY) / 2.0,
                                              (targetAABBinWorld.minZ + targetAABBinWorld.maxZ) / 2.0
          );
          Vec3d hitRelativeToCentre = hitLocation.subtract(targetCentre);

          // Which ring did it hit?  Calculate it as the biggest deviation of y and (x and z) from the centre.

          double xDeviationPixels = 0;
          double yDeviationPixels = Math.abs(hitRelativeToCentre.y * 16.0);
          double zDeviationPixels = 0;

          if (targetFacing == Direction.EAST || targetFacing == Direction.WEST) {
            zDeviationPixels = Math.abs(hitRelativeToCentre.z * 16.0);
          } else {
            xDeviationPixels = Math.abs(hitRelativeToCentre.x * 16.0);
          }

          double maxDeviationPixels = Math.max(yDeviationPixels, Math.max(xDeviationPixels, zDeviationPixels));
          if (maxDeviationPixels < closestDistance) {
            closestDistance = maxDeviationPixels;
          }

        }
      }
    }

    if (closestDistance == Float.MAX_VALUE) return MISS_VALUE;
    final int OUTERMOST_RING = 6;
    int ringHit = MathHelper.floor(closestDistance);
    return (ringHit <= OUTERMOST_RING) ? ringHit : MISS_VALUE;
  }

  private static final int INNERMOST_RING_DISTANCE = 0;
  private static final int OUTERMOST_RING_DISTANCE = 6;
  private static final int NO_ARROW = OUTERMOST_RING_DISTANCE + 1;

  /**
   * Find the point [x,y,z] that corresponds to where the arrow has struck the face of the target
   * @param arrow
   * @param targetAABB
   * @return
   */
  private static Vec3d getArrowIntersectionWithTarget(AbstractArrowEntity arrow, AxisAlignedBB targetAABB)
  {
    // create a vector that points in the same direction as the arrow.
    // Start with a vector pointing south - this corresponds to 0 degrees yaw and 0 degrees pitch
    // Then rotate about the x-axis to pitch up or down, then rotate about the y axis to yaw
    Vec3d arrowDirection = new Vec3d(0.0, 0.0, 10.0);
    float rotationPitchRadians = (float)Math.toRadians(arrow.rotationPitch);
    float rotationYawRadians = (float)Math.toRadians(arrow.rotationYaw);

    arrowDirection = arrowDirection.rotatePitch(-rotationPitchRadians);
    arrowDirection = arrowDirection.rotateYaw(+rotationYawRadians);

    Vec3d arrowRayOrigin = arrow.getPositionVector();
    Vec3d arrowRayEndpoint = arrowRayOrigin.add(arrowDirection);
    RayTraceResult hitLocation = targetAABB.calculateIntercept(arrowRayOrigin, arrowRayEndpoint);
    if (hitLocation == null) return null;
    if (hitLocation.typeOfHit != RayTraceResult.Type.BLOCK) return null;
    return hitLocation.hitVec;
  }

  // ---- methods to control placement of the target (must be on a solid wall)

  // When a neighbour changes - check if the supporting wall has been demolished
  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos neighbourPos)
  {
    if (!worldIn.isRemote) { // server side only
      Direction enumfacing = (Direction) state.getValue(PROPERTYFACING);
      Direction directionOfNeighbour = enumfacing.getOpposite();
      if (!adjacentBlockIsASuitableSupport(worldIn, pos, directionOfNeighbour)) {
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
      }
    }
  }

  /**
   * Can we place the block at this location?
   * @param worldIn
   * @param thisBlockPos    the position of this block (not the neighbour)
   * @param faceOfNeighbour the face of the neighbour that is adjacent to this block.  If I am facing east, with a stone
   *                        block to the east of me, and I click on the westward-pointing face of the block,
   *                        faceOfNeighbour is WEST
   * @return true if the block can be placed here
   */
  @Override
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos thisBlockPos, Direction faceOfNeighbour)
  {
    Direction directionOfNeighbour = faceOfNeighbour.getOpposite();
    if (directionOfNeighbour == Direction.DOWN || directionOfNeighbour == Direction.UP) {
      return false;
    }
    return adjacentBlockIsASuitableSupport(worldIn, thisBlockPos, directionOfNeighbour);
  }

  // Create the appropriate state for the block being placed - in this case, figure out which way the target is facing
  @Override
  public BlockState getStateForPlacement(World worldIn, BlockPos thisBlockPos, Direction faceOfNeighbour,
                                   float hitX, float hitY, float hitZ, int meta, LivingEntity placer)
  {
    Direction directionTargetIsPointing = faceOfNeighbour;
//    if

    return this.getDefaultState().withProperty(PROPERTYFACING, directionTargetIsPointing);
  }

  // Is the neighbouring block in the given direction suitable for mounting the target onto?
  private boolean adjacentBlockIsASuitableSupport(World world, BlockPos thisPos, Direction directionOfNeighbour)
  {
    BlockPos neighbourPos = thisPos.offset(directionOfNeighbour);
    Direction neighbourSide = directionOfNeighbour.getOpposite();
    boolean DEFAULT_SOLID_VALUE = false;
    return world.isSideSolid(neighbourPos, neighbourSide, DEFAULT_SOLID_VALUE);
  }

  //--- methods related to the appearance of the block
  //  See MBE03_block_variants for more explanation

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other block.
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isOpaqueCube(BlockState iBlockState)
  {
    return false;
  }

  // used by the renderer to control lighting and visibility of other block, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isFullCube(BlockState iBlockState)
  {
    return false;
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  /**
   * Returns the borders of the target, depends on which way it is facing.
   * Used by the vanilla getCollisionBox.
   * @param state
   * @param source
   * @param pos
   * @return the AxisAlignedBoundingBox of the target, origin at [0,0,0].
   */
  @Override
  public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
  {
    Direction facing = (Direction) state.getValue(PROPERTYFACING);

    switch (facing) {
      case NORTH: {
        return  NORTH_AABB;
      }
      case WEST: {
        return WEST_AABB;
      }
      case EAST: {
        return  EAST_AABB;
      }
      case SOUTH: {
        return SOUTH_AABB;
      }
    }
    return FULL_BLOCK_AABB;
  }

  private final AxisAlignedBB NORTH_AABB = getAABBFromPixels(0, 0, 15, 16, 16, 16);
  private final AxisAlignedBB SOUTH_AABB = getAABBFromPixels(0, 0, 0, 16, 16, 1);
  private final AxisAlignedBB EAST_AABB = getAABBFromPixels(0, 0, 0, 1, 16, 16);
  private final AxisAlignedBB WEST_AABB = getAABBFromPixels(15, 0, 0, 16, 16, 16);

  @Override
  public Block setBlockUnbreakable() {
    return super.setBlockUnbreakable();
  }

  private AxisAlignedBB getAABBFromPixels(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
  {
    final float PIXEL_WIDTH = 1.0F / 16.0F;
    return new AxisAlignedBB(minX * PIXEL_WIDTH, minY * PIXEL_WIDTH, minZ * PIXEL_WIDTH,
                             maxX * PIXEL_WIDTH, maxY * PIXEL_WIDTH, maxZ * PIXEL_WIDTH);
  }
  // ---------methods related to storing information about the block (which way it's facing, the power level)

  // BlockRedstoneTarget has two properties:
  //  1) The direction it's facing - eg EAST means that the red and white rings on the target are pointing east
  //  2) The distance from the best arrow to the centre of the target.
  //
  private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  // Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
  private static final IntegerProperty ARROW_DISTANCE_FROM_CENTRE = IntegerProperty.create("arrow_distance", INNERMOST_RING_DISTANCE, NO_ARROW);
  // INNERMOST_RING_DISTANCE = centre of target
  // OUTERMOST_RING_DISTANCE = outermost edge of target
  // NO_ARROW = there are no arrows stuck in the target

  // necessary to define which properties your block use
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(ARROW_DISTANCE_FROM_CENTRE).add(FACING);
  }

}
