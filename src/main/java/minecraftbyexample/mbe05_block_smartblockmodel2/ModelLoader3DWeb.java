package minecraftbyexample.mbe05_block_smartblockmodel2;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 *
 */
public class ModelLoader3DWeb implements ICustomModelLoader
{
  private IResourceManager resourceManager;
  private final String SMART_MODEL_RESOURCE_LOCATION = "models/block/smartmodel/";

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager) {
    this.resourceManager = resourceManager;
  }

  @Override
  public boolean accepts(ResourceLocation resourceLocation) {
    return resourceLocation.getResourceDomain().equals("minecraftbyexample")
           && resourceLocation.getResourcePath().startsWith(SMART_MODEL_RESOURCE_LOCATION);
  }

  @Override
  public IModel loadModel(ResourceLocation resourceLocation) {
    String resourcePath = resourceLocation.getResourcePath();
    if (!resourcePath.startsWith(SMART_MODEL_RESOURCE_LOCATION)) {
      assert false : "loadModel expected " + SMART_MODEL_RESOURCE_LOCATION + " but found " + resourcePath;
    }
    String modelName = resourcePath.substring(SMART_MODEL_RESOURCE_LOCATION.length());

    if(modelName.equals("webmodel")) {

      ModelLoader.VanillaLoader.instance.loadModel

      return new WebModel();
    }

  }
}
