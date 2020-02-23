# MBE01_BLOCK_SIMPLE

This example is just a very simple block--an ordinary solid cube with the six faces numbered from 0-5.

It will show you:

1. how to create a Block class and register it
1. how to define and register the model for rendering the block
1. how to set the type of rendering for the block (SOLID, CUTOUT, TRANSLUCENT etc)
1. how to define and register the item corresponding to the block (i.e. when the block is shown in the inventory, or in your hand)
1. how to make your block drop an item when you harvest it

The pieces you need to understand are located in:

* `StartupClientOnly` and `StartupCommon` classes
* `BlockSimple` class
* `MinecraftByExample` class
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe01_block_simple_registry_name` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe01_block_simple_model` -- for the model used to render the block
* `resources\assets\minecraftbyexample\textures\block\mbe01_***.png` -- textures used for the faces of the block. The particle texture, used for the flying shards when the block is destroyed, is based on the vanilla lapis lazuli block texture.
* `resources\assets\minecraftbyexample\models\item\mbe01_block_simple_registry_name` -- the model for rendering the block as an item (i.e. in your hand, in the inventory, when thrown on the ground)
* `resources\data\minecraftbyexample\loot_tables\blocks\mbe01_block_simple_registry_name` -- the loot table which makes the block drop an item when harvested

The block will appear in the Blocks tab in the creative inventory.

For background information on:

* blocks: see [https://greyminecraftcoder.blogspot.com/2020/02/blocks-1144.html](https://greyminecraftcoder.blogspot.com/2020/02/blocks-1144.html)
          or [forge blocks](https://mcforge.readthedocs.io/en/latest/blocks/blocks/)
* registering blocks: see [https://mcforge.readthedocs.io/en/latest/concepts/registries/#registering-things](https://mcforge.readthedocs.io/en/latest/concepts/registries/#registering-things) 
* rendering blocks: see [http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html](http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html) (the topics under the Block Rendering heading)
* render types: see [http://greyminecraftcoder.blogspot.com/2014/12/block-rendering-18.html](http://greyminecraftcoder.blogspot.com/2014/12/block-rendering-18.html) - the method for specifying the render type is different, but the concepts of the different types (SOLID, CUTOUT etc are the same)
* loot tables: see [https://minecraft.gamepedia.com/Loot_table](https://minecraft.gamepedia.com/Loot_table) - complicated!  See vanilla examples for guidance.

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. You've misspelled it
1. The upper/lower case doesn't match
1. You've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. The folder structure of your assets folders is incorrect

Blocks or Items don't register (don't appear in the game at all):
1. You've registered your event handlers on the wrong bus (see MinecraftByExample class for more detail)
1. You're registering MyEventHandler.class on the event bus, but your event handler methods aren't static.
  1. or... You're registering myEventHandler instance on the event bus, but your event handler methods are static.
1. You haven't specified a tab for the item, eg .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?

My block doesn't drop an item when I harvest it:
1. You have to harvest the block in survival mode (creative mode doesn't drop items when blocks are harvested)
1. You have to harvest the block using a suitable tool, depending on the Material you used to define your block and 
the harvestLevel and harvestTool Block Properties.  (eg: you need a pickaxe to mine Material.ROCK)
1. You've stored the loot table json in the wrong folder or given it a name that doesn't correspond to the block