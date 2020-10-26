//package minecraftbyexample.mbe81_entity_projectile;
//
//import minecraftbyexample.usefultools.UsefulFunctions;
//import net.minecraft.enchantment.*;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.*;
//import net.minecraft.potion.Effects;
//import net.minecraft.stats.Stats;
//import net.minecraft.util.*;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.world.World;
//
//import javax.annotation.Nullable;
//
///**
// * Created by TGG on 24/06/2020.
// *
// * The boomerang is "tiered" similar to wood sword (vs stone sword, iron sword, etc)
// * This lets us easily make the boomerang enchantable and repairable
// */
//public class BoomerangItem extends TieredItem {
//
//  static private final int MAXIMUM_NUMBER_OF_BOOMERANGS = 1; // maximum stack size
//
//  public BoomerangItem() {
//    super(ItemTier.WOOD, new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_BOOMERANGS).group(ItemGroup.COMBAT)
//    );
//
//    // Because we are extending a TieredItem, it automatically calculates the max # of uses (damage), repair material,
//    //   enchantability (etc) for us.
//
//    // We use a PropertyOverride for this item to change the appearance depending on the state of the property;
//    // In this case, the boomerang moves gradually higher when the player holds the right button for longer.
//    // getChargeUpTime() is used as a lambda function to calculate the current chargefraction during rendering
//    // See the mbe81b_boomerang_charge_0.json, mbe81b_boomerang_charge_1.json, etc to see how this is done.
//    //  See also mbe12 for a more-detailed explanation
//    this.addPropertyOverride(new ResourceLocation("chargefraction"), BoomerangItem::getChargeUpTime);
//  }
//
//  /**
//   * Returns the amount of time that the boomerang has been charging up (player has been holding down the right mouse button)
//   * @param stack
//   * @param worldIn
//   * @param entityIn
//   * @return 0.0 = not charged at all --> 1.0 = fully charged
//   */
//  public static float getChargeUpTime(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
//    final float IDLE_FRAME_INDEX = 0.0F;
//    final float FULLY_CHARGED_INDEX = 1.0F;
//    final int FULL_CHARGE_TICKS = 20;
//
//    if (worldIn == null && entityIn != null)  {
//      worldIn = entityIn.world;
//    }
//
//    if (entityIn == null || worldIn == null) return IDLE_FRAME_INDEX;
//    if (!entityIn.isHandActive()) {  // player isn't holding down the right mouse button, i.e. not charging
//      return IDLE_FRAME_INDEX;
//    }
//    int ticksInUse = stack.getUseDuration() - entityIn.getItemInUseCount();
//    return (float)UsefulFunctions.interpolate_with_clipping(
//            ticksInUse, 0, FULL_CHARGE_TICKS,
//            IDLE_FRAME_INDEX, FULLY_CHARGED_INDEX);
//  }
//
//  /**
//   * Called when the player stops using an Item (stops holding the right mouse button).
//   */
//  @Override
//  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
//
//    if (!(entityLiving instanceof PlayerEntity)) return;
//    PlayerEntity playerEntity = (PlayerEntity)entityLiving;
//
//    final int MAX_CHARGEUP_TIME_TICKS = 20;
//    int ticksSpentChargingUp = this.getUseDuration(stack) - timeLeft;
//
//    final float MIN_FLIGHT_DISTANCE = 4;
//    final float MAX_FLIGHT_DISTANCE = 20;
//    float distanceToApex = (float)UsefulFunctions.interpolate_with_clipping(ticksSpentChargingUp,
//                                                                  0, MAX_CHARGEUP_TIME_TICKS,
//                                                                      MIN_FLIGHT_DISTANCE, MAX_FLIGHT_DISTANCE);
//    if (entityLiving.isPotionActive(Effects.STRENGTH)) {
//      distanceToApex *= 2;
//    }
//    final float SIDEWAYS_DEFLECTION_RATIO = 0.2F;
//    float sidewaysDeflection = distanceToApex * SIDEWAYS_DEFLECTION_RATIO;
//
//    final float MIN_FLIGHT_SPEED_BLOCKS_PER_SECOND = 4;
//    final float MAX_FLIGHT_SPEED_BLOCKS_PER_SECOND = 10;
//    float flightSpeedBPS = (float)UsefulFunctions.interpolate_with_clipping(ticksSpentChargingUp,
//            0, MAX_CHARGEUP_TIME_TICKS,
//            MIN_FLIGHT_SPEED_BLOCKS_PER_SECOND, MAX_FLIGHT_SPEED_BLOCKS_PER_SECOND);
//
//    if (entityLiving.isPotionActive(Effects.HASTE)) {
//      flightSpeedBPS *= 2;
//    }
//
//    ItemStack heldItem = stack;
//
//    if (!worldIn.isRemote) {
//      ItemStack thrownBoomerang = heldItem.copy();
//      final double OFFSET_FROM_PLAYER_EYE = -0.1;
//      Vector3d startPosition = new Vector3d(playerEntity.getPosX(), playerEntity.getPosYEye() + OFFSET_FROM_PLAYER_EYE, playerEntity.getPosZ());
//
//      boolean mainHandIsActive = (Hand.MAIN_HAND == playerEntity.getActiveHand());
//      boolean playerIsLeftHander = HandSide.LEFT == playerEntity.getPrimaryHand();
//      boolean boomerangIsInLeftHand = (mainHandIsActive && playerIsLeftHander) || (!mainHandIsActive && !playerIsLeftHander);
//      boolean antiClockwisePath = !boomerangIsInLeftHand;
//
//      BoomerangEntity boomerangEntity = new BoomerangEntity(worldIn, thrownBoomerang, playerEntity,
//              startPosition, playerEntity.getYaw(1.0F), playerEntity.getPitch(1.0F),
//              distanceToApex, sidewaysDeflection,
//              antiClockwisePath, flightSpeedBPS
//              );
//
//      // spawn the entity in the world
//      worldIn.addEntity(boomerangEntity);
//    }
//
//    playerEntity.addStat(Stats.ITEM_USED.get(this));
//    final boolean REMOVE_FROM_HAND_EVEN_WHEN_IN_CREATIVE = true;
//    if (!playerEntity.abilities.isCreativeMode || REMOVE_FROM_HAND_EVEN_WHEN_IN_CREATIVE) {
//      heldItem.shrink(1);
//    }
//  }
//
//  /**
//   * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
//   * {@link #onItemUse}.
//   */
//  @Override
//  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
//    ItemStack itemstack = playerIn.getHeldItem(handIn);
//    playerIn.setActiveHand(handIn);
//    return ActionResult.resultConsume(itemstack);
//  }
//
//  @Override
//  public UseAction getUseAction(ItemStack stack) {
//    return UseAction.NONE;
//  }
//
//  /**
//   * How long it takes to use or consume an item
//   */
//  @Override
//  public int getUseDuration(ItemStack stack) {
//    final int ARBITRARY_LONG_TIME = 72000;
//    return ARBITRARY_LONG_TIME;
//  }
//
//  //-----------------------------------
//
//  private final Enchantment [] VALID_ENCHANTMENTS = {Enchantments.KNOCKBACK, Enchantments.PUNCH,
//          Enchantments.FLAME, Enchantments.FIRE_ASPECT,
//          Enchantments.POWER,
//          Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS,
//          Enchantments.SILK_TOUCH, Enchantments.EFFICIENCY, Enchantments.FORTUNE};
//  /**
//   * Which enchantments can be applied to the boomerang?
//   * We're implementing several related to entity damage, as well as others related to block harvesting
//   * @param stack
//   * @param enchantment
//   * @return
//   */
//  @Override
//  public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
//    for (Enchantment enchantmentToCheck : VALID_ENCHANTMENTS) {
//      if (enchantmentToCheck == enchantment) return true;
//    }
//    return false;
//  }
//}
