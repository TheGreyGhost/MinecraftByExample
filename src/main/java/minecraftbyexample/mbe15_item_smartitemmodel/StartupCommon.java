package minecraftbyexample.mbe15_item_smartitemmodel;

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
  public static ItemChessBoard itemChessBoard;  // this holds the unique instance of the chessboard item

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemChessBoard = (ItemChessBoard)(new ItemChessBoard().setUnlocalizedName("mbe15_item_chessboard"));
    GameRegistry.registerItem(itemChessBoard, "mbe15_item_chessboard");
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
