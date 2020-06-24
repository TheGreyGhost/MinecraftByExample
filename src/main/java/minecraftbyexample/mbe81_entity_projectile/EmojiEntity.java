package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
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
public class EmojiEntity extends ProjectileItemEntity{
  public EmojiEntity(EntityType<? extends EmojiEntity> entityType, World world) {
    super(entityType, world);
  }

  public EmojiEntity(World world, LivingEntity livingEntity) {
    super(EntityType.SNOWBALL, livingEntity, world);
  }

  public EmojiEntity(World world, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
    super(EntityType.SNOWBALL, p_i1775_2_, p_i1775_4_, p_i1775_6_, world);
  }

  protected Item getDefaultItem() {
    return StartupCommon.emojiItem;
  }

  private IParticleData makeParticle() {
    ItemStack lvt_1_1_ = this.func_213882_k();
    return (IParticleData)(lvt_1_1_.isEmpty()? ParticleTypes.ITEM_SNOWBALL:new ItemParticleData(ParticleTypes.ITEM, lvt_1_1_));
  }

  /*
  https://wiki.vg/Entity_statuses
   */

  public void handleStatusUpdate(byte p_70103_1_) {
    if(p_70103_1_ == 3) {
      IParticleData lvt_2_1_ = this.makeParticle();

      for(int lvt_3_1_ = 0; lvt_3_1_ < 8; ++lvt_3_1_) {
        this.world.addParticle(lvt_2_1_, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
      }
    }

  }

  protected void onImpact(RayTraceResult p_70184_1_) {
    if(p_70184_1_.getType() == RayTraceResult.Type.ENTITY) {
      Entity lvt_2_1_ = ((EntityRayTraceResult)p_70184_1_).getEntity();
      int lvt_3_1_ = lvt_2_1_ instanceof BlazeEntity ?3:0;
      lvt_2_1_.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)lvt_3_1_);
    }

    if(!this.world.isRemote) {
      this.world.setEntityState(this, (byte)3);
      this.remove();
    }

  }

}
