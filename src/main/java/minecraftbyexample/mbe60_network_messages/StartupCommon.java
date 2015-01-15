package minecraftbyexample.mbe60_network_messages;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
public class StartupCommon
{
  public static ItemAirStrike itemAirStrike;                  // this holds the unique instance of your block
  public static SimpleNetworkWrapper simpleNetworkWrapper;

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemAirStrike = (ItemAirStrike)(new ItemAirStrike().setUnlocalizedName("mbe60_item_airstrike"));
    GameRegistry.registerItem(itemAirStrike, "mbe60_item_airstrike");

    // You MUST register the message in Common, not in ClientOnly.
    final byte AIRSTRIKE_MESSAGE_ID = 35;  // just an arbitrary value.  It helps detect errors if you don't use zero!
    simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MBEchannel");
    simpleNetworkWrapper.registerMessage(ServerMessageHandler.class, AirstrikeMessageToServer.class, AIRSTRIKE_MESSAGE_ID, Side.SERVER);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
