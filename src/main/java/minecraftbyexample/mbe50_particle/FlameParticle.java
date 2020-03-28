package minecraftbyexample.mbe50_particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 * Custom Particle to illustrate how to add a Particle with your own texture and movement/animation behaviour
 *
 * A good vanilla example is PoofParticle
 *
 */
public class FlameParticle extends SpriteTexturedParticle
{
  /**
   * Construct a new FlameParticle at the given [x,y,z] position, with the given initial velocity, the given color, and the
   *   given diameter.
   *   We also supply sprites so that you can change the sprite texture in the tick() method (although not needed for this example)
   */
  public FlameParticle(World world, double x, double y, double z,
                       double velocityX, double velocityY, double velocityZ,
                       Color tint, double diameter,
                       IAnimatedSprite sprites)
  {
    super(world, x, y, z, velocityX, velocityY, velocityZ);
    this.sprites = sprites;

    setColor(tint.getRed()/255.0F, tint.getGreen()/255.0F, tint.getBlue()/255.0F);
    setSize((float)diameter, (float)diameter);    // the size (width, height) of the collision box.

    final float PARTICLE_SCALE_FOR_ONE_METRE = 0.5F; //  if the particleScale is 0.5, the texture will be rendered as 1 metre high
    particleScale = PARTICLE_SCALE_FOR_ONE_METRE * (float)diameter; // sets the rendering size of the particle for a TexturedParticle.

    maxAge = 100;  // lifetime in ticks: 100 ticks = 5 seconds

    final float ALPHA_VALUE = 1.0F;
    this.particleAlpha = ALPHA_VALUE;

    //the vanilla Particle constructor added random variation to our starting velocity.  Undo it!
    motionX = velocityX;
    motionY = velocityY;
    motionZ = velocityZ;

    this.canCollide = true;  // the move() method will check for collisions with scenery
  }

  // ---- methods used by TexturedParticle.renderParticle() method to find out how to render your particle
  //  the base method just renders a quad, rotated to directly face the player

  // can be used to change the skylight+blocklight brightness of the rendered Particle.
  @Override
  protected int getBrightnessForRender(float partialTick)
  {
    final int BLOCK_LIGHT = 15;  // maximum brightness
    final int SKY_LIGHT = 15;    // maximum brightness
    final int FULL_BRIGHTNESS_VALUE = LightTexture.packLight(BLOCK_LIGHT, SKY_LIGHT);
    return FULL_BRIGHTNESS_VALUE;

    // if you want the brightness to be the local illumination (from block light and sky light) you can just use
    //  the Particle.getBrightnessForRender() base method, which contains:
    //    BlockPos blockPos = new BlockPos(this.posX, this.posY, this.posZ);
    //    return this.world.isBlockLoaded(blockPos) ? WorldRenderer.getCombinedLight(this.world, blockPos) : 0;
  }

  // Choose the appropriate render type for your particles:
  // There are several useful predefined types:
  // PARTICLE_SHEET_TRANSLUCENT semi-transparent (translucent) particles
  // PARTICLE_SHEET_OPAQUE    opaque particles
  // TERRAIN_SHEET            particles drawn from block or item textures
  // PARTICLE_SHEET_LIT       appears to be the same as OPAQUE.  Not sure of the difference.  In previous versions of minecraft,
  //                          "lit" particles changed brightness depending on world lighting i.e. block light + sky light
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  /**
   * call once per tick to update the Particle position, calculate collisions, remove when max lifetime is reached, etc
   */
  @Override
  public void tick()
  {
    // if you want to change the texture as the particle gets older, you can use
    // selectSpriteWithAge(sprites);

    prevPosX = posX;
    prevPosY = posY;
    prevPosZ = posZ;

    move(motionX, motionY, motionZ);  // simple linear motion.  You can change speed by changing motionX, motionY,
      // motionZ every tick.  For example - you can make the particle accelerate downwards due to gravity by
      // final double GRAVITY_ACCELERATION_PER_TICK = -0.02;
      // motionY += GRAVITY_ACCELERATION_PER_TICK;
      // calling move() also calculates collisions with other objects

    // collision with a block makes the ball disappear.  But does not collide with entities
    if (onGround) {  // onGround is only true if the particle collides while it is moving downwards...
      this.setExpired();
    }

    if (prevPosY == posY && motionY > 0) {  // detect a collision while moving upwards (can't move up at all)
      this.setExpired();
    }

    if (this.age++ >= this.maxAge) {
      this.setExpired();
    }
  }

  private final IAnimatedSprite sprites;  // contains a list of textures; choose one using either
  // newParticle.selectSpriteRandomly(sprites); or newParticle.selectSpriteWithAge(sprites);
}
