# MBE04_BLOCK_DYNAMIC_BLOCK_MODEL1

Based on code by Herbix and RainWarrior.

This example shows how to use `IBakedModel#getQuads` to dynamically generate a model in conjunction with `ModelBakeEvent`.

Normal blocks can only have up to 16 different states, and you need to create a blockstates JSON file which handles every single state. But what if you want a block which needs to display many more possibilities? For example a wool block with 100 different colours, or a camouflage block which copies the texture of the adjacent block? In these cases you can use either `IBakedModel#getQuad` or a `TileEntitySpecialRenderer`. Which to choose?

TileEntitySpecialRenderer is often a better (simpler!) choice if the block is animated, or needs fancy rendering, for example like an animated chest, or the lettering on a sign.  You must give your block a `TileEntity` as well.
`IBakedModel#getQuads` is often a better (more realistic) choice if the block should have shaded lighting like a normal block, or if there are very many of the block and they don't need to store information (can calculate their state from adjacent blocks). For example, a chameleon block that changes its colour to match the colours of its neighbours.

This example shows the basics of using `IBakedModel#getQuads` to dynamically generate a model.

How it works:

1. `ModelLoader.setCustomStateMapper` is used to set up a custom `StateMapper` for `BlockCamouflage`. This `StateMapper` tells vanilla to use `ModelResourceLocation` `mbe04_block_camouflage` for rendering, regardless of the block's state.
1. The vanilla loader loads the model for `mbe04_block_camouflage` using the blockstates json and model json, as per normal blocks
1. The `ModelBakeEvent` is then used to remove the model json from the registry and replace it with our custom `IBakedModel`.
1. During the render, `BlockCamouflage.getExtendedState()` is called, which looks at the block's neighbours to pick a suitable block for the camouflage block to copy.  It then stores the identity of that copied block in the `IExtendedBlockState`.
1. The `IExtendedBlockState` is passed to the custom `IBakedModel`, which looks up the `IBakedModel` for the copied block and returns it to the caller for rendering.

BEWARE! Rendering is multithreaded so your custom `IBakedModel` must be thread-safe, preferably immutable.

The camouflage block appears in the Blocks tab in the creative inventory.

The pieces you need to understand are located in:

* `StartupClientOnly` and `StartupCommon`
* `BlockCamouflage`
* `CamouflageISmartBlockModelFactory`
* `ModelBakeEventHandler`
* `UnlistedPropertyCopiedBlock`
* `resources\assets\minecraftbyexample\lang\en_US.lang`--for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe04_block_camouflage`--for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe04_block_camouflage_model`--the model used to render the uncamouflaged block
* `resources\assets\minecraftbyexample\models\item\mbe04_block_camouflage`--the model for rendering as an item
* `resources\assets\minecraftbyexample\textures\blocks\mbe04_block_camouflage`--the texture used for the uncamouflaged block

More background information:

* blocks: see [http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html](http://www.minecraftforge.net/forum/index.php/topic,21354.0.html)
* rendering blocks: see [http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html](http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html) (the topics under the Block Rendering heading)
* test code for ISmartBlockModel which generates the model quads using an algorithm instead of using a pre-existing model
    * [https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/net/minecraftforge/debug/ModelBakeEventDebug.java](https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/net/minecraftforge/debug/ModelBakeEventDebug.java)
    * Also illustrated for items in `mbe15_item_smartitemmodel`, can be easily adapted to blocks.
* another way of implementing `ISmartBlockModel`, also explains `IExtendedBlockState`: [http://www.minecraftforge.net/forum/index.php/topic,28714.0.html](http://www.minecraftforge.net/forum/index.php/topic,28714.0.html)
* A way to automate insertion of your `ISmartBlockModel` into the model registry, useful if you have lots of models: [http://www.minecraftforge.net/forum/index.php/topic,26267.msg158055.html#msg158055](http://www.minecraftforge.net/forum/index.php/topic,26267.msg158055.html#msg158055)

Useful vanilla classes to look at (for BlockStateMapping):  `BlockModelShapes.registerAllBlocks()`

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
