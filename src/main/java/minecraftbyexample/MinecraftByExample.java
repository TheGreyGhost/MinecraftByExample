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
      Each example has a Startup class with methods which are called during the mod startup, in the following order
     *  preInitCommon
     *  preInitClientOnly
     *  initCommon
     *  initClientOnly
     *  postInitCommon
     *  postInitClientOnly
 */

@Mod(modid = MinecraftByExample.MODID, version = MinecraftByExample.VERSION)
public class MinecraftByExample
{
    public static final String MODID = "minecraftbyexample";
    public static final String VERSION = "1.0";

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
