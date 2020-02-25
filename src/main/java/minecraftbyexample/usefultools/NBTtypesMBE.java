package minecraftbyexample.usefultools;


import net.minecraft.nbt.*;

import java.util.List;

/**
 * Created by TGG on 21/02/2020.
 * Just used to make the code more readable due to obfuscated names
 * Once the mapping is updated to something nicer, we can remove...
 */
public class NBTtypesMBE {
  public static final byte LONG_NBT_ID = LongNBT.func_229698_a_(0).getId();
  public static final byte INT_NBT_ID = IntNBT.func_229692_a_(0).getId();
  public static final byte SHORT_NBT_ID = ShortNBT.func_229701_a_((short)0).getId();
  public static final byte BYTE_NBT_ID = ByteNBT.func_229671_a_((byte)0).getId();
  public static final byte FLOAT_NBT_ID = FloatNBT.func_229689_a_(0).getId();
  public static final byte DOUBLE_NBT_ID = DoubleNBT.func_229684_a_(0).getId();
  public static final byte STRING_NBT_ID = StringNBT.func_229705_a_("").getId();

  private static byte [] dummyByteArray = {0};
  private static int [] dummyIntArray = {0};
  private static long [] dummyLongArray = {0};
  public static final byte BYTE_ARRAY_NBT_ID = new ByteArrayNBT(dummyByteArray).getId();
  public static final byte INT_ARRAY_NBT_ID = new IntArrayNBT(dummyIntArray).getId();
  public static final byte LONG_ARRAY_NBT_ID = new LongArrayNBT(dummyLongArray).getId();
  public static final byte LIST_NBT_ID = new ListNBT().getId();
  public static final byte COMPOUND_NBT_ID = new CompoundNBT().getId();
}
