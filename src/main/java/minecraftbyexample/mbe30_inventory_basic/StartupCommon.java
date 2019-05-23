package minecraftbyexample.mbe30_inventory_basic;

import minecraftbyexample.GuiHandlerRegistry;
import minecraftbyexample.MinecraftByExample;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: brandon3055
 * Date: 06/01/2015
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
	public static Block blockInventoryBasic;  // this holds the unique instance of your block
	public static ItemBlock itemBlockInventoryBasic; // and the corresponding item form that block

	public static void preInitCommon()
	{
		// each instance of your block should have a name that is unique within your mod.  use lower case.
		/* it is a good practise to use a consistent registry name and obtain the unlocalised name from the registry name,
		 *  this will avoid breaking old worlds if something changes. This would look like
		 *  		blockInventoryBasic.getRegistryName().toString();
		 *  and would require changing the lang file as the block's name would be now
		 *          tile.minecraftbyexample:mbe_30_inventory_basic.name
		 */
		blockInventoryBasic = (BlockInventoryBasic)(new BlockInventoryBasic().setRegistryName("mbe30_inventory_basic"));
		blockInventoryBasic.setUnlocalizedName("mbe30_inventory_basic");
		ForgeRegistries.BLOCKS.register(blockInventoryBasic);

		// same but for the associated item
		itemBlockInventoryBasic = new ItemBlock(blockInventoryBasic);
		itemBlockInventoryBasic.setRegistryName(blockInventoryBasic.getRegistryName());
    ForgeRegistries.ITEMS.register(itemBlockInventoryBasic);

		// register the tile entity associated with the inventory block
		GameRegistry.registerTileEntity(TileEntityInventoryBasic.class, "minecraftbyexample:mbe30_tile_inventory_basic");

		// You need to register a GUIHandler for the container.  However there can be only one handler per mod, so for the purposes
		//   of this project, we create a single GuiHandlerRegistry for all examples.
		// We register this GuiHandlerRegistry with the NetworkRegistry, and then tell the GuiHandlerRegistry about
		//   each example's GuiHandler, in this case GuiHandlerMBE30, so that when it gets a request from NetworkRegistry,
		//   it passes the request on to the correct example's GuiHandler.
		NetworkRegistry.INSTANCE.registerGuiHandler(MinecraftByExample.instance, GuiHandlerRegistry.getInstance());
		GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandlerMBE30(), GuiHandlerMBE30.getGuiID());
	}

	public static void initCommon()
	{
	}

	public static void postInitCommon()
	{
	}
}
