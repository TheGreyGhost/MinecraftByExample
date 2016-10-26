package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 // ModelBakeEvent will be used to add our ISmartItemModel to the ModelManager's registry (the
 //  registry used to map all the ModelResourceLocations to IBlockModels).
 // For the chessboard item, it will map from
 // "minecraftbyexample:mbe15_item_chessboard#inventory to our SmartChessboardModel instance
 */
public class ModelBakeEventHandlerMBE15 {
  public static final ModelBakeEventHandlerMBE15 instance = new ModelBakeEventHandlerMBE15();

  private ModelBakeEventHandlerMBE15() {};

  // Called after all the other baked models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing mapping for ChessboardModel - we added it in StartupClientOnly.initClientOnly(), which
    //   caused it to be loaded from resources (model/items/mbe15_item_chessboard.json) just like an ordinary item
    // Replace the mapping with our ISmartBlockModel, using the existing mapped model as the base for the smart model.
    Object object =  event.getModelRegistry().getObject(ChessboardModel.modelResourceLocation);
    if (object instanceof IBakedModel) {
      IBakedModel existingModel = (IBakedModel)object;
      ChessboardModel customModel = new ChessboardModel(existingModel);
      event.getModelRegistry().putObject(ChessboardModel.modelResourceLocation, customModel);
    }
  }
}
