//package minecraftbyexample.mbe60_network_messages;
//
//
//import net.minecraft.item.Item;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.minecraftforge.fml.network.NetworkRegistry;
//import net.minecraftforge.fml.network.simple.SimpleChannel;
//
//import java.util.Optional;
//
//import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
//import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;
//
///**
// * User: The Grey Ghost
// * Date: 24/12/2014
// *  See MinecraftByExample class for more information
// */
//public class StartupCommon
//{
//  public static ItemAirStrike itemAirStrike;    // this holds the unique instance of your item
//  public static SimpleChannel simpleChannel;    // used to transmit your network messages
//
//  public static final byte AIRSTRIKE_MESSAGE_ID = 35;      // a unique ID for this message type.  It helps detect errors if you don't use zero!
//  public static final byte TARGET_EFFECT_MESSAGE_ID = 63;
//
//  public static final String MESSAGE_PROTOCOL_VERSION = "1.0";  // a version number for the protocol you're using.  Can be used to maintain backward
//                                                                // compatibility.  But to be honest you'll probably never need it for anything useful...
//
//  public static final ResourceLocation simpleChannelRL = new ResourceLocation("minecraftbyexample", "mbechannel");
//
//  @SubscribeEvent
//  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
//    itemAirStrike = new ItemAirStrike();
//    itemAirStrike.setRegistryName("mbe60_item_airstrike_registry_name");
//    itemRegisterEvent.getRegistry().register(itemAirStrike);
//  }
//
//    // Register a channel for your packets.  You can send multiple types of packets on the same channel.  Most mods will only ever
//    //  need one channel.
//    @SubscribeEvent
//    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
//
//    simpleChannel = NetworkRegistry.newSimpleChannel(simpleChannelRL, () -> MESSAGE_PROTOCOL_VERSION,
//            MessageHandlerOnClient::isThisProtocolAcceptedByClient,
//            MessageHandlerOnServer::isThisProtocolAcceptedByServer);
//
//    // Register the two different types of messages:
//    //  AirStrike, which is sent from the client to the server to say "call an air strike on {this location} that I just clicked on"
//    //  TargetEffect, which is sent from the server to all clients to say "someone called an air strike on {this location}, draw some particles there"
//
//    simpleChannel.registerMessage(AIRSTRIKE_MESSAGE_ID, AirstrikeMessageToServer.class,
//            AirstrikeMessageToServer::encode, AirstrikeMessageToServer::decode,
//            MessageHandlerOnServer::onMessageReceived,
//            Optional.of(PLAY_TO_SERVER));
//
//    simpleChannel.registerMessage(TARGET_EFFECT_MESSAGE_ID, TargetEffectMessageToClient.class,
//            TargetEffectMessageToClient::encode, TargetEffectMessageToClient::decode,
//            MessageHandlerOnClient::onMessageReceived,
//            Optional.of(PLAY_TO_CLIENT));
//
//    // it is possible to register the same message class and handler on both sides if you want, eg,
////    simpleChannel.registerMessage(AIRSTRIKE_MESSAGE_ID, AirstrikeMessageToServer.class,
////            AirstrikeMessageBothDirections::encode, AirstrikeMessageBothDirections::decode,
////            MessageHandlerOnBothSides::onMessage);
//    // I recommend that you don't do this because it can lead to crashes (and in particular dedicated server problems) if you aren't
//    //    very careful to keep the client-side and server-side code separate
//  }
//
//}
