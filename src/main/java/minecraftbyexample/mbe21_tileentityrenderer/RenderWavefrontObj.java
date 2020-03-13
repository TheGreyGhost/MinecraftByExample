package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import minecraftbyexample.usefultools.RenderTypeHelper;
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
 * This class show how to render a Wavefront OBJ model using a TileEntityRenderer
 */
public class RenderWavefrontObj {

  public static void renderWavefrontObj(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                        int combinedLight, int combinedOverlay) {

    // When the TER::render method is called, the origin [0,0,0] is at the current [x,y,z] of the block being rendered.
    // The tetrahedron-drawing method draws the tetrahedron in a cube region from [0,0,0] to [1,1,1] but we want it
    //   to be in the block one above this, i.e. from [0,1,0] to [1,2,1],
    //   so we need to translate up by one block, i.e. by [0,1,0]
    final Vec3d TRANSLATION_OFFSET = new Vec3d(0, 1, 0);

    matrixStack.push(); // push the current transformation matrix + normals matrix
    matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
    Color artifactColour = tileEntityMBE21.getArtifactColour();

    BlockState state = StartupCommon.blockMBE21.getDefaultState().with(BlockMBE21.USE_WAVERFRONT_OBJ_MODEL, true);
    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
    IBakedModel model = dispatcher.getModelForState(state);

    World world = tileEntityMBE21.getWorld();
    if (world == null) return;
    BlockPos blockPos = tileEntityMBE21.getPos();

    // you can use one of the various BlockRenderer render methods depending on how much control you want to retain over the
    //   model's appearance.
    // To render the model as if it were a block placed in the world (lighting, shading, etc) you can use
    // the renderModel() with World (ILightReader) as the first argument.

    // To render the model with more control over lighting, colour, etc, use the renderModel() shown below

    MatrixStack.Entry currentMatrix = matrixStack.getLast();

    Color RENDER_COLOUR = Color.WHITE;

    float red = RENDER_COLOUR.getRed() / 255.0F;
    float green = RENDER_COLOUR.getGreen() / 255.0F;
    float blue = RENDER_COLOUR.getBlue() / 255.0F;

    IVertexBuilder vertexBuffer = renderBuffers.getBuffer(RenderType.getSolid());
    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, model,
            red, green, blue, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);

    matrixStack.pop(); // restore the original transformation matrix + normals matrix
  }


}
