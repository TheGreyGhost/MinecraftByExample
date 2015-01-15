package minecraftbyexample.mbe60_network_messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class AirstrikeMessageToServer implements IMessage
{
  public AirstrikeMessageToServer(Projectile i_projectile, Vec3 i_targetCoordinates)
  {
    projectile = i_projectile;
    targetCoordinates = i_targetCoordinates;
    messageIsValid = true;
  }

  public Vec3 getTargetCoordinates() {
    return targetCoordinates;
  }

  public Projectile getProjectile() {
    return projectile;
  }

  public boolean isMessageValid() {
    return messageIsValid;
  }

  // for use by the message handler only.
  public AirstrikeMessageToServer()
  {
    messageIsValid = false;
  }

  /**
   * Convert from the supplied buffer into your specific message type
   * @param buf
   */
  @Override
  public void fromBytes(ByteBuf buf)
  {
    try {
      projectile = Projectile.fromBytes(buf);
      double x = buf.readDouble();
      double y = buf.readDouble();
      double z = buf.readDouble();
      targetCoordinates = new Vec3(x, y, z);

      // these may also be of use:
      // for Itemstacks - ByteBufUtils.readItemStack()
      // for NBT tags ByteBufUtils.readTag();
      // for Strings: ByteBufUtils.readUTF8String();

    } catch (IndexOutOfBoundsException ioe) {
      System.err.println("Exception while reading AirStrikeMessageToServer: " + ioe);
      return;
    }
    messageIsValid = true;
  }

  /**
   * Deconstruct your message into the supplied byte buffer
   * @param buf
   */
  @Override
  public void toBytes(ByteBuf buf)
  {
    if (!messageIsValid) return;
    projectile.toBytes(buf);
    buf.writeDouble(targetCoordinates.xCoord);
    buf.writeDouble(targetCoordinates.yCoord);
    buf.writeDouble(targetCoordinates.zCoord);

    // these may also be of use:
    // for Itemstacks - ByteBufUtils.writeItemStack()
    // for NBT tags ByteBufUtils.writeTag();
    // for Strings: ByteBufUtils.writeUTF8String();

  }

  public enum Projectile {
    PIG(1, "PIG"), SNOWBALL(2, "SNOWBALL"), TNT(3, "TNT");

    public void toBytes(ByteBuf buffer) {
      buffer.writeByte(projectileID);
    }

    public static Projectile fromBytes(ByteBuf buffer) {
      byte ID = buffer.readByte();
      for (Projectile projectile : Projectile.values()) {
        if (ID == projectile.projectileID) return projectile;
      }
      return null;
    }

    @Override
    public String toString() {return name;}

    private Projectile(int i_projectileID, String i_name) {
      projectileID = (byte)i_projectileID;
      name = i_name;
    }

    private final byte projectileID;
    private final String name;
  }

  @Override
  public String toString()
  {
    return "AirstrikeMessageToServer[projectile=" + String.valueOf(projectile)
                                                  + ", targetCoordinates=" + String.valueOf(targetCoordinates) + "]";
  }

  private Vec3 targetCoordinates;
  private Projectile projectile;
  private boolean messageIsValid;
}
