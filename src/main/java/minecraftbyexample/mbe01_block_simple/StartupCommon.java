package minecraftbyexample.mbe01_block_simple;

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
  public static BlockSimple blockSimple;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockSimple = (BlockSimple)(new BlockSimple().setUnlocalizedName("mbe01_block_simple"));
    GameRegistry.registerBlock(blockSimple, "mbe01_block_simple");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
