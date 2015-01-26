package minecraftbyexample.mbe14_item_camera_transforms;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * User: The Grey Ghost
 * Date: 20/01/2015
 * We use the ModelBakeEvent to iterate through all the registered models, wrap each one in an ItemModelFlexibleCamera, and write it
 *   back into the registry.
 * Each wrapped model gets a reference to ModelBakeEventHandler::itemOverrideLink.
 * Later, we can alter the members of itemOverrideLink to change the ItemCameraTransforms for a desired Item
 */
public class ModelBakeEventHandler
{
  public ModelBakeEventHandler()
  {
    itemOverrideLink.forcedTransform = new ItemCameraTransforms(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
                                                                ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
  }

  @SubscribeEvent
  public void modelBakeEvent(ModelBakeEvent event)
  {
    IRegistry modelRegistry = event.modelRegistry;
    if (!(modelRegistry instanceof RegistrySimple)) {
      System.err.println("ModelBakeEventHandler::modelBakeEvent expected modelRegistry to be RegistrySimple, was actually:"+modelRegistry);
      return;
    }
    RegistrySimple modelSimpleRegistry = (RegistrySimple)modelRegistry;

    for (Object modelKey : modelSimpleRegistry.getKeys()) {
      IBakedModel iBakedModel = (IBakedModel)event.modelRegistry.getObject(modelKey);
      ItemModelFlexibleCamera wrappedModel = new ItemModelFlexibleCamera(iBakedModel, itemOverrideLink);
      event.modelRegistry.putObject(modelKey, wrappedModel);
    }
  }

  public ItemModelFlexibleCamera.UpdateLink getItemOverrideLink() {
    return itemOverrideLink;
  }

  private ItemModelFlexibleCamera.UpdateLink itemOverrideLink = new ItemModelFlexibleCamera.UpdateLink();
}
