package minecraftbyexample.mbe81_entity_projectile;

import minecraftbyexample.mbe10_item_simple.ItemSimple;
import minecraftbyexample.mbe50_particle.FlameParticleType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
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
  public static EmojiItem emojiItem;  // this holds the unique instance of your block
  public static EntityType<EmojiEntity> emojiEntityType;

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    emojiItem = new EmojiItem();
    emojiItem.setRegistryName("mbe81a_emoji_registry_name");
    itemRegisterEvent.getRegistry().register(emojiItem);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }

  @SubscribeEvent
  public static void onEntityTypeRegistration(RegistryEvent.Register<EntityType<?>> entityTypeRegisterEvent) {
    EntityType<EmojiEntity> emojiEntityType = EntityType.Builder.<EmojiEntity>create(EmojiEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .build("minecraftbyexample:mbe81_emoji_type_registry_name");
    emojiEntityType.setRegistryName("minecraftbyexample:mbe81_emoji_type_registry_name");
    entityTypeRegisterEvent.getRegistry().register(emojiEntityType);
  }


}
