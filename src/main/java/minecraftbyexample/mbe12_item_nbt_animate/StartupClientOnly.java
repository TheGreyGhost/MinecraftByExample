package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

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
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    // model to be used for rendering this item
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemNBTAnimate, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    // previously, you needed to add the other variants to the bakery, so it knows what models are available for rendering.
    // This is no longer required; the other models are all listed in the json
//    ModelBakery.registerItemVariants(StartupCommon.itemNBTAnimate,
//            new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_0"),
//            new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_1"),
//            new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_2"),
//            new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_3"),
//            new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_4"),
//            new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_5"));
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
