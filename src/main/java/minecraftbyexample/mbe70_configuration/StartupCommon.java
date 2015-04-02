package minecraftbyexample.mbe70_configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

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
	//Define your configuration object
	private static Configuration config;
	
	//Declare all configuration variables used by the mod here
	public static int myInteger = 10;
	public static boolean myBoolean = true;
	public static float myFloat = 0.08f;
	public static double myDouble = 0.80;
	public static int[] myNumbers = { 1, 2, 3, 4, 5 };
	
	public static void preInitCommon() 
	{
		config = null;
		
		/*
		 * Here is where you specify the location from where your config file will be read, or
		 * 	created if it is not present.
		 * Loader.instance().getConfigDir() returns the default config directory and you specify
		 * 	the name of the config file, together this works similar to the old getSuggestedConfigurationFile() function
		 */
		File configFile = new File(Loader.instance().getConfigDir(), "MinecraftByExample.cfg");
		//initialize your configuration object with your configuration file values
		config = new Configuration(configFile);
	}
	
	public static void initCommon() 
	{
	}
	
	public static void postInitCommon() 
	{
	}
	
	public static Configuration getConfig() 
	{
		return config;
	}
	
	public static void syncConfig(boolean load) 
	{
		//By defining a property order we can control the order of the properties in the config file and GUI
		//This is defined on a per config-catagory basis
		List<String> propOrder = new ArrayList<String>();
		
		/*Check if this configuration object is the main config file or a child configuration
		 *For simple configuration setups, this only matters if you enable global configuration
		 *	for your configuration object by using config.enableGlobalConfiguration(),
		 *	this will cause your config file to be 'global.cfg' in the default configuration directory
		 *  and use it to read/write your configuration options
		 */
		if (!config.isChild) 
		{
			//check if loading configuration from file for first time
			if (load) 
			{
				config.load();
			}
		}
		
		Property prop;
		/* Using language keys are a good idea if you are using a config GUI
		 * This allows you to provide "pretty" names for the config properties
		 * 	in a .lang file as well as allow others to provide other localizations
		 *  for your mod
		 * The language key is also used to get the tooltip for your property,
		 * 	the language key for each properties tooltip is langKey + ".tooltip"
		 *  If no tooltip lang key is specified in a .lang file, it will default to
		 *  the property's comment field
		 */
		
		//how to read integers from configuration (bounding your values is recommended for stability)
		prop = config.get(Configuration.CATEGORY_GENERAL, "myInteger", 10, "Configuration integer", 3, 12);
		prop.setLanguageKey("gui.mbe70_configuration.myInteger");
		//If getInt cannot get an integer value from the config file value of myInteger (e.g. corrupted file)
		// it will set it to the default value of 10 (passed to the function)
		myInteger = prop.getInt(10);
		if (myInteger > 12 || myInteger < 3) 
		{
			myInteger = 10;
			prop.set(10); //shows how to set property values
		}
		propOrder.add(prop.getName()); //push the config value's name into the ordered list
		
		//how to read booleans from configuration
		prop = config.get(Configuration.CATEGORY_GENERAL,  "myBoolean", true);
		prop.comment = "Configuration boolean";
		prop.setLanguageKey("gui.mbe70_configuration.myBoolean");
		myBoolean = prop.getBoolean(myBoolean); //can also use a literal (see integer example) if desired
		propOrder.add(prop.getName());
		
		//how to read doubles from configuration
		prop = config.get(Configuration.CATEGORY_GENERAL, "myDouble", 0.80, "Configuration double", 0, 1);
		prop.setLanguageKey("gui.mbe70_configuration.myDouble");
		myDouble = prop.getDouble(0.80);
		if (myDouble > 1.0 || myDouble < 0.0) 
		{
			myDouble = 0.80;
		}
		propOrder.add(prop.getName());
		
		//how to read floating point numbers from configuration
		prop = config.get(Configuration.CATEGORY_GENERAL, "myFloat", 0.08, "Configuration float", 0, 1);
		prop.setLanguageKey("gui.mbe70_configuration.myFloat");
		myFloat = (float) prop.getDouble((double) myFloat); //no getFloat function is implemented so we use type casting
		if (myFloat > 1.0f || myFloat < 0.0f) 
		{
			myFloat = 0.08f;
		}
		propOrder.add(prop.getName());
		
		//how to read integer lists from configuration
		prop = config.get(Configuration.CATEGORY_GENERAL, "myNumbers", new int[] { 1, 2, 3, 4, 5 }, "Configuration integer list");
		prop.setLanguageKey("gui.mbe70_configuration.myNumbers");
		myNumbers = prop.getIntList();
		propOrder.add(prop.getName());
		
		config.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, propOrder);
		
		if (config.hasChanged()) 
		{
			config.save();
		}
	}
}
