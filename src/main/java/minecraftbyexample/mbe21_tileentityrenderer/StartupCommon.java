package minecraftbyexample.mbe21_tileentityrenderer;

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
  public static BlockMBE21 blockMBE21;  // this holds the unique instance of your block
  public static BlockItem itemBlockMBE21; // this holds the unique instance of the itemblock corresponding to the block
  public static TileEntityType<TileEntityMBE21> tileEntityDataTypeMBE21;  // Holds the type of our tile entity; needed for the TileEntityData constructor

//  public static void preInitCommon()
//  {
//
//    blockMBE21 = (BlockMBE21)(new BlockMBE21().setUnlocalizedName("mbe21_tesr_block_unlocalised_name"));
//    blockMBE21.setRegistryName("minecraftbyexample:mbe21_tesr_block_registry_name");
//    ForgeRegistries.BLOCKS.register(blockMBE21);
//
//    // We also need to create and register an ItemBlock for this block otherwise it won't appear in the inventory
//    itemBlockMBE21 = new BlockItem(blockMBE21);
//    itemBlockMBE21.setRegistryName(blockMBE21.getRegistryName());
//    ForgeRegistries.ITEMS.register(itemBlockMBE21);
//
//    // Each of your tile entities needs to be registered with a name that is unique to your mod.
//    GameRegistry.registerTileEntity(TileEntityMBE21.class, "minecraftbyexample:mbe21_tesr_tile_entity");
//  }

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockMBE21 = (BlockMBE21)(new BlockMBE21().setRegistryName("minecraftbyexample", "mbe21_ter_block_registry_name"));
    blockRegisterEvent.getRegistry().register(blockMBE21);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold 1 of this block in their hand at once

    Item.Properties itemSimpleProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockMBE21 = new BlockItem(blockMBE21, itemSimpleProperties);
    itemBlockMBE21.setRegistryName(blockMBE21.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockMBE21);
  }

  @SubscribeEvent
  public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
    tileEntityDataTypeMBE21 =
            TileEntityType.Builder.create(TileEntityMBE21::new, blockMBE21).build(null);  // you probably don't need a datafixer --> null should be fine
    tileEntityDataTypeMBE21.setRegistryName("minecraftbyexample:mbe21_tile_entity_type_registry_name");
    event.getRegistry().register(tileEntityDataTypeMBE21);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }

}
