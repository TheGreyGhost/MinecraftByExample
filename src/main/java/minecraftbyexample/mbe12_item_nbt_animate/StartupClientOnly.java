package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // we need to attach the "chargefraction" PropertyOverride to the Item, but there are two things to be careful of:
    // 1) We should do this on a client installation only, not on a DedicatedServer installation.  Hence we need to use
    //    FMLClientSetupEvent.
    // 2) FMLClientSetupEvent is multithreaded but ItemModelsProperties is not multithread-safe.  So we need to use the enqueueWork method,
    //    which lets us register a function for synchronous execution in the main thread after the parallel processing is completed
    event.enqueueWork(StartupClientOnly::registerPropertyOverride);
  }

  // We use a PropertyOverride for this item to change the appearance depending on the state of the property.
  //  See ItemNBTanimationTimer for more information.
  // ItemNBTanimationTimer() is used as a lambda function to calculate the current chargefraction during rendering
  public static void registerPropertyOverride() {
    ItemModelsProperties.registerProperty(StartupCommon.itemNBTAnimate, new ResourceLocation("chargefraction"), new ItemNBTanimationTimer());
  }
}
