package minecraftbyexample.mbe50_particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Created by TGG on 25/03/2020.
 */
public class ParticleDataFlame implements IParticleData {


  @Nonnull
  @Override
  public ParticleType<ParticleDataFlame> getType() {
    return StartupCommon.flameParticleType;
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeFloat(size);
    buf.writeFloat(r);
    buf.writeFloat(g);
    buf.writeFloat(b);
    buf.writeFloat(maxAgeMul);
    buf.writeBoolean(depthTest);
    buf.writeBoolean(noClip);
  }

  @Nonnull
  @Override
  public String getParameters() {
    return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s",
            this.getType().getRegistryName(), this.size, this.r, this.g, this.b, this.maxAgeMul, this.depthTest);
  }

  public static final IDeserializer<ParticleDataFlame> DESERIALIZER = new IDeserializer<ParticleDataFlame>() {
    @Nonnull
    @Override
    public ParticleDataFlame deserialize(@Nonnull ParticleType<ParticleDataFlame> type, @Nonnull StringReader reader) throws CommandSyntaxException {
      reader.expect(' ');
      float size = reader.readFloat();
      reader.expect(' ');
      float r = reader.readFloat();
      reader.expect(' ');
      float g = reader.readFloat();
      reader.expect(' ');
      float b = reader.readFloat();
      reader.expect(' ');
      float mam = reader.readFloat();
      boolean depth = true;
      if (reader.canRead()) {
        reader.expect(' ');
        depth = reader.readBoolean();
      }
      boolean noClip = false;
      if (reader.canRead()) {
        reader.expect(' ');
        depth = reader.readBoolean();
      }
      return new ParticleDataFlame(size, r, g, b, mam, depth, noClip);
    }

    @Override
    public ParticleDataFlame read(@Nonnull ParticleType<ParticleDataFlame> type, PacketBuffer buf) {
      return new ParticleDataFlame(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean());
    }
  };
}
