package minecraftbyexample.mbe17_block_crops;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeedFood;

import static minecraftbyexample.mbe17_block_crops.StartupCommon.blockBlueberries;

public class ItemBlueberries extends ItemSeedFood {
  public ItemBlueberries() {
    super(2, 0.1f, blockBlueberries, Blocks.FARMLAND);
  }
}
