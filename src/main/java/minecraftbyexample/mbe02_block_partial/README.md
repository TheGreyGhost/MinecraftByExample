# MBE02_BLOCK_PARTIAL

This example is a simple block made of two parts that don't occupy the whole 1x1x1m space:

1. a "rectangular base" with the lapis lazuli texture
1. a "pole" with a stone texture

It will show you:

1. how to create a Block class for a block that isn't 1x1x1m, and register it
1. how to define and register the model for rendering the block when it has more than one part in the model
1. how to define and register the item corresponding to the block (i.e. when the block is shown in the inventory, or in your hand)

The pieces you need to understand are located in:

* `Startup`
* `BlockPartial`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe02_block_partial` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe02_block_partial_model` -- for the model used to render the block
* `resources\assets\minecraftbyexample\models\item\mbe02_block_partial_model` -- the model for rendering the block as an item (i.e. in your hand, in the inventory, when thrown on the ground)

The block will appear in the Blocks tab in the creative inventory.

For background information on:

* blocks: see [http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html)
* rendering blocks: see [http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html](http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html) (the topics under the Block Rendering heading)

## Common errors

"Missing Model", "Missing texture", etc. These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect
1. If using IntelliJ 14--the assets isn't be copied to the right place, you need to apply a fix to your build.gradle, see [http://www.minecraftforge.net/forum/index.php/topic,21354.0.html](http://www.minecraftforge.net/forum/index.php/topic,21354.0.html)

The model has the wrong shape or is textured strangely:

1. your model file is wrong! `ModelBakery.bakeModel` is a useful place for a breakpoint (with a condition on `modelBlockIn.name`) to check whether the model has been read in as you expect.
