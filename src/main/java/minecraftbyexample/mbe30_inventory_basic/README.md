# MBE30_INVENTORY_BASIC

Code by Brandon3055 and TGG, with excerpts from Botania mod https://github.com/Vazkii/Botania
Also contains examples of using an item (eg ItemFlowerBag) to store multiple items within itself.

This example shows how to make an inventory block similar to a chest, with less storage space (a Footlocker)

The example will show you:

1. how to create a simple `Block` with a linked `TileEntity` that will store items permanently
1. how to set up a `ContainerType`, `Container`, and `ContainerScreen`, to add/remove items from the `TileEntity`.

The Footlocker will appear in the Miscellaneous tab in the creative inventory.

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly` - used for registering the various components
* `BlockInventoryBasic`
* `TileInventoryBasic` - permanent storage of items (ie on disk)
* `ChestContents` - stores the items and is used by the container to communicate back to the tileentity
* `ContainerBasic` - used to collate items together into one place (eg from player inventory and chest inventory)
* `ContainerScreenBasic` - used to draw the GUI for the container
* `resources\assets\minecraftbyexample\textures\gui\mbe30_inventory_basic_bg.png`

The lifecycle of the Container is reasonably complicated, see [here](http://greyminecraftcoder.blogspot.com/2020/04/containers-1144.html) for more information.<br>
The key points are:<br>
During placement of the chest:

1. Block creates a tileEntity
1. TileEntity constructs a member variable(chestContents) to permanently store the ItemStacks 

When the block is clicked to open the GUI:
1. Block calls openGUI and gives it a NamedContainerProvider (in this case: the TileEntity)
1. openGUI asks the TileEntity to create a server-side Container which is linked to the TileEntity.  The TileEntity does this
   by creating a container and giving it a reference to its chestContents.  The container uses chestContents to directly
   manipulate the stored ItemStacks, and also to send notifications to the TileEntity when the inventory has been changed.
1. Meanwhile, the server sends a packet to the client telling it which ContainerType is being opened.
1. When the client receives the packet, it uses the registered ContainerType to construct a temporary Container, i.e. one that isn't
   linked to a TileEntity (the Container is constructed using an empty chestContents whose notifications are ignored)
1. The client also constructs a Screen using the registered ScreenManager.IScreenFactory for that ContainerType.    

The example uses some other resources as well for block rendering etc, these aren't the focus of this example, see examples mbe01, mbe02 for more background.  This example doesn't do many of the fancy extras that vanilla chests do, for example--animation of the lid, multiple people accessing the chest at once, sound effects, facing north/south/east/west.
See other MBE examples or alternatively the vanilla classes for clues.

See also
* Background information on [Containers](http://greyminecraftcoder.blogspot.com/2020/04/containers-1144.html)


## Common errors

* "Array index out of bounds"--you have messed up the mapping of slot numbers to inventories
* Items appear in strange spots, or can't click on slots--you have messed up the [x,y] positions of the slots
