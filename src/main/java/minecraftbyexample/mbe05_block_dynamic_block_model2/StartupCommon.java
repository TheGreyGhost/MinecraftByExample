package minecraftbyexample.mbe05_block_dynamic_block_model2;

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
 * These methods are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockGlassLantern blockGlassLantern;  // this holds the unique instance of your block
  public static BlockItem itemBlockGlassLantern;  // this holds the unique instance of the ItemBlock corresponding to your block

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockGlassLantern= (BlockGlassLantern)(new BlockGlassLantern().setRegistryName("minecraftbyexample", "mbe05a_block_glass_lantern_registry_name"));
    blockRegisterEvent.getRegistry().register(blockGlassLantern);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold one of this block in their hand at once

    Item.Properties itemGlassLanternProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.DECORATIONS);  // which inventory tab?
    itemBlockGlassLantern = new BlockItem(blockGlassLantern, itemGlassLanternProperties);
    itemBlockGlassLantern.setRegistryName(blockGlassLantern.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockGlassLantern);
  }
}
