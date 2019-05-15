# MBE13_ITEM_TOOLS

This example is intended to provide an overview of the key ways you can control how tools work.  The logic is rather complicated and is described in more detail here: [http://greyminecraftcoder.blogspot.ch/2015/01/mining-blocks-with-tools.html](http://greyminecraftcoder.blogspot.ch/2015/01/mining-blocks-with-tools.html)

The example consists of a `Block`, `Item` and `Forge Event Handler`. The most relevant methods and Events for mining have been defined and logging code has been added to them. This allows you to see the sequence of calls that vanilla makes when mining blocks, which is difficult using the debugger because the debugger interrupts the game and stops the block mining.

The logging can be customised using the setup code in `Startup`.

In general:

* If possible, give your `Item` or `Block` the appropriate `ToolClasses`, `Materials`, `EFFECTIVE_ON Block Set`, etc to produce the desired mining speed and harvesting behaviour.

Otherwise:

* if you are adding a custom `Block`, override the appropriate `Block` methods.
* if you are adding a custom `Item`, override the appropriate `Item` methods.
* Avoid overriding the "high level" `Block` or `Item` methods if possible; use the lower-level methods instead. As a guide:
  * if the vanilla base method is not overridden by any vanilla classes, you probably shouldn’t either unless you have good reason.
* If you want to modify the behaviour of vanilla items on vanilla blocks, use `Events`.
