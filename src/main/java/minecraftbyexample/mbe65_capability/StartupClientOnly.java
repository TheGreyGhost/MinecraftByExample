package minecraftbyexample.mbe65_capability;

import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * The Startup class for this example is called during startup of the mod
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly {
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // we need to attach several PropertyOverrides to the Items, but there are two things to be careful of:
    // 1) We should do this on a client installation only, not on a DedicatedServer installation.  Hence we need to use
    //    FMLClientSetupEvent.
    // 2) FMLClientSetupEvent is multithreaded but ItemModelsProperties is not multithread-safe.  So we need to use the enqueueWork method,
    //    which lets us register a function for synchronous execution in the main thread after the parallel processing is completed
    event.enqueueWork(StartupClientOnly::registerPropertyOverride);
  }

  // We use a PropertyOverride for this item to change the appearance depending on the state of the property.
  public static void registerPropertyOverride() {
    ItemModelsProperties.registerProperty(StartupCommon.itemElementalBowFire, new ResourceLocation("pulltime"), ItemElementalBowFire::getPullDurationSeconds);
    ItemModelsProperties.registerProperty(StartupCommon.itemElementalBowFire, new ResourceLocation("isbeingpulled"), ItemElementalBowFire::isBeingPulled);
    ItemModelsProperties.registerProperty(StartupCommon.itemElementalCrossbowAir, new ResourceLocation("pullfraction"), ItemElementalCrossbowAir::getPullFraction);
    ItemModelsProperties.registerProperty(StartupCommon.itemElementalCrossbowAir, new ResourceLocation("isbeingpulled"), ItemElementalCrossbowAir::isBeingPulled);
    ItemModelsProperties.registerProperty(StartupCommon.itemElementalCrossbowAir, new ResourceLocation("ischarged"), ItemElementalCrossbowAir::isFullyCharged);
  }
}

