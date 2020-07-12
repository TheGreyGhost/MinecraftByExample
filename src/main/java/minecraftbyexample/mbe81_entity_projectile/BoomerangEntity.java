package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by TGG on 24/06/2020.
 *
 * Heavily based on the vanilla SnowballEntity
 */
public class BoomerangEntity extends Entity {
  public BoomerangEntity(EntityType<? extends BoomerangEntity> entityType, World world) {
    super(entityType, world);
  }

  public BoomerangEntity(World world, LivingEntity livingEntity) {
    super(StartupCommon.boomerangEntityType, livingEntity, world);
  }

  public BoomerangEntity(World world, double x, double y, double z) {
    super(StartupCommon.boomerangEntityType, x, y, z, world);
  }

  protected void registerData() {
    this.getDataManager().register(IN_FLIGHT, Boolean.TRUE);
  }

    // Is this boomerang in flight?  (true = yes, false = no (it has hit something))
  private static final DataParameter<Boolean> IN_FLIGHT =
          EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BOOLEAN);

  protected LivingEntity owner;
  private UUID ownerId;

  // If you forget to override this method, the default vanilla method will be called.
  // This sends a vanilla spawn packet, which is then silently discarded when it reaches the client.
  //  Your entity will be present on the server and can cause effects, but the client will not have a copy of the entity
  //    and hence it will not render.
  @Nonnull
  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }


  // We hit something (entity or block).
  @Override
  protected void onImpact(RayTraceResult rayTraceResult) {
    // if we hit an entity, apply an effect to it depending on the emoji mood
    if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
      EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
      Entity entity = entityRayTraceResult.getEntity();
      if (entity instanceof LivingEntity) {
        LivingEntity livingEntity = (LivingEntity)entity;
        Optional<EmojiItem.EmojiMood> mood = getMoodFromMyItem();
        if (mood.isPresent()) {
          EffectInstance effect = (mood.get() == EmojiItem.EmojiMood.HAPPY) ?
                  new EffectInstance(Effects.REGENERATION, 100, 1) :
                  new EffectInstance(Effects.POISON, 10, 0);
          livingEntity.addPotionEffect(effect);
        }
      }
    }

    if (!this.world.isRemote) {
      this.world.setEntityState(this, VANILLA_IMPACT_STATUS_ID);  // calls handleStatusUpdate which tells the client to render particles
      this.remove();
    }
  }


  private static final byte VANILLA_IMPACT_STATUS_ID = 3;

  /*   see https://wiki.vg/Entity_statuses
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

  private IParticleData makeParticle() {
    return (mood.get() == EmojiItem.EmojiMood.HAPPY) ? ParticleTypes.HEART : ParticleTypes.ANGRY_VILLAGER;
  }

  public void writeAdditional(CompoundNBT compound) {
    if (this.ownerId != null) {
      compound.put("owner", NBTUtil.writeUniqueId(this.ownerId));
    }
  }

  /**
   * (abstract) Protected helper method to read subclass entity data from NBT.
   */
  public void readAdditional(CompoundNBT compound) {
    this.owner = null;
    if (compound.contains("owner", 10)) {
      this.ownerId = NBTUtil.readUniqueId(compound.getCompound("owner"));
    }
  }

  @Nullable
  public LivingEntity getThrower() {
    if ((this.owner == null || this.owner.removed) && this.ownerId != null && this.world instanceof ServerWorld) {
      Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.ownerId);
      if (entity instanceof LivingEntity) {
        this.owner = (LivingEntity)entity;
      } else {
        this.owner = null;
      }
    }

    return this.owner;
  }


  /**
   * Updates the entity motion clientside, called by packets from the server
   */
  @OnlyIn(Dist.CLIENT)
  public void setVelocity(double x, double y, double z) {
    this.setMotion(x, y, z);
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt(x * x + z * z);
      this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
      this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI));
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
    }

  }

  /**
   * Called to update the entity's position/logic.
   */
  public void tick() {
    super.tick();

    AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion()).grow(1.0D);

    for(Entity entity : this.world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_213881_0_) -> {
      return !p_213881_0_.isSpectator() && p_213881_0_.canBeCollidedWith();
    })) {
      if (entity == this.ignoreEntity) {
        ++this.ignoreTime;
        break;
      }

      if (this.owner != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
        this.ignoreEntity = entity;
        this.ignoreTime = 3;
        break;
      }
    }

    RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, axisalignedbb, (p_213880_1_) -> {
      return !p_213880_1_.isSpectator() && p_213880_1_.canBeCollidedWith() && p_213880_1_ != this.ignoreEntity;
    }, RayTraceContext.BlockMode.OUTLINE, true);
    if (this.ignoreEntity != null && this.ignoreTime-- <= 0) {
      this.ignoreEntity = null;
    }

    if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
      if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && this.world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()).getBlock() == Blocks.NETHER_PORTAL) {
        this.setPortal(((BlockRayTraceResult)raytraceresult).getPos());
      } else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
        this.onImpact(raytraceresult);
      }
    }

    Vec3d vec3d = this.getMotion();
    double d0 = this.getPosX() + vec3d.x;
    double d1 = this.getPosY() + vec3d.y;
    double d2 = this.getPosZ() + vec3d.z;
    float f = MathHelper.sqrt(horizontalMag(vec3d));
    this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));

    for(this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
      ;
    }

    while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
      this.prevRotationPitch += 360.0F;
    }

    while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
      this.prevRotationYaw -= 360.0F;
    }

    while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
      this.prevRotationYaw += 360.0F;
    }

    this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
    this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
    float f1;
    if (this.isInWater()) {
      for(int i = 0; i < 4; ++i) {
        float f2 = 0.25F;
        this.world.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
      }

      f1 = 0.8F;
    } else {
      f1 = 0.99F;
    }

    this.setMotion(vec3d.scale((double)f1));
    if (!this.hasNoGravity()) {
      Vec3d vec3d1 = this.getMotion();
      this.setMotion(vec3d1.x, vec3d1.y - (double)this.getGravityVelocity(), vec3d1.z);
    }

    this.setPosition(d0, d1, d2);
  }

  /**
   * Gets the amount of gravity to apply to the thrown entity with each tick.
   */
  protected float getGravityVelocity() {
    return 0.03F;
  }


}
