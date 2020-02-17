# MBE45_COMMANDS

This example shows how to write custom commands.

It will show you how to create a command and register it.

The pieces you need to understand are located in:

* `StartupCommon` and `ServerLifeCycleEvents` classes
* `MBEsayCommand` and `MBEquoteCommand` classes

The structure of the command is built up using a builder chain.  It can be quite confusing until you get the hang of it, 
  especially because the .executes method uses lambda functions which can look strange.
  
The basic concepts are:
1) Commands are broken up into tokens separated by spaces
2) Each token is either<br> 
  a literal (eg "time" or "say" or "day") that exactly matches what the player typed; or<br>
  an argument (eg a number or string or similar) that stores an argument the player typed<br>
  For example:<br>
  "time set 35" comprises: literal "time", literal "set", IntegerArgumentType 35<br>
3) Each .then() in the chain is an attempt to match the next token.  If it succeeds, the contents of the .then() are
     processed further.  If it fails, it proceeds to the next .then() in the chain.
4) The end of each chain is an .executes(), which uses a lambda to tell the command parser which function should be executed 
   when the end of that chain is reached.

There are a couple of other fancy features: 
* redirect() which is just an alias for an existing command (eg "tm" is redirected to "teammsg") 
* fork() which is used by the execute command to execute other commands.
* suggests() which is used to provide autocomplete suggestions to the user (eg a list of valid team names)
  
If you are creating a new command, it's very helpful to think of a vanilla command which is similar, and look at the
corresponding class.  (see Commands constructor for all registered commands)<br>

 My biggest advice to you: be very careful laying out the builder chain.  It is very easy to put a bracket in the wrong place,
 and this completely throws the syntax out leading to strange parsing errors.

For further background information on commands and the brigadier parser:
* [brigadier parser](https://github.com/Mojang/brigadier)
* [minecraft commands wiki](https://minecraft.gamepedia.com/Commands)

A good example from TimeCommand:
[minecraft time command](https://minecraft.gamepedia.com/Commands/time)
reformatted to make the structure clearer:

    dispatcher.register(
    Commands.literal("time")
    .requires((commandSource) -> {return commandSource.hasPermissionLevel(2);})
    
    // time set day
    // time set noon
    // time set night
    // time set midnight
    // time set <IntegerArgument>
    .then(Commands.literal("set")
            .then(Commands.literal("day")
                    .executes((commandSource) -> {return setTime(commandSource.getSource(), 1000);}) )
            .then(Commands.literal("noon")
                    .executes((commandSource) -> {return setTime(commandSource.getSource(), 6000);}) )
            .then(Commands.literal("night")
                    .executes((commandSource) -> {return setTime(commandSource.getSource(), 13000);}) )
            .then(Commands.literal("midnight")
                    .executes((commandSource) -> {return setTime(commandSource.getSource(), 18000);}) )
            .then(Commands.argument("time", TimeArgument.func_218091_a())
                    .executes((commandSource) -> {return setTime(commandSource.getSource(), 
                                                                 IntegerArgumentType.getInteger(commandSource, "time"));}) )
     )
    // if we didn't match "time set", then move on to 
    // time add <IntegerArgument> 
    .then(Commands.literal("add")
            .then(Commands.argument("time", TimeArgument.func_218091_a()) // func_218091_a() is timeArgument()
                    .executes((commandSource) -> {return addTime(commandSource.getSource(), IntegerArgumentType.getInteger(commandSource, "time"));}) )
         )
    
    // if we didn't match "time add", then move on to 
    // time query daytime
    // time query gametime
    // time query day
    .then(Commands.literal("query")
            .then(Commands.literal("daytime")
                    .executes((commandSource) -> {return sendQueryResults(commandSource.getSource(), 
                                                                          getDayTime(commandSource.getSource().getWorld()));}) )
            .then(Commands.literal("gametime")
                    .executes((commandSource) -> {return sendQueryResults(commandSource.getSource(), 
                                                                          (int)(commandSource.getSource().getWorld().getGameTime() % 2147483647L));}) )
            .then(Commands.literal("day")
                    .executes((commandSource) -> {return sendQueryResults(commandSource.getSource(), 
                                                                          (int)(commandSource.getSource().getWorld().getDayTime() / 24000L % 2147483647L));}) )
         )
    // if none of those matched, we fall through to here and get an error     
         
    );



## Common errors

Your command does nothing (doesn't appear in autocomplete): 
you haven't registered it properly.

Your parameters don't work as expected; gives unexpected errors:
You have a nested bracket in the wrong place in your command builder chain 