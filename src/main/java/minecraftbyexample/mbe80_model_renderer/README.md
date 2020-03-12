# MBE80_MODEL_RENDERER

This example shows some of the basics of Models (such as the PigModel)

It uses a Model which can be adjusted in real time using commands; for example the size of the block can be adjusted using<br>
`/mbedebug paramvec3d parent_block_dimensions 12 8 12`

The model is rendered with two crosshairs to show where the origin of the model is located:
* Red crosshairs for the origin of the model
* Blue crosshairs for the origin of the model in world coordinates (when rendered as an entity)
See screenshots [here](https://greyminecraftcoder.blogspot.com/2020/03/minecraft-model-1144.html) for examples on what it looks like 

Usage:
1. Place the TestModel block in the world
2. Right click on the block.  The interactive parameters are printed to the debug console.
3. use `/mbedebug param` or `/mbedebug paramvec3d` as appropriate to set the parameters of interest

-------------

The Model is rendered in two ways:
1. Using a TileEntityRenderer with a simple block and a TileEntity (for more details see mbe21)
2. Using an EntityRenderer

The example will show you:

1. how to construct a simple Model 
1. how to render a Model from a TileEntityRenderer

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly`
* `TestModel`
* `TestModelTileEntityRenderer`
* `resources\assets\minecraftbyexample\textures\model\mbe80_test_model_texture.png` -- texture used for the model

There are a number of supporting files for the example which are explained in earlier mbe examples.
* `BlockMBE80` and `TileEntityMBE80`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe80_testmodel_block_registry_name.json`
* `resources\assets\minecraftbyexample\models\item\mbe80_testmodel_block_registry_name.json` - for rendering the item
* `resources\assets\minecraftbyexample\textures\item\mbe80_testmodel_icon.png` -- item icon

The block will appear in the BuildingBlocks tab in the creative inventory.

For background information on:
* Models - see [Minecraft Model Basics](https://greyminecraftcoder.blogspot.com/2020/03/minecraft-model-1144.html) 
* OpenGL rendering--see [http://www.glprogramming.com/red/](http://www.glprogramming.com/red/)

Useful vanilla classes to look at:
* PigModel, BatModel, MinecartModel
* PigRenderer, BatRenderer, MinecartRenderer
* LivingRenderer::render() 

A useful tool for making minecraft models is [Blockbench](https://blockbench.net/blog/).  You can construct models manually for simple models, but BlockBench makes it much easier and faster to get it right.

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect

Rendering doesn't look right--this is a relatively complex subject and can be difficult to troubleshoot. Generally:
The object is in the wrong spot, or doesn't rotate how you expect:

1. your translation is wrong
1. your order of transformations (rotations, translations, scaling etc) are wrong. See Chapter 3 OpenGL red book: [http://www.glprogramming.com/red/chapter03.html](http://www.glprogramming.com/red/chapter03.html)
1. you are trying to transform in world coordinate space rather than the model (Vanilla inverts the x-axis and y-axis for entities, and also moves the origin up by 1.5 in world space)

If the object is too light or too dark:
1. you have chosen the wrong render buffer type
1. the multitexturing "brightness" (combinedLight blocklight/skylight) is not right
1. your blending colour is not right; (usually - use white); you may have mixed up integer colour with float colour i.e. in some places colour is an int from 0->255, others it is a float from 0.0->1.0

If the texture is wrong:
1. your model's textureWidth and/or textureHeight don't match the actual size of the texture file you provided
2. your texture sheet is not laid out properly
3. you haven't applied the vanilla entity transformation (invert x-axis and y-axis)
