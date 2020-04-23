package minecraftbyexample.mbe03_block_variants;

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
 * The Startup classes for this example are called during startup, in the following order:
 * onBlocksRegistration then onItemsRegistration then onCommonSetupEvent
 *  See MinecraftByExample class for more information

 */
public class StartupCommon
{
  public static BlockVariants blockVariantsBlue;  // one unique instance for each colour
  public static BlockVariants blockVariantsRed;  // one unique instance for each colour
  public static BlockVariants blockVariantsGreen;  // one unique instance for each colour
  public static BlockVariants blockVariantsYellow;  // one unique instance for each colour

  public static Block3DWeb block3DWeb; // multipart block

  public static BlockItem itemBlockVariantsBlue;  // this holds the unique ItemBlock instance corresponding to your block
  public static BlockItem itemBlockVariantsRed;  // this holds the unique ItemBlock instance corresponding to your block
  public static BlockItem itemBlockVariantsGreen;  // this holds the unique ItemBlock instance corresponding to your block
  public static BlockItem itemBlockVariantsYellow;  // this holds the unique ItemBlock instance corresponding to your block

  public static BlockItem itemBlock3DWeb;

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockVariantsBlue= (BlockVariants)(new BlockVariants(BlockVariants.EnumColour.BLUE).setRegistryName("minecraftbyexample", "mbe03a_block_variants_blue_registry_name"));
    blockVariantsRed = (BlockVariants)(new BlockVariants(BlockVariants.EnumColour.RED).setRegistryName("minecraftbyexample", "mbe03a_block_variants_red_registry_name"));
    blockVariantsGreen= (BlockVariants)(new BlockVariants(BlockVariants.EnumColour.GREEN).setRegistryName("minecraftbyexample", "mbe03a_block_variants_green_registry_name"));
    blockVariantsYellow = (BlockVariants)(new BlockVariants(BlockVariants.EnumColour.YELLOW).setRegistryName("minecraftbyexample", "mbe03a_block_variants_yellow_registry_name"));
    blockRegisterEvent.getRegistry().register(blockVariantsBlue);
    blockRegisterEvent.getRegistry().register(blockVariantsRed);
    blockRegisterEvent.getRegistry().register(blockVariantsGreen);
    blockRegisterEvent.getRegistry().register(blockVariantsYellow);

    block3DWeb = (Block3DWeb)(new Block3DWeb().setRegistryName("minecraftbyexample", "mbe03b_block_3dweb_registry_name"));
    blockRegisterEvent.getRegistry().register(block3DWeb);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold one of this block in their hand at once

    Item.Properties itemSimpleProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockVariantsBlue = new BlockItem(blockVariantsBlue, itemSimpleProperties);
    itemBlockVariantsBlue.setRegistryName(blockVariantsBlue.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockVariantsBlue);

    itemBlockVariantsRed = new BlockItem(blockVariantsRed, itemSimpleProperties);
    itemBlockVariantsRed.setRegistryName(blockVariantsRed.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockVariantsRed);

    itemBlockVariantsGreen = new BlockItem(blockVariantsGreen, itemSimpleProperties);
    itemBlockVariantsGreen.setRegistryName(blockVariantsGreen.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockVariantsGreen);

    itemBlockVariantsYellow = new BlockItem(blockVariantsYellow, itemSimpleProperties);
    itemBlockVariantsYellow.setRegistryName(blockVariantsYellow.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockVariantsYellow);

    itemBlock3DWeb = new BlockItem(block3DWeb, itemSimpleProperties);
    itemBlock3DWeb.setRegistryName(block3DWeb.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlock3DWeb);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }

}
