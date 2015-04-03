package minecraftbyexample.mbe70_configuration;

import minecraftbyexample.MinecraftByExample;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StartupClientOnly {
	public static void preInitClientOnly() 
	{
		//register the save config handler to the forge mod loader event bus
		// creates an instance of the static class ConfigEventHandler and has it listen
		// on the FML bus (see Notes and ConfigEventHandler for more information)
		FMLCommonHandler.instance().bus().register(new ConfigEventHandler());
	}
	
	public static void initClientOnly() 
	{
	}
	
	public static void postInitClientOnly() 
	{
	}
	
	public static class ConfigEventHandler 
	{
		/*
		 * This class, when instantiated as an object, will listen on the FML
		 *  event bus for an OnConfigChangedEvent
		 */
		@SubscribeEvent(priority=EventPriority.NORMAL)
		public void onEvent(OnConfigChangedEvent event) 
		{
			if (MinecraftByExample.MODID.equals(event.modID) && !event.isWorldRunning) 
			{
				if (Configuration.CATEGORY_GENERAL.equals(event.configID)) 
				{
					StartupCommon.syncConfig(false);
				}
				else if ("miscConfig".equals(event.configID)) 
				{
					//can use other configuration categories in additional
					// else if blocks, miscConfig is a sample name for a
					// custom category that a mod creator could implement
					// in addition to CATEGORY_GENERAL, see the Configuration class
					// for more information
				}
			}
		}
	}
}
