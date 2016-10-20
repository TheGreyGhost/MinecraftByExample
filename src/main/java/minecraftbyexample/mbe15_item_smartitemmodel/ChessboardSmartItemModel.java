package minecraftbyexample.mbe15_item_smartitemmodel;

import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class modifies the displayed item (a chessboard) to show a number of "pieces" (blue squares) on the chessboard,
 *   one square for each item in the itemstack.
 */
public class ChessboardSmartItemModel implements IBakedModel {

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
//
//  // for the difference between face quads and general quads, see here
//  //  http://minecraft.gamepedia.com/Block_models#Item_models
//  @Override
//  public List getFaceQuads(EnumFacing enumFacing) {
//    return baseChessboardModel.getFaceQuads(enumFacing);
//  }

  // during handleItemState(), we stored the number of chess pieces in a member variable.
  // now, we use that stored information to add extra BakedQuads to the list of Quads to be rendered.
  @Override
  public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
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

  @Override
  public ItemOverrideList getOverrides() {
    return null;
  }



  private IBakedModel baseChessboardModel;
  private int numberOfChessPieces;
}
