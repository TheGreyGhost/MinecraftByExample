package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
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
  public static ItemNBTAnimate itemNBTAnimate;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemNBTAnimate = (ItemNBTAnimate)(new ItemNBTAnimate().setUnlocalizedName("mbe12_item_nbt_animate"));
    GameRegistry.registerItem(itemNBTAnimate, "mbe12_item_nbt_animate");
    // need to add the variants to the bakery so it knows what models are available for rendering.
    ModelBakery.addVariantName(itemNBTAnimate, "minecraftbyexample:mbe12_item_nbt_animate_still",
                                               "minecraftbyexample:mbe12_item_nbt_animate_0",
                                               "minecraftbyexample:mbe12_item_nbt_animate_1",
                                               "minecraftbyexample:mbe12_item_nbt_animate_2",
                                               "minecraftbyexample:mbe12_item_nbt_animate_3",
                                               "minecraftbyexample:mbe12_item_nbt_animate_4",
                                               "minecraftbyexample:mbe12_item_nbt_animate_5"
                                             );
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
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_still", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemNBTAnimate, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void postInitCommon()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
