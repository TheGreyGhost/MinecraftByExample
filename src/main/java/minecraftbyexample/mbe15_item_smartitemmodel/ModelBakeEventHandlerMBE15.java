package minecraftbyexample.mbe15_item_smartitemmodel;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * ModelBakeEvent will be used to add our ISmartBlockModel to the ModelManager's registry (the registry used to map
 *   all the ModelResourceLocations to IBlockModels).  For the example of the stone block, there is a map from
 *  ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
 * For the camouflage block, it will map from
 *  CamouflageISmartBlockModelFactory.modelResourceLocation to our CamouflageISmartBlockModelFactory instance
 */
public class ModelBakeEventHandlerMBE15 {
  public static final ModelBakeEventHandlerMBE15 instance = new ModelBakeEventHandlerMBE15();

  private ModelBakeEventHandlerMBE15() {};

  // Called after all the other baked block models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing mapping for CamouflageISmartBlockModelFactory - it will have been added automatically because
    //  we registered a custom BlockStateMapper for it (using ModelLoader.setCustomStateMapper)
    // Replace the mapping with our ISmartBlockModel.
    Object object =  event.modelRegistry.getObject(ChessboardSmartItemModel.modelResourceLocation);
    if (object instanceof IBakedModel) {
      IBakedModel existingModel = (IBakedModel)object;
      ChessboardSmartItemModel customModel = new ChessboardSmartItemModel(existingModel);
      event.modelRegistry.putObject(ChessboardSmartItemModel.modelResourceLocation, customModel);
    }
  }
}
