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
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.util.Optional;

import static minecraftbyexample.usefultools.debugging.DebugSettings.getDebugParameterVec3d;

/**
 * This class is adapted from part of the Botania Mod, thanks to Vazkii and WillieWillus
 * Get the Source Code in github
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

public class ModelHourglass extends Model {

  public ModelRenderer overallModel;
	public ModelRenderer top;
	public ModelRenderer glassT;
	public ModelRenderer ring;
	public ModelRenderer glassB;
	public ModelRenderer bottom;

	public ModelRenderer sandT;
	public ModelRenderer sandB;

	public ModelHourglass() {
		super(RenderType::getEntityTranslucent);

		textureWidth = 64;
		textureHeight = 32;

		/*
		   Construct the hourglass model by building it up from boxes
		   The dimensions of the box are given in texels, each of which is 1/16 of minecraft world coordinates

		   i.e. a box which is 8x4x16 in size corresponds to a world block 0.5 x 0.25 x 1
		   That 8x4x16 will have six faces with texture


		 */

    final Vector3f TOP_CORNER = new Vector3f(-3.0F, -6.5F, -3.0F);
    final Pair<Integer, Integer> TOP_TEXTURE_ORIGIN = Pair.of(20, 0);
		final Vec3i TOP_BLOCK_DIMENSIONS = new Vec3i(6, 1, 6);

		top = new ModelRenderer(this,TOP_TEXTURE_ORIGIN.getLeft(), TOP_TEXTURE_ORIGIN.getRight());
		top.setRotationPoint(0.0F, 0.0F, 0.0F);
		top.addBox(TOP_CORNER.getX(), TOP_CORNER.getY(), TOP_CORNER.getZ(),
            TOP_BLOCK_DIMENSIONS.getX(), TOP_BLOCK_DIMENSIONS.getY(), TOP_BLOCK_DIMENSIONS.getZ(),
            0.0F);

//		glassT = new ModelRenderer(this, 0, 0);
//		glassT.setRotationPoint(0.0F, 0.0F, 0.0F);
//		glassT.addBox(-2.5F, -5.5F, -2.5F, 5, 5, 5, 0.0F);
		ring = new ModelRenderer(this, 0, 20);
		ring.setRotationPoint(0.0F, 0.0F, 0.0F);
		ring.addBox(-1.5F, -0.5F, -1.5F, 3, 1, 3, 0.0F);
//		glassB = new ModelRenderer(this, 0, 10);
//		glassB.setRotationPoint(0.0F, 0.0F, 0.0F);
//		glassB.addBox(-2.5F, 0.5F, -2.5F, 5, 5, 5, 0.0F);
//		bottom = new ModelRenderer(this, 20, 7);
//		bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
//		bottom.addBox(-3.0F, 5.5F, -3.0F, 6, 1, 6, 0.0F);
//
//		sandT = new ModelRenderer(this, 20, 14);
//		sandT.setRotationPoint(0.0F, 0.0F, 0.0F);
//		sandT.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F); // -2.0F, -5.0F, -2.0F
		sandB = new ModelRenderer(this, 20, 14);
		sandB.setRotationPoint(0.0F, 0.0F, 0.0F);
		sandB.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F); 
//
//    overallModel = new ModelRenderer(this, )
//

  }

	@Override
	public void render(MatrixStack ms, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, float r, float g, float b, float a) {
		render(ms, renderBuffer, combinedLight, combinedOverlay, r, g, b, 0, 1);
	}

	public void render(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay,
                     float sandRed, float sandGreen, float sandBlue, float topSandFraction, float bottomSandFraction) {

    final float CONTAINER_RED = 1.0F;
    final float CONTAINER_GREEN = 1.0F;
    final float CONTAINER_BLUE = 1.0F;

    final float ALPHA_VALUE = 1.0F;
		float f = 1F / 16F;

		// debugging purposes: use mbedebug paramvec3d mbe21RingRotation xrotation yrotation zrotation to rotate the ring to a fixed position
    Optional<Vec3d> debugRingRotation = DebugSettings.getDebugParameterVec3d("mbe21RingRotation");
    if (debugRingRotation.isPresent()) {
      ring.rotateAngleX = (float)Math.toRadians(debugRingRotation.get().x);
      ring.rotateAngleY = (float)Math.toRadians(debugRingRotation.get().y);
      ring.rotateAngleZ = (float)Math.toRadians(debugRingRotation.get().z);
    }

		ring.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
            CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);

    // debugging purposes: use mbedebug paramvec3d mbe21TopRotation xrotation yrotation zrotation to rotate the ring to a fixed position
    Optional<Vec3d> topRotation = DebugSettings.getDebugParameterVec3d("mbe21TopRotation");
    if (topRotation.isPresent()) {
      top.rotateAngleX = (float)Math.toRadians(topRotation.get().x);
      top.rotateAngleY = (float)Math.toRadians(topRotation.get().y);
      top.rotateAngleZ = (float)Math.toRadians(topRotation.get().z);
    }

		top.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
            CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);
//		bottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
//            containerBlendingColor.getRed(), containerBlendingColor.getGreen(), containerBlendingColor.getBlue(), ALPHA_VALUE);
//
//		if (topSandFraction > 0) {
//			matrixStack.push();
////			if (flip) {
////				matrixStack.translate(-2.0F * f, 1.0F * f, -2.0F * f);
////			} else {
////				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
////				matrixStack.translate(-2.0F * f, -5.0F * f, -2.0F * f);
////			}
//			matrixStack.scale(1F, topSandFraction, 1F); // shrink in the y model direction
//			sandT.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
//                   sandColour.getRed(), sandColour.getGreen(), sandColour.getBlue(), sandColour.getAlpha());
//			matrixStack.pop();
//		}
		// debugging purposes: use mbedebug mbe21bottomSandFraction value to set the sandfraction to a desired value
    Optional<Double> debugBottomSandFraction = DebugSettings.getDebugParameter("mbe21bottomSandFraction");
    if (debugBottomSandFraction.isPresent()) {
      bottomSandFraction = debugBottomSandFraction.get().floatValue();
    }

    // debugging purposes: use mbedebug paramvec3d mbe21SandRotation xrotation yrotation zrotation to rotate the sand to a fixed position
    Optional<Vec3d> sandRotation = DebugSettings.getDebugParameterVec3d("mbe21SandRotation");
    if (sandRotation.isPresent()) {
      sandB.rotateAngleX = (float)Math.toRadians(sandRotation.get().x);
      sandB.rotateAngleY = (float)Math.toRadians(sandRotation.get().y);
      sandB.rotateAngleZ = (float)Math.toRadians(sandRotation.get().z);
    }


    if (bottomSandFraction > 0) {
			matrixStack.push();
//			if (flip) {
//				matrixStack.translate(-2.0F * f, -5.0F * f, -2.0F * f);
//			} else {
//				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
//				matrixStack.translate(-2.0F * f, 1.0F * f, -2.0F * f);
//			}
      // debugging purposes: use mbedebug paramvec3d mbe21SandRotation xrotation yrotation zrotation to rotate the sand to a fixed position
      Optional<Vec3d> sandTranslation = DebugSettings.getDebugParameterVec3d("mbe21SandTranslation");
      if (sandTranslation.isPresent()) {
        matrixStack.translate(sandTranslation.get().x,sandTranslation.get().y, sandTranslation.get().z);
      }

      matrixStack.scale(1F, bottomSandFraction, 1F); // shrink in the y model direction
			sandB.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
              sandRed, sandGreen, sandBlue, ALPHA_VALUE);
			matrixStack.pop();
		}
//
//    Color glassBlendingColour = Color.WHITE;
//    glassT.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
//            glassBlendingColour.getRed(), glassBlendingColour.getGreen(), glassBlendingColour.getBlue(), ALPHA_VALUE);
//		glassB.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
//            glassBlendingColour.getRed(), glassBlendingColour.getGreen(), glassBlendingColour.getBlue(), ALPHA_VALUE);
	}

}
