package minecraftbyexample.creativetab;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class DummyBlock extends Block {
  public DummyBlock() {
    super(Material.rock);
    setUnlocalizedName("dummy_block");
    
    /* To add blocks to the creative tab, call setCreativeTab() with the static
     * reference of the creative tab that you want it to be in
     */
    setCreativeTab(Startup.myTab);
  }
}
