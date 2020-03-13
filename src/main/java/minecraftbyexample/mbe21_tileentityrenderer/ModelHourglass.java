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

import java.util.Optional;

/**
 * This class is adapted from part of the Botania Mod, thanks to Vazkii and WillieWillus
 * Get the Source Code in github
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

public class ModelHourglass extends Model {

  public ModelRenderer solidParts;
	public ModelRenderer upperSand;
	public ModelRenderer lowerSand;
  public ModelRenderer neckSand;

	public ModelHourglass() {
		super(RenderType::getEntityTranslucent);

		textureWidth = 64;
		textureHeight = 32;

		/*
		   Construct the hourglass model by building it up from boxes
		   The dimensions of the box are given in texels, each of which is 1/16 of minecraft world coordinates

		   e.g. a box which is 8x4x16 in size corresponds to a world block 0.5 x 0.25 x 1

       For more information - see mbe80 and http://greyminecraftcoder.blogspot.com/2020/03/minecraft-model-1144.html

		    The solid parts of the hourglass are composed of the Top, Bottom, central ring, and two glass boxes.
		    These are combined into one model.
		    The rotation point is [0, 0, 0] for all added boxes, this is the centre of the ring in the middle.
		    a setRotationPoint of [0, 24, 0] is added to counteract
		      Vanilla's entity transformation upon rendering- i.e. all standard Vanilla models have a y offset of
		      24 units (1.5 blocks) to place their feet on the ground when rendered at y = 0.  (see mbe80)
		 */

		final float EXPANSION_AMOUNT = 0;  // if not zero, the box is expanded on all sides by this amount

    final Vector3f TOP_POS = new Vector3f(-3.0F, -6.5F, -3.0F);
    final Pair<Integer, Integer> TOP_TEXTURE_ORIGIN = Pair.of(20, 0);
		final Vec3i TOP_SIZE = new Vec3i(6, 1, 6);

    final Vector3f BOTTOM_POS = new Vector3f(-3.0F, 5.5F, -3.0F);
    final Pair<Integer, Integer> BOTTOM_TEXTURE_ORIGIN = Pair.of(20, 7);
    final Vec3i BOTTOM_SIZE = new Vec3i(6, 1, 6);

    final Vector3f RING_POS = new Vector3f(-1.5F, -0.5F, -1.5F);
    final Pair<Integer, Integer> RING_TEXTURE_ORIGIN = Pair.of(0, 20);
    final Vec3i RING_SIZE = new Vec3i(3, 1, 3);

    final Vector3f TOP_GLASS_POS = new Vector3f(-2.5F, -5.5F, -2.5F);
    final Pair<Integer, Integer> TOP_GLASS_TEXTURE_ORIGIN = Pair.of(0, 0);
    final Vec3i TOP_GLASS_SIZE = new Vec3i(5, 5, 5);

    final Vector3f BOTTOM_GLASS_POS = new Vector3f(-2.5F, 0.5F, -2.5F);
    final Pair<Integer, Integer> BOTTOM_GLASS_TEXTURE_ORIGIN = Pair.of(0, 10);
    final Vec3i BOTTOM_GLASS_SIZE = new Vec3i(5, 5, 5);

    solidParts = new ModelRenderer(this);
		solidParts.setTextureOffset(TOP_TEXTURE_ORIGIN.getLeft(), TOP_TEXTURE_ORIGIN.getRight());
		solidParts.addBox(TOP_POS.getX(), TOP_POS.getY(), TOP_POS.getZ(),
                      TOP_SIZE.getX(), TOP_SIZE.getY(), TOP_SIZE.getZ(),
                      EXPANSION_AMOUNT);

    solidParts.setTextureOffset(BOTTOM_TEXTURE_ORIGIN.getLeft(), BOTTOM_TEXTURE_ORIGIN.getRight());
    solidParts.addBox(BOTTOM_POS.getX(), BOTTOM_POS.getY(), BOTTOM_POS.getZ(),
                      BOTTOM_SIZE.getX(), BOTTOM_SIZE.getY(), BOTTOM_SIZE.getZ(),
                      EXPANSION_AMOUNT);

    solidParts.setTextureOffset(RING_TEXTURE_ORIGIN.getLeft(), RING_TEXTURE_ORIGIN.getRight());
    solidParts.addBox(RING_POS.getX(), RING_POS.getY(), RING_POS.getZ(),
                      RING_SIZE.getX(), RING_SIZE.getY(), RING_SIZE.getZ(),
                      EXPANSION_AMOUNT);

    solidParts.setTextureOffset(TOP_GLASS_TEXTURE_ORIGIN.getLeft(), TOP_GLASS_TEXTURE_ORIGIN.getRight());
    solidParts.addBox(TOP_GLASS_POS.getX(), TOP_GLASS_POS.getY(), TOP_GLASS_POS.getZ(),
            TOP_GLASS_SIZE.getX(), TOP_GLASS_SIZE.getY(), TOP_GLASS_SIZE.getZ(),
            EXPANSION_AMOUNT);

    solidParts.setTextureOffset(BOTTOM_GLASS_TEXTURE_ORIGIN.getLeft(), BOTTOM_GLASS_TEXTURE_ORIGIN.getRight());
    solidParts.addBox(BOTTOM_GLASS_POS.getX(), BOTTOM_GLASS_POS.getY(), BOTTOM_GLASS_POS.getZ(),
            BOTTOM_GLASS_SIZE.getX(), BOTTOM_GLASS_SIZE.getY(), BOTTOM_GLASS_SIZE.getZ(),
            EXPANSION_AMOUNT);

    solidParts.setRotationPoint(0, 24, 0); // counteract Vanilla's entity transformation upon rendering.  (see mbe80)

//    There are three separate models for the sand:
//      * sand in the upper glass
//      * sand in the lower glass
//      * sand in the neck
//    These must be separate because we change their colour independently as the sand runs from one chamber to the other

    final Vector3f UPPER_SAND_POS = new Vector3f(-2, -5, -2);
    final Pair<Integer, Integer> UPPER_SAND_TEXTURE_ORIGIN = Pair.of(20, 14);
    final Vec3i UPPER_SAND_SIZE = new Vec3i(4, 4, 4);

    upperSand = new ModelRenderer(this);
    upperSand.setTextureOffset(UPPER_SAND_TEXTURE_ORIGIN.getLeft(), UPPER_SAND_TEXTURE_ORIGIN.getRight());
    upperSand.addBox(UPPER_SAND_POS.getX(), UPPER_SAND_POS.getY(), UPPER_SAND_POS.getZ(),
                    UPPER_SAND_SIZE.getX(), UPPER_SAND_SIZE.getY(), UPPER_SAND_SIZE.getZ(),
                    EXPANSION_AMOUNT);
    upperSand.setRotationPoint(0, 24, 0);

    final Vector3f LOWER_SAND_POS = new Vector3f(-2, 1, -2);
    final Pair<Integer, Integer> LOWER_SAND_TEXTURE_ORIGIN = Pair.of(20, 14);
    final Vec3i LOWER_SAND_SIZE = new Vec3i(4, 4, 4);

    lowerSand = new ModelRenderer(this, LOWER_SAND_TEXTURE_ORIGIN.getLeft(), LOWER_SAND_TEXTURE_ORIGIN.getRight());
    lowerSand.addBox(LOWER_SAND_POS.getX(), LOWER_SAND_POS.getY(), LOWER_SAND_POS.getZ(),
                      LOWER_SAND_SIZE.getX(), LOWER_SAND_SIZE.getY(), LOWER_SAND_SIZE.getZ(),
                      EXPANSION_AMOUNT);
    lowerSand.setRotationPoint(0, 24, 0);

    final Vector3f NECK_SAND_POS = new Vector3f(-0.5F, -1.5F, -0.5F);  // protrudes slightly into the larger sand
    final Pair<Integer, Integer> NECK_SAND_TEXTURE_ORIGIN = Pair.of(24, 14);
    final Vec3i NECK_SAND_SIZE = new Vec3i(1, 3, 1);

    neckSand = new ModelRenderer(this, NECK_SAND_TEXTURE_ORIGIN.getLeft(), NECK_SAND_TEXTURE_ORIGIN.getRight());
    neckSand.addBox(NECK_SAND_POS.getX(), NECK_SAND_POS.getY(), NECK_SAND_POS.getZ(),
            NECK_SAND_SIZE.getX(), NECK_SAND_SIZE.getY(), NECK_SAND_SIZE.getZ(),
            EXPANSION_AMOUNT);
    neckSand.setRotationPoint(0, 24, 0);
  }

	@Override
	public void render(MatrixStack ms, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, float r, float g, float b, float a) {
		render(ms, renderBuffer, combinedLight, combinedOverlay, r, g, b, 0, 0, 1);
	}

	public void render(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay,
                     float sandRed, float sandGreen, float sandBlue,
                     float rotationDegrees,
                     float upperSandFraction, float lowerSandFraction) {

    final float CONTAINER_RED = 1.0F;
    final float CONTAINER_GREEN = 1.0F;
    final float CONTAINER_BLUE = 1.0F;

    final float ALPHA_VALUE = 1.0F;

    solidParts.rotateAngleZ = (float)Math.toRadians(rotationDegrees);
    upperSand.rotateAngleZ = (float)Math.toRadians(rotationDegrees);
    lowerSand.rotateAngleZ = (float)Math.toRadians(rotationDegrees);
    neckSand.rotateAngleZ = (float)Math.toRadians(rotationDegrees);

		// draw sand first so that the semi-transparent glass blends properly over the top of it

    // neck sand will match whichever of the sands is on top.
    //  if rotation is between -90 to + 90 (>270 or <90) then the "upper" sand is on top.
    float neckSandFraction = (Math.abs(rotationDegrees - 180) < 90) ? lowerSandFraction : upperSandFraction;
    float neckSandAlpha = ALPHA_VALUE * neckSandFraction;
    neckSand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
            sandRed, sandGreen, sandBlue, neckSandAlpha);

    float upperSandAlpha = ALPHA_VALUE * upperSandFraction;
    upperSand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
            sandRed, sandGreen, sandBlue, upperSandAlpha);

    float lowerSandAlpha = ALPHA_VALUE * lowerSandFraction;
    lowerSand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
            sandRed, sandGreen, sandBlue, lowerSandAlpha);

    solidParts.render(matrixStack, renderBuffer, combinedLight, combinedOverlay,
                      CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);
	}
}
