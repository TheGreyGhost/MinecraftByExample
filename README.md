MinecraftByExample
==================

The purpose of MinecraftByExample is to give simple working examples of the important concepts in Minecraft and Forge. If you're anything like me, a good code example is worth several screens worth of waffling explanation, and can very quickly explain the key concepts.  I also find it much easier to adapt and debug something that already works, than to have to synthesize something from scratch and spend hours trying to discover the missing bit of information I didn't know about.

I've tried to keep the code simple and obvious and to resist the urge to be clever. The examples might not be the most efficient or succinct implementation, I've deliberately left the optimization to you.

Each example is split up to be totally independent of all the others.  The only parts of the code which are common to more than one example are the MinecraftByExample, CommonProxy, ClientOnlyProxy, and DedicatedServer classes, which can be found [here][main_classes].


If you want more information and explanatory text about the concepts, the following links might be useful:

  - [Guide to how Minecraft works][greyminecraftcoder]
  - [Forge Modding articles][wuppy]
  - [Forge Tutorials listing][tutorials_forum]

##List of examples
### Blocks
  - [MBE01][01] - a simple cube
  - [MBE02][02] - a block with a more complicated shape
  - [MBE03][03] - a block (coloured signpost) with multiple variants- four colours, can be placed facing in four directions
  - [MBE08][08] - how to add a creative tab for organising your custom blocks / items

### Items
  - [MBE10][10] - a simple item
  - [MBE11][11] - an item with multiple variants - rendered using multiple models and multiple layers
  - [MBE12][12] - an item that stores extra information in NBT, also illustrates the "in use" animation similar to drawing a bow
  - [MBE13][13] - customise Mining behaviour of Blocks and Items - several test classes that show how mining works
  - [MBE14][14] - an interactive helper tool to adjust the ItemCameraTransforms for your custom item

### TileEnities
  - [MBE20][20] - using a tile entity to store information about a block - also shows examples of using NBT storage
  - [MBE21][21] - using the TileEntitySpecialRenderer to render unusual shapes or animations

### Containers (Inventories)
  - [MBE30][30] - a simple container for storing items in the world - similar to a Chest
  - [MBE31][31] - a functional container such as a Furnace or Crafting Table

### Recipes (Crafting/Furnace)
  - [MBE35][35] - some typical example crafting recipes and furnace (smelting) recipes

### Heads Up Display/Overlays
  - [MBE40][40] - simple customisations of the heads up display (hotbar, health meter)

### Network
  - [MBE60][60] - send network messages between client and server

## Usage
  - You can browse directly in GitHub, or alternatively, download it as a zip and browse it locally.

  - If you want to install it and compile it, the basic steps for beginners are:
    1. Download the project as a zip.
    2. Unzip it to an appropriate folder on your computer, such as My Documents.  (Or, if you know how to fork a project on GitHub and import it into a local git repository, you can do that instead)
    3. From the command line, run `gradlew setupDecompWorkspace` to install Forge and configure the project, this will take quite some time, maybe 20 minutes or more.
    4. From the command line, run `gradlew idea` if you are using IntelliJ IDEA ([alternate method][alternate_idea]), or `gradlew eclipse` if you are using Eclipse.
    5. Open IntelliJ or Eclipse, then open the project. (If you want, you can import the gradle project so you don't have to use the command line when building the mod)
    6. If using Eclipse:
        1. Right click on the project, select "Run As" > "Run Configurations..."
        2. Set the main class to "GradleStart"
    7. You should be able to start Minecraft now (using the Run or Debug configuration) and the mod will be loaded.
    
### Alternate Setup for IntelliJ IDEA
By using `gradlew idea` to create the project files for IDEA, will work perfectly fine, but to be able to use the [Gradle Tool Window][gradle_tool_window] in IDEA, you must set up your project slightly differently. Check out [this tutorial][diesieben_idea] by diesieben07 on how to set up your project to take advantage of Gradle integraion in IDEA.

#### If You're Still Confused
Head over [here][more_help] if this didn't make sense to you.

Check out [this video][forge_installation] for more help installing Forge.


[main_classes]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample
[greyminecraftcoder]: http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html
[wuppy]: http://www.wuppy29.com/minecraft/modding-tutorials/forge-modding-1-8
[tutorials_forum]: http://www.minecraftforge.net/forum/index.php/board,120.0.html

[01]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe01_block_simple
[02]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe02_block_partial
[03]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe03_block_variants
[08]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe08_creative_tab

[10]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe10_item_simple
[11]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe11_item_variants
[12]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe12_item_nbt_animate
[13]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe14_item_camera_transforms
[14]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe14_item_camera_transforms

[20]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe20_tileentity_data
[21]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe21_tileentityspecialrenderer

[30]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe30_inventory_basic
[31]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace
[35]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe35_recipes

[40]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe40_hud_overlay

[60]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe60_network_messages

[more_help]: http://www.minecraftforge.net/forum/index.php?topic=21354.msg108332#msg108332
[forge_installation]: http://youtu.be/8VEdtQLuLO0
[alternate_idea]: #alternate-setup-for-intellij-idea
[gradle_tool_window]: https://www.jetbrains.com/idea/help/gradle-tool-window.html
[diesieben_idea]: http://www.minecraftforge.net/forum/index.php/topic,21354.0.html

With thanks to these helpful folks:
Brandon3035,
Nephroid, and
Shadowfacts
