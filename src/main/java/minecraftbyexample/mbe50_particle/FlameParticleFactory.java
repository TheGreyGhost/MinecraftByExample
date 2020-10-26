//package minecraftbyexample.mbe50_particle;
//
//import net.minecraft.client.particle.IAnimatedSprite;
//import net.minecraft.client.particle.IParticleFactory;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.world.World;
//
//import javax.annotation.Nullable;
//
///**
// * Created by TGG on 25/03/2020.
// *
// * On the client side:
// * When the client wants to spawn a Particle, it gives the FlameParticleData to this factory method
// * The factory selects an appropriate Particle class and instantiates it
// *
// */
//public class FlameParticleFactory implements IParticleFactory<FlameParticleData> {  //IParticleFactory
//
//  @Nullable
//  @Override
//  public Particle makeParticle(FlameParticleData flameParticleData, ClientWorld world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
//    FlameParticle newParticle = new FlameParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity,
//            flameParticleData.getTint(), flameParticleData.getDiameter(),
//            sprites);
//    newParticle.selectSpriteRandomly(sprites);  // choose a random sprite from the available list (in this case there is only one)
//    return newParticle;
//  }
//
//  private final IAnimatedSprite sprites;  // contains a list of textures; choose one using either
//  // newParticle.selectSpriteRandomly(sprites); or newParticle.selectSpriteWithAge(sprites);
//
//  // this method is needed for proper registration of your Factory:
//  // The ParticleManager.register method creates a Sprite and passes it to your factory for subsequent use when rendering, then
//  //   populates it with the textures from your textures/particle/xxx.json
//
//  public FlameParticleFactory(IAnimatedSprite sprite) {
//    this.sprites = sprite;
//  }
//
//  // This is private to prevent you accidentally registering the Factory using the default constructor.
//  // ParticleManager has two register methods, and if you use the wrong one the game will enter an infinite loop
//  private FlameParticleFactory() {
//    throw new UnsupportedOperationException("Use the FlameParticleFactory(IAnimatedSprite sprite) constructor");
//  }
//
//}
