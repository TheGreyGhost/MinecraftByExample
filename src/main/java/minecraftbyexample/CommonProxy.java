package minecraftbyexample;

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
    minecraftbyexample.testingarea.Startup.initCommon();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
    minecraftbyexample.mbe01_block_simple.Startup.postInitCommon();
    minecraftbyexample.mbe02_block_partial.Startup.postInitCommon();
    minecraftbyexample.testingarea.Startup.postInitCommon();
  }
}
