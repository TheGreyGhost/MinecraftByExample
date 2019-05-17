package minecraftbyexample.mbe07_block_behavior;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class StartupCommon
{
  public static BlockDropper blockDropper;
  public static ItemBlock itemBlockDropper;

  public static void preInitCommon()
  {
    blockDropper = (BlockDropper)(new BlockDropper().setUnlocalizedName("mbe07_block_dropper_unlocalised_name"));
    blockDropper.setRegistryName("mbe07_block_dropper_registry_name");
    ForgeRegistries.BLOCKS.register(blockDropper);

    itemBlockDropper = new ItemBlock(blockDropper);
    itemBlockDropper.setRegistryName(blockDropper.getRegistryName());
    ForgeRegistries.ITEMS.register(itemBlockDropper);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
