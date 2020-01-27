package minecraftbyexample.mbe08_creative_tab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// This creative tab is very similar to the basic CreativeTab, but overrides displayAllReleventItems to
//  customise the list of displayed items - filters through all the items looking for ones whose name starts
//  with "mbe"

public class AllMbeItemsTab extends ItemGroup {
  public AllMbeItemsTab(String label) {
    super(label);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public ItemStack getTabIconItem() {
    return new ItemStack(Items.BOOK);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void displayAllRelevantItems(NonNullList<ItemStack> itemsToShowOnTab)
  {
    for (Item item : Item.REGISTRY) {
      if (item != null) {
        if (item.getUnlocalizedName().contains(".mbe")) {
          item.getSubItems(ItemGroup.SEARCH, itemsToShowOnTab);  // CreativeTabs.SEARCH will find all items even if they belong to another tab
                                                                    //   except if the item has no tab (item.getCreativeTab() == NULL)
        }
      }
    }
  }

}
