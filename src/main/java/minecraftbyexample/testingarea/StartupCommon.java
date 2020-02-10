package minecraftbyexample.testingarea;

import com.mojang.brigadier.CommandDispatcher;
import minecraftbyexample.mbe01_block_simple.BlockSimple;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.AdvancementCommand;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 * onBlocksRegistration then onItemsRegistration then FMLCommonSetupEvent
 *  See MinecraftByExample class for more information
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
    // not actually required for this example....
  }
}
