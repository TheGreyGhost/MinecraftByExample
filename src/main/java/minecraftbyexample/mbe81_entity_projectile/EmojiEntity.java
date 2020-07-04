package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by TGG on 24/06/2020.
 *
 * Heavily based on the vanilla SnowballEntity
 */
public class EmojiEntity extends ProjectileItemEntity {
  public EmojiEntity(EntityType<? extends EmojiEntity> entityType, World world) {
    super(entityType, world);
  }

  public EmojiEntity(World world, LivingEntity livingEntity) {
    super(StartupCommon.emojiEntityType, livingEntity, world);
  }

  public EmojiEntity(World world, double x, double y, double z) {
    super(StartupCommon.emojiEntityType, x, y, z, world);
  }

  @Override
  protected Item getDefaultItem() {
    return StartupCommon.emojiItemHappy;
  }

  private IParticleData makeParticle() {
    ItemStack itemStackForThisEntity = this.func_213882_k();
    return (itemStackForThisEntity.isEmpty() ? ParticleTypes.SNEEZE : new ItemParticleData(ParticleTypes.ITEM, itemStackForThisEntity));
    UPDATE TO BE EITHER HEARTS OR ANGRY VILLAGER
  }

  private static final byte VANILLA_IMPACT_STATUS_ID = 3;
  /*
       see https://wiki.vg/Entity_statuses
       make a cloud of particles at the impact point
   */
  @Override
  public void handleStatusUpdate(byte statusID) {
    if (statusID == VANILLA_IMPACT_STATUS_ID) {
      IParticleData particleData = this.makeParticle();

      for(int i = 0; i < 8; ++i) {
        this.world.addParticle(particleData, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  protected void onImpact(RayTraceResult rayTraceResult) {

    // if we hit an entity, apply an effect to it depending on the emoji mood

    if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
      EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
      Entity entity = entityRayTraceResult.getEntity();
      if (entity instanceof LivingEntity) {
        LivingEntity livingEntity = (LivingEntity)entity;
        ItemStack itemStackForThisEntity = this.func_213882_k();
        Item item = itemStackForThisEntity.getItem();
        if (item instanceof EmojiItem) {
          EmojiItem.EmojiMood mood = ((EmojiItem)item).getEmojiMood();
          EffectInstance effect = (mood == EmojiItem.EmojiMood.HAPPY) ?
                  new EffectInstance(Effects.REGENERATION, 100, 1) :
                  new EffectInstance(Effects.POISON, 5, 0);
          livingEntity.addPotionEffect(effect);
        }
      }
    }

    if (!this.world.isRemote) {
      this.world.setEntityState(this, VANILLA_IMPACT_STATUS_ID);  // calls handleStatusUpdate which tells the client to render particles
      this.remove();
    }
  }
}
