package minecraftbyexample.mbe05_block_dynamic_block_model2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

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
    // We need to tell Forge how to map our Block3DWebs's IBlockState to a ModelResourceLocation.
    // For example, the BlockStone granite variant has a BlockStateMap entry that looks like
    //   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
    // For the 3DWeb block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
    //   which is done using the anonymous class below
    StateMapperBase ignoreState = new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
        return new ModelResourceLocation("minecraftbyexample:mbe05_block_3d_web_statemapper_name");
      }
    };
    ModelLoader.setCustomStateMapper(StartupCommon.block3DWeb, ignoreState);

    ModelLoaderRegistry.registerLoader(new ModelLoader3DWeb());

    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.
    // It must be done on client only, and must be done after the block has been created in Common.preinit().

    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe05_block_3d_web", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlock3DWeb, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {

  }

  public static void postInitClientOnly()
  {
  }
}
