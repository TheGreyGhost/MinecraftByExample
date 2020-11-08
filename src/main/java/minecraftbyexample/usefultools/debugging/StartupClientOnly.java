package minecraftbyexample.usefultools.debugging;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 */
public class StartupClientOnly
{
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
      MinecraftForge.EVENT_BUS.register(DebugBlockVoxelShapeHighlighter.class);
    }
}
