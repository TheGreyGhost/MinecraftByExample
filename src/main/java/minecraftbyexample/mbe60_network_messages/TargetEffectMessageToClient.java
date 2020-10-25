package minecraftbyexample.mbe60_network_messages;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This Network Message is sent from the server to all clients in the same dimension, to tell them to draw a "target indicator" at the target point
 * Typical usage:
 * PREQUISITES:
 *   have previously setup SimpleChannel, registered the message class and the handler
 *
 * 1) User creates a TargetEffectMessageToClient(targetCoordinates)
 * 2) simpleChannel.sendToDimension(targetEffectMessageToClient);
 * 3) Forge network code calls targetEffectMessageToClient.encode() to copy the message member variables to a PacketBuffer, ready for sending
 * ... bytes are sent over the network and arrive at the client....
 * 4) Forge network code calls targetEffectMessageToClient.decode() to recreate the targetEffectMessageToClient instance by reading
 *     from the PacketBuffer into the member variables
 * 5) the handler.onMessage(targetEffectMessageToClient) is called to process the message
 *
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class TargetEffectMessageToClient
{
  public TargetEffectMessageToClient(Vector3d i_targetCoordinates)
  {
    targetCoordinates = i_targetCoordinates;
    messageIsValid = true;
  }

  public Vector3d getTargetCoordinates() {
    return targetCoordinates;
  }

  public boolean isMessageValid() {
    return messageIsValid;
  }

  // for use by the message handler only.
  public TargetEffectMessageToClient()
  {
    messageIsValid = false;
  }

  /**
   * Called by the network code once it has received the message bytes over the network.
   * Used to read the ByteBuf contents into your member variables
   * @param buf
   */
  public static TargetEffectMessageToClient decode(PacketBuffer buf)
  {
    TargetEffectMessageToClient retval = new TargetEffectMessageToClient();
    try {
      double x = buf.readDouble();
      double y = buf.readDouble();
      double z = buf.readDouble();
      retval.targetCoordinates = new Vector3d(x, y, z);

      // these methods may also be of use for your code:
      // for Itemstacks - ByteBufUtils.readItemStack()
      // for NBT tags ByteBufUtils.readTag();
      // for Strings: ByteBufUtils.readUTF8String();
      // NB that PacketBuffer is a derived class of ByteBuf

    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
      LOGGER.warn("Exception while reading TargetEffectMessageToClient: " + e);
      return retval;
    }
    retval.messageIsValid = true;
    return retval;
  }

  /**
   * Called by the network code.
   * Used to write the contents of your message member variables into the ByteBuf, ready for transmission over the network.
   * @param buf
   */
  public void encode(PacketBuffer buf)
  {
    if (!messageIsValid) return;
    buf.writeDouble(targetCoordinates.x);
    buf.writeDouble(targetCoordinates.y);
    buf.writeDouble(targetCoordinates.z);

    // these methods may also be of use for your code:
    // for Itemstacks - ByteBufUtils.writeItemStack()
    // for NBT tags ByteBufUtils.writeTag();
    // for Strings: ByteBufUtils.writeUTF8String();
//    System.out.println("TargetEffectMessageToClient:toBytes length=" + buf.readableBytes());  // debugging only
  }

  @Override
  public String toString()
  {
    return "TargetEffectMessageToClient[targetCoordinates=" + String.valueOf(targetCoordinates) + "]";
  }

  private Vector3d targetCoordinates;
  private boolean messageIsValid;

  private static final Logger LOGGER = LogManager.getLogger();
}
