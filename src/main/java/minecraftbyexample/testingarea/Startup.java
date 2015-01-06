package minecraftbyexample.testingarea;

import minecraftbyexample.mbe01_block_simple.BlockSimple;
import minecraftbyexample.usefultools.MethodCallLogger;

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

  public static BlockSimple block1;
  public static BlockSimple block2;

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
    MethodCallLogger.test();

  }

  public static void preInitClientOnly()
  {

  }

  public static void initCommon()
  {

  }

  public static void initClientOnly()
  {
//    Item itemBlockSimple = GameRegistry.findItem("minecraftbyexample", "mbe01_block_simple");
//    Item itemBlockPartial = GameRegistry.findItem("minecraftbyexample", "mbe02_block_partial");
//    Item itemBlockTest1 = GameRegistry.findItem("minecraftbyexample", "test_torch");
//
//    int i1 = Item.getIdFromItem(itemBlockSimple);
//    int i2 = Item.getIdFromItem(itemBlockPartial);
//    int i3 = Item.getIdFromItem(itemBlockTest1);

//    Item itemBlock1 = GameRegistry.findItem("minecraftbyexample", "test_block1");
//    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:test_block1", "inventory");
//    final int DEFAULT_ITEM_SUBTYPE = 0;
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock1, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
//
//    Item itemBlock2 = GameRegistry.findItem("minecraftbyexample", "test_block2");
//    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:test_block2", "inventory");
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock2, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void postInitCommon()
  {

  }

  public static void postInitClientOnly()
  {

  }

}
