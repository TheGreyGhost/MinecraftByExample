package minecraftbyexample.mbe60_network_messages;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *

 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
//  public static void preInitClientOnly()
//  {
//    // Client-side message handler must be registered in clientproxy only, unless you are very careful to keep all
//    //   client-side-only code out of your client-side message handler.
//    //  See further discussion in StartupCommon.
//    StartupCommon.simpleNetworkWrapper.registerMessage(MessageHandlerOnClient.class, TargetEffectMessageToClient.class,
//                                                       StartupCommon.TARGET_EFFECT_MESSAGE_ID, Side.CLIENT);
//    // required in order for the renderer to know how to render your item.  Likely to change in the near future.
//    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe60_item_airstrike", "inventory");
//    final int DEFAULT_ITEM_SUBTYPE = 0;
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemAirStrike, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
//  }
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // not actually required for this example....
  }
}
