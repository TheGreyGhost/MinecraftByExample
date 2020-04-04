package minecraftbyexample.mbe31_inventory_furnace;

import net.minecraft.util.IIntArray;

/**
 * Created by TGG on 4/04/2020.
 * This class is used to store some state data for the furnace
 *  It encapsulates
 *
 */
public class TrackedFurnaceStateData implements IIntArray {

  @Override
  int get(int index);

  @Override
  void set(int index, int value);

  @Override
  int size();

}
