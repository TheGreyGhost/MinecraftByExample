package minecraftbyexample.mbe81_entity_projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

/**
 * Created by TGG on 24/08/2020.
 *
 * Used to render your BoomerangEntity
 */
public class BoomerangRenderer extends EntityRenderer<BoomerangEntity> {

  public static final ResourceLocation BOOMERANG_MODEL_RESOURCE_LOCATION = new ResourceLocation("minecraftbyexample:entity/mbe81b_boomerang_wrapper");

  protected BoomerangRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public ResourceLocation getEntityTexture(BoomerangEntity entity) {
    return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    // we're returning this texture because the model has been stitched into the texture sheet for block models
    // Normally entities have their own ResourceLocation (TextureAtlasSprite) which is not stitched together.
    // See vanilla for examples; eg DragonFireballRenderer
  }

  /**
   * Render the model for the boomerang.
   * @param boomerangEntity
   * @param entityYaw
   * @param partialTicks
   * @param matrixStack
   * @param renderBuffers
   * @param packedLightIn
   */
  public void render(BoomerangEntity boomerangEntity, float entityYaw, float partialTicks,
                     MatrixStack matrixStack, IRenderTypeBuffer renderBuffers, int packedLightIn) {

    IBakedModel boomerangModel = Minecraft.getInstance().getModelManager().getModel(BOOMERANG_MODEL_RESOURCE_LOCATION);

    matrixStack.push();
    MatrixStack.Entry currentMatrix = matrixStack.getLast();

    // rotate to set the pitch and yaw correctly.
    // For an entity like an arrow, the pitch is the up/down direction angle, and the yaw is the heading (eg north, east, etc)
    // The boomerang is a bit different because it is flipping end-over-end.
    // The three rotations required are (see also boomerang_rotations.png):
    // 1) The "end over end" rotation of the spinning boomerang
    // 2) The 90 degree rotation ("pitch") so that the top face of the boomerang is pointing in the correct elevation (pitch)
    //    (the model has its top face pointing directly up, but in flight it needs to point sideways)
    // 3) The "yaw" which is the direction that the boomerang is heading in.
    // 3D rotations roll/pitch/yaw are hard to get right.  The axis and correct order aren't obvious unless you're a lot smarter than I am.

    // We must also smooth out the motion by linearly interpolating between the "tick" frames using the partialTicks
    // i.e. if we are rendering at 80 frames a second, then render is called four times between ticks.
    // If the yaw changes from 50 degrees to 58 degrees during the tick, then the four yaw values are
    // first call: partialTick = 0 --> yaw = 50
    // second call : partialTick = 0.25 -> yaw is 25% of the way from 50 to 58, i.e. 52 degrees
    // third call: partialTick = 0.5 -> yaw = 54 degrees
    // fourth call: partialTick = 0.75 -> yaw = 56 degrees
    float directionOfMotion = MathHelper.lerp(partialTicks, boomerangEntity.prevRotationYaw, boomerangEntity.rotationYaw);
    float directionOfBoomerangTopFace = directionOfMotion + (boomerangEntity.isRightHandThrown() ? 90 : -90);

    matrixStack.rotate(Vector3f.YP.rotationDegrees(-1 * directionOfBoomerangTopFace));        // yaw
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(                                                   // pitch
            MathHelper.lerp(partialTicks, boomerangEntity.prevRotationPitch, boomerangEntity.rotationPitch)) );

    // rotate the boomerang end-over-end
    matrixStack.rotate(Vector3f.YP.rotationDegrees(                                       // end-over-end
            boomerangEntity.getEndOverEndRotation(partialTicks)) );

    // 3D rotations roll/pitch/yaw are hard to get right.  The axis and correct order aren't obvious.
    //  I've found it easiest to just test it interactively, using the DebugSettings method eg using the combinations below
    //  but it's still quite awkward.
    //  See the DebugSettings class for further information on how to interactively set these parameters
//    matrixStack.rotate(Vector3f.XP.rotationDegrees(DebugSettings.getDebugParameter("xp1").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.YP.rotationDegrees(DebugSettings.getDebugParameter("yp1").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.ZP.rotationDegrees(DebugSettings.getDebugParameter("zp1").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.XP.rotationDegrees(DebugSettings.getDebugParameter("xp2").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.YP.rotationDegrees(DebugSettings.getDebugParameter("yp2").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.ZP.rotationDegrees(DebugSettings.getDebugParameter("zp2").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.XP.rotationDegrees(DebugSettings.getDebugParameter("xp3").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.YP.rotationDegrees(DebugSettings.getDebugParameter("yp3").orElse(0.0).floatValue()));
//    matrixStack.rotate(Vector3f.ZP.rotationDegrees(DebugSettings.getDebugParameter("zp3").orElse(0.0).floatValue()));

    final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 14.0F;  // size of the wavefront model
    final float TARGET_SIZE_WHEN_RENDERED = 0.5F;  // desired size when rendered (in metres)

    final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
    matrixStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);

    Color blendColour = Color.WHITE;
    float red = blendColour.getRed() / 255.0F;
    float green = blendColour.getGreen() / 255.0F;
    float blue = blendColour.getBlue() / 255.0F;

    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
    //   our entity model as if it were a block model.
    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

    IVertexBuilder vertexBuffer = renderBuffers.getBuffer(RenderType.getSolid());
    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, boomerangModel,
            red, green, blue, packedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

    matrixStack.pop(); // restore the original transformation matrix + normals matrix

    super.render(boomerangEntity, entityYaw, partialTicks, matrixStack, renderBuffers, packedLightIn);  // renders labels
    // other useful examples of projectile entity rendering code are in ArrowRenderer, ItemRenderer extends EntityRenderer<ItemEntity>, and
    //   SpriteRenderer
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
