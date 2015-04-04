package minecraftbyexample.mbe70_configuration;

import minecraftbyexample.MinecraftByExample;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    MBEConfiguration.clientInit();  // used to set up an event handler for the GUI so that the altered values are
                                    //  saved back to disk.
	}
	
	public static void initClientOnly() 
	{
	}
	
	public static void postInitClientOnly() 
	{
	}
	
}
