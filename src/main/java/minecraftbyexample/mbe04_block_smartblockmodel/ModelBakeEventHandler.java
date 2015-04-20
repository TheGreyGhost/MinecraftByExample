package minecraftbyexample.mbe04_block_smartblockmodel;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * ModelBakeEvent will be used to add our ISmartBlockModel to the ModelManager's registry, i.e. the
 *  registry used to map all the ModelResourceLocations to IBlockModels.  For the stone example there is a map from
 *  ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
 * For the camouflage block, it will map from
 *  CamouflageISmartBlockModelFactory.modelResourceLocation to our CamouflageISmartBlockModelFactory instance
 */
public class ModelBakeEventHandler {
  public static final ModelBakeEventHandler instance = new ModelBakeEventHandler();

  private ModelBakeEventHandler() {};

  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event)
  {
    CamouflageISmartBlockModelFactory customModel = new CamouflageISmartBlockModelFactory();
    Object object =  event.modelRegistry.getObject(CamouflageISmartBlockModelFactory.modelResourceLocation);
    event.modelRegistry.putObject(CamouflageISmartBlockModelFactory.modelResourceLocation, customModel);
//    event.modelRegistry.putObject(ClientProxy.itemLocation, customModel);
  }
}
