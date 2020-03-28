package minecraftbyexample.mbe08_creative_tab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// This creative tab is very similar to the basic CreativeTab, but overrides displayAllReleventItems to
//  customise the list of displayed item - filters through all the item looking for ones whose name starts
//  with "mbe"

//doesn't work any more.
//
//        Need to override IForgeIetm    default java.util.Collection<ItemGroup> getCreativeTabs()
//        '
//or
//Item.fillItemGroup perhaps
//

public class AllMbeItemsItemGroup extends ItemGroup {
  public AllMbeItemsItemGroup(String label) {
    super(label);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public ItemStack createIcon() {
    return new ItemStack(Items.BOOK);
  }

//  @OnlyIn(Dist.CLIENT)
//  @Override
//  public void displayAllRelevantItems(NonNullList<ItemStack> itemsToShowOnTab)
//  {
//    for (Item item : Item.REGISTRY) {
//      if (item != null) {
//        if (item.getUnlocalizedName().contains(".mbe")) {
//          item.getSubItems(ItemGroup.SEARCH, itemsToShowOnTab);  // CreativeTabs.SEARCH will find all item even if they belong to another tab
//                                                                    //   except if the item has no tab (item.getCreativeTab() == NULL)
//        }
//      }
//    }
//  }

}
