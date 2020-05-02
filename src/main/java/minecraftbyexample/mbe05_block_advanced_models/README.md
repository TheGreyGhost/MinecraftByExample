# MBE05_BLOCK_MULTILAYER_MODEL

This example shows 
* how to use the forge multilayer model extension
* how to use an animated texture

Normally, blocks can only be rendered in one layer (eg solid, or cutout, or translucent).  Multilayer can be used to
overcome this limitation.

Features of this example:
* uses multilayer to render a lantern with solid components and translucent components
* uses an animated texture 

Similar to vanilla, the lantern can either be hanging or non-hanging (resting on a solid surface)
The lantern can be 
* lit (with animated flame, yellow-tinted glass, and illuminates nearby blocks); or
* unlit (no flame, blue-tinted glass, no illumination of nearby blocks)

Some notes:
When rendered directly as an item, the multilayer model does not really work as expected.
If any of the layers is translucent, the model is rendered as translucent, otherwise it is rendered as cutout.
Your cutout textures may render correctly as translucent, except that backface culling is off in some views (3rd person).  This may cause flickering if you have used flat elements (eg similar to grass or bamboo leaves).  A possible solution is to use a separate item model where the flat elements only have one face (which can be seen from both sides), unfortunately some of the item views (eg 1st person Left hand, right hand) do use backface culling.  If you select the item json `display` transforms carefully you can ensure that only the front face of the element is visible in 1st person view; that's the approach I've taken with this example.

The item render model also uses gui_light=front to make the icon appear brighter when it's rendered in the hotbar or inventory

If you want maximum control over the appearance of your item, you will need to render it using an ItemStackTileEntityRenderer (see Item.Properties.setISTER).

The pieces you need to understand are located in:
* `StartupClientOnly` and `StartupCommon` classes
* `BlockGlassLantern`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe05a_block_glass_lantern_registry_name` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe05a_glass_lantern_****` -- where **** refers to the lantern state
* `resources\assets\minecraftbyexample\models\item\mbe05a_glass_lantern_registry_name` -- the model for rendering as an item
* `resources\assets\minecraftbyexample\textures\blocks\mbe05a_lantern_***.png` -- the textures used for the components
* `resources\assets\minecraftbyexample\textures\blocks\mbe05_lantern_animated_flame.png.mcmeta` -- file used to animate the flame

I used blockbench and GIMP to generate the lantern model and textures; the files are here (some of them in sub-directories):
[blockbench files](https://github.com/TheGreyGhost/MinecraftByExample/tree/master/miscellaneous/blockbench)

## Common errors

* Misspelled filenames and model resource locations.
* Most bugs will need you to set breakpoint in vanilla and inspect the registries to figure out what's going wrong.

Block appears as a black-and-purple square
* Misspelled filenames, model resource locations, texture locations, blockstate model names
* Your RenderTypeLookup.setRenderLayer lambda function returns true for a layer which isn't in your multilayer model json.

