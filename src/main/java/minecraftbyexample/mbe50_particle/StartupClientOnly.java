package minecraftbyexample.mbe50_particle;

import minecraftbyexample.mbe01_block_simple.*;
import minecraftbyexample.usefultools.RenderTypeMBE;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
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

  // Stitch the cube texture into the block texture sheet so that we can use it later for rendering.
  @SubscribeEvent
  public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {

    // There are many different texture sheets; if this is not the right one, do nothing
    AtlasTexture map = event.getMap();
    if (!map.getTextureLocation().equals(AtlasTexture.LOCATION_PARTICLES_TEXTURE)) return;

    event.addSprite(MBE21_CUBE_TEXTURE);
  }

  //  // Stitch the cube texture into the block texture sheet so that we can use it later for rendering.
//  Not needed for this example
//  @SubscribeEvent
//  public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
//
//    // There are many different texture sheets; if this is not the right one, do nothing
//    AtlasTexture map = event.getMap();
//    if (!map.getTextureLocation().equals(LOCATION_BLOCKS_TEXTURE)) return;
//
//    event.addSprite(MBE21_CUBE_TEXTURE);
//  }


//  public static void preInitClientOnly()
//  {
//    // register the texture stitcher, which is used to insert the flame picture into the block texture sheet
//    MinecraftForge.EVENT_BUS.register(new TextureStitcherBreathFX());
//
//  }
}
