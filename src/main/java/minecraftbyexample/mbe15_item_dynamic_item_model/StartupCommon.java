package minecraftbyexample.mbe15_item_dynamic_item_model;

import minecraftbyexample.mbe11_item_variants.ItemVariants;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemChessBoard itemChessBoard;  // this holds the unique instance of the chessboard item

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    itemChessBoard = new ItemChessBoard();
    itemChessBoard.setRegistryName("mbe15_item_chessboard_registry_name");
    itemRegisterEvent.getRegistry().register(itemChessBoard);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}
