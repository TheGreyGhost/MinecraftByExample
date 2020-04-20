# MBE11_ITEM_VARIANTS

This example is an Item with variants, illustrating two different ways of changing the rendering to suit the variant.

Note that- in general, if you have an Item with a few variants (say up to sixteen), you should implement these as separate items 
For example - see vanilla's GREEN_BED, BLACK_BED, RED_BED etc.  If you do that, then the techniques in this example are not required and you 
can just base your code on mbe10.

However if you have many different variants, or your item changes its appearance based on its state, for example:

* a magical wand that charges up over time and gradually changes its appearance; or
* a sword where you can independently customise the appearance of the blade, crossguard, and pommel
<br>, then the two methods in this example might be of interest.

The bottle has two types of information, both stored in the NBT data of the itemstack:

* Bottle contents (LIME, LEMON, CHERRY, ORANGE) - uses IItemColor to change the colour
* Bottle fullness (EMPTY, 25%, 50%, 75%, 100%) - uses custom BakedModel to change the shape

It will show you:

1. how to create an Item class with different variants and how to register them
1. how to control which variants are displayed on the creative tabs
1. how to control which rendering model is used for each variant
1. how to change the item description for each variant
1. how to use multiple layer rendering to change the appearance depending on the variant--in this case, changing the colour of the bottle contents using `IItemColor`

The item has two layers in the model file (also referred to as "tintindex" in the code):

* `Layer0` is the bottle outline, which is always black
* `Layer1` is the liquid contents, which change colour depending on the LIME, LEMON, CHERRY, ORANGE NBT data.

The pieces you need to understand are located in:

* `Startup`
* `ItemVariants`
* `LiquidColour`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the item variants
* `resources\assets\minecraftbyexample\models\item\mbe11_item_variants_xxx` -- for the models used to render the item variants
* `resources\assets\minecraftbyexample\textures\items\mbe11_item_variants_layerx_xxx.png` -- textures used for the item rendering in layer 0 and layer 1

The item will appear in the Brewing tab in the creative inventory.

For background information on:

* items: see [http://greyminecraftcoder.blogspot.com/2013/12/items.html](http://greyminecraftcoder.blogspot.com/2013/12/items.html)
* rendering items: see [http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html)

NOTE - I've used four colours as an example because it lets me demonstrate the IItemColor lambda interface.  But in general, if you had an Item with four colour variants, you should implement these as four separate items (LIME_BOTTLE, CHERRY_BOTTLE, etc) - see for example vanilla's GREEN_BED, BLACK_BED, RED_BED etc.

Useful vanilla classes for further info:

`PotionItem`, `SpawnEggItem', `DyeItem`, `ItemColors.init()`

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect

Rule of thumb:
If the item in your hand is a big purple-and-black cube, then your item model was not found or is missing (`resources\assets\minecraftbyexample\models\item\`).  Check the console for an error message that tells you where Forge is expecting to find your item model.
If the item in your hand is the right shape but is purple and black, then your item model is right but the texture file wasn't found.  No console error message appears in this case.

Items don't register (don't appear in the game at all):
1. You've registered your event handlers on the wrong bus (see MinecraftByExample class for more detail)
1. You're registering MyEventHandler.class on the event bus, but your event handler methods aren't static.
  1. or... You're registering myEventHandler instance on the event bus, but your event handler methods are static.
1. You haven't specified a tab for the item, eg .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
