package minecraftbyexample.mbe65_capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by TGG on 17/06/2020.
 *
 * This class doesn't do much!
 * It's really just a marker for the capability.
 * register() should be called during FMLCommonSetupEvent: forge comes back later and stores the
 *   CapabilityElementalFire instance into the @CapabilityInject field.
 *
 * Note that, although I've used concrete implementations of the interface, you can register using a bare interface instead
 * see for example CapabilityItemHandler which uses
 *     @CapabilityInject(IItemHandler.class)
 *     public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
 *
 *     public static void register() {
 *         CapabilityManager.INSTANCE.register(IItemHandler.class, new Capability.IStorage<IItemHandler>()
 *
 */
public class CapabilityElementalAir {
    @CapabilityInject(ElementalAir.class)
    public static Capability<ElementalAir> CAPABILITY_ELEMENTAL_AIR = null;

    public static void register() {
      CapabilityManager.INSTANCE.register(
              ElementalAir.class,
              new ElementalAir.ElementalAirNBTStorage(),
              ElementalAir::createADefaultInstance);
    }
}
