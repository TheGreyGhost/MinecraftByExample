//package minecraftbyexample.mbe81_entity_projectile;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.renderer.vertex.VertexFormat;
//import net.minecraft.profiler.IProfiler;
//import net.minecraft.resources.IResourceManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.resource.IResourceType;
//import net.minecraftforge.resource.ISelectiveResourceReloadListener;
//
//import javax.annotation.Nullable;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executor;
//import java.util.function.Function;
//import java.util.function.Predicate;
//
///**
// * Created by TGG on 25/08/2020.
// * modified from WIPtech code made by Cadiboo and Draco18s.
// */
//public class EntityObjModelCache implements ISelectiveResourceReloadListener {
//
//  public static final EntityObjModelCache INSTANCE = new EntityObjModelCache();
//
//  private EntityObjModelCache() {
//  }
//
//  public static final IModelState										DEFAULTMODELSTATE		= part -> java.util.Optional.empty();
//  public static final VertexFormat DEFAULTVERTEXFORMAT		= DefaultVertexFormats.BLOCK;
//  public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULTTEXTUREGETTER	= texture -> Minecraft.getInstance().g().getAtlasSprite(texture.toString());
//
//  private final Map<ResourceLocation, IModel>			modelCache	= new HashMap<>();
//  private final Map<ResourceLocation, IBakedModel>	bakedCache	= new HashMap<>();
//
//  public IModel getModel(final ModResourceLocation location) {
//    IModel model = this.modelCache.get(location);
//    if (model == null) {
//      try {
//        model = ModelLoaderRegistry.getModel(location);
//      } catch (final Exception e) {
//        WIPTech.error("Error loading model " + location.toString());
//        e.printStackTrace();
//        model = ModelLoaderRegistry.getMissingModel();
//      }
//      this.modelCache.put(location, model);
//    }
//    return model;
//  }
//
//  public IBakedModel getBakedModel(final ModResourceLocation location) {
//    return this.getBakedModel(location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
//  }
//
//  public IBakedModel getBakedModel(final ModResourceLocation location, final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
//    IBakedModel bakedModel = this.bakedCache.get(location);
//    if (bakedModel == null) {
//      bakedModel = this.getModel(location).bake(state, format, textureGetter);
//      this.bakedCache.put(location, bakedModel);
//    }
//    return bakedModel;
//  }
//
//  @Nullable
//  @Override
//  public IResourceType getResourceType() {
//    return net.minecraftforge.resource.VanillaResourceType.MODELS;
//  }
//
//  @Override
//  public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
//    this.modelCache.clear();
//    this.bakedCache.clear();
//
//  }
//}