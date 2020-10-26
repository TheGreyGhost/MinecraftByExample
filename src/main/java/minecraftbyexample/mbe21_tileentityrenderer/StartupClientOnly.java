//package minecraftbyexample.mbe21_tileentityrenderer;
//
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.RenderTypeLookup;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.client.registry.ClientRegistry;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//
///**
// * User: The Grey Ghost
// * Date: 24/12/2014
// *
// *  See MinecraftByExample class for more information
// */
//public class StartupClientOnly
//{
//
//  /**
//   * @param event
//   */
//  @SubscribeEvent
//  public static void onClientSetupEvent(FMLClientSetupEvent event) {
//    // Tell the renderer that the base is rendered using CUTOUT_MIPPED (to match the Block Hopper)
//    RenderTypeLookup.setRenderLayer(StartupCommon.blockMBE21, RenderType.getCutoutMipped());
//    // Register the custom renderer for our tile entity
//    ClientRegistry.bindTileEntityRenderer(StartupCommon.tileEntityDataTypeMBE21, TileEntityRendererMBE21::new);
//
//    MinecraftForge.EVENT_BUS.register(AnimationTickCounter.class);  // counts ticks, used for animation
//  }
//
////  // Stitch the cube texture into the block texture sheet so that we can use it later for rendering.
////  Not needed for this example
////  @SubscribeEvent
////  public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
////
////    // There are many different texture sheets; if this is not the right one, do nothing
////    AtlasTexture map = event.getMap();
////    if (!map.getTextureLocation().equals(LOCATION_BLOCKS_TEXTURE)) return;
////
////    event.addSprite(MBE21_CUBE_TEXTURE);
////  }
//
//}
