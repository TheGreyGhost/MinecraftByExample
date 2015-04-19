package minecraftbyexample.mbe04_block_smartblockmodel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import java.util.List;

/**
 * Created by EveryoneElse on 19/04/2015.
 */
public class CamouflageModel implements IBakedModel {

  public CamouflageModel(IBlockState iBlockState)
  {

  }

  @Override
  public List getFaceQuads(EnumFacing p_177551_1_) {
    return null;
  }

  @Override
  public List getGeneralQuads() {
    return null;
  }

  @Override
  public boolean isAmbientOcclusion() {
    return false;
  }

  @Override
  public boolean isGui3d() {
    return false;
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public TextureAtlasSprite getTexture() {
    return null;
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return null;
  }

  private IBlockState blockBeingImitated;
}
