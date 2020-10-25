/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;
import java.util.Random;

/**
 * This class is adapted from part of the Botania Mod, thanks to Vazkii and WillieWillus
 * Get the Source Code in github
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * Features of the Hourglass model:
 * 1) Parts of the hourglass are transparent (use alpha blending)
 * 2) The hourglass is animated - it moves (wiggles) up and down, and rotates
 * 3) The sand in the hourglass is rendered with different colours; the top colour fades and the bottom colour intensifies
 *    as the sand in the top chamber runs down into the bottom chamber
 */

public class RenderModelHourglass  {

	final static ModelHourglass model = new ModelHourglass();

	public static void renderUsingModel(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                      int combinedLight, int combinedOverlay) {

    matrixStack.push(); // push the current transformation matrix + normals matrix

    // The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
    // at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
    // We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
    final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 1.5, 0.5);
    matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z); // translate

    // we use an animation timer to manipulate the render, with a "random" offset added to the animation timer based on
    // the world position
    // This ensures that adjacent hourglasses don't animate in lock step, which looks weird
    double animationTicks = AnimationTickCounter.getTotalElapsedTicksInGame() + partialTicks;
    animationTicks += new Random(tileEntityMBE21.getPos().hashCode()).nextInt(Integer.MAX_VALUE);

    AnimationState animationState = new AnimationState(animationTicks);

    // add a small amount of wiggle to the hourglass' position
    Vector3f wiggle = getWiggle(animationTicks);
    matrixStack.translate(wiggle.getX(), wiggle.getY(), wiggle.getZ());

    // Vanilla applies the following transformations to standard Models, set USE_ENTITY_MODEL_TRANSFORMATIONS to true
    //  to apply those.
    // i.e. all standard Vanilla models are defined to have their feet on the ground for a model y coordinate of 24 units
    //   when rendered at y = 0.  (see mbe80 and http://greyminecraftcoder.blogspot.com/2020/03/minecraft-model-1144.html)
    boolean USE_ENTITY_MODEL_TRANSFORMATIONS = true;

    if (USE_ENTITY_MODEL_TRANSFORMATIONS) {
      matrixStack.scale(-1, -1, 1);
      matrixStack.translate(0.0D, (double) -1.501F, 0.0D);
    }

    Color sandColour = tileEntityMBE21.getArtifactColour();
    float sandColourRed = sandColour.getRed()/255.0F;
    float sandColourGreen = sandColour.getGreen()/255.0F;
    float sandColourBlue = sandColour.getBlue()/255.0F;

    IVertexBuilder renderBuffer = renderBuffers.getBuffer(model.getRenderType(HOURGLASS_MODEL_TEXTURE));
    model.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
                 sandColourRed, sandColourGreen, sandColourBlue,
                 animationState.flipRotationDegrees,
                 animationState.fractionSandInTop, animationState.fractionSandInBottom);
    matrixStack.pop();
  }

  /**
   * Add a small amount of wiggle to the hourglass' position, based on the animation timer
   * @param animationTicks animation ticks including partialTicks
   * @return
   */
	private static Vector3f getWiggle(double animationTicks) {
    final int TICKS_PER_SECOND = 20;
    final double X_WIGGLE_CYCLE_SECONDS = 0.7;
    final double Y_WIGGLE_CYCLE_SECONDS = 2.3;
    final double Z_WIGGLE_CYCLE_SECONDS = 0.76;
    final double X_WIGGLE_CYCLE_TICKS = X_WIGGLE_CYCLE_SECONDS * TICKS_PER_SECOND;
    final double Y_WIGGLE_CYCLE_TICKS = Y_WIGGLE_CYCLE_SECONDS * TICKS_PER_SECOND;
    final double Z_WIGGLE_CYCLE_TICKS = Z_WIGGLE_CYCLE_SECONDS * TICKS_PER_SECOND;
    final float XZ_WIGGLE_AMPLITUDE = 0.01F;
    final float Y_WIGGLE_AMPLITUDE = 0.05F;
    final double RADIANS_PER_CYCLE = 2*Math.PI;

    double wiggle_x = XZ_WIGGLE_AMPLITUDE * Math.cos((animationTicks / X_WIGGLE_CYCLE_TICKS) * RADIANS_PER_CYCLE);
    double wiggle_y = Y_WIGGLE_AMPLITUDE  * Math.sin((animationTicks / Y_WIGGLE_CYCLE_TICKS) * RADIANS_PER_CYCLE);
    double wiggle_z = XZ_WIGGLE_AMPLITUDE * Math.sin((animationTicks / Z_WIGGLE_CYCLE_TICKS) * RADIANS_PER_CYCLE);
	  return new Vector3f((float)wiggle_x, (float)wiggle_y, (float)wiggle_z);
  }

  /**
   * Helper class to convert the animation ticks into animation parameters of the hourglass (sand fullness,
   *   and end-over-end rotation when the sand expires)
   */
  static class AnimationState {
	  public AnimationState (double animationTicks) {
      float cycleOffset = (float)(animationTicks % FULL_CYCLE_DURATION_TICKS);

      // the animation cycle is:
      // 1) Hourglass is upright.  Sand runs out for SAND_DEPLETION_TICKS
      // 2) Hourglass spends FLIP_DURATION_TICKS to flip over by 180 degrees
      // 3) Hourglass is upside down.  Sand runs out for SAND_DEPLETION_TICKS
      // 4) Hourglass spends FLIP_DURATION_TICKS to flip over by 180 degrees to be upright again.

      if (cycleOffset <= SAND_DEPLETION_TICKS) {
        flipRotationDegrees = 0;
        fractionSandInTop = 1.0F - cycleOffset / SAND_DEPLETION_TICKS;
      } else {
        cycleOffset -= SAND_DEPLETION_TICKS;
        if (cycleOffset <= FLIP_DURATION_TICKS) {
          flipRotationDegrees = 180.0F * cycleOffset / FLIP_DURATION_TICKS;
          fractionSandInTop = 0.0F;
        } else {
          cycleOffset -= FLIP_DURATION_TICKS;
          if (cycleOffset <= SAND_DEPLETION_TICKS) {
            flipRotationDegrees = 180.0F;
            fractionSandInTop = cycleOffset / SAND_DEPLETION_TICKS;
          } else {
            cycleOffset -= SAND_DEPLETION_TICKS;
            flipRotationDegrees = 180.0F + 180.0F * cycleOffset / FLIP_DURATION_TICKS;
            fractionSandInTop = 1.0F;
          }
        }
      }
      fractionSandInBottom = 1.0F - fractionSandInTop;
    }

    public float flipRotationDegrees;  // used to flip the hourglass end-over-end (when the sand runs out)
                                       // 180 = upside down
    public float fractionSandInTop;    // how much sand is in the top bulb? (0 = none, 1 = full)
    public float fractionSandInBottom; // how much sand is in the bottom bulb? (0 = none, 1 = full)

    private final float FLIP_DURATION_SECONDS = 0.2F;  // how long does it take to flip the hourglass end-over-end?
    private final float SAND_DEPLETION_SECONDS = 17.8F; // how long does it take the sand to
    private final float HALF_CYCLE_DURATION_SECONDS = FLIP_DURATION_SECONDS + SAND_DEPLETION_SECONDS;
    private final float FULL_CYCLE_DURATION_SECONDS = 2*HALF_CYCLE_DURATION_SECONDS;

    private final int TICKS_PER_SECOND = 20;
    private final float FLIP_DURATION_TICKS = FLIP_DURATION_SECONDS * TICKS_PER_SECOND;
    private final float SAND_DEPLETION_TICKS = SAND_DEPLETION_SECONDS * TICKS_PER_SECOND;
    private final float FULL_CYCLE_DURATION_TICKS = FULL_CYCLE_DURATION_SECONDS * TICKS_PER_SECOND;
  }
  public static final ResourceLocation HOURGLASS_MODEL_TEXTURE
          = new ResourceLocation("minecraftbyexample:textures/model/mbe21_hourglass_model.png");

}
