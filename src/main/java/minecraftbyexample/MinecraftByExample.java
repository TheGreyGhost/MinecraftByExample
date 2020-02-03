package minecraftbyexample;

import minecraftbyexample.mbe01_block_simple.StartupClientOnly;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/*
   Each mod has a main class which is used by Forge to interact with the mod during startup.

   The major points:
   1) your mod is initialised in several phases.  Each phase is intended for different
      kinds of tasks your mod should do, such as registering new blocks for example.
      These phases occur in a defined order, and at each phase an Event is triggered.  You can use these Events to make
      sure your code gets called at the right time.
      How do you set this up?
      Register your function as a subscriber to the Event, on the correct Event bus (in this case - the ModEventBus).
      When the Event is triggered, it will call your function.
      There are basically three ways to do this; see the forge MDK ExampleMod for more details.
      I've just picked my favourite (MinecraftForge.EVENT_BUS.register() with @SubscribeEvent), to make it easier to split MBE into different (self-contained) examples.
      More information here: http://greyminecraftcoder.blogspot.com/2020/02/how-forge-starts-up-your-code-1144.html

   2) your mod may have to initialise itself differently depending on whether it is being run on a DedicatedServer or a CombinedClient distribution of Minecraft
     Some of the minecraft code is marked @OnlyIn(Dist.CLIENT), and this must not be called when
      the mod is installed in a dedicated server.  This is called ClientOnly code.  All the other code is Common code.
     (For more explanation, see here: http://greyminecraftcoder.blogspot.com/2020/02/the-client-server-division-1144.html).
     Generally speaking, this means that your mod will need to initialise itself in two different sections of code:
     1) code which needs to be called regardless of whether your mod is installed in a DedicatedServer or CombinedClient distribution.  This is usually related to game logic and
        any other things that the server needs to keep track of.
     2) code which is only relevant to the client distribution.  This is usually related to graphical rendering, user input, or similar.

      The basic order of events is
     CONSTRUCTION of the mod class
     CREATE_REGISTRIES
     LOAD_REGISTRIES  such as RegistryEvent.Register<Block> to register blocks.
     COMMON_SETUP (FMLCommonSetupEvent): code which is needed regardless of whether this is a DedicatedServer distribution or a CombinedClient distribution
     SIDED_SETUP --> either FMLDedicatedServerSetupEvent(for DedicatedServer distribution) or FMLClientSetupEvent (for CombinedClient distribution).
     INTERMOD COMMUNICATION (advanced stuff, don't worry about it until you've got more experience :))

     Most of the time, you'll only ever need the Registry events (to register your blocks, items, etc), the FMLCommonSetupEvent, and perhaps the FMLClientSetupEvent

 */

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinecraftByExample.MODID)
//@Mod(modid = )  //delete guiFactory if MBE70 not present and you don't have a configuration GUI
public class MinecraftByExample {
  // you also need to update the modid in two other places as well:
  //  build.gradle file (the version, group, and archivesBaseName parameters)
  //  resources/META-INF/mods.toml (the name, description, and version parameters)
  public static final String MODID = "minecraftbyexample";
//    public static final String GUIFACTORY = "minecraftbyexample.mbe70_configuration.MBEGuiFactory"; //delete if MBE70 not present

  public MinecraftByExample() {
    // The event bus will search these classes for methods which are interested in startup events
    //    (i.e. methods that are decorated with the @SubscribeEvent annotation)

    MinecraftForge.EVENT_BUS.register(minecraftbyexample.mbe01_block_simple.StartupClientOnly.class);


    MinecraftForge.EVENT_BUS.register(minecraftbyexample.mbe01_block_simple.StartupCommon.class);
  }
}
