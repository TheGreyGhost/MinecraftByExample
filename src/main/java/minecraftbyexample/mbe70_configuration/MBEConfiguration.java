package minecraftbyexample.mbe70_configuration;

import minecraftbyexample.MinecraftByExample;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the configuration information and synchronises the various copies of it.
 * The configuration information is stored in three places:
 * 1) in the configuration file on disk, as text.
 * 2) in the Configuration object config (accessed by the mod GUI), as text.
 * 3) in the MBEConfiguration variables (fields), as native values (integer, double, etc).
 *
 * Setup:
 * (1) During your mod preInit(), call MBEConfiguration.preInit() to:
 *     a) set up the format of the configuration file.
 *     b) load the settings from the existing file, or if it doesn't exist yet - create it with default values.
 * (2) On the client proxy (not dedicated server), call clientPreInit() to register an OnConfigChangedEvent handler-
 *     your GUI will modify the config object, and when it is closed it will trigger a OnConfigChangedEvent,
 *     which should call syncFromGUI().
 *
 * Usage:
 * (3) You can read the fields such as myInteger directly.
 * (4) If you modify the configuration fields, you can save them to disk using syncFromFields().
 * (5) To reload the values from disk, call syncFromFile().
 * (6) If you have used a GUI to alter the config values, call syncFromGUI()
 *     (If you called clientPreInit(), this will happen automatically).
 *
 *  => See ForgeModContainer for more examples.
 */
public class MBEConfiguration {

	// Declare all configuration fields used by the mod here
	public static int myInteger;
	public static boolean myBoolean;
	public static double myDouble;
	public static int[] myIntList;
	public static String myString;
	public static String myColour;

	public static final String CATEGORY_NAME_GENERAL = "category_general";
	public static final String CATEGORY_NAME_OTHER = "category_other";

	public static void preInit()
	{
		/*
		 * Here is where you specify the location from where your config file
		 * will be read, or created if it is not present.
		 *
		 * Loader.instance().getConfigDir() returns the default config directory
		 * and you specify the name of the config file, together this works
		 * similar to the old getSuggestedConfigurationFile() function.
		 */
		File configFile = new File(Loader.instance().getConfigDir(), "MinecraftByExample.cfg");

		// initialize your configuration object with your configuration file values.
		config = new Configuration(configFile);

		// load config from file (see mbe70 package for more info)
		syncFromFile();
	}

	public static void clientPreInit() {
		/*
		 * Register the save config handler to the Forge event bus, creates an
		 * instance of the static class ConfigEventHandler and has it listen on
		 * the core Forge event bus (see Notes and ConfigEventHandler for more information).
		 */
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	public static Configuration getConfig() {
		return config;
	}

	/**
	 * load the configuration values from the configuration file
	 */
	public static void syncFromFile() {
		syncConfig(true, true);
	}

	/**
	 * save the GUI-altered values to disk
	 */
	public static void syncFromGUI() {
		syncConfig(false, true);
	}

	/**
	 * save the MBEConfiguration variables (fields) to disk
	 */
	public static void syncFromFields() {
		syncConfig(false, false);
	}

	/**
	 * Synchronise the three copies of the data
	 * 1) loadConfigFromFile && readFieldsFromConfig -> initialise everything from the disk file.
	 * 2) !loadConfigFromFile && readFieldsFromConfig --> copy everything from the config file (altered by GUI).
	 * 3) !loadConfigFromFile && !readFieldsFromConfig --> copy everything from the native fields.
	 *
	 * @param loadConfigFromFile if true, load the config field from the configuration file on disk.
	 * @param readFieldsFromConfig if true, reload the member variables from the config field.
	 */

	private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig)
	{
		/*
		 * ---- step 1 - load raw values from config file (if loadFromFile true) -------------------
		 *
		 * Check if this configuration object is the main config file or a child
		 * configuration For simple configuration setups, this only matters if
		 * you enable global configuration for your configuration object by
		 * using config.enableGlobalConfiguration(), this will cause your config
		 * file to be 'global.cfg' in the default configuration directory and
		 * use it to read/write your configuration options
		 */
		if (loadConfigFromFile) {
			config.load();
		}

		/*
		 * Using language keys are a good idea if you are using a config GUI
		 * This allows you to provide "pretty" names for the config properties
		 * in a .lang file as well as allow others to provide other
		 * localizations for your mod.
		 *
		 * The language key is also used to get the tooltip for your property,
		 * the language key for each properties tooltip is langKey + ".tooltip"
		 * If no tooltip lang key is specified in a .lang file, it will default
		 * to the property's comment field.
		 *
		 * prop.setRequiresWorldRestart(true); and
		 * prop.setRequiresMcRestart(true); can be used to tell Forge if that
		 * specific property requires a world or complete Minecraft restart,
		 * respectively.
		 *
		 * Note: if a property requires a world restart it cannot be edited in
		 * the in-world mod settings (which hasn't been implemented yet by
		 * Forge), only when a world isn't loaded.
		 *
		 * -See the function definitions for more info
		 */

		/*
		 * ---- step 2 - define the properties in the configuration file -------------------
		 *
		 * The following code is used to define the properties in the
		 * configuration file: their name, type, default / min / max values, a
		 * comment. These affect what is displayed on the GUI. If the file
		 * already exists, the property values will already have been read from
		 * the file, otherwise they will be assigned the default value.
		 */

		// integer

		final int MY_INT_MIN_VALUE = 3;
		final int MY_INT_MAX_VALUE = 12;
		final int MY_INT_DEFAULT_VALUE = 10;
		Property propMyInt = config.get(CATEGORY_NAME_GENERAL, "myInteger", MY_INT_DEFAULT_VALUE,
				"Configuration integer (myInteger)", MY_INT_MIN_VALUE, MY_INT_MAX_VALUE);
		propMyInt.setLanguageKey("gui.mbe70_configuration.myInteger");

		// boolean
		final boolean MY_BOOL_DEFAULT_VALUE = true;
		Property propMyBool = config.get(CATEGORY_NAME_GENERAL, "myBoolean", MY_BOOL_DEFAULT_VALUE);
		propMyBool.setComment("Configuration boolean (myBoolean)");
		propMyBool.setLanguageKey("gui.mbe70_configuration.myBoolean").setRequiresMcRestart(true);

		// double
		final double MY_DOUBLE_MIN_VALUE = 0.0;
		final double MY_DOUBLE_MAX_VALUE = 1.0;
		final double MY_DOUBLE_DEFAULT_VALUE = 0.80;
		Property propMyDouble = config.get(CATEGORY_NAME_GENERAL, "myDouble", MY_DOUBLE_DEFAULT_VALUE,
				"Configuration double (myDouble)", MY_DOUBLE_MIN_VALUE, MY_DOUBLE_MAX_VALUE);
		propMyDouble.setLanguageKey("gui.mbe70_configuration.myDouble");

		// string
		final String MY_STRING_DEFAULT_VALUE = "default";
		Property propMyString = config.get(CATEGORY_NAME_GENERAL, "myString", MY_STRING_DEFAULT_VALUE);
		propMyString.setComment("Configuration string (myString)");
		propMyString.setLanguageKey("gui.mbe70_configuration.myString").setRequiresWorldRestart(true);

		// list of integer values
		final int[] MY_INT_LIST_DEFAULT_VALUE = new int[] { 1, 2, 3, 4, 5 };
		Property propMyIntList = config.get(CATEGORY_NAME_GENERAL, "myIntList", MY_INT_LIST_DEFAULT_VALUE,
				"Configuration integer list (myIntList)");
		propMyIntList.setLanguageKey("gui.mbe70_configuration.myIntList");

		// a string restricted to several choices - located on a separate category tab in the GUI
		final String COLOUR_DEFAULT_VALUE = "red";
		final String[] COLOUR_CHOICES = { "blue", "red", "yellow" };
		Property propColour = config.get(CATEGORY_NAME_OTHER, "myColour", COLOUR_DEFAULT_VALUE);
		propColour.setComment("Configuration string (myColour): blue, red, yellow");
		propColour.setLanguageKey("gui.mbe70_configuration.myColour").setRequiresWorldRestart(true);
		propColour.setValidValues(COLOUR_CHOICES);

		// By defining a property order we can control the order of the
		// properties in the config file and GUI. This is defined on a per config-category basis.

		List<String> propOrderGeneral = new ArrayList<String>();
		propOrderGeneral.add(propMyInt.getName()); // push the config value's name into the ordered list
		propOrderGeneral.add(propMyBool.getName());
		propOrderGeneral.add(propMyDouble.getName());
		propOrderGeneral.add(propMyString.getName());
		propOrderGeneral.add(propMyIntList.getName());
		config.setCategoryPropertyOrder(CATEGORY_NAME_GENERAL, propOrderGeneral);

		List<String> propOrderOther = new ArrayList<String>();
		propOrderOther.add(propColour.getName());
		config.setCategoryPropertyOrder(CATEGORY_NAME_OTHER, propOrderOther);

		/*
		 * ---- step 3 - read the configuration property values into the class's  -------------------
		 *               variables (if readFieldsFromConfig)
		 *
		 * As each value is read from the property, it should be checked to make
		 * sure it is valid, in case someone has manually edited or corrupted
		 * the value. The get() methods don't check that the value is in range
		 * even if you have specified a MIN and MAX value of the property.
		 */

		if (readFieldsFromConfig)
		{
			// If getInt() cannot get an integer value from the config file
			// value of myInteger (e.g. corrupted file).
			// It will set it to the default value passed to the function.

			myInteger = propMyInt.getInt(MY_INT_DEFAULT_VALUE);
			if (myInteger > MY_INT_MAX_VALUE || myInteger < MY_INT_MIN_VALUE) {
				myInteger = MY_INT_DEFAULT_VALUE;
			}

			myBoolean = propMyBool.getBoolean(MY_BOOL_DEFAULT_VALUE); // can also use a literal (see integer example) if desired

			myDouble = propMyDouble.getDouble(MY_DOUBLE_DEFAULT_VALUE);
			if (myDouble > MY_DOUBLE_MAX_VALUE || myDouble < MY_DOUBLE_MIN_VALUE) {
				myDouble = MY_DOUBLE_DEFAULT_VALUE;
			}

			myString = propMyString.getString();
			myIntList = propMyIntList.getIntList();

			myColour = propColour.getString();
			boolean matched = false;
			for (String entry : COLOUR_CHOICES) {
				if (entry.equals(myColour)) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				myColour = COLOUR_DEFAULT_VALUE;
			}
		}

		/*
		 * ---- step 4 - write the class's variables back into the config  -------------------
		 *               properties and save to disk
		 *
		 * This is done even for a 'loadFromFile==true', because some of the
		 * properties may have been assigned default values if the file was empty or corrupt.
		 */

		propMyInt.set(myInteger);
		propMyBool.set(myBoolean);
		propMyDouble.set(myDouble);
		propMyString.set(myString);
		propMyIntList.set(myIntList);
		propColour.set(myColour);

		if (config.hasChanged()) {
			config.save();
		}
	}

	// Define your configuration object
	private static Configuration config = null;

	public static class ConfigEventHandler
	{
		/*
	     * This class, when instantiated as an object, will listen on the Forge
	     * event bus for an OnConfigChangedEvent
	     */
		@SubscribeEvent(priority = EventPriority.NORMAL)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if (MinecraftByExample.MODID.equals(event.getModID()) && !event.isWorldRunning())
			{
				if (event.getConfigID().equals(CATEGORY_NAME_GENERAL) || event.getConfigID().equals(CATEGORY_NAME_OTHER))
				{
					syncFromGUI();
				}
			}
		}
	}
}
