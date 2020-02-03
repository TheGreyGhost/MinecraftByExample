# MBE01_BLOCK_SIMPLE

This example is just a very simple block--an ordinary solid cube with the six faces numbered from 0-5.

It will show you:

1. how to create a Block class and register it
1. how to define and register the model for rendering the block
1. how to define and register the item corresponding to the block (i.e. when the block is shown in the inventory, or in your hand)

The pieces you need to understand are located in:

* `Startup`
* `BlockSimple`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe01_block_simple` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe01_block_simple_model` -- for the model used to render the block
* `resources\assets\minecraftbyexample\textures\blocks\mbe01_***.png` -- textures used for the faces of the block. The particle texture, used for the flying shards when the block is destroyed, is based on the vanilla lapis lazuli block texture.
* `resources\assets\minecraftbyexample\models\item\mbe01_block_simple_model` -- the model for rendering the block as an item (i.e. in your hand, in the inventory, when thrown on the ground)

The block will appear in the Blocks tab in the creative inventory.

For background information on:

* blocks: see [http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html)
          or [forge blocks](https://documentation-tterrag.readthedocs.io/en/latest/blocks/blocks/)
* registering blocks: see [https://documentation-tterrag.readthedocs.io/en/latest/concepts/registries/#registering-things](https://documentation-tterrag.readthedocs.io/en/latest/concepts/registries/#registering-things) 
* rendering blocks: see [http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html](http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html) (the topics under the Block Rendering heading)

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. You've misspelled it
1. The upper/lower case doesn't match
1. You've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. The folder structure of your assets folders is incorrect

