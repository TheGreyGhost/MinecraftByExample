package minecraftbyexample.usefultools;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestLogging is used to produce a log of method calls, useful for debugging where a breakpoint would
 *  alter the behaviour (eg behaviour depends on user input)
 *  How to use:
 *  1) Create the MethodCallLogger, optionally with a given PrintStream (will use System.out by default)
 *  2) add enterMethod() and exitMethod() to the method(s) you wish to log, as per the example below
 *  3) (optional) call setShouldLog() for each method to true (logging on) or false (logging off).  If you call enterMethod or exitMethod
 *     with a methodName that doesn't exist, it will default to true
 *  The logger will produce indented output as per the example below; By default it will store up the output until the last outdent, in order
 *    to avoid interleaving client and server logging statements.  You can turn this off using setImmediateOutput.
 *  You can turn off logging for one or both sides (CLIENT / SERVER) using setSideLogging
 *  If you use mismatched enterMethod and exitMethod, the logger will detect it and turn off logging for that methodName.
 *  The class is not explicitly designed to handle stop/start of logging while running the methods.  It may work, but may
 *    also produce strange output.
 * User: The Grey Ghost
 * Date: 6/01/2015
 */
public class MethodCallLogger
{
  public MethodCallLogger()
  {
    this(System.out);
  }

  public MethodCallLogger(PrintStream stream)
  {
    printStream = stream;
  }
  /**
   * Log the entry into this method
   * @param methodName the name of the method eg myMethodToBeLogged
   * @param parameters the parameters eg "true, 36"
   */
  public void enterMethod(String methodName, String parameters)
  {
    if (!shouldLog(methodName)) return;
    Side side = (forcedSideForTesting != null) ? forcedSideForTesting : FMLCommonHandler.instance().getEffectiveSide();
    ConcurrentHashMap<String, Boolean> reentryFlagsSide = (side == Side.CLIENT) ? reentryFlagsClient : reentryFlagsServer;
    Integer indentLevel = (side == Side.CLIENT) ? indentLevelClient : indentLevelServer;
    if (reentryFlagsSide.containsKey(methodName) && reentryFlagsSide.get(methodName) ) {  // reentry: disable logging for this method
      String errorMessage = "Re-entry into " + methodName + "(" + parameters + ")";
      addIndentedOutputLine(indentLevel, errorMessage, false);
      errorMessage = "Further logging of " + methodName + " disabled.";
      --indentLevel;
      addIndentedOutputLine(indentLevel, errorMessage, indentLevel == 0 || immediateOutput);
      setShouldLog(methodName, false);
      return;
    }

    reentryFlagsSide.put(methodName, true);
    addIndentedOutputLine(indentLevel, methodName + "(" + parameters + ") {", immediateOutput);
    ++indentLevel;
  }

  /**
   * Log the exit from this method
   * @param methodName the name of the method eg myMethodToBeLogged
   * @param returnValue the return value (eg "true")
   */
  public void exitMethod(String methodName, String returnValue)
  {
    if (!shouldLog(methodName)) return;
    Side side = (forcedSideForTesting != null) ? forcedSideForTesting : FMLCommonHandler.instance().getEffectiveSide();
    ConcurrentHashMap<String, Boolean> reentryFlagsSide = (side == Side.CLIENT) ? reentryFlagsClient : reentryFlagsServer;
    Integer indentLevel = (side == Side.CLIENT) ? indentLevelClient : indentLevelServer;

    if (!reentryFlagsSide.containsKey(methodName) || !reentryFlagsSide.get(methodName) ) {  // never entered: ignore call
      return;
    }

    reentryFlagsSide.put(methodName, false);
    --indentLevel;
    if (indentLevel < 0) indentLevel = 0; // just in case - should never happen!
    addIndentedOutputLine(indentLevel, "} " + methodName + " return= " + returnValue, indentLevel == 0 || immediateOutput);
  }

  /**
   * Turn the logging on or off for a given method.  (Optional since logging defaults to true)
   * @param methodName the name of the method eg "myMethod"
   * @param shouldLog true to enable logging for this method, false to disable
   */
  public void setShouldLog(String methodName, boolean shouldLog)
  {
    shouldLogMap.put(methodName, shouldLog);
  }

  /**
   * Turn the logging on or off for each side (SERVER or CLIENT)
   * @param side which side
   * @param shouldLog true to enable logging for this side
   */
  public void setSideLogging(Side side, boolean shouldLog) {
    if (side == Side.CLIENT) {
      shouldLogClient = shouldLog;
    } else if (side == Side.SERVER) {
      shouldLogServer = shouldLog;
    } else {
      System.out.println("Illegal side :" + side);
    }
  }

  /**
   * Should the logging be sent to the output (eg the console) immediately?
   * @param outputIsImmediate
   */
  public void setImmediateOutput(boolean outputIsImmediate)
  {
    immediateOutput = outputIsImmediate;
  }

  /**
   * Is logging enabled for this method?
   * @param methodName the name of the method.
   * @return true if should log, false if not.  defaults to true.
   */
  public boolean shouldLog(String methodName) {
    Side side = (forcedSideForTesting != null) ? forcedSideForTesting : FMLCommonHandler.instance().getEffectiveSide();
    if (side == Side.SERVER) {
      if (shouldLogServer) return false;
    } else if (side == Side.CLIENT) {
      if (shouldLogClient) return false;
    }
    if (!shouldLogMap.containsKey(methodName)) return true;
    return shouldLogMap.get(methodName);
  }

  /**
   * Adds a line of logging information to the output buffer for later printing to the output device (eg console)
   * @param outputToAdd the line to add
   * @param flushImmediately if true - flush the output buffer to the output device
   */
  public void addOutputLine(Side side, String outputToAdd, boolean flushImmediately)
  {
    StringBuilder outputBuffer = (side == Side.CLIENT) ? outputBufferClient : outputBufferServer;
    outputBuffer.append(outputToAdd);
    outputBuffer.append(CRLF);
    if (flushImmediately) {
      printStream.print(outputBuffer);
      outputBuffer.;
    }
  }

  /**
   * Adds a line of logging information to the output buffer for later printing to the output device
   * indents the line according to the specified indent level
   * @param indentLevel the indent level (>= 0)
   * @param outputToAdd the line to add
   * @param flushImmediately if true - flush the output buffer to the output device
   */
  private void addIndentedOutputLine(int indentLevel, String outputToAdd, boolean flushImmediately)
  {
    int endIndex = indentLevel * SPACES_PER_INDENT;
    if (endIndex < 0) endIndex = 0;
    if (endIndex > INDENT_STRING.length()) endIndex = INDENT_STRING.length();
    outputBufferClient.append(INDENT_STRING.substring(0, endIndex));
    addOutputLine(outputToAdd, flushImmediately);
  }

  private ConcurrentHashMap<String, Boolean> shouldLogMap = new ConcurrentHashMap<String, Boolean>();
  private boolean immediateOutput = true;
  private boolean shouldLogClient = true;
  private boolean shouldLogServer = true;
  private ConcurrentHashMap<String, Boolean> reentryFlagsClient = new ConcurrentHashMap<String, Boolean>();
  private ConcurrentHashMap<String, Boolean> reentryFlagsServer = new ConcurrentHashMap<String, Boolean>();
  private Integer indentLevelClient = 0;
  private Integer indentLevelServer = 0;

  private PrintStream printStream;
  private static final int INITIAL_STRING_CAPACITY = 1000;
  private StringBuilder outputBufferClient = new StringBuilder(INITIAL_STRING_CAPACITY);
  private StringBuilder outputBufferServer = new StringBuilder(INITIAL_STRING_CAPACITY);
  private static final String CRLF = System.getProperty("line.separator");
  private static final String INDENT_STRING = "                                                            ";  // maximum indent 60 spaces
  private static final int SPACES_PER_INDENT = 2;
  private Side forcedSideForTesting = null;

  /**
   * For testing purposes
   */
  public void test()
  {
    MethodCallLogger logger = new MethodCallLogger();

    final String NAME1 = "method1";
    final String NAME2 = "method2";
    final String NAME3 = "method3";

    // to test:
    // 1) proper indenting
    // 2) test disabling of
    // 2) detect reentrancy and handle properly
    // 2) immediate flush mode
    // 3)

  }


}
