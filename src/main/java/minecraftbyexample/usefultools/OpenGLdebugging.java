package minecraftbyexample.usefultools;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

/**
 * User: The Grey Ghost
 * Date: 9/02/14
 */
public class OpenGLdebugging
{
  public class GLproperty
  {
    public GLproperty(int init_gLconstant, String init_name, String init_description, String init_category, String init_fetchCommand) {
      gLconstant = init_gLconstant;
      name = init_name;
      description = init_description;
      category = init_category;
      fetchCommand = init_fetchCommand;
    }

    public int gLconstant;
    public String name;
    public String description;
    public String category;
    public String fetchCommand;
  };

  public static OpenGLdebugging instance = new OpenGLdebugging();

  public GLproperty[] propertyList = {
      new GLproperty(GL11.GL_CURRENT_COLOR, "GL_CURRENT_COLOR", "Current color", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_INDEX, "GL_CURRENT_INDEX", "Current color index", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_TEXTURE_COORDS, "GL_CURRENT_TEXTURE_COORDS", "Current texture coordinates", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_NORMAL, "GL_CURRENT_NORMAL", "Current normal", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_RASTER_POSITION, "GL_CURRENT_RASTER_POSITION", "Current raster position", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_RASTER_DISTANCE, "GL_CURRENT_RASTER_DISTANCE", "Current raster distance", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_RASTER_COLOR, "GL_CURRENT_RASTER_COLOR", "Color associated with raster position", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_RASTER_INDEX, "GL_CURRENT_RASTER_INDEX", "Color index associated with raster position", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_RASTER_TEXTURE_COORDS, "GL_CURRENT_RASTER_TEXTURE_COORDS", "Texture coordinates associated with raster position", "current", "glGetFloatv()"),
      new GLproperty(GL11.GL_CURRENT_RASTER_POSITION_VALID, "GL_CURRENT_RASTER_POSITION_VALID", "Raster position valid bit", "current", "glGetBooleanv()"),
      new GLproperty(GL11.GL_EDGE_FLAG, "GL_EDGE_FLAG", "Edge flag", "current", "glGetBooleanv()"),
      new GLproperty(GL11.GL_VERTEX_ARRAY, "GL_VERTEX_ARRAY", "Vertex array enable", "vertex-array", "glIsEnabled()"),
      new GLproperty(GL11.GL_VERTEX_ARRAY_SIZE, "GL_VERTEX_ARRAY_SIZE", "Coordinates per vertex", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_VERTEX_ARRAY_TYPE, "GL_VERTEX_ARRAY_TYPE", "Type of vertex coordinates", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_VERTEX_ARRAY_STRIDE, "GL_VERTEX_ARRAY_STRIDE", "Stride between vertices", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_VERTEX_ARRAY_POINTER, "GL_VERTEX_ARRAY_POINTER", "Pointer to the vertex array", "vertex-array", "glGetPointerv()"),
      new GLproperty(GL11.GL_NORMAL_ARRAY, "GL_NORMAL_ARRAY", "Normal array enable", "vertex-array", "glIsEnabled()"),
      new GLproperty(GL11.GL_NORMAL_ARRAY_TYPE, "GL_NORMAL_ARRAY_TYPE", "Type of normal coordinates", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_NORMAL_ARRAY_STRIDE, "GL_NORMAL_ARRAY_STRIDE", "Stride between normals", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_NORMAL_ARRAY_POINTER, "GL_NORMAL_ARRAY_POINTER", "Pointer to the normal array", "vertex-array", "glGetPointerv()"),
      new GLproperty(GL11.GL_COLOR_ARRAY, "GL_COLOR_ARRAY", "RGBA color array enable", "vertex-array", "glIsEnabled()"),
      new GLproperty(GL11.GL_COLOR_ARRAY_SIZE, "GL_COLOR_ARRAY_SIZE", "Colors per vertex", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_COLOR_ARRAY_TYPE, "GL_COLOR_ARRAY_TYPE", "Type of color components", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_COLOR_ARRAY_STRIDE, "GL_COLOR_ARRAY_STRIDE", "Stride between colors", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_COLOR_ARRAY_POINTER, "GL_COLOR_ARRAY_POINTER", "Pointer to the color array", "vertex-array", "glGetPointerv()"),
      new GLproperty(GL11.GL_INDEX_ARRAY, "GL_INDEX_ARRAY", "Color-index array enable", "vertex-array", "glIsEnabled()"),
      new GLproperty(GL11.GL_INDEX_ARRAY_TYPE, "GL_INDEX_ARRAY_TYPE", "Type of color indices", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_INDEX_ARRAY_STRIDE, "GL_INDEX_ARRAY_STRIDE", "Stride between color indices", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_INDEX_ARRAY_POINTER, "GL_INDEX_ARRAY_POINTER", "Pointer to the index array", "vertex-array", "glGetPointerv()"),
      new GLproperty(GL11.GL_TEXTURE_COORD_ARRAY, "GL_TEXTURE_COORD_ARRAY", "Texture coordinate array enable", "vertex-array", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_COORD_ARRAY_SIZE, "GL_TEXTURE_COORD_ARRAY_SIZE", "Texture coordinates per element", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_TEXTURE_COORD_ARRAY_TYPE, "GL_TEXTURE_COORD_ARRAY_TYPE", "Type of texture coordinates", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_TEXTURE_COORD_ARRAY_STRIDE, "GL_TEXTURE_COORD_ARRAY_STRIDE", "Stride between texture coordinates", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_TEXTURE_COORD_ARRAY_POINTER, "GL_TEXTURE_COORD_ARRAY_POINTER", "Pointer to the texture coordinate array", "vertex-array", "glGetPointerv()"),
      new GLproperty(GL11.GL_EDGE_FLAG_ARRAY, "GL_EDGE_FLAG_ARRAY", "Edge flag array enable", "vertex-array", "glIsEnabled()"),
      new GLproperty(GL11.GL_EDGE_FLAG_ARRAY_STRIDE, "GL_EDGE_FLAG_ARRAY_STRIDE", "Stride between edge flags", "vertex-array", "glGetIntegerv()"),
      new GLproperty(GL11.GL_EDGE_FLAG_ARRAY_POINTER, "GL_EDGE_FLAG_ARRAY_POINTER", "Pointer to the edge flag array", "vertex-array", "glGetPointerv()"),
      new GLproperty(GL11.GL_MODELVIEW_MATRIX, "GL_MODELVIEW_MATRIX", "Modelview matrix stack", "matrix", "glGetFloatv()"),
      new GLproperty(GL11.GL_PROJECTION_MATRIX, "GL_PROJECTION_MATRIX", "Projection matrix stack", "matrix", "glGetFloatv()"),
      new GLproperty(GL11.GL_TEXTURE_MATRIX, "GL_TEXTURE_MATRIX", "Texture matrix stack", "matrix", "glGetFloatv()"),
      new GLproperty(GL11.GL_VIEWPORT, "GL_VIEWPORT", "Viewport origin and extent", "viewport", "glGetIntegerv()"),
      new GLproperty(GL11.GL_DEPTH_RANGE, "GL_DEPTH_RANGE", "Depth range near and far", "viewport", "glGetFloatv()"),
      new GLproperty(GL11.GL_MODELVIEW_STACK_DEPTH, "GL_MODELVIEW_STACK_DEPTH", "Modelview matrix stack pointer", "matrix", "glGetIntegerv()"),
      new GLproperty(GL11.GL_PROJECTION_STACK_DEPTH, "GL_PROJECTION_STACK_DEPTH", "Projection matrix stack pointer", "matrix", "glGetIntegerv()"),
      new GLproperty(GL11.GL_TEXTURE_STACK_DEPTH, "GL_TEXTURE_STACK_DEPTH", "Texture matrix stack pointer", "matrix", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MATRIX_MODE, "GL_MATRIX_MODE", "Current matrix mode", "transform", "glGetIntegerv()"),
      new GLproperty(GL11.GL_NORMALIZE, "GL_NORMALIZE", "Current normal normalization on/off", "transform/ enable", "glIsEnabled()"),
//      new GLproperty(GL11.GL_CLIP_PLANEi, "GL_CLIP_PLANEi", "User clipping plane coefficients", "transform", "glGetClipPlane()"),
//      new GLproperty(GL11.GL_CLIP_PLANEi, "GL_CLIP_PLANEi", "ith user clipping plane enabled", "transform/ enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_FOG_COLOR, "GL_FOG_COLOR", "Fog color", "fog", "glGetFloatv()"),
      new GLproperty(GL11.GL_FOG_INDEX, "GL_FOG_INDEX", "Fog index", "fog", "glGetFloatv()"),
      new GLproperty(GL11.GL_FOG_DENSITY, "GL_FOG_DENSITY", "Exponential fog density", "fog", "glGetFloatv()"),
      new GLproperty(GL11.GL_FOG_START, "GL_FOG_START", "Linear fog start", "fog", "glGetFloatv()"),
      new GLproperty(GL11.GL_FOG_END, "GL_FOG_END", "Linear fog end", "fog", "glGetFloatv()"),
      new GLproperty(GL11.GL_FOG_MODE, "GL_FOG_MODE", "Fog mode", "fog", "glGetIntegerv()"),
      new GLproperty(GL11.GL_FOG, "GL_FOG", "True if fog enabled", "fog/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_SHADE_MODEL, "GL_SHADE_MODEL", "glShadeModel() setting", "lighting", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LIGHTING, "GL_LIGHTING", "True if lighting is enabled", "lighting/e nable", "glIsEnabled()"),
      new GLproperty(GL11.GL_COLOR_MATERIAL, "GL_COLOR_MATERIAL", "True if color tracking is enabled", "lighting", "glIsEnabled()"),
      new GLproperty(GL11.GL_COLOR_MATERIAL_PARAMETER, "GL_COLOR_MATERIAL_PARAMETER", "Material properties tracking current color", "lighting", "glGetIntegerv()"),
      new GLproperty(GL11.GL_COLOR_MATERIAL_FACE, "GL_COLOR_MATERIAL_FACE", "Face(s) affected by color tracking", "lighting", "glGetIntegerv()"),
      new GLproperty(GL11.GL_AMBIENT, "GL_AMBIENT", "Ambient material color", "lighting", "glGetMaterialfv()"),
      new GLproperty(GL11.GL_DIFFUSE, "GL_DIFFUSE", "Diffuse material color", "lighting", "glGetMaterialfv()"),
      new GLproperty(GL11.GL_SPECULAR, "GL_SPECULAR", "Specular material color", "lighting", "glGetMaterialfv()"),
      new GLproperty(GL11.GL_EMISSION, "GL_EMISSION", "Emissive material color", "lighting", "glGetMaterialfv()"),
      new GLproperty(GL11.GL_SHININESS, "GL_SHININESS", "Specular exponent of material", "lighting", "glGetMaterialfv()"),
      new GLproperty(GL11.GL_LIGHT_MODEL_AMBIENT, "GL_LIGHT_MODEL_AMBIENT", "Ambient scene color", "lighting", "glGetFloatv()"),
      new GLproperty(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, "GL_LIGHT_MODEL_LOCAL_VIEWER", "Viewer is local", "lighting", "glGetBooleanv()"),
      new GLproperty(GL11.GL_LIGHT_MODEL_TWO_SIDE, "GL_LIGHT_MODEL_TWO_SIDE", "Use two-sided lighting", "lighting", "glGetBooleanv()"),
      new GLproperty(GL11.GL_AMBIENT, "GL_AMBIENT", "Ambient intensity of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_DIFFUSE, "GL_DIFFUSE", "Diffuse intensity of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_SPECULAR, "GL_SPECULAR", "Specular intensity of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_POSITION, "GL_POSITION", "Position of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_CONSTANT_ATTENUATION, "GL_CONSTANT_ATTENUATION", "Constant attenuation factor", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_LINEAR_ATTENUATION, "GL_LINEAR_ATTENUATION", "Linear attenuation factor", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_QUADRATIC_ATTENUATION, "GL_QUADRATIC_ATTENUATION", "Quadratic attenuation factor", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_SPOT_DIRECTION, "GL_SPOT_DIRECTION", "Spotlight direction of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_SPOT_EXPONENT, "GL_SPOT_EXPONENT", "Spotlight exponent of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_SPOT_CUTOFF, "GL_SPOT_CUTOFF", "Spotlight angle of light i", "lighting", "glGetLightfv()"),
      new GLproperty(GL11.GL_LIGHT0, "GL_LIGHT0", "True if light 0 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT1, "GL_LIGHT1", "True if light 1 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT2, "GL_LIGHT2", "True if light 2 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT3, "GL_LIGHT3", "True if light 3 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT4, "GL_LIGHT4", "True if light 4 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT5, "GL_LIGHT5", "True if light 5 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT6, "GL_LIGHT6", "True if light 6 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LIGHT7, "GL_LIGHT7", "True if light 7 enabled", "lighting/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_COLOR_INDEXES, "GL_COLOR_INDEXES", "ca, cd, and cs for color-index lighting", "lighting/e nable", "glGetMaterialfv()"),
      new GLproperty(GL11.GL_POINT_SIZE, "GL_POINT_SIZE", "Point size", "point", "glGetFloatv()"),
      new GLproperty(GL11.GL_POINT_SMOOTH, "GL_POINT_SMOOTH", "Point antialiasing on", "point/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LINE_WIDTH, "GL_LINE_WIDTH", "Line width", "line", "glGetFloatv()"),
      new GLproperty(GL11.GL_LINE_SMOOTH, "GL_LINE_SMOOTH", "Line antialiasing on", "line/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LINE_STIPPLE_PATTERN, "GL_LINE_STIPPLE_PATTERN", "Line stipple", "line", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LINE_STIPPLE_REPEAT, "GL_LINE_STIPPLE_REPEAT", "Line stipple repeat", "line", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LINE_STIPPLE, "GL_LINE_STIPPLE", "Line stipple enable", "line/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_CULL_FACE, "GL_CULL_FACE", "Polygon culling enabled", "polygon/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_CULL_FACE_MODE, "GL_CULL_FACE_MODE", "Cull front-/back-facing polygons", "polygon", "glGetIntegerv()"),
      new GLproperty(GL11.GL_FRONT_FACE, "GL_FRONT_FACE", "Polygon front-face CW/CCW indicator", "polygon", "glGetIntegerv()"),
      new GLproperty(GL11.GL_POLYGON_SMOOTH, "GL_POLYGON_SMOOTH", "Polygon antialiasing on", "polygon/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_POLYGON_MODE, "GL_POLYGON_MODE", "Polygon rasterization mode (front and back)", "polygon", "glGetIntegerv()"),
      new GLproperty(GL11.GL_POLYGON_OFFSET_FACTOR, "GL_POLYGON_OFFSET_FACTOR", "Polygon offset factor", "polygon", "glGetFloatv()"),
//      new GLproperty(GL11.GL_POLYGON_OFFSET_BIAS, "GL_POLYGON_OFFSET_BIAS", "Polygon offset bias", "polygon", "glGetFloatv()"),
      new GLproperty(GL11.GL_POLYGON_OFFSET_POINT, "GL_POLYGON_OFFSET_POINT", "Polygon offset enable for GL_POINT mode rasterization", "polygon/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_POLYGON_OFFSET_LINE, "GL_POLYGON_OFFSET_LINE", "Polygon offset enable for GL_LINE mode rasterization", "polygon/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_POLYGON_OFFSET_FILL, "GL_POLYGON_OFFSET_FILL", "Polygon offset enable for GL_FILL mode rasterization", "polygon/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_POLYGON_STIPPLE, "GL_POLYGON_STIPPLE", "Polygon stipple enable", "polygon/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_1D, "GL_TEXTURE_1D", "True if 1-D texturing enabled ", "texture/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_2D, "GL_TEXTURE_2D", "True if 2-D texturing enabled ", "texture/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_BINDING_1D, "GL_TEXTURE_BINDING_1D", "Texture object bound to GL_TEXTURE_1D", "texture", "glGetIntegerv()"),
      new GLproperty(GL11.GL_TEXTURE_BINDING_2D, "GL_TEXTURE_BINDING_2D", "Texture object bound to GL_TEXTURE_2D", "texture", "glGetIntegerv()"),
      new GLproperty(GL11.GL_TEXTURE, "GL_TEXTURE", "x-D texture image at level of detail i", "UNUSED", "glGetTexImage()"),
      new GLproperty(GL11.GL_TEXTURE_WIDTH, "GL_TEXTURE_WIDTH", "x-D texture image i's width", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_HEIGHT, "GL_TEXTURE_HEIGHT", "x-D texture image i's height", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_BORDER, "GL_TEXTURE_BORDER", "x-D texture image i's border width", "UNUSED", "glGetTexLevelParameter*()"),
//      new GLproperty(GL11.GL_TEXTURE_INTERNAL, "GL_TEXTURE_INTERNAL", "x-D texture image i's internal image format", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_RED_SIZE, "GL_TEXTURE_RED_SIZE", "x-D texture image i's red resolution", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_GREEN_SIZE, "GL_TEXTURE_GREEN_SIZE", "x-D texture image i's green resolution", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_BLUE_SIZE, "GL_TEXTURE_BLUE_SIZE", "x-D texture image i's blue resolution", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_ALPHA_SIZE, "GL_TEXTURE_ALPHA_SIZE", "x-D texture image i's alpha resolution", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_LUMINANCE_SIZE, "GL_TEXTURE_LUMINANCE_SIZE", "x-D texture image i's luminance resolution", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_INTENSITY_SIZE, "GL_TEXTURE_INTENSITY_SIZE", "x-D texture image i's intensity resolution", "UNUSED", "glGetTexLevelParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_BORDER_COLOR, "GL_TEXTURE_BORDER_COLOR", "Texture border color", "texture", "glGetTexParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_MIN_FILTER, "GL_TEXTURE_MIN_FILTER", "Texture minification function", "texture", "glGetTexParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_MAG_FILTER, "GL_TEXTURE_MAG_FILTER", "Texture magnification function", "texture", "glGetTexParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_WRAP_S, "GL_TEXTURE_WRAP_S", "Texture wrap mode (x is S or T)", "texture", "glGetTexParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_WRAP_T, "GL_TEXTURE_WRAP_T", "Texture wrap mode (x is S or T)", "texture", "glGetTexParameter*()"),
      new GLproperty(GL11.GL_TEXTURE_PRIORITY, "GL_TEXTURE_PRIORITY", "Texture object priority", "texture", "glGetTexParameter*()"),
//      new GLproperty(GL11.GL_TEXTURE_RESIDENCY, "GL_TEXTURE_RESIDENCY", "Texture residency", "texture", "glGetTexParameteriv()"),
      new GLproperty(GL11.GL_TEXTURE_ENV_MODE, "GL_TEXTURE_ENV_MODE", "Texture application function", "texture", "glGetTexEnviv()"),
      new GLproperty(GL11.GL_TEXTURE_ENV_COLOR, "GL_TEXTURE_ENV_COLOR", "Texture environment color", "texture", "glGetTexEnvfv()"),
      new GLproperty(GL11.GL_TEXTURE_GEN_S, "GL_TEXTURE_GEN_S", "Texgen enabled (x is S, T, R, or Q)", "texture/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_GEN_T, "GL_TEXTURE_GEN_T", "Texgen enabled (x is S, T, R, or Q)", "texture/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_GEN_R, "GL_TEXTURE_GEN_R", "Texgen enabled (x is S, T, R, or Q)", "texture/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_TEXTURE_GEN_Q, "GL_TEXTURE_GEN_Q", "Texgen enabled (x is S, T, R, or Q)", "texture/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_EYE_PLANE, "GL_EYE_PLANE", "Texgen plane equation coefficients", "texture", "glGetTexGenfv()"),
      new GLproperty(GL11.GL_OBJECT_PLANE, "GL_OBJECT_PLANE", "Texgen object linear coefficients", "texture", "glGetTexGenfv()"),
      new GLproperty(GL11.GL_TEXTURE_GEN_MODE, "GL_TEXTURE_GEN_MODE", "Function used for texgen", "texture", "glGetTexGeniv()"),
      new GLproperty(GL11.GL_SCISSOR_TEST, "GL_SCISSOR_TEST", "Scissoring enabled", "scissor/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_SCISSOR_BOX, "GL_SCISSOR_BOX", "Scissor box", "scissor", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ALPHA_TEST, "GL_ALPHA_TEST", "Alpha test enabled", "color-buffer/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_ALPHA_TEST_FUNC, "GL_ALPHA_TEST_FUNC", "Alpha test function", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ALPHA_TEST_REF, "GL_ALPHA_TEST_REF", "Alpha test reference value", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_TEST, "GL_STENCIL_TEST", "Stenciling enabled", "stencil-buffer/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_STENCIL_FUNC, "GL_STENCIL_FUNC", "Stencil function", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_VALUE_MASK, "GL_STENCIL_VALUE_MASK", "Stencil mask", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_REF, "GL_STENCIL_REF", "Stencil reference value", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_FAIL, "GL_STENCIL_FAIL", "Stencil fail action", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_PASS_DEPTH_FAIL, "GL_STENCIL_PASS_DEPTH_FAIL", "Stencil depth buffer fail action", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_PASS_DEPTH_PASS, "GL_STENCIL_PASS_DEPTH_PASS", "Stencil depth buffer pass action", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_DEPTH_TEST, "GL_DEPTH_TEST", "Depth buffer enabled", "depth-buffer/ena ble", "glIsEnabled()"),
      new GLproperty(GL11.GL_DEPTH_FUNC, "GL_DEPTH_FUNC", "Depth buffer test function", "depth-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_BLEND, "GL_BLEND", "Blending enabled", "color-buffer/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_BLEND_SRC, "GL_BLEND_SRC", "Blending source function", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_BLEND_DST, "GL_BLEND_DST", "Blending destination function", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_DITHER, "GL_DITHER", "Dithering enabled", "color-buffer/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_INDEX_LOGIC_OP, "GL_INDEX_LOGIC_OP", "Color index logical operation enabled", "color-buffer/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_COLOR_LOGIC_OP, "GL_COLOR_LOGIC_OP", "RGBA color logical operation enabled", "color-buffer/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_LOGIC_OP_MODE, "GL_LOGIC_OP_MODE", "Logical operation function", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_DRAW_BUFFER, "GL_DRAW_BUFFER", "Buffers selected for drawing", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_INDEX_WRITEMASK, "GL_INDEX_WRITEMASK", "Color-index writemask", "color-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_COLOR_WRITEMASK, "GL_COLOR_WRITEMASK", "Color write enables; R, G, B, or A", "color-buffer", "glGetBooleanv()"),
      new GLproperty(GL11.GL_DEPTH_WRITEMASK, "GL_DEPTH_WRITEMASK", "Depth buffer enabled for writing", "depth-buffer", "glGetBooleanv()"),
      new GLproperty(GL11.GL_STENCIL_WRITEMASK, "GL_STENCIL_WRITEMASK", "Stencil-buffer writemask", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_COLOR_CLEAR_VALUE, "GL_COLOR_CLEAR_VALUE", "Color-buffer clear value (RGBA mode)", "color-buffer", "glGetFloatv()"),
      new GLproperty(GL11.GL_INDEX_CLEAR_VALUE, "GL_INDEX_CLEAR_VALUE", "Color-buffer clear value (color-index mode)", "color-buffer", "glGetFloatv()"),
      new GLproperty(GL11.GL_DEPTH_CLEAR_VALUE, "GL_DEPTH_CLEAR_VALUE", "Depth-buffer clear value", "depth-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_CLEAR_VALUE, "GL_STENCIL_CLEAR_VALUE", "Stencil-buffer clear value", "stencil-buffer", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ACCUM_CLEAR_VALUE, "GL_ACCUM_CLEAR_VALUE", "Accumulation-buffer clear value", "accum-buffer", "glGetFloatv()"),
      new GLproperty(GL11.GL_UNPACK_SWAP_BYTES, "GL_UNPACK_SWAP_BYTES", "Value of GL_UNPACK_SWAP_BYTES", "pixel-store", "glGetBooleanv()"),
      new GLproperty(GL11.GL_UNPACK_LSB_FIRST, "GL_UNPACK_LSB_FIRST", "Value of GL_UNPACK_LSB_FIRST", "pixel-store", "glGetBooleanv()"),
      new GLproperty(GL11.GL_UNPACK_ROW_LENGTH, "GL_UNPACK_ROW_LENGTH", "Value of GL_UNPACK_ROW_LENGTH", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_UNPACK_SKIP_ROWS, "GL_UNPACK_SKIP_ROWS", "Value of GL_UNPACK_SKIP_ROWS", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_UNPACK_SKIP_PIXELS, "GL_UNPACK_SKIP_PIXELS", "Value of GL_UNPACK_SKIP_PIXELS", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_UNPACK_ALIGNMENT, "GL_UNPACK_ALIGNMENT", "Value of GL_UNPACK_ALIGNMENT", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_PACK_SWAP_BYTES, "GL_PACK_SWAP_BYTES", "Value of GL_PACK_SWAP_BYTES", "pixel-store", "glGetBooleanv()"),
      new GLproperty(GL11.GL_PACK_LSB_FIRST, "GL_PACK_LSB_FIRST", "Value of GL_PACK_LSB_FIRST", "pixel-store", "glGetBooleanv()"),
      new GLproperty(GL11.GL_PACK_ROW_LENGTH, "GL_PACK_ROW_LENGTH", "Value of GL_PACK_ROW_LENGTH", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_PACK_SKIP_ROWS, "GL_PACK_SKIP_ROWS", "Value of GL_PACK_SKIP_ROWS", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_PACK_SKIP_PIXELS, "GL_PACK_SKIP_PIXELS", "Value of GL_PACK_SKIP_PIXELS", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_PACK_ALIGNMENT, "GL_PACK_ALIGNMENT", "Value of GL_PACK_ALIGNMENT", "pixel-store", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAP_COLOR, "GL_MAP_COLOR", "True if colors are mapped", "pixel", "glGetBooleanv()"),
      new GLproperty(GL11.GL_MAP_STENCIL, "GL_MAP_STENCIL", "True if stencil values are mapped", "pixel", "glGetBooleanv()"),
      new GLproperty(GL11.GL_INDEX_SHIFT, "GL_INDEX_SHIFT", "Value of GL_INDEX_SHIFT", "pixel", "glGetIntegerv()"),
      new GLproperty(GL11.GL_INDEX_OFFSET, "GL_INDEX_OFFSET", "Value of GL_INDEX_OFFSET", "pixel", "glGetIntegerv()"),
//      new GLproperty(GL11.GL_RED_SCALE, "GL_x_SCALE", "Value of GL_x_SCALE; x is GL_RED, GL_GREEN, GL_BLUE, GL_ALPHA, or GL_DEPTH", "pixel", "glGetFloatv()"),
//      new GLproperty(GL11.GL_x_BIAS, "GL_x_BIAS", "Value of GL_x_BIAS; x is one of GL_RED, GL_GREEN, GL_BLUE, GL_ALPHA, or GL_DEPTH", "pixel", "glGetFloatv()"),
      new GLproperty(GL11.GL_ZOOM_X, "GL_ZOOM_X", "x zoom factor", "pixel", "glGetFloatv()"),
      new GLproperty(GL11.GL_ZOOM_Y, "GL_ZOOM_Y", "y zoom factor", "pixel", "glGetFloatv()"),
//      new GLproperty(GL11.GL_x, "GL_x", "glPixelMap() translation tables; x is a map name from Table 8-1", "UNUSED", "glGetPixelMap*()"),
//      new GLproperty(GL11.GL_x_SIZE, "GL_x_SIZE", "Size of table x", "UNUSED", "glGetIntegerv()"),
      new GLproperty(GL11.GL_READ_BUFFER, "GL_READ_BUFFER", "Read source buffer", "pixel", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ORDER, "GL_ORDER", "1D map order", "capability", "glGetMapiv()"),
      new GLproperty(GL11.GL_ORDER, "GL_ORDER", "2D map orders", "capability", "glGetMapiv()"),
      new GLproperty(GL11.GL_COEFF, "GL_COEFF", "1D control points", "capability", "glGetMapfv()"),
      new GLproperty(GL11.GL_COEFF, "GL_COEFF", "2D control points", "capability", "glGetMapfv()"),
      new GLproperty(GL11.GL_DOMAIN, "GL_DOMAIN", "1D domain endpoints", "capability", "glGetMapfv()"),
      new GLproperty(GL11.GL_DOMAIN, "GL_DOMAIN", "2D domain endpoints", "capability", "glGetMapfv()"),
//      new GLproperty(GL11.GL_MAP1_x, "GL_MAP1_x", "1D map enables: x is map type", "eval/enable", "glIsEnabled()"),
//      new GLproperty(GL11.GL_MAP2_x, "GL_MAP2_x", "2D map enables: x is map type", "eval/enable", "glIsEnabled()"),
      new GLproperty(GL11.GL_MAP1_GRID_DOMAIN, "GL_MAP1_GRID_DOMAIN", "1D grid endpoints", "eval", "glGetFloatv()"),
      new GLproperty(GL11.GL_MAP2_GRID_DOMAIN, "GL_MAP2_GRID_DOMAIN", "2D grid endpoints", "eval", "glGetFloatv()"),
      new GLproperty(GL11.GL_MAP1_GRID_SEGMENTS, "GL_MAP1_GRID_SEGMENTS", "1D grid divisions", "eval", "glGetFloatv()"),
      new GLproperty(GL11.GL_MAP2_GRID_SEGMENTS, "GL_MAP2_GRID_SEGMENTS", "2D grid divisions", "eval", "glGetFloatv()"),
      new GLproperty(GL11.GL_AUTO_NORMAL, "GL_AUTO_NORMAL", "True if automatic normal generation enabled", "eval", "glIsEnabled()"),
      new GLproperty(GL11.GL_PERSPECTIVE_CORRECTION_HINT, "GL_PERSPECTIVE_CORRECTION_HINT", "Perspective correction hint", "hint", "glGetIntegerv()"),
      new GLproperty(GL11.GL_POINT_SMOOTH_HINT, "GL_POINT_SMOOTH_HINT", "Point smooth hint", "hint", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LINE_SMOOTH_HINT, "GL_LINE_SMOOTH_HINT", "Line smooth hint", "hint", "glGetIntegerv()"),
      new GLproperty(GL11.GL_POLYGON_SMOOTH_HINT, "GL_POLYGON_SMOOTH_HINT", "Polygon smooth hint", "hint", "glGetIntegerv()"),
      new GLproperty(GL11.GL_FOG_HINT, "GL_FOG_HINT", "Fog hint", "hint", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_LIGHTS, "GL_MAX_LIGHTS", "Maximum number of lights", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_CLIP_PLANES, "GL_MAX_CLIP_PLANES", "Maximum number of user clipping planes", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_MODELVIEW_STACK_DEPTH, "GL_MAX_MODELVIEW_STACK_DEPTH", "Maximum modelview-matrix stack depth", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_PROJECTION_STACK_DEPTH, "GL_MAX_PROJECTION_STACK_DEPTH", "Maximum projection-matrix stack depth", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_TEXTURE_STACK_DEPTH, "GL_MAX_TEXTURE_STACK_DEPTH", "Maximum depth of texture matrix stack", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_SUBPIXEL_BITS, "GL_SUBPIXEL_BITS", "Number of bits of subpixel precision in x and y", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_TEXTURE_SIZE, "GL_MAX_TEXTURE_SIZE", "See discussion in Texture Proxy in Chapter 9", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_PIXEL_MAP_TABLE, "GL_MAX_PIXEL_MAP_TABLE", "Maximum size of a glPixelMap() translation table", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_NAME_STACK_DEPTH, "GL_MAX_NAME_STACK_DEPTH", "Maximum selection-name stack depth", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_LIST_NESTING, "GL_MAX_LIST_NESTING", "Maximum display-list call nesting", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_EVAL_ORDER, "GL_MAX_EVAL_ORDER", "Maximum evaluator polynomial order", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_VIEWPORT_DIMS, "GL_MAX_VIEWPORT_DIMS", "Maximum viewport dimensions", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_ATTRIB_STACK_DEPTH, "GL_MAX_ATTRIB_STACK_DEPTH", "Maximum depth of the attribute stack", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_MAX_CLIENT_ATTRIB_STACK_DEPTH, "GL_MAX_CLIENT_ATTRIB_STACK_DEPTH", "Maximum depth of the client attribute stack", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_AUX_BUFFERS, "GL_AUX_BUFFERS", "Number of auxiliary buffers", "capability", "glGetBooleanv()"),
      new GLproperty(GL11.GL_RGBA_MODE, "GL_RGBA_MODE", "True if color buffers store RGBA", "capability", "glGetBooleanv()"),
      new GLproperty(GL11.GL_INDEX_MODE, "GL_INDEX_MODE", "True if color buffers store indices", "capability", "glGetBooleanv()"),
      new GLproperty(GL11.GL_DOUBLEBUFFER, "GL_DOUBLEBUFFER", "True if front and back buffers exist", "capability", "glGetBooleanv()"),
      new GLproperty(GL11.GL_STEREO, "GL_STEREO", "True if left and right buffers exist", "capability", "glGetBooleanv()"),
      new GLproperty(GL11.GL_POINT_SIZE_RANGE, "GL_POINT_SIZE_RANGE", "Range (low to high) of antialiased point sizes", "capability", "glGetFloatv()"),
      new GLproperty(GL11.GL_POINT_SIZE_GRANULARITY, "GL_POINT_SIZE_GRANULARITY", "Antialiased point-size granularity", "capability", "glGetFloatv()"),
      new GLproperty(GL11.GL_LINE_WIDTH_RANGE, "GL_LINE_WIDTH_RANGE", "Range (low to high) of antialiased line widths", "capability", "glGetFloatv()"),
      new GLproperty(GL11.GL_LINE_WIDTH_GRANULARITY, "GL_LINE_WIDTH_GRANULARITY", "Antialiased line-width granularity", "capability", "glGetFloatv()"),
      new GLproperty(GL11.GL_RED_BITS, "GL_RED_BITS", "Number of bits per red component in color buffers", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_GREEN_BITS, "GL_GREEN_BITS", "Number of bits per green component in color buffers", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_BLUE_BITS, "GL_BLUE_BITS", "Number of bits per blue component in color buffers", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ALPHA_BITS, "GL_ALPHA_BITS", "Number of bits per alpha component in color buffers", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_INDEX_BITS, "GL_INDEX_BITS", "Number of bits per index in color buffers", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_DEPTH_BITS, "GL_DEPTH_BITS", "Number of depth-buffer bitplanes", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_STENCIL_BITS, "GL_STENCIL_BITS", "Number of stencil bitplanes", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ACCUM_RED_BITS, "GL_ACCUM_RED_BITS", "Number of bits per red component in the accumulation buffer", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ACCUM_GREEN_BITS, "GL_ACCUM_GREEN_BITS", "Number of bits per green component in the accumulation buffer", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ACCUM_BLUE_BITS, "GL_ACCUM_BLUE_BITS", "Number of bits per blue component in the accumulation buffer", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ACCUM_ALPHA_BITS, "GL_ACCUM_ALPHA_BITS", "Number of bits per alpha component in the accumulation buffer", "capability", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LIST_BASE, "GL_LIST_BASE", "Setting of glListBase()", "list", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LIST_INDEX, "GL_LIST_INDEX", "Number of display list under construction; 0 if none", "current", "glGetIntegerv()"),
      new GLproperty(GL11.GL_LIST_MODE, "GL_LIST_MODE", "Mode of display list under construction; undefined if none", "current", "glGetIntegerv()"),
      new GLproperty(GL11.GL_ATTRIB_STACK_DEPTH, "GL_ATTRIB_STACK_DEPTH", "Attribute stack pointer", "current", "glGetIntegerv()"),
      new GLproperty(GL11.GL_CLIENT_ATTRIB_STACK_DEPTH, "GL_CLIENT_ATTRIB_STACK_DEPTH", "Client attribute stack pointer", "current", "glGetIntegerv()"),
      new GLproperty(GL11.GL_NAME_STACK_DEPTH, "GL_NAME_STACK_DEPTH", "Name stack depth", "current", "glGetIntegerv()"),
      new GLproperty(GL11.GL_RENDER_MODE, "GL_RENDER_MODE", "glRenderMode() setting", "current", "glGetIntegerv()"),
      new GLproperty(GL11.GL_SELECTION_BUFFER_POINTER, "GL_SELECTION_BUFFER_POINTER", "Pointer to selection buffer", "select", "glGetPointerv()"),
      new GLproperty(GL11.GL_SELECTION_BUFFER_SIZE, "GL_SELECTION_BUFFER_SIZE", "Size of selection buffer", "select", "glGetIntegerv()"),
      new GLproperty(GL11.GL_FEEDBACK_BUFFER_POINTER, "GL_FEEDBACK_BUFFER_POINTER", "Pointer to feedback buffer", "feedback", "glGetPointerv()"),
      new GLproperty(GL11.GL_FEEDBACK_BUFFER_SIZE, "GL_FEEDBACK_BUFFER_SIZE", "Size of feedback buffer", "feedback", "glGetIntegerv()"),
      new GLproperty(GL11.GL_FEEDBACK_BUFFER_TYPE, "GL_FEEDBACK_BUFFER_TYPE", "Type of feedback buffer", "feedback", "glGetIntegerv()"),
  };



  public static void dumpOpenGLstate()
  {
  }

  public static void dumpAllIsEnabled()
  {
    for (int i = 0; i < instance.propertyList.length; ++i) {
      if (instance.propertyList[i].fetchCommand.equals("glIsEnabled()")) {
        System.out.print(instance.propertyList[i].name + ":");
        System.out.print(GL11.glIsEnabled(instance.propertyList[i].gLconstant));
        System.out.println(" (" + instance.propertyList[i].description + ")");
      }
    }
  }

  public static void dumpAllType(String type) {
    for (int i = 0; i < instance.propertyList.length; ++i) {
      if (instance.propertyList[i].category.equals(type)) {
        System.out.print(instance.propertyList[i].name + ":");
        System.out.println(getPropertyAsString(i));
        System.out.println(" (" + instance.propertyList[i].description + ")");
      }
    }
  }

  private static String getPropertyAsString(int propertyListIndex)
  {
    int gLconstant = instance.propertyList[propertyListIndex].gLconstant;
    if (instance.propertyList[propertyListIndex].fetchCommand.equals("glIsEnabled()")) {
      return "" + GL11.glIsEnabled(gLconstant);
    }

    if (instance.propertyList[propertyListIndex].fetchCommand.equals("glGetBooleanv()")) {
      ByteBuffer params = BufferUtils.createByteBuffer(16);

      GL11.glGetBoolean(gLconstant, params);
      String out = "";
      for (int i = 0; i < params.capacity(); ++i) {
        out += (i == 0 ? "" : ", ") + params.get(i);
      }
      return out;
    }

    return "";
//    switch(instance.propertyList[propertyListIndex].fetchCommand) {
/*
      case "glGetBooleanv()":  { System.out.println(GL11.glGetBooleanv()); break;}
      case "glGetFloatv()":  { System.out.println(GL11.glGetFloatv()); break;}
      case "glGetIntegerv()":  { System.out.println(GL11.glGetIntegerv()); break;}
      case "glGetLightfv()":  { System.out.println(GL11.glGetLightfv()); break;}
      case "glGetMapfv()":  { System.out.println(GL11.glGetMapfv()); break;}
      case "glGetMapiv()":  { System.out.println(GL11.glGetMapiv()); break;}
      case "glGetMaterialfv()":  { System.out.println(GL11.glGetMaterialfv()); break;}
      case "glGetPixelMap*()":  { System.out.println(GL11.glGetPixelMap*()); break;}
      case "glGetPointerv()":  { System.out.println(GL11.glGetPointerv()); break;}
      case "glGetTexEnvfv()":  { System.out.println(GL11.glGetTexEnvfv()); break;}
      case "glGetTexEnviv()":  { System.out.println(GL11.glGetTexEnviv()); break;}
      case "glGetTexGenfv()":  { System.out.println(GL11.glGetTexGenfv()); break;}
      case "glGetTexGeniv()":  { System.out.println(GL11.glGetTexGeniv()); break;}
      case "glGetTexImage()":  { System.out.println(GL11.glGetTexImage()); break;}
      case "glGetTexLevelParameter*()":  { System.out.println(GL11.glGetTexLevelParameter*()); break;}
      case "glGetTexParameter*()":  { System.out.println(GL11.glGetTexParameter*()); break;}
      case "glGetTexParameteriv()":  { System.out.println(GL11.glGetTexParameteriv()); break;}
*/
  }

}
