package minecraftbyexample.mbe30b_inventory_basic;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * The Startup classes for this example are called during startup in the following order
 *  * Register<Block>
 *  * Register<Item>
 *  * Register<TileEntityType<?>>
 *  * Register<ContainerType<?>>
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
	public static Block blockInventoryBasic;  // this holds the unique instance of your block
	public static BlockItem itemBlockInventoryBasic; // and the corresponding item form that block

  public static TileEntityType<TileEntityInventoryBasic_IIH> tileEntityTypeMBE30_IIH;  // Holds the type of our tile entity; needed for the TileEntityData constructor
  public static ContainerType<ContainerBasic_IIH> containerTypeContainerBasic_IIH;

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockInventoryBasic = new BlockInventoryBasic_IIH().setRegistryName("mbe30b_block_registry_name");
    blockRegisterEvent.getRegistry().register(blockInventoryBasic);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold 1 of this block in their hand at once

    Item.Properties itemSimpleProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockInventoryBasic = new BlockItem(blockInventoryBasic, itemSimpleProperties);
    itemBlockInventoryBasic.setRegistryName(blockInventoryBasic.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockInventoryBasic);
  }

  @SubscribeEvent
  public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
    tileEntityTypeMBE30_IIH = TileEntityType.Builder.create(TileEntityInventoryBasic_IIH::new, blockInventoryBasic)
                                                .build(null);
                        // you probably don't need a datafixer --> null should be fine
    tileEntityTypeMBE30_IIH.setRegistryName("minecraftbyexample:mbe30b_tile_entity_type_registry_name");
    event.getRegistry().register(tileEntityTypeMBE30_IIH);
  }

  @SubscribeEvent
  public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
  {
    containerTypeContainerBasic_IIH = IForgeContainerType.create(ContainerBasic_IIH::createContainerClientSide);
    containerTypeContainerBasic_IIH.setRegistryName("mbe30b_container_registry_name");
    event.getRegistry().register(containerTypeContainerBasic_IIH);
  }
}
