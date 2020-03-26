package minecraftbyexample.mbe50_particle;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockFlameEmitter blockFlameEmitter;  // this holds the unique instance of your block
  public static BlockItem itemBlockFlameEmitter;  // the itemBlock corresponding to the block
  public static ParticleType<FlameParticleData> flameParticleType;


  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockFlameEmitter = (BlockFlameEmitter)(new BlockFlameEmitter().setRegistryName("mbe50_block_flame_emitter_registry_name"));
    blockRegisterEvent.getRegistry().register(blockFlameEmitter);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 1;  // player can only hold 1 of this block in their hand at once

    Item.Properties itemFlameEmitterProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.BUILDING_BLOCKS);  // which inventory tab?
    itemBlockFlameEmitter = new BlockItem(blockFlameEmitter, itemFlameEmitterProperties);
    itemBlockFlameEmitter.setRegistryName(blockFlameEmitter.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockFlameEmitter);
  }

  @SubscribeEvent
  public static void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> iParticleTypeRegisterEvent) {
    flameParticleType = new FlameParticleType();
    flameParticleType.setRegistryName("minecraftbyexample:mbe50_flame_particle_type_registry_name");
    iParticleTypeRegisterEvent.getRegistry().register(flameParticleType);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }

}
