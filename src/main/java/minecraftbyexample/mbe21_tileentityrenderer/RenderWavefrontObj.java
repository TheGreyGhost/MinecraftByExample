package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import minecraftbyexample.usefultools.RenderTypeHelper;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nullable;
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
 * 3) You  need to set ambientToFullbright if your model is always rendering at maximum brightness and is ignoring
 *    your combinedLight method parameter.
 *
 * A breakpoint in OBJloader::loadModel() can help you troubleshoot wavefront obj file problems
 *
 *
 *
 *
 */
public class RenderWavefrontObj {

  public static void renderWavefrontObj(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                        int combinedLight, int combinedOverlay) {

    // the gem model lies within a box from [-0.5, -1, -0.5] to [0.5, 1, 0.5]
    final Vec3d OBJ_MODEL_ORIGIN = new Vec3d(0, 0, 0);
    final Vec3d OBJ_MODEL_SIZE = new Vec3d(1, 2, 1);

    // When the TER::render method is called, the origin [0,0,0] is at the current [x,y,z] of the block being rendered.
    // We want it to be hovering halfway up the block which is one above this, i.e. add 1.5 to y and 0.5 to x and z
    // We also want the gem to be scaled down so that it is half a block high
    final Vec3d WORLD_MODEL_OFFSET_ABOVE_BLOCK_ORIGIN = new Vec3d(0.5, 1.5, 0.5);
    final Vec3d WORLD_MODEL_SIZE = new Vec3d(0.25, 0.5, 0.25);

    final Vec3d TRANSLATION_OFFSET = WORLD_MODEL_OFFSET_ABOVE_BLOCK_ORIGIN.subtract(OBJ_MODEL_ORIGIN);

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

    // The  drawing method draws the gem in a cube region from [0,0,0] to [1,1,1] but w i.e. from [0,1,0] to [1,2,1],
    //   so we need to translate up by one block, i.e. by [0,1,0]

    matrixStack.push(); // push the current transformation matrix + normals matrix
    matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
    matrixStack.scale((float)scaleX, (float)scaleY, (float)scaleZ);

    Color artifactColour = tileEntityMBE21.getArtifactColour();

    BlockState state = StartupCommon.blockMBE21.getDefaultState().with(BlockMBE21.USE_WAVERFRONT_OBJ_MODEL, true);
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

    IVertexBuilder vertexBuffer = renderBuffers.getBuffer(RenderType.getSolid());
    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, model,
            red, green, blue, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);

    matrixStack.pop(); // restore the original transformation matrix + normals matrix
  }


}
