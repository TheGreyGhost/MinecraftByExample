# MBE10_ITEM_SIMPLE

This example is just a very simple item--an ordinary two-dimension item that does nothing at all.

It will show you:

1. how to create an `Item` class and register it
1. how to define and register the model for rendering the `Item`

The pieces you need to understand are located in:

* `Startup`
* `ItemSimple`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the item
* `resources\assets\minecraftbyexample\models\item\mbe10_item_simple` -- for the model used to render the item
* `resources\assets\minecraftbyexample\textures\items\mbe10_item_simple_icon.png` -- texture used for the item.

The item will appear in the Miscellaneous tab in the creative inventory.

For background information on:

* items: see [http://greyminecraftcoder.blogspot.com/2013/12/items.html](http://greyminecraftcoder.blogspot.com/2013/12/items.html)
* rendering items: see [http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html)

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect
1. If using IntelliJ 14--the assets aren't copied to the right place, you need to apply a fix to your `build.gradle`, see [http://www.minecraftforge.net/forum/index.php/topic,21354.0.html](http://www.minecraftforge.net/forum/index.php/topic,21354.0.html)
