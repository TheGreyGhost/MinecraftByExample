package minecraftbyexample.mbe30b_inventory_basic;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * Converted to 10.1.2 by alvaropp
 *
 * The Startup class for this example is called during startup of the mod
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  // register the factory that is used on the client to generate a ContainerScreen corresponding to our Container
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    ScreenManager.registerFactory(StartupCommon.containerTypeContainerBasic_IIH, ContainerScreenBasic_IIH::new);
  }
}
