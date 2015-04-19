package minecraftbyexample.mbe04_block_smartblockmodel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;
import java.util.Map;

/**
 * Created by TheGreyGhost on 19/04/2015.
 */
public class CamouflageModelFactory implements ISmartBlockModel {

  public final ModelResourceLocation defaultModel = new ModelResourceLocation("minecraftbyexample:mbe04_block_camouflage");

  @SuppressWarnings("deprecation")  // IBakedModel is deprecated to encourage folks to use IFlexibleBakedModel instead
                                    // .. IFlexibleBakedModel is of no use here...
  @Override
  public IBakedModel handleBlockState(IBlockState iBlockState)
  {
    if (iBlockState instanceof IExtendedBlockState) {
      IExtendedBlockState
              IProperty
      Minecraft mc = Minecraft.getMinecraft();
      BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
      BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();

      IBakedModel ibakedmodel = blockModelShapes.getModelForState(defaultState);
      if (ibakedmodel instanceof net.minecraftforge.client.model.ISmartBlockModel) {
        ibakedmodel = ((net.minecraftforge.client.model.ISmartBlockModel) ibakedmodel).handleBlockState(defaultState);
      }

    }
        return new CamouflageModel(state);
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

}
