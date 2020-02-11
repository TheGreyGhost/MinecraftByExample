package minecraftbyexample.usefultools.debugging;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

import static minecraftbyexample.usefultools.debugging.DebugSettings.getDebugParameter;

/**
 * Created by TGG on 27/06/2019.
 *
 */
public class DebugBlockVoxelShapeHighlighter {
  @SubscribeEvent
  public static void checkForSpawnDenial(DrawBlockHighlightEvent event) {
    RayTraceResult rayTraceResult = event.getTarget();
    if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) return;
    World world;

    try {
      world = getPrivateWorldFromWorldRenderer(event.getContext());
    } catch (IllegalAccessException e) {
      return;
    }

    BlockPos blockpos = ((BlockRayTraceResult)rayTraceResult).getPos();
    BlockState blockstate = world.getBlockState(blockpos);
    if (blockstate.isAir(world, blockpos) || !world.getWorldBorder().contains(blockpos)) return;

    boolean showshape = (0!= DebugSettings.getDebugParameter("showshape"));
    boolean showrendershapeshape = (0!= DebugSettings.getDebugParameter("showrendershape"));
    boolean showcollisionshape = (0!= DebugSettings.getDebugParameter("showcollisionshape"));
    boolean showraytraceshape = (0!= DebugSettings.getDebugParameter("showraytraceshape"));

    if (! (showshape || showrendershapeshape || showcollisionshape || showraytraceshape)) return;
    event.setCanceled(true);


  }

  private static World getPrivateWorldFromWorldRenderer(WorldRenderer worldRenderer) throws IllegalAccessException{
    if (worldField == null) {
      worldField = ObfuscationReflectionHelper.findField(WorldRenderer.class, "world");
    }
    return (World)worldField.get(worldRenderer);
  }

  private static Field worldField;

  public void drawSelectionBox(ActiveRenderInfo activeRenderInfo, RayTraceResult rayTraceResult) {
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.lineWidth(Math.max(2.5F, (float)this.mc.mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
        GlStateManager.disableTexture();
        GlStateManager.depthMask(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.0F, 1.0F, 0.999F);
        double d0 = activeRenderInfo.getProjectedView().x;
        double d1 = activeRenderInfo.getProjectedView().y;
        double d2 = activeRenderInfo.getProjectedView().z;
        drawShape(blockstate.getShape(this.world, blockpos, ISelectionContext.forEntity(activeRenderInfo.getRenderViewEntity())), (double)blockpos.getX() - d0, (double)blockpos.getY() - d1, (double)blockpos.getZ() - d2, 0.0F, 0.0F, 0.0F, 0.4F);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
      }
    }

  }




}
