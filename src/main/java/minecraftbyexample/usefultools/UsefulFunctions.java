package minecraftbyexample.usefultools;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3d;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 */
public class UsefulFunctions
{
  /** linearly interpolate for y between [x1, y1] to [x2, y2] using x
   *  y = y1 + (y2 - y1) * (x - x1) / (x2 - x1)
   *  For example:  if [x1, y1] is [0, 100], and [x2,y2] is [1, 200], then as x increases from 0 to 1, this function
   *    will increase from 100 to 200
   * @param x  the x value to linearly interpolate on
   * @param x1
   * @param x2
   * @param y1
   * @param y2
   * @return linearly interpolated value.  If x is outside the range, clip it to the nearest end
   */
  public static double interpolate_with_clipping(double x, double x1, double x2, double y1, double y2)
  {
    if (x1 > x2) {
      double temp = x1; x1 = x2; x2 = temp;
      temp = y1; y1 = y2; y2 = temp;
    }

    if (x <= x1) return y1;
    if (x >= x2) return y2;
    double xFraction = (x - x1) / (x2 - x1);
    return y1 + xFraction * (y2 - y1);
  }

  public static Vector3d scalarMultiply(Vector3d source, double multiplier)
  {
    return new Vector3d(source.x * multiplier, source.y * multiplier, source.z * multiplier);
  }


  /***
   * creates a NBT list from the Vec3d passed to this function
   */
  public static ListNBT serializeVec3d(Vector3d vec3d) {
    ListNBT listnbt = new ListNBT();
    listnbt.add(DoubleNBT.valueOf(vec3d.x));
    listnbt.add(DoubleNBT.valueOf(vec3d.y));
    listnbt.add(DoubleNBT.valueOf(vec3d.z));
    return listnbt;
  }

  /**
   * Creates a Vec3d from the given NBT tag
   * @param nbt the Compound that holds the given tag
   * @param tagname name of the tag that was used to save the Vec3d
   * @return the new Vec3d
   */
  public static Vector3d deserializeVec3d(CompoundNBT nbt, String tagname) {
    ListNBT listnbt = nbt.getList(tagname, NBTtypesMBE.DOUBLE_NBT_ID);
    Vector3d retval = new Vector3d(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));
    return retval;
  }
}
