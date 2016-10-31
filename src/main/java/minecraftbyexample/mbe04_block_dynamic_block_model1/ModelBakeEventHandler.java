package minecraftbyexample.mbe04_block_dynamic_block_model1;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * ModelBakeEvent will be used to add our CamouflageBakedModel to the ModelManager's registry (the registry used to map
 *   all the ModelResourceLocations to IBlockModels).  For the example of the stone block, there is a map from
 *  ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
 * For the camouflage block, it will map from
 *  CamouflageBakedModel.modelResourceLocation to our CamouflageBakedModel instance
 *
 *  If there are multiple variants / models for your block, you need to find and replace them all
 *
 */
public class ModelBakeEventHandler {
  public static final ModelBakeEventHandler instance = new ModelBakeEventHandler();

  private ModelBakeEventHandler() {};

  // Called after all the other baked block models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing mapping for CamouflageBakedModel - it will have been added automatically because
    //  we registered a custom BlockStateMapper for it (using ModelLoader.setCustomStateMapper)
    // Replace the mapping with our CamouflageBakedModel.
    Object object =  event.getModelRegistry().getObject(CamouflageBakedModel.variantTag);
    if (object instanceof IBakedModel) {
      IBakedModel existingModel = (IBakedModel)object;
      CamouflageBakedModel customModel = new CamouflageBakedModel(existingModel);
      event.getModelRegistry().putObject(CamouflageBakedModel.variantTag, customModel);
    }
  }
}
