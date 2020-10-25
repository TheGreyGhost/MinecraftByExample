package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.*;
import net.minecraft.util.math.vector.Vector2f;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 * This class renders the artifact floating above the block.
 * The base model (the hopper shape) is drawn by the block model, not this class.
 * See assets/minecraftbyexample/blockstates/mbe21_tesr_block_registry_name.json
 *
 * This class demonstrate manually drawing quads.
 * The quads have position, colour, texture, normal, overlay, and lightmap information: see
 *   DefaultVertexFormats
 *       ENTITY = new VertexFormat(ImmutableList.builder().add(POSITION_3F).add(COLOR_4UB).add(TEX_2F).add(TEX_2S).add(TEX_2SB).add(NORMAL_3B).add(PADDING_1B).build());
 *     ENTITY = new VertexFormat(ImmutableList.builder()
 *       .add(POSITION_3F)
 *       .add(COLOR_4UB) // 4 unsigned bytes of colour (R, G, B, alpha)
 *       .add(TEX_2F)   // 2 floats for texture uv
 *       .add(TEX_2S)   // 2 shorts for overlay coordinates
 *       .add(TEX_2SB)  // 2 shorts for lightmap coordinates
 *       .add(NORMAL_3B) // 3 bytes for normal vector
 *       .add(PADDING_1B).build());
 *
 *   RenderType.getSolid() is also suitable if you're using a texture which has been stitched into the block texture sheet.
 *    Textures can be stitched into the texture sheet either
 *    1) automatically, if you have specified them in the block model for a registered block; or
 *    2) manually, by adding it yourself during TextureStitchEvent.Pre
 *
 *   Other types of rendering are possible by producing custom RenderTypes (see RenderTypeHelper)
 */
public class RenderQuads {

  // note - must include textures/ at the start and .png at the end, otherwise it will fail silently and just give you the purple & black
  //        missing texture
  public static final ResourceLocation MBE21_CUBE_FACE_TEXTURE = new ResourceLocation("minecraftbyexample:textures/entity/mbe21_ter_cube.png");

  public static void renderCubeUsingQuads(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                    int combinedLight, int combinedOverlay) {
      // draw the object as a cube, using quads
      // When render method is called, the origin [0,0,0] is at the current [x,y,z] of the block.

      // The cube-drawing method draws the cube in the region from [0,0,0] to [1,1,1] but we want it
      //   to be in the block one above this, i.e. from [0,1,0] to [1,2,1],
      //   so we need to translate up by one block, i.e. by [0,1,0]
      final Vector3d TRANSLATION_OFFSET = new Vector3d(0, 1, 0);

      matrixStack.push(); // push the current transformation matrix + normals matrix
      matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
      Color gemColour = tileEntityMBE21.getArtifactColour();

      drawCubeQuads(matrixStack, renderBuffers, gemColour, combinedLight);
      matrixStack.pop(); // restore the original transformation matrix + normals matrix
    }

  /**
   * Draw a cube from [0,0,0] to [1,1,1], same texture on all sides, using a supplied texture
   */
  private static void drawCubeQuads(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer,
                                    Color color, int combinedLight) {

    IVertexBuilder vertexBuilderBlockQuads = renderBuffer.getBuffer(RenderType.getEntitySolid(MBE21_CUBE_FACE_TEXTURE));
    // other typical RenderTypes used by TER are:
    // getEntityCutout, getBeaconBeam (which has translucency),

    Matrix4f matrixPos = matrixStack.getLast().getMatrix();     // retrieves the current transformation matrix
    Matrix3f matrixNormal = matrixStack.getLast().getNormal();  // retrieves the current transformation matrix for the normal vector

    // we use the whole texture
    Vector2f bottomLeftUV = new Vector2f(0.0F, 1.0F);
    float UVwidth = 1.0F;
    float UVheight = 1.0F;

    // all faces have the same height and width
    final float WIDTH = 1.0F;
    final float HEIGHT = 1.0F;

    final Vector3d EAST_FACE_MIDPOINT = new Vector3d(1.0, 0.5, 0.5);
    final Vector3d WEST_FACE_MIDPOINT = new Vector3d(0.0, 0.5, 0.5);
    final Vector3d NORTH_FACE_MIDPOINT = new Vector3d(0.5, 0.5, 0.0);
    final Vector3d SOUTH_FACE_MIDPOINT = new Vector3d(0.5, 0.5, 1.0);
    final Vector3d UP_FACE_MIDPOINT = new Vector3d(0.5, 1.0, 0.5);
    final Vector3d DOWN_FACE_MIDPOINT = new Vector3d(0.5, 0.0, 0.5);

    addFace(Direction.EAST, matrixPos, matrixNormal, vertexBuilderBlockQuads,
            color, EAST_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
    addFace(Direction.WEST, matrixPos, matrixNormal, vertexBuilderBlockQuads,
            color, WEST_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
    addFace(Direction.NORTH, matrixPos, matrixNormal, vertexBuilderBlockQuads,
            color, NORTH_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
    addFace(Direction.SOUTH, matrixPos, matrixNormal, vertexBuilderBlockQuads,
            color, SOUTH_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
    addFace(Direction.UP, matrixPos, matrixNormal, vertexBuilderBlockQuads,
            color, UP_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
    addFace(Direction.DOWN, matrixPos, matrixNormal, vertexBuilderBlockQuads,
            color, DOWN_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
  }

    private static void addFace(Direction whichFace,
                                Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                                Color color, Vector3d centrePos, float width, float height,
                                Vector2f bottomLeftUV, float texUwidth, float texVheight,
                                int lightmapValue) {
    // the Direction class has a bunch of methods which can help you rotate quads
    //  I've written the calculations out long hand, and based them on a centre position, to make it clearer what
    //   is going on.
    // Beware that the Direction class is based on which direction the face is pointing, which is opposite to
    //   the direction that the viewer is facing when looking at the face.
    // Eg when drawing the NORTH face, the face points north, but when we're looking at the face, we are facing south,
    //   so that the bottom left corner is the eastern-most, not the western-most!


    // calculate the bottom left, bottom right, top right, top left vertices from the VIEWER's point of view (not the
    //  face's point of view)

    Vector3f leftToRightDirection, bottomToTopDirection;

    switch (whichFace) {
      case NORTH: { // bottom left is east
        leftToRightDirection = new Vector3f(-1, 0, 0);  // or alternatively Vector3f.XN
        bottomToTopDirection = new Vector3f(0, 1, 0);  // or alternatively Vector3f.YP
        break;
      }
      case SOUTH: {  // bottom left is west
        leftToRightDirection = new Vector3f(1, 0, 0);
        bottomToTopDirection = new Vector3f(0, 1, 0);
        break;
      }
      case EAST: {  // bottom left is south
        leftToRightDirection = new Vector3f(0, 0, -1);
        bottomToTopDirection = new Vector3f(0, 1, 0);
        break;
      }
      case WEST: { // bottom left is north
        leftToRightDirection = new Vector3f(0, 0, 1);
        bottomToTopDirection = new Vector3f(0, 1, 0);
        break;
      }
      case UP: { // bottom left is southwest by minecraft block convention
        leftToRightDirection = new Vector3f(-1, 0, 0);
        bottomToTopDirection = new Vector3f(0, 0, 1);
        break;
      }
      case DOWN: { // bottom left is northwest by minecraft block convention
        leftToRightDirection = new Vector3f(1, 0, 0);
        bottomToTopDirection = new Vector3f(0, 0, 1);
        break;
      }
      default: {  // should never get here, but just in case;
        leftToRightDirection = new Vector3f(0, 0, 1);
        bottomToTopDirection = new Vector3f(0, 1, 0);
        break;
      }
    }
    leftToRightDirection.mul(0.5F * width);  // convert to half width
    bottomToTopDirection.mul(0.5F * height);  // convert to half height

    // calculate the four vertices based on the centre of the face

    Vector3f bottomLeftPos = new Vector3f(centrePos);
      bottomLeftPos.sub(leftToRightDirection);
      bottomLeftPos.sub(bottomToTopDirection);

    Vector3f bottomRightPos = new Vector3f(centrePos);
    bottomRightPos.add(leftToRightDirection);
    bottomRightPos.sub(bottomToTopDirection);

    Vector3f topRightPos = new Vector3f(centrePos);
    topRightPos.add(leftToRightDirection);
    topRightPos.add(bottomToTopDirection);

    Vector3f topLeftPos = new Vector3f(centrePos);
    topLeftPos.sub(leftToRightDirection);
    topLeftPos.add(bottomToTopDirection);

    // texture coordinates are "upside down" relative to the face
    // eg bottom left = [U min, V max]
    Vector2f bottomLeftUVpos = new Vector2f(bottomLeftUV.x, bottomLeftUV.y);
    Vector2f bottomRightUVpos = new Vector2f(bottomLeftUV.x + texUwidth, bottomLeftUV.y);
    Vector2f topLeftUVpos = new Vector2f(bottomLeftUV.x + texUwidth, bottomLeftUV.y + texVheight);
    Vector2f topRightUVpos = new Vector2f(bottomLeftUV.x, bottomLeftUV.y + texVheight);

    Vector3f normalVector = whichFace.toVector3f();  // gives us the normal to the face

    addQuad(matrixPos, matrixNormal, renderBuffer,
            bottomLeftPos, bottomRightPos, topRightPos, topLeftPos,
            bottomLeftUVpos, bottomRightUVpos, topLeftUVpos, topRightUVpos,
            normalVector, color, lightmapValue);
  }

  /**
   * Add a quad.
   * The vertices are added in anti-clockwise order from the VIEWER's  point of view, i.e.
   * bottom left; bottom right, top right, top left
   * If you add the vertices in clockwise order, the quad will face in the opposite direction; i.e. the viewer will be
   *   looking at the back face, which is usually culled (not visible)
   * See
   * http://greyminecraftcoder.blogspot.com/2014/12/the-tessellator-and-worldrenderer-18.html
   * http://greyminecraftcoder.blogspot.com/2014/12/block-models-texturing-quads-faces.html
   */
  private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                              Vector3f blpos, Vector3f brpos, Vector3f trpos, Vector3f tlpos,
                              Vector2f blUVpos, Vector2f brUVpos, Vector2f trUVpos, Vector2f tlUVpos,
                              Vector3f normalVector, Color color, int lightmapValue) {
    addQuadVertex(matrixPos, matrixNormal, renderBuffer, blpos, blUVpos, normalVector, color, lightmapValue);
    addQuadVertex(matrixPos, matrixNormal, renderBuffer, brpos, brUVpos, normalVector, color, lightmapValue);
    addQuadVertex(matrixPos, matrixNormal, renderBuffer, trpos, trUVpos, normalVector, color, lightmapValue);
    addQuadVertex(matrixPos, matrixNormal, renderBuffer, tlpos, tlUVpos, normalVector, color, lightmapValue);
  }

  // suitable for vertexbuilders using the DefaultVertexFormats.ENTITY format
  private static void addQuadVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                                    Vector3f pos, Vector2f texUV,
                                    Vector3f normalVector, Color color, int lightmapValue) {
    renderBuffer.pos(matrixPos, pos.getX(), pos.getY(), pos.getZ()) // position coordinate
            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())        // color
            .tex(texUV.x, texUV.y)                     // texel coordinate
            .overlay(OverlayTexture.NO_OVERLAY)  // only relevant for rendering Entities (Living)
            .lightmap(lightmapValue)             // lightmap with full brightness
            .normal(matrixNormal, normalVector.getX(), normalVector.getY(), normalVector.getZ())
            .endVertex();
  }
}
