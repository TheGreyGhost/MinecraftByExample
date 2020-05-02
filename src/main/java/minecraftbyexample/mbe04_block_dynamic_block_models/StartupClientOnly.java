package minecraftbyexample.mbe04_block_dynamic_block_models;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The classes for this example are called during startup
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
        LOGGER.warn("Did not find CamouflageBakedModel in registry");
      } else if (existingModel instanceof CamouflageBakedModel) {
        LOGGER.warn("Tried to replace CamouflagedBakedModel twice");
      } else {
        CamouflageBakedModel customModel = new CamouflageBakedModel(existingModel);
        event.getModelRegistry().put(variantMRL, customModel);
      }
    }
  }

  /**
   * Tell the renderer this is a solid block
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockCamouflage, RenderType.getSolid());
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
