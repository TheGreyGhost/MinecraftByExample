package minecraftbyexample.mbe21_tileentityspecialrenderer;

import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 */
public class TileEntitySpecialRendererMBE21 extends TileEntitySpecialRenderer
{
  /**
   * render the tile entity - called every frame while the tileentity is in view of the player
   * @param tileEntity the associated tile entity
   * @param relativeX the x distance from the player's eye to the tileentity
   * @param relativeY the y distance from the player's eye to the tileentity
   * @param relativeZ the z distance from the player's eye to the tileentity
   * @param partialTicks the fraction of a tick that this frame is being rendered at - used to interpolate frames between
   *                     ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
   *                     this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
   * @param blockDamageProgress the progress of the block being damaged (0 - 10), if relevant.  -1 if not relevant.
   */
  @Override
  public void renderTileEntityAt(TileEntity tileEntity, double relativeX, double relativeY, double relativeZ, float partialTicks, int blockDamageProgress)
  {
    if (!(tileEntity instanceof TileEntityMBE21)) return; // should never happen
    TileEntityMBE21 tileEntityMBE21 = (TileEntityMBE21)tileEntity;

    // the gem changes its appearance and animation as the player approaches.
    // When the player is a long distance away, the gem is dark, resting in the hopper, and does not rotate.
    // As the player approaches closer than 16 blocks, the gem first starts to glow brighter and to spin clockwise
    // When the player gets closer than 4 blocks, the gem is at maximum speed and brightness, and starts to levitate above the pedestal
    // Once the player gets closer than 1 block, the gem reaches maximum height.

    // the appearance and animation of the gem is hence made up of several parts:
    // 1) the colour of the gem, which is contained in the tileEntity
    // 2) the brightness of the gem, which depends on player distance
    // 3) the distance that the gem rises above the pedestal, which depends on player distance
    // 4) the speed at which the gem is spinning, which depends on player distance.

    final double pedestalCentreOffsetX = 0.5;
    final double pedestalCentreOffsetY = 0.8;
    final double pedestalCentreOffsetZ = 0.5;

    Vec3 playerEye = new Vec3(0.0, 0.0, 0.0);
    Vec3 pedestalCentre = new Vec3(relativeX + pedestalCentreOffsetX, relativeY + pedestalCentreOffsetY, relativeZ + pedestalCentreOffsetZ);
    double playerDistance = playerEye.distanceTo(pedestalCentre);

    final double DISTANCE_FOR_MIN_SPIN = 8.0;
    final double DISTANCE_FOR_MAX_SPIN = 4.0;
    final double DISTANCE_FOR_MIN_GLOW = 16.0;
    final double DISTANCE_FOR_MAX_GLOW = 4.0;
    final double DISTANCE_FOR_MIN_LEVITATE = 4.0;
    final double DISTANCE_FOR_MAX_LEVITATE = 2.0;

    final double MIN_LEVITATE_HEIGHT = 0.0;
    final double MAX_LEVITATE_HEIGHT = 0.5;
    double gemCentreOffsetX = pedestalCentreOffsetX;
    double gemCentreOffsetY = pedestalCentreOffsetY + UsefulFunctions.interpolate(playerDistance, DISTANCE_FOR_MIN_LEVITATE, DISTANCE_FOR_MAX_LEVITATE,
            MIN_LEVITATE_HEIGHT, MAX_LEVITATE_HEIGHT);
    double gemCentreOffsetZ = pedestalCentreOffsetZ;

    final double MIN_GLOW = 0.6;
    final double MAX_GLOW = 1.0;
    double glowMultiplier = UsefulFunctions.interpolate(playerDistance, DISTANCE_FOR_MIN_GLOW, DISTANCE_FOR_MAX_GLOW,
                                                                        MIN_GLOW, MAX_GLOW );

    final double MIN_REV_PER_SEC = 0.0;
    final double MAX_REV_PER_SEC = 0.5;
    double revsPerSecond = UsefulFunctions.interpolate(playerDistance, DISTANCE_FOR_MIN_SPIN, DISTANCE_FOR_MAX_SPIN,
                                                                       MIN_REV_PER_SEC, MAX_REV_PER_SEC);
    double position = tileEntityMBE21.getNextAngularPosition(revsPerSecond);

    // save the transformation matrix and the rendering attributes, so that we can restore them after rendering.  This
    //   prevents us disrupting any vanilla TESR that render after ours.
    //  using try..finally is not essential but helps make it more robust in case of exceptions
    // For further information on rendering using the Tessellator, see http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
    try {
      GlStateManager.pushMatrix();
      GlStateManager.pushAttrib();

      // First we need to set up the translation so that we render our gem with the bottom point at 0,0,0
      // when the renderTileEntityAt method is called, the tessellator is set up so that drawing a dot at [0,0,0] corresponds to the player's eyes
      // This means that, in order to draw a dot at the TileEntity [x,y,z], we need to translate the reference frame by the difference between the
      // two points, i.e. by the [relativeX, relativeY, relativeZ] passed to the method.  If you then draw a cube from [0,0,0] to [1,1,1], it will
      // render exactly over the top of the TileEntity's block.
      // In this example, the zero point of our model needs to be in the middle of the block, not at the [x,y,z] of the block, so we need to
      // add an extra offset as well, i.e. [gemCentreOffsetX, gemCentreOffsetY, gemCentreOffsetZ]

      gemCentreOffsetX = 0.5; gemCentreOffsetZ = 0.5; gemCentreOffsetY = 1.0;   //todo remove
      GlStateManager.translate(relativeX + gemCentreOffsetX, relativeY + gemCentreOffsetY, relativeZ + gemCentreOffsetZ);

//      GlStateManager.rotate((float) position, 0, 1, 0);   // rotate around the vertical axis //todo remove
      final double GEM_HEIGHT = 0.5;        // desired render height of the gem
      final double MODEL_HEIGHT = 1.0;      // actual height of the gem in the vertexTable
      final double SCALE_FACTOR = GEM_HEIGHT / MODEL_HEIGHT;
//      GlStateManager.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);                        //todo remove
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      this.bindTexture(gemTexture);
      // set the key rendering flags appropriately...
      GlStateManager.disableLighting();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);

      // set the rendering colour as the gem base colour multiplied by the glow brightness
      Color fullBrightnessColor = tileEntityMBE21.getGemColour();
      if (fullBrightnessColor == TileEntityMBE21.INVALID_COLOR) {
        worldrenderer.setColorOpaque(0, 0, 0);
      } else {
        float red = (float)(fullBrightnessColor.getRed() * glowMultiplier);
        float green = (float)(fullBrightnessColor.getGreen() * glowMultiplier);
        float blue = (float)(fullBrightnessColor.getBlue() * glowMultiplier);
        worldrenderer.setColorOpaque_F(red, green, blue);
      }

      addGemVertices(worldrenderer);
      tessellator.draw();
    } finally {
      GlStateManager.popAttrib();
      GlStateManager.popMatrix();
    }
  }

  // add the vertices for drawing the gem.  Generated using a model builder; pasted manually because the object model
  //   loader is (not yet?) implemented.
  private void addGemVertices(WorldRenderer worldrenderer) {
    worldrenderer.startDrawing(GL11.GL_TRIANGLES);
    final double[][] vertexTable = {
            {0.000,1.000,0.000,0.000,0.118},          //1
            {-0.354,0.500,-0.354,0.000,0.354},
            {-0.354,0.500,0.354,0.236,0.236},
            {-0.354,0.500,0.354,0.236,0.236},         //2
            {-0.354,0.500,-0.354,0.000,0.354},
            {0.000,0.000,0.000,0.236,0.471},
            {-0.354,0.500,0.354,0.236,0.236},         //3
            {0.000,0.000,0.000,0.236,0.471},
            {0.354,0.500,0.354,0.471,0.354},
            {-0.354,0.500,0.354,0.236,0.236},         //4
            {0.354,0.500,0.354,0.471,0.354},
            {0.000,1.000,0.000,0.471,0.118},
            {0.000,1.000,0.000,0.471,0.118},          //5
            {0.354,0.500,0.354,0.471,0.354},
            {0.354,0.500,-0.354,0.707,0.236},
            {0.354,0.500,-0.354,0.707,0.236},         //6
            {0.354,0.500,0.354,0.471,0.354},
            {0.000,0.000,0.000,0.707,0.471},
            {0.354,0.500,-0.354,0.707,0.236},         //7
            {0.000,0.000,0.000,0.707,0.471},
            {-0.354,0.500,-0.354,0.943,0.354},
            {0.000,1.000,0.000,0.943,0.118},          //8
            {0.354,0.500,-0.354,0.707,0.236},
            {-0.354,0.500,-0.354,0.943,0.354}
              };

    for (double [] vertex : vertexTable) {
      worldrenderer.addVertexWithUV(vertex[0], vertex[1], vertex[2], vertex[3], vertex[4]);
    }
  }

   private static final ResourceLocation gemTexture = new ResourceLocation("minecraftbyexample:textures/entity/mbe21_tesr_gem.png");
}
