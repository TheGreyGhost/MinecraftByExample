package minecraftbyexample.mbe15_item_smartitemmodel;

import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class is used to customise the rendering of the camouflage block, based on the block it is copying.
 */
public class ChessboardSmartItemModel implements ISmartItemModel {

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

  // This method is used to create a suitable IBakedModel based on the itemstack information.
  //  Typically, this will extract NBT information from the itemstack and customise the model based on that.
  // I think it is ok to just modify this instance instead of creating a new instance, because the IBakedModel
  //   isn't stored or cached and is discarded after rendering.  Haven't run into any problems yet
  @Override
  public IBakedModel handleItemState(ItemStack stack) {
    numberOfChessPieces = 0;
    if (stack != null) {
      numberOfChessPieces = stack.stackSize;
    }

    return this;
  }

  @Override
  public TextureAtlasSprite getTexture() {
    return baseChessboardModel.getTexture();
  }

  @Override
  public List getFaceQuads(EnumFacing enumFacing) {
    return baseChessboardModel.getFaceQuads(enumFacing);
  }

  @Override
  public List getGeneralQuads() {
    Item itemBlockSimple = GameRegistry.findItem("minecraftbyexample", "mbe01_block_simple");
    ItemStack itemStack = new ItemStack(itemBlockSimple);
    IBakedModel baseCube = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);

    List<BakedQuad> combinedQuadsList = new ArrayList();

//    List<BakedQuad> combinedQuadsList = new ArrayList(baseCube.getGeneralQuads());
    for (EnumFacing facing : EnumFacing.values()) {
      combinedQuadsList.addAll(baseCube.getFaceQuads(facing));
    }
//    combinedQuadsList.addAll(getChessPiecesQuads(numberOfChessPieces));
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

  public List<BakedQuad> getChessPiecesQuads(int numberOfPieces)
  {
    final int MIN_NUMBER_OF_PIECES = 1;
    final int PIECES_PER_ROW = 8;
    final int NUMBER_OF_ROWS = 8;
    final int MAX_NUMBER_OF_PIECES = PIECES_PER_ROW * NUMBER_OF_ROWS;

    TextureAtlasSprite chessPieceTexture = Minecraft.getMinecraft().getTextureMapBlocks()
                                                    .getAtlasSprite("minecraft:blocks/diamond_block");

    List<BakedQuad> returnList = new ArrayList<BakedQuad>(64);
    if (numberOfPieces < MIN_NUMBER_OF_PIECES || numberOfPieces > MAX_NUMBER_OF_PIECES) {
      return returnList;
    }

    double xposvble = Minecraft.getMinecraft().getRenderViewEntity().posX / 100.0;
    double zposvble = Minecraft.getMinecraft().getRenderViewEntity().posZ / 100.0;
    final float FRONT_FACE_POSITION = (float)zposvble;
    final float BACK_FACE_POSITION = (float)xposvble;
    final float FRACTION_OF_WIDTH_OCCUPIED_BY_A_PIECE = 0.75F;
    final float FRACTION_OF_HEIGHT_OCCUPIED_BY_A_PIECE = 0.75F;
    final float PIECE_WIDTH = 1.0F / PIECES_PER_ROW * FRACTION_OF_WIDTH_OCCUPIED_BY_A_PIECE;
    final float PIECE_HEIGHT = 1.0F / NUMBER_OF_ROWS * FRACTION_OF_HEIGHT_OCCUPIED_BY_A_PIECE;

    int row = 0;
    int col = 0;
    for (int i = 0; i < numberOfPieces; ++i) {
      float xposmiddle = ((float)col + 0.5F)/PIECES_PER_ROW;
      float zposmiddle = ((float)row + 0.5F)/NUMBER_OF_ROWS;
      float zpos = FRONT_FACE_POSITION;

//      final int ITEM_RENDER_LAYER_NONE = -1;
      final int ITEM_RENDER_LAYER0 = 0;
      final int ITEM_RENDER_LAYER1 = 1;

      // make a baked quad for each side of the chessboard i.e.front and back

      BakedQuad nextPieceFront = createSidedBakedQuad(xposmiddle - PIECE_WIDTH/2.0F, xposmiddle + PIECE_WIDTH/2.0F,
                                                 zposmiddle - PIECE_HEIGHT/2.0F, zposmiddle + PIECE_HEIGHT/2.0F,
                                                 FRONT_FACE_POSITION,
                                                 ITEM_RENDER_LAYER0, chessPieceTexture, EnumFacing.SOUTH
                                                 );
      BakedQuad nextPieceBack = createSidedBakedQuad(xposmiddle - PIECE_WIDTH/2.0F, xposmiddle + PIECE_WIDTH/2.0F,
                                                    zposmiddle - PIECE_HEIGHT/2.0F, zposmiddle + PIECE_HEIGHT/2.0F,
                                                    BACK_FACE_POSITION,
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

  // creates a baked quad for the given side.
  // when you are looking at the side face on, the quad is centred at [centreLR, centreUD]
  // the width of the quad is width, the height is height.
  //  the depth of the quad (perpendicular to the flat face you can see) is depthPos
  private BakedQuad createSidedBakedQuad(float centreLR, float width, float centreUD, float height, float depthPos,
                                         int itemRenderLayer,
                                         TextureAtlasSprite texture, EnumFacing side)
  {
//    float x1, x2, x3;
//    float y1, y2, y3;
//    float z1, z2, z3;
//
//    switch (side) {
//
//
//
//
//    }
//
//    return new BakedQuad(Ints.concat(vertexToInts(width, top, z, Color.WHITE.getRGB(), texture, 16, 0),
//                                     vertexToInts(width, height, z, Color.WHITE.getRGB(), texture, 16, 16),
//                                     vertexToInts(left, height, z, Color.WHITE.getRGB(), texture, 0, 16),
//                                     vertexToInts(left, top, z, Color.WHITE.getRGB(), texture, 0, 0)    ),
//                         itemRenderLayer, side);
    return null;
  }

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
