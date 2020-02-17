package minecraftbyexample.usefultools.debugging;

import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by TGG on 29/06/2015.
 *
 * Holds debugging settings for interactive debugging:
 * Typical usage
 * 1) setDebugParameter(name, value) - for example in response to user command\
 *    /mbedebug param <name> <value></value>
 *    /mbedebug param x 0.3
 * 2) getDebugParameter(name) - for example in a rendering routine
 *    double renderOffsetX = getDebugParameter("x");
 *    which can then be subsequently used for (eg) interactively adjusting rendering offsets in-game
 * 3) listAllDebugParameters() to be used for (eg) autocompletion suggestions
 *        is automatically populated when code sets or gets a name
 *
 *  Also has parallel implementation for Vec3d instead of double.
 *
 *  Also has parallel implementation for triggers (set the trigger, which is automatically reset upon read)
 *
 */
public class DebugSettings {

  public static void setDebugParameter(String parameterName, double value) {
    debugParameters.put(parameterName, value);
  }

  /**
   * Gets the value of the given debug parameter; or 0 if not previously set
   * @param parameterName
   * @return
   */
  public static double getDebugParameter(String parameterName) {
    Double value = debugParameters.get(parameterName);
    if (value == null) {
      value = 0.0;
      debugParameters.put(parameterName, value);
    }
    return value;
  }

  public static Set<String> listAllDebugParameters() {
    return debugParameters.keySet();
  }

  private static HashMap<String, Double> debugParameters = new HashMap<>();

  //-----------

  public static void setDebugParameterVec3d(String parameterName, Vec3d value) {
    debugParameterVec3ds.put(parameterName, value);
  }

  /**
   * Gets the value of the given debug parameter; or 0 if not previously set
   * @param parameterName
   * @return
   */
  public static Vec3d getDebugParameterVec3d(String parameterName) {
    Vec3d value = debugParameterVec3ds.get(parameterName);
    if (value == null) {
      value = Vec3d.ZERO;
      debugParameterVec3ds.put(parameterName, value);
    }
    return value;
  }

  public static Set<String> listAllDebugParameterVec3ds() {
    return debugParameterVec3ds.keySet();
  }

  private static HashMap<String, Vec3d> debugParameterVec3ds = new HashMap<>();

  //-----------

  public static void setDebugTrigger(String parameterName) {
    debugTriggers.put(parameterName, true);
  }

  /**
   * Returns true if the trigger is set.  Resets to false.
   * @param parameterName
   * @return
   */
  public static boolean getDebugTrigger(String parameterName) {
    Boolean value = debugTriggers.get(parameterName);
    if (value == null) {
      value = false;
      debugTriggers.put(parameterName, value);
    }
    return value;
  }

  public static Set<String> listAllDebugTriggers() {
    return debugTriggers.keySet();
  }

  private static HashMap<String, Boolean> debugTriggers = new HashMap<>();

  //-----------------

  public static void setDebugTest(int testnumber) {
    debugTest = testnumber;
  }

  /**
   * Returns a test number if one has been triggered.  resets after being called
   * @return the test number to execute, or -1 if none triggered
   */
  public static int getDebugTest() {
    int value = debugTest;
    debugTest = -1;
    return value;
  }

  private static int debugTest = -1;
}
