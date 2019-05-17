package minecraftbyexample.mbe17_block_crops;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class StartupCommon
{
  public static BlockBlueberries blockBlueberries;
  public static ItemBlock itemBlockBlueberries;
  public static ItemBlueberries itemBlueberries;

  public static void preInitCommon()
  {
    blockBlueberries = (BlockBlueberries)(new BlockBlueberries().setUnlocalizedName("mbe17_block_blueberries_unlocalised_name"));
    blockBlueberries.setRegistryName("mbe17_block_blueberries_registry_name");
    ForgeRegistries.BLOCKS.register(blockBlueberries);

    itemBlockBlueberries = new ItemBlock(blockBlueberries);
    itemBlockBlueberries.setRegistryName(blockBlueberries.getRegistryName());
    ForgeRegistries.ITEMS.register(itemBlockBlueberries);

    itemBlueberries = (ItemBlueberries)(new ItemBlueberries().setUnlocalizedName("mbe17_item_blueberries_unlocalised_name"));
    itemBlueberries.setRegistryName("mbe17_item_blueberries_registry_name");
    ForgeRegistries.ITEMS.register(itemBlueberries);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
