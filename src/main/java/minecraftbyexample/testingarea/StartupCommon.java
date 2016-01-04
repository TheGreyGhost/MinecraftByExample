package minecraftbyexample.testingarea;

import minecraftbyexample.mbe01_block_simple.BlockSimple;
import minecraftbyexample.mbe10_item_simple.ItemSimple;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample for more information
 */
public class StartupCommon
{
  public static BlockTestTorch blockTestTorch;  // this holds the unique instance of your block
  public static BlockTestNumberedPart blockTestNumberedPart;  // this holds the unique instance of your block

  public static BlockSimple block1;
  public static BlockSimple block2;

  public static Item

  public static void preInitCommon()
  {
//    block1 = (BlockSimple)(new BlockSimple().setUnlocalizedName("test_block1"));
//    block2 = (BlockSimple)(new BlockSimple().setUnlocalizedName("test_block2"));
//    GameRegistry.registerBlock(block2, "test_block2");
//    GameRegistry.registerBlock(block1, "test_block1");

//    // each instance of your block should have a name that is unique within your mod.  use lower case.
//    blockTestTorch = (BlockTestTorch)(new BlockTestTorch().setUnlocalizedName("test_torch"));
//    GameRegistry.registerBlock(blockTestTorch, "test_torch");
//
//    blockTestNumberedPart = (BlockTestNumberedPart)(new BlockTestNumberedPart().setUnlocalizedName("test_numbered_part"));
//    GameRegistry.registerBlock(blockTestNumberedPart, "test_numbered_part");
//
//    MethodCallLogger.test();

    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemSimple = (ItemSimple)(new ItemSimple().setUnlocalizedName("mbe10_item_simple"));
    GameRegistry.registerItem(itemSimple, "mbe10_item_simple");

  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
