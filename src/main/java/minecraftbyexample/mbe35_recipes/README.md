# MBE35_RECIPES

This example is a collection of the different types of recipes in the game. It will show you:

1. how to create recipes for the crafting table
1. how to use "ore dictionary" recipes for the crafting table
1. how to create recipes for the furnace ("smelting")
1. how to add a "fuel" value for your items when burnt in a furnace
1. how to add your own `IRecipe` class for complicated crafting logic

In earlier versions, recipes had to be added by code. Now they can be added by json as well. This example shows both. For pictures of the recipes being added, see [http://greyminecraftcoder.blogspot.com/2015/02/recipes.html](http://greyminecraftcoder.blogspot.com/2015/02/recipes.html).

The pieces you need to understand for adding recipes by code are located in:

* `StartupCommon`

For json recipes, all you need is the json file in the right location: `resources.assets.minecraftbyexample.recipes.mbe35_recipe_painting.json`

The parsing of recipes is done mostly in `CraftingHelper`--`getIngredient`, `getItemStack`

Useful discussion about recipes, especially the new json: [http://www.minecraftforum.net/forums/minecraft-java-edition/redstone-discussion-and/commands-command-blocks-and/2810250-1-12-custom-recipes](http://www.minecraftforum.net/forums/minecraft-java-edition/redstone-discussion-and/commands-command-blocks-and/2810250-1-12-custom-recipes)

The names of vanilla Items can be found in the `Items` class (see the static initialiser at the bottom).

This code is heavily based on Wuppy29's tutorials:

* [http://www.wuppy29.com/minecraft/modding-tutorials/wuppys-minecraft-forge-modding-tutorials-for-1-7-crafting-recipes](http://www.wuppy29.com/minecraft/modding-tutorials/wuppys-minecraft-forge-modding-tutorials-for-1-7-crafting-recipes)
* [http://www.wuppy29.com/minecraft/modding-tutorials/wuppys-minecraft-forge-modding-tutorials-for-1-7-shapeless-recipes-and-smelting](http://www.wuppy29.com/minecraft/modding-tutorials/wuppys-minecraft-forge-modding-tutorials-for-1-7-shapeless-recipes-and-smelting)

For vanilla recipes:

* smelting--see `FurnaceRecipes`
* shapeless recipes--see `RecipeDyes`
* ore based recipes--`OreDictionary`
* custom recipes--see `RecipeFireworks`
