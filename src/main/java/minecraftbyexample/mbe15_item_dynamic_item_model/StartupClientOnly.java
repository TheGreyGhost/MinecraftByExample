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
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
//  public static void preInitClientOnly()
//  {
//    // ModelBakeEvent will be used to add our ISmartItemModel to the ModelManager's registry (the
//    //  registry used to map all the ModelResourceLocations to IBlockModels).
//    // For the chessboard item, it will map from
//    // "minecraftbyexample:mbe15_item_chessboard#inventory to our SmartChessboardModel instance
//    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandlerMBE15.instance);
//
//    // model to be used for rendering this item
//    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard_registry_name", "inventory");
//    final int DEFAULT_ITEM_SUBTYPE = 0;
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemChessBoard, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
//  }

  // Called after all the other baked block models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public static void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing model for ChessBoard - it will have been added automatically by vanilla due to our registration of
    //   of the item.
    // Replace the mapping with our custom ChessboardModel.

    ModelResourceLocation itemModelResourceLocation = ChessboardModel.modelResourceLocation;
//            new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard_registry_name", "inventory");
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
