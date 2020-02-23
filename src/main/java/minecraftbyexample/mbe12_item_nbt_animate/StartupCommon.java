package minecraftbyexample.mbe12_item_nbt_animate;

import minecraftbyexample.mbe11_item_variants.ItemVariants;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014

 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemNBTAnimate itemNBTAnimate;  // this holds the unique instance of this item

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemNBTAnimate = new ItemNBTAnimate();
    itemNBTAnimate.setRegistryName("mbe12_item_nbt_animate_registry_name");
    itemRegisterEvent.getRegistry().register(itemNBTAnimate);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}

