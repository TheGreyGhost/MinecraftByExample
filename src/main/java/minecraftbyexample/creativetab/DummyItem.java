package minecraftbyexample.creativetab;

import net.minecraft.item.Item;

public class DummyItem extends Item {
  public DummyItem() {
    setUnlocalizedName("dummy_item");
    
    /* To add items to the creative tab, call setCreativeTab() with the static
     * reference of the creative tab that you want it to be in
     */
    setCreativeTab(Startup.anonymousTab);
  }
}
