package minecraftbyexample.mbe65_capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides all the capabilities that an Air ItemStack can possess.
 * In this case there is only one.
 * 1) CapabilityElementalAir --> ElementalAirInterfaceInstance
 */
public class CapabilityProviderAirItems implements ICapabilitySerializable<INBT> {

  private final Direction NO_SPECIFIC_SIDE = null;

  /**
   * Asks the Provider if it has the given capability
   * @param capability<T> capability to be checked for
   * @param facing the side of the provider being checked (null = no particular side)
   * @param <T> The interface instance that is used
   * @return a lazy-initialisation supplier of the interface instance that is used to access this capability
   *         In this case, we don't actually use lazy initialisation because the instance is very quick to create.
   *         See CapabilityProviderFlowerBag for an example of lazy initialisation
   */
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
    if (CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE == capability) {
      return (LazyOptional<T>)LazyOptional.of(()-> elementalAirInterfaceInstance);
      // why are we using a lambda?  Because LazyOptional.of() expects a NonNullSupplier interface.  The lambda automatically
      //   conforms itself to that interface.  This save me having to define an inner class implementing NonNullSupplier.
      // The explicit cast to LazyOptional<T> is required because our CAPABILITY_ELEMENTAL_FIRE can't be typed.  Our code has
      //   checked that the requested capability matches, so the explict cast is safe (unless you have mixed them up)
    }
    return LazyOptional.empty();
  // Note that if you are implementing getCapability in a derived class which implements ICapabilityProvider, eg MyEntity, then you should call
  //     return super.getCapability(capability, facing);
  //   instead of returning empty.
  }

  /**Write all the capability state information to NBT - in this case only the air information
   */
  @Override
  public INBT serializeNBT() {
    return CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR.writeNBT(elementalAirInterfaceInstance, NO_SPECIFIC_SIDE);
  }

  /**Read the capability state information out of NBT - in this case only the air information
   * Overwrites the interfaceInstance with the NBT information
   */
  @Override
  public void deserializeNBT(INBT nbt) {
    CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR.readNBT(elementalAirInterfaceInstance, NO_SPECIFIC_SIDE, nbt);
  }

  private ElementalAirInterfaceInstance elementalAirInterfaceInstance = new ElementalAirInterfaceInstance();
}