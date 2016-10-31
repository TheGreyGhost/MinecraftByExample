package minecraftbyexample.mbe06_redstone;

import minecraftbyexample.mbe06_redstone.input.LampColour;
import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
import minecraftbyexample.mbe06_redstone.input_and_output.TileEntitySpecialRendererRedstoneMeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
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
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.
    // It must be done on client only, and must be done after the block has been created in Common.preinit().
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe06_block_redstone_variable_source", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneVariableSource, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    ModelResourceLocation itemModelResourceLocationB = new ModelResourceLocation("minecraftbyexample:mbe06b_block_redstone_target", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneTarget, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocationB);

    ModelResourceLocation itemModelResourceLocationC = new ModelResourceLocation("minecraftbyexample:mbe06c_block_redstone_meter", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneMeter, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocationC);

    ModelResourceLocation itemModelResourceLocationD = new ModelResourceLocation("minecraftbyexample:mbe06d_block_redstone_coloured_lamp", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneColouredLamp, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocationD);

    // Need to bind the renderer used for the Redstone Meter TileEntity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneMeter.class, new TileEntitySpecialRendererRedstoneMeter());
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
    // the LampColour class is used to change the rendering colour of the RedstoneColouredLamp
    Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new LampColour(), StartupCommon.blockRedstoneColouredLamp);
  }
}
