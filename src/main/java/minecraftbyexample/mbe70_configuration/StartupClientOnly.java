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

public class StartupClientOnly {
	public static void preInitClientOnly()
	{
    MBEConfiguration.clientPreInit();  // used to set up an event handler for the GUI so that the altered values are
                                    //  saved back to disk.
	}

	public static void initClientOnly()
	{
	}

	public static void postInitClientOnly()
	{
	}

}
