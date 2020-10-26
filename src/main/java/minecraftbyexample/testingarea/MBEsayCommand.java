//package minecraftbyexample.testingarea;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.builder.ArgumentBuilder;
//import com.mojang.brigadier.builder.LiteralArgumentBuilder;
//import com.mojang.brigadier.builder.RequiredArgumentBuilder;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.command.CommandSource;
//import net.minecraft.command.Commands;
//import net.minecraft.command.arguments.MessageArgument;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
//import org.lwjgl.system.CallbackI;
//
///**
// * Created by TGG on 9/02/2020.
// */
//public class MBEsayCommand {
//  public static void register(CommandDispatcher<CommandSource> dispatcher) {
//
//
//
//    dispatcher.register(Commands.literal("mbesay1")
//                        .requires((commandSource) -> {return commandSource.hasPermissionLevel(2); })
//                        .then(Commands.argument("message", MessageArgument.message())
//                                .executes((commandContext) -> {
//                                          ITextComponent itextcomponent = MessageArgument.getMessage(commandContext, "message");
//                                          commandContext.getSource().getServer().getPlayerList().sendMessage(
//                                                  new TranslationTextComponent("chat.type.announcement",
//                                                          commandContext.getSource().getDisplayName(), itextcomponent));
//                                          return 1;
//                                        }
//                                )));
//
//    LiteralArgumentBuilder<CommandSource> rootKeyword = Commands.literal("mbesay2");
//    RequiredArgumentBuilder messageKeyword = Commands.argument("message", MessageArgument.message())
//                                                     .executes(commandContext-> sendMessage(commandContext));
//
//    LiteralArgumentBuilder<CommandSource> fullCommand = rootKeyword
//              .requires((commandSource) -> {return commandSource.hasPermissionLevel(2); })
//              .then(messageKeyword);
//    dispatcher.register(fullCommand);
//  }
//
//  static int sendMessage(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
//    ITextComponent itextcomponent = MessageArgument.getMessage(commandContext, "message");
//    commandContext.getSource().getServer().getPlayerList().sendMessage(
//            new TranslationTextComponent("chat.type.announcement",
//                    commandContext.getSource().getDisplayName(), itextcomponent));
//    return 1;
//  }
//
//}
//
///*
//    .execute ends the node
//
// */
//
