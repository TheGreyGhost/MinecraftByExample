//package minecraftbyexample.usefultools.debugging;
//
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.math.Vec3d;
//import net.minecraftforge.client.event.DrawBlockHighlightEvent;
//import net.minecraftforge.fml.common.eventhandler.EventPriority;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import org.apache.commons.lang3.tuple.ImmutablePair;
//import org.apache.commons.lang3.tuple.Pair;
//
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * For debugging purposes:
// * renders a 3D crosshair at a given location (currently used for throat location and BreathFX rendering)
// * Usage:
// * (1) Register the handler during PostInit client only
// * (2) Each frame, add all desired centrepoints with addCentrepointToRenderWorld()
// * Created by TGG on 29/06/2019
// */
//public class CentrepointCrosshairRenderer {
//  /**
//   * add a centrepoint to be rendered (as a crosshair) on the next frame.
//   *
//   * @param x world coordinates
//   * @param y
//   * @param z
//   */
//  public static void addCentrepointToRenderWorld(double x, double y, double z) {
//    addCentrepointToRenderWorld(x, y, z, Color.WHITE);
//  }
//
//  /**
//   * add a centrepoint to be rendered (as a crosshair) on the next frame.
//   *
//   * @param x world coordinates
//   * @param y
//   * @param z
//   */
//  public static void addCentrepointToRenderWorld(double x, double y, double z, Color colour) {
//    if (pointsToRenderNextFrameWorld.size() < MAX_POINTS) {
//      pointsToRenderNextFrameWorld.add(new ImmutablePair(new Vec3d(x, y, z), colour));
//    }
//  }
//
//  /**
//   * add a centrepoint to be rendered (as a crosshair) on the next frame.
//   *
//   * @param x scene coordinates (origin = player's eye)
//   * @param y
//   * @param z
//   */
//  public static void addCentrepointToRenderScene(double x, double y, double z) {
//    addCentrepointToRenderScene(x, y, z, Color.WHITE);
//  }
//
//  /**
//   * add a centrepoint to be rendered (as a crosshair) on the next frame.
//   *
//   * @param x scene coordinates (origin = player's eye)
//   * @param y
//   * @param z
//   */
//  public static void addCentrepointToRenderScene(double x, double y, double z, Color colour) {
//    if (pointsToRenderNextFrameScene.size() < MAX_POINTS) {
//      pointsToRenderNextFrameScene.add(new ImmutablePair(new Vec3d(x, y, z), colour));
//    }
//  }
//
//  /**
//   * render this first (before any other DrawBlockHighlightEvents which might cancel it)
//   *
//   * @param event the event
//   */
//  @SubscribeEvent(priority = EventPriority.HIGHEST)
//  public void centrepointCrosshairRenderer(DrawBlockHighlightEvent event) {
//    for (Pair<Vec3d, Color> cpc : pointsToRenderNextFrameWorld) {
//      draw3DCrosshair(cpc.getLeft(), cpc.getRight(), event.getPlayer(), event.getPartialTicks(), true);
//    }
//    pointsToRenderNextFrameWorld.clear();
//
//    for (Pair<Vec3d, Color> cpc : pointsToRenderNextFrameScene) {
//      draw3DCrosshair(cpc.getLeft(), cpc.getRight(), event.getPlayer(), event.getPartialTicks(), false);
//    }
//    pointsToRenderNextFrameScene.clear();
//
//  }
//
//  /**
//   * draw an oscillating outlined bounding box around the indicated aabb
//   *
//   * @param centrepoint        the location of the centrepoint of the 3D crosshair
//   * @param entityPlayer       used to offset based on player's eye position
//   * @param partialTick
//   * @param isWorldCoordinates if true, use world coordinates.  Otherwise: scene coordinates (player eye = origin)
//   */
//  private void draw3DCrosshair(Vec3d centrepoint, Color colour, EntityPlayer entityPlayer, double partialTick, boolean isWorldCoordinates) {
//
//    // copied from DebugRendererCollisionBox
//
//    GlStateManager.enableBlend();
//    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
//            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//    GlStateManager.glLineWidth(2.0F);
//    GlStateManager.disableTexture2D();
//    GlStateManager.depthMask(false);
//    final float MAX_BYTE = 255;
////    GlStateManager.color(colour.getRed()/MAX_BYTE, colour.getGreen()/MAX_BYTE, colour.getBlue()/MAX_BYTE, colour.getAlpha()/MAX_BYTE);
//
//
//    double px = 0;
//    double py = 0;
//    double pz = 0;
//
//    if (isWorldCoordinates) {
//      px = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTick;
//      py = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * partialTick;
//      pz = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTick;
//    }
//
//    Color dummyDrawColour = Color.WHITE;
//    double cx = centrepoint.x - px;
//    double cy = centrepoint.y - py;
//    double cz = centrepoint.z - pz;
//
//    final double CROSSHAIR_RADIUS = 6.0;
//    drawLine(cx - CROSSHAIR_RADIUS, cy, cz, cx + CROSSHAIR_RADIUS, cy, cz, colour);
//    drawLine(cx, cy - CROSSHAIR_RADIUS, cz, cx, cy + CROSSHAIR_RADIUS, cz, colour);
//    drawLine(cx, cy, cz - CROSSHAIR_RADIUS, cx, cy, cz + CROSSHAIR_RADIUS, colour);
//
//    GlStateManager.depthMask(true);
//    GlStateManager.enableTexture2D();
//    GlStateManager.disableBlend();
//  }
//
//  private static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color colour) {
//    Tessellator tessellator = Tessellator.getInstance();
//    BufferBuilder bufferbuilder = tessellator.getBuffer();
//    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
//    bufferbuilder.pos(x1, y1, z1).color(colour.getRed(), colour.getGreen(), colour.getBlue(), 255).endVertex();
//    bufferbuilder.pos(x2, y2, z2).color(colour.getRed(), colour.getGreen(), colour.getBlue(), 255).endVertex();
//    tessellator.draw();
//  }
//  private static final int MAX_POINTS = 500;
//  private static List<Pair<Vec3d, Color>> pointsToRenderNextFrameWorld = new ArrayList<>(MAX_POINTS);
//  private static List<Pair<Vec3d, Color>> pointsToRenderNextFrameScene = new ArrayList<>(MAX_POINTS);
//
//}
