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
  public static EmojiItem emojiItemHappy;
  public static EmojiItem emojiItemGrumpy;
  public static EntityType<EmojiEntity> emojiEntityType;

  public static BoomerangItem boomerangItem;
  public static EntityType<BoomerangEntity> boomerangEntityType;

  // register the two different items- they both use the same class but each have a different mood
  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    emojiItemHappy = new EmojiItem(EmojiItem.EmojiMood.HAPPY);
    emojiItemHappy.setRegistryName("mbe81a_emoji_happy_registry_name");
    itemRegisterEvent.getRegistry().register(emojiItemHappy);

    emojiItemGrumpy = new EmojiItem(EmojiItem.EmojiMood.GRUMPY);
    emojiItemGrumpy.setRegistryName("mbe81a_emoji_grumpy_registry_name");
    itemRegisterEvent.getRegistry().register(emojiItemGrumpy);

    boomerangItem = new BoomerangItem();
    boomerangItem.setRegistryName("mbe81b_boomerang_registry_name");
    itemRegisterEvent.getRegistry().register(boomerangItem);
  }

  // register our entity type
  @SubscribeEvent
  public static void onEntityTypeRegistration(RegistryEvent.Register<EntityType<?>> entityTypeRegisterEvent) {
    emojiEntityType = EntityType.Builder.<EmojiEntity>create(EmojiEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .build("minecraftbyexample:mbe81a_emoji_type_registry_name");
    emojiEntityType.setRegistryName("minecraftbyexample:mbe81a_emoji_type_registry_name");
    entityTypeRegisterEvent.getRegistry().register(emojiEntityType);

    boomerangEntityType = EntityType.Builder.<BoomerangEntity>create(BoomerangEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .build("minecraftbyexample:mbe81b_boomerang_type_registry_name");
    boomerangEntityType.setRegistryName("minecraftbyexample:mbe81b_boomerang_type_registry_name");
    entityTypeRegisterEvent.getRegistry().register(boomerangEntityType);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}
