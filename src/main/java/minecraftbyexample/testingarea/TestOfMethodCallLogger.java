package minecraftbyexample.testingarea;

import minecraftbyexample.usefultools.MethodCallLogger;

/**
 * User: The Grey Ghost
 * Date: 6/01/2015
 */
public class TestOfMethodCallLogger
{
  static public void test()
  {
    MethodCallLogger logger = new MethodCallLogger();

    final String NAME1 = "method1";
    final String NAME2 = "method2";
    final String NAME3 = "method3";

    // to test:
    // 1) proper indenting
    // 2) detect reentrancy and handle properly
    // 2) immediate flush mode
    // 3)

  }
}
