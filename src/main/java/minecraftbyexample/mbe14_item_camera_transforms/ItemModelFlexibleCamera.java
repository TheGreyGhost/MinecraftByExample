package minecraftbyexample.mbe14_item_camera_transforms;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 20/01/2015
 * This class is a simple wrapper to substitute a new set of camera transforms for an existing item
 * Usage:
 * 1) Construct a new ItemModelFlexibleCamera with the model to wrap and the ItemCameraTransforms object
 * 2) Replace the ItemModelFlexibleCamera into the ModelBakery in place of the model to wrap
 * 3) alter the ItemCameraTransforms to change the transform
 */
public class ItemModelFlexibleCamera implements IBakedModel
{
  public ItemModelFlexibleCamera(IBakedModel i_modelToWrap, ItemCameraTransforms i_cameraTransforms)
  {
    itemCameraTransforms = i_cameraTransforms;
    iBakedModel = i_modelToWrap;
  }

  @Override
  public List getFaceQuads(EnumFacing enumFacing) {
    return iBakedModel.getFaceQuads(enumFacing);
  }

  @Override
  public List getGeneralQuads() {
    return iBakedModel.getGeneralQuads();
  }

  @Override
  public boolean isAmbientOcclusion() {
    return iBakedModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return iBakedModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return iBakedModel.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getTexture() {
    return iBakedModel.getTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return itemCameraTransforms;
  }

  private ItemCameraTransforms itemCameraTransforms;
  private IBakedModel iBakedModel;
}
