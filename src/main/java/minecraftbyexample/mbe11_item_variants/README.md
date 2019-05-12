# MBE11_ITEM_VARIANTS

This example is an Item with variants, illustrating two different ways of changing the rendering to suit the variant.

The bottle has two types of information, both stored in the metadata / damage of the ItemStack:

* Bottle contents (LIME, LEMON, CHERRY, ORANGE)
* Bottle fullness (EMPTY, 25%, 50%, 75%, 100%)

It will show you:

1. how to create an Item class with different variants and how to register them
1. how to control which variants are displayed on the creative tabs
1. how to control which rendering model is used for each variant
1. how to change the item description for each variant
1. how to use multiple layer rendering to change the appearance depending on the variant--in this case, changing the colour of the bottle contents using `IItemColor`

The item has two layers in the model file (also referred to as "tintindex" in the code):

* `Layer0` is the bottle outline, which is always black
* `Layer1` is the liquid contents, which change colour depending on the LIME, LEMON, CHERRY, ORANGE metadata.

The pieces you need to understand are located in:

* `Startup`
* `ItemVariants`
* `LiquidColour`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the item variants
* `resources\assets\minecraftbyexample\models\item\mbe11_item_variants_xxx` -- for the models used to render the item variants
* `resources\assets\minecraftbyexample\textures\items\mbe11_item_variants_layerx_xxx.png` -- textures used for the item rendering in layer 0 and layer 1

The item will appear in the Miscellaneous tab in the creative inventory.

For background information on:

* items: see [http://greyminecraftcoder.blogspot.com/2013/12/items.html](http://greyminecraftcoder.blogspot.com/2013/12/items.html)
* rendering items: see [http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html)

Useful vanilla classes for further info:

`ItemPotion`, `ItemMonsterPlacer` (spawn egg), `ItemDye`, `ItemColors.init()`

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect
1. If using IntelliJ 14--the assets isn't be copied to the right place, you need to apply a fix to your `build.gradle`, see [http://www.minecraftforge.net/forum/index.php/topic,21354.0.html](http://www.minecraftforge.net/forum/index.php/topic,21354.0.html)
1. You haven't properly registered with the `ModelBakery` and/or the `ItemModelMesher`
