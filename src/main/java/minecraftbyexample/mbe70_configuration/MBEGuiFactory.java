package minecraftbyexample.mbe70_configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class MBEGuiFactory implements IModGuiFactory 
{
	//this class is accessed when Forge needs a GUI made relating to your mod (e.g. config GUI)
	
	@Override
	public void initialize(Minecraft minecraftInstance) 
	{
		//needed to implement IModGuiFactory but doesn't need to do anything
		// for the configuration gui
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() 
	{
		return MBEConfigGui.class; //tells Forge which class represents our main GUI screen
	}
	
	//The following two functions are needed for implementation only, the config gui does not require them
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() 
	{
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) 
	{
		return null;
	}
	
	//This class inherits from GuiConfig, a specialized GuiScreen designed to display your
	// configuration categories
	public static class MBEConfigGui extends GuiConfig 
	{
		public MBEConfigGui(GuiScreen parentScreen) 
		{
			//I18n function basically "translates" or localizes the given key using the appropriate .lang file
			super(parentScreen, getConfigElements(), MinecraftByExample.MODID, false, false, I18n.format("gui.mbe70_configuration.mainTitle"));
		}
		
		private static List<IConfigElement> getConfigElements() 
		{
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			//Add the two buttons that will go to each config category edit screen
			list.add(new DummyCategoryElement("mainCfg", "gui.mbe70_configuration.ctgy.GeneralConfig", GeneralEntry.class));
			list.add(new DummyCategoryElement("miscCfg", "gui.mbe70_configuration.ctgy.MiscConfig", MiscEntry.class));
			return list;
		}
		
		//the next two classes are the edit screens for each configuration category
		public static class GeneralEntry extends CategoryEntry 
		{
			public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) 
			{
				super(owningScreen, owningEntryList, prop);
			}
			
			@Override
			protected GuiScreen buildChildScreen() 
			{
				//The following GuiConfig object specifies the configID of the object and thus will force-save
				// when closed.
				//Parent GuiConfig object's entryList will also be refreshed to reflect the changes.
				// --see GuiFactory of Forge for more info
				//Additionally, Forge best practices say to put the path to the config file for the category as
				// the title for the category config screen
				return new GuiConfig(this.owningScreen,
						(new ConfigElement(StartupCommon.getConfig().getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
						this.owningScreen.modID, Configuration.CATEGORY_GENERAL, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
						this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
						GuiConfig.getAbridgedConfigPath(StartupCommon.getConfig().toString()));
				//this is a complicated object that specifies the category's gui screen, to better understand
				// how it works, look into the definitions of the called functions and objects
			}
		}
		
		public static class MiscEntry extends CategoryEntry 
		{
			public MiscEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) 
			{
				super(owningScreen, owningEntryList, prop);
			}
			
			@Override
			protected GuiScreen buildChildScreen() 
			{
				//implement a category entry screen for the misc category here, following the example of the GeneralEntry
				// buildChildScreen()  function, other classes can be made for additional categories
				return null; //until the null is replaced, it will simply return to the Forge mod menu
			}
		}
	}
}
