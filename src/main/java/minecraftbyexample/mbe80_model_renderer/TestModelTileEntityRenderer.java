/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package minecraftbyexample.mbe80_model_renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

/**

 */

public class TestModelTileEntityRenderer extends net.minecraft.client.renderer.tileentity.TileEntityRenderer<TileEntityMBE80> {

  // Vanilla models have a rescaling and translation applied to them before rendering: the x and y axes are inverted,
  //   and the model is translated upwards by 1.5
  // If you want to match this, set USE_ENTITY_MODEL_TRANSFORMATIONS to true
  final static boolean USE_ENTITY_MODEL_TRANSFORMATIONS = true;

  public TestModelTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
    super(tileEntityRendererDispatcher);
  }

  /**
   * render the tile entity - called every frame while the tileentity is in view of the player
   *
   * @param tileEntityMBE80 the associated tile entity
   * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
   *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
   *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
   * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
   *                        it is needed for you to render the view properly.
   * @param renderBuffers    the list of buffers that you should render your model to (picking the appropriate one)
   * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
   * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
   *                        Used by vanilla for (1) red tint when a living entity is damaged, and (2) "flash" effect for creeper when ignited
   *                        CreeperRenderer.func_225625_b_()
   */
  @Override
  public void render(TileEntityMBE80 tileEntityMBE80, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                     int combinedLight, int combinedOverlay) {
    matrixStack.push(); // push the current transformation matrix + normals matrix

    TestModel.InteractiveParameters interactiveParameters = tileEntityMBE80.getInteractiveParameters();
//    interactiveParameters.updateFromDebugSettingsIfActive(tileEntityMBE80.getPos());  update in tileentity tick() instead

    // normally you would not create a new model at every render..!
    Model model = new TestModel(interactiveParameters);

    // vanilla models have a rescaling and translation applied to them before rendering: the x and y axes are inverted,
    //   and the model is translated upwards by 1.5
    // generally, you will want to apply it to yours too, if you have designed it like an entity.
    // unfortunately this means that further translations will take place in the entity coordinate space, not the
    //    world coordinate space, which gets confusing (eg translating towards +y will make the model render lower down in the world)

    if (USE_ENTITY_MODEL_TRANSFORMATIONS) {
      matrixStack.scale(-1, -1, 1);
      matrixStack.translate(0.0D, (double) -1.501F, 0.0D);
    }

    // The model is defined to draw itself centred on [0,0,0], but we want it to hover above the ground otherwise we
    //  won't be able to see it, so translate it upwards (default is to move the origin by [0.5, 1.0, 0.5]
    Vector3f TRANSLATION_OFFSET = interactiveParameters.MODEL_TRANSLATE;
    TRANSLATION_OFFSET = new Vector3f(TRANSLATION_OFFSET.getX(), TRANSLATION_OFFSET.getY(), TRANSLATION_OFFSET.getZ());  // make a copy

    if (USE_ENTITY_MODEL_TRANSFORMATIONS) {  // convert translation from world coordinates to model coordinates
      TRANSLATION_OFFSET.mul(-1, -1, 1);
    }

    matrixStack.translate(TRANSLATION_OFFSET.getX(),TRANSLATION_OFFSET.getY(),TRANSLATION_OFFSET.getZ()); // translate

    IVertexBuilder renderBuffer = renderBuffers.getBuffer(model.getRenderType(TEST_MODEL_TEXTURE));
    model.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F); // white, fully opaque
    matrixStack.pop();
  }

  // Always render: never cull based on where the player is looking
  @Override
  public boolean isGlobalRenderer(TileEntityMBE80 tileEntityMBE80) {
    return true;
  }

  public static final ResourceLocation TEST_MODEL_TEXTURE
          = new ResourceLocation("minecraftbyexample:textures/model/mbe80_test_model_texture.png");
}
