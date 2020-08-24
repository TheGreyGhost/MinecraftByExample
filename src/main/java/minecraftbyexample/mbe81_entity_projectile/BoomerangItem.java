package minecraftbyexample.mbe81_entity_projectile;

import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by TGG on 24/06/2020.
 *
 * The boomerang is "tiered" similar to wood sword (vs stone sword, iron sword, etc)
 * This lets us easily make the boomerang enchantable and repairable
 */
public class BoomerangItem extends TieredItem {

  static private final int MAXIMUM_NUMBER_OF_BOOMERANGS = 1; // maximum stack size

  public BoomerangItem() {
    super(ItemTier.WOOD, new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_BOOMERANGS).group(ItemGroup.COMBAT)
    );

    // Because we are extending a TieredItem, it automatically calculates the max # of uses (damage), repair material,
    //   enchantability (etc) for us.
  }

  private final Enchantment [] VALID_ENCHANTMENTS = {Enchantments.KNOCKBACK, Enchantments.FLAME,
                                                     Enchantments.POWER, Enchantments.PUNCH,
                                                     Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS,
                                                     Enchantments.SILK_TOUCH, Enchantments.EFFICIENCY, Enchantments.FORTUNE};
  /**
   * Which enchantments can be applied to the boomerang?
   * We're implementing several related to entity damage, as well as others related to block harvesting
   * @param stack
   * @param enchantment
   * @return
   */
  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
    for (Enchantment enchantmentToCheck : VALID_ENCHANTMENTS) {
      if (enchantmentToCheck == enchantment) return true;
    }
    return false;
  }

  /**
   * Called when the player stops using an Item (stops holding the right mouse button).
   */
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {

    if (!(entityLiving instanceof PlayerEntity)) return;
    PlayerEntity playerEntity = (PlayerEntity)entityLiving;

    final int MAX_CHARGEUP_TIME_TICKS = 20;
    int ticksSpentChargingUp = this.getUseDuration(stack) - timeLeft;

    final float MIN_FLIGHT_DISTANCE = 4;
    final float MAX_FLIGHT_DISTANCE = 32;
    float distanceToApex = (float)UsefulFunctions.interpolate_with_clipping(ticksSpentChargingUp,
                                                                  0, MAX_CHARGEUP_TIME_TICKS,
                                                                      MIN_FLIGHT_DISTANCE, MAX_FLIGHT_DISTANCE);
    if (entityLiving.isPotionActive(Effects.STRENGTH)) {
      distanceToApex *= 2;
    }
    final float SIDEWAYS_DEFLECTION_RATIO = 0.2F;
    float sidewaysDeflection = distanceToApex * SIDEWAYS_DEFLECTION_RATIO;

    final float MIN_FLIGHT_SPEED_BLOCKS_PER_SECOND = 4;
    final float MAX_FLIGHT_SPEED_BLOCKS_PER_SECOND = 10;
    float flightSpeedBPS = (float)UsefulFunctions.interpolate_with_clipping(ticksSpentChargingUp,
            0, MAX_CHARGEUP_TIME_TICKS,
            MIN_FLIGHT_SPEED_BLOCKS_PER_SECOND, MAX_FLIGHT_SPEED_BLOCKS_PER_SECOND);

    if (entityLiving.isPotionActive(Effects.HASTE)) {
      flightSpeedBPS *= 2;
    }

//    /**
//     * Throw the projectile
//     * @param world
//     * @param playerEntity
//     * @param hand
//     * @return
//     */
//  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity, Hand hand) {
//    ItemStack heldItem = playerEntity.getHeldItem(hand);
    ItemStack heldItem = stack;

    if (!worldIn.isRemote) {
      ItemStack thrownBoomerang = heldItem.copy();
      final double OFFSET_FROM_PLAYER_EYE = -0.1;
      Vec3d startPosition = new Vec3d(playerEntity.getPosX(), playerEntity.getPosYEye() + OFFSET_FROM_PLAYER_EYE, playerEntity.getPosZ());

//      public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
//        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
//        float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
//        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
//        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
//        this.setMotion(this.getMotion().add(shooter.getMotion().x, shooter.onGround ? 0.0D : shooter.getMotion().y, shooter.getMotion().z));
//      }
      boolean antiClockwisePath = Hand.OFF_HAND == playerEntity.getActiveHand();  // I think the off hand is always on the left?

      BoomerangEntity boomerangEntity = new BoomerangEntity(worldIn, thrownBoomerang, playerEntity,
              startPosition, playerEntity.getYaw(1.0F), playerEntity.getPitch(1.0F),
              distanceToApex, sidewaysDeflection,
              antiClockwisePath, flightSpeedBPS
              );

      // spawn the entity in the world
      worldIn.addEntity(boomerangEntity);
    }
    playerEntity.addStat(Stats.ITEM_USED.get(this));
    if (!playerEntity.abilities.isCreativeMode) {
      heldItem.shrink(1);
    }
  }

  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK; // or UseAction.BOW?
  }

  /**
   * How long it takes to use or consume an item
   */
  public int getUseDuration(ItemStack stack) {
    final int ARBITRARY_LONG_TIME = 72000;
    return ARBITRARY_LONG_TIME;
  }
}
