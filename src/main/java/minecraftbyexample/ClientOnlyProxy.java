package minecraftbyexample;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * ClientProxy is used to set up the mod and start it running on normal minecraft.  It contains all the code that should run on the
 *   client side only.
 *   For more background information see here http://greyminecraftcoder.blogspot.com/2013/11/how-forge-starts-up-your-code.html
 */
public class ClientOnlyProxy extends CommonProxy
{

  /**
   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
   */
  public void preInit()
  {
    super.preInit();
    minecraftbyexample.mbe70_configuration.StartupClientOnly.preInitClientOnly();
    
    minecraftbyexample.mbe01_block_simple.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe02_block_partial.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe03_block_variants.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe04_block_dynamic_block_model1.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe05_block_dynamic_block_model2.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe06_redstone.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe08_creative_tab.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe10_item_simple.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe11_item_variants.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe13_item_tools.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe15_item_dynamic_item_model.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe35_recipes.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe40_hud_overlay.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe50_particle.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe60_network_messages.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe75_testing_framework.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.testingarea.StartupClientOnly.preInitClientOnly();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init()
  {
    super.init();
    minecraftbyexample.mbe70_configuration.StartupClientOnly.initClientOnly();
    
    minecraftbyexample.mbe01_block_simple.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe02_block_partial.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe03_block_variants.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe04_block_dynamic_block_model1.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe05_block_dynamic_block_model2.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe06_redstone.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe08_creative_tab.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe10_item_simple.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe11_item_variants.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe13_item_tools.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe15_item_dynamic_item_model.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe35_recipes.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe40_hud_overlay.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe50_particle.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe60_network_messages.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe75_testing_framework.StartupClientOnly.initClientOnly();
    minecraftbyexample.testingarea.StartupClientOnly.initClientOnly();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
    super.postInit();
    minecraftbyexample.mbe70_configuration.StartupClientOnly.postInitClientOnly();

    minecraftbyexample.mbe01_block_simple.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe02_block_partial.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe03_block_variants.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe04_block_dynamic_block_model1.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe05_block_dynamic_block_model2.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe06_redstone.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe08_creative_tab.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe10_item_simple.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe11_item_variants.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe13_item_tools.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe15_item_dynamic_item_model.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe35_recipes.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe40_hud_overlay.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe50_particle.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe60_network_messages.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe75_testing_framework.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.testingarea.StartupClientOnly.postInitClientOnly();
  }

  @Override
  public boolean playerIsInCreativeMode(EntityPlayer player) {
    if (player instanceof EntityPlayerMP) {
      EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
      return entityPlayerMP.interactionManager.isCreative();
    } else if (player instanceof EntityPlayerSP) {
      return Minecraft.getMinecraft().playerController.isInCreativeMode();
    }
    return false;
  }

  @Override
  public boolean isDedicatedServer() {return false;}

}
