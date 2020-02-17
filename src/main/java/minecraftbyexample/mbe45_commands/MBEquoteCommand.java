package minecraftbyexample.mbe45_commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;
import java.util.RandomAccess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.util.math.MathHelper.clamp;

/**
 * Created by TGG on 9/02/2020.
 *
 * Adds a command "mbequote"
 *  prints a variety of quotes depending on the arguments given
 *
 *  Shows a more complicated parsing structure, including mixing different types of literals and arguments
 *
 * mbequote python --> random Monty Python quote
 *
 * mbequote blues --> random Blues Brothers quote
 *
 * mbequote yogi bear --> random Yogi Bear quote
 * mbequote yogi berra --> random Yogi Berra quote
 * mbequote yogi --> "Which Yogi do you mean?"
 * mbequote yogi xxxx --> error
 *
 * mbequote gibberish <lettercount>  --> random bunch of letters, total length lettercount
 *
 * mbequote xxxx --> prints xxxxx (where xxxx is the text typed by the player)
 *
 * mbequote  --> Nothing to say!
 *
 * https://github.com/Mojang/brigadier for syntax
 *
 * My advice to you: be very careful laying out the builder chain.  It is very easy to put a bracket in the wrong place,
 *    and this completely throws the syntax out leading to strange parsing errors.
 *
 */
public class MBEquoteCommand {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    LiteralArgumentBuilder<CommandSource> mbequoteCommand
    = Commands.literal("mbequote")
         .requires((commandSource) -> commandSource.hasPermissionLevel(1))
         .then(Commands.literal("python")
                 .executes(commandContext -> sendMessage(commandContext, QuoteSource.MONTY_PYTHON.getQuote()))
              )
         .then(Commands.literal("blues")
                 .executes(commandContext -> sendMessage(commandContext, QuoteSource.BLUES_BROTHERS.getQuote()))
              )
         .then(Commands.literal("yogi")
                 .then(Commands.literal("bear")
                         .executes(commandContext -> sendMessage(commandContext, QuoteSource.YOGI_BEAR.getQuote()))
                      )
                 .then(Commands.literal("berra")
                         .executes(commandContext -> sendMessage(commandContext, QuoteSource.YOGI_BERRA.getQuote()))
                      )
                 .executes(commandContext -> sendMessage(commandContext, "Which Yogi do you mean?")) // no argument provided after "yogi"
              )
         .then(Commands.literal("gibberish")
                 .then(Commands.argument("lettercount", IntegerArgumentType.integer(1, 40))
                         .executes(commandContext -> sendMessage(commandContext,
                                                                 gibberish(IntegerArgumentType.getInteger(commandContext,
                                                                                                   "lettercount"))))
                      )
              )
         .then(Commands.argument("custommessage", MessageArgument.message())  // see also StringArgumentType; .word() or .string() or .greedystring()
                 .executes(commandContext -> {
                                 ITextComponent iTextComponent = MessageArgument.getMessage(commandContext, "custommessage");
                                 sendMessage(commandContext, iTextComponent.getFormattedText());
                                 return 1;
                                })
              )
         .executes(commandContext -> sendMessage(commandContext, "Nothing to say!"));  // blank: didn't match a literal or the custommessage argument

    dispatcher.register(mbequoteCommand);
  }

  static int sendMessage(CommandContext<CommandSource> commandContext, String message) throws CommandSyntaxException {
    TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
            commandContext.getSource().getDisplayName(), new StringTextComponent(message));

    commandContext.getSource().getServer().getPlayerList().sendMessage(finalText);
    return 1;
  }

  static String gibberish(int length) {
    Random random = new Random();
    length = clamp(length, 0, 40);
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; ++i) builder.append((char)('a'+ random.nextInt(26)));
    return builder.toString();
  }

  enum QuoteSource {
    MONTY_PYTHON(new String [] {"Nobody expects the Spanish Inquisition!",
                                "What sad times are these when passing ruffians can say 'Ni' at will to old ladies.",
                                "That's the machine that goes 'ping'.",
                                "Have you got anything without spam?",
                                "We interrupt this program to annoy you and make things generally more irritating.",
                                "My brain hurts!"}),
    YOGI_BERRA(new String [] {"When you come to a fork in the road…. take it.",
                              "It ain't over till it's over.",
                              "The future ain't what it used to be.",
                              "If the world was perfect, it wouldn't be."}),
    YOGI_BEAR(new String[] {"I'm smarter than the average bear."}),
    BLUES_BROTHERS(new String [] {"Four fried chickens and a Coke, please.",
                                  "It's 106 miles to Chicago, we've got a full tank, half pack of cigarettes, it's dark out, and we're wearing sunglasses. Hit it.",
                                  "Are you the police?  No ma'am, we're musicians."});

    public String getQuote() {
      return quotes[new Random().nextInt(quotes.length)];
    }

    QuoteSource(String [] quotes) {
      this.quotes = quotes;
    }

    private String [] quotes;
  }

}


