package minecraftbyexample.mbe50_entityfx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 * Custom EntityFX to illustrate how to add an EntityFX with your own texture and movement/animation behaviour
 */
public class FlameFX extends EntityFX
{
  private final ResourceLocation flameRL = new ResourceLocation("minecraftbyexample:entity/flame_fx");

  /**
   * Construct a new FlameFX at the given [x,y,z] position with the given initial velocity.
   */
  public FlameFX(World world, double x, double y, double z,
                 double velocityX, double velocityY, double velocityZ)
  {
    super(world, x, y, z, velocityX, velocityY, velocityZ);

    particleGravity = Blocks.fire.blockParticleGravity;  /// arbitrary block!  not required here since we have
    // overriden onUpdate()
    particleMaxAge = 100; // not used since we have overridden onUpdate

    final float ALPHA_VALUE = 0.99F;
    this.particleAlpha = ALPHA_VALUE;  // a value less than 1 turns on alpha blending. Otherwise, alpha blending is off
    // and the particle won't be transparent.

    //the vanilla EntityFX constructor added random variation to our starting velocity.  Undo it!
    motionX = velocityX;
    motionY = velocityY;
    motionZ = velocityZ;

    // set the texture to the flame texture, which we have previously added using TextureStitchEvent
    TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(flameRL.toString());
    func_180435_a(sprite);  // initialise the icon to our custom texture
  }

  /**
   * Used to control what texture and lighting is used for the EntityFX.
   * Returns 1, which means "use a texture from the blocks + items texture sheet"
   * The vanilla layers are:
   * normal particles: ignores world brightness lighting map
   * Layer 0 - uses the particles texture sheet (textures\particle\particles.png)
   * Layer 1 - uses the blocks + items texture sheet
   * <p/>
   * lit particles: changes brightness depening on world lighting i.e. block light + sky light
   * Layer 3 - uses the blocks + items texture sheet (I think)
   *
   * @return
   */
  @Override
  public int getFXLayer()
  {
    return 1;
  }

  // can be used to change the brightness of the rendered EntityFX.
  @Override
  public int getBrightnessForRender(float partialTick)
  {
    final int FULL_BRIGHTNESS_VALUE = 0xf000f0;
    return FULL_BRIGHTNESS_VALUE;
  }

  // this function is used by EffectRenderer.addEffect() to determine whether depthmask writing should be on or not.
  // by default, vanilla turns off depthmask writing for entityFX with alphavalue less than 1.0
  // FlameBreathFX uses alphablending (i.e. the FX is partially transparent) but we want depthmask writing on,
  //   otherwise translucent objects (such as water) render over the top of our breath, even if the breath is in front
  //  of the water and not behind
  @Override
  public float func_174838_j()
  {
    return 1.0F;
  }

  /**
   * call once per tick to update the EntityFX position, calculate collisions, remove when max lifetime is reached, etc
   */
  @Override
  public void onUpdate()
  {
    prevPosX = posX;
    prevPosY = posY;
    prevPosZ = posZ;

    moveEntity(motionX, motionY, motionZ);

    // collision with a block makes the ball disappear.  But does not collide with entities
    if (isCollided) {
      this.setDead();
    }

    if (this.particleMaxAge-- <= 0) {
      this.setDead();
    }
  }

  /**
   * Render the EntityFX onto the screen.  For more help with the tessellator see
   * http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
   * <p/>
   * You don't actually need to override this method, this is just a deobfuscated example of the vanilla, to show you
   * how it works in case you want to do something a bit unusual.
   * <p/>
   * The EntityFX is rendered as a two-dimensional object (Quad) in the world (three-dimensional coordinates).
   * The corners of the quad are chosen so that the EntityFX is drawn directly facing the viewer (or in other words,
   * so that the quad is always directly face-on to the screen.)
   * In order to manage this, it needs to know two direction vectors:
   * 1) the 3D vector direction corresponding to left-right on the viewer's screen (edgeLRdirection)
   * 2) the 3D vector direction corresponding to up-down on the viewer's screen (edgeUDdirection)
   * These two vectors are calculated by the caller.
   * For example, the top right corner of the quad on the viewer's screen is equal to the centre point of the quad
   * (x,y,z)
   * plus the edgeLRdirection vector multiplied by half the quad's width, plus the edgeUDdirection vector multiplied
   * by the quad's height.
   * NB edgeLRdirectionY is not provided because it's always 0, i.e. the top of the viewer's screen is always directly
   * up so moving left-right on the viewer's screen doesn't affect the y coordinate position in the world
   *
   * @param worldRenderer
   * @param entity
   * @param partialTick
   * @param edgeLRdirectionX edgeLRdirection[XYZ] is the vector direction pointing left-right on the player's screen
   * @param edgeUDdirectionY edgeUDdirection[XYZ] is the vector direction pointing up-down on the player's screen
   * @param edgeLRdirectionZ edgeLRdirection[XYZ] is the vector direction pointing left-right on the player's screen
   * @param edgeUDdirectionX edgeUDdirection[XYZ] is the vector direction pointing up-down on the player's screen
   * @param edgeUDdirectionZ edgeUDdirection[XYZ] is the vector direction pointing up-down on the player's screen
   */
  @Override
  public void func_180434_a(WorldRenderer worldRenderer, Entity entity, float partialTick,
                            float edgeLRdirectionX, float edgeUDdirectionY, float edgeLRdirectionZ,
                            float edgeUDdirectionX, float edgeUDdirectionZ)
  {
    double minU = this.particleIcon.getMinU();
    double maxU = this.particleIcon.getMaxU();
    double minV = this.particleIcon.getMinV();
    double maxV = this.particleIcon.getMaxV();

    double scale = 0.1F * this.particleScale;  // vanilla scaling factor
    final double scaleLR = scale;
    final double scaleUD = scale;
    double x = this.prevPosX + (this.posX - this.prevPosX) * partialTick - interpPosX;
    double y = this.prevPosY + (this.posY - this.prevPosY) * partialTick - interpPosY;
    double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTick - interpPosZ;

    worldRenderer.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
    worldRenderer.addVertexWithUV(x - edgeLRdirectionX * scaleLR - edgeUDdirectionX * scaleUD,
                                         y - edgeUDdirectionY * scaleUD,
                                         z - edgeLRdirectionZ * scaleLR - edgeUDdirectionZ * scaleUD,
                                         maxU, maxV);
    worldRenderer.addVertexWithUV(x - edgeLRdirectionX * scaleLR + edgeUDdirectionX * scaleUD,
                                         y + edgeUDdirectionY * scaleUD,
                                         z - edgeLRdirectionZ * scaleLR + edgeUDdirectionZ * scaleUD,
                                         maxU, minV);
    worldRenderer.addVertexWithUV(x + edgeLRdirectionX * scaleLR + edgeUDdirectionX * scaleUD,
                                         y + edgeUDdirectionY * scaleUD,
                                         z + edgeLRdirectionZ * scaleLR + edgeUDdirectionZ * scaleUD,
                                         minU, minV);
    worldRenderer.addVertexWithUV(x + edgeLRdirectionX * scaleLR - edgeUDdirectionX * scaleUD,
                                         y - edgeUDdirectionY * scaleUD,
                                         z + edgeLRdirectionZ * scaleLR - edgeUDdirectionZ * scaleUD,
                                         minU, maxV);
  }

}
