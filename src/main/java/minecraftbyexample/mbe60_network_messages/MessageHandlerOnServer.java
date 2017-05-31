package minecraftbyexample.mbe60_network_messages;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

/**
 * The MessageHandlerOnServer is used to process the network message once it has arrived on the Server side.
 * WARNING!  In 1.8 onwards the MessageHandler now runs in its own thread.  This means that if your onMessage code
 * calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce.
 * Your onMessage handler should create a task which is later executed by the client or server thread as
 * appropriate - see below.
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class MessageHandlerOnServer implements IMessageHandler<AirstrikeMessageToServer, IMessage>
{
  /**
   * Called when a message is received of the appropriate type.
   * CALLED BY THE NETWORK THREAD
   * @param message The message
   */
  public IMessage onMessage(final AirstrikeMessageToServer message, MessageContext ctx) {
    if (ctx.side != Side.SERVER) {
      System.err.println("AirstrikeMessageToServer received on wrong side:" + ctx.side);
      return null;
    }
    if (!message.isMessageValid()) {
      System.err.println("AirstrikeMessageToServer was invalid" + message.toString());
      return null;
    }

    // we know for sure that this handler is only used on the server side, so it is ok to assume
    //  that the ctx handler is a serverhandler, and that WorldServer exists.
    // Packets received on the client side must be handled differently!  See MessageHandlerOnClient

    final EntityPlayerMP sendingPlayer = ctx.getServerHandler().playerEntity;
    if (sendingPlayer == null) {
      System.err.println("EntityPlayerMP was null when AirstrikeMessageToServer was received");
      return null;
    }

    // This code creates a new task which will be executed by the server during the next tick,
    //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section
    //      this.theProfiler.startSection("jobs");
    //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
    final WorldServer playerWorldServer = sendingPlayer.getServerWorld();
    playerWorldServer.addScheduledTask(new Runnable() {
      public void run() {
        processMessage(message, sendingPlayer);
      }
    });

    return null;
  }

  // This message is called from the Server thread.
  //   It spawns a random number of the given projectile at a position above the target location
  void processMessage(AirstrikeMessageToServer message, EntityPlayerMP sendingPlayer)
  {
    // first send a message to all clients to render a "target" effect on the ground
//    StartupCommon.simpleNetworkWrapper.sendToDimension(msg, sendingPlayer.dimension);  // DO NOT USE sendToDimension, it is buggy
//    as of build 1419 - see https://github.com/MinecraftForge/MinecraftForge/issues/1908

    int dimension = sendingPlayer.dimension;
    MinecraftServer minecraftServer = sendingPlayer.mcServer;

    for (EntityPlayerMP player : minecraftServer.getPlayerList().getPlayers()) {
      TargetEffectMessageToClient msg = new TargetEffectMessageToClient(message.getTargetCoordinates());   // must generate a fresh message for every player!
      if (dimension == player.dimension) {
        StartupCommon.simpleNetworkWrapper.sendTo(msg, player);
      }
    }

    // spawn projectiles
    Random random = new Random();
    final int MAX_NUMBER_OF_PROJECTILES = 20;
    final int MIN_NUMBER_OF_PROJECTILES = 2;
    int numberOfProjectiles = MIN_NUMBER_OF_PROJECTILES + random.nextInt(MAX_NUMBER_OF_PROJECTILES - MIN_NUMBER_OF_PROJECTILES + 1);
    for (int i = 0; i < numberOfProjectiles; ++i) {
      World world = sendingPlayer.world;

      final double MAX_HORIZONTAL_SPREAD = 4.0;
      final double MAX_VERTICAL_SPREAD = 20.0;
      final double RELEASE_HEIGHT_ABOVE_TARGET = 40;
      double xOffset = (random.nextDouble() * 2 - 1) * MAX_HORIZONTAL_SPREAD;
      double zOffset = (random.nextDouble() * 2 - 1) * MAX_HORIZONTAL_SPREAD;
      double yOffset = RELEASE_HEIGHT_ABOVE_TARGET + (random.nextDouble() * 2 - 1) * MAX_VERTICAL_SPREAD;
      Vec3d releasePoint = message.getTargetCoordinates().addVector(xOffset, yOffset, zOffset);
      float yaw = random.nextFloat() * 360;
      float pitch = random.nextFloat() * 360;

      Entity entity;
      switch (message.getProjectile()) {
        case PIG: {
          entity = new EntityPig(world);
          entity.setLocationAndAngles(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, yaw, pitch);
          break;
        }
        case SNOWBALL: {
          entity = new EntitySnowball(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord);
          break;
        }
        case TNT: {
          entity = new EntityTNTPrimed(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, sendingPlayer);
          break;
        }
        case SNOWMAN: {
          entity = new EntitySnowman(world);
          entity.setLocationAndAngles(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, yaw, pitch);
          break;
        }
        case EGG: {
          entity = new EntityEgg(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord);
          break;
        }
        case FIREBALL: {
          final double Y_ACCELERATION = -0.5;
          // this method is now client-side only, so use another constructor and manually set the missing values as copied from the
          //   constructor
//          entity = new EntityLargeFireball(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, 0.0, Y_ACCELERATION, 0.0);

          EntityLargeFireball entityLargeFireball =  new EntityLargeFireball(world);
          entity = entityLargeFireball;
          entity.setLocationAndAngles(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, entity.rotationYaw, entity.rotationPitch);
          entity.setPosition(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord);
          entityLargeFireball.accelerationX = 0.0;
          entityLargeFireball.accelerationY = Y_ACCELERATION;
          entityLargeFireball.accelerationZ = 0.0;
          break;
        }
        default: {
          System.err.println("Invalid projectile type in ServerMessageHandler:" + String.valueOf(message.getProjectile()));
          return;
        }
      }

      world.spawnEntity(entity);
      final float VOLUME = 10000.0F;
      final float PITCH = 0.8F + random.nextFloat() * 0.2F;
      final boolean DISTANCE_DELAY_FALSE = false;
      world.playSound(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord,
                      SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, VOLUME, PITCH, DISTANCE_DELAY_FALSE);
    }

    return;
  }
}
