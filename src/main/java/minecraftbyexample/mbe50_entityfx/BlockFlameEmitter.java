package minecraftbyexample.mbe50_entityfx;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 03/11/2015
 *
 * BlockFlameEmitter is a simple block made from a couple of smaller pieces.
 * See mbe02_block_partial for more information
 * The interesting part for EntityFX is randomDisplayTick(), which spawns our FlameFX... see below.
 */
public class BlockFlameEmitter extends Block
{
  public BlockFlameEmitter()
  {
    super(Material.rock);
    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
  }

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to true because this block is opaque and occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to true because this block occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isFullCube() {
    return true;
  }

  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public int getRenderType() {
    return 3;
  }


  // This method is called at random intervals - typically used by blocks which produce occasional effects, like
  //  smoke from a torch or stars from a portal.
  //  In this case, we use it to spawn two different types of EntityFX- vanilla, or custom.
  // Don't forget     @SideOnly(Side.CLIENT) otherwise this will crash on a dedicated server.
  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
  {
    // EntityFX must be spawned on the client only.
    // If you want the server to be able to spawn EntityFX, you need to send a network message to the client and get the
    //   client to spawn the EntityFX in response to the message (see mbe60 MessageHandlerOnClient for an example).
    if (worldIn.isRemote) {  // is this on the client side?
      // first example:
      // spawn a vanilla particle of LAVA type (smoke from lava)
      //  The starting position is the [x,y,z] of the tip of the pole (i.e. at [0.5, 1.0, 0.5] relative to the block position)
      //  Set the initial velocity to zero.
      // When the particle is spawned, it will automatically add a random amount of velocity - see EntityLavaFX constructor and
      //   EntityFX constructor.  This can be a nuisance if you don't want your EntityFX to have a random starting velocity!  See
      //  second example below for more information.

      double xpos = pos.getX() + 0.5;
      double ypos = pos.getY() + 1.0;
      double zpos = pos.getZ() + 0.5;
      double velocityX = 0; // increase in x position every tick
      double velocityY = 0; // increase in y position every tick;
      double velocityZ = 0; // increase in z position every tick
      int [] extraInfo = new int[0];  // extra information if needed by the particle - in this case unused

      worldIn.spawnParticle(EnumParticleTypes.LAVA, xpos, ypos, zpos, velocityX, velocityY, velocityZ, extraInfo);

      // second example:
      // spawn a custom EntityFX ("FlameFX") with a texture we have added ourselves.
      // FlameFX also has custom movement and collision logic - it moves in a straight line until it hits something.
      // To make it more interesting, the stream of fireballs will target the nearest non-player entity within 16 blocks at
      //   the height of the pole or above.

      // starting position = top of the pole
      xpos = pos.getX() + 0.5;
      ypos = pos.getY() + 1.0;
      zpos = pos.getZ() + 0.5;

      EntityMob mobTarget = getNearestTargetableMob(worldIn, xpos, ypos, zpos);
      Vec3 fireballDirection;
      if (mobTarget == null) { // no target: fire straight upwards
        fireballDirection = new Vec3(0.0, 1.0, 0.0);
      } else {  // otherwise: aim at the mob
        // the direction that the fireball needs to travel is calculated from the starting point (the pole) and the
        //   end point (the mob's eyes).  A bit of googling on vector maths will show you that you calculate this by
        //  1) subtracting the start point from the end point
        //  2) normalising the vector (if you don't do this, then the fireball will fire faster if the mob is further away

        fireballDirection = mobTarget.getPositionEyes(1.0F).subtract(xpos, ypos, zpos);  // NB this method only works on client side
        fireballDirection = fireballDirection.normalize();
      }

      // the velocity vector is now calculated as the fireball's speed multiplied by the direction vector.

      final double SPEED_IN_BLOCKS_PER_SECOND = 2.0;
      final double TICKS_PER_SECOND = 20;
      final double SPEED_IN_BLOCKS_PER_TICK = SPEED_IN_BLOCKS_PER_SECOND / TICKS_PER_SECOND;

      velocityX = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.xCoord; // how much to increase the x position every tick
      velocityY = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.yCoord; // how much to increase the y position every tick
      velocityZ = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.zCoord; // how much to increase the z position every tick

      FlameFX newEffect = new FlameFX(worldIn, xpos, ypos, zpos, velocityX, velocityY, velocityZ);
      Minecraft.getMinecraft().effectRenderer.addEffect(newEffect);
    }
  }

  /**
   * Returns the nearest targetable mob to the indicated [xpos, ypos, zpos].
   * @param world
   * @param xpos [x,y,z] position to s
   * @param ypos
   * @param zpos
   * @return the nearest mob, or null if none within range.
   */
  private EntityMob getNearestTargetableMob(World world, double xpos, double ypos, double zpos) {
    final double TARGETING_DISTANCE = 16;
    AxisAlignedBB targetRange = AxisAlignedBB.fromBounds(xpos - TARGETING_DISTANCE,
                                                                ypos,
                                                                zpos - TARGETING_DISTANCE,
                                                                xpos + TARGETING_DISTANCE,
                                                                ypos + TARGETING_DISTANCE,
                                                                zpos + TARGETING_DISTANCE);

    List<EntityMob> allNearbyMobs = world.getEntitiesWithinAABB(EntityMob.class, targetRange);
    EntityMob nearestMob = null;
    double closestDistance = Double.MAX_VALUE;
    for (EntityMob nextMob : allNearbyMobs) {
      double nextClosestDistance = nextMob.getDistanceSq(xpos, ypos, zpos);
      if (nextClosestDistance < closestDistance) {
        closestDistance = nextClosestDistance;
        nearestMob = nextMob;
      }
    }
    return nearestMob;
  }

}
