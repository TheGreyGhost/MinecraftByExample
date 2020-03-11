package minecraftbyexample.usefultools.debugging;

import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * Created by TGG on 29/06/2015.
 *
 * Holds debugging settings for interactive debugging:
 * Typical usage
 * 1) setDebugParameter(name, value) - for example in response to user command\
 *    /mbedebug param <name> <value></value>
 *    /mbedebug param x 0.3
 * 2) getDebugParameter(name) - for example in a rendering routine
 *    double renderOffsetX = getDebugParameter("x").orElse(0.0);
 *    which can then be subsequently used for (eg) interactively adjusting rendering offsets in-game
 * 3) listAllDebugParameters() to be used for (eg) autocompletion suggestions
 *        is automatically populated when code sets or gets a name
 *
 *  Also has parallel implementation for Vec3d instead of double.
 *
 *  Also has parallel implementation for triggers (set the trigger, which is automatically reset upon read)
 *
 *  Note that this class only works properly for debugging on an integrated server;  i.e. the DebugSettings can be
 *    read from either the client or the server side.  There are no network messages to synchronise debug settings
 *    between the server and multiple clients.
 *
 */
public class DebugSettings {

  public static synchronized void setDebugParameter(String parameterName, double value) {
    debugParameters.put(parameterName, Optional.of(value));
  }

  public static synchronized void clearDebugParameter(String parameterName) {
    debugParameters.put(parameterName, Optional.empty());
  }

  /**
   * Gets the value of the given debug parameter; or empty if not previously set.
   * @param parameterName
   * @return
   */
  public static synchronized Optional<Double> getDebugParameter(String parameterName) {
    Optional<Double> value = debugParameters.get(parameterName);
    if (value == null) {
      debugParameters.put(parameterName, Optional.empty());
      return Optional.empty();
    }
    return value;
  }

  public static synchronized Set<String> listAllDebugParameters() {
    return debugParameters.keySet();
  }

  private static HashMap<String, Optional<Double>> debugParameters = new HashMap<>();

  //-----------

  public static synchronized void setDebugParameterVec3d(String parameterName, Vec3d value) {
    debugParameterVec3ds.put(parameterName, Optional.of(value));
  }

  public static synchronized void clearDebugParameterVec3d(String parameterName) {
    debugParameterVec3ds.put(parameterName, Optional.empty());
  }

  /**
   * Gets the value of the given debug parameter; or 0 if not previously set
   * @param parameterName
   * @return
   */
  public static synchronized Optional<Vec3d> getDebugParameterVec3d(String parameterName) {
    Optional<Vec3d> value = debugParameterVec3ds.get(parameterName);
    if (value == null) {
      debugParameterVec3ds.put(parameterName, Optional.empty());
      return Optional.empty();
    }
    return value;
  }

  public static synchronized Set<String> listAllDebugParameterVec3ds() {
    return debugParameterVec3ds.keySet();
  }

  private static HashMap<String, Optional<Vec3d>> debugParameterVec3ds = new HashMap<>();

  //-----------

  public static synchronized void setDebugTrigger(String parameterName) {
    debugTriggers.put(parameterName, true);
  }

  /**
   * Returns true if the trigger is set.  Resets to false.
   * @param parameterName
   * @return
   */
  public static synchronized boolean getDebugTrigger(String parameterName) {
    Boolean value = debugTriggers.getOrDefault(parameterName, false);
    debugTriggers.put(parameterName, false);
    return value;
  }

  public static synchronized Set<String> listAllDebugTriggers() {
    return debugTriggers.keySet();
  }

  private static HashMap<String, Boolean> debugTriggers = new HashMap<>();

  //-----------------

  public static synchronized void setDebugTest(int testnumber) {
    debugTest = testnumber;
  }

  /**
   * Returns a test number if one has been triggered.  resets after being called
   * @return the test number to execute, or -1 if none triggered
   */
  public static synchronized int getDebugTest() {
    int value = debugTest;
    debugTest = -1;
    return value;
  }

  private static int debugTest = -1;



}
