package minecraftbyexample.mbe10_item_simple;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  No client-only events are needed for this example
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
//  public static void preInitClientOnly()
//  {
//    // required in order for the renderer to know how to render your item.
//    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe10_item_simple", "inventory");
//    final int DEFAULT_ITEM_SUBTYPE = 0;
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
//  }
//
//  public static void initClientOnly()
//  {
//  }
//
//  public static void postInitClientOnly()
//  {
//  }
  @SubscribeEvent
  public void onClientSetupEvent(FMLClientSetupEvent event) {
    // not actually required for this example....
  }
}
