package minecraftbyexample.mbe21_tileentityrenderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * User: The Grey Ghost
 * Date: 12/01/2015
 * This class renders the artifact floating above the block.
 * The base model (the hopper shape) is drawn by the block model, not this class.
 * See assets/minecraftbyexample/blockstates/mbe21_tesr_block_registry_name.json
 *
 * The class demonstrates four different examples of rendering:
 * 1) Lines
 * 2) Manually drawing quads
 * 3) Rendering a block model
 * 4) Rendering a wavefront object
 *
 * 1) The lines have position and colour information only  (RenderType.getLines().  No lightmap information, which means that they will always be the
 *   same brightness regardless of day/night or nearby torches.
 *
 * 2) The quads have position, colour, texture, normal, and lightmap information
 *   RenderType.getSolid() is suitable if you're using a texture which has been stitched into the block texture sheet (either by defining it
 *      in a block model, or by manually adding it during TextureStitchEvent.)
 *   Otherwise you need to create your own RenderType.
 *
 * 3) Reads the block model for vanilla object and renders a smaller version of it
 *
 * 4) Reads a custom wavefront object from a file
 *
 */
public class TileEntityRendererMBE21 extends TileEntityRenderer<TileEntityMBE21> {

  public TileEntityRendererMBE21(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
    super(tileEntityRendererDispatcher);
  }

  /**
   * (this function is called "render" in previous mcp mappings)
   * render the tile entity - called every frame while the tileentity is in view of the player
   *
   * @param tileEntityMBE21 the associated tile entity
   * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
   *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
   *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
   * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
   *                        it is needed for you to render the view properly.
   * @param renderBuffers    the buffer that you should render your model to
   * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
   * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
   *                        Used by vanilla for (1) red tint when a living entity is damaged, and (2) "flash" effect for creeper when ignited
   *                        CreeperRenderer.func_225625_b_()
   */
  @Override
  public void render(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                     int combinedLight, int combinedOverlay) {
    TileEntityMBE21.EnumRenderStyle objectRenderStyle = tileEntityMBE21.getArtifactRenderStyle();

    switch (objectRenderStyle) {
      case WIREFRAME: RenderLines.renderWireframe(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
      case QUADS: RenderQuads.renderCubeUsingQuads(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
      case BLOCKQUADS: RenderModelHourglass.renderUsingModel(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
//      case WAVEFRONT: renderWireframe(tileEntityMBE21, partialTicks, matrixStack, renderBuffers, combinedLight, combinedOverlay); break;
      default: { LOGGER.debug("Unexpected objectRenderStyle:" + objectRenderStyle);}
    }

    int blockLight = LightTexture.getLightBlock(combinedLight);
    int skyLight = LightTexture.getLightBlock(combinedLight);
    int repackedValue = LightTexture.packLight(blockLight, skyLight);
  }

//  private void renderWireframe(TileEntityMBE21 tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer,
//                               int combinedLight, int combinedOverlay) {
//    // draw the object using lines
//    // When render method is called, the origin [0,0,0] is at the current [x,y,z] of the block.
//    // Draw an inverted tetrahedron wireframe above the rendered base block
//    // The tetrahedron-drawing method draws the tetrahedron in a cube region from [0,0,0] to [1,1,1] but we want it
//    //   to be in the block one above this, i.e. from [0,1,0] to [1,2,1],
//    //   so we need to translate up by one block, i.e. by [0,1,0]
//    final Vec3d TRANSLATION_OFFSET = new Vec3d(0, 1, 0);
//
//    matrixStack.push(); // push the current transformation matrix + normals matrix
//    matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
//    Color artifactColour = tileEntityMBE21.getArtifactColour();
//
//    drawTetrahedronWireframe(matrixStack, renderBuffer, artifactColour);
//    matrixStack.pop(); // restore the original transformation matrix + normals matrix
//  }
//

//    ResourceLocation beamTextureRL = TEXTURE_BEACON_BEAM;
////    matrixStack.func_227860_a_(); //push
////    matrixStack.func_227863_a_(   //rotate
////            Vector3f.field_229181_d_.func_229187_a_(f * 2.25F - 45.0F));    //  YP.rotationDegrees
//    IVertexBuilder vertexBuilder = renderBuffer.getBuffer(RenderType.getBeaconBeam(beamTextureRL, false));
//    float red = 0;
//    float green = 0;
//    float blue = 0;
//    float alpha = 1.0f;
//    int startHeight = 0;
//    int endHeight = 1;
//    float u1 = 0;
//    float u2 = 1;
//    float v1 = 0;
//    float v2 = 0;
//    float beamRadius = 1;
//
//    float x1 = 0; float z1 = 0;
//    float x2 = 1; float z2 = 0;
//    float x3 = 0; float z3 = 1;
//    float x4 = 1; float z4 = 1;
//
////    renderPart(matrixStack, vertexBuilder, red, green, blue, alpha, startHeight, endHeight,
////             x1, z1, x2, z2, x3, z3, x4, z4, u1, u2, v1, v2);

    // the artifact changes its appearance and animation as the player approaches.
    // When the player is a long distance away, the artifact is dark, resting in the hopper, and does not rotate.
    // As the player approaches closer than 16 block, the artifact first starts to glow brighter and to spin anti-clockwise
    // When the player gets closer than 4 block, the artifact is at maximum speed and brightness, and starts to levitate above the pedestal
    // Once the player gets closer than 2 block, the artifact reaches maximum height.

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
//
//  private static void renderPart(MatrixStack matrixStack, IVertexBuilder vertexBuilder,
//                                 float red, float green, float blue, float alpha, int ymin, int ymax,
//                                 float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
//                                 float u1, float u2, float v1, float v2) {
//    MatrixStack.Entry currentTransformMatrix = matrixStack.getLast();  // getLast
//    Matrix4f positionMatrix = currentTransformMatrix.getMatrix();  // getPositionMatrix
//    Matrix3f normalMatrix = currentTransformMatrix.getNormal();  // getNormalMatrix
//    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x1, z1, x2, z2, u1, u2, v1, v2);
//    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x4, z4, x3, z3, u1, u2, v1, v2);
//    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x2, z2, x4, z4, u1, u2, v1, v2);
//    addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x3, z3, x1, z1, u1, u2, v1, v2);
//  }


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
                   .tex((float) vertex[3], (float) vertex[4])                         // func_225583_a_ is tex
                   .endVertex();
    }
  }
//
//  /**
//   * Draw a cube from [0,0,0] to [1,1,1], same texture on all sides, texture has been stitched into the block texture sheet
//   */
//    private static void drawCubeUsingQuads(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer,
//                                           Color cubeColour,
//                                           int combinedLight, int combinedOverlay){
//
//      // out
//      IVertexBuilder vertexBuilderBlockQuads = renderBuffer.getBuffer(RenderType.getSolid());
//      Matrix4f matrixPos = matrixStack.getLast().getMatrix();     // retrieves the current transformation matrix
//      Matrix3f matrixNormal = matrixStack.getLast().getNormal();  // retrieves the current transformation matrix for the normal vector
//
//      // all faces have the same height and width
//      final float WIDTH = 1.0F;
//      final float HEIGHT = 1.0F;
//
//      Minecraft.getInstance().getTextureManager().getTexture();
//
//      addFace(Direction.EAST,
//              matrixPos, matrixNormal, vertexBuilderBlockQuads,
//              cubeColour, new Vec3d(1.0, 0.5, 0.5), WIDTH, HEIGHT,
//              ;
//
//    }
//
//    private static void addFace(Direction whichFace,
//                                Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
//                              Color color, Vec3d centrePos, float width, float height,
//                              Vec2f bottomLeftUV, float texUwidth, float texVheight,
//                              int lightmapValue) {
//    // the Direction class has a bunch of methods which can help you rotate quads
//    //  I've written the calculations out long hand, and based them on a centre position, to make it clearer what
//    //   is going on.
//    // Beware that the Direction class is based on which direction the face is pointing, which is opposite to
//    //   the direction that the viewer is facing when looking at the face.
//    // Eg when drawing the NORTH face, the face points north, but when we're looking at the face, we are facing south,
//    //   so that the bottom left corner is the eastern-most, not the western-most!
//
//    //calculate the bottom left, bottom right, top right, top left vertices from the VIEWER's point of view (not the
//    //  face's point of view)
//
//    Vector3f leftToRightDirection, bottomToTopDirection;
//
//    switch (whichFace) {
//      case NORTH: { // bottom left is east
//        leftToRightDirection = new Vector3f(-1, 0, 0);  // or alternatively Vector3f.XN
//        bottomToTopDirection = new Vector3f(0, 1, 0);  // or alternatively Vector3f.YP
//        break;
//      }
//      case SOUTH: {  // bottom left is west
//        leftToRightDirection = new Vector3f(1, 0, 0);
//        bottomToTopDirection = new Vector3f(0, 1, 0);
//        break;
//      }
//      case EAST: {  // bottom left is south
//        leftToRightDirection = new Vector3f(0, 0, -1);
//        bottomToTopDirection = new Vector3f(0, 1, 0);
//      }
//      case WEST: { // bottom left is north
//        leftToRightDirection = new Vector3f(0, 0, 1);
//        bottomToTopDirection = new Vector3f(0, 1, 0);
//      }
//      case UP: { // bottom left is southwest by minecraft block convention
//        leftToRightDirection = new Vector3f(-1, 0, 0);
//        bottomToTopDirection = new Vector3f(0, 0, 1);
//      }
//      case DOWN: { // bottom left is northwest by minecraft block convention
//        leftToRightDirection = new Vector3f(1, 0, 0);
//        bottomToTopDirection = new Vector3f(0, 0, 1);
//      }
//      default: {  // should never get here, but just in case;
//        leftToRightDirection = Vector3f.XP;
//        bottomToTopDirection = Vector3f.YP;
//      }
//    }
//    leftToRightDirection.mul(0.5F * width);  // convert to half width
//    bottomToTopDirection.mul(0.5F * height);  // convert to half height
//
//    // calculate the four vertices based on the centre of the face
//
//    Vector3f bottomLeftPos = new Vector3f(centrePos);
//      bottomLeftPos.sub(leftToRightDirection);
//      bottomLeftPos.sub(bottomToTopDirection);
//
//    Vector3f bottomRightPos = new Vector3f(centrePos);
//    bottomRightPos.add(leftToRightDirection);
//    bottomRightPos.sub(bottomToTopDirection);
//
//    Vector3f topRightPos = new Vector3f(centrePos);
//    topRightPos.add(leftToRightDirection);
//    topRightPos.add(bottomToTopDirection);
//
//    Vector3f topLeftPos = new Vector3f(centrePos);
//    topLeftPos.sub(leftToRightDirection);
//    topLeftPos.add(bottomToTopDirection);
//
//    // texture coordinates are "upside down" relative to the face
//    // eg bottom left = [U min, V max]
//    Vec2f bottomLeftUVpos = new Vec2f(bottomLeftUV.x, bottomLeftUV.y);
//    Vec2f bottomRightUVpos = new Vec2f(bottomLeftUV.x + texUwidth, bottomLeftUV.y);
//    Vec2f topLeftUVpos = new Vec2f(bottomLeftUV.x + texUwidth, bottomLeftUV.y + texVheight);
//    Vec2f topRightUVpos = new Vec2f(bottomLeftUV.x, bottomLeftUV.y + texVheight);
//
//    Vector3f normalVector = whichFace.toVector3f();  // gives us the normal to the face
//
//    addQuad(matrixPos, matrixNormal, vertexBuilder,
//            bottomLeftPos, bottomRightPos, topRightPos, topLeftPos,
//            bottomLeftUVpos, bottomRightUVpos, topLeftUVpos, topRightUVpos,
//            normalVector, color, lightmapValue);
//  }
//
//  /**
//   * Add a quad.
//   * It is added in anti-clockwise order from the VIEWER's  point of view, i.e.
//   * bottom left; bottom right, top right, top left
//   * If you add it in the other direction, the quad will face in the opposite direction; i.e. the viewer will be
//   *   looking at the back face, which is usually culled (not visible)
//   * See
//   * http://greyminecraftcoder.blogspot.com/2014/12/the-tessellator-and-worldrenderer-18.html
//   * http://greyminecraftcoder.blogspot.com/2014/12/block-models-texturing-quads-faces.html
//   */
//  private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
//                              Vector3f blpos, Vector3f brpos, Vector3f trpos, Vector3f tlpos,
//                              Vec2f blUVpos, Vec2f brUVpos, Vec2f trUVpos, Vec2f tlUVpos,
//                              Vector3f normalVector, Color color, int lightmapValue) {
//    addQuadVertex(matrixPos, matrixNormal, vertexBuilder, blpos, blUVpos, normalVector, color, lightmapValue);
//    addQuadVertex(matrixPos, matrixNormal, vertexBuilder, brpos, brUVpos, normalVector, color, lightmapValue);
//    addQuadVertex(matrixPos, matrixNormal, vertexBuilder, trpos, trUVpos, normalVector, color, lightmapValue);
//    addQuadVertex(matrixPos, matrixNormal, vertexBuilder, tlpos, tlUVpos, normalVector, color, lightmapValue);
//  }
//
//  private static void addQuadVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
//                                    Vector3f pos, Vec2f texUV,
//                                    Vector3f normalVector, Color color, int lightmapValue) {
//    vertexBuilder.pos(matrixPos, pos.getX(), pos.getY(), pos.getZ()) // position coordinate
//            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())        // color
//            .tex(texUV.x, texUV.y)                     // texel coordinate
//            .overlay(OverlayTexture.NO_OVERLAY) // overlay; field_229196_a_ = no modifier
//            .lightmap(lightmapValue)             // lightmap with full brightness
//            .normal(matrixNormal, normalVector.getX(), normalVector.getY(), normalVector.getZ())
//            .endVertex();
//  }

//  /**
//   * Draw an upside-down wireframe tetrahedron with its tip at [0.5,0,0.5]
//   *    and 1x1 square "base" at y = 1 (x= 0 to 1, z = 0 to 1)
//   * @param matrixStack transformation matrix and normal matrix
//   * @param renderBuffer the renderbuffers we'll be drawing to
//   */
//  private static void drawTetrahedronWireframe(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer,
//                                               Color color) {
//
//      final Vec3d [] BASE_VERTICES = {
//              new Vec3d(0, 1, 0),
//              new Vec3d(1, 1, 0),
//              new Vec3d(1, 1, 1),
//              new Vec3d(0, 1, 1),
//      };
//      final Vec3d APEX_VERTEX = new Vec3d(0.5, 0, 0.5);
//
//    IVertexBuilder vertexBuilderLines = renderBuffer.getBuffer(RenderType.getLines());
//    Matrix4f matrixPos = matrixStack.getLast().getMatrix();  //retrieves the current transformation matrix
//    // draw the base
//    for (int i = 1; i < BASE_VERTICES.length; ++i) {
//      drawLine(matrixPos, vertexBuilderLines, color, BASE_VERTICES[i-1], BASE_VERTICES[i]);
//    }
//    drawLine(matrixPos, vertexBuilderLines, color, BASE_VERTICES[BASE_VERTICES.length - 1], BASE_VERTICES[0]);
//
//    // draw the sides (from the corners of the base to the apex)
//    for (Vec3d baseVertex : BASE_VERTICES) {
//      drawLine(matrixPos, vertexBuilderLines, color, APEX_VERTEX, baseVertex);
//    }
//  }

//  private static void drawCubeFromQuads(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer,
//                                        Color color) {
//
//    // we are using a solid block render with texture that has been stitched into the block texture sheet
//    IVertexBuilder vertexBuilderQuads = renderBuffer.getBuffer(RenderType.getSolid());
//    Matrix4f matrixPos = matrixStack.getLast().getMatrix();  //retrieves the current transformation matrix
//    Matrix3f matrixNormal = matrixStack.getLast().getNormal();
//
//    // retrieve the [U,V] coordinates of the texture that we want to use
//    TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(StartupClientOnly.MBE21_CUBE_TEXTURE).apply(StartupClientOnly.MBE21_CUBE_TEXTURE);
//    Vec2f bottomLeftUV = new Vec2f(sprite.getMinU(), sprite.getMaxV());
//    float UVwidth = sprite.getMaxU() - sprite.getMinU();
//    float UVheight = sprite.getMinV() - sprite.getMaxV();
//
//
//
//  }

//  public static RenderType getQuadsWithCustomTexture(ResourceLocation textureRL) {
//    RenderType.State renderTypeState = RenderType.State.getBuilder()
//            .texture(new RenderState.TextureState(textureRL, false, false))
//            .build(false);
//
//    RenderType.State.getBuilder().shadeModel(RenderType.SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).build(true)
//
//    return makeType("beacon_beam", DefaultVertexFormats.BLOCK, 7, 256, false, true, renderTypeState);
//
//    SOLID = makeType("solid", DefaultVertexFormats.BLOCK, 7, 2097152, true, false,);
//
//
//  }


//
//
//  /**
//   * Draw a coloured line from a starting vertex to an end vertex
//   * @param matrixPos the current transformation matrix
//   * @param vertexBuilderLines the vertex builder used to draw the line
//   * @param startVertex
//   * @param endVertex
//   */
//  private static void drawLine(Matrix4f matrixPos, IVertexBuilder vertexBuilderLines,
//                               Color color,
//                               Vec3d startVertex, Vec3d endVertex) {
//    vertexBuilderLines.pos(matrixPos, (float) startVertex.getX(), (float) startVertex.getY(), (float) startVertex.getZ())
//            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
//            .endVertex();
//    vertexBuilderLines.pos(matrixPos, (float) endVertex.getX(), (float) endVertex.getY(), (float) endVertex.getZ())
//            .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())   // there is also a version for floats (0 -> 1)
//            .endVertex();
//  }
//


//  private static final ResourceLocation gemTexture = new ResourceLocation("minecraftbyexample:textures/entity/mbe21_tesr_gem.png");

  //notes see RenderType
  // vertexbuilder addQuad for a baked quad

  private static final Logger LOGGER = LogManager.getLogger();
}
