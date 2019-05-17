package minecraftbyexample.mbe10_tool_simple;

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
    ModelResourceLocation itemBlockModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe10_tool_simple", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.toolSimple, DEFAULT_ITEM_SUBTYPE, itemBlockModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
