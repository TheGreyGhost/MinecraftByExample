package minecraftbyexample.mbe81_entity_projectile;

import minecraftbyexample.usefultools.NBTtypesMBE;
import minecraftbyexample.usefultools.SetBlockStateFlag;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by TGG on 24/06/2020.
 *
 * Heavily based on the vanilla SnowballEntity, ThrowableEntity, and AbstractArrowEntity
 */
public class BoomerangEntity extends Entity implements IEntityAdditionalSpawnData {
  public BoomerangEntity(EntityType<? extends BoomerangEntity> entityType, World world) {
    super(entityType, world);
  }

  /** Create a new BoomerangEntity
   * @param boomerangItemStack the boomerang item being thrown
   * @param startPosition  the spawn point of the flight path
   * @param apexYaw the yaw angle in degrees (compass direction of the apex relative to the thrower).  0 degrees is south and increases clockwise.
   * @param apexPitch the pitch angle in degrees (elevation/declination of the apex relative to the thrower).  0 degrees is horizontal: -90 is up, 90 is down.
   * @param distanceToApex number of blocks to the apex of the flight path
   * @param maximumSidewaysDeflection maximum sideways deflection from the straight line from thrower to apex
   * @param rightHandThrown was thrown using right hand?
   * @param flightSpeed speed of the flight in blocks per second
   */
  public BoomerangEntity(World world, ItemStack boomerangItemStack,
                         @Nullable LivingEntity thrower,
                         Vec3d startPosition,
                         float apexYaw, float apexPitch,
                         float distanceToApex,
                         float maximumSidewaysDeflection,
                         boolean rightHandThrown,
                         float flightSpeed) {
    super(StartupCommon.boomerangEntityType, world);
    this.thrower = thrower;
    if (thrower != null) {
      throwerID = thrower.getUniqueID();
    }
    // datamanager parameters are automatically synchronised to the client

    this.setPosition(startPosition.getX(), startPosition.getY(), startPosition.getZ());
    dataManager.set(ITEMSTACK, boomerangItemStack);
    dataManager.set(IN_FLIGHT, true);
    boomerangFlightPath = new BoomerangFlightPath(startPosition, apexYaw, apexPitch, distanceToApex,
            maximumSidewaysDeflection, rightHandThrown, flightSpeed);
    this.rightHandThrown = rightHandThrown;
    remainingMomentum = 1.0F;
    copyEnchantmentData(boomerangItemStack);
    rotationYaw = boomerangFlightPath.getYaw(0);
    rotationPitch = -90;  // the model has its flat face point up, but during flight it needs to be pointing sideways, which
                          // corresponds to pitching it up by 90 degrees.
    endOverEndRotation = 0;
    prevRotationYaw = rotationYaw;
    prevRotationPitch = rotationPitch;
    prevEndOverEndRotation = endOverEndRotation;
  }

  /**
   * Copy relevant enchantments into member variables for ease of reference
   * @param boomerangItemStack
   */
  private void copyEnchantmentData(ItemStack boomerangItemStack) {
    boolean isEnchanted = boomerangItemStack.isEnchanted();

    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(boomerangItemStack);
    float knockback1 = enchantments.getOrDefault(Enchantments.KNOCKBACK, 0) / (float)Enchantments.KNOCKBACK.getMaxLevel();
    float knockback2 = enchantments.getOrDefault(Enchantments.PUNCH, 0) / (float)Enchantments.PUNCH.getMaxLevel();
    knockbackLevel = Math.max(knockback1, knockback2);
    float fire1 = 2* enchantments.getOrDefault(Enchantments.FLAME, 0) / (float)Enchantments.FLAME.getMaxLevel();
    float fire2 = enchantments.getOrDefault(Enchantments.FIRE_ASPECT, 0) / (float)Enchantments.FIRE_ASPECT.getMaxLevel();
    flameLevel = Math.max(fire1, fire2);
    damageBoostLevel = enchantments.getOrDefault(Enchantments.POWER, 0) / (float)Enchantments.POWER.getMaxLevel();

    // add any "special damage" enchantment types to a dedicated list
    List<Enchantment> specialDamageEnchantmentTypes = Arrays.asList(Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS);
    specialDamages = enchantments.entrySet().stream()
            .filter(x -> specialDamageEnchantmentTypes.contains(x.getKey()))
            .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

    efficiencyLevel = enchantments.getOrDefault(Enchantments.EFFICIENCY, 0)/ (float)Enchantments.EFFICIENCY.getMaxLevel();
    // silkTouch and fortuneLevel are implemented using vanilla directly on the enchantment tags, so we don't need to extract them ourselves
//    silkTouch = 0 != enchantments.getOrDefault(Enchantments.SILK_TOUCH, 0);
//    fortuneLevel = enchantments.getOrDefault(Enchantments.FORTUNE, 0)/ (float)Enchantments.FORTUNE.getMaxLevel();
  }

  protected void registerData() {
    this.getDataManager().register(IN_FLIGHT, Boolean.TRUE);
    this.getDataManager().register(ITEMSTACK, ItemStack.EMPTY);
//    this.getDataManager().register(TICKS_SPENT_IN_FLIGHT, 0);
  }

    // Is this boomerang in flight?  (true = yes, false = no (it has hit something and is now acting like a discarded item))
  private static final DataParameter<Boolean> IN_FLIGHT =
          EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BOOLEAN);
    // What ItemStack was used to throw the boomerang?
  private static final DataParameter<ItemStack> ITEMSTACK = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.ITEMSTACK);

//  private static final DataParameter<Integer> TICKS_SPENT_IN_FLIGHT = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.VARINT);

  protected LivingEntity thrower;
  private UUID throwerID = null;

  // member variables that need to be saved to record its state
  private BoomerangFlightPath boomerangFlightPath = new BoomerangFlightPath();  //dummy
  private boolean rightHandThrown = false;  // which hand was used to throw?
  private int pickupDelay = 0;        // delay until a non-in-flight boomerang can be picked up (in ticks)
  private int ticksSpentInFlight = 0;
  private int ticksSpentNotInFlight = 0;
  private double damage = 2.0D;
  private float remainingMomentum = 1.0F;  // the fraction of momentum remaining - reduced when harvesting blocks.
                                           // the boomerang stops flying when this is reduced to zero
  private float endOverEndRotation = 0.0F;
  private float prevEndOverEndRotation = 0.0F;

  // member variables that are regenerated from other information, hence don't need saving
  private float knockbackLevel;  // 0.0 -> 1.0
  private float flameLevel;      // 0.0 -> 1.0
  private float damageBoostLevel;     // 0.0 -> 1.0
  private Map<Enchantment, Integer> specialDamages = new HashMap<>();
  private float efficiencyLevel; // 0.0 -> 1.0

  // If you forget to override this method, the default vanilla method will be called.
  // This sends a vanilla spawn packet, which is then silently discarded when it reaches the client.
  //  Your entity will be present on the server and can cause effects, but the client will not have a copy of the entity
  //    and hence it will not render.
  @Nonnull
  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  private static final byte VANILLA_ARROW_IMPACT_STATUS_ID = 0;

  /*   see https://wiki.vg/Entity_statuses
       make a cloud of particles at the impact point
   */
  @Override
  public void handleStatusUpdate(byte statusID) {
    if (statusID == VANILLA_ARROW_IMPACT_STATUS_ID) {
      final Color BROWN = new Color(165,42,42);
      final double MAXIMUM_DEVIATION = 0.5;  // the scatter of the spawning position, as a proportion of the entity width
      for(int i = 0; i < 20; ++i) {
        this.world.addParticle(ParticleTypes.ENTITY_EFFECT,
                this.getPosXRandom(MAXIMUM_DEVIATION), this.getPosYRandom(), this.getPosZRandom(MAXIMUM_DEVIATION),
                BROWN.getRed() / 255.0, BROWN.getGreen() / 255.0, BROWN.getBlue() / 255.0);
      }
    }
  }

  private final String THROWER_NBT = "thrower";
  private final String IN_FLIGHT_NBT = "inflight";
  private final String TICKS_IN_FLIGHT_NBT = "ticksinflight";
  private final String TICKS_NOT_IN_FLIGHT_NBT = "ticksnotinflight";
  private final String PICKUP_DELAY_NBT = "pickupdelay";
  private final String FLIGHT_PATH_NBT = "flightpath";
  private final String RIGHT_HAND_THROWN_NBT = "righthandthrown";
  private final String ITEMSTACK_NBT = "Item";
  private final String DAMAGE_NBT = "damage";
  private final String MOMENTUM_NBT = "momentum";

  public void writeAdditional(CompoundNBT compound) {
    if (this.throwerID != null) {
      compound.put("thrower", NBTUtil.writeUniqueId(this.throwerID));
    }
    compound.putBoolean(IN_FLIGHT_NBT, this.dataManager.get(IN_FLIGHT));
    compound.putInt(PICKUP_DELAY_NBT, pickupDelay);
    compound.putInt(TICKS_IN_FLIGHT_NBT, ticksSpentInFlight);
    compound.putInt(TICKS_NOT_IN_FLIGHT_NBT, ticksSpentNotInFlight);

    compound.put(FLIGHT_PATH_NBT, boomerangFlightPath.serializeNBT());
    if (!this.getItemStack().isEmpty()) {
      compound.put(ITEMSTACK_NBT, this.getItemStack().write(new CompoundNBT()));
    }
    compound.putBoolean(RIGHT_HAND_THROWN_NBT, rightHandThrown);
    compound.putDouble(DAMAGE_NBT, this.damage);
    compound.putFloat(MOMENTUM_NBT, this.remainingMomentum);
    if (Double.isNaN(this.getPosX())) { //todo remove
      int i = 1;
    }
  }

  /** Read further NBT information to initialise the entity (after loading from disk or transmission from server)
   */
  public void readAdditional(CompoundNBT compound) {
    this.thrower = null;
    if (compound.contains(THROWER_NBT, NBTtypesMBE.COMPOUND_NBT_ID)) {
      this.throwerID = NBTUtil.readUniqueId(compound.getCompound(THROWER_NBT));
    }
    boolean isInFlight = compound.getBoolean(IN_FLIGHT_NBT);
    this.dataManager.set(IN_FLIGHT, isInFlight);
    pickupDelay = compound.getInt(PICKUP_DELAY_NBT);
    ticksSpentInFlight = compound.getInt(TICKS_IN_FLIGHT_NBT);
    ticksSpentNotInFlight = compound.getInt(TICKS_NOT_IN_FLIGHT_NBT);

    boomerangFlightPath.deserializeNBT(compound.getCompound(FLIGHT_PATH_NBT));
    CompoundNBT compoundnbt = compound.getCompound(ITEMSTACK_NBT);
    ItemStack boomerangItemStack = ItemStack.read(compoundnbt);
    this.setItemStack(boomerangItemStack);
    if (this.getItemStack().isEmpty()) {
      this.remove();
    }
    rightHandThrown = compound.getBoolean(RIGHT_HAND_THROWN_NBT);
    damage = compound.getDouble(DAMAGE_NBT);
    remainingMomentum = compound.getFloat(MOMENTUM_NBT);
    copyEnchantmentData(boomerangItemStack);
  }

  /**
   * Who threw this boomerang?
   * @return
   */
  @Nullable
  public LivingEntity getThrower() {
    if ((this.thrower == null || this.thrower.removed)
            && this.throwerID != null
            && this.world instanceof ServerWorld) {
      Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.throwerID);
      if (entity instanceof LivingEntity) {
        this.thrower = (LivingEntity)entity;
      } else {
        this.thrower = null;
      }
    }
    return this.thrower;
  }

  public boolean isRightHandThrown() {
    return rightHandThrown;
  }

  /**
   * Sets a target for the client to interpolate towards over the next few ticks
   */
  @OnlyIn(Dist.CLIENT)
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    boolean isInFlight = this.dataManager.get(IN_FLIGHT);
    if (isInFlight) return;   // if we are in flight, the client tick will force the position, yaw and pitch every tick

    // otherwise, update position and rotation to match the server
    this.setPosition(x, y, z);
    this.setRotation(yaw, pitch);
  }

  /**
   * Updates the entity motion clientside, called by packets from the server
   */
  @OnlyIn(Dist.CLIENT)
  public void setVelocity(double x, double y, double z) {
//    LOGGER.info("setVelocity:" + x + ", " + y + ", " + z);
    this.setMotion(x, y, z);
//    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
//      float f = MathHelper.sqrt(x * x + z * z);
//      this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
//      this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI));
//      this.prevRotationYaw = this.rotationYaw;
//      this.prevRotationPitch = this.rotationPitch;
//    }
  }

//  private static boolean canBeCollidedWith(Entity entityToCheck) {
//    return !entityToCheck.isSpectator() && entityToCheck.canBeCollidedWith();
//  }

  /**
   * Called to update the entity's position/logic.
   * We are using some unusual logic to create the particular flight path that we want:
   *
   * 1) If the boomerang is in flight, every tick we force the entity position to a defined position on the curve.
   * 2) If the boomerang is not in flight (has hit something and is falling to the ground), then we use vanilla
   *    mechanics i.e. set motion (velocity) and let gravity act on the entity
   */
  public void tick() {
    Optional<Double> debugYaw = DebugSettings.getDebugParameter("mbe81b_yaw");
    Optional<Double> debugPitch = DebugSettings.getDebugParameter("mbe81b_pitch");
    Optional<Double> debugEndOverEndRotation = DebugSettings.getDebugParameter("mbe81b_endoverendrotation");
    if (debugYaw.isPresent()) {
      this.rotationYaw = debugYaw.get().floatValue();
      this.prevRotationYaw = this.rotationYaw;
    }
    if (debugPitch.isPresent()) {
      this.rotationPitch = debugPitch.get().floatValue();
      this.prevRotationPitch = this.rotationPitch;
    }
    if (debugEndOverEndRotation.isPresent()) {
      this.endOverEndRotation = debugEndOverEndRotation.get().floatValue();
      this.prevEndOverEndRotation = this.endOverEndRotation;
    }
    if (DebugSettings.getDebugParameter("mbe81b_notick").isPresent()) { // for debugging purposes only: freeze animation
      return;
    }
    super.tick();

    boolean isInFlight = this.dataManager.get(IN_FLIGHT);
    if (DebugSettings.getDebugParameter("mbe81b_not_in_flight").isPresent()) {
      this.dataManager.set(IN_FLIGHT, false);
    }
    if (isInFlight) {
      tickInFlight();
    } else {
      tickNotInFlight();
    }
  }

  /**
   * Once the boomerang has hit something, it stops flying and just drops as if it were an item
   * Code copied from ItemEntity
   */
  private void tickNotInFlight() {
    if (this.pickupDelay > 0) {
      --this.pickupDelay;
    }

    // apply buoyancy if underwater, or gravity acceleration if out of water
    this.prevPosX = this.getPosX();
    this.prevPosY = this.getPosY();
    this.prevPosZ = this.getPosZ();
    this.prevEndOverEndRotation = this.endOverEndRotation;  // no spinning when not in flight
    Vec3d initialVelocity = this.getMotion();
    if (this.areEyesInFluid(FluidTags.WATER)) {
      this.applyFloatMotion();
    } else if (!this.hasNoGravity()) {
      final double ACCELERATION_DUE_TO_GRAVITY = -0.04; // blocks per tick squared
      this.setMotion(this.getMotion().add(0.0, ACCELERATION_DUE_TO_GRAVITY, 0.0));
    }

    // check if we have collided with a block; if so, move out of the block
    // "noClip" is confusingly named.  It actually means "this entity is already colliding with a block before it has moved on this tick"
    //  If this is true, then we just push the entity out of the block in a suitable direction, and the move method doesn't check for
    //   collisions again on this tick.
    if (this.world.isRemote) {
      this.noClip = false;
    } else {
      this.noClip = !this.world.hasNoCollisions(this);
      if (this.noClip) {
        this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getPosZ());
      }
    }

    // move the item and adjust its speed
    final float THRESHOLD_HORIZONTAL_SPEED = 1E-5F;// below this speed, there is negligible horizontal speed
    if (!this.onGround
            || horizontalMag(this.getMotion()) > THRESHOLD_HORIZONTAL_SPEED
            || (this.ticksExisted + this.getEntityId()) % 4 == 0) {  // check for movement at least every fourth tick
      this.move(MoverType.SELF, this.getMotion());
      final float FRICTION_FACTOR = 0.98F;
      float horizontalfrictionFactor = FRICTION_FACTOR;

      if (this.onGround) {  // get the slipperiness of the block under the entity's "feet"
        BlockPos pos = new BlockPos(this.getPosX(), this.getPosY() - 1.0D, this.getPosZ());
        horizontalfrictionFactor *= this.world.getBlockState(pos).getSlipperiness(this.world, pos, this);
      }

      this.setMotion(this.getMotion().mul(horizontalfrictionFactor, FRICTION_FACTOR, horizontalfrictionFactor));
      if (this.onGround) {
        final double BOUNCE_MULTIPLIER = -0.5;
        this.setMotion(this.getMotion().mul(1.0, BOUNCE_MULTIPLIER, 1.0));
      }
    }

    // check if we're in lava, bounce around if we are
    boolean blockPositionHasChanged = MathHelper.floor(this.prevPosX) != MathHelper.floor(this.getPosX())
                     || MathHelper.floor(this.prevPosY) != MathHelper.floor(this.getPosY())
                     || MathHelper.floor(this.prevPosZ) != MathHelper.floor(this.getPosZ());
    int tickUpdatePeriod = blockPositionHasChanged ? 2 : 40;
    if (this.ticksExisted % tickUpdatePeriod == 0) {
      if (this.world.getFluidState(new BlockPos(this)).isTagged(FluidTags.LAVA)) {
        this.setMotion( (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F,
                        0.2,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F  );
        this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
      }
    }

    // isAirBorne is poorly named.  it should actually be "has entity accelerated significantly?"
    //  if true, it prompts the server to send an immediate entity update to the client
    this.isAirBorne |= this.handleWaterMovement();
    if (!this.world.isRemote) {
      double accelerationSquared = this.getMotion().subtract(initialVelocity).lengthSquared();
      final double THRESHOLD_ACCELERATION_TO_TRIGGER_UPDATE = 0.1;
      if (accelerationSquared > THRESHOLD_ACCELERATION_TO_TRIGGER_UPDATE * THRESHOLD_ACCELERATION_TO_TRIGGER_UPDATE) {
        this.isAirBorne = true;
      }
    }

    ItemStack item = this.getItemStack();
    ++ticksSpentNotInFlight;
    if (!this.world.isRemote && ticksSpentNotInFlight >= LIFESPAN_BEFORE_DISAPPEAR_TICKS) {
      this.remove();
    }

    if (item.isEmpty()) {
      this.remove();
    }

  }

  /** called when underwater: apply upwards acceleration if currently moving slower than maximum upwards speed
   */
  private void applyFloatMotion() {
    Vec3d velocity = this.getMotion();
    final double SIDEWAYS_FRICTION_FACTOR = 0.99;
    final double MAXIMUM_UPWARDS_VELOCITY = 0.06;    // blocks per tick
    final double ACCELERATION_DUE_TO_BUOYANCY = 0.0005;  // blocks per tick squared
    double upwardsAcceleration = 0;
    if (velocity.y < MAXIMUM_UPWARDS_VELOCITY) {
      upwardsAcceleration = ACCELERATION_DUE_TO_BUOYANCY;
    }
    this.setMotion(velocity.x * SIDEWAYS_FRICTION_FACTOR,
                   velocity.y + upwardsAcceleration,
                   velocity.z * SIDEWAYS_FRICTION_FACTOR);
  }

  private void tickInFlight() {
    final float TICKS_PER_SECOND = 20.0F;
    Vec3d startPosition = this.getPositionVec();
    if (Double.isNaN(startPosition.getY())) { //todo remove
      int i = 1;
    }
    double timeSpentInFlight = (ticksSpentInFlight + 1) / TICKS_PER_SECOND;
    Vec3d endPosition = boomerangFlightPath.getPosition(timeSpentInFlight);
    if (Double.isNaN(endPosition.getY())) { //todo remove
      int i = 1;
    }
    Vec3d motion = endPosition.subtract(startPosition);
    if (Double.isNaN(motion.getY())) { //todo remove
      int i = 1;
    }
    AxisAlignedBB aabbCollisionZone = this.getBoundingBox().expand(motion).grow(1.0D);

//    List<Entity> possibleCollisions = this.world.getEntitiesInAABBexcluding(this,
//            axisalignedbb, BoomerangEntity::canBeCollidedWith);
//    for (Entity entity : possibleCollisions) {
//      if (entity == this.ignoreEntity) {
//        ++this.ignoreEntityTime;
//        break;
//      }
//
//      if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
//        this.ignoreEntity = entity;
//        this.ignoreEntityTime = 3;
//        break;
//      }
//    }

    // Calculate the first object that the boomerang hits (if any)
    // We use a "ray trace" to do this, which assumes that the boomerang has no height or width, i.e.
    //  will not collide with any objects unless its centre point intersects it.
    // The boomerang moves quite quickly so this is not likely to be visually distracting
    // If this isn't accurate enough for your taste, you can look at Entity::move and Entity::getAllowedMovement
    // When we hit an object, trigger an appropriate effect on that object, and then we're no longer in flight.
    RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, aabbCollisionZone,
            this::canEntityBeCollidedWith, RayTraceContext.BlockMode.OUTLINE, true);

    switch (raytraceresult.getType()) {
      case BLOCK: {
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)raytraceresult;
        BlockPos blockHitPos = blockRayTraceResult.getPos();
        if (world.getBlockState(blockHitPos).getBlock() == Blocks.NETHER_PORTAL) {
          this.setPortal(blockHitPos);
        } else {
          if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
            this.onImpactWithBlock((BlockRayTraceResult)raytraceresult);
          }
        }
        break;
      }
      case ENTITY: {
        if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
          this.onImpactWithEntity((EntityRayTraceResult)raytraceresult);
        }
        break;
      }
      case MISS: {
        break;
      }
    }
//    StringBuilder sb = new StringBuilder();
//    sb.append("rotationYaw1:" + this.rotationYaw);
    this.rotationYaw = boomerangFlightPath.getYaw((ticksSpentInFlight + 1) / TICKS_PER_SECOND);
//    sb.append(" rotationYaw2:" + this.rotationYaw);
    this.rotationPitch = -90;  // the model has its flat face point up, but during flight it needs to be pointing sideways, which
                               // corresponds to pitching it up by 90 degrees.

    // ensure proper wraparound to avoid jerkiness due to partialTick interpolation when rendering

    while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
      this.prevRotationPitch -= 360.0F;
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
//    sb.append(" rotationYaw3:" + this.rotationYaw);

    // smooth the rotations so that they're not abrupt / jerky
    this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
    this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
//    sb.append(" rotationYaw4:" + this.rotationYaw);
//    LOGGER.info(sb.toString());

    final float ROTATIONS_PER_SECOND = 2.0F;
    final float DEGREES_PER_TICK = 360.0F * ROTATIONS_PER_SECOND / TICKS_PER_SECOND;
    this.prevEndOverEndRotation = MathHelper.wrapDegrees(this.endOverEndRotation);
    this.endOverEndRotation = this.prevEndOverEndRotation + DEGREES_PER_TICK;

    Vec3d newPosition = this.getPositionVec().add(motion);

    // if flying through water, add bubbles but don't slow the boomerang down (unrealistic I guess, but making it slow down in water
    //   would add extra complexity to the code and it's complicated enough already!)
    if (this.isInWater()) {
      for (int i = 0; i < 4; ++i) {
        final float TRAIL_DISTANCE_FACTOR = 0.25F;
        Vec3d distanceBackFromNewPosition = motion.scale(TRAIL_DISTANCE_FACTOR);
        Vec3d bubbleSpawnPosition = newPosition.subtract(distanceBackFromNewPosition);
        this.world.addParticle(ParticleTypes.BUBBLE,
                bubbleSpawnPosition.getX(), bubbleSpawnPosition.getY(), bubbleSpawnPosition.getZ(),
                motion.getX(), motion.getY(), motion.getZ());
      }
    }

    this.setMotion(motion);
    if (Double.isNaN(this.getMotion().getY())) { //todo remove
      int i = 1;
    }

    this.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
    this.doBlockCollisions();  // "collision" just means that the boomerang has entered this block's space
                               //  eg a tripwire, or moving through a web
                               //  it doesn't mean that the boomerang has hit anything

    // at the end of the pre-programmed flight path, convert to normal ballistic flight
    //  will occur if the player has moved since throwing the boomerang
    if (boomerangFlightPath.hasReachedEndOfFlightPath(timeSpentInFlight)) {
      dataManager.set(IN_FLIGHT, false);
    }
    ++ticksSpentInFlight;
  }

  // Called when the boomerang hits a block.
  // If the block is weak, smash it and keep flying
  // Otherwise, bounce off it and stop flying.
  private void onImpactWithBlock(BlockRayTraceResult rayTraceResult) {

    // is the block weak enough for the boomerang to smash through it?
    //   the boomerang is modelled as a wooden axe
    BlockPos blockPos = rayTraceResult.getPos();
    World world = this.getEntityWorld();
    BlockState blockState = world.getBlockState(blockPos);
    float blockHardness = blockState.getBlockHardness(world, blockPos);
    ItemStack dummyAxe = new ItemStack(Items.WOODEN_AXE);

    // typical destroy speeds:
    //  1.0F default, 2.0F wooden axe on proper material
    // typical hardnesses:
    // 1.5 for stone, 0.6 for grass, 2.0 for logs, 0.2 for leaves

    final float RATIO_AT_MINIMUM_EFFICIENCY = 1.0F;
    final float RATIO_AT_MAXIMUM_EFFICIENCY = 32.0F;  // at max enchantment efficiency, harvesting is 32x more efficient
    final float COEFFICIENT = (float)Math.log(RATIO_AT_MAXIMUM_EFFICIENCY / RATIO_AT_MINIMUM_EFFICIENCY);
    float efficiency = RATIO_AT_MINIMUM_EFFICIENCY *
                      (float)Math.exp(COEFFICIENT * efficiencyLevel);
    float destroySpeed = Items.WOODEN_AXE.getDestroySpeed(dummyAxe, blockState);
    float momentumLoss = (destroySpeed > 0.001F) ? blockHardness / destroySpeed / efficiency : 1.0F;

    if (momentumLoss > remainingMomentum) {  // block is too hard; make the boomerang bounce off and stop flying
      stopFlightDueToBlockImpact(rayTraceResult);
    } else { // smash block and keep flying
      harvestBlockWithItemDrops(world, blockPos);
      remainingMomentum -= momentumLoss;
    }
  }

  // richochet off a solid block and stop flying.
  private void stopFlightDueToBlockImpact(BlockRayTraceResult rayTraceResult) {
    pickupDelay = MINIMUM_TIME_BEFORE_PICKUP_TICKS;
    dataManager.set(IN_FLIGHT, false);
    this.playSound(SoundEvents.BLOCK_WOOD_HIT, 0.25F, 0.5F);
    // make the boomerang ricochet off the face
    Vec3d velocity = this.getMotion();
    final double RICHOCHET_SPEED = 0.5; // amount of speed left after richochet
    switch (rayTraceResult.getFace()) {
      case EAST: {
        if (velocity.getX() < 0) velocity = new Vec3d(-RICHOCHET_SPEED * velocity.getX(), velocity.getY(), velocity.getZ());
        break;
      }
      case WEST: {
        if (velocity.getX() > 0) velocity = new Vec3d(-RICHOCHET_SPEED * velocity.getX(), velocity.getY(), velocity.getZ());
        break;
      }
      case NORTH: {
        if (velocity.getZ() > 0) velocity = new Vec3d(velocity.getX(), velocity.getY(), -RICHOCHET_SPEED * velocity.getZ());
        break;
      }
      case SOUTH: {
        if (velocity.getZ() < 0) velocity = new Vec3d(velocity.getX(), velocity.getY(), -RICHOCHET_SPEED * velocity.getZ());
        break;
      }
      case UP:      // shouldn't happen, but if it does- just "graze" the surface without bouncing off
      case DOWN:{
        break;
      }
      default: {
        break;
      }
    }
    this.setMotion(velocity);
    this.world.setEntityState(this, (byte)VANILLA_ARROW_IMPACT_STATUS_ID);
  }

  /**
   * Called when the arrow hits an entity
   */
  private void onImpactWithEntity(EntityRayTraceResult rayTraceResult) {
    Entity target = rayTraceResult.getEntity();
    float speed = (float)this.getMotion().length();
    int baseDamage = MathHelper.ceil(Math.max(speed * this.damage, 0.0D));
    final float MAX_DAMAGE_BOOST_RATIO = 3.0F;  // at max power enchantment, add this much extra, eg 3 = add 300% extra damage
    float damageBoost = baseDamage * (1.0F + damageBoostLevel * MAX_DAMAGE_BOOST_RATIO);

    float specialDamageRatio = 0;
    if (!this.world.isRemote && target instanceof LivingEntity) {
      LivingEntity targetLivingEntity = (LivingEntity)target;
      for (Map.Entry<Enchantment, Integer> enchantment : specialDamages.entrySet()) {
        if (enchantment.getKey() instanceof DamageEnchantment) {
          DamageEnchantment damageEnchantment = (DamageEnchantment)enchantment;
          specialDamageRatio += damageEnchantment.calcDamageByCreature(enchantment.getValue(), targetLivingEntity.getCreatureAttribute());
        } else {
          LOGGER.warn("Expected a DamageEnchantment but got instead:" + enchantment.getKey());
        }
      }
    }
    float specialDamage = baseDamage * specialDamageRatio;
    float totalDamage = baseDamage + damageBoost + specialDamage;

    LivingEntity thrower = this.getThrower();
    DamageSource damagesource;
    if (thrower == null) {
      damagesource = DamageSource.causeThrownDamage(this, this);
    } else {
      damagesource = DamageSource.causeThrownDamage(this, thrower);
      thrower.setLastAttackedEntity(target);
    }

    boolean isEnderMan = target.getType() == EntityType.ENDERMAN;
    int fireTimer = target.getFireTimer();

    int burnTimeSeconds = 0;
    if (this.isBurning()) burnTimeSeconds += 5;
    final float BURN_TIME_SECONDS_AT_MAX_ENCHANTMENT = 8;
    burnTimeSeconds += flameLevel * BURN_TIME_SECONDS_AT_MAX_ENCHANTMENT;

    if (this.isBurning() && !isEnderMan) {
      target.setFire(burnTimeSeconds);
    }

    boolean entityTookDamage = target.attackEntityFrom(damagesource, totalDamage);

    if (entityTookDamage) {
      if (isEnderMan) {
        return;
      }

      if (target instanceof LivingEntity) {
        LivingEntity livingentity = (LivingEntity)target;

        if (this.knockbackLevel > 0) {
          final float VERTICAL_KNOCKBACK = 0.1F;
          final float KNOCKBACK_VELOCITY_AT_MAX_ENCHANTMENT = 1.2F;   // blocks per tick velocity
          Vec3d knockbackVelocity = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize()
                                        .scale(this.knockbackLevel * KNOCKBACK_VELOCITY_AT_MAX_ENCHANTMENT);
          if (knockbackVelocity.lengthSquared() > 0.0D) {
            livingentity.addVelocity(knockbackVelocity.x, VERTICAL_KNOCKBACK, knockbackVelocity.z);
          }
        }

        if (!this.world.isRemote && thrower instanceof LivingEntity) {
          for (Map.Entry<Enchantment, Integer> enchantment : specialDamages.entrySet()) {
            if (enchantment.getKey() instanceof DamageEnchantment) {
              DamageEnchantment damageEnchantment = (DamageEnchantment)enchantment;
              damageEnchantment.onEntityDamaged(thrower, target, enchantment.getValue());
            } else {
              LOGGER.warn("Expected a DamageEnchantment but got instead:" + enchantment.getKey());
            }
          }
        }
      }
    } else {
      target.setFireTimer(fireTimer);  // undo any flame effect we added
    }
    stopFlightDueToEntityImpact(rayTraceResult, !entityTookDamage);
  }

  // stop flying after hitting an entity:
  //  if the entity was vulnerable to the damage, drop the item
  //  if the entity was invulnerable, bounce off
  private void stopFlightDueToEntityImpact(EntityRayTraceResult rayTraceResult, boolean bounceOff) {
    pickupDelay = MINIMUM_TIME_BEFORE_PICKUP_TICKS;
    dataManager.set(IN_FLIGHT, false);

    if (bounceOff) {
      this.playSound(SoundEvents.BLOCK_WOOD_HIT, 0.25F, 0.5F);
    } else {
      this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));  // kind of like a meaty splat sound
    }

    Vec3d newVelocity;
    if (bounceOff) {
      // make the boomerang ricochet off the entity
      //  assume that the point of impact is at a circle around the centre of the entity
      //  so that the boomerang bounces off as if it hit the rim of the circle

      Entity target = rayTraceResult.getEntity();
      Vec3d pointOfImpact = rayTraceResult.getHitVec();
      Vec3d impactRadialPosition = pointOfImpact.subtract(target.getPositionVec());

      // some vector math to calculate the path when bouncing off
      Vec3d boomerangVelocity = this.getMotion();
      double radialProjectionLength = boomerangVelocity.dotProduct(impactRadialPosition) / impactRadialPosition.lengthSquared();
      Vec3d radialComponent = impactRadialPosition.scale(radialProjectionLength);
      Vec3d tangentialComponent = boomerangVelocity.subtract(radialComponent);
      final double RICHOCHET_SPEED = 0.5; // amount of speed left after richochet
      // the ricochet keeps the same tangential component and inverts the radial component
      newVelocity = tangentialComponent.subtract(radialComponent);
      newVelocity = newVelocity.scale(RICHOCHET_SPEED);
    } else {
      newVelocity = new Vec3d(0, 0, 0);
    }
    this.setMotion(newVelocity);
    this.world.setEntityState(this, (byte)VANILLA_ARROW_IMPACT_STATUS_ID);
  }

  // destroy this block and spawn the relevant item drops
  //  copied from world.destroyBlock
  private void harvestBlockWithItemDrops(World world, BlockPos blockPos) {
    BlockState blockstate = world.getBlockState(blockPos);
    if (blockstate.isAir(world, blockPos)) return;

    IFluidState ifluidstate = world.getFluidState(blockPos);
    final int EVENT_ID_BREAK_BLOCK_SOUND_AND_PARTICLES = 2001;
    world.playEvent(EVENT_ID_BREAK_BLOCK_SOUND_AND_PARTICLES, blockPos, Block.getStateId(blockstate));
    TileEntity tileentity = blockstate.hasTileEntity() ? world.getTileEntity(blockPos) : null;
    Block.spawnDrops(blockstate, world, blockPos, tileentity, this, this.getItemStack());  // FORTUNE and SILK TOUCH enchantments affect drops

    int flags = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
    world.setBlockState(blockPos, ifluidstate.getBlockState(), flags);
  }

  /** The item collided with a player
   * @param entityIn
   */
  @Override
  public void onCollideWithPlayer(PlayerEntity entityIn) {
    if (dataManager.get(IN_FLIGHT).booleanValue()) {
      onCollideWithPlayerInFlight(entityIn);
    } else {
      onCollideWithPlayerNotInFlight(entityIn);
    }
  }

  private final int MINIMUM_TIME_BEFORE_PICKUP_TICKS = 40;
  private final int LIFESPAN_BEFORE_DISAPPEAR_TICKS = 6000;
    /**
     * Collision with the player while still in flight
     */
  public void onCollideWithPlayerInFlight(PlayerEntity entityIn) {
    // if this player is the thrower -
    //   if a hand is free, then catch the boomerang.  If both hands are full, drop to the ground.
    // otherwise - ignore
    if (!this.world.isRemote) return;
    if (getThrower() != entityIn) return;

    Hand handToCatch = null;
    if (entityIn.getHeldItemMainhand().isEmpty()) {
      handToCatch = Hand.MAIN_HAND;
    } else if (entityIn.getHeldItemOffhand().isEmpty()) {
      handToCatch = Hand.OFF_HAND;
    }
    if (handToCatch != null) {
      entityIn.setHeldItem(handToCatch, getItemStack());
      this.remove();
      return;
    }
    pickupDelay = MINIMUM_TIME_BEFORE_PICKUP_TICKS;
    dataManager.set(IN_FLIGHT, false);
  }

  /**
   * Collision with the player while not in flight
   */
  public void onCollideWithPlayerNotInFlight(PlayerEntity entityIn) {
    if (this.world.isRemote) return;
    if (pickupDelay > 0) return;
    ItemStack pickedUpBoomerang = new ItemStack(StartupCommon.boomerangItem);

    boolean successfullyPickedUp = entityIn.inventory.addItemStackToInventory(pickedUpBoomerang);
    if (successfullyPickedUp) {
      entityIn.onItemPickup(this, 1);
      this.remove();
    }
  }

  public float getEndOverEndRotation(float partialTicks) {
    float rawAngle = MathHelper.lerp(partialTicks, prevEndOverEndRotation, endOverEndRotation);
    return MathHelper.wrapDegrees(rawAngle);
  }

  /**
   * Gets the boomerang item that was used to throw this boomerangEntity
   */
  public ItemStack getItemStack() {
    return this.getDataManager().get(ITEMSTACK);
  }

  /**
   * Sets the boomerang item that was used to throw this boomerangEntity
   */
  public void setItemStack(ItemStack stack) {
    this.getDataManager().set(ITEMSTACK, stack);
  }

  // When the client entity is spawned (in response to a packet from the server), we need to send it two extra pieces of information
  // 1) The boomerang flight path information
  // 2) The number of ticks that the entity has spent in flight.
  // Why is this necessary?
  // Because when the entity is first created, or when it is loaded from disk (using NBT), the client does not receive all of the
  //   information that the server does.
  // Some of the information is synchronised by vanilla (DataManager variables, position, motion, yaw+pitch), but anything else
  //  must be transmitted by us.

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    CompoundNBT nbt = boomerangFlightPath.serializeNBT();
    buffer.writeCompoundTag(nbt);
    buffer.writeInt(ticksSpentInFlight);
    buffer.writeBoolean(rightHandThrown);
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    CompoundNBT nbt = additionalData.readCompoundTag();
    boomerangFlightPath.deserializeNBT(nbt);
    ticksSpentInFlight = additionalData.readInt();
    rightHandThrown = additionalData.readBoolean();
  }

  private final int INITIAL_NON_COLLISION_TICKS = 2;
  private boolean canEntityBeCollidedWith(Entity entityToTest) {
    return !entityToTest.isSpectator() && entityToTest.canBeCollidedWith()
            && !(entityToTest == this.thrower && this.ticksExisted <= INITIAL_NON_COLLISION_TICKS);
  }
  private static final Logger LOGGER = LogManager.getLogger();
}
