# MBE08_CREATIVE_TAB

Code by Nephroid

This example adds two "ItemGroups" to the vanilla tabs:

1. an ordinary tab which uses the Item / Blocks `Item.Properties.group()`
1. an 'advanced' tab which displays all items and blocks belonging to this mod, by looking at the item/block name

It will show you how to add an ItemGroup for organising your custom blocks and items.

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly`
* `AllMbeItemsItemGroup`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed names of the ItemGroup.  Note the absence of minecraftbyexample in the tag.

The example also uses some files in `resources\assets\minecraftbyexample` to draw the test block and item, these aren't relevant to the example. See examples mbe01 and mbe10.

For background information see:
* how to sort your items in a specific order: [http://www.minecraftforge.net/forum/index.php/topic,23782.0.html](http://www.minecraftforge.net/forum/index.php/topic,23782.0.html)
