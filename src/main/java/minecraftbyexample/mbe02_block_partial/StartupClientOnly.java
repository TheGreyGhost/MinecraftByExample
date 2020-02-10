package minecraftbyexample.mbe02_block_partial;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  No client-only events are needed for this example
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  @SubscribeEvent
  public void onClientSetupEvent(FMLClientSetupEvent event) {
    // not actually required for this example....
  }
}

