package minecraftbyexample.mbe06_redstone.input_and_output;

import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 27/11/2015
 * This class renders the meter needle on each face of the block.
 * The base model is drawn by the block model, not this class.
 */
public class TileEntitySpecialRendererRedstoneMeter extends TileEntitySpecialRenderer
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
   * @param alpha I'm not sure what this is used for; the name suggests alpha blending but Vanilla doesn't appear to use it
   */
  @Override
  public void render(TileEntity tileEntity, double relativeX, double relativeY, double relativeZ, float partialTicks, int blockDamageProgress, float alpha)
  {
    if (!(tileEntity instanceof TileEntityRedstoneMeter)) return; // should never happen
    TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileEntity;
    double powerLevel = tileEntityRedstoneMeter.getSmoothedNeedlePosition();
    renderNeedleOnFace(powerLevel, relativeX, relativeY, relativeZ, EnumFacing.SOUTH);
    renderNeedleOnFace(powerLevel, relativeX, relativeY, relativeZ, EnumFacing.NORTH);
    renderNeedleOnFace(powerLevel, relativeX, relativeY, relativeZ, EnumFacing.EAST);
    renderNeedleOnFace(powerLevel, relativeX, relativeY, relativeZ, EnumFacing.WEST);
  }

  /**
   * Render the needle at the current power level
   * @param powerLevel 0 - 1
   * @param relativeX the x/y/z distance from the player's eye to the tileentity
   * @param relativeY
   * @param relativeZ
   * @param whichFace which face of the block should the needle render on?
   */
  private void renderNeedleOnFace(double powerLevel, double relativeX, double relativeY, double relativeZ,
                                  EnumFacing whichFace)
  {
    double needleSpindleX = 0.5;
    double needleSpindleY = 0.5;
    double needleSpindleZ = 0.5;
    float faceRotationAngle = 0;
    final float RENDER_NUDGE = 0.001F; // nudge out the needle from its face by a small amount.  If you try to render the
                                       //  needle directly on top of the face, it leads to 'z-fighting' and looks
                                      // awful
    switch (whichFace) {
      case SOUTH: {
        needleSpindleZ = 1.0 + RENDER_NUDGE;
        faceRotationAngle = 0;
        break;
      }
      case NORTH: {
        needleSpindleZ = 0.0 - RENDER_NUDGE;
        faceRotationAngle = 180;
        break;
      }
      case EAST: {
        needleSpindleX = 1.0 + RENDER_NUDGE;
        faceRotationAngle = 90;
        break;
      }
      case WEST: {
        needleSpindleX = 0.0 - RENDER_NUDGE;
        faceRotationAngle = 270;
        break;
      }
      default: {
        System.err.println("Illegal face in renderNeedleOnFace:" + whichFace);
      }
    }

    try {
      // save the transformation matrix and the rendering attributes, so that we can restore them after rendering.  This
      //   prevents us disrupting any vanilla TESR that render after ours.
      //  using try..finally is not essential but helps make it more robust in case of exceptions
      // For further information on rendering using the Tessellator, see http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
      GL11.glPushMatrix();
      GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

      // First we need to set up the translation so that we render our needle with the centre point at 0,0,0
      // Our model is set up so that the centre is the spindle of the needle, i.e. the point that the needle rotates around
      // When the renderTileEntityAt method is called, the tessellator is set up so that drawing a dot at [0,0,0] corresponds to the player's eyes
      // This means that, in order to draw a dot at the TileEntity [x,y,z], we need to translate the reference frame by the difference between the
      // two points, i.e. by the [relativeX, relativeY, relativeZ] passed to the method.  If you then draw a cube from [0,0,0] to [1,1,1], it will
      // render exactly over the top of the TileEntity's block.
      // In this example, the zero point of our model needs to be the spindle, not at the [x,y,z] of the block, so we need to
      // add an extra offset as well, i.e. [needleSpindleX, needleSpindleY, needleSpindleZ]
      GlStateManager.translate(relativeX + needleSpindleX, relativeY + needleSpindleY, relativeZ + needleSpindleZ);

      // we now need to rotate the needle:
      // 1) around the z axis, to spin the needle to show the correct power level
      // 2) around the vertical axis, to move it to the correct face (EAST, WEST, SOUTH, NORTH)
      // Rendering transformations take effect in "reverse order" so we have to do needle second

      // zero degrees is down, increases clockwise with increasing power level
      final double ZERO_ANGLE = 45;
      final double MAX_ANGLE = 315;
      float needleAngle = (float)UsefulFunctions.interpolate(powerLevel, 0, 1, ZERO_ANGLE, MAX_ANGLE);

      GlStateManager.rotate(faceRotationAngle, 0, 1, 0);   // rotate around the vertical axis to make face correct direction

      GlStateManager.rotate(-needleAngle, 0, 0, 1);   // rotate around the Z axis (our model is designed that way).  -ve because
                                                      //  we need to rotate clockwise but rotate expects anticlockwise

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
//      this.bindTexture(needleTexture);         // we don't need a texture for the needle appearance - we're just using
                                                 //  solid colour

      // set the key rendering flags appropriately...
      GL11.glDisable(GL11.GL_LIGHTING);     // turn off "item" lighting (face brightness depends on which direction it is facing)
      GL11.glDisable(GL11.GL_BLEND);        // turn off "alpha" transparency blending
      GL11.glDepthMask(true);               // needle is hidden behind other objects
      GL11.glDisable(GL11.GL_TEXTURE_2D);   // turn off texturing - we're just going to draw a single-colour needle.
                                            //   See MBE21 for rendering with texture.

      Color needleColour = Color.RED;
      GlStateManager.color(needleColour.getRed()/255.0F,
                           needleColour.getGreen()/255.0F,
                           needleColour.getBlue()/255.0F);     // change the rendering colour

        // change the "multitexturing" lighting value (default value is the brightness of the tile entity's block)
        // - this will make the needle "glow" brighter than the surroundings if it is dark.
      final int SKY_LIGHT_VALUE = 15;
      final int BLOCK_LIGHT_VALUE = 0;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, SKY_LIGHT_VALUE * 16.0F, BLOCK_LIGHT_VALUE * 16.0F);

      bufferBuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
      addNeedleVertices(bufferBuilder);
      tessellator.draw();

    } finally {
      GL11.glPopAttrib();
      GL11.glPopMatrix();
    }
  }

  // add the vertices for drawing the needle.  Normally you would use a model loader for a more complicated shape
  private void addNeedleVertices(BufferBuilder vertexBuffer) {
    // needle is a triangle pointing down, the rotation spindle is at [0,0,0] and the spindle points north-south,
    //  i.e. the needle renders in the z=0 plane; rotating the needle around the spindle will make the needle point
    //  down, east, up, west.
    // anticlockwise vertices -> the spindle is facing south, i.e. the needle will display on the south side of a block

    vertexBuffer.pos(0.00, -0.45, 0.00).endVertex();  // tip (pointer) of needle
    vertexBuffer.pos(0.05, 0.05, 0.00).endVertex();  // stub - one side
    vertexBuffer.pos(-0.05, 0.05, 0.00).endVertex();  // stub - other side
  }
}
