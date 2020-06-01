package minecraftbyexample.mbe08_itemgroup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

// This creative tab is very similar to the basic CreativeTab, but overrides displayAllReleventItems to
//  customise the list of displayed item - filters through all the item looking for ones whose namespace is
//  "minecraftbyexample"

public class AllMbeItemsItemGroup extends ItemGroup {
  public AllMbeItemsItemGroup(String label) {
    super(label);
  }

  @Override
  public ItemStack createIcon() {
    return new ItemStack(Items.BOOK);
  }

  // The code below is not necessary for your own ItemGroup, if you specify the ItemGroup within your item
  //  eg
  // public class ItemSimple extends Item {
  //  public ItemSimple() {
  //    super(new Item.Properties().group(StartupCommon.allMbeItemsItemGroup)
  //    );
  //  }
  //
  @Override
  public void fill(NonNullList<ItemStack> itemsToShowOnTab)
  {
    for (Item item : ForgeRegistries.ITEMS) {
      if (item != null) {
        if (item.getRegistryName().getNamespace().equals("minecraftbyexample")) {
          item.fillItemGroup(ItemGroup.SEARCH, itemsToShowOnTab);  // Specifying CreativeTabs.SEARCH will find all items even if they belong to another tab
        }
      }
    }
  }

}
