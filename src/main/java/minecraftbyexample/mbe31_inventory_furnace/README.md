# MBE31_INVENTORY_FURNACE

Code by Brandon3055 and TheGreyGhost

This example shows how to make an inventory block with added functionality similar to a furnace (with a few extra slots). It builds on example MBE30 for a simple inventory block.

The example will show you:

1. how to create a `Block` with a linked `TileEntity` that will store items permanently
1. how to set up a `ContainerType`, `Container`, and `ContainerScreen`, to add/remove items from the `TileEntity`.
1. how to synchronise the `Container between client and server
1. how to force block re-rendering and re-lighting when the TE state changes.

The TurboFurnace(TM) will appear in the Miscellaneous tab in the creative inventory.

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly` - used for registering the various components
* `BlockInventoryFurnace`
* `TileEntityFurnace` - permanent storage of items (ie on disk), some logic for smelting
* `ContainerFurnace`  - used to collate items together into one place (eg from player inventory and furnace inventory)
* `ContainerScreenFurnace` - used to draw the GUI for the container
* `FurnaceZoneContents` - used to store the items which are in the input zone, fuel zone, and output zone
* `FurnaceStateData` - used to track the furnace state (cook time, burn time for each slot)
* `resources\assets\minecraftbyexample\textures\gui\mbe30_inventory_furnace_bg.png`

See ReadMeFurnaceDataFlow.png for a diagram of the dataflow between the classes.


The example uses some other resources as well for block rendering etc, these aren't the focus of this example, see examples mbe01, mbe02, mbe03 for more background.  This example doesn't do all of the furnace effects (eg smoke & flame). See the vanilla `FurnaceBlock.animateTick()` for that.

The key to understanding how this example works is to keep in mind the client-server synchronisation that is required. On the server, the data (items in slots, and burning/cooking info) are all stored in the `TileEntity`.  There is also a client TileEntity.

1. when loading the game from disk, the server must send the TileEntity information to the client.  This is done as follows:
    1. server calls `getUpdateTag()` to create an NBT containing the tileentity info. `writeToNBT()` is  used to create this NBT
    1. the packet is sent through the network to the client
    1. upon arrival at the client, it calls `handleUpdateTag()`, which then calls `readFromNBT()` which is used to populate the client TE data from the packet
1. when just one `tileentity` needs to be updated (not a whole chunk at once), then vanilla uses a different set of methods to do the synchronising:
    1. server calls `getUpdatePacket()` to create a packet containing the NBT info. `writeToNBT()` is used to create this NBT
    1. the packet is sent through the network to the client
    1. upon arrival at the client, it calls `onDataPacket()`, which then calls `readFromNBT()` which is used to populate the client TE data from the packet
    
The synchronisation for the GUI is different.  It takes place through two Containers, one on the server and one on the client:   
1. When the furnace GUI is opened, two Containers are created.  The Container on the server is linked to the TE. The Container on the client is NOT linked to a TE.  It is synchronised with the server container instead.  Manipulation of items in slots is handled automatically between the two containers, as well as other data such as fuel burn time, using `trackedInt`s. 
If more than one player has the furnace open at the same time, the information is sent to each of them.
1. When the items in the slots change on the server, the `tileentity` is marked as "dirty" so that the updated information will be sent to all clients, to make sure they stay in synch, regardless of whether they have the container open or not. Caching where possible will help reduce the amount of network traffic and CPU time.

Useful vanilla classes for further info: `AbstractFurnaceBlock`, `AbstractFurnaceTileEntity`, `AbstractFurnaceContainer`, `AbstractFurnaceScreen`

See also
* Background information on [Containers](http://greyminecraftcoder.blogspot.com/2020/04/containers-1144.html)

## Futher notes
Depending on your container, there are a few other interfaces you might want to implement-
IRecipeHolder, IRecipeHelperPopulator, IRecipeShownListener, RecipeBookContainer - for vanilla's recipe helper
ISidedInventoryProvider - if you want a hopper to be able to draw items from your furnace

## Common errors

* "Array index out of bounds"--you have messed up the mapping of slot numbers to inventories using addSlotToContainer
* Items appear in strange spots, or can't click on slots--you have messed up the [x,y] positions of the slots; you may have forgotten to offset by guiLeft and guiTop
* Items disappear after loading and reloading--your NBT read/write code is wrong / mismatched
* Items don't work properly in their slots (eg won't burn)--the order you added the slots to your container using `addSlotToContainer` doesn't match the order of the items in the `TileEntityFurnace` `itemstack` array
* Container doesn't synchronise properly when loading game--`getUpdatePacket()`, `getUpdateTag()`, `onDataPacket()`, and/or `handleUpdateTag()` are probably wrong
