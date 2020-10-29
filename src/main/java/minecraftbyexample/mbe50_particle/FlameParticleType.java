package minecraftbyexample.mbe50_particle;

import com.mojang.serialization.Codec;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;

/**
 * Created by TGG on 25/03/2020.
 * Simple class used to describe the Particle
 */
public class FlameParticleType extends ParticleType<FlameParticleData> {
  private static boolean ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER = false;
  public FlameParticleType() {
    super(ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER, FlameParticleData.DESERIALIZER);
  }

  // get the Codec used to
  // a) convert a FlameParticleData to a serialised format
  // b) construct a FlameParticleData object from the serialised format
  public Codec<FlameParticleData> func_230522_e_() {
    return FlameParticleData.CODEC;
  }
}
