//package minecraftbyexample.mbe60_network_messages;
//
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.particles.ParticleTypes;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.LogicalSidedProvider;
//import net.minecraftforge.fml.network.NetworkEvent;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.Optional;
//import java.util.Random;
//import java.util.function.Supplier;
//
///**
// * The MessageHandlerOnClient is used to process the network message once it has arrived on the Client side.
// * WARNING!  The MessageHandler runs in its own thread.  This means that if your onMessage code
// * calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce.
// * Your onMessage handler should create a task which is later executed by the client or server thread as
// * appropriate - see below.
// * User: The Grey Ghost
// * Date: 15/01/2015
// */
//public class MessageHandlerOnClient {
//
//  /**
//   * Called when a message is received of the appropriate type.
//   * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD
//   */
//  public static void onMessageReceived(final TargetEffectMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier) {
//    NetworkEvent.Context ctx = ctxSupplier.get();
//    LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
//    ctx.setPacketHandled(true);
//
//    if (sideReceived != LogicalSide.CLIENT) {
//      LOGGER.warn("TargetEffectMessageToClient received on wrong side:" + ctx.getDirection().getReceptionSide());
//      return;
//    }
//    if (!message.isMessageValid()) {
//      LOGGER.warn("TargetEffectMessageToClient was invalid" + message.toString());
//      return;
//    }
//    // we know for sure that this handler is only used on the client side, so it is ok to assume
//    //  that the ctx handler is a client, and that Minecraft exists.
//    // Packets received on the server side must be handled differently!  See MessageHandlerOnServer
//
//    Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
//    if (!clientWorld.isPresent()) {
//      LOGGER.warn("TargetEffectMessageToClient context could not provide a ClientWorld.");
//      return;
//    }
//
//    // This code creates a new task which will be executed by the client during the next tick
//    //  In this case, the task is to call messageHandlerOnClient.processMessage(worldclient, message)
//    ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
//  }
//
//  // This message is called from the Client thread.
//  //   It spawns a number of Particle particles at the target location within a short range around the target location
//  private static void processMessage(ClientWorld worldClient, TargetEffectMessageToClient message)
//  {
//    Random random = new Random();
//    final int NUMBER_OF_PARTICLES = 100;
//    final double HORIZONTAL_SPREAD = 1.5;
//    for (int i = 0; i < NUMBER_OF_PARTICLES; ++i) {
//      Vector3d targetCoordinates = message.getTargetCoordinates();
//      double spawnXpos = targetCoordinates.x + (2*random.nextDouble() - 1) * HORIZONTAL_SPREAD;
//      double spawnYpos = targetCoordinates.y;
//      double spawnZpos = targetCoordinates.z + (2*random.nextDouble() - 1) * HORIZONTAL_SPREAD;
//      worldClient.addParticle(ParticleTypes.INSTANT_EFFECT, spawnXpos, spawnYpos, spawnZpos, 0, 0, 0);
//    }
//    return;
//  }
//
//  public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
//    return StartupCommon.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
//  }
//
//  private static final Logger LOGGER = LogManager.getLogger();
//}
