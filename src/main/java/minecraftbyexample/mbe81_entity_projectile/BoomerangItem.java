package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.enchantment.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

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

    SoundEvent soundEvent = (mood == EmojiMood.HAPPY) ? SoundEvents.ENTITY_VILLAGER_YES : SoundEvents.ENTITY_VILLAGER_NO;

    world.playSound(null, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(),
            soundEvent, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    if (!world.isRemote) {
      BoomerangEntity boomerangEntity = new BoomerangEntity(world, playerEntity,)

      // spawn the entity in the world
      world.addEntity(boomerangEntity);
    }

    playerEntity.addStat(Stats.ITEM_USED.get(this));
    if (!playerEntity.abilities.isCreativeMode) {
      heldItem.shrink(1);
    }

    return ActionResult.resultSuccess(heldItem);
  }


}
