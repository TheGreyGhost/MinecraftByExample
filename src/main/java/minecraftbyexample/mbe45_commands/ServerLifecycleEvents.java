package minecraftbyexample.mbe45_commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import minecraftbyexample.mbe45_commands.MBEsayCommand;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * Register our commands when the server starts up.
 *
 * Don't forget to register this class on the MinecraftForge.EVENT_BUS.
 */
public class ServerLifecycleEvents
{
  @SubscribeEvent
  public static void onServerStartingEvent(FMLServerStartingEvent event) {
    CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
    MBEsayCommand.register(commandDispatcher);
    MBEquoteCommand.register(commandDispatcher);
  }
}
