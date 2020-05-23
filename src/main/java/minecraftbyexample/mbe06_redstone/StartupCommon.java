package minecraftbyexample.mbe06_redstone;

//import minecraftbyexample.mbe06_redstone.input.BlockRedstoneColouredLamp;
//import minecraftbyexample.mbe06_redstone.input.TileEntityRedstoneColouredLamp;
//import minecraftbyexample.mbe06_redstone.input_and_output.BlockRedstoneMeter;
//import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
//import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneTarget;
import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneTarget;
import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneVariableSource;
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
 * These methods are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
//  public static BlockRedstoneColouredLamp blockRedstoneColouredLamp;
  public static BlockRedstoneTarget blockRedstoneTarget;
//  public static BlockRedstoneMeter blockRedstoneMeter;
  public static BlockRedstoneVariableSource blockRedstoneVariableSource;

  public static BlockItem itemBlockRedstoneColouredLamp;
  public static BlockItem itemBlockRedstoneTarget;
  public static BlockItem itemBlockRedstoneMeter;
  public static BlockItem itemBlockRedstoneVariableSource;

//  public static void preInitCommon()
//  {
//   // ------input-------
//    // each instance of your block should have a name that is unique within your mod.  use lower case.
//
//    blockRedstoneColouredLamp = (BlockRedstoneColouredLamp)(new BlockRedstoneColouredLamp()
//            .setUnlocalizedName("mbe06d_block_redstone_coloured_lamp_unlocalised_name"));
//    blockRedstoneColouredLamp.setRegistryName("minecraftbyexample:mbe06d_block_redstone_coloured_lamp_registry_name");
//    ForgeRegistries.BLOCKS.register(blockRedstoneColouredLamp);
//
//    // We also need to create and register an ItemBlock for this block otherwise it won't appear in the inventory
//    itemBlockRedstoneColouredLamp = new BlockItem(blockRedstoneColouredLamp);
//    itemBlockRedstoneColouredLamp.setRegistryName(blockRedstoneColouredLamp.getRegistryName());
//    ForgeRegistries.ITEMS.register(itemBlockRedstoneColouredLamp);
//
//    // And register the tileentity too...
//    GameRegistry.registerTileEntity(TileEntityRedstoneColouredLamp.class, "minecraftbyexample:mbe06d_redstone_coloured_lamp_tileentity");
//
//    // -----input and output ---------
//
//    blockRedstoneMeter = (BlockRedstoneMeter)(new BlockRedstoneMeter()
//            .setUnlocalizedName("mbe06c_block_redstone_meter_unlocalised_name"));
//    blockRedstoneMeter.setRegistryName("minecraftbyexample:mbe06c_block_redstone_meter_registry_name");
//    ForgeRegistries.BLOCKS.register(blockRedstoneMeter);
//
//    itemBlockRedstoneMeter = new BlockItem(blockRedstoneMeter);
//    itemBlockRedstoneMeter.setRegistryName(blockRedstoneMeter.getRegistryName());
//    ForgeRegistries.ITEMS.register(itemBlockRedstoneMeter);
//
//    GameRegistry.registerTileEntity(TileEntityRedstoneMeter.class, "minecraftbyexample:mbe06c_redstone_meter_tileentity");
//
//    //-----------output_only---------
//
//    blockRedstoneTarget = (BlockRedstoneTarget)(new BlockRedstoneTarget()
//            .setUnlocalizedName("mbe06b_block_redstone_target_unlocalised_name"));
//    blockRedstoneTarget.setRegistryName("minecraftbyexample:mbe06b_block_redstone_target_registry_name");
//    ForgeRegistries.BLOCKS.register(blockRedstoneTarget);
//
//    itemBlockRedstoneTarget = new BlockItem(blockRedstoneTarget);
//    itemBlockRedstoneTarget.setRegistryName(blockRedstoneTarget.getRegistryName());
//    ForgeRegistries.ITEMS.register(itemBlockRedstoneTarget);
//
//    blockRedstoneVariableSource = (BlockRedstoneVariableSource)(new BlockRedstoneVariableSource()
//            .setUnlocalizedName("mbe06_block_redstone_variable_source_unlocalised_name"));
//    blockRedstoneVariableSource.setRegistryName("minecraftbyexample:mbe06_block_redstone_variable_source_registry_name");
//    ForgeRegistries.BLOCKS.register(blockRedstoneVariableSource);
//
//    itemBlockRedstoneVariableSource = new BlockItem(blockRedstoneVariableSource);
//    itemBlockRedstoneVariableSource.setRegistryName(blockRedstoneVariableSource.getRegistryName());
//    ForgeRegistries.ITEMS.register(itemBlockRedstoneVariableSource);
//  }

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockRedstoneVariableSource = (BlockRedstoneVariableSource) (new BlockRedstoneVariableSource()
            .setRegistryName("minecraftbyexample", "mbe06_block_redstone_variable_source_registry_name"));
    blockRegisterEvent.getRegistry().register(blockRedstoneVariableSource);

    blockRedstoneTarget = (BlockRedstoneTarget) (new BlockRedstoneTarget()
            .setRegistryName("minecraftbyexample", "mbe06_block_redstone_target_registry_name"));
    blockRegisterEvent.getRegistry().register(blockRedstoneTarget);

  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 4;  // player can only hold 4 of this block in their hand at once

    Item.Properties itemProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.REDSTONE);  // which inventory tab?
    itemBlockRedstoneVariableSource = new BlockItem(blockRedstoneVariableSource, itemProperties);
    itemBlockRedstoneVariableSource.setRegistryName(blockRedstoneVariableSource.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockRedstoneVariableSource);

    itemBlockRedstoneTarget = new BlockItem(blockRedstoneTarget, itemProperties);
    itemBlockRedstoneTarget.setRegistryName(blockRedstoneTarget.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockRedstoneTarget);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}
