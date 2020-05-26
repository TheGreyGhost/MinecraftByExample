package minecraftbyexample.mbe06_redstone;

//import minecraftbyexample.mbe06_redstone.input.LampColour;
//import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
//import minecraftbyexample.mbe06_redstone.input_and_output.TileEntitySpecialRendererRedstoneMeter;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.model.ModelResourceLocation;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.fml.client.registry.ClientRegistry;

import minecraftbyexample.mbe06_redstone.input.LampColour;
import minecraftbyexample.mbe11_item_variants.LiquidColour;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * These methods are called during startup
 *  See MinecraftByExample class for more information

 */
public class StartupClientOnly
{
//  public static void preInitClientOnly()
//  {
//    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
//    //   or in your hand or thrown on the ground).
//    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
//    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
//    //   of any extra item you have created.  Hence you have to do it manually.
//    // It must be done on client only, and must be done after the block has been created in Common.preinit().
//    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe06_block_redstone_variable_source", "inventory");
//    final int DEFAULT_ITEM_SUBTYPE = 0;
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneVariableSource, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
//
//    ModelResourceLocation itemModelResourceLocationB = new ModelResourceLocation("minecraftbyexample:mbe06b_block_redstone_target", "inventory");
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneTarget, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocationB);
//
//    ModelResourceLocation itemModelResourceLocationC = new ModelResourceLocation("minecraftbyexample:mbe06c_block_redstone_meter", "inventory");
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneMeter, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocationC);
//
//    ModelResourceLocation itemModelResourceLocationD = new ModelResourceLocation("minecraftbyexample:mbe06d_block_redstone_coloured_lamp", "inventory");
//    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockRedstoneColouredLamp, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocationD);
//
//    // Need to bind the renderer used for the Redstone Meter TileEntity
//    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneMeter.class, new TileEntitySpecialRendererRedstoneMeter());
//  }
//
//  public static void initClientOnly()
//  {
//  }
//
//  public static void postInitClientOnly()
//  {
//    // the LampColour class is used to change the rendering colour of the RedstoneColouredLamp
//    Minecraft.getInstance().getBlockColors().registerBlockColorHandler(new LampColour(), StartupCommon.blockRedstoneColouredLamp);
//  }

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneVariableSource, RenderType.getSolid());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneTarget, RenderType.getSolid());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneColouredLamp, RenderType.getCutoutMipped());
    RenderTypeLookup.setRenderLayer(StartupCommon.blockRedstoneMeter, RenderType.getCutoutMipped());
  }

  @SubscribeEvent
  public static void onColorHandlerEvent(ColorHandlerEvent.Block event)
  {
    // the LiquidColour lambda function is used to change the rendering colour of the liquid in the bottle
    // i.e.: when vanilla wants to know what colour to render our itemVariants instance, it calls the LiquidColour lambda function
    event.getBlockColors().register(new LampColour(), StartupCommon.blockRedstoneColouredLamp);
  }
}
