package minecraftbyexample.mbe35_recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

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

    // a) Shaped recipe without metadata - emerald surrounded by diamond makes ender eye
    GameRegistry.addRecipe(new ItemStack(Items.ender_eye), new Object[]{
            ".D.",
            "DED",
            ".D.",
                  'D', Items.diamond,
                  'E', Items.emerald     // note carefully - 'E' not "E" !
    });

    // note - smaller grids are also possible, you don't need to fill up the entire 3x3 space.

    // b) shaped recipe with metadata - cobblestone surrounded by red dye makes redstone
    final int RED_DYE_DAMAGE_VALUE = EnumDyeColor.RED.getDyeDamage();
    GameRegistry.addRecipe(new ItemStack(Items.redstone), new Object[]{
            "RRR",
            "RCR",
            "RRR",
                  'C', Blocks.cobblestone,
                  'R', new ItemStack(Items.dye, 1, RED_DYE_DAMAGE_VALUE)
    });

    // c) shaped recipe for items which are damaged, or which have a metadata you want to ignore
    //      wooden sword (any damage value) in a cobblestone shell plus iron ingot makes iron sword
    GameRegistry.addRecipe(new ItemStack(Items.iron_sword), new Object[]{
            "CIC",
            "CWC",
            "CCC",
                  'C', Blocks.cobblestone,
                  'W', new ItemStack(Items.wooden_sword, 1, OreDictionary.WILDCARD_VALUE),
                  'I', Items.iron_ingot
    });

    // for comparison - this recipe only words with an undamaged wooden sword
    //   wooden sword (undamaged) in a cobblestone shell plus gold ingot makes gold sword
    GameRegistry.addRecipe(new ItemStack(Items.golden_sword), new Object[]{
            "CIC",
            "CWC",
            "CCC",
            'C', Blocks.cobblestone,
            'W', Items.wooden_sword,
            'I', Items.gold_ingot
    });

    // d) Shapeless recipe - blue dye plus yellow dye makes two green dye
    final int BLUE_DYE_DAMAGE_VALUE = EnumDyeColor.BLUE.getDyeDamage();
    final int YELLOW_DYE_DAMAGE_VALUE = EnumDyeColor.YELLOW.getDyeDamage();
    final int GREEN_DYE_DAMAGE_VALUE = EnumDyeColor.GREEN.getDyeDamage();
    final int NUMBER_OF_GREEN_DYE_PRODUCED = 2;
    GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, NUMBER_OF_GREEN_DYE_PRODUCED, GREEN_DYE_DAMAGE_VALUE),
            new Object[] {
                    new ItemStack(Items.dye, 1, YELLOW_DYE_DAMAGE_VALUE),
                    new ItemStack(Items.dye, 1, BLUE_DYE_DAMAGE_VALUE)
    });

    // g) Shaped Ore recipe - any type of tree leaves arranged around sticks makes a sapling
    //    Ores are a way for mods to add blocks & items which are equivalent to vanilla blocks for crafting
    //    For example - an ore recipe which uses "logWood" will accept a log of spruce, oak, birch, pine, etc.
    //    If your mod registers its balsawood log using  OreDictionary.registerOre("logWood", BalsaWood), then your
    //    BalsaWood log will also be accepted in the recipe.
    IRecipe saplingRecipe = new ShapedOreRecipe(new ItemStack(Blocks.sapling), new Object[] {
            "LLL",
            "LSL",
            ".S.",
            'S', Items.stick,   // can use ordinary items, blocks, itemstacks in ShapedOreRecipe
            'L', "treeLeaves",  // look in OreDictionary for vanilla definitions
    });
    GameRegistry.addRecipe(saplingRecipe);

    // h) by default, recipes are automatically mirror-imaged, i.e. you can flip the recipe left<--> right and it will
    //    produce the same output.  This one isn't.  Only works for OreRecipes, but you can make ShapedOreRecipe from vanilla
    //    Items or Blocks too (see (g) above)
    IRecipe unmirroredRecipe = new ShapedOreRecipe(new ItemStack(Items.cauldron), new Object[] {
            false,
            "III",
            "I..",
            "III",
            'I', Items.iron_ingot
    });
    GameRegistry.addRecipe(unmirroredRecipe);

    //---------------- FURNACE RECIPES (smelting)

    // d) smelting recipe - smelt cake gives you charcoal (coal with metadata value of 1)
    final float COAL_SMELT_XP = 0.1F;
    final float DIAMOND_SMELT_XP = 1.0F;
    final float CAKE_SMELT_XP = 0.0F;   // negative XP would probably cause a problem :)
    final int NUMBER_OF_ITEMS = 1;
    final int CHARCOAL_METADATA_VALUE = 1;
    GameRegistry.addSmelting(Items.cake, new ItemStack(Items.coal, NUMBER_OF_ITEMS, CHARCOAL_METADATA_VALUE), CAKE_SMELT_XP);

    // e) fuel - use wheat as fuel in a furnace
    //   We use an anonymous class... you can use an ordinary class instead if you prefer.
    IFuelHandler wheatFuelHandler = new IFuelHandler() {
      @Override
      public int getBurnTime(ItemStack fuel) {
        final int BURN_TIME_SECONDS = 5;
        final int TICKS_PER_SECOND = 20;
        if (fuel != null && fuel.getItem() == Items.wheat) {
          return BURN_TIME_SECONDS * TICKS_PER_SECOND;
        } else {
          return 0;
        }
      }
    };
    GameRegistry.registerFuelHandler(wheatFuelHandler);

    //  ------------- Custom recipes

    // you can even register your own custom IRecipe class to match complicated inputs - see for example RecipeFireworks
    // GameRegister.addRecipe(myRecipe implements IRecipe);
  }

  public static void postInitCommon()
  {
  }

}
