//package minecraftbyexample.usefultools.debugging;
//
//import com.TheRPGAdventurer.ROTD.DragonMounts;
//import net.minecraft.util.math.Vec3d;
//
//import java.util.HashMap;
//
///**
// * Created by TGG on 29/06/2015.
// * Freeze dragon animation and updates for debugging purposes
// * frozen when the scroll lock key is down (and debug mode is set)
// */
//public class DebugSettings {
//
//  public static boolean isDebugGuiEnabled() {
//    return DragonMounts.instance.getConfig().isDebug() && debugGuiEnabled;
//  }
//
//  public static void setDebugGuiEnabled(boolean newstate) {
//    debugGuiEnabled = newstate;
//  }
//
//  public static boolean isSpawningInhibited() {
//    return DragonMounts.instance.getConfig().isDebug() && spawningInhibited;
//  }
//
//  public static void setSpawningInhibited(boolean newstate) {
//    spawningInhibited = newstate;
//  }
//
//  /**
//   * are the draqon and breath animations be frozen in place?
//   * Usage:
//   * 1) setAnimationFreezeEnabled to enable freezing
//   * 2) whenever you want the animation to be frozen, call setAnimationFreezeActive
//   * (this does nothing unless freezing has been enabled)
//   * Disabling the freezing will also deactivate freezing
//   *
//   * @return
//   */
//  public static boolean isAnimationFrozen() {
//    return DragonMounts.instance.getConfig().isDebug() && frozenEnabled && frozenActive;
//  }
//
//  public static float animationFrozenPartialTicks() {
//    return partialTicks;
//  }
//
//  public static boolean isAnimationFreezeEnabled() {
//    return frozenEnabled;
//  }
//
//  public static void setAnimationFreezeEnabled(boolean newstate) {
//    frozenEnabled = newstate;
//    if (!frozenEnabled) frozenActive = false;
//  }
//
//  public static void setAnimationFreezeActive(boolean newstate) {
//    frozenActive = newstate;
//  }
//
//  public static boolean isRenderCentrePoints() {
//    return renderCentrePoints;
//  }
//
//  public static void setRenderCentrePoints(boolean renderCentrePoints) {
//    DebugSettings.renderCentrePoints = renderCentrePoints;
//  }
//
//  public static boolean isRenderXYZmarkers() {
//    return renderXYZmarkers;
//  }
//
//  public static void setRenderXYZmarkers(boolean renderXYZmarkers) {
//    DebugSettings.renderXYZmarkers = renderXYZmarkers;
//  }
//
//  public static boolean isRenderDragonPoints() {
//    return renderDragonPoints;
//  }
//
//  public static void setRenderDragonPoints(boolean renderDragonPoints) {
//    DebugSettings.renderDragonPoints = renderDragonPoints;
//  }
//
//  public static boolean isForceDragonModel() {
//    return forceDragonModel;
//  }
//
//  public static void setForceDragonModel(boolean forceDragonModel) {
//    DebugSettings.forceDragonModel = forceDragonModel;
//  }
//
//  public static boolean isBoxDragon() {
//    return boxDragon;
//  }
//
//  public static void setBoxDragon(boolean boxDragon) {
//    DebugSettings.boxDragon = boxDragon;
//  }
//
//  public static boolean isRiderPositionTweak() {
//    return riderPositionTweak;
//  }
//
//  public static void setRiderPositionTweak(boolean riderPositionTweak) {
//    DebugSettings.riderPositionTweak = riderPositionTweak;
//  }
//
//  public static Vec3d getRiderPositionOffset(int rider, Vec3d offset) {
//    double x = offset.x;
//    double y = offset.y;
//    double z = offset.z;
//    if (existsDebugParameter("rx" + rider)) {
//      x = (float) DebugSettings.getDebugParameter("rx" + rider);
//    }
//    if (existsDebugParameter("ry" + rider)) {
//      y = (float) DebugSettings.getDebugParameter("ry" + rider);
//    }
//    if (existsDebugParameter("rz" + rider)) {
//      z = (float) DebugSettings.getDebugParameter("rz" + rider);
//    }
//    return new Vec3d(x, y, z);
//  }
//
//  public static boolean isForceDragonWalk() {
//    return forceDragonWalk;
//  }
//
//  public static void setForceDragonWalk(boolean forceDragonWalk) {
//    DebugSettings.forceDragonWalk = forceDragonWalk;
//  }
//
//  public static float getForceDragonWalkCycles() {
//    float dragonWalkCycle = (float) getDebugParameter("dragonwalkcycle");
//    if (dragonWalkCycle < 0) {
//      final long FULL_CYCLE_MS = 3000;
//      long cycleRemainder = System.currentTimeMillis() % FULL_CYCLE_MS;
//      dragonWalkCycle = cycleRemainder / (float) FULL_CYCLE_MS;
//    }
//    return dragonWalkCycle;
//  }
//
//  public static boolean isDragonWalkStraightLine() {
//    return dragonWalkStraightLine;
//  }
//
//  public static void setDragonWalkStraightLine(boolean dragonWalkStraightLine) {
//    DebugSettings.dragonWalkStraightLine = dragonWalkStraightLine;
//  }
//
//  public static float getDragonWalkSpeed() {
//    return (float) getDebugParameter("dragonwalkspeed");
//  }
//
//  /**
//   * Debug parameters can be set using the command console
//   * /dragon debug parameter {name} {value}
//   * eg
//   * /dragon debug parameter x 0.3
//   * Useful for interactively adjusting rendering offsets in-game
//   *
//   * @param parameterName
//   * @param value
//   */
//  public static void setDebugParameter(String parameterName, double value) {
//    debugParameters.put(parameterName, value);
//  }
//
//  public static double getDebugParameter(String parameterName) {
//    Double value = debugParameters.get(parameterName);
//    return (value == null) ? 0.0 : value;
//  }
//
//  public static boolean existsDebugParameter(String parameterName) {
//    return debugParameters.containsKey(parameterName);
//  }
//  private static final float partialTicks = 0.25F;
//  private static boolean debugGuiEnabled;
//  private static boolean spawningInhibited;
//  private static boolean frozenEnabled;
//  private static boolean frozenActive;
//  private static boolean renderCentrePoints;
//  private static boolean renderXYZmarkers;
//  private static boolean renderDragonPoints;
//  private static boolean forceDragonModel = false; //todo restore to false
//  private static boolean boxDragon = false;    //todo restore to false
//  private static boolean riderPositionTweak = false;
//  private static boolean forceDragonWalk = false;
//  private static boolean dragonWalkStraightLine = false;
//  private static HashMap<String, Double> debugParameters = new HashMap<>();
//
//
//}
