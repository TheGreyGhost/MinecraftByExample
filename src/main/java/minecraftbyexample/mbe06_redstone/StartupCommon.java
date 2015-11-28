package minecraftbyexample.mbe06_redstone;

import minecraftbyexample.mbe06_redstone.redstone_meter.BlockRedstoneMeter;
import minecraftbyexample.mbe06_redstone.redstone_meter.TileEntityRedstoneMeter;
import minecraftbyexample.mbe21_tileentityspecialrenderer.TileEntityMBE21;
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
  public static BlockRedstoneVariableSource blockRedstoneVariableSource;
  public static BlockRedstoneTarget blockRedstoneTarget;
  public static BlockRedstoneMeter blockRedstoneMeter;

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockRedstoneVariableSource = (BlockRedstoneVariableSource)(new BlockRedstoneVariableSource().setUnlocalizedName("mbe06_block_redstone_variable_source"));
    GameRegistry.registerBlock(blockRedstoneVariableSource, "mbe06_block_redstone_variable_source");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.

    blockRedstoneTarget = (BlockRedstoneTarget)(new BlockRedstoneTarget().setUnlocalizedName("mbe06b_block_redstone_target"));
    GameRegistry.registerBlock(blockRedstoneTarget, "mbe06b_block_redstone_target");

    blockRedstoneMeter = (BlockRedstoneMeter)(new BlockRedstoneMeter().setUnlocalizedName("mbe06c_block_redstone_meter"));
    GameRegistry.registerBlock(blockRedstoneMeter, "mbe06c_block_redstone_meter");

    GameRegistry.registerTileEntity(TileEntityRedstoneMeter.class, "mbe06c_tesr_te");

  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
