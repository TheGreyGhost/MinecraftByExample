package minecraftbyexample.mbe03_block_variants;

import minecraftbyexample.usefultools.RenderTypeMBE;
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
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsBlue, RenderTypeMBE.CUTOUT());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsRed, RenderTypeMBE.CUTOUT());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsGreen, RenderTypeMBE.CUTOUT());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockVariantsYellow, RenderTypeMBE.CUTOUT());
  }
}
