package minecraftbyexample.mbe50_particle;

import minecraftbyexample.usefultools.RenderTypeMBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.particles.IParticleData;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
//  public static ResourceLocation FLAME_TEXTURE_RL = new ResourceLocation("minecraftbyexample:entity/mbe50_flame_fx");
  /**
   * Tell the renderer this is a solid block (default is translucent)
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockFlameEmitter, RenderTypeMBE.SOLID());
  }

  // Register the factory that will spawn our Particle from ParticleData
  @SubscribeEvent
  public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {

    final boolean TRIGGER_INFINITE_LOOP = true;
    if (TRIGGER_INFINITE_LOOP) {
      Minecraft.getInstance().particles.registerFactory(StartupCommon.flameParticleType, new FlameParticleFactory());

      // invokes first ParticleManager register method signature:
//      public <T extends IParticleData > void registerFactory(ParticleType<T> particleTypeIn, IParticleFactory<T> particleFactoryIn) {
//        this.factories.put(Registry.PARTICLE_TYPE.getKey(particleTypeIn), particleFactoryIn);
//      }
//      This does not add an IAnimatedSprite to ParticleManager.sprites, which causes an exception to be thrown during texture loading:
//      from mbe50_flame_particle_type_registry_name.json:
//      {
//        "textures": [
//        "minecraftbyexample:mbe50_flame_fx"
//        ]
//      }
//
//      private void loadTextureLists(IResourceManager manager, ResourceLocation particleId, Map<ResourceLocation, List<ResourceLocation>> textures) {
//        boolean flag = this.sprites.containsKey(particleId);
//        if (list == null) {
//          if (flag) {
//            throw new IllegalStateException("Missing texture list for particle " + particleId);
//          }
//        } else {
//          if (!flag) {
//            throw new IllegalStateException("Redundant texture list for particle " + particleId);  // < ----- here
//          }
      } else {
        Minecraft.getInstance().particles.registerFactory(StartupCommon.flameParticleType, sprite -> new FlameParticleFactory(sprite));

//invokes second method signature in ParticleManager
//      public <T extends IParticleData> void registerFactory(ParticleType<T> particleTypeIn, ParticleManager.IParticleMetaFactory<T> particleMetaFactoryIn) {
//        ParticleManager.AnimatedSpriteImpl particlemanager$animatedspriteimpl = new ParticleManager.AnimatedSpriteImpl();
//        this.sprites.put(Registry.PARTICLE_TYPE.getKey(particleTypeIn), particlemanager$animatedspriteimpl);
//        this.factories.put(Registry.PARTICLE_TYPE.getKey(particleTypeIn), particleMetaFactoryIn.create(particlemanager$animatedspriteimpl));
//      }
//     which registers the sprite as expected by the loadTextureLists.

    }
  }

}


