package minecraftbyexample.mbe20_tileentity_data;

import minecraftbyexample.mbe01_block_simple.BlockSimple;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 * Register<Block>
 * Register<Item>
 * Register<TileEntityType<?>>
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockTileEntityData blockTileEntityData;  // this holds the unique instance of your block
  public static BlockItem itemBlockTileEntityData; // this holds the unique instance of the ItemBlock corresponding to your block
  public static TileEntityType<TileEntityData> tileEntityDataType;  // Holds the type of our tile entity; needed for the TileEntityData constructor

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockTileEntityData = (BlockTileEntityData)(new BlockTileEntityData().setRegistryName("minecraftbyexample", "mbe20_tileentity_data_block_registry_name"));
    blockRegisterEvent.getRegistry().register(blockTileEntityData);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold 1 of this block in their hand at once

    Item.Properties itemSimpleProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockTileEntityData = new BlockItem(blockTileEntityData, itemSimpleProperties);
    itemBlockTileEntityData.setRegistryName(blockTileEntityData.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockTileEntityData);
  }

  @SubscribeEvent
  public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
    tileEntityDataType =
            TileEntityType.Builder.create(TileEntityData::new, blockTileEntityData).build(null);  // you probably don't need a datafixer --> null should be fine
    tileEntityDataType.setRegistryName("minecraftbyexample:mbe20_tile_entity_type_registry_name");
    event.getRegistry().register(tileEntityDataType);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}
