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
  public static ItemAirStrike itemAirStrike;                  // this holds the unique instance of your item
  public static SimpleNetworkWrapper simpleNetworkWrapper;    // used to transmit your network messages

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemAirStrike = (ItemAirStrike)(new ItemAirStrike().setUnlocalizedName("mbe60_item_airstrike"));
    GameRegistry.registerItem(itemAirStrike, "mbe60_item_airstrike");

    // You MUST register the messages in Common, not in ClientOnly.
    final byte AIRSTRIKE_MESSAGE_ID = 35;  // a unique ID for this message type.  It helps detect errors if you don't use zero!
    final byte TARGET_EFFECT_MESSAGE_ID = 63;

    simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MBEchannel");
    simpleNetworkWrapper.registerMessage(MessageHandlerOnServer.class, AirstrikeMessageToServer.class,
                                          AIRSTRIKE_MESSAGE_ID, Side.SERVER);
    simpleNetworkWrapper.registerMessage(MessageHandlerOnClient.class, TargetEffectMessageToClient.class,
                                          TARGET_EFFECT_MESSAGE_ID, Side.CLIENT);
    // it is possible to register the same message class on both sides if you want, eg,
    // simpleNetworkWrapper.registerMessage(ServerMessageHandler.class, AirstrikeMessageToServer.class, AIRSTRIKE_MESSAGE_ID, Side.SERVER);
    // simpleNetworkWrapper.registerMessage(ClientMessageHandler.class, AirstrikeMessageToServer.class, AIRSTRIKE_MESSAGE_ID, Side.CLIENT);
    // it is also possible to register the same message handler on both sides.  I recommend that you don't do this because it can lead to
    //  crashes (and in particular dedicated server problems) if you aren't very careful to keep the client-side and server-side code separate
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
