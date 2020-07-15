package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by TGG on 24/06/2020.
 */
public class BoomerangItem extends Item {

  static private final int MAXIMUM_NUMBER_OF_BOOMERANGS = 1; // maximum stack size

  public BoomerangItem() {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_BOOMERANGS).group(ItemGroup.COMBAT)
    );
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
