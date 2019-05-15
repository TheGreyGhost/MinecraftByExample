# MBE21_TILEENTITYSPECIALRENDERER

This example shows how to use a `TileEntitySpecialRenderer`. It uses a simple block and a `TileEntity` to store the animation parameters.

It will show you:

1. how to create a TESR and bind it to a `TileEntity`
1. how to correctly translate so that your `TileEntity` renders in the correct place
1. how to save and restore the rendering settings so that subsequent renders by vanilla aren't affected
1. how to set the typical settings for rendering

The pieces you need to understand are located in:

* `Startup`
* `TileEntityMBE21`
* `TileEntitySpecialRendererMBE21`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe21_tesr_block.json` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\item\mbe21_tesr_block.json` -- the model for rendering the item
* `resources\assets\minecraftbyexample\textures\items\mbe21_tesr_item_icon.png` -- item icon
* `resources\assets\minecraftbyexample\entity\items\mbe21_tesr_gem.png` -- texture used for the gem

The block will appear in the Blocks tab in the creative inventory.

For background information on:

* OpenGL rendering--see [http://www.glprogramming.com/red/](http://www.glprogramming.com/red/)

## Common errors

"Missing Model", "Missing texture", etc:

These are caused when you have specified a filename or path which is not correct, typically:

1. you've misspelled it
1. the upper/lower case doesn't match
1. you've forgotten the resource domain, eg `blockmodel` instead of `minecraftbyexample:blockmodel`
1. the folder structure of your assets folders is incorrect
1. If using IntelliJ 14--the assets isn't be copied to the right place, you need to apply a fix to your `build.gradle`, see [http://www.minecraftforge.net/forum/index.php/topic,21354.0.html](http://www.minecraftforge.net/forum/index.php/topic,21354.0.html)

Rendering doesn't look right--this is a relatively complex subject and can be difficult to troubleshoot. Generally:

The object is in the wrong spot, or doesn't rotate how you expect:

1. your translation is wrong
1. your order of transformations (rotations, translations, scaling etc) are wrong. See Chapter 3 OpenGL red book: [http://www.glprogramming.com/red/chapter03.html](http://www.glprogramming.com/red/chapter03.html)

If the object is too light or too dark:

1. one or more of the openGL settings is incorrect (especially `GL_LIGHTING`)
1. the multitexturing "brightness" (blocklight/skylight) is not right (see `OpenGlHelper.setLightmapTextureCoords`)
1. your `GlStateManager.color(red, green, blue)` is wrong

You can't see parts of your object from one side:

1. If you want your faces to be visible on both sides, you need to disable `GL_CULL_FACE`.
1. If your face should be one-sided, but is facing the wrong way, you need to reverse the order of the points for that face. For example, you have specified points in the order A, B, C, D --> change the order to D, C, B, A
