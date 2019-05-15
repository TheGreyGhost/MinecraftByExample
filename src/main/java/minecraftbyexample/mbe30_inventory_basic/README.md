# MBE30_INVENTORY_BASIC

Code by Brandon3055

This example shows how to make an inventory block similar to a chest, with less storage space (a Footlocker)

The example will show you:

1. how to create a simple `Block` with a linked `TileEntity` that will store items permanently
1. how to set up a `Container`, linked `GuiContainer`, and `IGuiHandler` to add/remove items from the `TileEntity`.

The Footlocker will appear in the Miscellaneous tab in the creative inventory.

The pieces you need to understand are located in:

* `Startup`
* `BlockInventoryBasic`
* `TileInventoryBasic`
* `ContainerBasic`
* `GuiInventoryBasic`
* `GuiHandlerMBE30`
* `resources\assets\minecraftbyexample\textures\gui\mbe30_inventory_basic_bg.png`

The example uses some other resources as well for block rendering etc, these aren't the focus of this example, see examples mbe01, mbe02 for more background.  This example doesn't do many of the fancy extras that vanilla chests do, for example--animation of the lid, multiple people accessing the chest at once, sound effects, facing north/south/east/west.

See other MBE examples or alternatively the vanilla classes for clues.

Useful vanilla classes for further info: `BlockChest`, `TileEntityChest`, `ContainerChest`, `GuiChest`

## Common errors

* "Array index out of bounds"--you have messed up the mapping of slot numbers to inventories
* Items appear in strange spots, or can't click on slots--you have messed up the [x,y] positions of the slots
