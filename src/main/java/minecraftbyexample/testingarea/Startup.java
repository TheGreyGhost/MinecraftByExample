package minecraftbyexample.testingarea;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup class for this example is called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample for more information
 */
public class Startup
{
  public static BlockTestTorch blockTestTorch;  // this holds the unique instance of your block
  public static BlockTestNumberedPart blockTestNumberedPart;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockTestTorch = (BlockTestTorch)(new BlockTestTorch().setUnlocalizedName("test_torch"));
    GameRegistry.registerBlock(blockTestTorch, "test_torch");

    blockTestNumberedPart = (BlockTestNumberedPart)(new BlockTestNumberedPart().setUnlocalizedName("test_numbered_part"));
    GameRegistry.registerBlock(blockTestNumberedPart, "test_numbered_part");

  }

  public static void preInitClientOnly()
  {

  }

  public static void initCommon()
  {

  }

  public static void initClientOnly()
  {
    Item itemBlockSimple = GameRegistry.findItem("minecraftbyexample", "mbe01_block_simple");
    Item itemBlockPartial = GameRegistry.findItem("minecraftbyexample", "mbe02_block_partial");
    Item itemBlockTest1 = GameRegistry.findItem("minecraftbyexample", "test_torch");

    int i1 = Item.getIdFromItem(itemBlockSimple);
    int i2 = Item.getIdFromItem(itemBlockPartial);
    int i3 = Item.getIdFromItem(itemBlockTest1);
  }

  public static void postInitCommon()
  {

  }

  public static void postInitClientOnly()
  {

  }

}
