package minecraftbyexample.mbe05_block_smartblockmodel2;

import minecraftbyexample.mbe04_block_smartblockmodel1.CamouflageISmartBlockModelFactory;
import minecraftbyexample.mbe04_block_smartblockmodel1.ModelBakeEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
    // We need to tell Forge how to map our Block3DWebs's IBlockState to a ModelResourceLocation.
    // For example, the BlockStone granite variant has a BlockStateMap entry that looks like
    //   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
    // For the 3DWeb block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
    //   which is done using the anonymous class below
    StateMapperBase ignoreState = new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
        return new ModelResourceLocation("minecraftbyexample:mbe05_block_3d_web");
      }
    };
    ModelLoader.setCustomStateMapper(StartupCommon.block3DWeb, ignoreState);

    ModelLoaderRegistry.registerLoader(new ModelLoader3DWeb());
  }

  public static void initClientOnly()
  {
    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.  This will probably change in future.
    // It must be done in the init phase, not preinit, and must be done on client only.
    Item itemBlockCamouflage = GameRegistry.findItem("minecraftbyexample", "mbe05_block_3d_web");
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe05_block_3d_web", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockCamouflage, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void postInitClientOnly()
  {
  }
}
