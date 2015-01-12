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
    minecraftbyexample.mbe01_block_simple.Startup.preInitClientOnly();
    minecraftbyexample.mbe02_block_partial.Startup.preInitClientOnly();
    minecraftbyexample.mbe03_block_variants.Startup.preInitClientOnly();
    minecraftbyexample.mbe10_item_simple.Startup.preInitClientOnly();
    minecraftbyexample.mbe11_item_variants.Startup.preInitClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.Startup.preInitClientOnly();
    minecraftbyexample.mbe13_item_tools.Startup.preInitClientOnly();
    minecraftbyexample.mbe20_tileentity_data.Startup.preInitClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.Startup.preInitClientOnly();
    minecraftbyexample.testingarea.Startup.preInitClientOnly();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init()
  {
    super.init();
    minecraftbyexample.mbe01_block_simple.Startup.initClientOnly();
    minecraftbyexample.mbe02_block_partial.Startup.initClientOnly();
    minecraftbyexample.mbe03_block_variants.Startup.initClientOnly();
    minecraftbyexample.mbe10_item_simple.Startup.initClientOnly();
    minecraftbyexample.mbe11_item_variants.Startup.initClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.Startup.initClientOnly();
    minecraftbyexample.mbe13_item_tools.Startup.initClientOnly();
    minecraftbyexample.mbe20_tileentity_data.Startup.initClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.Startup.initClientOnly();
    minecraftbyexample.testingarea.Startup.initClientOnly();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
    super.postInit();
    minecraftbyexample.mbe01_block_simple.Startup.postInitClientOnly();
    minecraftbyexample.mbe02_block_partial.Startup.postInitClientOnly();
    minecraftbyexample.mbe03_block_variants.Startup.postInitClientOnly();
    minecraftbyexample.mbe10_item_simple.Startup.postInitClientOnly();
    minecraftbyexample.mbe11_item_variants.Startup.postInitClientOnly();
    minecraftbyexample.mbe12_item_nbt_animate.Startup.postInitClientOnly();
    minecraftbyexample.mbe13_item_tools.Startup.postInitClientOnly();
    minecraftbyexample.mbe20_tileentity_data.Startup.postInitClientOnly();
    minecraftbyexample.mbe21_tileentityspecialrenderer.Startup.postInitClientOnly();
    minecraftbyexample.testingarea.Startup.postInitClientOnly();
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
