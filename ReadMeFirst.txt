MinecraftByExample [1.12.2]
==========================
The purpose of MinecraftByExample is to give simple working examples of the important concepts in Minecraft and Forge.
If you're anything like me, a good code example is worth several screens' worth of waffling explanation, and can very
quickly explain the key concepts.  I also find it much easier to adapt and debug something that already works, than to have to
synthesise something from scratch and spend hours trying to discover the missing bit of information I didn't know about.

I've tried to keep the code simple and obvious and to resist the urge to be clever.  The examples might not be the most
efficient or succinct implementation, I've deliberately left the optimisation to you.

Each example is split up to be totally independent of all the others.  The only parts of the code which are common
to more than one example are the MinecraftByExample, CommonProxy, ClientOnlyProxy, and DedicatedServer classes.

If you want more information and explanatory text about the concepts, the following links might be useful
Guide to how Minecraft works-  http://greyminecraftcoder.blogspot.com.au/p/list-of-topics.html
The Official Forge documentation- http://mcforge.readthedocs.org/en/latest/
Forge Modding articles- http://www.wuppy29.com/minecraft/modding-tutorials/forge-modding-1-8
Forge Tutorials listing- http://www.minecraftforge.net/forum/index.php/board,120.0.html

For earlier versions, see the relevant GitHub branch
  1.8: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-8final
  1.8.9: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-8-9final
  1.10.2: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-10-2final
  1.11: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-11-final
  1.11.2: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-11-2-final

List of examples
Blocks
  MBE01 - a simple cube
  MBE02 - a block with a more complicated shape
  MBE03 - a block (coloured signpost) with multiple variants- four colours, can be placed facing in four directions
  MBE04 - a camouflage ("secret door") block which copies the appearance of adjacent blocks - uses
          IBlockModel.getQuads() and onModelBakeEvent()
  MBE05 - a 3D web which joins to neighbours in all six directions - uses IBlockModel.getQuads() and ICustomModelLoader
  MBE06 - several different types of block which use redstone
  MBE08 - how to add a creative tab for organising your custom blocks / items

Items
  MBE10 - a simple item
  MBE11 - an item with multiple variants - rendered using multiple models and multiple layers
  MBE12 - an item that stores extra information in NBT, also illustrates the "in use" animation similar to drawing a bow
  MBE13 - customise Mining behaviour of Blocks and Items - several test classes that show how mining works
  MBE14 - an interactive helper tool to adjust the ItemCameraTransforms for your custom item
  MBE15 - a chessboard item with 1 - 64 pieces; uses ItemOverrideList.handleItemState(), IBlockModel.getQuads() and onModelBakeEvent()

TileEntities
  MBE20 - using a tile entity to store information about a block - also shows examples of using NBT storage
  MBE21 - using the TileEntitySpecialRenderer to render unusual shapes or animations

Containers (Inventory blocks)
  MBE30 - a simple container for storing items in the world - similar to a Chest
  MBE31 - a functional container such as a Furnace or Crafting Table

Recipes (Crafting and Furnaces)
  MBE35 - some typical example crafting recipes and furnace (smelting) recipes

HeadsUpDisplay - overlays
  MBE40 - simple customisations of the heads up display (hotbar, health meter)

Particle - particle effects
  MBE50 - shows how to use vanilla Particles; also how to generate your own custom Particles

Network
  MBE60 - send network messages between client and server

Configuration GUI
  MBE70 - configuration file linked to the "mod options" button GUI on the mods list screen

Testing tools
  MBE75 - a tool to help you automate testing of your classes in-game.

How to use this example project-
A) You can browse directly in GitHub, or alternatively download it as a zip and browse it locally.

B) If you want to install it and compile it, the basic steps for beginners are:
1) Download the project as a zip.  Unzip it to an appropriate folder on your computer, such as My Documents.  (Or, if you know
   how to fork a project on GitHub and import it into a local git repository, you can do that instead).
2) From the command line, run gradlew setupDecompWorkspace to install Forge and configure the project.  This will take
   quite some time, maybe 20 minutes or more.
3) From the command line, run gradlew idea if you are using IntelliJ IDEA, or gradlew eclipse if you are using Eclipse.
4) If using IntelliJ:
    a) Open the project (open the MinecraftByExample.ipr file).
    b) It will ask you whether you want to import the "unlinked gradle project".  This is optional.  If you choose no,
       later on (when you publish your mod) you will need to package it up using the command line gradlew build, if you
       choose yes you can run the gradle build task from inside IntelliJ.  For most users, there's no other difference.
    c) If you imported the gradle project, you should run the gradle task 'getIntellijRuns' afterwards to get the Run and Debug
       configurations.
5) If using Eclipse:
    a) Open the project
    b) Right click on the project, select "Run As" > "Run Configurations..."
    c) Set the main class to "GradleStart"
6) You should be able to start Minecraft now (using the Run or Debug configuration) and the mod will be loaded.
7) If you aren't using an IDE you can build the project with gradlew build and then run it with gradlew runClient. In order to load the mod into a production or stand-alone Minecraft, run gradlew reobf and copy build/libs/minecraftbyexample-1.12.2a.jar into the Minecraft mods directory before starting Minecraft.  Personally, I recommend to use an IDE, it makes coding and debugging a lot easier.  But it's not essential.

For some extra help if this doesn't make sense to you
http://www.minecraftforge.net/forum/index.php?topic=21354.msg108332#msg108332

Some extra help for installation of forge:
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

With thank to these helpful folks:
Alvaropp,
yooksi,
Brandon3035,
twrightsman (greekphysique)
Nephroid,
Herbix, and
Shadowfacts

--------------
Licence Info
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