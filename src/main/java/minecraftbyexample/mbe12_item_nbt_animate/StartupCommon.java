package minecraftbyexample.mbe12_item_nbt_animate;

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
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemNBTAnimate itemNBTAnimate;  // this holds the unique instance of your block

  public static void preInitCommon() {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemNBTAnimate = (ItemNBTAnimate) (new ItemNBTAnimate().setUnlocalizedName("mbe12_item_nbt_animate"));
    GameRegistry.registerItem(itemNBTAnimate, "mbe12_item_nbt_animate");
  }

  public static void initCommon() {
  }

  public static void postInitCommon() {
  }
}

