package minecraftbyexample.mbe11_item_variants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
//    need to add the variants to the bakery so it knows what models are available for rendering the different subtypes
//    In previous versions, this was required, however as of 1.8.9 ModelLoader.setCustomModelResourceLocation does this
//      for you - see later on - so it's not necessary.
//    ModelBakery.addVariantName(StartupCommon.itemVariants,  "minecraftbyexample:mbe11_item_variants_0pc",
//            "minecraftbyexample:mbe11_item_variants_25pc",
//            "minecraftbyexample:mbe11_item_variants_50pc",
//            "minecraftbyexample:mbe11_item_variants_75pc",
//            "minecraftbyexample:mbe11_item_variants_100pc"
//    );

  }
  @SubscribeEvent
  public static void onColorHandlerEvent(ColorHandlerEvent.Item event)
  {
    // the LiquidColour lambda function is used to change the rendering colour of the liquid in the bottle
    // i.e.: when vanilla wants to know what colour to render our itemVariants instance, it calls the LiquidColour lambda function
    event.getItemColors().register(new LiquidColour(), StartupCommon.itemVariants);
  }

  @SubscribeEvent
  public static void onModelRegistryEvent(ModelRegistryEvent event)
  {
    // The item we registered in StartupCommon will only load and bake models\item\mbe11_item_variants_registry_name
    // For this reason, we need to tell Minecraft to load and bake the other models (one for each bottle fullness),
    // because no registered items refer to those and hence they wouldn't be loaded.

    for (ItemVariants.EnumBottleFullness fullness : ItemVariants.EnumBottleFullness.values()) {
      String itemModelName = "mbe11_item_variants_registry_name" + fullness.getName();
      ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:" + itemModelName, "inventory");
                // --> eg minecraftbyexample:mbe11_item_variants_0pc, mbe11_item_variants_25pc, etc
      ModelLoader.addSpecialModel(itemModelResourceLocation);
    }
  }


}
