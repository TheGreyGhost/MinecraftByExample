package minecraftbyexample.mbe35_recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{

  /**
   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
   */
  public static void preInitCommon() {
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public static void initCommon()
  {
    // your recipes must go in initialisation phase, not preInit.

    //  see http://greyminecraftcoder.blogspot.com/2015/02/recipes.html for illustrations of these recipes

    // group is optional.  The value of this string can be anything. Any recipes that have the same group name specified will be shown together in the crafting helper.
    // The intention is to keep similar items within the same category, such as all boats for example.
    // If you want to match an existing vanilla recipe group, look in the relevant recipe json in assets.minecraft.recipes

    ResourceLocation optionalGroup = new ResourceLocation("");

    // a) Shaped recipe without metadata - emerald surrounded by diamond makes ender eye
    GameRegistry.addShapedRecipe(new ResourceLocation("minecraftbyexample:mbe35_recipe_ender_eye"), optionalGroup, new ItemStack(Items.ENDER_EYE), new Object[]{
            " D ",
            "DED",
            " D ",
            'D', Items.DIAMOND,
            'E', Items.EMERALD     // note carefully - 'E' not "E" !
    });

    // note - smaller grids are also possible, you don't need to fill up the entire 3x3 space.

    // b) shaped recipe with metadata - cobblestone surrounded by red dye makes redstone
    final int RED_DYE_DAMAGE_VALUE = EnumDyeColor.RED.getDyeDamage();
    GameRegistry.addShapedRecipe(new ResourceLocation("minecraftbyexample:mbe35_recipe_redstone"), optionalGroup, new ItemStack(Items.REDSTONE), new Object[]{
            "RRR",
            "RCR",
            "RRR",
            'C', Blocks.COBBLESTONE,
            'R', new ItemStack(Items.DYE, 1, RED_DYE_DAMAGE_VALUE)
    });


    ResourceLocation woodSwordGroup = new ResourceLocation("mbe35_woodswordgroup");

    // c) shaped recipe for items which are damaged, or which have a metadata you want to ignore
    //      wooden sword (any damage value) in a cobblestone shell plus iron ingot makes iron sword
    GameRegistry.addShapedRecipe(new ResourceLocation("minecraftbyexample:mbe35_recipe_wood_to_iron_sword"), woodSwordGroup, new ItemStack(Items.IRON_SWORD), new Object[]{
            "CIC",
            "CWC",
            "CCC",
            'C', Blocks.COBBLESTONE,
            'W', new ItemStack(Items.WOODEN_SWORD, 1, OreDictionary.WILDCARD_VALUE),   // as of 1.12.2, you can also write simply Items.WOODEN_SWORD instead of new ItemStack,
                                                                                       // i.e. same as Items.IRON_INGOT on the next line
            'I', Items.IRON_INGOT
    });

    // for comparison - this recipe only works with an undamaged wooden sword
    //   wooden sword (undamaged) in a cobblestone shell plus gold ingot makes gold sword
    // NOTE - this has changed since 1.11.2
    GameRegistry.addShapedRecipe(new ResourceLocation("minecraftbyexample:mbe35_recipe_wood_to_gold_sword"), woodSwordGroup, new ItemStack(Items.GOLDEN_SWORD), new Object[]{
            "CIC",
            "CWC",
            "CCC",
            'C', Blocks.COBBLESTONE,
            'W', new ItemStack(Items.WOODEN_SWORD),
            'I', Items.GOLD_INGOT
    });

    // d) Shapeless recipe - blue dye plus yellow dye makes two green dye
    final int BLUE_DYE_DAMAGE_VALUE = EnumDyeColor.BLUE.getDyeDamage();
    final int YELLOW_DYE_DAMAGE_VALUE = EnumDyeColor.YELLOW.getDyeDamage();
    final int GREEN_DYE_DAMAGE_VALUE = EnumDyeColor.GREEN.getDyeDamage();
    final int NUMBER_OF_GREEN_DYE_PRODUCED = 2;

    GameRegistry.addShapelessRecipe(new ResourceLocation("minecraftbyexample:mbe35_recipe_greendye"), optionalGroup,
                                    new ItemStack(Items.DYE, NUMBER_OF_GREEN_DYE_PRODUCED, GREEN_DYE_DAMAGE_VALUE),
            new Ingredient[] {Ingredient.fromStacks(new ItemStack(Items.DYE, 1, YELLOW_DYE_DAMAGE_VALUE)),
                              Ingredient.fromStacks(new ItemStack(Items.DYE, 1, BLUE_DYE_DAMAGE_VALUE))
                             }
    );

    // g) Shaped Ore recipe - any type of tree leaves arranged around sticks makes a sapling
    //    Ores are a way for mods to add blocks & items which are equivalent to vanilla blocks for crafting
    //    For example - an ore recipe which uses "logWood" will accept a log of spruce, oak, birch, pine, etc.
    //    If your mod registers its balsawood log using  OreDictionary.registerOre("logWood", BalsaWood), then your
    //    BalsaWood log will also be accepted in the recipe.
    IRecipe saplingRecipe = new ShapedOreRecipe(optionalGroup,
            new ItemStack(Blocks.SAPLING), new Object[] {
            "LLL",
            "LSL",
            " S ",
            'S', Items.STICK,   // can use ordinary items, blocks, itemstacks in ShapedOreRecipe
            'L', "treeLeaves",  // look in OreDictionary for vanilla definitions
    });
    saplingRecipe.setRegistryName(new ResourceLocation("minecraftbyexample:mbe35_recipe_sapling"));
//    GameRegistry.register(saplingRecipe);
    GameData.register_impl(saplingRecipe);  // looks clumsy.  Not sure why GameRegistry doesn't have an appropriate register method.

    // h) by default, recipes are automatically mirror-imaged, i.e. you can flip the recipe left<--> right and it will
    //    produce the same output.  This one isn't.  Only works for OreRecipes, but you can make ShapedOreRecipe from vanilla
    //    Items or Blocks too (see (g) above)
    CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(new Object[]{
            false,
            "III",
            "I  ",
            "III",
            'I', Items.IRON_INGOT
    });
    primer.mirrored = false;
    IRecipe unmirroredRecipe = new ShapedOreRecipe(optionalGroup, new ItemStack(Items.CAULDRON), primer);
    unmirroredRecipe.setRegistryName(new ResourceLocation("minecraftbyexample:mbe35_recipe_cauldron"));
    GameData.register_impl(unmirroredRecipe);  // looks clumsy.  Not sure why GameRegistry doesn't have an appropriate register method.

    //---------------- FURNACE RECIPES (smelting)

    // d) smelting recipe - smelt cake gives you charcoal (coal with metadata value of 1)
    final float COAL_SMELT_XP = 0.1F;
    final float DIAMOND_SMELT_XP = 1.0F;
    final float CAKE_SMELT_XP = 0.0F;   // negative XP would probably cause a problem :)
    final int NUMBER_OF_ITEMS = 1;
    final int CHARCOAL_METADATA_VALUE = 1;
    GameRegistry.addSmelting(Items.CAKE, new ItemStack(Items.COAL, NUMBER_OF_ITEMS, CHARCOAL_METADATA_VALUE), CAKE_SMELT_XP);

    // e) fuel - use wheat as fuel in a furnace
    //   For your own item, override getItemBurnTime()
    //   For vanilla, need to register for the FurnaceFuelBurnTimeEvent, which is called whenever fuel is placed into a furnace
    //   Look in the FurnaceFuelBurnTimeEventHandler class for the details.
    MinecraftForge.EVENT_BUS.register(FurnaceFuelBurnTimeEventHandler.instance);

    //  ------------- Custom recipes

    // you can even register your own custom IRecipe class to match complicated inputs - see for example RecipeFireworks
    // GameData.register_impl(myRecipe implements IRecipe);
  }

  public static void postInitCommon()
  {
  }

}
