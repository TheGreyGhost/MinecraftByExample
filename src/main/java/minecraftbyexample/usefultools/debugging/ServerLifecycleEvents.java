package minecraftbyexample.usefultools.debugging;

import com.mojang.brigadier.CommandDispatcher;
import minecraftbyexample.usefultools.debugging.commands.MBEdebugCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

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
    MBEdebugCommand.register(commandDispatcher);
  }
}
