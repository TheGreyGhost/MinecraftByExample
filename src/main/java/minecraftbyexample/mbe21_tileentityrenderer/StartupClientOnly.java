package minecraftbyexample.mbe21_tileentityrenderer;

import minecraftbyexample.usefultools.RenderTypeMBE;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  public static final ResourceLocation MBE21_CUBE_TEXTURE = new ResourceLocation("minecraftbyexample:textures/entity/mbe21_ter_cube.png");

  /**
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // Tell the renderer that the base is rendered using CUTOUT_MIPPED (to match the Block Hopper)
    RenderTypeLookup.setRenderLayer(StartupCommon.blockMBE21, RenderType.getCutoutMipped());
    // Register the custom renderer for our tile entity
    ClientRegistry.bindTileEntityRenderer(StartupCommon.tileEntityDataTypeMBE21, TileEntityRendererMBE21::new);
  }

  // Stitch the cube texture into the block texture sheet so that we can use it later for rendering.
  @SubscribeEvent
  public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
    event.addSprite(MBE21_CUBE_TEXTURE);
  }

}
