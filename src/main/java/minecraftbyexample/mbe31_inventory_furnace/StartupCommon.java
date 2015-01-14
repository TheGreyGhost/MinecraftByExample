package minecraftbyexample.mbe31_inventory_furnace;

import minecraftbyexample.GuiHandlerRegistry;
import minecraftbyexample.MinecraftByExample;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * The Startup class for this example is called during startup, in the following order:
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
	public static Block blockInventoryAdvanced;  // this holds the unique instance of your block

	public static void preInitCommon()
	{
		// each instance of your block should have a name that is unique within your mod.  use lower case.
		blockInventoryAdvanced = new BlockInventoryFurnace().setUnlocalizedName("mbe31_block_inventory_furnace");
		GameRegistry.registerBlock(blockInventoryAdvanced, "mbe31_block_inventory_furnace");
		// Each of your tile entities needs to be registered with a name that is unique to your mod.
		GameRegistry.registerTileEntity(TileInventoryFurnace.class, "mbe31_block_inventory_furnace");
		// you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.

		// You need to register a GUIHandler for the container.  However there can be only one handler per mod, so for the purposes
		//   of this project, we create a single GuiHandlerRegistry for all examples.
		// We register this GuiHandlerRegistry with the NetworkRegistry, and then tell the GuiHandlerRegistry about
		//   each example's GuiHandler, in this case GuiHandlerMBE31, so that when it gets a request from NetworkRegistry,
		//   it passes the request on to the correct example's GuiHandler.
		NetworkRegistry.INSTANCE.registerGuiHandler(MinecraftByExample.instance, GuiHandlerRegistry.getInstance());
		GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandlerMBE31(), GuiHandlerMBE31.getGuiID());
	}

	public static void initCommon()
	{
	}

	public static void postInitCommon()
	{
	}
}
