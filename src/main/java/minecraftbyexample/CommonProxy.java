package minecraftbyexample;

import net.minecraft.entity.player.EntityPlayer;

/**
 * CommonProxy is used to set up the mod and start it running.  It contains all the code that should run on both the
 *   Standalone client and the dedicated server.
 *   For more background information see here http://greyminecraftcoder.blogspot.com/2013/11/how-forge-starts-up-your-code.html
 */
public abstract class CommonProxy {

  /**
   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
   */
  public void preInit()
  {
    minecraftbyexample.mbe01_block_simple.Startup.preInitCommon();
    minecraftbyexample.mbe02_block_partial.Startup.preInitCommon();
    minecraftbyexample.mbe03_block_variants.Startup.preInitCommon();
    minecraftbyexample.mbe10_item_simple.Startup.preInitCommon();
    minecraftbyexample.mbe11_item_variants.Startup.preInitCommon();
    minecraftbyexample.mbe12_item_nbt_animate.Startup.preInitCommon();
    minecraftbyexample.mbe13_item_tools.Startup.preInitCommon();
    minecraftbyexample.mbe20_tileentity_data.Startup.preInitCommon();
    minecraftbyexample.testingarea.Startup.preInitCommon();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init()
  {
    minecraftbyexample.mbe01_block_simple.Startup.initCommon();
    minecraftbyexample.mbe02_block_partial.Startup.initCommon();
    minecraftbyexample.mbe03_block_variants.Startup.initCommon();
    minecraftbyexample.mbe10_item_simple.Startup.initCommon();
    minecraftbyexample.mbe11_item_variants.Startup.initCommon();
    minecraftbyexample.mbe12_item_nbt_animate.Startup.initCommon();
    minecraftbyexample.mbe13_item_tools.Startup.initCommon();
    minecraftbyexample.mbe20_tileentity_data.Startup.initCommon();
    minecraftbyexample.testingarea.Startup.initCommon();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
    minecraftbyexample.mbe01_block_simple.Startup.postInitCommon();
    minecraftbyexample.mbe02_block_partial.Startup.postInitCommon();
    minecraftbyexample.mbe03_block_variants.Startup.postInitCommon();
    minecraftbyexample.mbe10_item_simple.Startup.postInitCommon();
    minecraftbyexample.mbe11_item_variants.Startup.postInitCommon();
    minecraftbyexample.mbe12_item_nbt_animate.Startup.postInitCommon();
    minecraftbyexample.mbe13_item_tools.Startup.postInitCommon();
    minecraftbyexample.mbe20_tileentity_data.Startup.postInitCommon();
    minecraftbyexample.testingarea.Startup.postInitCommon();
  }

  // helper to determine whether the given player is in creative mode
  //  not necessary for most examples
  abstract public boolean playerIsInCreativeMode(EntityPlayer player);

}
