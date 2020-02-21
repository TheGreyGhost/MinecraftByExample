package minecraftbyexample.usefultools.debugging.commands;

import com.sun.javafx.geom.Vec2d;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nullable;

/**
 * Created by TGG on 21/02/2020.
 * Used in conjunction with the MBEdebugCommand
 *
 * /mbedebug trigger killallentities
 */
public class DebugTriggerWatcher {
  @SubscribeEvent
  public static void onServerTick(TickEvent.PlayerTickEvent event) {
    if (event.side != LogicalSide.SERVER) return;

    if (DebugSettings.getDebugTrigger("killallentities")) {
      PlayerEntity player = event.player;
      CommandSource commandSource = player.getCommandSource();
      String command = "/kill @e[type=!minecraft:player]";
      player.getServer().getCommandManager().handleCommand(commandSource, command);
    }
  }
}
