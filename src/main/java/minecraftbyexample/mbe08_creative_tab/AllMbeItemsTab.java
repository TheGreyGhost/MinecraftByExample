package minecraftbyexample.mbe08_creative_tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

// This creative tab is very similar to the basic CreativeTab, but overrides displayAllReleventItems to
//  customise the list of displayed items - filters through all the items looking for ones whose name starts
//  with "mbe"

public class AllMbeItemsTab extends CreativeTabs {
  public AllMbeItemsTab(String label) {
    super(label);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Item getTabIconItem() {
    return Items.book;
  }

  @Override
  public void displayAllReleventItems(List itemsToShowOnTab)
  {
    for (Object itemObject : Item.itemRegistry) {
      Item item = (Item)itemObject;
      if (item != null) {
        if (item.getUnlocalizedName().contains(".mbe")) {
          item.getSubItems(item, this, itemsToShowOnTab);  // add all sub items to the list
        }
      }
    }
  }

}
