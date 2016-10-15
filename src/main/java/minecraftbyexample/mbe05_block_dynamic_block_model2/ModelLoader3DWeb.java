package minecraftbyexample.mbe05_block_dynamic_block_model2;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * The ModelLoader3DWeb is used to "load" the Block3D's model instead of the Vanilla loader looking for a .json file
 *
 */
public class ModelLoader3DWeb implements ICustomModelLoader
{
  public final String SMART_MODEL_RESOURCE_LOCATION = "models/block/smartmodel/";

  // return true if our Model Loader accepts this ModelResourceLocation
  @Override
  public boolean accepts(ResourceLocation resourceLocation) {
    return resourceLocation.getResourceDomain().equals("minecraftbyexample")
           && resourceLocation.getResourcePath().startsWith(SMART_MODEL_RESOURCE_LOCATION);
  }

  // When called for our Block3DWeb's ModelResourceLocation, return our WebModel.
  @Override
  public IModel loadModel(ResourceLocation resourceLocation) {
    String resourcePath = resourceLocation.getResourcePath();
    if (!resourcePath.startsWith(SMART_MODEL_RESOURCE_LOCATION)) {
      assert false : "loadModel expected " + SMART_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
    }
    String modelName = resourcePath.substring(SMART_MODEL_RESOURCE_LOCATION.length());

    if (modelName.equals("webmodel")) {
      return new WebModel();
    } else {
      return ModelLoaderRegistry.getMissingModel();
    }
  }

  // don't need it for this example; you might.  We have to implement it anyway.
  @Override
  public void onResourceManagerReload(IResourceManager resourceManager) {
    this.resourceManager = resourceManager;
  }

  private IResourceManager resourceManager;
}
