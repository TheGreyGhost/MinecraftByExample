package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemChessBoard is an ordinary two-dimensional item; the based model is auto-generated from the item's texture
 */
public class ItemChessBoard extends Item
{
  static public int MAXIMUM_NUMBER_OF_COUNTERS = 64;
  public ItemChessBoard()
  {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_COUNTERS).group(ItemGroup.MISC));
  }
}
