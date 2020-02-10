package minecraftbyexample.testingarea;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 */
public class StartupForgeEvents
{
  @SubscribeEvent
  public static void onServerStartingEvent(FMLServerStartingEvent event) {
    CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
    MBEsayCommand.register(commandDispatcher);
  }
}
