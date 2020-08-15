package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
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
   * Throw the projectile
   * @param world
   * @param playerEntity
   * @param hand
   * @return
   */
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity, Hand hand) {
    ItemStack heldItem = playerEntity.getHeldItem(hand);

    if (!world.isRemote) {
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

      BoomerangEntity boomerangEntity = new BoomerangEntity(world, thrownBoomerang, playerEntity,
              startPosition, playerEntity.getYaw(1.0F),

              )

      // spawn the entity in the world
      world.addEntity(boomerangEntity);
    }
  public BoomerangEntity(World world, ItemStack boomerangItemStack,
            @Nullable LivingEntity thrower,
            Vec3d startPosition,
    float apexYaw, float distanceToApex,
    float maximumSidewaysDeflection,
    boolean anticlockwise,
    float flightSpeed) {
    playerEntity.addStat(Stats.ITEM_USED.get(this));
    if (!playerEntity.abilities.isCreativeMode) {
      heldItem.shrink(1);
    }

    return ActionResult.resultSuccess(heldItem);
  }


}
