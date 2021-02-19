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
        only initialised on the client by using DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> eventRegistrar::registerClientOnlyEvents);

   3) Some of the events are called in parallel threads.  This means that your code in these events must not call non-thread-safe code; for example
       BrewingRecipeRegistry::addRecipe  or ItemModelsProperties registering events;  if multiple threads try to call addRecipe at the same time, it will occasionally cause data
         corruption or a game crash.
       Instead, use DeferredWordQueue::enqueueWork to register a function for synchronous execution in the main thread after the parallel processing is completed
       The Events affected by this are those which extend ParallelDispatchEvent (in particular FMLClientSetupEvent and FMLCommonSetupEvent)

   4) The basic order of events that your mod receives during startup is
     CONSTRUCTION of the mod class
     CREATE_REGISTRIES such as RegistryEvent.NewRegistry event (not commonly used)
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

  public MinecraftByExample() {
    final boolean HIDE_CONSOLE_NOISE = false;  // todo get rid of all the noise from the console (after mod is constructed) to show warnings more clearly.
    if (HIDE_CONSOLE_NOISE) {
      ForgeLoggerTweaker.setMinimumLevel(Level.WARN);
      ForgeLoggerTweaker.applyLoggerFilter();
    }

    // Get an instance of the mod event bus
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Get an instance of the event registrar that is used to bind events to the mod event bus
    // this is is a separate class to allow us to use `safeRunWhenOn` instead of the deprecated
    // `runWhenOn` method on the DistExecuter.
    final ClientSideOnlyModEventRegistrar clientSideOnlyModEventRegistrar = new ClientSideOnlyModEventRegistrar(modEventBus);

    // The event bus register method is used to specify classes used for receiving startup events:
    // The classes you register will be searched for methods which are interested in startup events
    //    (i.e. methods that are decorated with the @SubscribeEvent annotation)

    // Beware - there are two event busses: the MinecraftForge.EVENT_BUS, and your own ModEventBus.
    //  If you subscribe your event to the wrong bus, it will never get called.
    // likewise, beware of the difference between static and non-static methods, i.e.
    //  If you register a class, but the @SubscribeEvent is on a non-static method, it won't be called.  e.g.
    //  eventBus.register(MyClass.class);
    //  public class ServerLifecycleEvents {
    //    @SubscribeEvent
    //      public void onServerStartingEvent(FMLServerStartingEvent event) { // missing static! --> never gets called}
    //  }

    // Based on my testing: ModEventBus is used for setup events only, in the following order:
    // * RegistryEvent of all types
    // * ColorHandlerEvent for blocks & items
    // * ParticleFactoryRegisterEvent
    // * FMLCommonSetupEvent (multithreaded)
    // * TextureStitchEvent
    // * FMLClientSetupEvent or FMLDedicatedServerSetupEvent (multithreaded)
    // * ModelRegistryEvent
    // * Other ModLifecycleEvents such as InterModEnqueueEvent, InterModProcessEvent (multithreaded)
    // ModelBakeEvent

    // We need to split the registration of events into:
    // 1) "Common" events that are executed on a dedicated server and also on an integrated client + server installation
    // 2) "Client only" events that are not executed on a dedicated server.
    // If you aren't careful to split these into two parts, your mod will crash when installed on a dedicated server
    // It doesn't matter if your client-only code is never actually called; simply referencing the class is often enough to
    //   cause a crash.  I have also heard that the behaviour depends on the particular implementation of the Java Virtual
    //   Machine (for example Windows vs Linux), so you can't necessarily rely on testing to be sure it works.
    //   See the comments in DistExecutor class for more context.
    //  This is the reason that the ClientOnlyEvents are split into a completely-separate class.

    registerCommonEvents(modEventBus);
    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientSideOnlyModEventRegistrar::registerClientOnlyEvents);
  }


  /**
   * Register common events for both dedicated servers and clients. This method is safe to call directly.
   */
  public void registerCommonEvents(IEventBus eventBus) {
    eventBus.register(minecraftbyexample.mbe01_block_simple.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe02_block_partial.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe03_block_variants.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe04_block_dynamic_block_models.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe05_block_advanced_models.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe06_redstone.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe10_item_simple.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe08_itemgroup.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe11_item_variants.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe12_item_nbt_animate.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe15_item_dynamic_item_model.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe20_tileentity_data.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe21_tileentityrenderer.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe30_inventory_basic.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe31_inventory_furnace.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe32_inventory_item.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe35_recipes.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe45_commands.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe50_particle.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe60_network_messages.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe65_capability.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe75_testing_framework.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe80_model_renderer.StartupCommon.class);
    eventBus.register(minecraftbyexample.mbe81_entity_projectile.StartupCommon.class);

    //----------------
    eventBus.register(minecraftbyexample.usefultools.debugging.StartupCommon.class);
  }

}
