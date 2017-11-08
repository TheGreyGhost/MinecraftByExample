package minecraftbyexample.usefultools;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MethodCallLogger is used to produce a log of method calls, useful for debugging where a breakpoint would
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
    HashMap<String, Boolean> reentryFlagsSide = (side == Side.CLIENT) ? reentryFlagsClient : reentryFlagsServer;
    final int indentLevel = (side == Side.CLIENT) ? indentLevelClient : indentLevelServer;
    if (reentryFlagsSide.containsKey(methodName) && reentryFlagsSide.get(methodName) ) {  // reentry: disable logging for this method
      String errorMessage = "!! Re-entry into " + methodName + "(" + parameters + ")";
      addIndentedOutputLine(side, indentLevel, errorMessage, false);
      errorMessage = "!!Further logging of " + methodName + " disabled!!";
      if (side == Side.CLIENT) --indentLevelClient; else --indentLevelServer;
      addIndentedOutputLine(side, indentLevel, errorMessage, indentLevel == 0 || immediateOutput);
      setShouldLog(methodName, false);
      return;
    }

    reentryFlagsSide.put(methodName, true);
    addIndentedOutputLine(side, indentLevel, methodName + "(" + parameters + ") {", immediateOutput);
    if (side == Side.CLIENT) ++indentLevelClient; else ++indentLevelServer;
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
    HashMap<String, Boolean> reentryFlagsSide = (side == Side.CLIENT) ? reentryFlagsClient : reentryFlagsServer;
    final int indentLevel = (side == Side.CLIENT) ? indentLevelClient : indentLevelServer;

    if (!reentryFlagsSide.containsKey(methodName) || !reentryFlagsSide.get(methodName) ) {  // never entered: ignore call
      return;
    }

    reentryFlagsSide.put(methodName, false);
    if (side == Side.CLIENT) {
      --indentLevelClient;
      if (indentLevelClient < 0) indentLevelClient = 0;  // should never happen.
    } else {
      --indentLevelServer;
      if (indentLevelServer < 0) indentLevelServer = 0;  // should never happen.
    }
    addIndentedOutputLine(side, indentLevel-1, "} " + methodName + " return=" + returnValue, indentLevel <= 1 || immediateOutput);
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
      if (!shouldLogServer) return false;
    } else if (side == Side.CLIENT) {
      if (!shouldLogClient) return false;
    }
    if (!shouldLogMap.containsKey(methodName)) return true;  // default to true if user hasn't explicitly enabled logging
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
      outputBuffer.setLength(0);
    }
  }

  /**
   * Adds a line of logging information to the output buffer for later printing to the output device
   * indents the line according to the specified indent level
   * @param indentLevel the indent level (>= 0)
   * @param outputToAdd the line to add
   * @param flushImmediately if true - flush the output buffer to the output device
   */
  private void addIndentedOutputLine(Side side, int indentLevel, String outputToAdd, boolean flushImmediately)
  {
    StringBuilder outputBuffer = (side == Side.CLIENT) ? outputBufferClient : outputBufferServer;
    String sideSymbol = (side == Side.CLIENT) ? "C: " : "S: ";
    outputBuffer.append((sideSymbol));
    int endIndex = indentLevel * SPACES_PER_INDENT;
    if (endIndex < 0) endIndex = 0;
    if (endIndex > INDENT_STRING.length()) endIndex = INDENT_STRING.length();
    outputBuffer.append(INDENT_STRING.substring(0, endIndex));
    addOutputLine(side, outputToAdd, flushImmediately);
  }

  private ConcurrentHashMap<String, Boolean> shouldLogMap = new ConcurrentHashMap<String, Boolean>();
  private boolean immediateOutput = false;
  private boolean shouldLogClient = true;
  private boolean shouldLogServer = true;
  private HashMap<String, Boolean> reentryFlagsClient = new HashMap<String, Boolean>();
  private HashMap<String, Boolean> reentryFlagsServer = new HashMap<String, Boolean>();
  private int indentLevelClient = 0;
  private int indentLevelServer = 0;

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
  public static void test()
  {
    MethodCallLogger logger = new MethodCallLogger();

    final String NAME1 = "method1";
    final String NAME2 = "method2";
    final String NAME3 = "method3";

    // to test:
    // 1) proper indenting with several nested calls
    // 2) test disabling of a method
    // 3) test disabling of a side
    // 4) detect reentrancy and handle properly
    // 5) check for correct parallel operation on both sides
    // 6) immediate flush mode
    // verify by inspection

    System.out.println("MethodCallLogger test start");

    System.out.println("Test1");
    logger.forcedSideForTesting = Side.CLIENT;
    logger.enterMethod(NAME1, "param1");
      logger.enterMethod(NAME2, "param2");
        logger.enterMethod(NAME3, "3");
        logger.exitMethod(NAME3, "retval3");
      logger.exitMethod(NAME2, "retval2");
      logger.enterMethod(NAME3, "");
      logger.exitMethod(NAME3, "");
    logger.exitMethod(NAME1, "retval1");

    logger = new MethodCallLogger();
    System.out.println("Test2");    // result as per test1 except no method2
    logger.forcedSideForTesting = Side.CLIENT;
    logger.setShouldLog(NAME2, false);
    logger.enterMethod(NAME1, "param1");
                          logger.enterMethod(NAME2, "param2");
      logger.enterMethod(NAME3, "3");
      logger.exitMethod(NAME3, "retval3");
                          logger.exitMethod(NAME2, "retval2");
      logger.enterMethod(NAME3, "");
      logger.exitMethod(NAME3, "");
    logger.exitMethod(NAME1, "retval1");

    logger = new MethodCallLogger();
    System.out.println("Test3");    // result as per test1 except no method3 inside method2
    logger.forcedSideForTesting = Side.CLIENT;
    logger.setSideLogging(Side.SERVER, false);
    logger.enterMethod(NAME1, "param1");
      logger.enterMethod(NAME2, "param2");
    logger.forcedSideForTesting = Side.SERVER;
                           logger.enterMethod(NAME3, "3");
                           logger.exitMethod(NAME3, "retval3");
    logger.forcedSideForTesting = Side.CLIENT;
      logger.exitMethod(NAME2, "retval2");
      logger.enterMethod(NAME3, "");
      logger.exitMethod(NAME3, "");
    logger.exitMethod(NAME1, "retval1");

    logger = new MethodCallLogger();
    System.out.println("Test4");    // rentrant: stop logging method2: error message on 2nd, then 3 and 3 again
    logger.forcedSideForTesting = Side.CLIENT;
    logger.enterMethod(NAME1, "param1");
      logger.enterMethod(NAME2, "param2");
                           logger.enterMethod(NAME2, "param2");
      logger.enterMethod(NAME3, "param3");
                           logger.enterMethod(NAME2, "param2");
                           logger.exitMethod(NAME2, "retval2");
      logger.exitMethod(NAME3, "retval3");
                           logger.exitMethod(NAME2, "retval2");
                           logger.exitMethod(NAME2, "retval2");
      logger.enterMethod(NAME3, "");
      logger.exitMethod(NAME3, "");
    logger.exitMethod(NAME1, "retval1");

    logger = new MethodCallLogger();
    System.out.println("Test5");      // client nested 1,2,3.  Server 1 then 3
    logger.forcedSideForTesting = Side.CLIENT;
    logger.enterMethod(NAME1, "param1");
      logger.enterMethod(NAME2, "param2");

    logger.forcedSideForTesting = Side.SERVER;
    logger.enterMethod(NAME2, "Server1st");

    logger.forcedSideForTesting = Side.CLIENT;
        logger.enterMethod(NAME3, "3");

    logger.forcedSideForTesting = Side.SERVER;
    logger.exitMethod(NAME2, "Server1stExit");
    logger.enterMethod(NAME3, "serverside");

    logger.forcedSideForTesting = Side.CLIENT;
        logger.exitMethod(NAME3, "retval3");
      logger.exitMethod(NAME2, "retval2");

    logger.forcedSideForTesting = Side.SERVER;
    logger.exitMethod(NAME3, "serverside");
    logger.forcedSideForTesting = Side.CLIENT;
    logger.exitMethod(NAME1, "retval1");

    logger = new MethodCallLogger();
    System.out.println("Test6");      // interleaved output as per 5
    logger.setImmediateOutput(true);
    logger.forcedSideForTesting = Side.CLIENT;
    logger.enterMethod(NAME1, "param1");
      logger.enterMethod(NAME2, "param2");

    logger.forcedSideForTesting = Side.SERVER;
    logger.enterMethod(NAME2, "Server1st");

    logger.forcedSideForTesting = Side.CLIENT;
        logger.enterMethod(NAME3, "3");

    logger.forcedSideForTesting = Side.SERVER;
    logger.exitMethod(NAME2, "Server1stExit");
    logger.enterMethod(NAME3, "serverside");

    logger.forcedSideForTesting = Side.CLIENT;
        logger.exitMethod(NAME3, "retval3");
      logger.exitMethod(NAME2, "retval2");

    logger.forcedSideForTesting = Side.SERVER;
    logger.exitMethod(NAME3, "serverside");
    logger.forcedSideForTesting = Side.CLIENT;
    logger.exitMethod(NAME1, "retval1");

    System.out.println("MethodCallLogger test complete");
  }


}
