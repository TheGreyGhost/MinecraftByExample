package minecraftbyexample.mbe04_block_dynamic_block_models;

import com.google.common.collect.ImmutableList;
import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static net.minecraftforge.client.model.SimpleModelTransform.IDENTITY;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class is used to customise the rendering of the altimeter block, based on the block it is copying.
 * It uses the IForgeBakedModel extension of IBakedModel to pass IModelData (the "GPS coordinates" to be copied) to the getQuads.
 * getQuads then uses this information construct a mod.
 *
 * The Altimeter is composed of:
 * 1) A base model which is registered using the Altimeter Block (blockstates json) as normal
 * 2) Twelve quads with digits from 0 to 9.  These are generated programmatically depending on the blocks' altitude
 * 3) An arrow consisting of multiple copies of the needle model (a cube which is 1/16 in size).  The model for the needle is registered
 *     using ModelRegistryEvent; the model is retrieved, copied multiple times, and translated to the correct locations
 *
 */
public class AltimeterBakedModel implements IBakedModel {

  public AltimeterBakedModel(IBakedModel baseModel)
  {
    this.baseModel = baseModel;
  }

  public static ModelProperty<Optional<BlockAltimeter.GPScoordinate>> GPS_COORDINATE = new ModelProperty<>();

  public static ModelDataMap getEmptyIModelData() {
    ModelDataMap.Builder builder = new ModelDataMap.Builder();
    builder.withInitial(GPS_COORDINATE, Optional.empty());
    ModelDataMap modelDataMap = builder.build();
    return modelDataMap;
  }

  @Override
  @Nonnull
  public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
  {
    Optional<BlockAltimeter.GPScoordinate> bestAdjacentBlock = BlockAltimeter.getGPScoordinate(world, pos);
    ModelDataMap modelDataMap = getEmptyIModelData();
    modelDataMap.setData(GPS_COORDINATE, bestAdjacentBlock);
    return modelDataMap;
  }

  /**
   * Forge's extension in place of IBakedModel::getQuads
   * It allows us to pass in some extra information which we can use to choose the appropriate quads to render
   * @param state
   * @param side  quads for which side? if null: general quads.  if non-null: quads which can be culled (eg west means
   *              to return quads which are at the westmost edge of the block, i.e. which will be invisible if the block
   *              to the west of this one has a solid east face)
   * @param rand
   * @param extraData
   * @return
   */
  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
  {
    // shortcut: the quads that we are generating programmatically are all side==null, so if side != null then just return the base model
    if (side != null) {
      return baseModel.getQuads(state, side, rand);
    }
    return getBakedQuadsFromIModelData(state, side, rand, extraData);
  }

  private List<BakedQuad> getBakedQuadsFromIModelData(@Nullable BlockState state, Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
    if (!data.hasProperty(GPS_COORDINATE)) {
      if (!loggedError) {
        LOGGER.error("IModelData did not have expected property GPS_COORDINATE");
        loggedError = true;
      }
      return baseModel.getQuads(state, side, rand);
    }
    Optional<BlockAltimeter.GPScoordinate> gPScoordinate = data.getData(GPS_COORDINATE);
    if (!gPScoordinate.isPresent()) return baseModel.getQuads(state, side, rand);

    List<BakedQuad> digitQuads = getDigitQuads(gPScoordinate.get());

    List<BakedQuad> allQuads = new LinkedList<>();
    allQuads.addAll(baseModel.getQuads(state, side, rand, data));
    allQuads.addAll(digitQuads);

    return allQuads;
  }

  /**
   * Returns the quads which show the altitude in digits
   * @param gpScoordinate
   * @return List of twelve BakedQuads for the digits
   */
  private List<BakedQuad> getDigitQuads(BlockAltimeter.GPScoordinate gpScoordinate)  {

    // convert the altitude into digits
    final int MIN_ALTITUDE = 0;
    final int MAX_ALTITUDE = 999;
    int altitudeDigits = MathHelper.clamp(gpScoordinate.altitude, MIN_ALTITUDE, MAX_ALTITUDE);
    int digit100 = altitudeDigits % 100;
    altitudeDigits -= digit100 * 100;
    int digit10 = altitudeDigits % 10;
    altitudeDigits -= digit10 * 10;
    int digit1 = altitudeDigits;

    // remove leading zeros (will render as blanks)
    boolean digit100IsBlank = digit100 == 0;
    boolean digit10IsBlank = digit100IsBlank && digit10 == 0;

    // coordinates of the digit quads.
    // you can generate these programmatically with a bit of cleverness and the Direction class methods but I've used an int array for clarity instead
    // each group of six is minX, minY, minZ, to maxX, maxY, maxZ for that digit quad
    // for example - north face digit100 is faceCoordinates[0][0] which is from [10.5, 4, 0.5] to [13.5, 9, 0.5]
    final double[][][] faceCoordinates = {
            { {10.5, 4,  0.5,  13.5, 9,  0.5},  {  6.5, 4,  0.5,   9.5, 9,  0.5}, { 2.5, 4,  0.5,   5.5, 9,  0.5}},   // north face digit100, digit10, digit1
            { { 2.5, 4, 14.5,   5.5, 9, 14.5},  {  6.5, 4, 14.5,   9.5, 9, 14.5}, {10.5, 4, 14.5,  13.5, 9, 14.5}},   // south face digit100, digit10, digit1
            { { 0.5, 4,  2.5,   0.5, 9,  5.5},  {  0.5, 4,  6.5,   0.5, 9,  9.5}, { 0.5, 4, 10.5,   0.5, 9, 13.5}},   // west face digit100, digit10, digit1
            { {14.5, 4, 10.5,  14.5, 9, 13.5},  { 14.5, 4,  6.5,  14.5, 9,  9.5}, {14.5, 4,  5.5,  14.5, 9,  2.5}}    // east face digit100, digit10, digit1
    };
    final Direction [] faceDirections = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    ImmutableList.Builder<BakedQuad> builder = new ImmutableList.Builder<BakedQuad>();

    for (int face = 0; face < 1; ++face) { //todo 4
      double [] fmm = faceCoordinates[face][0]; // face min+max
      builder.add(getQuadForDigit(digit100, digit100IsBlank, faceDirections[face], fmm[0], fmm[1], fmm[2], fmm[3], fmm[4], fmm[5]));

//      fmm = faceCoordinates[face][1];
//      builder.add(getQuadForDigit(digit10, digit10IsBlank, faceDirections[face], fmm[0], fmm[1], fmm[2], fmm[3], fmm[4], fmm[5]));
//      fmm = faceCoordinates[face][2];
//      builder.add(getQuadForDigit(digit1, false, faceDirections[face], fmm[0], fmm[1], fmm[2], fmm[3], fmm[4], fmm[5]));
    }

    return builder.build();
  }

  /**
   * Returns a quad for the given digit
   * @param digit the digit (0 -> 9)
   * @param isBlank if true: this digit should be blank (is a leading zero)
   * @param minXYZ: the minimum [x,y,z] of the digit quad (from the viewer's point of view).  units = model space i.e. 0->16 is 1 metre block
   * @param maxXYZ: the maximum [x,y,z] of the digit quad (from the viewer's point of view).  units = model space i.e. 0->16 is 1 metre block
   * @return
   */
  private BakedQuad getQuadForDigit(int digit, boolean isBlank, Direction whichFace,
                                    double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    // generate a BakedQuad for the given digit

    // we can do this manually by providing a list of vertex data, or we can use the FaceBakery::bakeQuads method
    // FaceBakery::bakeQuad is much simpler and suitable for pretty much any block-style rendering, so I've used that here
    // If you want to manually provide vertex data yourself, the format is an array of ints; look in
    // IVertexBuilder::addQuad and FaceBakery; see also DefaultVertexFormats.BLOCK.
    // Summary:
    //    faceData[i + 0] = Float.floatToRawIntBits(positionIn.getX());
    //    faceData[i + 1] = Float.floatToRawIntBits(positionIn.getY());
    //    faceData[i + 2] = Float.floatToRawIntBits(positionIn.getZ());
    //    faceData[i + 3] = shadeColor;
    //    faceData[i + 4] = Float.floatToRawIntBits(textureU));
    //    faceData[i + 5] = Float.floatToRawIntBits(textureV));
    //    faceData[i + 6] = baked lighting (blocklight + skylight)
    //    faceData[i + 7] = normal;
    // When constructing a face manually in this way, the order of vertices is very important!
    // 1) must be added anti-clockwise (from the point of view of the person looking at the face).  Otherwise the face
    //    will point in the wrong direction and it may be invisible (backs of faces are usually culled for block rendering)
    // 2) ambient occlusion (a block lighting effect) assumes that the vertices are added in the order:
    //     top left, then bottom left, then bottom right, then top right - for the east, west, north, south faces.
    //     for the top face: NW, SW, SE, NE.  for the bottom face: SW, NW, NE, SE
    //    If your face has ambient occlusion enabled, and the order is wrong, then the shading will be messed up

    // FaceBakery:
    //  Vanilla uses it to convert from the elements in a block model, i.e.
    //    "elements": [
    //    { "from": [ 7, 0, 7 ],
    //      "to": [ 9, 10, 9 ],
    //      "shade": false,
    //      "faces": {
    //        "down": { "uv": [ 7, 13, 9, 15 ], "texture": "#torch" },
    //        "up":   { "uv": [ 7,  6, 9,  8 ], "texture": "#torch" }
    //      }
    //    },
    //    see https://minecraft.gamepedia.com/Model#Block_models
    //  In order to use the FaceBakery::bakeQuad method, we need to provide:
    //   1) A suitable cuboid 'from' and 'to', in model coordinate (eg the full 1 metre cube is from [0,0,0] to [16, 16, 16])
    //   2) the corresponding [u,v] texture coordinates for the face: [minU,minV] first then [maxU,maxV], again in texels 0->16
    //   3) the face we want to make the quad for (eg up, down, east, west, etc).

    Vector3f from = new Vector3f((float)minX, (float)minY, (float)minZ);
    Vector3f to = new Vector3f((float)maxX, (float)maxY, (float)maxZ);

    // texture UV order is important! i.e. [minU,minV] first then [maxU,maxV]
    float [] uvArray = getDigitUVs(digit, isBlank);
    final int ROTATION_NONE = 0;
    BlockFaceUV blockFaceUV = new BlockFaceUV(uvArray, ROTATION_NONE);

    final Direction NO_FACE_CULLING = null;
    final int TINT_INDEX_NONE = -1;  // used for tintable blocks such as grass, which make a call to BlockColors to change their rendering colour.  -1 for not tintable.
    final String DUMMY_TEXTURE_NAME = "";  // texture name is only needed for loading from json files; not needed here
    BlockPartFace blockPartFace = new BlockPartFace(NO_FACE_CULLING, TINT_INDEX_NONE, DUMMY_TEXTURE_NAME,  blockFaceUV);

    // we have previously registered digitsTexture in StartupClientOnly::onTextureStitchEvent
    AtlasTexture blocksStitchedTextures = ModelLoader.instance().getSpriteMap().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
    TextureAtlasSprite digitsTextures = blocksStitchedTextures.getSprite(digitsTextureRL);

    final IModelTransform NO_TRANSFORMATION = IDENTITY;
    final BlockPartRotation DEFAULT_ROTATION = null;   // rotate based on the face direction
    final boolean APPLY_SHADING = true;
    final ResourceLocation DUMMY_RL = new ResourceLocation("dummy_name");  // used for error message only
    BakedQuad bakedQuad = faceBakery.bakeQuad(from, to, blockPartFace, digitsTextures, whichFace, NO_TRANSFORMATION, DEFAULT_ROTATION,
                             APPLY_SHADING, DUMMY_RL);
    return bakedQuad;
  }

  /**
   * Return the texture U,V for the given digit
   * @param digit 0 -> 9
   * @param isBlank blank digit?
   * @return int[4] of the texture u,v: [minU,minV] first then [maxU,maxV].  Units = 0 to 16
   */
  private float [] getDigitUVs(int digit, boolean isBlank) {
    // the texture for the digits has a first row for 0->4, then a second row for 5->9, then a single blank on the third row
    final int DIGIT_TEXEL_WIDTH = 3;
    final int DIGIT_TEXEL_HEIGHT = 5;

    int minU, minV;
    if (isBlank) {
      minU = 0;
      minV = DIGIT_TEXEL_HEIGHT * 2;
    } else {
      digit = MathHelper.clamp(digit, 0, 9);
      int row = digit / 5;
      minU = (digit % 5) * DIGIT_TEXEL_WIDTH;
      minV = row * DIGIT_TEXEL_HEIGHT;
    }
    return new float[]{minU, minV, minU + DIGIT_TEXEL_WIDTH, minV + DIGIT_TEXEL_HEIGHT};
  }


  private FaceBakery faceBakery = new FaceBakery();

//  private List<BakedQuad> getArrowQuads(BlockAltimeter.GPScoordinate gpScoordinate)  {
//    Minecraft mc = Minecraft.getInstance();
//    BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
//    Blo
//            retval = blockRendererDispatcher.getModelForState(gpScoordinate.get());
//    Minecraft mc = Minecraft.getInstance();
//    BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
//    retval = blockRendererDispatcher.getModelForState(gpScoordinate.get());
//
//
//  }

  private IBakedModel baseModel;


  public static ResourceLocation needleTextureRL = new ResourceLocation("minecraftbyexample:block/mbe04b_altimeter_needle");
  public static ResourceLocation digitsTextureRL = new ResourceLocation("minecraftbyexample:block/mbe04b_altimeter_digits");


  // ---- All these methods are required by the interface but we don't do anything special with them.

  @Override
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
    throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
  }

  // getTexture is used directly when player is inside the block.  The game will crash if you don't use something
  //   meaningful here.
  @Override
  public TextureAtlasSprite getParticleTexture() {
    return baseModel.getParticleTexture();
  }


  // ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla or forge
    @Override
  public boolean isAmbientOcclusion()
  {
    return baseModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d()
  {
    return baseModel.isGui3d();
  }

  @Override
  public boolean func_230044_c_() {
    return baseModel.func_230044_c_();  // related to item "diffuselighting"
  }

  @Override
  public boolean isBuiltInRenderer()
  {
    return baseModel.isBuiltInRenderer();
  }

  @Override
  public ItemOverrideList getOverrides()
  {
    return baseModel.getOverrides();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms()
  {
    return baseModel.getItemCameraTransforms();
  }

  private static final Logger LOGGER = LogManager.getLogger();
  private static boolean loggedError = false; // prevent spamming console
}
