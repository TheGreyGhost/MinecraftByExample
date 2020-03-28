package minecraftbyexample.mbe50_particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by TGG on 25/03/2020.
 *
 * On the client side:
 * When the client wants to spawn a Particle, it gives the FlameParticleData to this factory method
 *
 */
public class FlameParticleFactory implements IParticleFactory<FlameParticleData> {  //IParticleFactory

  @Nullable
  @Override
  public Particle makeParticle(FlameParticleData flameParticleData, World world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
    FlameParticle newParticle = new FlameParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, flameParticleData.getTint(), flameParticleData.getDiameter());
    newParticle.selectSpriteRandomly(spriteSet);
    return newParticle;
  }

  private final IAnimatedSprite spriteSet;

  public FlameParticleFactory(IAnimatedSprite sprite) {
    this.spriteSet = sprite;
  }

  //todo remove for bug testing only
  public FlameParticleFactory() {
    spriteSet = null;
  }

}
