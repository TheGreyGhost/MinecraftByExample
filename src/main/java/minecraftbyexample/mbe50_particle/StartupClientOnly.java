package minecraftbyexample.mbe50_particle;

import minecraftbyexample.mbe01_block_simple.*;
import minecraftbyexample.usefultools.RenderTypeMBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
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

    // beware - there are two registerFactory methods with different signatures.
    // If you use the wrong one it will put Minecraft into an infinite loading loop with no console errors
    Minecraft.getInstance().particles.registerFactory(StartupCommon.flameParticleType, sprite -> new FlameParticleFactory(sprite));
    //  This lambda may not be obvious: its purpose is:
    //  the registerFactory method creates an IAnimatedSprite, then passes it to the constructor of FlameParticleFactory

    //  General rule of thumb:
    // If you are creating a TextureParticle with a corresponding json to specify textures which will be stitched into the
    //    particle texture sheet, then use the 1-parameter constructor method
    // If you're supplying the render yourself, or using a texture from the block sheet, use the 0-parameter constructor method
    //   (examples are MobAppearanceParticle, DiggingParticle).  See ParticleManager::registerFactories for more.
  }

}


