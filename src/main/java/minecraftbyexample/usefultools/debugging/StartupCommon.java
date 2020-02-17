package minecraftbyexample.usefultools.debugging;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 * onBlocksRegistration then onItemsRegistration then FMLCommonSetupEvent
 *  See MinecraftByExample class for more information
 *
 *  Just used to register for ServerLifeCycleEvents, which we need for our command registration.
 *  We could have done that in the MinecraftByExample constructor instead, I placed it here to make it more obvious for
 *     the example
 */
public class StartupCommon
{

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(ServerLifecycleEvents.class);
    MinecraftForge.EVENT_BUS.register(DebugSpawnInhibitor.class);
    MinecraftForge.EVENT_BUS.register(DebugBlockVoxelShapeHighlighter.class);
  }
}
