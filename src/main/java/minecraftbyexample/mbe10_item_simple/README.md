# MBE10_ITEM_SIMPLE

This example is just a very simple item--an ordinary two-dimension item that does nothing at all.

It will show you:

1. how to create an `Item` class and register it
1. how to define the model for rendering the `Item`

The pieces you need to understand are located in:

* `StartupClientOnly` and `StartupCommon` classes
* `ItemSimple` class
* `MinecraftByExample` class
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the item
* `resources\assets\minecraftbyexample\models\item\mbe10_item_simple_registryname` -- for the model used to render the item
* `resources\assets\minecraftbyexample\textures\item\mbe10_item_simple_icon.png` -- texture used for the item.

The item will appear in the Miscellaneous tab in the creative inventory.

For background information on:

* items: see [http://greyminecraftcoder.blogspot.com/2013/12/items.html](http://greyminecraftcoder.blogspot.com/2013/12/items.html)
* rendering items: see [http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html)  Old, but the concepts are still relevant.

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `item/mbe10_item_simple_icon` instead of 'minecraftbyexample:item/mbe10_item_simple_icon`
1. the folder structure of your assets folders is incorrect

Rule of thumb:
If the item in your hand is a big purple-and-black cube, then your item model was not found or is missing (`resources\assets\minecraftbyexample\models\item\`).  Check the console for an error message that tells you where Forge is expecting to find your item model.
If the item in your hand is the right shape but is purple and black, then your item model is right but the texture file wasn't found.  No console error message appears in this case.

Items don't register (don't appear in the game at all):
1. You've registered your event handlers on the wrong bus (see MinecraftByExample class for more detail)
1. You're registering MyEventHandler.class on the event bus, but your event handler methods aren't static.
  1. or... You're registering myEventHandler instance on the event bus, but your event handler methods are static.
1. You haven't specified a tab for the item, eg .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
