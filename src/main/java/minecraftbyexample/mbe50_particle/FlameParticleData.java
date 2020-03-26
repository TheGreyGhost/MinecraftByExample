package minecraftbyexample.mbe50_particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Locale;

/**
 * Created by TGG on 25/03/2020.
 *
 * The particle has two pieces of information which are used to customise it:
 *
 * 1) The colour (tint) which is used to change the hue of the particle
 * 2) The diameter of the particle
 *
 * This class is used to
 * 1) store this information, and
 * 2) transmit it between server and client (write and read methods), and
 * 3) parse it from a command string i.e. the /particle params
 */
public class FlameParticleData implements IParticleData {

  public FlameParticleData(Color tint, double diameter) {
    this.tint = tint;
    this.diameter = constrainDiameterToValidRange(diameter);
  }

  public Color getTint() {
    return tint;
  }

  public double getDiameter() {
    return diameter;
  }

  @Nonnull
  @Override
  public ParticleType<FlameParticleData> getType() {
    return StartupCommon.flameParticleType;
  }

  // write the particle information to a PacketBuffer, ready for transmission to a client
  @Override
  public void write(PacketBuffer buf) {
    buf.writeInt(tint.getRed());
    buf.writeInt(tint.getGreen());
    buf.writeInt(tint.getBlue());
    buf.writeDouble(diameter);
  }

  // used for debugging I think.
  @Nonnull
  @Override
  public String getParameters() {
    return String.format(Locale.ROOT, "%s %.2f %i %i %i",
            this.getType().getRegistryName(), diameter, tint.getRed(), tint.getGreen(), tint.getBlue());
  }

  private static double constrainDiameterToValidRange(double diameter) {
    final double MIN_DIAMETER = 0.05;
    final double MAX_DIAMETER = 1.0;
    return MathHelper.clamp(diameter, MIN_DIAMETER, MAX_DIAMETER);
  }


  private Color tint;
  private double diameter;

  public static final IDeserializer<FlameParticleData> DESERIALIZER = new IDeserializer<FlameParticleData>() {

    // parse the parameters for this particle from a /particle command
    @Nonnull
    @Override
    public FlameParticleData deserialize(@Nonnull ParticleType<FlameParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
      reader.expect(' ');
      double diameter = constrainDiameterToValidRange(reader.readDouble());

      final int MIN_COLOUR = 0;
      final int MAX_COLOUR = 255;
      reader.expect(' ');
      int red = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
      reader.expect(' ');
      int green = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
      reader.expect(' ');
      int blue = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
      Color color = new Color(red, green, blue);

      return new FlameParticleData(color, diameter);
    }

    // read the particle information from a PacketBuffer after the client has received it from the server
    @Override
    public FlameParticleData read(@Nonnull ParticleType<FlameParticleData> type, PacketBuffer buf) {
      // warning! never trust the data read in from a packet buffer.

      final int MIN_COLOUR = 0;
      final int MAX_COLOUR = 255;
      int red = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR);
      int green = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR);
      int blue = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR);
      Color color = new Color(red, green, blue);

      double diameter = constrainDiameterToValidRange(buf.readDouble());

      return new FlameParticleData(color, diameter);
    }
  };
}
