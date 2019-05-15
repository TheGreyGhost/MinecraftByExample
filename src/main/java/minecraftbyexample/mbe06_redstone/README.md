# MBE06_REDSTONE

There are a number of redstone examples in this package; grouped into three types:

* power source blocks ("output_only") which provide redstone power to other blocks, but don't accept input from other
    * Vanilla examples are `BlockCompressedPowered`, `BlockButton`
* power consumer blocks ("input_only") which accept redstone input from other blocks but don't provide it to others
    * Vanilla example is `BlockRedstoneLight`
* mixed blocks ("input_and_output") which accept redstone input and also provide output to others
    * Vanilla examples are `BlockRedstoneRepeater`, `BlockRedstoneComparator`

The blocks illustrate how the various types work, and some of the subtleties around server<-->client side, the use of `IBlockAccess` instead of `World`, and why output blocks need to store the power level in metadata or a `TileEntity` rather than just calculating it. For more explanation see [http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html](http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html).

The examples are:

* `BlockRedstoneColouredLamp`--a power consumer--has inputs on three sides: red, green, blue, which cause the lamp to light up in different colours depending on the signal strength on each input
* `BlockRedstoneVariableSource`--a weak power source similar to a redstone torch, except that the power level can be changed by clicking on it (0%, 25%, 50%, 75%, 100%)
* `BlockRedstoneTarget`--a strong power source similar to a button--it's a bullseye target which can be hung on the side of a stone block. When an arrow is shot into the target, it sends strong power into the stone block. The closer the arrow to the bullseye, the higher the power level.
* `BlockRedstoneMeter`--both consumes and produces power. The block has a dial gauge which shows the level of weak power being received from side neighbours. In addition, it emits weak power into the blocks above and below the meter. If a redstone lamp is placed on top of the meter, it will flash on and off--the higher the meter reading, the faster the flashing.

The pieces you need to understand are located in:

* `StartupCommon`, `StartupClient`
* `BlockRedstone****` classes -- the block behaviour
* `TileEntity****` classes -- sometimes needed to store power level information
* `resources\assets\minecraftbyexample\lang\en_US.lang` -- for the displayed name of the blocks
* `resources\assets\minecraftbyexample\blockstates\mbe06_block_****` -- for the blockstate definition
* `resources\assets\minecraftbyexample\models\block\mbe06_block_****_model` -- for the model used to render the block
* `resources\assets\minecraftbyexample\textures\blocks\mbe06_***.png` -- textures used for the faces of the block
* `resources\assets\minecraftbyexample\models\item\mbe06_block_****_model` -- the model for rendering the block as an item (i.e. in your hand, in the inventory, when thrown on the ground)

The blocks will appear in the Blocks tab in the creative inventory.

For background information on
* redstone: see [http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html](http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html)
* blocks: see [http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html](http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html)
* rendering blocks: see [http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html](http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html) (the topics under the Block Rendering heading)

## Common errors

* forgetting about the difference between server <--> client and the need to keep the client synchronised
* forgetting to store the power information needed by the query methods eg getWeakPower()
* getting the face / side parameter wrong (opposite direction) in the various redstone methods
* getting confused by the rubbish misleading names for the redstone methods
