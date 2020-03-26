package minecraftbyexample.mbe50_particle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 03/11/2015
 *
 * BlockFlameEmitter is a simple block made from a couple of smaller pieces.
 * See mbe02_block_partial for more information
 * The interesting part for Particle is animateTick(), which spawns our FlameParticle... see below.
 */
public class BlockFlameEmitter extends Block
{
  public BlockFlameEmitter()
  {
    super(Block.Properties.create(Material.ROCK));
  }

  // for this model, we're making the shape match the block model exactly
  //    - see assets\minecraftbyexample\models\block\mbe50_block_flame_emitter_model.json
  private static final Vec3d BASE_MIN_CORNER = new Vec3d(2.0, 0.0, 0.0);
  private static final Vec3d BASE_MAX_CORNER = new Vec3d(14.0, 1.0, 16.0);
  private static final Vec3d PILLAR_MIN_CORNER = new Vec3d(7.0, 1.0, 6.0);
  private static final Vec3d PILLAR_MAX_CORNER = new Vec3d(9.0, 8.0, 10.0);

  private static final VoxelShape BASE = Block.makeCuboidShape(BASE_MIN_CORNER.getX(), BASE_MIN_CORNER.getY(), BASE_MIN_CORNER.getZ(),
          BASE_MAX_CORNER.getX(), BASE_MAX_CORNER.getY(), BASE_MAX_CORNER.getZ());
  private static final VoxelShape PILLAR = Block.makeCuboidShape(PILLAR_MIN_CORNER.getX(), PILLAR_MIN_CORNER.getY(), PILLAR_MIN_CORNER.getZ(),
          PILLAR_MAX_CORNER.getX(), PILLAR_MAX_CORNER.getY(), PILLAR_MAX_CORNER.getZ());

  private static VoxelShape COMBINED_SHAPE = VoxelShapes.or(BASE, PILLAR);  // use this method to add two shapes together

  // returns the shape of the block:
  //  The image that you see on the screen (when a block is rendered) is determined by the block model (i.e. the model json file).
  //  But Minecraft also uses a number of other "shapes" to control the interaction of the block with its environment and with the player.
  // See  https://greyminecraftcoder.blogspot.com/2020/02/block-shapes-voxelshapes-1144.html
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return COMBINED_SHAPE;
  }

  // This method is called at random intervals - typically used by block which produce occasional effects, like
  //  smoke from a torch or stars from a portal.
  //  In this case, we use it to spawn two different types of Particle- vanilla, or custom.
  // Don't forget     @OnlyIn(Dist.CLIENT) otherwise this will crash on a dedicated server.
  @Override
  @OnlyIn(Dist.CLIENT)
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
  {
    // Usually, it is desirable to spawn particles on the client only.
    // It is possible for the server to send a "spawn particle" command to the client, but this requires a lot of bandwidth if
    //   you are rendering even modest amounts of particles.
    // Instead, send a custom message to the client and spawn lots of particles in response to the single message
    // (see mbe60 MessageHandlerOnClient for an example).
    if (worldIn.isRemote) {  // is this on the client side?  should always be true...

      // first example:
      // spawn a vanilla particle of LAVA type (smoke from lava)
      //  The starting position is the [x,y,z] of the tip of the pole (i.e. at [0.5, 1.0, 0.5] relative to the block position)
      //  Set the initial velocity to zero.
      // When the particle is spawned, it will automatically add a random amount of velocity - see EntityLavaFX constructor and
      //   Particle constructor.  This can be a nuisance if you don't want your Particle to have a random starting velocity!  See
      //  second example below for more information.

      double xpos = pos.getX() + 0.5;
      double ypos = pos.getY() + 1.0;
      double zpos = pos.getZ() + 0.5;
      double velocityX = 0; // increase in x position every tick
      double velocityY = 0; // increase in y position every tick;
      double velocityZ = 0; // increase in z position every tick
      int [] extraInfo = new int[0];  // extra information if needed by the particle - in this case unused

      final boolean IGNORE_RANGE_CHECK = false; // if true, always render particle regardless of how far away the player is
      worldIn.addParticle(ParticleTypes.LAVA, IGNORE_RANGE_CHECK,
              xpos, ypos, zpos, velocityX, velocityY, velocityZ);

      // second example:
      // spawn a custom Particle ("FlameParticle") with a texture we have added ourselves.
      // FlameParticle also has custom movement and collision logic - it moves in a straight line until it hits something.
      // To make it more interesting, the stream of fireballs will target the nearest non-player entity within 16 blocks at
      //   the height of the pole or above.

      // starting position = top of the pole
      xpos = pos.getX() + 0.5;
      ypos = pos.getY() + 1.0;
      zpos = pos.getZ() + 0.5;

      MonsterEntity mobTarget = getNearestTargetableMob(worldIn, xpos, ypos, zpos);
      Vec3d fireballDirection;
      if (mobTarget == null) { // no target: fire straight upwards
        fireballDirection = new Vec3d(0.0, 1.0, 0.0);
      } else {  // otherwise: aim at the mob
        // the direction that the fireball needs to travel is calculated from the starting point (the pole) and the
        //   end point (the mob's eyes).  A bit of googling on vector maths will show you that you calculate this by
        //  1) subtracting the start point from the end point
        //  2) normalising the vector (if you don't do this, then the fireball will fire faster if the mob is further away

        final float PARTIAL_TICKS = 1.0F;
        fireballDirection = mobTarget.getEyePosition(PARTIAL_TICKS).subtract(xpos, ypos, zpos);  // NB this method only works on client side
        fireballDirection = fireballDirection.normalize();
      }

      // the velocity vector is now calculated as the fireball's speed multiplied by the direction vector.

      final double SPEED_IN_BLOCKS_PER_SECOND = 2.0;
      final double TICKS_PER_SECOND = 20;
      final double SPEED_IN_BLOCKS_PER_TICK = SPEED_IN_BLOCKS_PER_SECOND / TICKS_PER_SECOND;

      velocityX = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.x; // how much to increase the x position every tick
      velocityY = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.y; // how much to increase the y position every tick
      velocityZ = SPEED_IN_BLOCKS_PER_TICK * fireballDirection.z; // how much to increase the z position every tick

      Color tint = getTint(pos);
      double diameter = getDiameter(pos);

      FlameParticleData flameParticleData = new FlameParticleData(tint, diameter);
      worldIn.addParticle(ParticleTypes.LAVA, IGNORE_RANGE_CHECK,
              xpos, ypos, zpos, velocityX, velocityY, velocityZ);
    }
  }

  // choose a semi-random colour based on the block's position
  private Color getTint(BlockPos blockPos) {
    Color [] tints = {
            new Color(1.0f, 1.0f, 1.0f),  // no tint (full white)
            new Color(1.0f, 0.5f, 0.5f),  // redder
            new Color(0.5f, 1.0f, 0.5f),  // greener
            new Color(0.5f, 0.5f, 1.0f),  // bluer
            new Color(1.0f, 1.0f, 0.5f),  // yellower
            new Color(1.0f, 0.5f, 1.0f),  // purpler
            new Color(0.5f, 1.0f, 1.0f),  // cyan-er
    };

    Random random = new Random(blockPos.hashCode());

    int idx = random.nextInt(tints.length);
    return tints[idx];
  }

  // choose a semi-random size based on the block's position
  private double getDiameter(BlockPos blockPos) {
    Random random = new Random(blockPos.hashCode());
    final double MIN_DIAMETER = 0.05;
    final double MAX_DIAMETER = 0.25;
    return MIN_DIAMETER + (MAX_DIAMETER - MIN_DIAMETER) *random.nextDouble();
  }

  /**
   * Returns the nearest targetable mob to the indicated [xpos, ypos, zpos].
   * @param world
   * @param xpos [x,y,z] position to s
   * @param ypos
   * @param zpos
   * @return the nearest mob, or null if none within range.
   */
  private MonsterEntity getNearestTargetableMob(World world, double xpos, double ypos, double zpos) {
    final double TARGETING_DISTANCE = 16;
    AxisAlignedBB targetRange = new AxisAlignedBB(xpos - TARGETING_DISTANCE,
                                                  ypos,
                                                  zpos - TARGETING_DISTANCE,
                                                  xpos + TARGETING_DISTANCE,
                                                  ypos + TARGETING_DISTANCE,
                                                  zpos + TARGETING_DISTANCE);

    List<MonsterEntity> allNearbyMobs = world.getEntitiesWithinAABB(MonsterEntity.class, targetRange);
    MonsterEntity nearestMob = null;
    double closestDistance = Double.MAX_VALUE;
    for (MonsterEntity nextMob : allNearbyMobs) {
      double nextClosestDistance = nextMob.getDistanceSq(xpos, ypos, zpos);
      if (nextClosestDistance < closestDistance) {
        closestDistance = nextClosestDistance;
        nearestMob = nextMob;
      }
    }
    return nearestMob;
  }

}
