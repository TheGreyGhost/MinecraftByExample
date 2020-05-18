package minecraftbyexample;

import minecraftbyexample.usefultools.debugging.ForgeLoggerTweaker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;

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
      I've just picked my favourite (@SubscribeEvent), to make it easier to split MBE into different (self-contained) examples.
      More information here: http://greyminecraftcoder.blogspot.com/2020/02/how-forge-starts-up-your-code-1144.html

   2) your mod may have to initialise itself differently depending on whether it is being run on a DedicatedServer or a CombinedClient distribution of Minecraft
     Some of the minecraft code is marked @OnlyIn(Dist.CLIENT), and this must not be called when
      the mod is installed in a dedicated server.  This is called ClientOnly code.  All the other code is Common code.
     (For more explanation, see here: http://greyminecraftcoder.blogspot.com/2020/02/the-client-server-division-1144.html).
     Generally speaking, this means that your mod will need to initialise itself in two different sections of code:
     1) code which needs to be called regardless of whether your mod is installed in a DedicatedServer or CombinedClient distribution.  This is usually related to game logic and
        any other things that the server needs to keep track of.
     2) code which is only relevant to the client distribution.  This is usually related to graphical rendering, user input, or similar.  You can ensure that this code is
        only initialised on the client by using DistExecutor.runWhenOn(Dist.CLIENT, () -> MinecraftByExample::registerClientOnlyEvents);

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
public class MinecraftByExample {
  // you also need to update the modid in two other places as well:
  //  build.gradle file (the version, group, and archivesBaseName parameters)
  //  resources/META-INF/mods.toml (the name, description, and version parameters)
  public static final String MODID = "minecraftbyexample";

  // get a reference to the event bus for this mod;  Registration events are fired on this bus.
  public static IEventBus MOD_EVENT_BUS;

  public MinecraftByExample() {
    final boolean HIDE_CONSOLE_NOISE = false;  // todo get rid of all the noise from the console (after mod is constructed) to show warnings more clearly.
    if (HIDE_CONSOLE_NOISE) {
      ForgeLoggerTweaker.setMinimumLevel(Level.WARN);
      ForgeLoggerTweaker.applyLoggerFilter();
    }

    MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    // The event bus register method is used to specify classes.
    // These classes will be searched for methods which are interested in startup events
    //    (i.e. methods that are decorated with the @SubscribeEvent annotation)

    // Beware - there are two event busses: the MinecraftForge.EVENT_BUS, and your own ModEventBus.
    //  If you subscribe your event to the wrong bus, it will never get called.
    // likewise, beware of the difference between static and non-static methods, i.e.
    //  If you register a class, but the @SubscribeEvent is on a non-static method, it won't be called.  e.g.
    //  MOD_EVENT_BUS.register(MyClass.class);
    //  public class ServerLifecycleEvents {
    //    @SubscribeEvent
    //      public void onServerStartingEvent(FMLServerStartingEvent event) { // missing static! --> never gets called}
    //  }

    // Based on my testing: ModEventBus is used for setup events only, in the following order:
    // * RegistryEvent of all types
    // * ColorHandlerEvent for blocks & items
    // * ParticleFactoryRegisterEvent
    // * FMLCommonSetupEvent
    // * TextureStitchEvent
    // * FMLClientSetupEvent or FMLDedicatedServerSetupEvent
    // * ModelRegistryEvent
    // * Other ModLifecycleEvents such as InterModEnqueueEvent, InterModProcessEvent
    // ModelBakeEvent

    // We need to split the registration of events into:
    // 1) "Common" events that are executed on a dedicated server and also on an integrated client + server installation
    // 2) "Client only" events that are not executed on a dedicated server.
    // If you aren't careful to split these into two parts, your mod will crash when installed on a dedicated server
    // It doesn't matter if your client-only code is never actually called; simply referencing the class is often enough to
    //   cause a crash.

    registerCommonEvents();
    DistExecutor.runWhenOn(Dist.CLIENT, () -> MinecraftByExample::registerClientOnlyEvents);
  }

  public static void registerCommonEvents() {
    MOD_EVENT_BUS.register(minecraftbyexample.mbe01_block_simple.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe02_block_partial.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe03_block_variants.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe04_block_dynamic_block_models.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe05_block_advanced_models.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe10_item_simple.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe08_itemgroup.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe11_item_variants.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe12_item_nbt_animate.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe15_item_dynamic_item_model.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe20_tileentity_data.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe21_tileentityrenderer.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe30_inventory_basic.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe31_inventory_furnace.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe45_commands.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe50_particle.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe60_network_messages.StartupCommon.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe80_model_renderer.StartupCommon.class);

    //----------------
    MOD_EVENT_BUS.register(minecraftbyexample.usefultools.debugging.StartupCommon.class);
  }

  public static void registerClientOnlyEvents() {
    MOD_EVENT_BUS.register(minecraftbyexample.mbe01_block_simple.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe02_block_partial.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe03_block_variants.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe04_block_dynamic_block_models.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe05_block_advanced_models.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe10_item_simple.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe08_itemgroup.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe11_item_variants.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe15_item_dynamic_item_model.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe21_tileentityrenderer.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe45_commands.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe50_particle.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe60_network_messages.StartupClientOnly.class);
    MOD_EVENT_BUS.register(minecraftbyexample.mbe80_model_renderer.StartupClientOnly.class);

    //----------------
    MOD_EVENT_BUS.register(minecraftbyexample.usefultools.debugging.StartupClientOnly.class);
  }

}
