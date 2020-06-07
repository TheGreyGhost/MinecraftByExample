package minecraftbyexample.mbe32_inventory_item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by TGG on 7/06/2020.
 */
public class ItemStackHandlerFlowerBag extends ItemStackHandler {

  public ItemStackHandlerFlowerBag(int maxNumberOfContents) {
    super(maxNumberOfContents);
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    Block blk = Block.getBlockFromItem(stack.getItem());
    return !stack.isEmpty()
            && blk.getClass() == BlockModFlower.class
            && slot == ((BlockModFlower) blk).color.getId();
  }
}
