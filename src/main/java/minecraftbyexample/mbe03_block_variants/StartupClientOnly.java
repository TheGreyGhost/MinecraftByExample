package minecraftbyexample.mbe03_block_variants;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsBlue, RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsRed, RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsGreen, RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsYellow, RenderType.getCutout());

    RenderTypeLookup.setRenderLayer(StartupCommon.block3DWeb, RenderType.getSolid());
  }
}
