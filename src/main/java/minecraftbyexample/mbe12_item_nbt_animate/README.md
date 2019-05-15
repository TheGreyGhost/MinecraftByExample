# MBE12_ITEM_NBT_ANIMATE

Based on code by Brandon3055

This example is an `Item` that stores extra information in NBT and also illustrates an animation technique designed for when you are "using" an item (eg similar to the way that the bow changes shape when you draw it before firing)

* When you shift-click with the item, it stores your current x,y,z location in NBT
* When the item has a stored waypoint, it has an enchanted "glint"
* When you then hold the use button for several seconds, there is a "charge up" animation, then it teleports you to the stored waypoint.

The example will show you:

1. how to store NBT within an item
1. how to add the enchanted "glint" to an item
1. how to change the item appearance while it is being used (similar to a bow)--using "overrides"
1. how to change the 'tool tips' for the item, i.e. the text that appears when your cursor hovers over the item

The pieces you need to understand are located in:

* `Startup`
* `ItemVariants`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the item variants
* `resources\assets\minecraftbyexample\models\item\mbe12_item_nbt_animate_xxx` -- for the models used to render the item variants
* `resources\assets\minecraftbyexample\textures\items\mbe12_item_nbt_animate_xxx.png` -- textures used for the item rendering

The item will appear in the Miscellaneous tab in the creative inventory.

For background information on:

* items: see [http://greyminecraftcoder.blogspot.com/2013/12/items.html](http://greyminecraftcoder.blogspot.com/2013/12/items.html)
* rendering items: see [http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html)
* NBT explorer--helpful tool for viewing NBT files
    * [http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac)
* Useful helper class for NBT:
    * [https://github.com/brandon3055/Draconic-Evolution/blob/master/src/main/java/com/brandon3055/draconicevolution/common/utills/ItemNBTHelper.java](https://github.com/brandon3055/Draconic-Evolution/blob/master/src/main/java/com/brandon3055/draconicevolution/common/utills/ItemNBTHelper.java)

Useful vanilla classes for further info:

`ItemStack.addEnchantment()`, `ItemPotion`

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect
1. If using IntelliJ 14--the assets isn't be copied to the right place, you need to apply a fix to your `build.gradle`, see [http://www.minecraftforge.net/forum/index.php/topic,21354.0.html](http://www.minecraftforge.net/forum/index.php/topic,21354.0.html)
1. You haven't properly registered with the `ModelBakery` and/or the `ItemModelMesher`
