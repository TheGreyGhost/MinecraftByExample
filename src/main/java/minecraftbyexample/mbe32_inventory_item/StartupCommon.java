package minecraftbyexample.mbe32_inventory_item;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{

  public static ItemFlowerBag itemFlowerBag;  // this holds the unique instance of your block

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemFlowerBag = new ItemFlowerBag();
    itemFlowerBag.setRegistryName("mbe10_item_simple_registry_name");
    itemRegisterEvent.getRegistry().register(itemFlowerBag);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }

  public static ContainerType<ContainerBasic> containerTypeContainerBasic;

  @SubscribeEvent
  public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
  {
    containerTypeContainerBasic = IForgeContainerType.create(ContainerBasic::createContainerClientSide);
    containerTypeContainerBasic.setRegistryName("mbe30_container_registry_name");
    event.getRegistry().register(containerTypeContainerBasic);
  }
}
