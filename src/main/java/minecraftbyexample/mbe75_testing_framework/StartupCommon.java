package minecraftbyexample.mbe75_testing_framework;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The methods in this class are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemTestRunner itemTestRunner;  // this holds the unique instance of your block

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemTestRunner = new ItemTestRunner();
    itemTestRunner.setRegistryName("mbe75_test_runner_registry_name");
    itemRegisterEvent.getRegistry().register(itemTestRunner);
  }
}
