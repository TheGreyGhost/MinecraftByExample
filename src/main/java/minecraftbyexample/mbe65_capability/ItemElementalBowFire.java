package minecraftbyexample.mbe65_capability;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemElementalBowFire is used to fire arrows which have been given Elemental Fire
 *
 *   The longer that the player pulls the bow back (holds the right button down), the more elemental fire energy is
 *     charged into the arrow, up to 4 seconds.
 *
 *   The arrow does no damage when it hits.
 *
 * Most of this class is copied directly from BowItem.
 *
 */
public class ItemElementalBowFire extends BowItem  // extend BowItem instead of ShootableItem, otherwise the Field Of View Zoom doesn't work.
{
  public ItemElementalBowFire()
  {
    super(new Properties().maxStackSize(1).group(ItemGroup.COMBAT)
    );
    this.addPropertyOverride(new ResourceLocation("pulltime"), ItemElementalBowFire::getPullDurationSeconds);
    this.addPropertyOverride(new ResourceLocation("isbeingpulled"), ItemElementalBowFire::isBeingPulled);
  }

  // The CapabilityProvider returned from this method is used to specify which capabilities the Bow has.
  @Nonnull
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
    return new CapabilityProviderFireItems();
  }

  /** Set the Elemental fire level of the given entity
   * @param abstractArrowEntity
   * @param fireCharge
   */
  private void setElementalFireLevel(Entity abstractArrowEntity, int fireCharge) {
    ElementalFire fireInterface = abstractArrowEntity.getCapability(CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE).orElse(null);
    if (fireInterface == null) return;;
    fireInterface.addCharge(fireCharge);
  }

  /**
   * Called when the player stops using an Item (stops holding the right mouse button).
   * Fire our arrow!  Copied straight from vanilla BowItem, with some tweaks
   *
   * WHen the arrow is fired, set its ElementalFireCapability
   */
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
    if (!(entityLiving instanceof PlayerEntity)) return;
    PlayerEntity playerentity = (PlayerEntity)entityLiving;
    boolean dontNeedAmmo = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
    ItemStack ammo = playerentity.findAmmo(stack);

    int pullDurationTicks = this.getUseDuration(stack) - timeLeft;
    pullDurationTicks = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity,
            pullDurationTicks, !ammo.isEmpty() || dontNeedAmmo);
    if (pullDurationTicks < 0) return;
    if (ammo.isEmpty() && !dontNeedAmmo) return;
    if (ammo.isEmpty()) {
      ammo = new ItemStack(Items.ARROW);
    }

    float arrowVelocity = getArrowVelocity(pullDurationTicks);
    if (arrowVelocity < 0.1) return;
    boolean infiniteAmmo = playerentity.abilities.isCreativeMode || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, stack, playerentity));

    if (!worldIn.isRemote) {
      ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
      AbstractArrowEntity abstractArrowEntity = arrowitem.createArrow(worldIn, ammo, playerentity);
      abstractArrowEntity = customeArrow(abstractArrowEntity);
      abstractArrowEntity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, arrowVelocity * 3.0F, 1.0F);
      if (arrowVelocity == 1.0F) {
        abstractArrowEntity.setIsCritical(true);
      }

      abstractArrowEntity.setDamage(0.0);
      abstractArrowEntity.setKnockbackStrength(0);

      if (infiniteAmmo || playerentity.abilities.isCreativeMode && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW)) {
        abstractArrowEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
      }

      // --------- our capability code is this part -----------

      // set the level of fire based on how long the bow has been charging up (how long the player has been pulling it)
      final float MAX_CHARGEUP_TIME = 4;
      final float MAX_CHARGE = 100;
      float pullDurationSeconds = getPullDurationSeconds(stack, worldIn, entityLiving);
      if (pullDurationSeconds > MAX_CHARGEUP_TIME) pullDurationSeconds = MAX_CHARGEUP_TIME;
      int fireCharge = (int)(MAX_CHARGE*pullDurationSeconds);
      setElementalFireLevel(abstractArrowEntity, fireCharge);

      worldIn.addEntity(abstractArrowEntity);
    }

    worldIn.playSound((PlayerEntity)null,
            playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(),
            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
            1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);
    if (!infiniteAmmo && !playerentity.abilities.isCreativeMode) {
      ammo.shrink(1);
      if (ammo.isEmpty()) {
        playerentity.inventory.deleteStack(ammo);
      }
    }

    playerentity.addStat(Stats.ITEM_USED.get(this));
  }

  // ---------- Vanilla code below -----------

  /** How long has the player been pulling the bow back for?
   * @return time spent pulling, in seconds
   */
  public static float getPullDurationSeconds(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
    final float NO_PULL = 0.0F;
    final float TICKS_PER_SECOND = 20.0F;
    if (livingEntity == null) return NO_PULL;
    if (livingEntity.getActiveItemStack() != itemStack) return NO_PULL;

    int pullDurationTicks = itemStack.getUseDuration() - livingEntity.getItemInUseCount();   // getItemInUseCount starts from maximum!
    return pullDurationTicks / TICKS_PER_SECOND;
  }

  /** Is the bow pulled back at all?
   * @return 0.0 = not pulled,  1.0 = yes
   */
  public static float isBeingPulled(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
    final float NOT_PULLED = 0.0F;
    final float IS_PULLED = 1.0F;
    if (livingEntity == null) return NOT_PULLED;
    if (livingEntity.isHandActive() && livingEntity.getActiveItemStack() == itemStack) return IS_PULLED;
    return NOT_PULLED;
  }

  /**
   * Gets the velocity of the arrow entity from the bow's charge
   */
  public static float getArrowVelocity(int pullDurationTicks) {
    float pullDurationSeconds = pullDurationTicks / 20.0F;
    float chargePowerFraction = (pullDurationSeconds * pullDurationSeconds + pullDurationSeconds * 2.0F) / 3.0F;
    if (chargePowerFraction > 1.0F) {
      chargePowerFraction = 1.0F;
    }
    return chargePowerFraction;
  }

  /**
   * How long it takes to use or consume an item
   */
  public int getUseDuration(ItemStack stack) {
    return 72000;  // arbitrary large value (1 hour)
  }

  /**
   * returns the action that specifies what animation to play when the items is being used
   */
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  /**
   * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
   * {@link #onItemUse}.
   */
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    boolean playerHasAmmo = !playerIn.findAmmo(itemstack).isEmpty();

    ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, playerHasAmmo);
    if (ret != null) return ret;

    if (!playerIn.abilities.isCreativeMode && !playerHasAmmo) {
      return ActionResult.resultFail(itemstack);
    } else {
      playerIn.setActiveHand(handIn);
      return ActionResult.resultConsume(itemstack);
    }
  }

  /**
   * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
   */
  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return ARROWS;
  }

  public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
    return arrow;
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
