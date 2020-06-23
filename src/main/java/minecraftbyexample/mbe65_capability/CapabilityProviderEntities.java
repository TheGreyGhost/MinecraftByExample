package minecraftbyexample.mbe65_capability;

import minecraftbyexample.usefultools.NBTtypesMBE;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides all the capabilities that an Entity can possess.
 * In this case there are two:
 * 1) CapabilityElementalFire --> ElementalFireInterfaceInstance
 * 2) CapabilityElementalAir --> ElementalAirInterfaceInstance
 */
public class CapabilityProviderEntities implements ICapabilitySerializable<INBT> {

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
      return (LazyOptional<T>)LazyOptional.of(()-> elementalFire);
      // why are we using a lambda?  Because LazyOptional.of() expects a NonNullSupplier interface.  The lambda automatically
      //   conforms itself to that interface.  This save me having to define an inner class implementing NonNullSupplier.
      // The explicit cast to LazyOptional<T> is required because our CAPABILITY_ELEMENTAL_FIRE can't be typed.  Our code has
      //   checked that the requested capability matches, so the explict cast is safe (unless you have mixed them up)
    }
    if (CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR == capability) {
      return (LazyOptional<T>)LazyOptional.of(()-> elementalAir);
    }

    return LazyOptional.empty();
    // Note that if you are implementing getCapability in a derived class which implements ICapabilityProvider
    // eg you have added a new MyEntity which has the method MyEntity::getCapability instead of using AttachCapabilitiesEvent to attach a
    // separate class, then you should call
    // return super.getCapability(capability, facing);
    //   instead of
    // return LazyOptional.empty();
  }

  private final static String AIR_NBT = "air";
  private final static String FIRE_NBT = "fire";

  /**Write all the capability state information to NBT - fire, and air
   */
  @Override
  public INBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    INBT fireNBT = CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE.writeNBT(elementalFire, NO_SPECIFIC_SIDE);
    INBT airNBT = CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR.writeNBT(elementalAir, NO_SPECIFIC_SIDE);
    nbt.put(AIR_NBT, airNBT);
    nbt.put(FIRE_NBT, fireNBT);
    return nbt;
  }

  /**Read the capability state information out of NBT - fire, and air
   * Overwrite the interface instances with the nbt information
   */
  @Override
  public void deserializeNBT(INBT nbt) {
    if (nbt.getId() != NBTtypesMBE.COMPOUND_NBT_ID) {
      LOGGER.warn("Unexpected NBT type:"+nbt);
      return;  // leave as default in case of error
    }
    CompoundNBT compoundNBT = (CompoundNBT)nbt;
    INBT airNBT = compoundNBT.get(AIR_NBT);
    INBT fireNBT = compoundNBT.get(FIRE_NBT);

    CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR.readNBT(elementalAir, NO_SPECIFIC_SIDE, airNBT);
    CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE.readNBT(elementalFire, NO_SPECIFIC_SIDE, fireNBT);
  }

  private ElementalFire elementalFire = new ElementalFire();
  private ElementalAir elementalAir = new ElementalAir();

  private static final Logger LOGGER = LogManager.getLogger();

}