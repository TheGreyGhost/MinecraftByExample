package minecraftbyexample.mbe50_particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by TGG on 25/03/2020.
 */
public class ParticleFactoryFlame implements IParticleFactory<ParticleDataFlame> {

  @Nullable
  @Override
  public Particle makeParticle(ParticleDataFlame particleDataFlame, World world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
    return new FlameParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity);
  }
}
