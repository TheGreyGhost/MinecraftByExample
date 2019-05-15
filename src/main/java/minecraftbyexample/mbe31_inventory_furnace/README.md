# MBE31_INVENTORY_FURNACE

Code by Brandon3055 and TheGreyGhost

This example shows how to make an inventory block with added functionality similar to a furnace (with a few extra slots). It builds on example MBE30 for a simple inventory block.

The example will show you:

1. how to create a `Block` with a linked `TileEntity` that will store items permanently
1. how to set up a `Container`, linked `GuiContainer`, and `IGuiHandler` to add/remove items from the `TileEntity`.
1. how to synchronise the `tileentity` between client and server
1. how to force block re-rendering and re-lighting when the TE state changes.

The TurboFurnace(TM) will appear in the Miscellaneous tab in the creative inventory.

The pieces you need to understand are located in:

* `Startup`
* `BlockInventoryFurnace`
* `TileInventoryFurnace`
* `ContainerInventoryFurnace`
* `GuiInventoryFurnace`
* `GuiHandlerMBE31`
* `resources\assets\minecraftbyexample\textures\gui\mbe30_inventory_furnace_bg.png`

The example uses some other resources as well for block rendering etc, these aren't the focus of this example, see examples mbe01, mbe02, mbe03 for more background.  This example doesn't do all of the furnace effects (eg smoke & flame). See the vanilla `BlockFurnace.randomDisplayTick()` for that.

The key to understanding how this example works is to keep in mind the client-server synchronisation that is required. The data (items in slots, and burning/cooking info) are all stored in the `TileEntity`.  There are two copies of the `TileEntity`, one on the client, and one on the server. They are kept synchronised as follows:

1. when loading the game from disk, the server must send the TileEntity information to the client.  This is done as follows:
    1. server calls `getUpdateTag()` to create an NBT containing the tileentity info. `writeToNBT()` is  used to create this NBT
    1. the packet is sent through the network to the client
    1. upon arrival at the client, it calls `handleUpdateTag()`, which then calls `readFromNBT()` which is used to populate the client TE data from the packet
1. when just one `tileentity` needs to be updated (not a whole chunk at once), then vanilla uses a different set of methods to do the synchronising:
    1. server calls `getUpdatePacket()` to create a packet containing the NBT info. `writeToNBT()` is used to create this NBT
    1. the packet is sent through the network to the client
    1. upon arrival at the client, it calls `onDataPacket()`, which then calls `readFromNBT()` which is used to populate the client TE data from the packet
1. When the furnace GUI is opened, two Containers are created linked to the TE. Manipulation of items in slots is handled automatically between the two containers, however other data such as fuel burn time must be manually synchronised. This is done using the tileentity "fields" (`getField`, `setField`)and the container methods `icrafting.sendWindowProperty()` and `container.updateProgressBar()`. If more than one player has the furnace open at the same time, the information is sent to each of them.
1. When the items in the slots change on the server, the `tileentity` is marked as "dirty" so that the updated information will be sent to all clients, to make sure they stay in synch, regardless of whether they have the container open or not. Caching where possible will help reduce the amount of network traffic and CPU time.

Useful vanilla classes for further info: `BlockFurnace`, `TileEntityFurnace`, `ContainerFurnace`, `GuiFurnace`

## Common errors

* "Array index out of bounds"--you have messed up the mapping of slot numbers to inventories using addSlotToContainer
* Items appear in strange spots, or can't click on slots--you have messed up the [x,y] positions of the slots
* Items disappear after loading and reloading--your NBT read/write code is wrong / mismatched
* Items don't work properly in their slots (eg won't burn)--the order you added the slots to your container using `addSlotToContainer` doesn't match the order of the items in the `TileEntityFurnace` `itemstack` array
* Container doesn't synchronise properly when loading game--`getUpdatePacket()`, `getUpdateTag()`, `onDataPacket()`, and/or `handleUpdateTag()` are probably wrong
