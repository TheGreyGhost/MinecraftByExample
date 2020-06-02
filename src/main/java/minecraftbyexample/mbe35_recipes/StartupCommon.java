package minecraftbyexample.mbe35_recipes;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {

    //  see http://greyminecraftcoder.blogspot.com/2015/02/recipes.html for illustrations of these recipes

    // "group" is optional.  The value of this string can be anything. Any recipes that have the same group name specified
    // will be shown together in the crafting helper.
    // The intention is to keep similar item within the same category, such as all boats for example.
    // If you want to match an existing vanilla recipe group, look in the relevant recipe json in data.minecraft.recipes

    // a) Shaped recipe emerald surrounded by diamond makes ender eye
    //  see data.minecraftbyexample.recipes.mbe35_recipe_ender_eye
    // note - smaller grids are also possible, you don't need to fill up the entire 3x3 space.

    // b) Shapeless recipe - blue dye plus yellow dye makes two green dye blue_dye
    //  data.minecraftbyexample.recipes.mbe35_recipe_green_dye

    // c) Tag recipe - any type of tree leaves arranged around sticks makes an oak sapling
    //    Tags are a way for mods to add block & items which are equivalent to vanilla block for crafting
    //    For example - a recipe which uses the tag "leaves" will accept leaves of jungle_leaves, oak_leaves, etc
    //    Vanilla tags are defined in data.minecraft.tags (eg data.minecraft.tags.items.leaves), and some additional
    //       Forge-defined tags in Tags class
    //   data.minecraftbyexample.recipes.mbe_35_recipe_sapling

    // d) Shaped recipe with NBT information
    // If you want your recipes to match particular NBT information, use the forge NBTIngredient serialiser instead of item
    // look at CraftingHelper.register(new ResourceLocation("forge", "nbt"), NBTIngredient.Serializer.INSTANCE);
    //  (the json tag is "nbt" instead of "item")
    // for this example, we use the nbt tag "damage" to match for an undamaged sword:
    //   wooden sword (undamaged only) in a clay mould plus iron ingot makes iron sword
    //   wooden sword (undamaged only) in a clay mould plus gold ingot makes gold sword
    // both recipes are in the same "group" (mbe35_sword_upgrade_group)
    //  see data.minecraftbyexample.recipes.mbe35_recipe_wooden_sword_to_iron and
    //  see data.minecraftbyexample.recipes.mbe35_recipe_wooden_sword_to_gold

    // d) Tag recipe with a custom tag - paper plus a feather plus the custom primary_paint_colours makes a painting
    //    We've created a custom tag primary_paint_colours in data.minecraftbyexample.tags.items.primary_paint_colours
    //    which contains the primary subtractive colours for paints, i.e. cyan, magenta, and yellow.
    //   see data.minecraftbyexample.recipes.mbe35_recipe_painting_custom_tag for the resulting recipe
    //   FYI - you can use Forge custom Ingredient "compound" instead of "tag" (see CompoundIngredient)
    //   see data.minecraftbyexample.recipes.mbe35_recipe_painting_compound
    //   both recipes are in the same "group" (mbe35_paintings_group)

    //---------------- FURNACE RECIPES (smelting)

    // e) smelting recipe - smelting cake gives you charcoal
    //    see data.minecraftbyexample.recipes.mbe35_recipe_smelting_cake

    // f) fuel - use wheat as fuel in a furnace
    //   For your own item, override getItemBurnTime()
    //   For vanilla, need to register for the FurnaceFuelBurnTimeEvent, which is called whenever fuel is placed into a furnace
    //   Look in the FurnaceFuelBurnTimeEventHandler class for the details.
    MinecraftForge.EVENT_BUS.register(FurnaceFuelBurnTimeEventHandler.instance);

    //  ------------- Custom recipes
    // you can even register your own custom IRecipe class to match complicated inputs - see for example RecipeFireworks
    //  There are a few useful vanilla recipes and Serializers you can base your Recipe on
    //  eg AbstractCookingRecipe, SpecialRecipe and SpecialRecipeSerializer
    // or go the whole hog and write your own.  Lots of vanilla inspiration to keep you on the right track.
    //  All you need to do is register your serializer like this:
    //  @SubscribeEvent
    //  public void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
    //    event.getRegistry().register(yourRecipeSerializer);
    //  }
  }
}
