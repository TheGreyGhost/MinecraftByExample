package minecraftbyexample.mbe15_item_smartitemmodel;

import com.google.common.primitives.Ints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class modifies the displayed item (a chessboard) to show a number of "pieces" (blue squares) on the chessboard,
 *   one square for each item in the itemstack.
 */
public class ChessboardSmartItemModel implements ISmartItemModel {

  /**
   * Create a smart model, using the given baked model as a base to add extra BakedQuads to.
   * @param i_baseChessboardModel the base model
   */
  public ChessboardSmartItemModel(IBakedModel i_baseChessboardModel)
  {
    baseChessboardModel = i_baseChessboardModel;
  }

  // create a tag (ModelResourceLocation) for our model.
  //  "inventory" is used for items. If you don't specify it, you will end up with "normal" by default,
  //  which is used for blocks.
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");

  @SuppressWarnings("deprecation")  // IBakedModel is deprecated to encourage folks to use IFlexibleBakedModel instead
                                    // .. but IFlexibleBakedModel is of no use here...

  // handleItemState() is used to create a suitable IBakedModel based on the itemstack information.
  //  Typically, this will extract NBT information from the itemstack and customise the model based on that.
  // I think it is ok to just modify this instance instead of creating a new instance, because the IBakedModel
  //   isn't stored or cached and is discarded after rendering.  Haven't run into any problems yet.
  @Override
  public IBakedModel handleItemState(ItemStack stack) {
    numberOfChessPieces = 0;
    if (stack != null) {
      numberOfChessPieces = stack.stackSize;
    }
    return this;
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return baseChessboardModel.getParticleTexture();
  }

  // for the difference between face quads and general quads, see here
  //  http://minecraft.gamepedia.com/Block_models#Item_models
  @Override
  public List getFaceQuads(EnumFacing enumFacing) {
    return baseChessboardModel.getFaceQuads(enumFacing);
  }

  // during handleItemState(), we stored the number of chess pieces in a member variable.
  // now, we use that stored information to add extra BakedQuads to the list of Quads to be rendered.
  @Override
  public List getGeneralQuads() {
//    Item itemBlockSimple = GameRegistry.findItem("minecraftbyexample", "mbe01_block_simple");       // code for testing.. renders a cube with numbered sides
//    ItemStack itemStack = new ItemStack(itemBlockSimple);
//    IBakedModel baseCube = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
//    List<BakedQuad> combinedQuadsList = new ArrayList();

    List<BakedQuad> combinedQuadsList = new ArrayList(baseChessboardModel.getGeneralQuads());
    combinedQuadsList.addAll(getChessPiecesQuads(numberOfChessPieces));
    return combinedQuadsList;
//    FaceBakery.makeBakedQuad() can be useful for generating quads
  }

  // not needed for items, but hey
  @Override
  public boolean isAmbientOcclusion() {
    return baseChessboardModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return baseChessboardModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return baseChessboardModel.getItemCameraTransforms();
  }

  // return a list of BakedQuads for drawing the chess pieces
  public List<BakedQuad> getChessPiecesQuads(int numberOfPieces)
  {
    final int MIN_NUMBER_OF_PIECES = 1;
    final int PIECES_PER_ROW = 8;
    final int NUMBER_OF_ROWS = 8;
    final int MAX_NUMBER_OF_PIECES = PIECES_PER_ROW * NUMBER_OF_ROWS;

    TextureAtlasSprite chessPieceTexture = Minecraft.getMinecraft().getTextureMapBlocks()
                                                    .getAtlasSprite("minecraft:blocks/diamond_block");
    // if you want to use your own texture, you can add it to the texture map using code similar to this:
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
    //    (i.e. north<-->south thickness) are centred around the z=0.5 plane.
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

    return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16),
                                     vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0),
                                     vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0),
                                     vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16)),
                         itemRenderLayer, face);
  }

  /**
   * Converts the vertex information to the int array format expected by BakedQuads.
   * @param x x coordinate
   * @param y y coordinate
   * @param z z coordinate
   * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
   * @param texture the texture to use for the face
   * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
   * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
   * @return
   */
  private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
  {
    return new int[] {
            Float.floatToRawIntBits(x),
            Float.floatToRawIntBits(y),
            Float.floatToRawIntBits(z),
            color,
            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
            0
    };
  }

  private IBakedModel baseChessboardModel;
  private int numberOfChessPieces;
}
