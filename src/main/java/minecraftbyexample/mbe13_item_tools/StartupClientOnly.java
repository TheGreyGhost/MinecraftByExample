package minecraftbyexample.mbe13_item_tools;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

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
    ModelResourceLocation itemBlockModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe13_item_tools_block", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockToolTest, DEFAULT_ITEM_SUBTYPE, itemBlockModelResourceLocation);

    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe13_item_tools_item", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemToolsTest, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
