# MBE35_RECIPES_AND_TAGS

This example is a collection of the different types of recipes in the game. It will show you:

1. how to create recipes for the crafting table
1. how to use "tag" recipes for the crafting table
1. how to use "nbt"-sensitive recipes for the crafting table
1. how to create recipes for the furnace ("smelting")
1. how to add a "fuel" value for your items when burnt in a furnace
1. how to add your own `IRecipe` class for complicated crafting logic

In earlier versions, recipes had to be added by code. Now they can only be added by json. 
For json recipes, all you need is the json file in the right location.
See `StartupCommon` for more information

For pictures of the recipes being added, see [https://greyminecraftcoder.blogspot.com/2020/05/minecraft-by-example.html](https://greyminecraftcoder.blogspot.com/2020/05/minecraft-by-example.html).


## Tags

Tags are used to group similar types of blocks and/or items together, usually for crafting or loot drop purposes.
Not to be confused with "NBT Tags", which are used to store extra information inside an ItemStack.

For example
* music_discs in data\minecraft\tags\items
* beds in data\minecraft\tags\blocks<br>
but also
* enderman_holdable in data\minecraft\tags\blocks

ItemTags has the vanilla tags.
Forge defines a number of custom tags in the Tags class in addition to those in the vanilla "data" resources.

Code can check if an Item has a particular tag by eg
itemstack.getItem().isIn(ItemTags.SMALL_FLOWERS)

To add your own tags, just define the appropriately named json in data.tags.item or .block or .entity_types (etc) 
To add your own item to an existing vanilla tag, put a tag json with the same name into the correct data folder, and your extra items will be automatically added to the vanilla tag.   


## Notes

The parsing of recipes is done in a serializer for each recipe type.  See IRecipeSerializer for the vanilla versions.  Each serializer has two main functions:
1) parse a json recipe into an IRecipe; used on the server to read in recipe data from resource packs
2) parse a PacketBuffer into an IRecipe; used on the client to read recipe data sent by the server

IRecipeSerializer contains all the vanilla recipes

If you want to add special crafting recipes, you can nearly always find something similar in one of the vanilla IRecipes and IRecipeSerializers
for example
* SuspiciousStewRecipe (SpecialRecipe and SpecialRecipeSerializer)
* FurnaceRecipe (AbstractCookingRecipe and CookingRecipeSerializer)<br>
etc

The names of vanilla Items can be found in the `Items` class.


## Further information: <br>
[https://minecraft.gamepedia.com/Recipe](https://minecraft.gamepedia.com/Recipe) <br>
[https://minecraft.gamepedia.com/Data_pack](https://minecraft.gamepedia.com/Data_pack) <br>
[https://minecraft.gamepedia.com/Tag](https://minecraft.gamepedia.com/Tag) <br>
[Information on Changes to recipes since 1.12.2](https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a) <br>
[Forge docs on recipes](https://mcforge.readthedocs.io/en/latest/utilities/recipes/) <br>
