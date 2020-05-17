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
 */
public class ChessboardModel implements IBakedModel {

  /**
   * Create our model, using the given baked model as a base to add extra BakedQuads to.
   * @param i_baseChessboardModel the base model
   */
  public ChessboardModel(IBakedModel i_baseChessboardModel)
  {
    baseChessboardModel = i_baseChessboardModel;
    chessboardItemOverrideList = new ChessboardItemOverrideList();
  }

  // create a tag (ModelResourceLocation) for our model.
  //  "inventory" is used for item. If you don't specify it, you will end up with "" by default,
  //  which is used for block.
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");

  // called for item rendering
  @Override
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
    return baseChessboardModel.getQuads(state, side, rand);
  }

  @Override
  public ItemOverrideList getOverrides() {
    return chessboardItemOverrideList;
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
    return baseChessboardModel.func_230044_c_();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return baseChessboardModel.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return baseChessboardModel.getParticleTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return baseChessboardModel.getItemCameraTransforms();
  }

  // This is a forge extension that is expected for blocks only.
  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
    throw new AssertionError("ChessboardModel::getQuads(IModelData) should never be called");
  }

  // This is a forge extension that is expected for blocks only.
  @Override
  @Nonnull
  public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
  {
    throw new AssertionError("ChessboardModel::getModelData should never be called");
  }

  private IBakedModel baseChessboardModel;
  private ChessboardItemOverrideList chessboardItemOverrideList;

//  @Override
//  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
////    if (baseChessboardModel instanceof IPerspectiveAwareModel) {
//      Matrix4f matrix4f = baseChessboardModel.handlePerspective(cameraTransformType).getRight();
//      return Pair.of(this, matrix4f);
////    } else {
////      // If the base model isn't an IPerspectiveAware, we'll need to generate the correct matrix ourselves using the
////      //  ItemCameraTransforms.
////
////      ItemCameraTransforms itemCameraTransforms = baseChessboardModel.getItemCameraTransforms();
////      ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
////      TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
////      Matrix4f mat = null;
////      if (tr != null) { // && tr != TRSRTransformation.identity()) {
////        mat = tr.getMatrix();
////      }
////      // The TRSRTransformation for vanilla item have blockCenterToCorner() applied, however handlePerspective
////      //  reverses it back again with blockCornerToCenter().  So we don't need to apply it here.
////
////      return Pair.of(this, mat);
//    }
//  }

  private static final Logger LOGGER = LogManager.getLogger();
  private static boolean loggedError = false; // prevent spamming console

}
