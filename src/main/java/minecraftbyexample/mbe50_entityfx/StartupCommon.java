package minecraftbyexample.mbe50_entityfx;

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
  public static BlockFlameEmitter blockFlameEmitter;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockFlameEmitter = (BlockFlameEmitter)(new BlockFlameEmitter().setUnlocalizedName("mbe50_block_flame_emitter"));
    GameRegistry.registerBlock(blockFlameEmitter, "mbe50_block_flame_emitter");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
