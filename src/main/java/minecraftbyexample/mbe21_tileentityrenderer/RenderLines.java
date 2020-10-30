package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import minecraftbyexample.usefultools.RenderTypeHelper;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 * This class shows examples of rendering using lines.
 * The lines have position and colour information only  (RenderType.getLines()).  No lightmap information, which means that
 *   they will always be the same brightness regardless of day/night or nearby torches.
 *
 * The lines are rendered to the ITEM_ENTITY framebuffer ("target")
 *
 */
public class RenderLines  {

  public static void renderWireframe(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                               int combinedLight, int combinedOverlay) {
    // draw the artifact using lines
    // (Draws an inverted tetrahedron wireframe above the rendered base block (hopper block model))
    // When the TER::render method is called, the origin [0,0,0] is at the current [x,y,z] of the block being rendered.
    // The tetrahedron-drawing method draws the tetrahedron in a cube region from [0,0,0] to [1,1,1] but we want it
    //   to be in the block one above this, i.e. from [0,1,0] to [1,2,1],
    //   so we need to translate up by one block, i.e. by [0,1,0]
    final Vector3d TRANSLATION_OFFSET = new Vector3d(0, 1, 0);

    matrixStack.push(); // push the current transformation matrix + normals matrix
    matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
    Color artifactColour = tileEntityMBE21.getArtifactColour();

    drawTetrahedronWireframe(matrixStack, renderBuffers, artifactColour);
    matrixStack.pop(); // restore the original transformation matrix + normals matrix
  }

  /**
   * Draw an upside-down wireframe tetrahedron with its tip at [0.5,0,0.5]
   *    and 1x1 square "base" at y = 1 (x= 0 to 1, z = 0 to 1)
   * @param matrixStack transformation matrix and normal matrix
   * @param renderBuffers the renderbuffers we'll be drawing to
   */
  private static void drawTetrahedronWireframe(MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                               Color color) {

      final Vector3d[] BASE_VERTICES = {
              new Vector3d(0, 1, 0),
              new Vector3d(1, 1, 0),
              new Vector3d(1, 1, 1),
              new Vector3d(0, 1, 1),
      };
      final Vector3d APEX_VERTEX = new Vector3d(0.5, 0, 0.5);

    IVertexBuilder vertexBuilderLines = renderBuffers.getBuffer(RenderTypeHelper.MBE_LINE_DEPTH_WRITING_ON);
    // Note that, although RenderType.getLines() might appear to be suitable, it leads to weird rendering if used in
    // tile entity rendering, because it doesn't write to the depth buffer.  In other words, any object in the scene
    // which is drawn after the lines, will render over the top of the line (erase it) even if the object is behind
    //  the lines.  This means that RenderType.getLines() is only suitable for lines which are the last thing drawn in
    //  the scene (such as DrawBlockHighlightEvent)
    // The solution I used here is a custom RenderType for lines which does write to the depth buffer.

    Matrix4f matrixPos = matrixStack.getLast().getMatrix();  //retrieves the current transformation matrix
    // draw the base
    for (int i = 1; i < BASE_VERTICES.length; ++i) {
      drawLine(matrixPos, vertexBuilderLines, color, BASE_VERTICES[i-1], BASE_VERTICES[i]);
    }
    drawLine(matrixPos, vertexBuilderLines, color, BASE_VERTICES[BASE_VERTICES.length - 1], BASE_VERTICES[0]);

    // draw the sides (from the corners of the base to the apex)
    for (Vector3d baseVertex : BASE_VERTICES) {
      drawLine(matrixPos, vertexBuilderLines, color, APEX_VERTEX, baseVertex);
    }
  }

  /**
   * Draw a coloured line from a starting vertex to an end vertex
   * @param matrixPos the current transformation matrix
   * @param renderBuffer the vertex builder used to draw the line
   * @param startVertex
   * @param endVertex
   */
  private static void drawLine(Matrix4f matrixPos, IVertexBuilder renderBuffer,
                               Color color,
                               Vector3d startVertex, Vector3d endVertex) {
    renderBuffer.pos(matrixPos, (float) startVertex.getX(), (float) startVertex.getY(), (float) startVertex.getZ())
            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
            .endVertex();
    renderBuffer.pos(matrixPos, (float) endVertex.getX(), (float) endVertex.getY(), (float) endVertex.getZ())
            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
            .endVertex();
  }
}
