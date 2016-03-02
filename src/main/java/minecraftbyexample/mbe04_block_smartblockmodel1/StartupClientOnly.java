package minecraftbyexample.mbe04_block_smartblockmodel1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    // We need to tell Forge how to map our BlockCamouflage's IBlockState to a ModelResourceLocation.
    // For example, the BlockStone granite variant has a BlockStateMap entry that looks like
    //   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
    // For the camouflage block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
    //   which is done using the anonymous class below
    StateMapperBase ignoreState = new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
        return CamouflageISmartBlockModelFactory.modelResourceLocation;
      }
    };
    ModelLoader.setCustomStateMapper(StartupCommon.blockCamouflage, ignoreState);

    // ModelBakeEvent will be used to add our ISmartBlockModel to the ModelManager's registry (the
    //  registry used to map all the ModelResourceLocations to IBlockModels).  For the stone example there is a map from
    // ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
    // For the camouflage block, it will map from
    // CamouflageISmartBlockModelFactory.modelResourceLocation to our CamouflageISmartBlockModelFactory instance
    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.instance);

    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.
    // It must be done on client only, and must be done after the block has been created in Common.preinit().
    Item itemBlockCamouflage = GameRegistry.findItem("minecraftbyexample", "mbe04_block_camouflage");
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe04_block_camouflage", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(itemBlockCamouflage, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {

  }

  public static void postInitClientOnly()
  {
  }
}
