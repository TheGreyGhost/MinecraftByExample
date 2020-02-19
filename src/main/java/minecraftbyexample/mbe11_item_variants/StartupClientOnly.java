package minecraftbyexample.mbe11_item_variants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  @SubscribeEvent
  public void onClientSetupEvent(FMLClientSetupEvent event) {
    // not actually required for this example....
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
    //todo actually perhaps this is not required now that we have overrides in the model
    // The item we registered in StartupCommon will only load and bake models\item\mbe11_item_variants_registry_name
    // For this reason, we need to tell Minecraft to load and bake the other models (one for each bottle fullness),
    // because no registered items refer to those and hence they wouldn't otherwise be loaded.

    for (ItemVariants.EnumBottleFullness fullness : ItemVariants.EnumBottleFullness.values()) {
      String itemModelName = "mbe11_item_variants_registry_name" + fullness.getName();
      ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:" + itemModelName, "inventory");
                // --> eg minecraftbyexample:mbe11_item_variants_0pc, mbe11_item_variants_25pc, etc
      ModelLoader.addSpecialModel(itemModelResourceLocation);
    }
  }


}
