package minecraftbyexample.usefultools.debugging.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import minecraftbyexample.mbe45_commands.MBEquoteCommand;
import minecraftbyexample.mbe45_commands.MBEsayCommand;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by TGG on 11/02/2020.
 *
 * Adds commands to assist in-game debugging:
 *
 * 1) mbedebug param <parametername> <parametervalue>
 *   sets the given debug  parameter to the given value
 *      /mbedebug param x 0.3
 *   which can then be subsequently used for (eg) interactively adjusting rendering offsets in-game
 *     double renderOffsetX = Debugsettings.getDebugParameter("x");
 * 2) mbedebug paramvec3d <parametername> <parametervalue>
 *    as per mbedebug param, but the parameter is a Vec3d [x,y,z]
 * 3) mbedebug trigger <triggername>
 *    triggers an action, eg
 *      /mbedebug trigger killallentities
 *    which a subsequent code can check for
 *      if (Debugsettings.getDebugTrigger("killallentities")) killAllEntities();
 * 4) mbedebug test <testnumber>
 *    executes a debug test eg
 *      /mbedebug test 34
 *    which a subsequent code can check for
 *      int testNumber = Debugsettings.getDebugTest(); if (test != 0) runTest(testNumber);
 *
 * Must be registered in FMLServerStartingEvent;
 *
 */
public class MBEdebugCommand {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    if (dispatcher.findNode(Arrays.asList("mbedebug")) != null) return;
    LiteralArgumentBuilder<CommandSource> mbedebugCommand
      = Commands.literal("mbedebug")
            .then(Commands.literal("param")
                    .then(Commands.argument("parametername", StringArgumentType.word())
                            .suggests((context, builder) ->
                                    ISuggestionProvider.suggest(DebugSettings.listAllDebugParameters().stream(), builder))
                            .then(Commands.literal("clear")
                                    .executes(context -> {
                                      DebugSettings.clearDebugParameter(StringArgumentType.getString(context, "parametername"));
                                      return 1;
                                    })
                            )
                            .then(Commands.argument("parametervalue", DoubleArgumentType.doubleArg())
                                    .executes(context -> { DebugSettings.setDebugParameter(
                                              StringArgumentType.getString(context, "parametername"),
                                              DoubleArgumentType.getDouble(context, "parametervalue")); return 1;}))
                    )
            )
            .then(Commands.literal("paramvec3d")
                    .then(Commands.argument("parametername", StringArgumentType.word())
                            .suggests((context, builder) ->
                                    ISuggestionProvider.suggest(DebugSettings.listAllDebugParameterVec3ds().stream(), builder))
                            .then(Commands.literal("clear")
                                    .executes(context -> {
                                      DebugSettings.clearDebugParameterVec3d(StringArgumentType.getString(context, "parametername"));
                                      return 1;
                                    })
                            )
                            .then(Commands.argument("parametervalue", Vec3Argument.vec3(false))  //don't automatically centre integers
                              .executes(context -> { DebugSettings.setDebugParameterVec3d(
                                      StringArgumentType.getString(context, "parametername"),
                                      Vec3Argument.getVec3(context, "parametervalue")); return 1;}))
                    )
            )
            .then(Commands.literal("trigger")
                    .then(Commands.argument("parametername", StringArgumentType.word())
                            .suggests((context, builder) ->
                                    ISuggestionProvider.suggest(DebugSettings.listAllDebugTriggers().stream(), builder))
                            .executes(context -> { DebugSettings.setDebugTrigger(
                                    StringArgumentType.getString(context, "parametername")); return 1;})
                    )
            )
            .then(Commands.literal("test")
                        .then(Commands.argument("testnumber", IntegerArgumentType.integer())
                          .executes(context -> { DebugSettings.setDebugTest(
                                  IntegerArgumentType.getInteger(context, "testnumber")); return 1;}))
            );
    dispatcher.register(mbedebugCommand);
  }
}
