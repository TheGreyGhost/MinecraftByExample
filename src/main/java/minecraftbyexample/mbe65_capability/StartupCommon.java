package minecraftbyexample.mbe65_capability;

import minecraftbyexample.mbe32_inventory_item.ContainerFlowerBag;
import minecraftbyexample.mbe32_inventory_item.ItemFlowerBag;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
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

  public static ItemElementalBowFire itemElementalBowFire;  // this holds the unique instance of your block
  public static ContainerType<ContainerFlowerBag> containerTypeFlowerBag;

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemElementalBowFire = new ItemElementalBowFire();
    itemElementalBowFire.setRegistryName("mbe65_bow_fire_registry_name");
    itemRegisterEvent.getRegistry().register(itemElementalBowFire);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    CapabilityElementalFire.register();
    MinecraftForge.EVENT_BUS.register(CapabilityAttachEventHandler.class);
    MinecraftForge.EVENT_BUS.register(ElementalInteractions.class);
  }
}
