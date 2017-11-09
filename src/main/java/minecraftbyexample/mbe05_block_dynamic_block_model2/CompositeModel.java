package minecraftbyexample.mbe05_block_dynamic_block_model2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TheGreyGhost on 22/04/2015.
 * CompositeModel is the class used to dynamically create a list of quads based on the web block's IBlockState
 * The Model is constructed from the various models corresponding to the subcomponents:
 * 1) "core" for the solid core in the middle of the web
 * 2) up, down, north, south, east, west for each "stem" that extends from the core in the centre to the edge of the block
 *   (eg the up model connects the core to the top face of the block)
 * An IBakedModel is just a list of quads; the composite model is created by concatenating the quads from the relevant
 *   subcomponents.
 *
 *   BEWARE! Rendering is multithreaded so your CompositeModel must be thread-safe

 */
public class CompositeModel implements IBakedModel {

  public CompositeModel(IBakedModel i_modelCore, IBakedModel i_modelUp, IBakedModel i_modelDown,
                        IBakedModel i_modelWest, IBakedModel i_modelEast,
                        IBakedModel i_modelNorth, IBakedModel i_modelSouth)
  {
    modelCore = i_modelCore;
    modelUp = i_modelUp;
    modelDown = i_modelDown;
    modelWest = i_modelWest;
    modelEast = i_modelEast;
    modelNorth = i_modelNorth;
    modelSouth = i_modelSouth;
  }

  /**
   * Compile a list of quads for rendering.  This is done by making a list of all the quads from the component
   *   models, depending on which links are present.
   * For example
   * @param blockState
   * @param side which side of the block is being rendered; null =
   * @param rand
   * @return
   */
  @Override
  public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
    List<BakedQuad> quadsList = new LinkedList<BakedQuad>();
    quadsList.addAll(modelCore.getQuads(blockState, side, rand));
    if (!(blockState instanceof IExtendedBlockState)) {
      return quadsList;
    }
    IExtendedBlockState extendedBlockState = (IExtendedBlockState)blockState;
    if (isLinkPresent(extendedBlockState, Block3DWeb.LINK_UP)) {
      quadsList.addAll(modelUp.getQuads(extendedBlockState, side, rand));
    }
    if (isLinkPresent(extendedBlockState, Block3DWeb.LINK_DOWN)) {
      quadsList.addAll(modelDown.getQuads(extendedBlockState, side, rand));
    }
    if (isLinkPresent(extendedBlockState, Block3DWeb.LINK_WEST)) {
      quadsList.addAll(modelWest.getQuads(extendedBlockState, side, rand));
    }
    if (isLinkPresent(extendedBlockState, Block3DWeb.LINK_EAST)) {
      quadsList.addAll(modelEast.getQuads(extendedBlockState, side, rand));
    }
    if (isLinkPresent(extendedBlockState, Block3DWeb.LINK_NORTH)) {
      quadsList.addAll(modelNorth.getQuads(extendedBlockState, side, rand));
    }
    if (isLinkPresent(extendedBlockState, Block3DWeb.LINK_SOUTH)) {
      quadsList.addAll(modelSouth.getQuads(extendedBlockState, side, rand));
    }
    return quadsList;
  }

  @Override
  public boolean isAmbientOcclusion() {
    return modelCore.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return modelCore.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  // used for block breaking shards
  @Override
  public TextureAtlasSprite getParticleTexture() {
    TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks()
                                                     .getAtlasSprite("minecraftbyexample:blocks/mbe05_block_3d_web");

    return textureAtlasSprite;
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return modelCore.getItemCameraTransforms();
  }

  /** this method is necessary because Forge has deprecated getItemCameraTransforms(), and modelCore.getItemCameraTransforms()
   *    may not return anything meaningful.  But if the base model doesn't implement IPerspectiveAwareModel then you
   *    need to generate it.
   * @param cameraTransformType
   * @return
   */
  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//    if (modelCore instanceof IPerspectiveAwareModel) {
      Matrix4f matrix4f = modelCore.handlePerspective(cameraTransformType).getRight();
      return Pair.of(this, matrix4f);
//    } else {
//      // If the parent model isn't an IPerspectiveAware, we'll need to generate the correct matrix ourselves using the
//      //  ItemCameraTransforms.
//
//      ItemCameraTransforms itemCameraTransforms = modelCore.getItemCameraTransforms();
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
    return null;
  }

  private boolean isLinkPresent(IExtendedBlockState iExtendedBlockState, IUnlistedProperty<Boolean> whichLink)
  {
    Boolean link = iExtendedBlockState.getValue(whichLink);
    if (link == null) {
      return false;
    }
    return link;
  }

  private IBakedModel modelCore;
  private IBakedModel modelUp;
  private IBakedModel modelDown;
  private IBakedModel modelWest;
  private IBakedModel modelEast;
  private IBakedModel modelNorth;
  private IBakedModel modelSouth;
}
