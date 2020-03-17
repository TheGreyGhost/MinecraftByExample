package minecraftbyexample.mbe60_network_messages;


import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

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
  public static SimpleChannel simpleChannel;    // used to transmit your network messages

  public static final byte AIRSTRIKE_MESSAGE_ID = 35;      // a unique ID for this message type.  It helps detect errors if you don't use zero!
  public static final byte TARGET_EFFECT_MESSAGE_ID = 63;

  public static final String MESSAGE_PROTOCOL_VERSION = "1.0";  // a version number for the protocol you're using.  Can be used to maintain backward
                                                                // compatibility.  But to be honest you'll probably never need it for anything useful...

  public static final ResourceLocation simpleChannelRL = new ResourceLocation("minecraftbyexample", "mbechannel");

  public static void preInitCommon()
  {
    // each instance of your item should have two names:
    // 1) a registry name that is used to uniquely identify this item.  Should be unique within your mod.  use lower case.
    // 2) an 'unlocalised name' that is used to retrieve the text name of your item in the player's language.  For example-
    //    the unlocalised name might be "water", which is printed on the user's screen as "Wasser" in German or
    //    "aqua" in Italian.
    itemAirStrike = (ItemAirStrike)(new ItemAirStrike().setUnlocalizedName("mbe60_item_airstrike_unlocalised_name"));
    itemAirStrike.setRegistryName("mbe60_item_airstrike_registry_name");
    ForgeRegistries.ITEMS.register(itemAirStrike);

    // Registering your message handlers and ID has some traps for the unwary.  The call to registerMessage has two functions:
    // 1) To register a message handler class for each side which receives a message
    // 2) To register a unique ID for the message, on at least one side, so that the sender of the message knows what
    //    ID to use when sending the message.
    // For messages which are sent by the server to the client, this leads to a problem:
    // 1) You can't register the client handler class in common proxy, because commonproxy is called by dedicated server,
    //    which may cause a crash if your client handler has any client-side-only classes in it.
    // 2) You must call registerMessage on the dedicated server at least once, in order to tell it what ID to use for the
    //    message sent to the client.
    // You can solve this in a number of ways, for example
    // 1) Register the client messagehandler in clientOnly proxy, and a dummy client messagehandler in dedicatedServerProxy
    // or
    // 2) Make your client messagehandler side-independent (make calls to other proxy methods)
    // or
    // 3) Register client messagehandler in clientOnly proxy, and a dummy server messagehandler in common proxy.
    // I have chosen the third option because it fits the MBE structure better.  It's not the only way.


    simpleChannel = NetworkRegistry.newSimpleChannel(simpleChannelRL, () -> MESSAGE_PROTOCOL_VERSION,
            MessageHandlerOnClient::isThisProtocolAcceptedByClient,
            MessageHandlerOnServer::isThisProtocolAcceptedByServer);

    simpleChannel.registerMessage(AIRSTRIKE_MESSAGE_ID, AirstrikeMessageToServer.class,
            AirstrikeMessageToServer::encode, AirstrikeMessageToServer::decode,
            MessageHandlerOnServer::onMessage,
            Optional.of(PLAY_TO_SERVER));

    simpleChannel.registerMessage(TARGET_EFFECT_MESSAGE_ID, TargetEffectMessageToClient.class,
            TargetEffectMessageToClient::encode, TargetEffectMessageToClient::decode,
            MessageHandlerOnClient::onMessage,
            Optional.of(PLAY_TO_CLIENT));

    // it is possible to register the same message class on both sides if you want, eg,
    // simpleNetworkWrapper.registerMessage(ServerMessageHandler.class, AirstrikeMessageBothDirections.class, AIRSTRIKE_MESSAGE_ID, Side.SERVER);
    // simpleNetworkWrapper.registerMessage(ClientMessageHandler.class, AirstrikeMessageBothDirections.class, AIRSTRIKE_MESSAGE_ID, Side.CLIENT);

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
