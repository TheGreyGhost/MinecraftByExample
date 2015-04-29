package minecraftbyexample.mbe15_item_smartitemmodel.mbe04_block_smartblockmodel1;

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

    // ModelBakeEvent will be used to add our ISmartItemModel to the ModelManager's registry (the
    //  registry used to map all the ModelResourceLocations to IBlockModels).  For the stone example there is a map from
    // ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
    // For the chessboard item, it will map from
    // "minecraftbyexample: to our SmartChessboardModel instance
    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.instance);
  }

  public static void initClientOnly()
  {
    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.  This will probably change in future.
    // It must be done in the init phase, not preinit, and must be done on client only.
    Item itemChessboard = GameRegistry.findItem("minecraftbyexample", "mbe15_item_chessboard");
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe15_item_", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemChessboard, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void postInitClientOnly()
  {
  }
}
