package minecraftbyexample.mbe65_capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides all the capabilities that a Fire ItemStack can possess. (used for the ItemElementalBowFire)
 * In this case there is only one.
 * 1) CapabilityElementalFire --> ElementalFireInterfaceInstance
 */
public class CapabilityProviderFireItems implements ICapabilitySerializable<INBT> {

  private static final Direction NO_SPECIFIC_SIDE = null;

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
      return (LazyOptional<T>)LazyOptional.of(()-> elementalFire);
      // why are we using a lambda?  Because LazyOptional.of() expects a NonNullSupplier interface.  The lambda automatically
      //   conforms itself to that interface.  This save me having to define an inner class implementing NonNullSupplier.
      // The explicit cast to LazyOptional<T> is required because our CAPABILITY_ELEMENTAL_FIRE can't be typed.  Our code has
      //   checked that the requested capability matches, so the explicit cast is safe (unless you have made a mistake and mixed them up!)
    }
    return LazyOptional.empty();
    // Note that if you are implementing getCapability in a derived class which implements ICapabilityProvider
    // eg you have added a new MyEntity which has the method MyEntity::getCapability instead of using AttachCapabilitiesEvent to attach a
    // separate class, then you should call
    // return super.getCapability(capability, facing);
    //   instead of
    // return LazyOptional.empty();
  }

  /**Write all the capability state information to NBT - in this case only the fire information
   */
  @Override
  public INBT serializeNBT() {
    return CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE.writeNBT(elementalFire, NO_SPECIFIC_SIDE);
  }

  /**Read the capability state information out of NBT - in this case only the fire information
   */
  @Override
  public void deserializeNBT(INBT nbt) {
    CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE.readNBT(elementalFire, NO_SPECIFIC_SIDE, nbt);
  }

  private ElementalFire elementalFire = new ElementalFire();
}