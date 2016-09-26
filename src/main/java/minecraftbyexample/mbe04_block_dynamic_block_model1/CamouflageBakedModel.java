package minecraftbyexample.mbe04_block_dynamic_block_model1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class is used to customise the rendering of the camouflage block, based on the block it is copying.
 */
public class CamouflageBakedModel implements IBakedModel {

  public CamouflageBakedModel(IBakedModel unCamouflagedModel)
  {
    modelWhenNotCamouflaged = unCamouflagedModel;
  }

  // create a tag (ModelResourceLocation) for our model.
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe04_block_camouflage");

  // return a list of the quads making up the model.
  // We choose the model based on the IBlockstate provided by the caller.
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
  {
    return handleBlockState(state).getQuads(state, side, rand);
  }

  // This method is used to create a suitable IBakedModel based on the IBlockState of the block being rendered.
  // If IBlockState is an instance of IExtendedBlockState, you can use it to pass in any information you want.
  // Some folks return a new instance of the same ISmartBlockModel; I think it is more logical to return a different
  //   class which implements IBakedModel instead of ISmartBlockModel, but it's a matter of taste.
  //  BEWARE! Rendering is multithreaded so your ISmartBlockModel must be thread-safe, preferably immutable.

  private IBakedModel handleBlockState(@Nullable IBlockState iBlockState)
  {
    IBakedModel retval = modelWhenNotCamouflaged;  // default
    IBlockState UNCAMOUFLAGED_BLOCK = Blocks.AIR.getDefaultState();

    // Extract the block to be copied from the IExtendedBlockState, previously set by Block.getExtendedState()
    // If the block is null, the block is not camouflaged so use the uncamouflaged model.
    if (iBlockState instanceof IExtendedBlockState) {
      IExtendedBlockState iExtendedBlockState = (IExtendedBlockState) iBlockState;
      IBlockState copiedBlockIBlockState = iExtendedBlockState.getValue(BlockCamouflage.COPIEDBLOCK);

      if (copiedBlockIBlockState != UNCAMOUFLAGED_BLOCK) {
        // Retrieve the IBakedModel of the copied block and return it.
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
        BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
        IBakedModel copiedBlockModel = blockModelShapes.getModelForState(copiedBlockIBlockState);
        retval = copiedBlockModel;
      }
    }
    return retval;
  }

  private IBakedModel modelWhenNotCamouflaged;

  // getTexture is used directly when player is inside the block.  The game will crash if you don't use something
  //   meaningful here.
  @Override
  public TextureAtlasSprite getParticleTexture() {
    return modelWhenNotCamouflaged.getParticleTexture();
  }

  // ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla
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
  public boolean isBuiltInRenderer()
  {
    return modelWhenNotCamouflaged.isBuiltInRenderer();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms()
  {
    return modelWhenNotCamouflaged.getItemCameraTransforms();
  }

  @Override
  public ItemOverrideList getOverrides()
  {
    return modelWhenNotCamouflaged.getOverrides();
  }


}
