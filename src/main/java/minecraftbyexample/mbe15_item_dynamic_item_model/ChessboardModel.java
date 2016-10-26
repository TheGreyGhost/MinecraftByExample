package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import java.util.Collections;
import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class modifies the displayed item (a chessboard) to show a number of "pieces" (blue squares) on the chessboard,
 *   one square for each item in the itemstack.
 */
public class ChessboardModel implements IBakedModel {

  /**
   * Create our model, using the given baked model as a base to add extra BakedQuads to.
   * @param i_baseChessboardModel the base model
   */
  public ChessboardModel(IBakedModel i_baseChessboardModel)
  {
    baseChessboardModel = i_baseChessboardModel;
    chessboardItemOverrideList = new ChessboardItemOverrideList(Collections.EMPTY_LIST);
  }

  // create a tag (ModelResourceLocation) for our model.
  //  "inventory" is used for items. If you don't specify it, you will end up with "normal" by default,
  //  which is used for blocks.
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return baseChessboardModel.getParticleTexture();
  }

  /**  Returns the quads for the base chessboard model only
   * @param state
   * @param side
   * @param rand
   * @return
   */
  @Override
  public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
    return baseChessboardModel.getQuads(state, side, rand);
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
    return chessboardItemOverrideList;
  }

  private IBakedModel baseChessboardModel;
  private ChessboardItemOverrideList chessboardItemOverrideList;
}
