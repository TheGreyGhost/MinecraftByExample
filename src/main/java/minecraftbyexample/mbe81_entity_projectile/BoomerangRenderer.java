package minecraftbyexample.mbe81_entity_projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by TGG on 24/08/2020.
 */
public class BoomerangRenderer extends EntityRenderer<BoomerangEntity> {

  protected BoomerangRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public ResourceLocation getEntityTexture(BoomerangEntity entity) {
    return null;
  }

  public void render(BoomerangEntity boomerangEntity, float entityYaw, float partialTicks,
                     MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

  }
}
