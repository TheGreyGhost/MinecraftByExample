package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
    // need to add the variants to the bakery so it knows what models are available for rendering.
    ModelBakery.addVariantName(StartupCommon.itemNBTAnimate, "minecraftbyexample:mbe12_item_nbt_animate_still",
            "minecraftbyexample:mbe12_item_nbt_animate_0",
            "minecraftbyexample:mbe12_item_nbt_animate_1",
            "minecraftbyexample:mbe12_item_nbt_animate_2",
            "minecraftbyexample:mbe12_item_nbt_animate_3",
            "minecraftbyexample:mbe12_item_nbt_animate_4",
            "minecraftbyexample:mbe12_item_nbt_animate_5"
    );
    // required in order for the renderer to know how to render your item.  Likely to change in the near future.
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_still", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemNBTAnimate, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
