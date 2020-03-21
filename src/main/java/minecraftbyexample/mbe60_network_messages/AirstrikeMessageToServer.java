package minecraftbyexample.mbe60_network_messages;

import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * This Network Message is sent from the client to the server, to tell it to spawn projectiles at a particular location.
 * Typical usage:
 * PREQUISITES:
 *   have previously setup SimpleChannel, registered the message class and the handler
 *
 * 1) User creates an AirStrikeMessageToServer(projectile, targetCoordinates)
 * 2) simpleChannel.sendToServer(airstrikeMessageToServer);
 * 3) Forge network code calls airstrikeMessageToServer.encode() to copy the message member variables to a PacketBuffer, ready for sending
 * ... bytes are sent over the network and arrive at the server....
 * 4) Forge network code calls airstrikeMessageToServer.decode() to recreate the airstrickeMessageToServer instance by reading
 *    from the PacketBuffer into the member variables
 * 6) the handler.onMessage(airStrikeMessageToServer) is called to process the message
 *
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class AirstrikeMessageToServer
{
  public AirstrikeMessageToServer(Projectile i_projectile, Vec3d i_targetCoordinates)
  {
    projectile = i_projectile;
    targetCoordinates = i_targetCoordinates;
    messageIsValid = true;
  }

  public Vec3d getTargetCoordinates() {
    return targetCoordinates;
  }

  public Projectile getProjectile() {
    return projectile;
  }

  public boolean isMessageValid() {
    return messageIsValid;
  }

  // not a valid way to construct the message
  private AirstrikeMessageToServer()
  {
    messageIsValid = false;
  }

  /**
   * Called by the network code once it has received the message bytes over the network.
   * Used to read the PacketBuffer contents into your member variables
   * @param buf
   */
  public static AirstrikeMessageToServer decode(PacketBuffer buf)
  {
    AirstrikeMessageToServer retval = new AirstrikeMessageToServer();
    try {
      retval.projectile = Projectile.fromPacketBuffer(buf);
      double x = buf.readDouble();
      double y = buf.readDouble();
      double z = buf.readDouble();
      retval.targetCoordinates = new Vec3d(x, y, z);

      // these methods may also be of use for your code:
      // for Itemstacks - ByteBufUtils.readItemStack()
      // for NBT tags ByteBufUtils.readTag();
      // for Strings: ByteBufUtils.readUTF8String();
      // NB that PacketBuffer is a derived class of ByteBuf
    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
      LOGGER.warn("Exception while reading AirStrikeMessageToServer: " + e);
      return retval;
    }
    retval.messageIsValid = true;
    return retval;
  }

  /**
   * Called by the network code.
   * Used to write the contents of your message member variables into the PacketBuffer, ready for transmission over the network.
   * @param buf
   */
  public void encode(PacketBuffer buf)
  {
    if (!messageIsValid) return;
    projectile.toPacketBuffer(buf);
    buf.writeDouble(targetCoordinates.x);
    buf.writeDouble(targetCoordinates.y);
    buf.writeDouble(targetCoordinates.z);

    // these methods may also be of use for your code:
    // for Itemstacks - ByteBufUtils.writeItemStack()
    // for NBT tags ByteBufUtils.writeTag();
    // for Strings: ByteBufUtils.writeUTF8String();
    // NB that PacketBuffer is a derived class of ByteBuf
  }

  public enum Projectile {
    PIG(1, "PIG", EntityType.PIG),
    SNOWBALL(2, "SNOWBALL", EntityType.SNOWBALL),
    TNT(3, "TNT", EntityType.TNT),
    SNOWMAN(4, "SNOWMAN", EntityType.SNOW_GOLEM),
    EGG(5, "EGG", EntityType.EGG),
    FIREBALL(6, "FIREBALL", EntityType.FIREBALL);

    public void toPacketBuffer(PacketBuffer buffer) {
      buffer.writeByte(projectileID);
    }

    public EntityType getEntityType() {return entityType;}

    public static Projectile fromPacketBuffer(PacketBuffer buffer) throws IllegalArgumentException {
      byte ID = buffer.readByte();
      for (Projectile projectile : Projectile.values()) {
        if (ID == projectile.projectileID) return projectile;
      }
      throw new IllegalArgumentException("Unrecognised Projectile ID:" + ID);
    }

    public static Projectile getRandom() {
      Random random = new Random();
      AirstrikeMessageToServer.Projectile [] choices = AirstrikeMessageToServer.Projectile.values();
      return choices[random.nextInt(choices.length)];
    }

    @Override
    public String toString() {return name;}

    private Projectile(int i_projectileID, String i_name, EntityType i_entityType) {
      projectileID = (byte)i_projectileID;
      name = i_name;
      entityType = i_entityType;
    }

    private final byte projectileID;
    private final String name;
    private final EntityType entityType;
  }

  @Override
  public String toString()  {
    return "AirstrikeMessageToServer[projectile=" + String.valueOf(projectile)
                                                  + ", targetCoordinates=" + String.valueOf(targetCoordinates) + "]";
  }

  private Vec3d targetCoordinates;
  private Projectile projectile;
  private boolean messageIsValid;

  private static final Logger LOGGER = LogManager.getLogger();
}
