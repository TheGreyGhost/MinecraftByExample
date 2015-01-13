package minecraftbyexample.testingarea;

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
public class StartupClientOnly
{
  public static void preInitClientOnly()
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

  public static void postInitClientOnly()
  {
  }
}
