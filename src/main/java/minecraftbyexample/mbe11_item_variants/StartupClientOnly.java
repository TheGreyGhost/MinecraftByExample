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

}
