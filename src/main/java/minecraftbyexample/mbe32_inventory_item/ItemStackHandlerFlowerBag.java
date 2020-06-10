package minecraftbyexample.mbe32_inventory_item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by TGG on 7/06/2020.
 *
 * Used to store Flower ItemStacks.  Will only accept SMALL_FLOWERS and TALL_FLOWERS
 */
public class ItemStackHandlerFlowerBag extends ItemStackHandler {

  public static final int MIN_FLOWER_SLOTS = 1;
  public static final int MAX_FLOWER_SLOTS = 16;

  public ItemStackHandlerFlowerBag(int numberOfSlots) {
    super(MathHelper.clamp(numberOfSlots, MIN_FLOWER_SLOTS, MAX_FLOWER_SLOTS));
    if (numberOfSlots < MIN_FLOWER_SLOTS || numberOfSlots > MAX_FLOWER_SLOTS) {
      throw new IllegalArgumentException("Invalid number of flower slots:"+numberOfSlots);
    }
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    if (slot < 0 || slot >= MAX_FLOWER_SLOTS) {
      throw new IllegalArgumentException("Invalid slot number:"+slot);
    }
    if (stack.isEmpty()) return false;
    Item item = stack.getItem();
    if (item.isIn(ItemTags.SMALL_FLOWERS) || item.isIn(ItemTags.TALL_FLOWERS)) return true;
    return false;
  }
}
