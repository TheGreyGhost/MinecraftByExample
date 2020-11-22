package minecraftbyexample.mbe04_block_dynamic_block_models;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class is used to customise the rendering of the camouflage block, based on the block it is copying.
 * It uses the IForgeBakedModel extension of IBakedModel to pass IModelData (blockstate to be copied) to the getQuads.
 * In this case, the getQuads just looks up the model for the copied blockstate, and returns its quads.
 */
public class CamouflageBakedModel implements IBakedModel {

  public CamouflageBakedModel(IBakedModel unCamouflagedModel)
  {
    modelWhenNotCamouflaged = unCamouflagedModel;
  }

  public static ModelProperty<Optional<BlockState>> COPIED_BLOCK = new ModelProperty<>();

  public static ModelDataMap getEmptyIModelData() {
    ModelDataMap.Builder builder = new ModelDataMap.Builder();
    builder.withInitial(COPIED_BLOCK, Optional.empty());
    ModelDataMap modelDataMap = builder.build();
    return modelDataMap;
  }

  /**
   * Forge's extension in place of IBakedModel::getQuads
   * It allows us to pass in some extra information which we can use to choose the appropriate quads to render
   * @param state
   * @param side
   * @param rand
   * @param extraData
   * @return
   */
  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
  {
    return getActualBakedModelFromIModelData(extraData).getQuads(state, side, rand);
  }

  @Override
  @Nonnull
  public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
  {
    Optional<BlockState> bestAdjacentBlock = BlockCamouflage.selectBestAdjacentBlock(world, pos);
    ModelDataMap modelDataMap = getEmptyIModelData();
    modelDataMap.setData(COPIED_BLOCK, bestAdjacentBlock);
    return modelDataMap;
  }

  @Override
  public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data)
  {
    return getActualBakedModelFromIModelData(data).getParticleTexture();
  }

  private IBakedModel getActualBakedModelFromIModelData(@Nonnull IModelData data) {
    IBakedModel retval = modelWhenNotCamouflaged;  // default
    if (!data.hasProperty(COPIED_BLOCK)) {
      if (!loggedError) {
        LOGGER.error("IModelData did not have expected property COPIED_BLOCK");
        loggedError = true;
      }
      return retval;
    }
    Optional<BlockState> copiedBlock = data.getData(COPIED_BLOCK);
    if (!copiedBlock.isPresent()) return retval;

    Minecraft mc = Minecraft.getInstance();
    BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
    retval = blockRendererDispatcher.getModelForState(copiedBlock.get());
    return retval;
  }

  private IBakedModel modelWhenNotCamouflaged;


  // ---- All these methods are required by the interface but we don't do anything special with them.

  @Override
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
    throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
  }

  // getTexture is used directly when player is inside the block.  The game will crash if you don't use something
  //   meaningful here.
  @Override
  public TextureAtlasSprite getParticleTexture() {
    return modelWhenNotCamouflaged.getParticleTexture();
  }


  // ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla or forge
    @Override
  public boolean isAmbientOcclusion()
  {
    return modelWhenNotCamouflaged.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d()
  {
    return modelWhenNotCamouflaged.isGui3d();
  }

  @Override
  public boolean isSideLit() {
    return modelWhenNotCamouflaged.isSideLit();  // related to item "diffuselighting"
  }

  @Override
  public boolean isBuiltInRenderer()
  {
    return modelWhenNotCamouflaged.isBuiltInRenderer();
  }

  @Override
  public ItemOverrideList getOverrides()
  {
    return modelWhenNotCamouflaged.getOverrides();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms()
  {
    return modelWhenNotCamouflaged.getItemCameraTransforms();
  }

  private static final Logger LOGGER = LogManager.getLogger();
  private static boolean loggedError = false; // prevent spamming console
}
