package minecraftbyexample.mbe08_creative_tab;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

/**
 * @author Nephroid
 *
 * User: Nephroid
 * Date: December 26, 2014
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
    // required in order for the renderer to know how to render your item.
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe08_creative_tab_item", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.testItem, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe08_creative_tab_block", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.testItemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);  }

  public static void initClientOnly()
  {

  }

  public static void postInitClientOnly()
  {
  }
}
