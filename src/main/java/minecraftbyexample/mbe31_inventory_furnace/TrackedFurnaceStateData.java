package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.util.IIntArray;

/**
 * Created by TGG on 4/04/2020.
 * This class is used to store some state data for the furnace (eg burn time, smelting time, etc)
 * It is used to
 * 1) store the state data permanently using NBT
 * 2) synchronise the data between the server and client containers (using trackIntArray)
 *
 */
public class TrackedFurnaceStateData implements IIntArray {



  @Override
  public int get(int index) {
    return allValues[index];
  }

  @Override
  public void set(int index, int value) {
    allValues[index] = value;
  }

  @Override
  public int size() {
    return  allValues.length;
  }

  int [] allValues = new int[3];

}
