package minecraftbyexample.mbe04_block_dynamic_block_models;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.client.renderer.texture.AtlasTexture.LOCATION_BLOCKS_TEXTURE;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class  StartupClientOnly
{
  // Called after all the other baked block models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public static void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing mappings for CamouflageBakedModel - they will have been added automatically because
    //  of our blockstates file for the BlockCamouflage.
    // Replace the mapping with our CamouflageBakedModel.
    // we only have one BlockState variant but I've shown code that loops through all of them, in case you have more than one.

    for (BlockState blockState : StartupCommon.blockCamouflage.getStateContainer().getValidStates()) {
      ModelResourceLocation variantMRL = BlockModelShapes.getModelLocation(blockState);
      IBakedModel existingModel = event.getModelRegistry().get(variantMRL);
      if (existingModel == null) {
        LOGGER.warn("Did not find the expected vanilla baked model(s) for blockCamouflage in registry");
      } else if (existingModel instanceof CamouflageBakedModel) {
        LOGGER.warn("Tried to replace CamouflagedBakedModel twice");
      } else {
        CamouflageBakedModel customModel = new CamouflageBakedModel(existingModel);
        event.getModelRegistry().put(variantMRL, customModel);
      }
    }

    // repeat the same trick for the Altimeter baked model

    for (BlockState blockState : StartupCommon.blockAltimeter.getStateContainer().getValidStates()) {
      ModelResourceLocation variantMRL = BlockModelShapes.getModelLocation(blockState);
      IBakedModel existingModel = event.getModelRegistry().get(variantMRL);
      if (existingModel == null) {
        LOGGER.warn("Did not find the expected vanilla baked model(s) for blockAltimeter in registry");
      } else if (existingModel instanceof AltimeterBakedModel) {
        LOGGER.warn("Tried to replace AltimeterBakedModel twice");
      } else {
        AltimeterBakedModel customModel = new AltimeterBakedModel(existingModel);
        event.getModelRegistry().put(variantMRL, customModel);
      }
    }

  }

  @SubscribeEvent
  public static void onModelRegistryEvent(ModelRegistryEvent event) {
    ModelLoader.addSpecialModel(AltimeterBakedModel.needleModelRL);
  }

  // mbe04b_altimeter uses two textures which aren't in the block model
  // In order to use them during block rendering, we need to manually register them for stitching into the blocks texture sheet
  // Alternatively, you could use them in an invisible part of your block model.
    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
      if (event.getMap().getTextureLocation() == LOCATION_BLOCKS_TEXTURE) {
        event.addSprite(AltimeterBakedModel.digitsTextureRL);
        event.addSprite(AltimeterBakedModel.needleTextureRL);
      }
    }
  
  /**
   *
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // Tell the renderer to render the camouflage block as a solid texture
    RenderTypeLookup.setRenderLayer(StartupCommon.blockCamouflage, RenderType.getSolid());
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
