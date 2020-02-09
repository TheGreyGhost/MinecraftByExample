package minecraftbyexample.mbe10_item_simple;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemSimple itemSimple;  // this holds the unique instance of your block

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemSimple = new ItemSimple();
    itemSimple.setRegistryName("mbe10_item_simple_registry_name");
    itemRegisterEvent.getRegistry().register(itemSimple);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}
