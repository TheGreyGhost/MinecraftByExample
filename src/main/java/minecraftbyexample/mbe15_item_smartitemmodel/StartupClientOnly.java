package minecraftbyexample.mbe15_item_smartitemmodel;

import net.minecraft.client.Minecraft;
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
    //  registry used to map all the ModelResourceLocations to IBlockModels).
    // For the chessboard item, it will map from
    // "minecraftbyexample:mbe15_item_chessboard#inventory to our SmartChessboardModel instance
    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandlerMBE15.instance);

    // Minecraft knows to look for the item model based on the GameRegistry.registerItem.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.
    // It must be done on client only, and must be done after the item has been created in Common.preinit().
    Item itemChessboard = GameRegistry.findItem("minecraftbyexample", "mbe15_item_chessboard");
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(itemChessboard, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
