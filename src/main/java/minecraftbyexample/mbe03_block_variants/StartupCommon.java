package minecraftbyexample.mbe03_block_variants;

import net.minecraftforge.fml.common.registry.GameRegistry;

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
  public static BlockVariants blockVariants;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockVariants = (BlockVariants)(new BlockVariants().setUnlocalizedName("mbe03_block_variants"));
    GameRegistry.registerBlock(blockVariants, ItemBlockVariants.class, "mbe03_block_variants");
    // you don't need to register any items corresponding to the block, GameRegistry.registerBlock does this automatically
    //   when you supply the custom Item class (ItemVariants), creating an item with ItemVariants(blockVariants)
    //  You can access that item using GameRegistry.findItem

  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
