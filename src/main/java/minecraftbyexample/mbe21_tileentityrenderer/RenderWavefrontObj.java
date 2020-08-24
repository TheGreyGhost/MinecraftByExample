package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 * This class shows how to render a Wavefront OBJ model using a TileEntityRenderer
 *
 * Notes regarding the Wavefront obj file:
 *
 * 1) You need to manually replace the texture path in the .mtl file:
 *  *   `map_Kd mbe21_ter_gem.png`
 * changes to
 *   `map_Kd minecraftbyexample:model/mbe21_ter_gem`
 * and you need to put the texture into
 * assets/minecraftbyexample/textures/model
 * 2) You may need to define flip-v: true in your model.json if your textures appear upside-down
 * 3) You may need to set ambientToFullbright if your model is always rendering at maximum brightness and is ignoring
 *    your combinedLight method parameter.
 *
 * A breakpoint in OBJloader::loadModel() can help you troubleshoot wavefront obj file problems
 */
public class RenderWavefrontObj {

  public static void renderWavefrontObj(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                        int combinedLight, int combinedOverlay) {

    // the gem model lies within a box from [-0.5, -1, -0.5] to [0.5, 1, 0.5]
    final Vec3d OBJ_MODEL_SIZE = new Vec3d(1, 2, 1);
    final Vec3d OBJ_MODEL_BOTTOM_APEX = new Vec3d(0, -1, 0);

    // When the TER::render method is called, the origin [0,0,0] is at the current [x,y,z] of the block being rendered.
    // We want the gem to be hovering with its bottom apex ([0,-1,0] in model space) touching the middle of the top of the hopper
    final Vec3d HOPPER_MIDDLE_OF_TOP = new Vec3d(0.5, 11.0/16.0, 0.5);

    // We also want the gem to be scaled down so that it is half a block high
    final Vec3d WORLD_MODEL_SIZE = new Vec3d(0.25, 0.5, 0.25);

    double scaleX, scaleY, scaleZ;
    final boolean SCALE_EVENLY_BASED_ON_Y = true;
    if (SCALE_EVENLY_BASED_ON_Y) {
      scaleY = WORLD_MODEL_SIZE.getY() / OBJ_MODEL_SIZE.getY();
      scaleX = scaleY;
      scaleZ = scaleY;
    } else {
      scaleX = WORLD_MODEL_SIZE.getX() / OBJ_MODEL_SIZE.getX();
      scaleY = WORLD_MODEL_SIZE.getY() / OBJ_MODEL_SIZE.getY();
      scaleZ = WORLD_MODEL_SIZE.getZ() / OBJ_MODEL_SIZE.getZ();
    }

    // must convert our model space size to world space before translating
    final Vec3d OBJ_MODEL_BOTTOM_APEX_WORLD_SPACE = new Vec3d(OBJ_MODEL_BOTTOM_APEX.getX() * scaleX,
            OBJ_MODEL_BOTTOM_APEX.getY() * scaleY,
            OBJ_MODEL_BOTTOM_APEX.getZ() * scaleZ);

    // translate model origin from [0,0,0] up to HOPPER_MIDDLE_OF_TOP and then further to place the apex in the right place
    final Vec3d TRANSLATION_OFFSET = HOPPER_MIDDLE_OF_TOP.subtract(OBJ_MODEL_BOTTOM_APEX_WORLD_SPACE);

    matrixStack.push(); // push the current transformation matrix + normals matrix
    matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate to put the gem in the right place

    applyAnimations(tileEntityMBE21, matrixStack);  // apply further translation and a rotation based on animation parameters

    matrixStack.scale((float)scaleX, (float)scaleY, (float)scaleZ);

    Color artifactColour = tileEntityMBE21.getArtifactColour();

    BlockState state = StartupCommon.blockMBE21.getDefaultState().with(BlockMBE21.USE_WAVEFRONT_OBJ_MODEL, true);
    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
    IBakedModel model = dispatcher.getModelForState(state);

    // you can use one of the various BlockRenderer render methods depending on how much control you want to retain over the
    //   model's appearance.
    // To render the model as if it were a block placed in the world (lighting, shading, etc) you can use
    // the renderModel() with World (ILightReader) as the first argument.
//    World world = tileEntityMBE21.getWorld();
//    if (world == null) return;
//    BlockPos blockPos = tileEntityMBE21.getPos();

    // To render the model with more control over lighting, colour, etc, use the renderModel() shown below

    MatrixStack.Entry currentMatrix = matrixStack.getLast();

    float red = artifactColour.getRed() / 255.0F;
    float green = artifactColour.getGreen() / 255.0F;
    float blue = artifactColour.getBlue() / 255.0F;

    combinedLight = getCombinedLight(tileEntityMBE21, combinedLight);

    IVertexBuilder vertexBuffer = renderBuffers.getBuffer(RenderType.getSolid());
    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, model,
            red, green, blue, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);

    matrixStack.pop(); // restore the original transformation matrix + normals matrix
  }

  /**
   // The gem changes its appearance and animation as the player approaches.
   // When the player is a long distance away, the gem is dark, resting in the hopper, and does not rotate.
   // As the player approaches closer than 8 blocks, the gem first starts to glow brighter
   // When the player gets closer than 6 blocks, the gem is at maximum brightness, and the gem starts to levitate above the pedestal
   // Once the player gets closer than 4 blocks, the gem reaches maximum height and starts to spin anti-clockwise
   // Once the player gets closer than 2 blocks, the gem reaches maximum spin speed

   // the appearance and animation of the gem is hence made up of several parts:
   // 1) the colour of the gem, which is contained in the tileEntity (accounted for in renderWavefrontObj already)
   // 2) the brightness of the gem, which depends on player distance
   // 3) the distance that the gem rises above the pedestal, which depends on player distance
   // 4) the speed at which the gem is spinning, which depends on player distance.
   */
  private static void applyAnimations(TileEntityMBE21 tileEntityMBE21, MatrixStack matrixStack) {

    // try to figure out the player's distance

    double playerDistance = 0.0;  // default
    ClientPlayerEntity player = Minecraft.getInstance().player;
    BlockPos blockPos = tileEntityMBE21.getPos();
    if (player != null && blockPos != null) {
      Vec3d pedestalCentre = new Vec3d(blockPos).add(0.5, 1.0, 0.5);
      Vec3d playerFeet = player.getPositionVec();
      playerDistance = playerFeet.distanceTo(pedestalCentre);
    }

    final double DISTANCE_FOR_MIN_SPIN = 4.0;
    final double DISTANCE_FOR_MAX_SPIN = 2.0;
    final double DISTANCE_FOR_MIN_LEVITATE = 6.0;
    final double DISTANCE_FOR_MAX_LEVITATE = 4.0;

    final double MIN_LEVITATE_HEIGHT = 0.0;
    final double MAX_LEVITATE_HEIGHT = 0.5;
    double levitateHeight = UsefulFunctions.interpolate_with_clipping(playerDistance, DISTANCE_FOR_MIN_LEVITATE, DISTANCE_FOR_MAX_LEVITATE,
            MIN_LEVITATE_HEIGHT, MAX_LEVITATE_HEIGHT);

    matrixStack.translate(0, levitateHeight, 0);

    final double MIN_REV_PER_SEC = 0.0;
    final double MAX_REV_PER_SEC = 0.5;
    double revsPerSecond = UsefulFunctions.interpolate_with_clipping(playerDistance, DISTANCE_FOR_MIN_SPIN, DISTANCE_FOR_MAX_SPIN,
            MIN_REV_PER_SEC, MAX_REV_PER_SEC);
    double angularPositionInDegrees = tileEntityMBE21.getNextAngularPosition(revsPerSecond);

    matrixStack.rotate(Vector3f.YP.rotationDegrees((float)angularPositionInDegrees));   // rotate around the vertical axis
  }

  /**
   // The gem changes its appearance and animation as the player approaches.
   // When the player is a long distance away, the gem is dark, resting in the hopper, and does not rotate.
   // As the player approaches closer than 16 blocks, the gem first starts to glow brighter
   // When the player gets closer than 8 blocks, the gem is at maximum brightness.
   // This method calculates the gem brightness (controlled by combinedLight = skylight + blocklight)
   */
  private static int getCombinedLight(TileEntityMBE21 tileEntityMBE21, int ambientCombinedLight) {

    // try to figure out the player's distance

    double playerDistance = 0.0;  // default
    ClientPlayerEntity player = Minecraft.getInstance().player;
    BlockPos blockPos = tileEntityMBE21.getPos();
    if (player != null && blockPos != null) {
      Vec3d pedestalCentre = new Vec3d(blockPos).add(0.5, 1.0, 0.5);
      Vec3d playerFeet = player.getPositionVec();
      playerDistance = playerFeet.distanceTo(pedestalCentre);
    }

    final double DISTANCE_FOR_MIN_GLOW = 8.0;
    final double DISTANCE_FOR_MAX_GLOW = 6.0;
    final double MIN_GLOW = 0.0;
    final double MAX_GLOW = 1.0;
    double glowMultiplier = UsefulFunctions.interpolate_with_clipping(playerDistance, DISTANCE_FOR_MIN_GLOW, DISTANCE_FOR_MAX_GLOW,
            MIN_GLOW, MAX_GLOW);

    // change the "multitexturing" lighting value (default value is the brightness of the tile entity's block)
    // - this will make the gem "glow" brighter than the surroundings if it is dark.
    final int SKY_LIGHT_VALUE = (int)(15 * glowMultiplier);
    final int BLOCK_LIGHT_VALUE = (int)(15 * glowMultiplier);

    int repackedValue = LightTexture.packLight(BLOCK_LIGHT_VALUE, SKY_LIGHT_VALUE);
    return repackedValue;
  }

}
