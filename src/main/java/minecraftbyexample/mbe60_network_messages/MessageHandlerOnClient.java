package minecraftbyexample.mbe60_network_messages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

/**
 * The MessageHandlerOnClient is used to process the network message once it has arrived on the Server side.
 * WARNING!  From 1.8 onwards the MessageHandler now runs in its own thread.  This means that if your onMessage code
 * calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce.
 * Your onMessage handler should create a task which is later executed by the client or server thread as
 * appropriate - see below.
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class MessageHandlerOnClient implements IMessageHandler<TargetEffectMessageToClient, IMessage>
{
  /**
   * Called when a message is received of the appropriate type.
   * CALLED BY THE NETWORK THREAD, NOT THE CLIENT THREAD
   * @param message The message
   */
  public IMessage onMessage(final TargetEffectMessageToClient message, MessageContext ctx) {
    if (ctx.side != Side.CLIENT) {
      System.err.println("TargetEffectMessageToClient received on wrong side:" + ctx.side);
      return null;
    }
    if (!message.isMessageValid()) {
      System.err.println("TargetEffectMessageToClient was invalid" + message.toString());
      return null;
    }

    // we know for sure that this handler is only used on the client side, so it is ok to assume
    //  that the ctx handler is a client, and that Minecraft exists.
    // Packets received on the server side must be handled differently!  See MessageHandlerOnServer

    // This code creates a new task which will be executed by the client during the next tick,
    //  for example see Minecraft.runGameLoop() , just under section
    //    this.mcProfiler.startSection("scheduledExecutables");
    //  In this case, the task is to call messageHandlerOnClient.processMessage(worldclient, message)
    Minecraft minecraft = Minecraft.getMinecraft();
    final WorldClient worldClient = minecraft.world;
    minecraft.addScheduledTask(new Runnable()
    {
      public void run() {
        processMessage(worldClient, message);
      }
    });

    return null;
  }

  // This message is called from the Client thread.
  //   It spawns a number of EntityFX particles at the target location within a short range around the target location
  void processMessage(WorldClient worldClient, TargetEffectMessageToClient message)
  {
    Random random = new Random();
    final int NUMBER_OF_PARTICLES = 100;
    final double HORIZONTAL_SPREAD = 1.5;
    for (int i = 0; i < NUMBER_OF_PARTICLES; ++i) {
      Vec3d targetCoordinates = message.getTargetCoordinates();
      double spawnXpos = targetCoordinates.xCoord + (2*random.nextDouble() - 1) * HORIZONTAL_SPREAD;
      double spawnYpos = targetCoordinates.yCoord;
      double spawnZpos = targetCoordinates.zCoord + (2*random.nextDouble() - 1) * HORIZONTAL_SPREAD;
      worldClient.spawnParticle(EnumParticleTypes.SPELL_INSTANT, spawnXpos, spawnYpos, spawnZpos, 0, 0, 0);
    }

    return;
  }
}
