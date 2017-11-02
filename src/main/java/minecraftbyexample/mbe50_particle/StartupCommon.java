package minecraftbyexample.mbe50_particle;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockFlameEmitter blockFlameEmitter;  // this holds the unique instance of your block
  public static ItemBlock itemBlockFlameEmitter;  // the itemBlock corresponding to the block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockFlameEmitter = (BlockFlameEmitter)(new BlockFlameEmitter().setUnlocalizedName("mbe50_block_flame_emitter_unlocalised_name"));
    blockFlameEmitter.setRegistryName("mbe50_block_flame_emitter_registry_name");
    ForgeRegistries.BLOCKS.register(blockFlameEmitter);

    // We also need to create and register an ItemBlock for this block otherwise it won't appear in the inventory
    itemBlockFlameEmitter = new ItemBlock(blockFlameEmitter);
    itemBlockFlameEmitter.setRegistryName(blockFlameEmitter.getRegistryName());
    ForgeRegistries.ITEMS.register(itemBlockFlameEmitter);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
