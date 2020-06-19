package minecraftbyexample.mbe65_capability;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemSimple is an ordinary two-dimensional item
 * For background information on item see here http://greyminecraftcoder.blogspot.com/2013/12/items.html
 *   and here http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html
 */
public class ItemElementalBowFire extends ShootableItem
{
  static private final int MAXIMUM_NUMBER_OF_FROGS = 6; // maximum stack size
  public ItemElementalBowFire()
  {
    super(new Properties().maxStackSize(MAXIMUM_NUMBER_OF_FROGS).group(ItemGroup.MISC) // the item will appear on the Miscellaneous tab in creative
    );
    this.addPropertyOverride(new ResourceLocation("pull"), ItemElementalBowFire::getPullDurationSeconds);
    this.addPropertyOverride(new ResourceLocation("pulling"), ItemElementalBowFire::isPulledBack);
  }

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
  public static float isPulledBack(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
    final float NOT_PULLED = 0.0F;
    final float IS_PULLED = 1.0F;
    if (livingEntity == null) return NOT_PULLED;
    if (livingEntity.isHandActive() && livingEntity.getActiveItemStack() == itemStack) return IS_PULLED;
    return NOT_PULLED;
  }

  // The CapabilityProvider returned from this method is used to specify which capabilities the Bow has.
  @Nonnull
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
    return new CapabilityProviderFireItems();
  }

  /**
   * Called when the player stops using an Item (stops holding the right mouse button).
   * Fire our arrow!  Copied straight from vanilla BowItem
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
    boolean flag1 = playerentity.abilities.isCreativeMode || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, stack, playerentity));

    if (!worldIn.isRemote) {
      ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
      AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, ammo, playerentity);
      abstractarrowentity = customeArrow(abstractarrowentity);
      abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, arrowVelocity * 3.0F, 1.0F);
      if (arrowVelocity == 1.0F) {
        abstractarrowentity.setIsCritical(true);
      }

      int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
      if (j > 0) {
        abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double)j * 0.5D + 0.5D);
      }

      int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
      if (k > 0) {
        abstractarrowentity.setKnockbackStrength(k);
      }

      if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
        abstractarrowentity.setFire(100);
      }

      stack.damageItem(1, playerentity, (p_220009_1_) -> {
        p_220009_1_.sendBreakAnimation(playerentity.getActiveHand());
      });
      if (flag1 || playerentity.abilities.isCreativeMode && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW)) {
        abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
      }

      worldIn.addEntity(abstractarrowentity);
    }

    worldIn.playSound((PlayerEntity)null,
            playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(),
            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
            1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);
    if (!flag1 && !playerentity.abilities.isCreativeMode) {
      ammo.shrink(1);
      if (ammo.isEmpty()) {
        playerentity.inventory.deleteStack(ammo);
      }
    }

    playerentity.addStat(Stats.ITEM_USED.get(this));
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
    boolean flag = !playerIn.findAmmo(itemstack).isEmpty();

    ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
    if (ret != null) return ret;

    if (!playerIn.abilities.isCreativeMode && !flag) {
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
}

}
