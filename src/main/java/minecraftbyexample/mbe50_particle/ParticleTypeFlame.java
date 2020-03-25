package minecraftbyexample.mbe50_particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

/**
 * Created by TGG on 25/03/2020.
 */
public class ParticleTypeFlame extends ParticleType<ParticleDataFlame> {
  public ParticleTypeFlame() {
    super(false, ParticleDataFlame.DESERIALIZER);
  }
}
