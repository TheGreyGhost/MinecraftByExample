package minecraftbyexample.mbe06_redstone.output_only;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
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
 *     15 for the bullseye, decreasing for every ring down to 0 for no arrow (or stuck in the wood)
 * For background information on blocks see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 */
public class BlockRedstoneTarget extends Block
{
  public BlockRedstoneTarget()
  {
    super(Material.WOOD);
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
  }

  //----- methods related to redstone

  /**
   * This block can provide power
   * @return
   */
  @Override
  public boolean canProvidePower(IBlockState iBlockState)
  {
    return true;
  }

  /** How much weak power does this block provide to the adjacent block?  In this example - none.
   * See http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html for more information
   * @param worldIn
   * @param pos the position of this block
   * @param state the blockstate of this block
   * @param side the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */
  @Override
  public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos,  EnumFacing side)
  {
    return 0;
  }

  /**
   *  The target provides strong power to the block it's mounted on (hanging on)
   * @param worldIn
   * @param pos the position of this block
   * @param state the blockstate of this block
   * @param side the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */

  @Override
  public int getStrongPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
  {
    EnumFacing targetFacing = (EnumFacing)state.getValue(PROPERTYFACING);

    // only provide strong power through the back of the target.  If the target is facing east, that means
    //   it provides power to the block which lies to the west.
    // When this method is called by the adjacent block which lies to the west, the value of the side parameter is EAST.

    // The amount of power provided is related to how close the arrow hit to the bullseye.
    //  Bullseye = 15; Outermost ring = 3.

    if (side != targetFacing) return 0;
    if (!(worldIn instanceof World)) return 0;
    World world = (World)worldIn;  // We're provided with IBlockAccess instead of World because this is sometimes called
                                   //  during rendering, which is multithreaded and might be called using a ChunkCache.
                                   //  This might mean that the appearance of the adjacent block is sometimes not right,
                                   //   since we always return 0 for a ChunkCache.  I haven't managed to trigger this
                                   //  potential bug, but I can't rule it out.
    int bestRing = findBestArrowRing(world, pos, state);
    if (bestRing < 0) return 0;

    return 15 - 2 * bestRing;
  }

  /**
   * Called with an entity collides with the block.
   * In this case - we check if an arrow has collided.
   * @param worldIn
   * @param pos
   * @param state
   * @param entityIn
   */
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
  {
    EnumFacing targetFacing = (EnumFacing)state.getValue(PROPERTYFACING);

    if (!worldIn.isRemote) {
      if (entityIn instanceof EntityArrow) {
        AxisAlignedBB targetAABB = getCollisionBoundingBox(state, worldIn, pos);
        AxisAlignedBB targetAABBinWorld = targetAABB.offset(pos);
        List<EntityArrow> embeddedArrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, targetAABBinWorld);

        // when a new arrow hits, remove all others which are already embedded

        for (EntityArrow embeddedEntity : embeddedArrows) {
          if (embeddedEntity.getEntityId() != entityIn.getEntityId()) {
            embeddedEntity.setDead();
          }
        }

        // notify my immediate neighbours, and also the immediate neighbours of the block I'm mounted on, because I
        //  am giving strong power to it.
        final boolean CASCADE_UPDATE = false;  // I'm not sure what this flag does, but vanilla always sets it to false
        // except for calls by World.setBlockState()
        worldIn.notifyNeighborsOfStateChange(pos, this, CASCADE_UPDATE);
        EnumFacing directionOfNeighbouringWall = targetFacing.getOpposite();
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
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
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
  private int findBestArrowRing(World worldIn, BlockPos pos, IBlockState state)
  {
    final int MISS_VALUE = -1;
    EnumFacing targetFacing = (EnumFacing)state.getValue(PROPERTYFACING);
    AxisAlignedBB targetAABB = getCollisionBoundingBox(state, worldIn, pos);
    AxisAlignedBB targetAABBinWorld = targetAABB.offset(pos);
    List<EntityArrow> embeddedArrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, targetAABBinWorld);
    if (embeddedArrows.isEmpty()) return MISS_VALUE;

    double closestDistance = Float.MAX_VALUE;
    for (EntityArrow entity : embeddedArrows) {
      if (!entity.isDead && entity instanceof EntityArrow) {
        EntityArrow entityArrow = (EntityArrow) entity;
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

          if (targetFacing == EnumFacing.EAST || targetFacing == EnumFacing.WEST) {
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

  /**
   * Find the point [x,y,z] that corresponds to where the arrow has struck the face of the target
   * @param arrow
   * @param targetAABB
   * @return
   */
  private static Vec3d getArrowIntersectionWithTarget(EntityArrow arrow, AxisAlignedBB targetAABB)
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
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos neighbourPos)
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
   * Can we place the block at this location?
   * @param worldIn
   * @param thisBlockPos    the position of this block (not the neighbour)
   * @param faceOfNeighbour the face of the neighbour that is adjacent to this block.  If I am facing east, with a stone
   *                        block to the east of me, and I click on the westward-pointing face of the block,
   *                        faceOfNeighbour is WEST
   * @return true if the block can be placed here
   */
  @Override
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos thisBlockPos, EnumFacing faceOfNeighbour)
  {
    EnumFacing directionOfNeighbour = faceOfNeighbour.getOpposite();
    if (directionOfNeighbour == EnumFacing.DOWN || directionOfNeighbour == EnumFacing.UP) {
      return false;
    }
    return adjacentBlockIsASuitableSupport(worldIn, thisBlockPos, directionOfNeighbour);
  }

  // Create the appropriate state for the block being placed - in this case, figure out which way the target is facing
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos thisBlockPos, EnumFacing faceOfNeighbour,
                                   float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    EnumFacing directionTargetIsPointing = faceOfNeighbour;
//    if

    return this.getDefaultState().withProperty(PROPERTYFACING, directionTargetIsPointing);
  }

  // Is the neighbouring block in the given direction suitable for mounting the target onto?
  private boolean adjacentBlockIsASuitableSupport(World world, BlockPos thisPos, EnumFacing directionOfNeighbour)
  {
    BlockPos neighbourPos = thisPos.offset(directionOfNeighbour);
    EnumFacing neighbourSide = directionOfNeighbour.getOpposite();
    boolean DEFAULT_SOLID_VALUE = false;
    return world.isSideSolid(neighbourPos, neighbourSide, DEFAULT_SOLID_VALUE);
  }

  //--- methods related to the appearance of the block
  //  See MBE03_block_variants for more explanation

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isOpaqueCube(IBlockState iBlockState)
  {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isFullCube(IBlockState iBlockState)
  {
    return false;
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
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
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    EnumFacing facing = (EnumFacing) state.getValue(PROPERTYFACING);

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
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, new IProperty[] {PROPERTYFACING});
  }
}
