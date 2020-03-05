package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 * This class renders the gem floating above the block.
 * The base model (the hopper shape) is drawn by the block model, not this class.
 */
public class TileEntityRendererMBE21 extends TileEntityRenderer<TileEntityMBE21>
{

  public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

  public TileEntityRendererMBE21(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
    super(tileEntityRendererDispatcher);
  }

  /**
   *  (this function is called "render" in previous mcp mappings)
   * render the tile entity - called every frame while the tileentity is in view of the player
   * @param tileEntityMBE21 the associated tile entity
   * @param partialTicks the fraction of a tick that this frame is being rendered at - used to interpolate frames between
   *                     ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
   *                     this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
   * @param matrixStack the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
   *                    it is needed for you to render the view properly.
   * @param renderBuffer the buffer that you should render your model to
   * @param combinedLight the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
   * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
   *                        Used by vanilla for (1) red tint when a living entity is damaged, and (2) "flash" effect for creeper when ignited
   *                        CreeperRenderer.func_225625_b_()
   */
  @Override
  public void render(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer,
                             int combinedLight, int combinedOverlay) {
    matrixStack.push(); // push
    matrixStack.translate(0.5D, 0.0D, 0.5D); // translate

    ResourceLocation beamTextureRL = TEXTURE_BEACON_BEAM;
//    matrixStack.func_227860_a_(); //push
//    matrixStack.func_227863_a_(   //rotate
//            Vector3f.field_229181_d_.func_229187_a_(f * 2.25F - 45.0F));    //  YP.rotationDegrees
    IVertexBuilder vertexBuilder = renderBuffer.getBuffer(RenderType.getBeaconBeam(beamTextureRL, false));
    float red = 0;
    float green = 0;
    float blue = 0;
    float alpha = 1.0f;
    int startHeight = 0;
    int endHeight = 1;
    float u1 = 0;
    float u2 = 1;
    float v1 = 0;
    float v2 = 0;
    float beamRadius = 1;

    float x1 = 0; float z1 = 0;
    float x2 = 1; float z2 = 0;
    float x3 = 0; float z3 = 1;
    float x4 = 1; float z4 = 1;

    renderPart(matrixStack, vertexBuilder, red, green, blue, alpha, startHeight, endHeight,
             x1, z1, x2, z2, x3, z3, x4, z4, u1, u2, v1, v2);
    matrixStack.pop(); // pop


    // the gem changes its appearance and animation as the player approaches.
    // When the player is a long distance away, the gem is dark, resting in the hopper, and does not rotate.
    // As the player approaches closer than 16 block, the gem first starts to glow brighter and to spin anti-clockwise
    // When the player gets closer than 4 block, the gem is at maximum speed and brightness, and starts to levitate above the pedestal
    // Once the player gets closer than 2 block, the gem reaches maximum height.

    // the appearance and animation of the gem is hence made up of several parts:
    // 1) the colour of the gem, which is contained in the tileEntity
    // 2) the brightness of the gem, which depends on player distance
    // 3) the distance that the gem rises above the pedestal, which depends on player distance
    // 4) the speed at which the gem is spinning, which depends on player distance.

    // something to do with blocklight + sky light
//    int lvt_11_1_ = ((Int2IntFunction)lvt_10_1_.apply(new DualBrightnessCallback())).get(p_225616_5_);
//
//
//    final double pedestalCentreOffsetX = 0.5;
//    final double pedestalCentreOffsetY = 0.8;
//    final double pedestalCentreOffsetZ = 0.5;
//    Vec3d playerEye = new Vec3d(0.0, 0.0, 0.0);
//    Vec3d pedestalCentre = new Vec3d(relativeX + pedestalCentreOffsetX, relativeY + pedestalCentreOffsetY, relativeZ + pedestalCentreOffsetZ);
//    double playerDistance = playerEye.distanceTo(pedestalCentre);
//
//    final double DISTANCE_FOR_MIN_SPIN = 8.0;
//    final double DISTANCE_FOR_MAX_SPIN = 4.0;
//    final double DISTANCE_FOR_MIN_GLOW = 16.0;
//    final double DISTANCE_FOR_MAX_GLOW = 4.0;
//    final double DISTANCE_FOR_MIN_LEVITATE = 4.0;
//    final double DISTANCE_FOR_MAX_LEVITATE = 2.0;
//
//    final double MIN_LEVITATE_HEIGHT = 0.0;
//    final double MAX_LEVITATE_HEIGHT = 0.5;
//    double gemCentreOffsetX = pedestalCentreOffsetX;
//    double gemCentreOffsetY = pedestalCentreOffsetY + UsefulFunctions.interpolate(playerDistance, DISTANCE_FOR_MIN_LEVITATE, DISTANCE_FOR_MAX_LEVITATE,
//            MIN_LEVITATE_HEIGHT, MAX_LEVITATE_HEIGHT);
//    double gemCentreOffsetZ = pedestalCentreOffsetZ;
//
//    final double MIN_GLOW = 0.0;
//    final double MAX_GLOW = 1.0;
//    double glowMultiplier = UsefulFunctions.interpolate(playerDistance, DISTANCE_FOR_MIN_GLOW, DISTANCE_FOR_MAX_GLOW,
//            MIN_GLOW, MAX_GLOW);
//
//    final double MIN_REV_PER_SEC = 0.0;
//    final double MAX_REV_PER_SEC = 0.5;
//    double revsPerSecond = UsefulFunctions.interpolate(playerDistance, DISTANCE_FOR_MIN_SPIN, DISTANCE_FOR_MAX_SPIN,
//            MIN_REV_PER_SEC, MAX_REV_PER_SEC);
//    double angularPositionInDegrees = tileEntityMBE21.getNextAngularPosition(revsPerSecond);
//
//    try {
//      // save the transformation matrix and the rendering attributes, so that we can restore them after rendering.  This
//      //   prevents us disrupting any vanilla TESR that render after ours.
//      //  using try..finally is not essential but helps make it more robust in case of exceptions
//      // For further information on rendering using the Tessellator, see http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
//      GL11.glPushMatrix();
//      GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//
//      // First we need to set up the translation so that we render our gem with the bottom point at 0,0,0
//      // when the renderTileEntityAt method is called, the tessellator is set up so that drawing a dot at [0,0,0] corresponds to the player's eyes
//      // This means that, in order to draw a dot at the TileEntity [x,y,z], we need to translate the reference frame by the difference between the
//      // two points, i.e. by the [relativeX, relativeY, relativeZ] passed to the method.  If you then draw a cube from [0,0,0] to [1,1,1], it will
//      // render exactly over the top of the TileEntity's block.
//      // In this example, the zero point of our model needs to be in the middle of the block, not at the [x,y,z] of the block, so we need to
//      // add an extra offset as well, i.e. [gemCentreOffsetX, gemCentreOffsetY, gemCentreOffsetZ]
//      GlStateManager.translate(relativeX + gemCentreOffsetX, relativeY + gemCentreOffsetY, relativeZ + gemCentreOffsetZ);
//
//      GlStateManager.rotate((float)angularPositionInDegrees, 0, 1, 0);   // rotate around the vertical axis
//
//      final double GEM_HEIGHT = 0.5;        // desired render height of the gem
//      final double MODEL_HEIGHT = 1.0;      // actual height of the gem in the vertexTable
//      final double SCALE_FACTOR = GEM_HEIGHT / MODEL_HEIGHT;
//      GlStateManager.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
//
//      Tessellator tessellator = Tessellator.getInstance();
//      BufferBuilder bufferBuilder = tessellator.getBuffer();
//      this.bindTexture(gemTexture);         // texture for the gem appearance
//
//      // set the key rendering flags appropriately...
//      GL11.glDisable(GL11.GL_LIGHTING);     // turn off "item" lighting (face brightness depends on which direction it is facing)
//      GL11.glDisable(GL11.GL_BLEND);        // turn off "alpha" transparency blending
//      GL11.glDepthMask(true);               // gem is hidden behind other objects
//
//      // set the rendering colour as the gem base colour
//      Color fullBrightnessColor = tileEntityMBE21.getGemColour();
//      float red = 0, green = 0, blue = 0;
//      if (fullBrightnessColor != TileEntityMBE21.INVALID_COLOR) {
//        red = (float) (fullBrightnessColor.getRed() / 255.0);
//        green = (float) (fullBrightnessColor.getGreen() / 255.0);
//        blue = (float) (fullBrightnessColor.getBlue() / 255.0);
//      }
//      GlStateManager.color(red, green, blue);     // change the rendering colour
//
//        // change the "multitexturing" lighting value (default value is the brightness of the tile entity's block)
//        // - this will make the gem "glow" brighter than the surroundings if it is dark.
//      final int SKY_LIGHT_VALUE = (int)(15 * glowMultiplier);
//      final int BLOCK_LIGHT_VALUE = 0;
//      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, SKY_LIGHT_VALUE * 16.0F, BLOCK_LIGHT_VALUE * 16.0F);
//
//      bufferBuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
//      addGemVertices(bufferBuilder);
//      tessellator.draw();
//
//    } finally {
//      GL11.glPopAttrib();
//      GL11.glPopMatrix();
//    }
  }

  private static void renderPart(MatrixStack matrixStack, IVertexBuilder vertexBuilder,
                                 float red, float green, float blue, float alpha, int ymin, int ymax,
                                 float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
                                 float u1, float u2, float v1, float v2) {
    MatrixStack.Entry currentTransformMatrix = matrixStack.getLast();  // getLast
    Matrix4f positionMatrix = currentTransformMatrix.getMatrix();  // getPositionMatrix
    Matrix3f normalMatrix = currentTransformMatrix.getNormal();  // getNormalMatrix
    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x1, z1, x2, z2, u1, u2, v1, v2);
    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x4, z4, x3, z3, u1, u2, v1, v2);
    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x2, z2, x4, z4, u1, u2, v1, v2);
    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x3, z3, x1, z1, u1, u2, v1, v2);
  }


  // this should be true for tileentities which render globally (no render bounding box), such as beacons.
  @Override
  public boolean isGlobalRenderer(TileEntityMBE21 tileEntityMBE21)
  {
    return false;
  }

  // add the vertices for drawing the gem.  Generated using a model builder and pasted manually because the object model
  //   loader wasn't implemented at the time I wrote this example...
  private void addGemVertices(BufferBuilder bufferBuilder) {
    final double[][] vertexTable = {
            {0.000,1.000,0.000,0.000,0.118},          //1
            {-0.354,0.500,-0.354,0.000,0.354},
            {-0.354,0.500,0.354,0.236,0.236},
            {-0.354,0.500,0.354,0.236,0.236},         //2
            {-0.354,0.500,-0.354,0.000,0.354},
            {0.000,0.000,0.000,0.236,0.471},
            {-0.354,0.500,0.354,0.236,0.236},         //3
            {0.000,0.000,0.000,0.236,0.471},
            {0.354,0.500,0.354,0.471,0.354},
            {-0.354,0.500,0.354,0.236,0.236},         //4
            {0.354,0.500,0.354,0.471,0.354},
            {0.000,1.000,0.000,0.471,0.118},
            {0.000,1.000,0.000,0.471,0.118},          //5
            {0.354,0.500,0.354,0.471,0.354},
            {0.354,0.500,-0.354,0.707,0.236},
            {0.354,0.500,-0.354,0.707,0.236},         //6
            {0.354,0.500,0.354,0.471,0.354},
            {0.000,0.000,0.000,0.707,0.471},
            {0.354,0.500,-0.354,0.707,0.236},         //7
            {0.000,0.000,0.000,0.707,0.471},
            {-0.354,0.500,-0.354,0.943,0.354},
            {0.000,1.000,0.000,0.943,0.118},          //8
            {0.354,0.500,-0.354,0.707,0.236},
            {-0.354,0.500,-0.354,0.943,0.354}
              };

    for (double [] vertex : vertexTable) {
      bufferBuilder.pos(vertex[0], vertex[1], vertex[2])   // func_225582_a_ is pos
                   .tex((float)vertex[3], (float)vertex[4])                         // func_225583_a_ is tex
                   .endVertex();
    }
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
    vertexBuilder.pos(matrixPos, x, (float)y, z) // pos
            .color(red, green, blue, alpha)        // color
            .tex(texU, texV)                     // tex
            .overlay(OverlayTexture.NO_OVERLAY) // overlay; field_229196_a_ = no modifier
            .lightmap(0xf000f0)                       // lightmap with full brightness
            .normal(matrixNormal, 0.0F, 1.0F, 0.0F)  // normal
            .endVertex();
  }

  private static final ResourceLocation gemTexture = new ResourceLocation("minecraftbyexample:textures/entity/mbe21_tesr_gem.png");
}
