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
    minecraftbyexample.mbe01_block_simple.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe02_block_partial.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe03_block_variants.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe08_creative_tab.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe10_item_simple.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe11_item_variants.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe13_item_tools.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe14_item_camera_transforms.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe40_hud_overlay.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.mbe60_network_messages.StartupClientOnly.preInitClientOnly();
    minecraftbyexample.testingarea.StartupClientOnly.preInitClientOnly();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init()
  {
    super.init();
    minecraftbyexample.mbe01_block_simple.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe02_block_partial.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe03_block_variants.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe08_creative_tab.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe10_item_simple.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe11_item_variants.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe13_item_tools.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe14_item_camera_transforms.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe40_hud_overlay.StartupClientOnly.initClientOnly();
    minecraftbyexample.mbe60_network_messages.StartupClientOnly.initClientOnly();
    minecraftbyexample.testingarea.StartupClientOnly.initClientOnly();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
    super.postInit();
    minecraftbyexample.mbe01_block_simple.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe02_block_partial.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe03_block_variants.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe08_creative_tab.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe10_item_simple.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe11_item_variants.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe13_item_tools.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe14_item_camera_transforms.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe40_hud_overlay.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.mbe60_network_messages.StartupClientOnly.postInitClientOnly();
    minecraftbyexample.testingarea.StartupClientOnly.postInitClientOnly();
  }

  @Override
  public boolean playerIsInCreativeMode(EntityPlayer player) {
    if (player instanceof EntityPlayerMP) {
      EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
      return entityPlayerMP.theItemInWorldManager.isCreative();
    } else if (player instanceof EntityPlayerSP) {
      return Minecraft.getMinecraft().playerController.isInCreativeMode();
    }
    return false;
  }
}
