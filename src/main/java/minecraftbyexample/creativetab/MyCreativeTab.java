package minecraftbyexample.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MyCreativeTab extends CreativeTabs {
  
  /* The advantage of extending a class instead of using an anonymous class is that
   * you can modify the constructor of this class. Here, I modify the constructor to
   * prepend a prefix to the name that I pass it. 
   * 
   * More importantly, extending a class lets you keep the code in a different file,
   * which is extremely important when your classes get large.
   */
  public MyCreativeTab(String label) {
    super("prefix_" + label);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Item getTabIconItem() {
    return Item.getItemFromBlock(Blocks.gold_block);
  }
  
}
