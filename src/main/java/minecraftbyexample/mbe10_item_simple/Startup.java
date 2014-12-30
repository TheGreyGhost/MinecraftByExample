package minecraftbyexample.mbe10_item_simple;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
 *  See MinecraftByExample class for more information
 */
public class Startup
{
  public static ItemSimple itemSimple;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemSimple = (ItemSimple)(new ItemSimple().setUnlocalizedName("mbe10_item_simple"));
    GameRegistry.registerItem(itemSimple, "mbe10_item_simple");
  }

  public static void preInitClientOnly()
  {
  }

  public static void initCommon()
  {
  }

  public static void initClientOnly()
  {
    // required in order for the renderer to know how to render your item.  Likely to change in the near future.
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe10_item_simple", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

  }

  public static void postInitCommon()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
