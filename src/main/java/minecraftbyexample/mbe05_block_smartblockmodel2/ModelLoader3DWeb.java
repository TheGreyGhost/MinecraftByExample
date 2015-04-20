//package minecraftbyexample.mbe04_block_smartblockmodel;
//
//import net.minecraft.client.resources.IResourceManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.model.ICustomModelLoader;
//import net.minecraftforge.client.model.IModel;
//
///**
// * Created by TheGreyGhost on 19/04/2015.
// *
// *
// */
//public class ModelLoader3DWeb implements ICustomModelLoader
//{
//  private IResourceManager resourceManager;
//  private final String mymodelResourcePath = "models/block/"
//
//  @Override
//  public void onResourceManagerReload(IResourceManager resourceManager) {
//    this.resourceManager = resourceManager;
//  }
//
//  @Override
//  public boolean accepts(ResourceLocation resourceLocation) {
//    return resourceLocation.getResourceDomain().equals("minecraftbyexample")
//           && resourceLocation.getResourcePath().startsWith("models/block/builtin/");
//  }
//
//  @Override
//  public IModel loadModel(ResourceLocation resourceLocation) {
//    String r = resourceLocation.getResourcePath().substring("models/block/builtin/".length());
//    if(r.equals("mymodel")) {
//      return new MyBlockModel(resourceManager);
//    }
//    throw new RuntimeException("A builtin model '" + r + "' is not defined.");
//  }
//}
