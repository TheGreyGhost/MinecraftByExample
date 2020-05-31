# MBE35_RECIPES_AND_TAGS

This example is a collection of the different types of recipes in the game. It will show you:

1. how to create recipes for the crafting table
1. how to use "ore dictionary" recipes for the crafting table
1. how to create recipes for the furnace ("smelting")
1. how to add a "fuel" value for your items when burnt in a furnace
1. how to add your own `IRecipe` class for complicated crafting logic

Tags are used to group similar types of blocks and/or items together, usually for crafting or loot drop purposes.
Not to be confused with "NBT Tags", which are used to store extra information inside an ItemStack.

For example
music_discs in data\minecraft\tags\items
beds in data\minecraft\tags\blocks
but also
enderman_holdable in data\minecraft\tags\blocks

Forge defines a number of custom tags in the Tags class in addition to those in the vanilla "data" resources.

Code can check if an Item has a particular tag by eg
itemstack.getItem().isIn(ItemTags.SMALL_FLOWERS)


Tags can be used in place of blocks or items using the following syntax:
#minecraft
TO CHECK


In earlier versions, recipes had to be added by code. Now they can only be added by json. 

IRecipeSerializer contains all the vanilla recipes

If you want to add special crafting recipes, you can nearly always find something similar in one of the vanilla IRecipe
for example
SuspiciousStewRecipe
FurnaceRecipe
etc



The pieces you need to understand for adding recipes by code are located in:

* `StartupCommon`

For json recipes, all you need is the json file in the right location: `resources.data.minecraftbyexample.recipes.mbe35_recipe_painting.json`

The parsing of recipes is done in a serializer for each recipe type.  See IRecipeSerializer for the vanilla versions.  Each serializer has two main functions:
1) parse a json recipe into an IRecipe; used on the server to read in recipe data from resource packs
2) parse a PacketBuffer into an IRecipe; used on the client to read recipe data sent by the server


The names of vanilla Items can be found in the `Items` class (see the static initialiser at the bottom).

For pictures of the recipes being added, see [http://greyminecraftcoder.blogspot.com/2015/02/recipes.html](http://greyminecraftcoder.blogspot.com/2015/02/recipes.html).


For vanilla recipes:
See IRecipeSerializer - this 

https://minecraft.gamepedia.com/Recipe
https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
https://mcforge.readthedocs.io/en/latest/utilities/recipes/