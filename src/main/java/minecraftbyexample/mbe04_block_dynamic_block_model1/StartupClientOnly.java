package minecraftbyexample.mbe04_block_dynamic_block_model1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class  StartupClientOnly
{
  public static void preInitClientOnly()
  {
    // We need to tell Forge how to map our BlockCamouflage's IBlockState to a ModelResourceLocation.
    // For example, the BlockStone granite variant has a BlockStateMap entry that looks like
    //   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
    // For the camouflage block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
    //   which is done using the anonymous class below.
    StateMapperBase ignoreState = new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
        return CamouflageBakedModel.variantTag;
      }
    };
    ModelLoader.setCustomStateMapper(StartupCommon.blockCamouflage, ignoreState);
    // NB If your block has multiple variants and you want vanilla to load a model for each variant, you don't need a
    //   custom state mapper.
    // You can see examples of vanilla custom state mappers in BlockModelShapes.registerAllBlocks()

    // ModelBakeEvent will be used to add our CamouflageBakedModel to the ModelManager's registry (the
    //  registry used to map all the ModelResourceLocations to IBlockModels).  For the stone example there is a map from
    // ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
    // For the camouflage block, it will map from
    // CamouflageBakedModel.modelResourceLocation to our CamouflageBakedModel instance
    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.instance);

    // This step is necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // It must be done on client only, and must be done after the block has been created in Common.preinit().
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe04_block_camouflage", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockCamouflage, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {

  }

  public static void postInitClientOnly()
  {
  }
}
