package minecraftbyexample.testingarea;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

/**
 * Created by TGG on 2/03/2020.
 */
public class RefactorBeaconRenderer extends TileEntityRenderer<BeaconTileEntity> {
  public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

  public RefactorBeaconRenderer(TileEntityRendererDispatcher p_i226003_1_) {
    super(p_i226003_1_);
  }

  public void func_225616_a_(BeaconTileEntity beaconTileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
    long i = beaconTileEntity.getWorld().getGameTime();
    List<BeaconTileEntity.BeamSegment> list = beaconTileEntity.getBeamSegments();
    int j = 0;

    for(int k = 0; k < list.size(); ++k) {
      BeaconTileEntity.BeamSegment beacontileentity$beamsegment = list.get(k);
      renderBeamSegment(matrixStack, renderBuffer, partialTicks, i, j, k == list.size() - 1 ? 1024 : beacontileentity$beamsegment.getHeight(), beacontileentity$beamsegment.getColors());
      j += beacontileentity$beamsegment.getHeight();
    }

  }

  private static void renderBeamSegment(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, float partialTicks, long gameTime, int startHeight, int endHeight, float[] colours) {
    renderBeamSegment(matrixStack, renderBuffer, TEXTURE_BEACON_BEAM, partialTicks, 1.0F, gameTime, startHeight, endHeight, colours, 0.2F, 0.25F);
  }

  public static void renderBeamSegment(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, ResourceLocation beamTextureRL, float partialTicks, float textureScale, long gameTime, int startHeight, int endHeight, float[] colours, float beamRadius, float glowRadius) {
    int i = startHeight + endHeight;
    matrixStack.func_227860_a_(); // push
    matrixStack.func_227861_a_(0.5D, 0.0D, 0.5D); // translate
    float f = (float)Math.floorMod(gameTime, 40L) + partialTicks;
    float f1 = endHeight < 0 ? f : -f;
    float animOffset = MathHelper.func_226164_h_(f1 * 0.2F - (float)MathHelper.floor(f1 * 0.1F));  //truncate
    float red = colours[0];
    float green = colours[1];
    float blue = colours[2];
    matrixStack.func_227860_a_(); //push
    matrixStack.func_227863_a_(   //rotate
            Vector3f.field_229181_d_.func_229187_a_(f * 2.25F - 45.0F));    //  YP.rotationDegrees
    float f6 = 0.0F;
    float f8 = 0.0F;
    float f9 = -beamRadius;
    float f10 = 0.0F;
    float f11 = 0.0F;
    float f12 = -beamRadius;
    float f13 = 0.0F;
    float f14 = 1.0F;
    float v2 = -1.0F + animOffset;
    float v1 = (float)endHeight * textureScale * (0.5F / beamRadius) + v2;
    renderPart(matrixStack, renderBuffer.getBuffer(RenderType.func_228637_a_(beamTextureRL, false)), red, green, blue, 1.0F, startHeight, i, 0.0F, beamRadius, beamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, v1, v2);
    matrixStack.func_227865_b_(); // pop
    f6 = -glowRadius;
    float f7 = -glowRadius;
    f8 = -glowRadius;
    f9 = -glowRadius;
    f13 = 0.0F;
    f14 = 1.0F;
    v2 = -1.0F + animOffset;
    v1 = (float)endHeight * textureScale + v2;
    renderPart(matrixStack, renderBuffer.getBuffer(RenderType.func_228637_a_(beamTextureRL, true)), red, green, blue, 0.125F, startHeight, i, f6, f7, glowRadius, f8, f9, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, v1, v2);
    matrixStack.func_227865_b_();  //pop
  }

  private static void renderPart(MatrixStack matrixStack, IVertexBuilder vertexBuilder,
                                 float red, float green, float blue, float alpha, int ymin, int ymax, float p_228840_8_, float p_228840_9_, float p_228840_10_, float p_228840_11_, float p_228840_12_, float p_228840_13_, float p_228840_14_, float p_228840_15_, float u1, float u2, float v1, float v2) {
    MatrixStack.Entry matrixstack$entry = matrixStack.func_227866_c_();  // getLast
    Matrix4f matrix4f = matrixstack$entry.func_227870_a_();  // getPositionMatrix
    Matrix3f matrix3f = matrixstack$entry.func_227872_b_();  // getNormalMatrix
    addQuad(matrix4f, matrix3f, vertexBuilder, red, green, blue, alpha, ymin, ymax, p_228840_8_, p_228840_9_, p_228840_10_, p_228840_11_, u1, u2, v1, v2);
    addQuad(matrix4f, matrix3f, vertexBuilder, red, green, blue, alpha, ymin, ymax, p_228840_14_, p_228840_15_, p_228840_12_, p_228840_13_, u1, u2, v1, v2);
    addQuad(matrix4f, matrix3f, vertexBuilder, red, green, blue, alpha, ymin, ymax, p_228840_10_, p_228840_11_, p_228840_14_, p_228840_15_, u1, u2, v1, v2);
    addQuad(matrix4f, matrix3f, vertexBuilder, red, green, blue, alpha, ymin, ymax, p_228840_12_, p_228840_13_, p_228840_8_, p_228840_9_, u1, u2, v1, v2);
  }

  private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
                              float red, float green, float blue, float alpha, int yMin, int yMax,
                              float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
    addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMax, x1, z1, u2, v1);
    addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMin, x1, z1, u2, v2);
    addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMin, x2, z2, u1, v2);
    addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMax, x2, z2, u1, v1);
  }

  private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
                                float red, float green, float blue, float alpha,
                                int y, float x, float z, float texU, float texV) {
    vertexBuilder.func_227888_a_(matrixPos, x, (float)y, z) // pos
            .func_227885_a_(red, green, blue, alpha)        // color
            .func_225583_a_(texU, texV)                     // tex
            .func_227891_b_(OverlayTexture.field_229196_a_) // overlay; field_229196_a_ = no modifier
            .func_227886_a_(0xf000f0)                       // lightmap with full brightness
            .func_227887_a_(matrixNormal, 0.0F, 1.0F, 0.0F)  // normal
            .endVertex();
  }

  public boolean isGlobalRenderer(BeaconTileEntity te) {
    return true;
  }
}