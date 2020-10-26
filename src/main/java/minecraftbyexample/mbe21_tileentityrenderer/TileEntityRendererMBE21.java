//package minecraftbyexample.mbe21_tileentityrenderer;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import net.minecraft.client.renderer.*;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import static minecraftbyexample.mbe21_tileentityrenderer.RenderWavefrontObj.renderWavefrontObj;
//
///**
// * User: The Grey Ghost
// * Date: 12/01/2015
// * This class renders the artifact floating above the block.
// * The base model (the hopper shape) is drawn by the block model, not this class.
// * See assets/minecraftbyexample/blockstates/mbe21_tesr_block_registry_name.json
// *
// * The class demonstrates four different examples of rendering:
// * 1) Lines
// * 2) Manually drawing quads
// * 3) Rendering a block model
// * 4) Rendering a wavefront object
// *
// * 1) The lines have position and colour information only  (RenderType.getLines().  No lightmap information, which means that they will always be the
// *   same brightness regardless of day/night or nearby torches.
// *
// * 2) The quads have position, colour, texture, normal, and lightmap information
// *   RenderType.getSolid() is suitable if you're using a texture which has been stitched into the block texture sheet (either by defining it
// *      in a block model, or by manually adding it during TextureStitchEvent.)
// *   Otherwise you need to create your own RenderType.
// *
// * 3) Reads the block model for vanilla object and renders a smaller version of it
// *
// * 4) Reads a custom wavefront object (as a block model), and renders it using block rendering methods
// *
// */
//public class TileEntityRendererMBE21 extends TileEntityRenderer<TileEntityMBE21> {
//
//  public TileEntityRendererMBE21(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
//    super(tileEntityRendererDispatcher);
//  }
//
//  /**
//   * render the tile entity - called every frame while the tileentity is in view of the player
//   *
//   * @param tileEntityMBE21 the associated tile entity
//   * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
//   *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
//   *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
//   * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
//   *                        it is needed for you to render the view properly.
//   * @param renderBuffers    the buffer that you should render your model to
//   * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
//   * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
//   *                        Used by vanilla for (1) red tint when a living entity takes damage, and (2) "flash" effect for creeper when ignited
//   *                        CreeperRenderer.getOverlayProgress()
//   */
//  @Override
//  public void render(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
//                     int combinedLight, int combinedOverlay) {
//    TileEntityMBE21.EnumRenderStyle objectRenderStyle = tileEntityMBE21.getArtifactRenderStyle();
//
//    switch (objectRenderStyle) {
//      case WIREFRAME: RenderLines.renderWireframe(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
//      case QUADS: RenderQuads.renderCubeUsingQuads(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
//      case BLOCKQUADS: RenderModelHourglass.renderUsingModel(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
//      case WAVEFRONT: renderWavefrontObj(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
//      default: { LOGGER.debug("Unexpected objectRenderStyle:" + objectRenderStyle);}
//    }
//
//    // if you need to manually change the combinedLight you can use these helper functions...
//    int blockLight = LightTexture.getLightBlock(combinedLight);
//    int skyLight = LightTexture.getLightSky(combinedLight);
//    int repackedValue = LightTexture.packLight(blockLight, skyLight);
//  }
//
//  // this should be true for tileentities which render globally (no render bounding box), such as beacons.
//  @Override
//  public boolean isGlobalRenderer(TileEntityMBE21 tileEntityMBE21)
//  {
//    return false;
//  }
//
//  private static final Logger LOGGER = LogManager.getLogger();
//}
