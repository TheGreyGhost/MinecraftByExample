package minecraftbyexample.mbe05_block_smartblockmodel2;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by TheGreyGhost on 22/04/2015.
 */
public class WebModel implements IModel {

    public static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("minecraftbyexample:blocks/mbe05_block_web");
//    public static final ResourceLocation TB = new ResourceLocation("mymodid:blocks/textureb");
//    public static final ResourceLocation TC = new ResourceLocation("mymodid:blocks/texturec");

    public static final ResourceLocation MODEL_CORE = new ResourceLocation("minecraftbyexample:block/mbe05_block_web_core");
    public static final ResourceLocation MODEL_UP = new ResourceLocation("minecraftbyexample:block/mbe05_block_web_up");
//
//    public static final ResourceLocation MB = new ResourceLocation("mymodid:block/modelb");
//    public static final ResourceLocation MC = new ResourceLocation("mymodid:block/modelc");

    public WebModel(IResourceManager resourceManager) {

    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
      return ImmutableList.copyOf(new ResourceLocation[]{MODEL_CORE, MODEL_UP});
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
      return ImmutableList.copyOf(new ResourceLocation[]{TEXTURE_SHEET});
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ModelLoaderRegistry.getModel?
                the dependencies are automatically loaded in ModelLoader.loadAnyModel



      return new CompositeModel(format, bakedTextureGetter);
    }

    // Our custom loaded doesn't need a default state, just return null
    @Override
    public IModelState getDefaultState() {
      return null;
    }

}
