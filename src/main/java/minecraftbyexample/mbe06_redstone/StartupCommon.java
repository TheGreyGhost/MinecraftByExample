package minecraftbyexample.mbe06_redstone;

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
  public static BlockRedstoneSource blockRedstoneSource;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockRedstoneSource = (BlockRedstoneSource)(new BlockRedstoneSource().setUnlocalizedName("mbe06_block_redstone_source"));
    GameRegistry.registerBlock(blockRedstoneSource, "mbe06_block_redstone_source");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
