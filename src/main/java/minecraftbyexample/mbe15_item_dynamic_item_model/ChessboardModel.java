package minecraftbyexample.mbe15_item_dynamic_item_model;

import com.google.common.collect.ImmutableList;
import minecraftbyexample.mbe04_block_dynamic_block_models.BlockAltimeter;
import minecraftbyexample.mbe04_block_dynamic_block_models.BlockCamouflage;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class modifies the displayed item (a chessboard) to show a number of "pieces" (blue squares) on the chessboard,
 *   one square for each item in the itemstack.
 * For models generated from a texture turned into a "texture with thickness" (i.e. like most item), you must implement
 *   IPerspectiveAwareModel instead of IBakedModel otherwise the item transforms won't work.  This is because Forge
 *   doesn't implement BakedItemModel.getItemCameraTransforms() correctly.
 */
public class ChessboardModel implements IBakedModel {

  /**
   * Create our model, using the given baked model as a base to add extra BakedQuads to.
   * @param i_baseChessboardModel the base model
   */
  public ChessboardModel(IBakedModel i_baseChessboardModel)
  {
    baseChessboardModel = i_baseChessboardModel;
//    chessboardItemOverrideList = new ChessboardItemOverrideList(Collections.EMPTY_LIST);
  }

  // property used to provide information to the item renderer - in this case, the number of chess pieces to draw on the
  //  board

  public static ModelProperty<Integer> NUMBER_OF_PIECES = new ModelProperty<>();

  public static ModelDataMap getEmptyIModelData() {
    ModelDataMap.Builder builder = new ModelDataMap.Builder();
    builder.withInitial(NUMBER_OF_PIECES, 0);
    ModelDataMap modelDataMap = builder.build();
    return modelDataMap;
  }

  // create a tag (ModelResourceLocation) for our model.
  //  "inventory" is used for item. If you don't specify it, you will end up with "normal" by default,
  //  which is used for block.
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");

  /**
   * Forge's extension in place of IBakedModel::getQuads
   * It allows us to pass in some extra information which we can use to choose the appropriate quads to render
   * @param state
   * @param side
   * @param rand
   * @param extraData
   * @return
   */
  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
  {
    if (!extraData.hasProperty(NUMBER_OF_PIECES)) {
      if (!loggedError) {
        LOGGER.error("IModelData did not have expected property NUMBER_OF_PIECES");
        loggedError = true;
      }
      return baseChessboardModel.getQuads(state, side, rand);
    }

    // we construct the item model by taking the base model's list of quads and appending extra quads to draw the chess pieces.

    Integer numberOfPieces = extraData.getData(NUMBER_OF_PIECES);
    List<BakedQuad> allQuads = new LinkedList<>();
    allQuads.addAll(baseChessboardModel.getQuads(state, side, rand, extraData));
    allQuads.addAll(getChessPiecesQuads());
    return allQuads;
  }

  @Override
  @Nonnull
  public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
  {
    ModelDataMap modelDataMap = getEmptyIModelData();
    modelDataMap.setData(NUMBER_OF_PIECES, bestAdjacentBlock);
    return modelDataMap;
  }

  @Override
  public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data)
  {
    return getActualBakedModelFromIModelData(data).getParticleTexture();
  }



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
  public List<BakedQuad> getQuads(BlockState state, Direction side, long rand) {
    return baseChessboardModel.getQuads(state, side, rand);
  }

  @Override
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
    return null;
  }

  // not needed for item, but hey
  @Override
  public boolean isAmbientOcclusion() {
    return baseChessboardModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return baseChessboardModel.isGui3d();
  }

  @Override
  public boolean func_230044_c_() {
    return false;
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return baseChessboardModel.getItemCameraTransforms();  // NB this is not enough for BakedItemModels, must do handlePerspective as well
  }

  @Override
  public ItemOverrideList getOverrides() {
    return chessboardItemOverrideList;
  }

  private IBakedModel baseChessboardModel;
  private ChessboardItemOverrideList chessboardItemOverrideList;

  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//    if (baseChessboardModel instanceof IPerspectiveAwareModel) {
      Matrix4f matrix4f = baseChessboardModel.handlePerspective(cameraTransformType).getRight();
      return Pair.of(this, matrix4f);
//    } else {
//      // If the base model isn't an IPerspectiveAware, we'll need to generate the correct matrix ourselves using the
//      //  ItemCameraTransforms.
//
//      ItemCameraTransforms itemCameraTransforms = baseChessboardModel.getItemCameraTransforms();
//      ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
//      TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
//      Matrix4f mat = null;
//      if (tr != null) { // && tr != TRSRTransformation.identity()) {
//        mat = tr.getMatrix();
//      }
//      // The TRSRTransformation for vanilla item have blockCenterToCorner() applied, however handlePerspective
//      //  reverses it back again with blockCornerToCenter().  So we don't need to apply it here.
//
//      return Pair.of(this, mat);
//    }
  }

  // ---- All these methods are required by the interface but we don't do anything special with them.

  @Override
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
    throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
  }

  // getTexture is used directly when player is inside the block.  The game will crash if you don't use something
  //   meaningful here.
  @Override
  public TextureAtlasSprite getParticleTexture() {
    return modelWhenNotCamouflaged.getParticleTexture();
  }


  // ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla or forge
  @Override
  public boolean isAmbientOcclusion()
  {
    return modelWhenNotCamouflaged.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d()
  {
    return modelWhenNotCamouflaged.isGui3d();
  }

  @Override
  public boolean func_230044_c_() {
    return modelWhenNotCamouflaged.func_230044_c_();  // related to item "diffuselighting"
  }

  @Override
  public boolean isBuiltInRenderer()
  {
    return modelWhenNotCamouflaged.isBuiltInRenderer();
  }

  @Override
  public ItemOverrideList getOverrides()
  {
    return modelWhenNotCamouflaged.getOverrides();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms()
  {
    return modelWhenNotCamouflaged.getItemCameraTransforms();
  }

  private static final Logger LOGGER = LogManager.getLogger();
  private static boolean loggedError = false; // prevent spamming console

}
