package minecraftbyexample.mbe15_item_dynamic_item_model;

import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGG on 20/10/2016.
 */
public class ChessboardFinalisedModel implements IBakedModel {

  public ChessboardFinalisedModel(IBakedModel i_parentModel, int i_numberOfChessPieces)
  {
    parentModel = i_parentModel;
    numberOfChessPieces = i_numberOfChessPieces;
  }

  /**
   * We return a list of quads here which is used to draw the chessboard.
   * We do this by getting the list of quads for the base model (the chessboard itself), then adding an extra quad for
   *   every piece on the chessboard.  The number of pieces was provided to the constructor of the finalise model.
   *
   * @param state
   * @param side  which side: north, east, south, west, up, down, or null.  NULL is a different kind to the others
   *   see here for more information: http://minecraft.gamepedia.com/Block_models#Item_models
   * @param rand
   * @return the list of quads to be rendered
   */

  @Override
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    // our chess pieces are only drawn when side is NULL.
    if (side != null) {
      return parentModel.getQuads(state, side, rand);
    }

    List<BakedQuad> combinedQuadsList = new ArrayList(parentModel.getQuads(state, side, rand));
    combinedQuadsList.addAll(getChessPiecesQuads(numberOfChessPieces));
    return combinedQuadsList;
//    FaceBakery.makeBakedQuad() can also be useful for generating quads
  }

  @Override
  public boolean isAmbientOcclusion() {
    return parentModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return parentModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return parentModel.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return parentModel.getParticleTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return parentModel.getItemCameraTransforms();
  }

  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//    if (parentModel instanceof IPerspectiveAwareModel) {
      Matrix4f matrix4f = parentModel.handlePerspective(cameraTransformType).getRight();
      return Pair.of(this, matrix4f);
//    } else {
//      // If the parent model isn't an IPerspectiveAware, we'll need to generate the correct matrix ourselves using the
//      //  ItemCameraTransforms.
//
//      ItemCameraTransforms itemCameraTransforms = parentModel.getItemCameraTransforms();
//      ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
//      TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
//      Matrix4f mat = null;
//      if (tr != null) { // && tr != TRSRTransformation.identity()) {
//        mat = tr.getMatrix();
//      }
//      // The TRSRTransformation for vanilla items have blockCenterToCorner() applied, however handlePerspective
//      //  reverses it back again with blockCornerToCenter().  So we don't need to apply it here.
//
//      return Pair.of(this, mat);
//    }
  }

  @Override
  public ItemOverrideList getOverrides() {
    throw new UnsupportedOperationException("The finalised model does not have an override list.");
  }

  // return a list of BakedQuads for drawing the chess pieces
  private List<BakedQuad> getChessPiecesQuads(int numberOfPieces)
  {
    final int MIN_NUMBER_OF_PIECES = 1;
    final int PIECES_PER_ROW = 8;
    final int NUMBER_OF_ROWS = 8;
    final int MAX_NUMBER_OF_PIECES = PIECES_PER_ROW * NUMBER_OF_ROWS;

    TextureAtlasSprite chessPieceTexture = Minecraft.getMinecraft().getTextureMapBlocks()
            .getAtlasSprite("minecraft:blocks/diamond_block");
    // if you want to use your own texture, you can add it to the texture map using code similar to this in your ClientProxy:
    //   MinecraftForge.EVENT_BUS.register(new StitcherAddDigitsTexture());
    //    public class StitcherAddDigitsTexture {
    //      @SubscribeEvent
    //      public void stitcherEventPre(TextureStitchEvent.Pre event) {
    //        ResourceLocation digits = new ResourceLocation("stickmod:items/digits");
    //        event.map.registerSprite(digits);
    //      }
    //    }

    List<BakedQuad> returnList = new ArrayList<BakedQuad>(64);
    if (numberOfPieces < MIN_NUMBER_OF_PIECES || numberOfPieces > MAX_NUMBER_OF_PIECES) {
      return returnList;
    }

    // "builtin/generated" items, which are generated from the 2D texture by adding a thickness in the z direction
    //    (i.e. north<-->south thickness), are centred around the z=0.5 plane.
    final float BUILTIN_GEN_ITEM_THICKNESS = 1/16.0F;
    final float BUILTIN_GEN_ITEM_Z_CENTRE = 0.5F;
    final float BUILTIN_GEN_ITEM_Z_MAX = BUILTIN_GEN_ITEM_Z_CENTRE + BUILTIN_GEN_ITEM_THICKNESS / 2.0F;
    final float BUILTIN_GEN_ITEM_Z_MIN = BUILTIN_GEN_ITEM_Z_CENTRE - BUILTIN_GEN_ITEM_THICKNESS / 2.0F;
    final float SOUTH_FACE_POSITION = 1.0F;  // the south face of the cube is at z = 1.0F
    final float NORTH_FACE_POSITION = 0.0F;  // the north face of the cube is at z = 0.0F
    // http://greyminecraftcoder.blogspot.co.at/2014/12/blocks-18.html

    final float DISTANCE_BEHIND_SOUTH_FACE = SOUTH_FACE_POSITION - BUILTIN_GEN_ITEM_Z_MAX;
    final float DISTANCE_BEHIND_NORTH_FACE = BUILTIN_GEN_ITEM_Z_MIN - NORTH_FACE_POSITION;
    final float FRACTION_OF_WIDTH_OCCUPIED_BY_A_PIECE = 0.75F;
    final float FRACTION_OF_HEIGHT_OCCUPIED_BY_A_PIECE = 0.75F;
    final float PIECE_WIDTH = 1.0F / PIECES_PER_ROW * FRACTION_OF_WIDTH_OCCUPIED_BY_A_PIECE;
    final float PIECE_HEIGHT = 1.0F / NUMBER_OF_ROWS * FRACTION_OF_HEIGHT_OCCUPIED_BY_A_PIECE;

    int row = 0;
    int col = 0;
    for (int i = 0; i < numberOfPieces; ++i) {
      float columnCentrePosition = ((float)col + 0.5F)/PIECES_PER_ROW;
      float rowCentrePosition = ((float)row + 0.5F)/NUMBER_OF_ROWS;

      final int ITEM_RENDER_LAYER_NONE_SPECIFIED = -1;
      final int ITEM_RENDER_LAYER0 = 0;
      final int ITEM_RENDER_LAYER1 = 1;

      // make a baked quad for each side of the chessboard i.e. front and back (south and north)
      final float DELTA_FOR_OVERLAP = 0.001F;  // add a small overlap to stop the quad from lying exactly on top of the
      //   existing face, which leads to "z-fighting" where the two quads
      //   fight each other to be on top.  looks awful.

      BakedQuad nextPieceFront = createBakedQuadForFace(columnCentrePosition, PIECE_WIDTH,
              rowCentrePosition, PIECE_HEIGHT,
              -DISTANCE_BEHIND_SOUTH_FACE + DELTA_FOR_OVERLAP,
              ITEM_RENDER_LAYER0, chessPieceTexture, EnumFacing.SOUTH
      );
      BakedQuad nextPieceBack = createBakedQuadForFace(columnCentrePosition, PIECE_WIDTH,
              rowCentrePosition, PIECE_HEIGHT,
              -DISTANCE_BEHIND_NORTH_FACE + DELTA_FOR_OVERLAP,
              ITEM_RENDER_LAYER0, chessPieceTexture, EnumFacing.NORTH
      );
      returnList.add(nextPieceFront);
      returnList.add(nextPieceBack);

      ++col;
      if (col >= PIECES_PER_ROW) {
        col = 0;
        ++row;
      }
    }
    return returnList;
  }

  /**
   // Creates a baked quad for the given face.
   // When you are directly looking at the face, the quad is centred at [centreLR, centreUD]
   // The left<->right "width" of the face is width, the bottom<-->top "height" is height.
   // The amount that the quad is displaced towards the viewer i.e. (perpendicular to the flat face you can see) is forwardDisplacement
   //   - for example, for an EAST face, a value of 0.00 lies directly on the EAST face of the cube.  a value of 0.01 lies
   //     slightly to the east of the EAST face (at x=1.01).  a value of -0.01 lies slightly to the west of the EAST face (at x=0.99).
   // The orientation of the faces is as per the diagram on this page
   //   http://greyminecraftcoder.blogspot.com.au/2014/12/block-models-texturing-quads-faces.html
   // Read this page to learn more about how to draw a textured quad
   //   http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
   * @param centreLR the centre point of the face left-right
   * @param width    width of the face
   * @param centreUD centre point of the face top-bottom
   * @param height height of the face from top to bottom
   * @param forwardDisplacement the displacement of the face (towards the front)
   * @param itemRenderLayer which item layer the quad is on
   * @param texture the texture to use for the quad
   * @param face the face to draw this quad on
   * @return
   */
  private BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height, float forwardDisplacement,
                                           int itemRenderLayer,
                                           TextureAtlasSprite texture, EnumFacing face)
  {
    float x1, x2, x3, x4;
    float y1, y2, y3, y4;
    float z1, z2, z3, z4;
    int packednormal;
    final float CUBE_MIN = 0.0F;
    final float CUBE_MAX = 1.0F;

    switch (face) {
      case UP: {
        x1 = x2 = centreLR + width/2.0F;
        x3 = x4 = centreLR - width/2.0F;
        z1 = z4 = centreUD + height/2.0F;
        z2 = z3 = centreUD - height/2.0F;
        y1 = y2 = y3 = y4 = CUBE_MAX + forwardDisplacement;
        break;
      }
      case DOWN: {
        x1 = x2 = centreLR + width/2.0F;
        x3 = x4 = centreLR - width/2.0F;
        z1 = z4 = centreUD - height/2.0F;
        z2 = z3 = centreUD + height/2.0F;
        y1 = y2 = y3 = y4 = CUBE_MIN - forwardDisplacement;
        break;
      }
      case WEST: {
        z1 = z2 = centreLR + width/2.0F;
        z3 = z4 = centreLR - width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        x1 = x2 = x3 = x4 = CUBE_MIN - forwardDisplacement;
        break;
      }
      case EAST: {
        z1 = z2 = centreLR - width/2.0F;
        z3 = z4 = centreLR + width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        x1 = x2 = x3 = x4 = CUBE_MAX + forwardDisplacement;
        break;
      }
      case NORTH: {
        x1 = x2 = centreLR - width/2.0F;
        x3 = x4 = centreLR + width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        z1 = z2 = z3 = z4 = CUBE_MIN - forwardDisplacement;
        break;
      }
      case SOUTH: {
        x1 = x2 = centreLR + width/2.0F;
        x3 = x4 = centreLR - width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        z1 = z2 = z3 = z4 = CUBE_MAX + forwardDisplacement;
        break;
      }
      default: {
        assert false : "Unexpected facing in createBakedQuadForFace:" + face;
        return null;
      }
    }

    packednormal = calculatePackedNormal(x1, y1, z1,  x2, y2, z2,  x3, y3, z3,  x4, y4, z4);
    return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16, packednormal),
            vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0, packednormal),
            vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0, packednormal),
            vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16, packednormal)),
            itemRenderLayer, face, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);
  }

  /**
   * Converts the vertex information to the int array format expected by BakedQuads.  Useful if you don't know
   *   in advance what it should be.
   * @param x x coordinate
   * @param y y coordinate
   * @param z z coordinate
   * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
   * @param texture the texture to use for the face
   * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
   * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
   * @param normal the packed representation of the normal vector, see calculatePackedNormal().  Used for lighting items.
   * @return
   */
  private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v, int normal)
  {
    return new int[] {
            Float.floatToRawIntBits(x),
            Float.floatToRawIntBits(y),
            Float.floatToRawIntBits(z),
            color,
            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
            normal
    };
  }

  /**
   * Calculate the normal vector based on four input coordinates
   * assumes that the quad is coplanar but should produce a 'reasonable' answer even if not.
   * @return the packed normal, ZZYYXX
   */
  private int calculatePackedNormal(
          float x1, float y1, float z1,
          float x2, float y2, float z2,
          float x3, float y3, float z3,
          float x4, float y4, float z4) {

    float xp = x4-x2;
    float yp = y4-y2;
    float zp = z4-z2;

    float xq = x3-x1;
    float yq = y3-y1;
    float zq = z3-z1;

    //Cross Product
    float xn = yq*zp - zq*yp;
    float yn = zq*xp - xq*zp;
    float zn = xq*yp - yq*xp;

    //Normalize
    float norm = (float)Math.sqrt(xn*xn + yn*yn + zn*zn);
    final float SMALL_LENGTH =  1.0E-4F;  //Vec3d.normalise() uses this
    if (norm < SMALL_LENGTH) norm = 1.0F;  // protect against degenerate quad

    norm = 1.0F / norm;
    xn *= norm;
    yn *= norm;
    zn *= norm;

    int x = ((byte)(xn * 127)) & 0xFF;
    int y = ((byte)(yn * 127)) & 0xFF;
    int z = ((byte)(zn * 127)) & 0xFF;
    return x | (y << 0x08) | (z << 0x10);
  }

  private int numberOfChessPieces;
  private IBakedModel parentModel;
}
