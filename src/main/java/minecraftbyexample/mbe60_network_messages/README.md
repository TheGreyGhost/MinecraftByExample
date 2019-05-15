# MBE60_NETWORK_MESSAGES

The purpose of this example is to show how to send and receive network messages between the client and the server. It will show you:

1. how to create a network message (`IMessage`)
1. how to create a handler (`IMessageHandler`) that will process your message on the receiving side without crashing the game...
1. how to use the `SimpleNetworkWrapper` to register your messages and handler, and to send messages.

The most critical point to realise is that network messages are received in a network thread, not the client or server thread. This means that if your message handler code calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce. Instead, your `onMessage` handler should create a Runnable task which is later executed by the client or server thread during its normal tick(). See the `MessageHandler` classes in this example for more detail.

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly`
* `ItemAirStrike`
* `AirStrikeMessageToServer` and `MessageHandlerOnServer` -- messages from client to server
* `TargetEffectMessageToClient` and `MessageHandlerOnClient` -- messages from server to client

The basic protocol is:

1. right click using the item sends a message to the server telling it to bombard a target location with projectiles
1. when the message arrives at the server, it:
    1. spawns the projectiles above the target location.
    1. sends a message to all clients in that dimension, telling them to draw effects particles at the target location
1. when the 'draw effects' message arrives at the client, it spawns a number of `EntityParticleFX` at the specified location

The example uses some resources item rendering etc, these aren't the focus of this example, see example mbe10.

The item will appear in the Miscellaneous tab in the creative inventory.

For further information:

* dieSieben tutorial: [http://www.minecraftforge.net/forum/index.php/topic,20135.0.html](http://www.minecraftforge.net/forum/index.php/topic,20135.0.html)
* coolAlias example mod with more sophisticated network handler: [https://github.com/coolAlias/Tutorial-Demo/tree/master/src/main/java/tutorial](https://github.com/coolAlias/Tutorial-Demo/tree/master/src/main/java/tutorial)
* four posts explaining network messages in more detail: [http://greyminecraftcoder.blogspot.com.au/2015/01/the-client-server-division.html](http://greyminecraftcoder.blogspot.com.au/2015/01/the-client-server-division.html)
* general info on thread safety: [http://www.javaworld.com/article/2076747/core-java/design-for-thread-safety.html](http://www.javaworld.com/article/2076747/core-java/design-for-thread-safety.html)

## Common errors

* Getting the Side wrong, getting the packet ID wrong (missing, or duplicated).
* Referring to client-side vanilla objects in a server handler (this will appear to work fine in single player or a multiplayer hosted using "Open to LAN" but will cause a crash when installed on a dedicated server)

If you get this error:

```
[22:56:00] [Netty Local Client IO #0/ERROR] [FML]: SimpleChannelHandlerWrapper exception
..
Caused by: java.lang.InstantiationException: .....
..
Caused by: java.lang.NoSuchMethodException: ....
```

It is probably because you have forgotten to add a default constructor to your `IMessage`, eg:

```java
 public MyMessager() {
    //  do nothing... make an empty shell to be filled with fromBytes
 }
```
