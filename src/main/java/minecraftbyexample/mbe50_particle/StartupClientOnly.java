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
//  public static ResourceLocation FLAME_TEXTURE_RL = new ResourceLocation("minecraftbyexample:entity/mbe50_flame_fx");
  /**
   * Tell the renderer this is a solid block (default is translucent)
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockFlameEmitter, RenderTypeMBE.SOLID());
  }

//  // Stitch the cube texture into the block texture sheet so that we can use it later for rendering.
//  @SubscribeEvent
//  public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
//
//    // There are many different texture sheets; if this is not the right one, do nothing
//    AtlasTexture map = event.getMap();
//    if (!map.getTextureLocation().equals(AtlasTexture.LOCATION_PARTICLES_TEXTURE)) return;
//    event.addSprite(FLAME_TEXTURE_RL);
//  }

  // Register the factory that will spawn our Particle from ParticleData
  @SubscribeEvent
  public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
//    Minecraft.getInstance().particles.registerFactory(StartupCommon.flameParticleType, new FlameParticleFactory()); // here
    int j = 1;
    Minecraft.getInstance().particles.registerFactory(StartupCommon.flameParticleType, sprite -> new FlameParticleFactory(sprite)); // here
  }

}


