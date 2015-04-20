package minecraftbyexample.mbe04_block_smartblockmodel;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;
import java.util.Map;

/**
 * Created by TheGreyGhost on 19/04/2015.
 */
public class CamouflageISmartBlockModelFactory implements ISmartBlockModel {

  // create a tag (ModelResourceLocation) for our model.
  // Blocks usually have "normal" as the variant, i.e. modid:block#normal, in this case we use
  //  "custom" to avoid creating a duplicate.
//  public static final ModelResourceLocation modelResourceLocation
//          = new ModelResourceLocation("minecraftbyexample:mbe04_block_camouflage", "custom");
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe04_block_camouflage_model");

  @SuppressWarnings("deprecation")  // IBakedModel is deprecated to encourage folks to use IFlexibleBakedModel instead
                                    // .. but IFlexibleBakedModel is of no use here...

  // This method is used to create a suitable IBakedModel based on the IBlockState of the block being rendered.
  // If IBlockState is an instance of IExtendedBlockState, you can use it to pass in any information you want.
  // Some folks return a new instance of the same ISmartBlockModel; I think it is more logical to return a different
  //   class which implements IBakedModel instead of ISmartBlockModel, but it's a matter of taste.
  @Override
  public IBakedModel handleBlockState(IBlockState iBlockState)
  {
    // Extract the block to be copied from the IExtendedBlockState, previously set by Block.getExtendedState()
    IBlockState copiedBlockIBlockState = Blocks.slime_block.getDefaultState();
    if (iBlockState instanceof IExtendedBlockState) {
      IExtendedBlockState iExtendedBlockState = (IExtendedBlockState) iBlockState;
      copiedBlockIBlockState = iExtendedBlockState.getValue(BlockCamouflage.COPIEDBLOCK);
    }

    // Retrieve the IBakedModel of the copied block and return it.
    Minecraft mc = Minecraft.getMinecraft();
    BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
    BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
    IBakedModel copiedBlockModel = blockModelShapes.getModelForState(copiedBlockIBlockState);
    if (copiedBlockModel instanceof ISmartBlockModel) {
      copiedBlockModel = ((ISmartBlockModel)copiedBlockModel).handleBlockState(copiedBlockIBlockState);
    }
    return copiedBlockModel;
  }


  // The methods below are all unused for CamouflageISmartBlockModelFactory because we always return a vanilla model
  //  from handleBlockState.

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
