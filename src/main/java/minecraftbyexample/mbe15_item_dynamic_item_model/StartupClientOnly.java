package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The methods in this class are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  // Called after all the other baked block models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public static void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing model for ChessBoard - it will have been added automatically by vanilla due to our registration of
    //   of the item in StartupCommon.
    // Replace the mapping with our custom ChessboardModel.

    ModelResourceLocation itemModelResourceLocation = ChessboardModel.modelResourceLocation;
    IBakedModel existingModel = event.getModelRegistry().get(itemModelResourceLocation);
    if (existingModel == null) {
      LOGGER.warn("Did not find the expected vanilla baked model for ChessboardModel in registry");
    } else if (existingModel instanceof ChessboardModel) {
      LOGGER.warn("Tried to replace ChessboardModel twice");
    } else {
      ChessboardModel customModel = new ChessboardModel(existingModel);
      event.getModelRegistry().put(itemModelResourceLocation, customModel);
    }
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
