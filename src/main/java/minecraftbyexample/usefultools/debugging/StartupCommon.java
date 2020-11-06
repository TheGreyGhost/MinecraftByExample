package minecraftbyexample.usefultools.debugging;

import minecraftbyexample.usefultools.debugging.commands.DebugTriggerWatcher;
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
 * Set up the debugging tools
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
    MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
    MinecraftForge.EVENT_BUS.register(DebugSpawnInhibitor.class);
    MinecraftForge.EVENT_BUS.register(DebugTriggerWatcher.class);
  }

}
