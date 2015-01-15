package minecraftbyexample.mbe60_network_messages;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class ServerMessageHandler implements IMessageHandler<AirstrikeMessageToServer, IMessage>
{
  /**
   * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
   * is needed.
   *
   * @param message The message
   * @return an optional return message
   */
  public IMessage onMessage(AirstrikeMessageToServer message, MessageContext ctx)
  {
    MinecraftServer.getServer().addScheduledTask(new Runnable()


            IThreadListener thread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
    if (thread.isCallingFromMinecraftThread())
    {
//      ServerMessageHandler.this process(msg);
    }
    else
    {
//      thread.addScheduledTask(new Runnable()
//      {
//        public void run()
//        {
//          this.onMessage(msg);
//        }
//      });
    }

    if (ctx.side != Side.SERVER) {
      System.err.println("AirstrikeMessageToServer received on wrong side:" + ctx.side);
      return null;
    }
    if (!message.isMessageValid()) {
      System.err.println("AirstrikeMessageToServer was invalid" + message.toString());
      return null;
    }
    EntityPlayerMP entityPlayerMP = ctx.getServerHandler().playerEntity;
    if (entityPlayerMP == null) {
      System.err.println("EntityPlayerMP was null when AirstrikeMessageToServer was received");
      return null;
    }

    Random random = new Random();
    final int MAX_NUMBER_OF_PROJECTILES = 8;
    final int MIN_NUMBER_OF_PROJECTILES = 2;
    int numberOfProjectiles = MIN_NUMBER_OF_PROJECTILES + random.nextInt(MAX_NUMBER_OF_PROJECTILES - MIN_NUMBER_OF_PROJECTILES + 1);
    for (int i = 0; i < numberOfProjectiles; ++i) {
      World world = entityPlayerMP.worldObj;
      Entity entity;
      switch (message.getProjectile()) {
        case PIG: {
          entity = new EntityPig(world);
          break;
        }
        case SNOWBALL: {
          entity = new EntitySnowball(world);
          break;
        }
        case TNT: {
          entity = new EntityTNTPrimed(world);
          break;
        }
        default: {
          System.err.println("Invalid projectile type in ServerMessageHandler:" + String.valueOf(message.getProjectile()));
          return null;
        }
      }

      final double MAX_HORIZONTAL_SPREAD = 4.0;
      final double MAX_VERTICAL_SPREAD = 10.0;
      final double RELEASE_HEIGHT_ABOVE_TARGET = 40;
      double xOffset = (random.nextDouble() * 2 - 1) * MAX_HORIZONTAL_SPREAD;
      double zOffset = (random.nextDouble() * 2 - 1) * MAX_HORIZONTAL_SPREAD;
      double yOffset = RELEASE_HEIGHT_ABOVE_TARGET + (random.nextDouble() * 2 - 1) * MAX_VERTICAL_SPREAD;
      Vec3 releasePoint = message.getTargetCoordinates().addVector(xOffset, yOffset, zOffset);
      float yaw = random.nextFloat() * 360;
      float pitch = random.nextFloat() * 360;
      entity.setLocationAndAngles(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, yaw, pitch);
    }

    return null;
  }
}
