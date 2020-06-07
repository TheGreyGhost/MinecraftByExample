package minecraftbyexample.mbe32_inventory_item;

import minecraftbyexample.mbe30_inventory_basic.ContainerScreenBasic;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * The Startup class for this example is called during startup of the mod
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  // register the factory that is used on the client to generate a ContainerScreen corresponding to our Container
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    ScreenManager.registerFactory(StartupCommon.containerTypeContainerBasic, ContainerScreenBasic::new);
  }
}
