package minecraftbyexample.mbe65_capability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by TGG on 18/06/2020.
 *
 * This class stores the amount of ElementalFire that has been attached to the ItemStack or Entity.
 *
 * It contains a static inner class which is used to convert to/from NBT storage, for loading/saving to disk and for network transmission
 */
public class ElementalFireInterfaceInstance {

  public ElementalFireInterfaceInstance() {
    this(0);
  }

  public ElementalFireInterfaceInstance(int initialChargeLevel) {
    chargeLevel = initialChargeLevel;
  }

  public int getChargeLevel() {return chargeLevel;}
  public void addCharge(int chargeToAdd) {chargeLevel += chargeToAdd;}

  private void setChargeLevel(int chargeLevelToSet) {chargeLevel = chargeLevelToSet;}

  private int chargeLevel;

  // Convert to/from NBT
  public static class ElementalFireNBTStorage implements Capability.IStorage<ElementalFireInterfaceInstance> {
    @Override
    public INBT writeNBT(Capability<ElementalFireInterfaceInstance> capability, ElementalFireInterfaceInstance instance, Direction side) {
      IntNBT intNBT = IntNBT.valueOf(instance.chargeLevel);
      return intNBT;
    }

    @Override
    public void readNBT(Capability<ElementalFireInterfaceInstance> capability, ElementalFireInterfaceInstance instance, Direction side, INBT nbt) {
      int chargeLevel = 0;
      if (nbt.getType() == IntNBT.TYPE) {
        chargeLevel = ((IntNBT)nbt).getInt();
      }
      instance.setChargeLevel(chargeLevel);
    }
  }

  public static ElementalFireInterfaceInstance createADefaultInstance() {
    return new ElementalFireInterfaceInstance();
  }

}
