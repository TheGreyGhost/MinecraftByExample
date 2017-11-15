package minecraftbyexample.mbe35_recipes;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 13/11/2017.
 *  Turns vanilla item (wheat) into a fuel.  Can also be used for more-complicated calculations on whether the
 *    item is a fuel or not, for example depending on the time of day, or whether it's raining or not, or similar.
 *  For your own items, override Item.getItemBurnTime(ItemStack)
 */
public class FurnaceFuelBurnTimeEventHandler {
  public static final FurnaceFuelBurnTimeEventHandler instance = new FurnaceFuelBurnTimeEventHandler();

  private FurnaceFuelBurnTimeEventHandler() {};

  // Called whenever fuel is added into a furnace
  // Allows us to check whether the item added into the fuel slot is burnable.  If it is Wheat, burn it.
  @SubscribeEvent
  public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event)
  {
    ItemStack fuel = event.getItemStack();
    final int BURN_TIME_SECONDS = 5;
    final int TICKS_PER_SECOND = 20;
    if (fuel.getItem() == Items.WHEAT) {
      event.setBurnTime(BURN_TIME_SECONDS * TICKS_PER_SECOND);
    }
  }
}
