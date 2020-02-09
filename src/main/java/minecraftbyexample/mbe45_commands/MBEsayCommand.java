package minecraftbyexample.mbe45_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by TGG on 9/02/2020.
 *
 * Exactly the same as the vanilla "say" command except that the words are converted to pig latin
 *
 * https://www.geeksforgeeks.org/encoding-word-pig-latin/
 *
 *
 */
public class MBEsayCommand {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("mbesay1")
                        .requires((commandSource) -> {return commandSource.hasPermissionLevel(2); })
                        .then(Commands.argument("message", MessageArgument.message())
                                .executes((commandContext) -> {
                                          ITextComponent itextcomponent = MessageArgument.getMessage(commandContext, "message");
                                          commandContext.getSource().getServer().getPlayerList().sendMessage(
                                                  new TranslationTextComponent("chat.type.announcement",
                                                          commandContext.getSource().getDisplayName(), itextcomponent));
                                          return 1;
                                        }
                                )));

    LiteralArgumentBuilder<CommandSource> rootKeyword = Commands.literal("mbesay2");
    RequiredArgumentBuilder messageKeyword = Commands.argument("message", MessageArgument.message())
                                                     .executes(commandContext-> sendMessage(commandContext));

    LiteralArgumentBuilder<CommandSource> fullCommand = rootKeyword
              .requires((commandSource) -> {return commandSource.hasPermissionLevel(2); })
              .then(messageKeyword);
    dispatcher.register(fullCommand);
  }

  static int sendPigLatinMessage(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
    ITextComponent itextcomponent = MessageArgument.getMessage(commandContext, "message");
    String unformattedText = itextcomponent.getString();
    String pigifiedText = convertParagraphToPigLatin(unformattedText);
    commandContext.getSource().getServer().getPlayerList().sendMessage(
            new TranslationTextComponent("chat.type.announcement",
                    commandContext.getSource().getDisplayName(), itextcomponent));
    return 1;
  }

  static String convertParagraphToPigLatin(String s) {


  }

  static boolean isVowel(char c) {
    return (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' ||
            c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');
  }

  static String convertWordToPigLatin(String s) {
    // the index of the first vowel is stored.
    int len = s.length();
    int index = -1;
    for (int i = 0; i < len; i++) {
      if (isVowel(s.charAt(i))) {
        index = i;
        break;
      }
    }

    // Pig Latin is possible only if vowels
    // is present
    if (index == -1) return s;

    // Take all characters after index (including
    // index). Append all characters which are before
    // index. Finally append "ay"
    return s.substring(index) + s.substring(0, index) + "ay";
  }
}


