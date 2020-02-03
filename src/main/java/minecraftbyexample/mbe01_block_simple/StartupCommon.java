package minecraftbyexample.mbe01_block_simple;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 * onBlocksRegistration then onItemsRegistration
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockSimple blockSimple;  // this holds the unique instance of your block
  public static BlockItem itemBlockSimple;  // this holds the unique instance of the ItemBlock corresponding to your block

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockSimple = (BlockSimple)(new BlockSimple().setRegistryName("minecraftbyexample", "mbe01_block_simple_registry_name"));
    blockRegisterEvent.getRegistry().register(blockSimple);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    Item.Properties itemSimpleProperties = new Item.Properties().maxStackSize(20).group(ItemGroup.BUILDING_BLOCKS);
    itemBlockSimple = new BlockItem(blockSimple, itemSimpleProperties);
    itemRegisterEvent.getRegistry().register(itemBlockSimple);
  }
}
