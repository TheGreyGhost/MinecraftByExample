package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

import java.util.Arrays;

/**
 * Created by TGG on 4/04/2020.
 * This class is used to store some state data for the furnace (eg burn time, smelting time, etc)
 * 1) The TileEntity uses it to store the data permanently, including NBT serialisation and deserialisation
 * 2) The server container uses it to
 *    a) read/write permanent data
 *    b) synchronise the data to the client container using the IIntArray interface (via Container::trackIntArray)
 * 3) The client container uses it to store a temporary copy of the data, for rendering / GUI purposes
 * The TileEntity and the client container both use it by poking directly into its member variables.  That's not good
 *   practice but it's easier to understand that the vanilla method which uses an anonymous class/lambda functions
 *
 *  The IIntArray interface collates all the separate member variables into a single array for the purposes of transmitting
 *     from server to cient.
 */
public class FurnaceStateData implements IIntArray {

  public final int FUEL_SLOTS_COUNT = TileInventoryFurnace.FUEL_SLOTS_COUNT;

  /**The number of ticks the current item has been cooking*/
  public int cookTime;
  /** The initial fuel value of the currently burning fuel (in ticks of burn duration) */
  public int [] burnTimeInitialValues = new int[FUEL_SLOTS_COUNT];
  /** The number of burn ticks remaining on the current piece of fuel */
  public int [] burnTimeRemainings = new int[FUEL_SLOTS_COUNT];

  // --------- read/write to NBT for permanent storage (on disk, or packet transmission) - used by the TileEntity only

  public void putIntoNBT(CompoundNBT nbtTagCompound) {
    nbtTagCompound.putInt("CookTime", cookTime);
    nbtTagCompound.putIntArray("burnTimeRemainings", burnTimeRemainings);
    nbtTagCompound.putIntArray("burnTimeInitial", burnTimeInitialValues);
  }

  public void readFromNBT(CompoundNBT nbtTagCompound) {
      // Trim the arrays (or pad with 0) to make sure they have the correct number of elements
    cookTime = nbtTagCompound.getInt("CookTime");
    burnTimeRemainings = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeRemainings"), FUEL_SLOTS_COUNT);
    burnTimeInitialValues = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeInitialValues"), FUEL_SLOTS_COUNT);
  }

  // -------- used by vanilla, not intended for mod code
//  * The ints are mapped (internally) as:
//  * 0 = cookTime
//  * 1 .. FUEL_SLOTS_COUNT = burnTimeInitialValues[]
//  * FUEL_SLOTS_COUNT + 1 .. 2*FUEL_SLOTS_COUNT = burnTimeRemainings[]
//  *

  private final int COOKTIME_INDEX = 0;
  private final int BURNTIME_INITIAL_VALUE_INDEX = 1;
  private final int BURNTIME_REMAINING_INDEX = BURNTIME_INITIAL_VALUE_INDEX + FUEL_SLOTS_COUNT;

  @Override
  public int get(int index) {
    validateIndex(index);
    if (index < BURNTIME_INITIAL_VALUE_INDEX) {
      return cookTime;
    } else if (index < BURNTIME_REMAINING_INDEX) {
      return  burnTimeInitialValues[index - BURNTIME_INITIAL_VALUE_INDEX];
    } else {
      return  burnTimeRemainings[index - BURNTIME_REMAINING_INDEX];
    }
  }

  @Override
  public void set(int index, int value) {
    validateIndex(index);
    if (index < BURNTIME_INITIAL_VALUE_INDEX) {
      cookTime = value;
    } else if (index < BURNTIME_REMAINING_INDEX) {
      burnTimeInitialValues[index - BURNTIME_INITIAL_VALUE_INDEX] = value;
    } else {
      burnTimeRemainings[index - BURNTIME_REMAINING_INDEX] = value;
    }
  }

  @Override
  public int size() {
    return 1 + FUEL_SLOTS_COUNT * 2;
  }

  private void validateIndex(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException("Index out of bounds:"+index);
    }
  }

}
