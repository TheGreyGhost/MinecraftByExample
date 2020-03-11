package minecraftbyexample.mbe80_model_renderer;

import minecraftbyexample.mbe21_tileentityrenderer.AnimationTickCounter;
import minecraftbyexample.mbe21_tileentityrenderer.TileEntityRendererMBE21;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  /**
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // Tell the renderer that the base is rendered using CUTOUT (but the base is actually invisible so it doesn't really matter...)
    RenderTypeLookup.setRenderLayer(StartupCommon.blockMBE80, RenderType.getCutout());
    // Register the custom renderer for our tile entity
    ClientRegistry.bindTileEntityRenderer(StartupCommon.tileEntityDataTypeMBE80, TestModelTileEntityRenderer::new);
  }

}
