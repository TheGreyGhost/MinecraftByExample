package minecraftbyexample.mbe70_configuration;

/*
 * User: TW
 * Date: 2/4/2015
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
	public static void preInitCommon()
	{
    MBEConfiguration.preInit();
    System.out.println("MBE70: myInteger=" + MBEConfiguration.myInteger
                               + "; myBoolean=" + MBEConfiguration.myBoolean
                               + "; myString=" + MBEConfiguration.myString);
    System.out.println("MBE70: myDouble=" + MBEConfiguration.myDouble
                               + "; myColour=" + MBEConfiguration.myColour);
    System.out.print("MBE70: myIntList=");
    for (int value : MBEConfiguration.myIntList) {
      System.out.print(value + "; ");
    }
    System.out.println();

  }

	public static void initCommon()
	{
	}

	public static void postInitCommon()
	{
	}

}
