package minecraftbyexample.mbe80_model_renderer;

import minecraftbyexample.mbe21_tileentityrenderer.TileEntityMBE21;
import net.minecraft.block.Block;
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
 * The Startup classes for this example are called during startup, in the following order:
 * Register<Block>
 * Register<Item>
 * Register<TileEntityType<?>>
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockMBE80 blockMBE80;  // this holds the unique instance of your block
  public static BlockItem itemBlockMBE80; // this holds the unique instance of the itemblock corresponding to the block
  public static TileEntityType<TileEntityMBE80> tileEntityDataTypeMBE80;  // Holds the type of our tile entity; needed for the TileEntityData constructor

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockMBE80 = (BlockMBE80)(new BlockMBE80().setRegistryName("minecraftbyexample", "mbe80_testmodel_block_registry_name"));
    blockRegisterEvent.getRegistry().register(blockMBE80);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold 1 of this block in their hand at once

    Item.Properties itemSimpleProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockMBE80 = new BlockItem(blockMBE80, itemSimpleProperties);
    itemBlockMBE80.setRegistryName(blockMBE80.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockMBE80);
  }

  @SubscribeEvent
  public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
    tileEntityDataTypeMBE80 =
            TileEntityType.Builder.create(TileEntityMBE80::new, blockMBE80).build(null);  // you probably don't need a datafixer --> null should be fine
    tileEntityDataTypeMBE80.setRegistryName("minecraftbyexample:mbe80_tile_entity_type_registry_name");
    event.getRegistry().register(tileEntityDataTypeMBE80);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }

}
