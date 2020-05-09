package minecraftbyexample.mbe04_block_dynamic_block_models;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The methods for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockCamouflage blockCamouflage;  // this holds the unique instance of your block
  public static BlockItem itemBlockCamouflage;  // this holds the unique instance of the ItemBlock corresponding to your block

  public static BlockAltimeter blockAltimeter;  // this holds the unique instance of your block
  public static BlockItem itemBlockAltimeter;  // this holds the unique instance of the ItemBlock corresponding to your block

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockCamouflage = (BlockCamouflage)(new BlockCamouflage().setRegistryName("minecraftbyexample", "mbe04_block_camouflage_registry_name"));
    blockRegisterEvent.getRegistry().register(blockCamouflage);

    blockAltimeter = (BlockAltimeter)(new BlockAltimeter().setRegistryName("minecraftbyexample", "mbe04b_block_altimeter_registry_name"));
    blockRegisterEvent.getRegistry().register(blockAltimeter);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 64;  // player can hold 64 of this block in their hand at once

    Item.Properties itemProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockCamouflage = new BlockItem(blockCamouflage, itemProperties);
    itemBlockCamouflage.setRegistryName(blockCamouflage.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockCamouflage);

    itemProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.DECORATIONS);  // which inventory tab?
    itemBlockAltimeter = new BlockItem(blockAltimeter, itemProperties);
    itemBlockAltimeter.setRegistryName(blockAltimeter.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockAltimeter);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not required for this example....
  }
}
