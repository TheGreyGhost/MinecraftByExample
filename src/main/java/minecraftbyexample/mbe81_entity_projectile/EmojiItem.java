package minecraftbyexample.mbe81_entity_projectile;

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
 * Heavily based on the vanilla SnowballItem
 */
public class EmojiItem extends Item {
  static private final int MAXIMUM_NUMBER_OF_EMOJI = 64; // maximum stack size

  public EmojiItem( EmojiMood emojiMood) {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_EMOJI).group(ItemGroup.MISC)
    );
    this.emojiMood = emojiMood;
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

    // randomly choose the mood depending on how hungry the player is
    final int MAX_FOOD_LEVEL = 20;
    int foodlevel = playerEntity.getFoodStats().getFoodLevel();

    double happyChance = foodlevel / (MAX_FOOD_LEVEL * 1.1);
    Random random = new Random();
    EmojiMood mood = random.nextDouble() < happyChance ? EmojiMood.HAPPY : EmojiMood.GRUMPY;
    SoundEvent soundEvent = (mood == EmojiMood.HAPPY) ? SoundEvents.ENTITY_VILLAGER_CELEBRATE : SoundEvents.ENTITY_VILLAGER_NO;
    EmojiItem itemToThrow = (mood == EmojiMood.HAPPY) ? StartupCommon.emojiItemHappy : StartupCommon.emojiItemGrumpy;
    ItemStack itemStackToThrow = new ItemStack(itemToThrow);

    world.playSound(null, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(),
            soundEvent, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    if (!world.isRemote) {
      EmojiEntity emojiEntity = new EmojiEntity(world, playerEntity);
      emojiEntity.setItem(itemStackToThrow);
      emojiEntity.shoot(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 0.0F, 1.5F, 1.0F);
      world.addEntity(emojiEntity);
    }

    playerEntity.addStat(Stats.ITEM_USED.get(this));
    if (!playerEntity.abilities.isCreativeMode) {
      heldItem.shrink(1);
    }

    return ActionResult.resultSuccess(heldItem);
  }

  public EmojiMood getEmojiMood() {return emojiMood;}

  public enum EmojiMood {HAPPY, GRUMPY};

  private EmojiMood emojiMood;
}
