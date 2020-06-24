package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Created by TGG on 24/06/2020.
 * Heavily based on the vanilla SnowballItem
 */
public class EmojiItem extends Item {
  static private final int MAXIMUM_NUMBER_OF_EMOJI = 64; // maximum stack size

  public EmojiItem() {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_EMOJI).group(ItemGroup.BREWING)
    );
  }

  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity, Hand hand) {
    ItemStack heldItem = playerEntity.getHeldItem(hand);
    world.playSound((PlayerEntity) null, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    if(!world.isRemote) {
      SnowballEntity lvt_5_1_ = new SnowballEntity(world, playerEntity);
      lvt_5_1_.setItem(heldItem);
      lvt_5_1_.shoot(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 0.0F, 1.5F, 1.0F);
      world.addEntity(lvt_5_1_);
    }

    playerEntity.addStat(Stats.ITEM_USED.get(this));
    if(!playerEntity.abilities.isCreativeMode) {
      heldItem.shrink(1);
    }

    return ActionResult.resultSuccess(heldItem);
  }
}
