package minecraftbyexample.mbe21_tileentityspecialrenderer;

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
  public static BlockMBE21 blockMBE21;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockMBE21 = (BlockMBE21)(new BlockMBE21().setUnlocalizedName("mbe21_tesr_block"));
    GameRegistry.registerBlock(blockMBE21, "mbe21_tesr_block");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.
    GameRegistry.registerTileEntity(TileEntityMBE21.class, "mbe21_tesr_te");
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
