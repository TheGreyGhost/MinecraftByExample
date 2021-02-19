package minecraftbyexample;

import net.minecraftforge.eventbus.api.IEventBus;

/*
    This class is required to make sure that we don't accidentally try to load any client-side-only classes
      on a dedicated server.
    It is a rather convoluted way of doing it, but I haven't found a simpler way to do it which is robust
 */

public class ClientSideOnlyModEventRegistrar {
    private final IEventBus eventBus;

    /**
     * @param eventBus an instance of the mod event bus
     */
    public ClientSideOnlyModEventRegistrar(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Register client only events. This method must only be called when it is certain that the mod is
     * is executing code on the client side and not the dedicated server.
     */
    public void registerClientOnlyEvents() {
        eventBus.register(minecraftbyexample.mbe01_block_simple.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe02_block_partial.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe03_block_variants.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe04_block_dynamic_block_models.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe05_block_advanced_models.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe06_redstone.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe10_item_simple.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe08_itemgroup.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe11_item_variants.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe12_item_nbt_animate.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe15_item_dynamic_item_model.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe20_tileentity_data.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe21_tileentityrenderer.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe30_inventory_basic.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe31_inventory_furnace.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe32_inventory_item.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe35_recipes.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe45_commands.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe50_particle.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe60_network_messages.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe65_capability.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe75_testing_framework.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe80_model_renderer.StartupClientOnly.class);
        eventBus.register(minecraftbyexample.mbe81_entity_projectile.StartupClientOnly.class);

        //----------------
        eventBus.register(minecraftbyexample.usefultools.debugging.StartupClientOnly.class);
    }
}
