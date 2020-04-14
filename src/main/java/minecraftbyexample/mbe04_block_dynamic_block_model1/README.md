# MBE04_BLOCK_DYNAMIC_BLOCK_MODEL1

This example shows how to use `IBakedModel#getQuads` to dynamically generate a model in conjunction with `ModelBakeEvent`.

Normal blocks have a unique blockstate for every configuration, and you need to create a blockstates JSON file which handles every single state. But what if you want a block which needs to display many more possibilities? For example a wool block with 100 different colours, or a camouflage block which copies the texture of the adjacent block? In these cases you can use either `IBakedModel#getQuad` or a `TileEntityRenderer`. Which to choose?

TileEntityRenderer is often a better (simpler!) choice if the block is animated, or needs fancy rendering, for example like an animated chest, or the lettering on a sign.  However you must give your block a `TileEntity` as well.<br>

`IBakedModel#getQuads` is often a better (more realistic) choice if the block should have shaded lighting like a normal block, or if there are very many of the block and they don't need to store information (can calculate their state from adjacent blocks, or other world information). For example, a chameleon block that changes its colour to match the colours of its neighbours.  However this method is not suitable for animation because the getQuads is cached- it is only altered when the blockstate is updated or its neighbours are changed and the chunk quad list needs to be regenerated. 

This example shows the basics of using `IBakedModel#getQuads` to dynamically generate a model.

How it works:
When a block is rendered, the following sequence occurs:
1) The BlockState is used to retrieve the corresponding IBakedModel for that blockstate
2) The IBakedModel::getQuads is called, which returns a collection of quads that are drawn by the rendererer

In order to make a dynamic model (not based on quads), all we need to do is add our own custom IBakedModel which changes the quads returned by getQuads.  They can be copied from other models, or generated dynamically.

Forge adds an extended implementation of IBakedModel called IForgeBakedModel.  Its main benefit is allow IModelData to be passed into the getQuads so that the getQuads can customise its quads appropriately.

BlockCamouflage:
1. The vanilla loader loads the model for `mbe04_block_camouflage` using the blockstates json and model json, as per normal blocks
1. The `ModelBakeEvent` is then used to remove the model json from the registry and replace it with our custom `IBakedModel`.
1. During the render, `IForgeBakedModel::getModelData` is called, which looks at the block's neighbours to pick a suitable block for the camouflage block to copy.  It then stores the identity of that copied block in the IModelData.
1. The `IModelData` is passed to the custom `IForgeBakedModel::getQuads`, which looks up the `IBakedModel` for the copied block and returns it to the caller for rendering.

BEWARE! Rendering is multithreaded so your custom `IBakedModel` must be thread-safe, preferably immutable.

The camouflage block appears in the Blocks tab in the creative inventory.

The pieces you need to understand are located in:

* `StartupClientOnly` and `StartupCommon`
* `BlockCamouflage`
* `CamouflageBakedModel`
* `resources\assets\minecraftbyexample\lang\en_US.lang`--for the displayed name of the block
* `resources\assets\minecraftbyexample\blockstates\mbe04_block_camouflage_registry_name`--for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe04_block_camouflage_model`--the model used to render the uncamouflaged block
* `resources\assets\minecraftbyexample\models\item\mbe04_block_camouflage_registry_name`--the model for rendering as an item
* `resources\assets\minecraftbyexample\textures\blocks\mbe04_block_camouflage`--the texture used for the uncamouflaged block

More background information:

* block models: [https://greyminecraftcoder.blogspot.com/2020/04/block-models-1144.html](https://greyminecraftcoder.blogspot.com/2020/04/block-models-1144.html)
* baking block models: [https://greyminecraftcoder.blogspot.com/2020/04/baking-models-1144.html](https://greyminecraftcoder.blogspot.com/2020/04/baking-models-1144.html)
* rendering blocks: see [https://greyminecraftcoder.blogspot.com/2020/04/block-rendering-1144.html](https://greyminecraftcoder.blogspot.com/2020/04/block-rendering-1144.html)
* Quads: [https://greyminecraftcoder.blogspot.com/2020/04/quad-rendering-1144.html](https://greyminecraftcoder.blogspot.com/2020/04/quad-rendering-1144.html) 

## Common errors

* Misspelled filenames and model resource locations.
* Most bugs will need you to set breakpoint in vanilla and inspect the registries to figure out what's going wrong.

