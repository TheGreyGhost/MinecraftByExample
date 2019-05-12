# MBE05_BLOCK_DYNAMIC_BLOCK_MODEL2

Based on code by Herbix.

This example shows how to use `IBakedModel#getQuads` in conjunction with `ModelLoader`. It is very similar to MBE04, it just uses a different method of injecting your `IBakedModel` into the registry, and assembles the quads differently.

Guidelines on choosing which injection method to use:

ModelLoader:
The ModelLoader can be useful if your IBakedModel is composed of a number of subcomponents and you want to use a `model.json` for each subcomponent. It is also well-suited if you are parsing your own custom model definitions (similar to the `B3DModelLoader`).

ModelBakeEvent:
The `BakeEvent` is more useful if you want to copy an existing model, or to generate your model's quads algorithmically.

How it works:

1. `ModelLoader.setCustomStateMapper` is used to set up a custom `StateMapper` for `Block3DWeb`.  This `StateMapper` tells vanilla to use `ModelResourceLocation` `mbe05_block_3d_web_statemapper_name` for rendering, regardless of the block's state.
1. In `mbe05_block_3d_web.json`, the model `ResourceLocation` is set to `minecraftbyexample:smartmodel/webmodel`
1. The custom `ModelLoader` `ModelLoader3DWeb`, which was previously registered using `ModelLoaderRegistry.registerLoader()`, recognises the smartmodel tag and creates a `WebModel` (implements `IModel`).
1. The `IModel` (`WebModel`) is then "baked" to form an `IBakedModel`. During baking, the `WebModel` loads and bakes the models for each of the seven subcomponents (a central "core" plus one model for each of the six strands that can extend up, down, east, west, north, or south).  Each of these models is defined using vanilla `.json` model and a blockstates file as usual.  (The blockstates file is necessary to get vanilla to load the model)
1. During the render, `Block3DWeb.getExtendedBlockState` is called, which looks at the block's neighbours to see which strands should be drawn (a strand is drawn to each solid neighbour). The yes/no for each strand is then stored in the `IExtendedBlockState`.
1. The `IExtendedBlockState` is passed to the `IBakedModel`, which generates a list of quads from the sub-component models (including or excluding each one depending on which directions have strands) and then returns it to the caller for rendering.

BEWARE! Rendering is multithreaded so your `IBakedModel` must be thread-safe, preferably Immutable.

The 3d web block appears in the Blocks tab in the creative inventory.

The pieces you need to understand are located in:
* `StartupClientOnly` and `StartupCommon`
* `Block3DWeb`
* `CompositeModel`
* `ModelLoader3DWeb`
* `WebModel`
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe05_block_3d_web_statemapper_name` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe05_block_web_****_model` -- where **** is eg core, up, down, etc (sub-component models)
* `resources\assets\minecraftbyexample\blockstates\mbe05_web_subblocks\mbe05_block_web_****_model` -- for the blockstates definition for the sub-components
* `resources\assets\minecraftbyexample\models\item\mbe05_block_3D_web` -- the model for rendering as an item
* `resources\assets\minecraftbyexample\textures\blocks\mbe05_block_3D_web` -- the base texture used for all components

More background information:
* blocks: see [http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html)
* rendering blocks: see [http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html](http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html) (the topics under the Block Rendering heading)
* test code for `ISmartBlockModel` which generates the model quads using an algorithm instead of using a pre-existing model
  [https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/net/minecraftforge/debug/ModelBakeEventDebug.java](https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/net/minecraftforge/debug/ModelBakeEventDebug.java)
* more explanation on `ISmartBlockModel`: [http://www.minecraftforge.net/forum/index.php/topic,28714.0.html](http://www.minecraftforge.net/forum/index.php/topic,28714.0.html)

Useful vanilla classes to look at (for `BlockStateMapping`): `BlockModelShapes.registerAllBlocks()`

## Common errors

* Misspelled filenames and model resource locations.
* Most bugs will need you to set breakpoint in vanilla and inspect the registries to figure out what's going wrong.

Good places for a breakpoint:

* `BlockModelShapes.reloadModels()`
* `BlockStateMapper.putAllStateModelLocations()`
* `BlockRendererDispatcher.getModelForState()`
* `YourCustomBlock.getActualState()` -- called just before render--can add a dummy method specially for use by breakpoint

A conditional breakpoint will often be very useful to break when your block is being processed, for example:

```java
BlockRendererDispatcher::
  public IBakedModel getModelForState(IBlockState state)
    {
      // conditional breakpoint here on state.getblock().getUnlocalisedName().contains("partofmyblockname")
```
