package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    // ModelBakeEvent will be used to add our ISmartItemModel to the ModelManager's registry (the
    //  registry used to map all the ModelResourceLocations to IBlockModels).
    // For the chessboard item, it will map from
    // "minecraftbyexample:mbe15_item_chessboard#inventory to our SmartChessboardModel instance
    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandlerMBE15.instance);

    // model to be used for rendering this item
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemChessBoard, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
