package minecraftbyexample;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
   Each mod has a main class which is used by Forge to interact with the mod during startup.
   For more background information see here http://greyminecraftcoder.blogspot.com/2013/11/how-forge-starts-up-your-code.html

   The major points:
   1) your mod is initialised in 3 phases - first preInit, then init, then postInit.  Each phase is intended for different
      kinds of tasks your mod should do, such as registering new blocks for example.
   2) CommonProxy, ClientOnlyProxy, and DedicatedServerProxy are used to make sure your mod works correctly when installed on
      a dedicated server.  Some of the minecraft code is marked @SideOnly(Side.CLIENT), and this must not be called when
      installed in a dedicated server.  This is called ClientOnly code.  All the other code is Common code.
      Each example has two Startup classes, StartupCommon and StartupClientOnly with methods which are called during the mod startup,
       in the following order:
     *  StartupCommon.preInitCommon
     *  StartupClientOnly.preInitClientOnly
     *  StartupCommon.initCommon
     *  StartupClientOnly.initClientOnly
     *  StartupCommon.postInitCommon
     *  StartupClientOnly.postInitClientOnly

   The basic rule for maintaining compatibility with DedicatedServer is-
   Classes which might be called by DedicatedServer code must not contain any mention of Vanilla Client-Side-Only classes.
   Due to the way the Java classloader works with Forge, the presence of a vanilla client-side-only class is often enough to
   cause a crash even if you never create an instance of it.

   For example, this code will not work on dedicated server:

   public class StartupCommon {
      public static void preInitCommon() {
        GameRegistry.registerTileEntity(TileEntityMBE21.class, "mbe21_tesr_te");
      }

      public static void initClientOnly() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMBE21.class, new TileEntitySpecialRendererMBE21());
      }
    }

    it will cause the following error message when preInitCommon() is called:
    java.lang.NoClassDefFoundError: net/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer
  	at minecraftbyexample.CommonProxy.preInit(CommonProxy.java:25) ~[CommonProxy.class:?]
     Caused by: java.lang.RuntimeException: Attempted to load class net/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer for invalid side SERVER
	   at net.minecraftforge.fml.common.asm.transformers.SideTransformer.transform(SideTransformer.java:49) ~[forgeSrc-1.8-11.14.0.1285-1.8.jar:?]

   The reason is that, when calling preInitCommon, the DedicatedServer loads StartupCommon and finds the TileEntitySpecialRendererMBE21,
     goes to look for its superclass TileEntitySpecialRenderer.  It can't find it, because the DedicatedServer doesn't have it.
   The initClientOnly method must be moved to a different class StartupClientOnly, which is never loaded in the DedicatedServer at all.
 */

@Mod(modid = MinecraftByExample.MODID, version = MinecraftByExample.VERSION,
     guiFactory= MinecraftByExample.GUIFACTORY)  //delete guiFactory if MBE70 not present and you don't have a configuration GUI
public class MinecraftByExample
{
  // you also need to update the modid and version in two other places as well:
  //  build.gradle file (the version, group, and archivesBaseName parameters)
  //  resources/mcmod.info (the name, description, and version parameters)
   public static final String MODID = "minecraftbyexample";
    public static final String VERSION = "1.12.2a";

    public static final String GUIFACTORY = "minecraftbyexample.mbe70_configuration.MBEGuiFactory"; //delete if MBE70 not present

    // The instance of your mod that Forge uses.  Optional.
    @Mod.Instance(MinecraftByExample.MODID)
    public static MinecraftByExample instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="minecraftbyexample.ClientOnlyProxy", serverSide="minecraftbyexample.DedicatedServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
      proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
      proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
      proxy.postInit();
    }

    /**
     * Prepend the name with the mod ID, suitable for ResourceLocations such as textures.
     * @param name
     * @return eg "minecraftbyexample:myblockname"
     */
    public static String prependModID(String name) {return MODID + ":" + name;}
}
