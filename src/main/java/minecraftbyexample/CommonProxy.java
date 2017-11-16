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
	   //read config first
	  minecraftbyexample.mbe70_configuration.StartupCommon.preInitCommon();

    minecraftbyexample.mbe01_block_simple.StartupCommon.preInitCommon();
    minecraftbyexample.mbe02_block_partial.StartupCommon.preInitCommon();
    minecraftbyexample.mbe03_block_variants.StartupCommon.preInitCommon();
    minecraftbyexample.mbe04_block_dynamic_block_model1.StartupCommon.preInitCommon();
    minecraftbyexample.mbe05_block_dynamic_block_model2.StartupCommon.preInitCommon();
    minecraftbyexample.mbe06_redstone.StartupCommon.preInitCommon();
    minecraftbyexample.mbe08_creative_tab.StartupCommon.preInitCommon();
    minecraftbyexample.mbe10_item_simple.StartupCommon.preInitCommon();
    minecraftbyexample.mbe11_item_variants.StartupCommon.preInitCommon();
    minecraftbyexample.mbe12_item_nbt_animate.StartupCommon.preInitCommon();
    minecraftbyexample.mbe13_item_tools.StartupCommon.preInitCommon();
    minecraftbyexample.mbe15_item_dynamic_item_model.StartupCommon.preInitCommon();
    minecraftbyexample.mbe20_tileentity_data.StartupCommon.preInitCommon();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupCommon.preInitCommon();
    minecraftbyexample.mbe30_inventory_basic.StartupCommon.preInitCommon();
    minecraftbyexample.mbe31_inventory_furnace.StartupCommon.preInitCommon();
    minecraftbyexample.mbe35_recipes.StartupCommon.preInitCommon();
    minecraftbyexample.mbe40_hud_overlay.StartupCommon.preInitCommon();
    minecraftbyexample.mbe50_particle.StartupCommon.preInitCommon();
    minecraftbyexample.mbe60_network_messages.StartupCommon.preInitCommon();
    minecraftbyexample.mbe75_testing_framework.StartupCommon.preInitCommon();
    minecraftbyexample.testingarea.StartupCommon.preInitCommon();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init()
  {
	minecraftbyexample.mbe70_configuration.StartupCommon.initCommon();
	  
    minecraftbyexample.mbe01_block_simple.StartupCommon.initCommon();
    minecraftbyexample.mbe02_block_partial.StartupCommon.initCommon();
    minecraftbyexample.mbe03_block_variants.StartupCommon.initCommon();
    minecraftbyexample.mbe04_block_dynamic_block_model1.StartupCommon.initCommon();
    minecraftbyexample.mbe05_block_dynamic_block_model2.StartupCommon.initCommon();
    minecraftbyexample.mbe06_redstone.StartupCommon.initCommon();
    minecraftbyexample.mbe08_creative_tab.StartupCommon.initCommon();
    minecraftbyexample.mbe10_item_simple.StartupCommon.initCommon();
    minecraftbyexample.mbe11_item_variants.StartupCommon.initCommon();
    minecraftbyexample.mbe12_item_nbt_animate.StartupCommon.initCommon();
    minecraftbyexample.mbe13_item_tools.StartupCommon.initCommon();
    minecraftbyexample.mbe15_item_dynamic_item_model.StartupCommon.initCommon();
    minecraftbyexample.mbe20_tileentity_data.StartupCommon.initCommon();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupCommon.initCommon();
    minecraftbyexample.mbe30_inventory_basic.StartupCommon.initCommon();
    minecraftbyexample.mbe31_inventory_furnace.StartupCommon.initCommon();
    minecraftbyexample.mbe35_recipes.StartupCommon.initCommon();
    minecraftbyexample.mbe40_hud_overlay.StartupCommon.initCommon();
    minecraftbyexample.mbe50_particle.StartupCommon.initCommon();
    minecraftbyexample.mbe60_network_messages.StartupCommon.initCommon();
    minecraftbyexample.mbe75_testing_framework.StartupCommon.initCommon();
//    minecraftbyexample.testingarea.StartupCommon.initCommon();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
	minecraftbyexample.mbe70_configuration.StartupCommon.postInitCommon();
	  
    minecraftbyexample.mbe01_block_simple.StartupCommon.postInitCommon();
    minecraftbyexample.mbe02_block_partial.StartupCommon.postInitCommon();
    minecraftbyexample.mbe03_block_variants.StartupCommon.postInitCommon();
    minecraftbyexample.mbe04_block_dynamic_block_model1.StartupCommon.postInitCommon();
    minecraftbyexample.mbe05_block_dynamic_block_model2.StartupCommon.postInitCommon();
    minecraftbyexample.mbe06_redstone.StartupCommon.postInitCommon();
    minecraftbyexample.mbe08_creative_tab.StartupCommon.postInitCommon();
    minecraftbyexample.mbe10_item_simple.StartupCommon.postInitCommon();
    minecraftbyexample.mbe11_item_variants.StartupCommon.postInitCommon();
    minecraftbyexample.mbe12_item_nbt_animate.StartupCommon.postInitCommon();
    minecraftbyexample.mbe13_item_tools.StartupCommon.postInitCommon();
    minecraftbyexample.mbe15_item_dynamic_item_model.StartupCommon.postInitCommon();
    minecraftbyexample.mbe20_tileentity_data.StartupCommon.postInitCommon();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupCommon.postInitCommon();
    minecraftbyexample.mbe30_inventory_basic.StartupCommon.postInitCommon();
    minecraftbyexample.mbe31_inventory_furnace.StartupCommon.postInitCommon();
    minecraftbyexample.mbe35_recipes.StartupCommon.postInitCommon();
    minecraftbyexample.mbe40_hud_overlay.StartupCommon.postInitCommon();
    minecraftbyexample.mbe50_particle.StartupCommon.postInitCommon();
    minecraftbyexample.mbe60_network_messages.StartupCommon.postInitCommon();
    minecraftbyexample.mbe75_testing_framework.StartupCommon.postInitCommon();
    minecraftbyexample.testingarea.StartupCommon.postInitCommon();
  }

  // helper to determine whether the given player is in creative mode
  //  not necessary for most examples
  abstract public boolean playerIsInCreativeMode(EntityPlayer player);

  /**
   * is this a dedicated server?
   * @return true if this is a dedicated server, false otherwise
   */
  abstract public boolean isDedicatedServer();
}
