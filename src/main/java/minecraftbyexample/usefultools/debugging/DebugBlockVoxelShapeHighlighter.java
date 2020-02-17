package minecraftbyexample.usefultools.debugging;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.*;
import java.lang.reflect.Field;

import static minecraftbyexample.usefultools.debugging.DebugSettings.getDebugParameter;

/**
 * Created by TGG on 27/06/2019.
 */
public class DebugBlockVoxelShapeHighlighter {
  @SubscribeEvent
  public static void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
    RayTraceResult rayTraceResult = event.getTarget();
    if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) return;
    World world;

    try {
      world = getPrivateWorldFromWorldRenderer(event.getContext());
    } catch (IllegalAccessException e) {
      return;
    }

    BlockPos blockpos = ((BlockRayTraceResult) rayTraceResult).getPos();
    BlockState blockstate = world.getBlockState(blockpos);
    if (blockstate.isAir(world, blockpos) || !world.getWorldBorder().contains(blockpos)) return;

    final Color SHAPE_COLOR = Color.RED;
    final Color RENDERSHAPE_COLOR = Color.BLUE;
    final Color COLLISIONSHAPE_COLOR = Color.GREEN;
    final Color RAYTRACESHAPE_COLOR = Color.MAGENTA;

    boolean showshape = (0 != DebugSettings.getDebugParameter("showshape"));
    boolean showrendershapeshape = (0 != DebugSettings.getDebugParameter("showrendershape"));
    boolean showcollisionshape = (0 != DebugSettings.getDebugParameter("showcollisionshape"));
    boolean showraytraceshape = (0 != DebugSettings.getDebugParameter("showraytraceshape"));

    if (!(showshape || showrendershapeshape || showcollisionshape || showraytraceshape)) return;

    ActiveRenderInfo activeRenderInfo = event.getInfo();
    ISelectionContext iSelectionContext = ISelectionContext.forEntity(activeRenderInfo.getRenderViewEntity());
    if (showshape) {
      VoxelShape shape = blockstate.getShape(world, blockpos, iSelectionContext);
      drawSelectionBox(event.getContext(), blockpos, activeRenderInfo, shape, SHAPE_COLOR);
    }
    if (showrendershapeshape) {
      VoxelShape shape = blockstate.getRenderShape(world, blockpos);
      drawSelectionBox(event.getContext(), blockpos, activeRenderInfo, shape, RENDERSHAPE_COLOR);
    }
    if (showcollisionshape) {
      VoxelShape shape = blockstate.getCollisionShape(world, blockpos, iSelectionContext);
      drawSelectionBox(event.getContext(), blockpos, activeRenderInfo, shape, COLLISIONSHAPE_COLOR);
    }
    if (showraytraceshape) {
      VoxelShape shape = blockstate.getRaytraceShape(world, blockpos);
      drawSelectionBox(event.getContext(), blockpos, activeRenderInfo, shape, RAYTRACESHAPE_COLOR);
    }
    event.setCanceled(true);
  }

  private static World getPrivateWorldFromWorldRenderer(WorldRenderer worldRenderer) throws IllegalAccessException {
    if (worldField == null) {
      worldField = ObfuscationReflectionHelper.findField(WorldRenderer.class, "world");
    }
    return (World)worldField.get(worldRenderer);
  }

  private static Field worldField;

  /**
   * copied from WorldRenderer.drawSelectionBox()
   *
   * @param activeRenderInfo
   */
  public static void drawSelectionBox(WorldRenderer worldRenderer, BlockPos blockPos, ActiveRenderInfo activeRenderInfo, VoxelShape shape, Color color) {
    GlStateManager.enableBlend();
    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
    GlStateManager.disableTexture();
    GlStateManager.depthMask(false);
    GlStateManager.matrixMode(5889);
    GlStateManager.pushMatrix();
    GlStateManager.scalef(1.0F, 1.0F, 0.999F);
    double eyeX = activeRenderInfo.getProjectedView().x;
    double eyeY = activeRenderInfo.getProjectedView().y;
    double eyeZ = activeRenderInfo.getProjectedView().z;
    final float ALPHA = 0.5f;
    worldRenderer.drawShape(shape,
            blockPos.getX() - eyeX, blockPos.getY() - eyeY, blockPos.getZ() - eyeZ,
            color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, ALPHA);
    GlStateManager.popMatrix();
    GlStateManager.matrixMode(5888);
    GlStateManager.depthMask(true);
    GlStateManager.enableTexture();
    GlStateManager.disableBlend();
  }
}
