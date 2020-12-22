package minecraftbyexample.mbe30b_inventory_basic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by TGG on 22/12/2020.
 */
public class SlotItemHandlerMBE extends SlotItemHandler {
  public SlotItemHandlerMBE(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn)
  {

  }
}
