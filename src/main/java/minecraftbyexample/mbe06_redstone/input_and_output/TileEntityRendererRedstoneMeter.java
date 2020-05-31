package minecraftbyexample.mbe06_redstone.input_and_output;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import minecraftbyexample.usefultools.RenderTypeHelper;
import minecraftbyexample.usefultools.UsefulFunctions;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 27/11/2015
 * This class renders the meter needle on each face of the block.
 * The base model is drawn by the block model, not this class.
 */
public class TileEntityRendererRedstoneMeter extends TileEntityRenderer<TileEntityRedstoneMeter>
{
  public TileEntityRendererRedstoneMeter(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
    super(tileEntityRendererDispatcher);
  }

  /**
   * render the tile entity - called every frame while the tileentity is in view of the player
   * @param tileEntityRedstoneMeter the associated tile entity

   * @param partialTicks the fraction of a tick that this frame is being rendered at - used to interpolate frames between
   *                     ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
   *                     this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75

   */
  @Override
  public void render(TileEntityRedstoneMeter tileEntityRedstoneMeter, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                     int combinedLight, int combinedOverlay)
  {
    double powerLevel = tileEntityRedstoneMeter.getSmoothedNeedlePosition();

    IVertexBuilder vertexBuilderTriangles = renderBuffers.getBuffer(RenderTypeHelper.MBE_TRIANGLES_NO_TEXTURE);
    // Created a custom RenderType for triangles with no texture
    // Unlike normal blocks, this RenderType has no lightmap coordinates

    renderNeedleOnFace(powerLevel, matrixStack, vertexBuilderTriangles, Direction.SOUTH);
    renderNeedleOnFace(powerLevel, matrixStack, vertexBuilderTriangles, Direction.NORTH);
    renderNeedleOnFace(powerLevel, matrixStack, vertexBuilderTriangles, Direction.EAST);
    renderNeedleOnFace(powerLevel, matrixStack, vertexBuilderTriangles, Direction.WEST);
  }

  /**
   * Render the needle at the current power level
   * @param powerLevel 0 - 1
   * @param whichFace which face of the block should the needle render on?
   */
  private void renderNeedleOnFace(double powerLevel, MatrixStack matrixStack, IVertexBuilder renderBuffer, Direction whichFace)
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
        LOGGER.error("Illegal face in renderNeedleOnFace:" + whichFace);
      }
    }

    try {
      // save the transformation matrix and the rendering attributes, so that we can restore them after rendering.
      //  using try..finally is not essential but helps make it more robust in case of exceptions
      // For further information on rendering using the Tessellator, see http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
      matrixStack.push();

      // First we need to set up the translation so that we render our needle with the centre point at 0,0,0,
      // i.e. the point that the needle rotates around.
      // Vanilla has called the renderer with the origin at the [x,y,z] of the block, so we need to
      // add an extra offset to move our needle to the correct location on the correct face, i.e. [needleSpindleX, needleSpindleY, needleSpindleZ]
      matrixStack.translate(needleSpindleX, needleSpindleY, needleSpindleZ);

      // we now need to rotate the needle:
      // 1) around the z axis, to spin the needle to show the correct power level
      // 2) around the vertical axis, to move it to the correct face (EAST, WEST, SOUTH, NORTH)
      // Rendering transformations take effect in "reverse order" so we have to do needle second

      // zero degrees is down, increases clockwise with increasing power level
      final double ZERO_ANGLE = 45;
      final double MAX_ANGLE = 315;
      float needleAngle = (float)UsefulFunctions.interpolate_with_clipping(powerLevel, 0, 1, ZERO_ANGLE, MAX_ANGLE);

      // rotate around the vertical axis to make face correct direction (degrees anticlockwise from south)
      Quaternion faceRotation = Vector3f.YP.rotationDegrees(faceRotationAngle);
      matrixStack.rotate(faceRotation);

      // rotate around the Z axis to make our needle point in the right direction (our model is designed that way).
      Quaternion needleRotation = Vector3f.ZN.rotationDegrees(needleAngle);
      matrixStack.rotate(needleRotation);


      Color needleColour = Color.RED;
      Matrix4f matrixPos = matrixStack.getLast().getMatrix();  //retrieves the current transformation matrix
      addNeedleVertices(matrixPos, renderBuffer, needleColour);
    } finally {
      matrixStack.pop();
    }
  }

  // add the vertices for drawing the needle.  Normally you would use a wavefront model loader (or similar) for a more complicated shape
  private void addNeedleVertices(Matrix4f matrixPos, IVertexBuilder renderBuffer,
                                 Color color) {
    // needle is a triangle pointing down, the rotation spindle is at [0,0,0] and the spindle points north-south,
    //  i.e. the needle renders in the z=0 plane; rotating the needle around the spindle will make the needle point
    //  down, east, up, west.
    // anticlockwise vertices -> the spindle is facing south, i.e. the needle will display on the south side of a block

    renderBuffer.pos(matrixPos, 0.00F, -0.45F, 0.00F)  // tip (pointer) of needle
            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
            .endVertex();
    renderBuffer.pos(matrixPos, 0.05F, 0.05F, 0.00F)   // stub - one side
            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
            .endVertex();
    renderBuffer.pos(matrixPos, -0.05F, 0.05F, 0.00F)  // stub - other side
            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
            .endVertex();
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
