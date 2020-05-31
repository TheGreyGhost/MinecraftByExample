package minecraftbyexample.mbe06_redstone;

import minecraftbyexample.mbe06_redstone.input.LampColour;
import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRendererRedstoneMeter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * These methods are called during startup
 *  See MinecraftByExample class for more information

 */
public class StartupClientOnly
{

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneVariableSource, RenderType.getSolid());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneTarget, RenderType.getSolid());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneColouredLamp, RenderType.getCutoutMipped());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneMeter, RenderType.getCutoutMipped());

    ClientRegistry.bindTileEntityRenderer(StartupCommon.tileEntityDataTypeMBE06, TileEntityRendererRedstoneMeter::new);
  }

  @SubscribeEvent
  public static void onColorHandlerEvent(ColorHandlerEvent.Block event)
  {
    // when vanilla wants to know what colour to render our BlockRedstoneColourLamp, it calls our
    //  IBlockColour implementation.  Because the interface only has one method, you could also use a
    //   lambda function instead (as vanilla does)
    event.getBlockColors().register(new LampColour(), StartupCommon.blockRedstoneColouredLamp);
  }
}
