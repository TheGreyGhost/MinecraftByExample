package minecraftbyexample.mbe05_block_dynamic_block_model2;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

/**
 * Created by TheGreyGhost on 22/04/2015.
 * WebModel is an intermediate step between the ModelResourceLocation and the final IBakedModel.
 * In this case, our IModel has a number of dependant sub-components.  We bake them all individually and store them
 *   in the CompositeModel.
 * Each of the submodels needs to be loaded by vanilla as a block model which means we need a blockstates file for each of them.
 *   see blockstates/mbe05_web_subblocks
 */
public class WebModel implements IModel {

    public static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("minecraftbyexample:blocks/mbe05_block_3d_web");

    public static final ModelResourceLocation MODEL_CORE = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_core_model");
    public static final ModelResourceLocation MODEL_UP = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_up_model");
    public static final ModelResourceLocation MODEL_DOWN = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_down_model");
    public static final ModelResourceLocation MODEL_NORTH = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_north_model");
    public static final ModelResourceLocation MODEL_SOUTH = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_south_model");
    public static final ModelResourceLocation MODEL_WEST = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_west_model");
    public static final ModelResourceLocation MODEL_EAST = new ModelResourceLocation("minecraftbyexample:mbe05_web_subblocks/mbe05_block_web_east_model");

    public WebModel() {
    }

  // return all other resources used by this model (not strictly needed for this example because we load all the subcomponent
  //   models during the bake anyway)
    @Override
    public Collection<ResourceLocation> getDependencies() {
      return ImmutableList.copyOf(new ResourceLocation[]{MODEL_CORE, MODEL_UP, MODEL_DOWN, MODEL_WEST, MODEL_EAST, MODEL_NORTH, MODEL_SOUTH});
    }

    // return all the textures used by this model (not strictly needed for this example because we load all the subcomponent
    //   models during the bake anyway)
    @Override
    public Collection<ResourceLocation> getTextures() {
      return ImmutableList.copyOf(new ResourceLocation[]{TEXTURE_SHEET});
    }

  //  Bake the subcomponents into a CompositeModel
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)  {
      try {
        IModel subComponent = ModelLoaderRegistry.getModel(MODEL_CORE);
        IBakedModel bakedModelCore = subComponent.bake(state, format, bakedTextureGetter);

        subComponent = ModelLoaderRegistry.getModel(MODEL_UP);
        IBakedModel bakedModelUp = subComponent.bake(state, format, bakedTextureGetter);

        subComponent = ModelLoaderRegistry.getModel(MODEL_DOWN);
        IBakedModel bakedModelDown = subComponent.bake(state, format, bakedTextureGetter);

        subComponent = ModelLoaderRegistry.getModel(MODEL_WEST);
        IBakedModel bakedModelWest = subComponent.bake(state, format, bakedTextureGetter);

        subComponent = ModelLoaderRegistry.getModel(MODEL_EAST);
        IBakedModel bakedModelEast = subComponent.bake(state, format, bakedTextureGetter);

        subComponent = ModelLoaderRegistry.getModel(MODEL_NORTH);
        IBakedModel bakedModelNorth = subComponent.bake(state, format, bakedTextureGetter);

        subComponent = ModelLoaderRegistry.getModel(MODEL_SOUTH);
        IBakedModel bakedModelSouth = subComponent.bake(state, format, bakedTextureGetter);

        return new CompositeModel(bakedModelCore, bakedModelUp, bakedModelDown,
                                         bakedModelWest, bakedModelEast, bakedModelNorth, bakedModelSouth);
      } catch (Exception exception) {
        System.err.println("WebModel.bake() failed due to exception:" + exception);
        return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
      }
    }

    // Our custom loaded doesn't need a default state, but if we return null Forge gets upset
    // The interface now provides a sensible default so just use that
/*    @Override
    public IModelState getDefaultState() {
      return null;
    }
*/
}
