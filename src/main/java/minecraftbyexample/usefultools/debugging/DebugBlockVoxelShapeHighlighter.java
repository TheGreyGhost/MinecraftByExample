package minecraftbyexample.usefultools.debugging;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * Created by TGG on 27/06/2019.
 */
public class DebugBlockVoxelShapeHighlighter {
  @SubscribeEvent
  public static void onDrawBlockHighlightEvent(DrawHighlightEvent.HighlightBlock event) {
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

    boolean showshape = DebugSettings.getDebugParameter("showshape").isPresent();
    boolean showrendershapeshape = DebugSettings.getDebugParameter("showrendershape").isPresent();
    boolean showcollisionshape = DebugSettings.getDebugParameter("showcollisionshape").isPresent();
    boolean showraytraceshape = DebugSettings.getDebugParameter("showraytraceshape").isPresent();

    if (!(showshape || showrendershapeshape || showcollisionshape || showraytraceshape)) return;

    ActiveRenderInfo activeRenderInfo = event.getInfo();
    ISelectionContext iSelectionContext = ISelectionContext.forEntity(activeRenderInfo.getRenderViewEntity());
    IRenderTypeBuffer renderTypeBuffers = event.getBuffers();
    MatrixStack matrixStack = event.getMatrix();
    if (showshape) {
      VoxelShape shape = blockstate.getShape(world, blockpos, iSelectionContext);
      drawSelectionBox(event.getContext(), renderTypeBuffers, matrixStack, blockpos, activeRenderInfo, shape, SHAPE_COLOR);
    }
    if (showrendershapeshape) {
      VoxelShape shape = blockstate.getRenderShape(world, blockpos);
      drawSelectionBox(event.getContext(), renderTypeBuffers, matrixStack, blockpos, activeRenderInfo, shape, RENDERSHAPE_COLOR);
    }
    if (showcollisionshape) {
      VoxelShape shape = blockstate.getCollisionShape(world, blockpos, iSelectionContext);
      drawSelectionBox(event.getContext(), renderTypeBuffers, matrixStack, blockpos, activeRenderInfo, shape, COLLISIONSHAPE_COLOR);
    }
    if (showraytraceshape) {
      VoxelShape shape = blockstate.getRaytraceShape(world, blockpos);
      drawSelectionBox(event.getContext(), renderTypeBuffers, matrixStack, blockpos, activeRenderInfo, shape, RAYTRACESHAPE_COLOR);
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
   * copied from WorldRenderer; starting from the code marked with iprofiler.endStartSection("outline");
   *
   * @param activeRenderInfo
   */
  private static void drawSelectionBox(WorldRenderer worldRenderer, IRenderTypeBuffer renderTypeBuffers, MatrixStack matrixStack,
                                      BlockPos blockPos, ActiveRenderInfo activeRenderInfo, VoxelShape shape, Color color) {
    RenderType renderType = RenderType.getLines();
    IVertexBuilder vertexBuilder = renderTypeBuffers.getBuffer(renderType);

    double eyeX = activeRenderInfo.getProjectedView().getX();
    double eyeY = activeRenderInfo.getProjectedView().getY();
    double eyeZ = activeRenderInfo.getProjectedView().getZ();
    final float ALPHA = 0.5f;
    drawShapeOutline(matrixStack, vertexBuilder, shape,
            blockPos.getX() - eyeX, blockPos.getY() - eyeY, blockPos.getZ() - eyeZ,
            color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, ALPHA);

  }

  private static void drawShapeOutline(MatrixStack matrixStack,
                                       IVertexBuilder vertexBuilder,
                                       VoxelShape voxelShape,
                                       double originX, double originY, double originZ,
                                       float red, float green, float blue, float alpha) {

    Matrix4f matrix4f = matrixStack.getLast().getMatrix();
    voxelShape.forEachEdge((x0, y0, z0, x1, y1, z1) -> {
      vertexBuilder.pos(matrix4f, (float)(x0 + originX), (float)(y0 + originY), (float)(z0 + originZ)).color(red, green, blue, alpha).endVertex();
      vertexBuilder.pos(matrix4f, (float)(x1 + originX), (float)(y1 + originY), (float)(z1 + originZ)).color(red, green, blue, alpha).endVertex();
    });
  }

}
