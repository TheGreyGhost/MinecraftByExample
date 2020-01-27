package minecraftbyexample;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * DedicatedServerProxy is used to set up the mod and start it running on dedicated servers.  It contains all the code that should run on the
 *   dedicated servers.  This is almost never required.
 *   For more background information see here http://greyminecraftcoder.blogspot.com/2013/11/how-forge-starts-up-your-code.html
 */
public class DedicatedServerProxy extends CommonProxy
{

  /**
   * Run before anything else. Read your config, create block, item, etc, and register them with the GameRegistry
   */
  public void preInit() {
    super.preInit();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init() {
    super.init();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit() {
    super.postInit();
  }

  @Override
  public boolean playerIsInCreativeMode(PlayerEntity player) {
    if (player instanceof ServerPlayerEntity) {
      ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity) player;
      return entityPlayerMP.interactionManager.isCreative();
    }
    return false;
  }

  @Override
  public boolean isDedicatedServer() {return true;}

}

