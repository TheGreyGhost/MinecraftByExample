package minecraftbyexample.mbe06_redstone;

import minecraftbyexample.mbe06_redstone.input.BlockRedstoneColouredLamp;
import minecraftbyexample.mbe06_redstone.input.TileEntityRedstoneColouredLamp;
import minecraftbyexample.mbe06_redstone.input_and_output.BlockRedstoneMeter;
import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneTarget;
import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneVariableSource;
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
  public static BlockRedstoneColouredLamp blockRedstoneColouredLamp;

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockRedstoneVariableSource = (BlockRedstoneVariableSource)(new BlockRedstoneVariableSource()
            .setUnlocalizedName("mbe06_block_redstone_variable_source"));
    GameRegistry.registerBlock(blockRedstoneVariableSource, "mbe06_block_redstone_variable_source");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.

    blockRedstoneTarget = (BlockRedstoneTarget)(new BlockRedstoneTarget().setUnlocalizedName("mbe06b_block_redstone_target"));
    GameRegistry.registerBlock(blockRedstoneTarget, "mbe06b_block_redstone_target");

    blockRedstoneMeter = (BlockRedstoneMeter)(new BlockRedstoneMeter().setUnlocalizedName("mbe06c_block_redstone_meter"));
    GameRegistry.registerBlock(blockRedstoneMeter, "mbe06c_block_redstone_meter");
    GameRegistry.registerTileEntity(TileEntityRedstoneMeter.class, "mbe06c_tesr_te");

    blockRedstoneColouredLamp = (BlockRedstoneColouredLamp)(new BlockRedstoneColouredLamp()
            .setUnlocalizedName("mbe06d_block_redstone_coloured_lamp"));
    GameRegistry.registerBlock(blockRedstoneColouredLamp, "mbe06d_block_redstone_coloured_lamp");
    GameRegistry.registerTileEntity(TileEntityRedstoneColouredLamp.class, "mbe06d_te_redstone_coloured_lamp");

  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
