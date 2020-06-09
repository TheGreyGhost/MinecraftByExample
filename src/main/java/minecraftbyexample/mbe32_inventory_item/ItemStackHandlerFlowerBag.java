package minecraftbyexample.mbe32_inventory_item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by TGG on 7/06/2020.
 *
 * Used to store Flower ItemStacks.  Will only accept SMALL_FLOWERS and TALL_FLOWERS
 */
public class ItemStackHandlerFlowerBag extends ItemStackHandler {

  public ItemStackHandlerFlowerBag(int maxNumberOfContents) {
    super(maxNumberOfContents);
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    if (stack.isEmpty()) return false;
    Item item = stack.getItem();
    if (item.isIn(ItemTags.SMALL_FLOWERS) || item.isIn(ItemTags.TALL_FLOWERS)) return true;
    return false;
  }
}
