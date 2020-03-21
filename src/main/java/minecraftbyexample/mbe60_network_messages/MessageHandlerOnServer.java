package minecraftbyexample.mbe60_network_messages;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.security.krb5.internal.crypto.EType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

import static net.minecraft.entity.EntityType.FIREBALL;

/**
 * The MessageHandlerOnServer is used to process the network message once it has arrived on the Server side.
 * WARNING!  The MessageHandler  runs in its own thread.  This means that if your onMessage code
 * calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce.
 * Your onMessage handler should create a task which is later executed by the client or server thread as
 * appropriate - see below.
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class MessageHandlerOnServer {

  /**
   * Called when a message is received of the appropriate type.
   * CALLED BY THE NETWORK THREAD, NOT THE SERVER THREAD
   * @param message The message
   */
  public static void onMessageReceived(final AirstrikeMessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
    NetworkEvent.Context ctx = ctxSupplier.get();
    LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
    ctx.setPacketHandled(true);

    if (sideReceived != LogicalSide.SERVER) {
      LOGGER.warn("AirstrikeMessageToServer received on wrong side:" + ctx.getDirection().getReceptionSide());
      return;
    }
    if (!message.isMessageValid()) {
      LOGGER.warn("AirstrikeMessageToServer was invalid" + message.toString());
      return;
    }

    // we know for sure that this handler is only used on the server side, so it is ok to assume
    //  that the ctx handler is a serverhandler, and that ServerPlayerEntity exists
    // Packets received on the client side must be handled differently!  See MessageHandlerOnClient

    final ServerPlayerEntity sendingPlayer = ctx.getSender();
    if (sendingPlayer == null) {
      LOGGER.warn("EntityPlayerMP was null when AirstrikeMessageToServer was received");
    }

    // This code creates a new task which will be executed by the server during the next tick,
    //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
    ctx.enqueueWork(() -> processMessage(message, sendingPlayer));
  }

  // This message is called from the Server thread.
  //   It spawns a random number of the given projectile at a position above the target location
  static void processMessage(AirstrikeMessageToServer message, ServerPlayerEntity sendingPlayer)
  {
    // 1) First send a message to all other clients who are in the same dimension, to tell them to render a "target"
    //      effect on the ground
    // There are a number of other PacketDistributor types defined for other cases, for example
    // Sending to one player
    //    INSTANCE.send(PacketDistributor.PLAYER.with(playerMP), new MyMessage());
    //
    // Send to all players tracking this chunk
    //    INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(chunk), new MyMessage());
    //
    // Sending to all connected players
    //    INSTANCE.send(PacketDistributor.ALL.noArg(), new MyMessage());

    TargetEffectMessageToClient msg = new TargetEffectMessageToClient(message.getTargetCoordinates());   // must generate a fresh message for every player!
    DimensionType playerDimension = sendingPlayer.dimension;
    StartupCommon.simpleChannel.send(PacketDistributor.DIMENSION.with(() -> playerDimension), msg);

    // 2) Next: spawn the projectiles on the server
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
      Vec3d releasePoint = message.getTargetCoordinates().add(xOffset, yOffset, zOffset);

      EntityType entityType = message.getProjectile().getEntityType();

      CompoundNBT spawnNBT = null;
      ITextComponent customName = null;
      PlayerEntity spawningPlayer = null;
      BlockPos spawnLocation = new BlockPos(releasePoint);
      boolean SPAWN_ON_TOP_OF_GIVEN_BLOCK_LOCATION = false;  // not 100% sure of what this does...
      boolean SEARCH_DOWN_WHEN_PLACED_ON_TOP_OF_GIVEN_BLOCK_LOCATION = false; // not 100% sure of what this does...
      Entity spawnedEntity = entityType.spawn(world, spawnNBT, customName, spawningPlayer, spawnLocation,
              SpawnReason.SPAWN_EGG,
              SPAWN_ON_TOP_OF_GIVEN_BLOCK_LOCATION, SEARCH_DOWN_WHEN_PLACED_ON_TOP_OF_GIVEN_BLOCK_LOCATION);

      // special cases handled by switch() - clumsy method for purposes of simplicity only...
      switch (message.getProjectile()) {
        case FIREBALL: {
          FireballEntity fireballEntity = (FireballEntity)spawnedEntity;
          final double Y_ACCELERATION = -0.5;
          fireballEntity.accelerationX = 0.0;
          fireballEntity.accelerationY = Y_ACCELERATION;
          fireballEntity.accelerationZ = 0.0;
          break;
        }
        default: {
          break;
        }
      }


      // 3: Play a thunder sound using the server method (sends to all clients, so all clients hear it)
      final float VOLUME = 10000.0F;
      final float PITCH = 0.8F + random.nextFloat() * 0.2F;
      PlayerEntity playerCausingSound = null;
      world.playSound(playerCausingSound, releasePoint.x, releasePoint.y, releasePoint.z,
                      SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, VOLUME, PITCH);
    }
    return;
  }

  public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
    return StartupCommon.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
