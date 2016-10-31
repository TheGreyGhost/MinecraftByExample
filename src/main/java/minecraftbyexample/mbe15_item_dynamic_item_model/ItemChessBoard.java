package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * User: The Grey Ghost <br>
 * Date: 30/12/2014     <p>
 *
 * ItemChessBoard is an ordinary two-dimensional item. <br>
 * For background information on items see <a href="http://greyminecraftcoder.blogspot.com/2013/12/items.html">here</a>
 * and <a href="http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html">here</a>.
 */
public class ItemChessBoard extends Item
{
	public ItemChessBoard()
	{
		final int MAXIMUM_NUMBER_OF_COUNTERS = 64;
		this.setMaxStackSize(MAXIMUM_NUMBER_OF_COUNTERS);
		this.setCreativeTab(CreativeTabs.MISC);
	}
}
