package minecraftbyexample.mbe65_capability;

import minecraftbyexample.mbe32_inventory_item.ContainerFlowerBag;
import minecraftbyexample.mbe32_inventory_item.ItemFlowerBag;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
  public static ContainerType<ContainerFlowerBag> containerTypeFlowerBag;

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemFlowerBag = new ItemFlowerBag();
    itemFlowerBag.setRegistryName("mbe32_flower_bag_registry_name");
    itemRegisterEvent.getRegistry().register(itemFlowerBag);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    CapabilityElementalFire.register();
  }

  @SubscribeEvent
  public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
    containerTypeFlowerBag = IForgeContainerType.create(ContainerFlowerBag::createContainerClientSide);
    containerTypeFlowerBag.setRegistryName("mbe32_container_registry_name");
    event.getRegistry().register(containerTypeFlowerBag);
  }
}
