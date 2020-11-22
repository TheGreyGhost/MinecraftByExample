package minecraftbyexample.mbe11_item_variants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
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
  public static void onColorHandlerEvent(ColorHandlerEvent.Item event)
  {
    // the LiquidColour lambda function is used to change the rendering colour of the liquid in the bottle
    // i.e.: when vanilla wants to know what colour to render our itemVariants instance, it calls the LiquidColour lambda function
    event.getItemColors().register(new LiquidColour(), StartupCommon.itemVariants);
  }

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // we need to attach the fullness PropertyOverride to the Item, but there are two things to be careful of:
    // 1) We should do this on a client installation only, not on a DedicatedServer installation.  Hence we need to use
    //    FMLClientSetupEvent.
    // 2) FMLClientSetupEvent is multithreaded but ItemModelsProperties is not multithread-safe.  So we need to use the enqueueWork method,
    //    which lets us register a function for synchronous execution in the main thread after the parallel processing is completed
    event.enqueueWork(StartupClientOnly::registerPropertyOverride);
  }

  public static void registerPropertyOverride() {
    ItemModelsProperties.registerProperty(StartupCommon.itemVariants, new ResourceLocation("fullness"), ItemVariants::getFullnessPropertyOverride);
    // use lambda function to link the NBT fullness value to a suitable property override value
  }
}
