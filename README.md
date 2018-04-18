MinecraftByExample [1.12.2]
==================
The purpose of MinecraftByExample is to give simple working examples of the important concepts in Minecraft and Forge. If you're anything like me, a good code example is worth several screens worth of waffling explanation, and can very quickly explain the key concepts.  I also find it much easier to adapt and debug something that already works, than to have to synthesize something from scratch and spend hours trying to discover the missing bit of information I didn't know about.

I've tried to keep the code simple and obvious and to resist the urge to be clever. The examples might not be the most efficient or succinct implementation, I've deliberately left the optimization to you.

Each example is split up to be totally independent of all the others.  The only parts of the code which are common to more than one example are the MinecraftByExample, CommonProxy, ClientOnlyProxy, and DedicatedServer classes, which can be found [here][main_classes].

If you want more information and explanatory text about the concepts, the following links might be useful:

  - [Guide to how Minecraft works][greyminecraftcoder]
  - [Forge Modding articles][wuppy]
  - [The Official Forge documentation][forgedocs]
  - [Forge Tutorials listing][tutorials_forum]
  - [List of Good Tutorials][list_of_tutorials]

####For earlier versions, see the relevant GitHub branch:
 - MBE for Forge 1.8: [1-8final][version1-8]
 - MBE for Forge 1.8.9: [1-8-9final][version1-8-9]
 - MBE for Forge 1.10.2: [1-10-2final][version1-10-2]
 - MBE for Forge 1.11: [1-11final][version1-11]
 - MBE for Forge 1.11.2: [1-11-2final][version1-11-2]

##List of examples
### Blocks
  - [MBE01][01] - a simple cube
  - [MBE02][02] - a block with a more complicated shape
  - [MBE03][03] - a block (coloured signpost) with multiple variants- four colours, can be placed facing in four directions
  - [MBE04][04] - a camouflage ("secret door") block which dynamically changes its appearance to match adjacent blocks - uses IBlockModel.getQuads() and onModelBakeEvent() 
  - [MBE05][05] - a 3D web which joins to neighbours in all six directions - uses IBlockModel.getQuads() and ICustomModelLoader
  - [MBE06][06] - several different types of block which use redstone
  - [MBE08][08] - how to add a creative tab for organising your custom blocks / items

### Items
  - [MBE10][10] - a simple item
  - [MBE11][11] - an item with multiple variants - rendered using multiple models and multiple layers
  - [MBE12][12] - an item that stores extra information in NBT, also illustrates the "in use" animation similar to drawing a bow
  - [MBE13][13] - customise Mining behaviour of Blocks and Items - several test classes that show how mining works
  - [MBE14][14] - an interactive helper tool to adjust the ItemCameraTransforms for your custom item
  - [MBE15][15] - a chessboard item with 1 - 64 pieces; uses ItemOverrideList.handleItemState(), IBlockModel.getQuads() and onModelBakeEvent()


### TileEntities
  - [MBE20][20] - using a tile entity to store information about a block - also shows examples of using NBT storage
  - [MBE21][21] - using the TileEntitySpecialRenderer to render unusual shapes or animations

### Containers (Inventories)
  - [MBE30][30] - a simple container for storing items in the world - similar to a Chest
  - [MBE31][31] - a functional container such as a Furnace or Crafting Table

### Recipes (Crafting/Furnace)
  - [MBE35][35] - some typical example crafting recipes and furnace (smelting) recipes

### Heads Up Display/Overlays
  - [MBE40][40] - simple customisations of the heads up display (hotbar, health meter)

### Particles - particle effects
  - [MBE50][50] - shows how to use vanilla Particles; also how to generate your own custom Particles

### Network
  - [MBE60][60] - send network messages between client and server

### Configuration GUI
  - [MBE70][70] - configuration file linked to the "mod options" button GUI on the mods list screen

### Testing tools
  - [MBE75][75] - a tool to help you automate testing of your classes in-game.

## Usage
  - You can browse directly in GitHub, or alternatively, download it as a zip and browse it locally.

  - If you want to install it and compile it, the basic steps for beginners are:
    1. Download the project as a zip.
    2. Unzip it to an appropriate folder on your computer, such as My Documents.  (Or, if you know how to fork a project on GitHub and import it into a local git repository, you can do that instead)
    3. From the command line, run `gradlew setupDecompWorkspace` to install Forge and configure the project, this will take quite some time, maybe 20 minutes or more.
    4. If using an IDE, such as Eclipse or IntelliJ IDEA, you can configure the IDE from the command line: run `gradlew idea` if you are using IntelliJ IDEA, or `gradlew eclipse` if you are using Eclipse.
    5. If using IntelliJ:
        1. Open the project (open the MinecraftByExample.ipr file).
        2. It will ask you whether you want to import the "unlinked gradle project".  This is optional.  If you choose no,
           later on (when you publish your mod) you will need to package it up using the command line gradlew build, if you
           choose yes you can run the gradle build task from inside IntelliJ.  For most users, there's no other difference.
        3. If you imported the gradle project, you should run the gradle task 'genIntellijRuns' afterwards to get the Run and Debug
           configurations.
    6. If using Eclipse:
        1. Open the Project.
        2. Right click on the project, select "Run As" > "Run Configurations..."
        3. Set the main class to "GradleStart"
    8. You should be able to start Minecraft now (using the Run or Debug configuration) and the mod will be loaded.	
    9. Without an IDE and running from the command line, you can build the project with `gradlew build` and then run it with `gradlew runClient`. In order to load the mod into a production or stand-alone Minecraft, run `gradlew reobf` and copy `build/libs/minecraftbyexample-1.12.2a.jar` into the Minecraft mods directory before starting Minecraft.  Personally, I recommend to use an IDE, it makes coding and debugging a lot easier.  But it's not essential.  

    
#### If You're Still Confused
Head over [here][more_help] if this didn't make sense to you, alternatively [this tutorial][diesieben_idea] by diesieben07.

Check out [this video][forge_installation] for more help installing Forge.

[main_classes]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample
[greyminecraftcoder]: http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html
[forgedocs]:http://mcforge.readthedocs.org/en/latest/
[wuppy]: http://www.wuppy29.com/minecraft/modding-tutorials/forge-modding-1-8
[tutorials_forum]: http://www.minecraftforge.net/forum/index.php/board,120.0.html
[list_of_tutorials]: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2716947-can-we-start-a-thorough-list-of-really-good

[01]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe01_block_simple
[02]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe02_block_partial
[03]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe03_block_variants
[04]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe04_block_dynamic_block_model1
[05]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe05_block_dynamic_block_model2
[06]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe06_redstone
[08]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe08_creative_tab

[10]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe10_item_simple
[11]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe11_item_variants
[12]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe12_item_nbt_animate
[13]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe14_item_camera_transforms
[14]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe14_item_camera_transforms
[15]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe15_item_dynamic_item_model

[20]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe20_tileentity_data
[21]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe21_tileentityspecialrenderer

[30]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe30_inventory_basic
[31]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace
[35]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe35_recipes

[40]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe40_hud_overlay

[50]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe50_particle

[60]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe60_network_messages

[70]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe70_configuration

[75]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe75_testing_framework

[more_help]: http://www.minecraftforge.net/forum/index.php?topic=21354.msg108332#msg108332
[forge_installation]: http://youtu.be/8VEdtQLuLO0
[alternate_idea]: #alternate-setup-for-intellij-idea
[gradle_tool_window]: https://www.jetbrains.com/idea/help/gradle-tool-window.html
[diesieben_idea]: http://www.minecraftforge.net/forum/index.php/topic,21354.0.html
[IntellijFix]:http://www.minecraftforge.net/forum/index.php/topic,21354.0.html

[version1-8]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-8final
[version1-8-9]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-8-9final
[version1-10-2]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-10-2final
[version1-11]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-11-final
[version1-11-2]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-11-2-final

With thanks to these helpful folks:
alvaropp, 
yooksi,
Brandon3035,
twrightsman (greekphysique),
Nephroid,
Herbix, and
Shadowfacts

## Licence Info:
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>