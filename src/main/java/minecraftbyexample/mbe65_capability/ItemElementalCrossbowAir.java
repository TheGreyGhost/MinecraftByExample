package minecraftbyexample.mbe65_capability;

import com.google.common.collect.Lists;
import minecraftbyexample.usefultools.NBTtypesMBE;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by TGG on 19/06/2020.
 */
public class ItemElementalCrossbowAir extends ShootableItem {
  private boolean isLoadingStart = false;
  private boolean isLoadingMiddle = false;

  static private final int MAXIMUM_NUMBER_OF_FROGS = 6; // maximum stack size

  //todo make arrow disappear on hit

  public ItemElementalCrossbowAir() {

    super(new Properties().maxStackSize(MAXIMUM_NUMBER_OF_FROGS).group(ItemGroup.MISC) // the item will appear on the Miscellaneous tab in creative
    );
    this.addPropertyOverride(new ResourceLocation("pullfraction"), ItemElementalCrossbowAir::getPullFraction);
    this.addPropertyOverride(new ResourceLocation("isbeingpulled"), ItemElementalCrossbowAir::isBeingPulled);
    this.addPropertyOverride(new ResourceLocation("ischarged"), ItemElementalCrossbowAir::isFullyCharged);
  }

  /** How long has the player been pulling the crossbow back for?
   * @return fraction pulled back (0.0--> 1.0);
   */
  public static float getPullFraction(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
    final float NO_PULL = 0.0F;
    final float MAX_PULL = 1.0F;
    if (livingEntity == null) return NO_PULL;
    if (livingEntity.getActiveItemStack() != itemStack) return NO_PULL;
    if (isCharged(itemStack)) return MAX_PULL;

    int pullDurationTicks = itemStack.getUseDuration() - livingEntity.getItemInUseCount();   // getItemInUseCount starts from maximum!
    return (float)UsefulFunctions.interpolate_with_clipping(pullDurationTicks, 0, getChargeTime(itemStack), NO_PULL, MAX_PULL);
  }

  /** Is the crossbow currently being pulled back?
   * @return 0.0 = not pulled,  1.0 = yes.  Once fully charged, returns 0.0
   */
  public static float isBeingPulled(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
    final float NOT_PULLED = 0.0F;
    final float IS_PULLED = 1.0F;
    if (livingEntity == null) return NOT_PULLED;
    if (livingEntity.isHandActive()
            && livingEntity.getActiveItemStack() == itemStack
            && !isCharged(itemStack)
            ) return IS_PULLED;
    return NOT_PULLED;
  }

  /** Is the crossbow fully charged?
   * @return 0.0 = no,  1.0 = yes.
   */
  public static float isFullyCharged(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
    return livingEntity != null && isCharged(itemStack) ? 1.0F : 0.0F;
  }

  public Predicate<ItemStack> getAmmoPredicate() {
    return ARROWS;
  }

  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return ARROWS;
  }

  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity, Hand hand) {
    ItemStack heldItem = playerEntity.getHeldItem(hand);
    if (isCharged(heldItem)) {
      final float ARROW_SPEED = 1.6F;
      fireProjectiles(world, playerEntity, hand, heldItem, ARROW_SPEED, 1.0F);
      setCharged(heldItem, false);
      return ActionResult.resultConsume(heldItem);
    } else if (!playerEntity.findAmmo(heldItem).isEmpty()) {
      if (!isCharged(heldItem)) {
        this.isLoadingStart = false;
        this.isLoadingMiddle = false;
        playerEntity.setActiveHand(hand);
      }

      return ActionResult.resultConsume(heldItem);
    } else {
      return ActionResult.resultFail(heldItem);
    }
  }

  public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
    int lvt_5_1_ = this.getUseDuration(p_77615_1_) - p_77615_4_;
    float lvt_6_1_ = getCharge(lvt_5_1_, p_77615_1_);
    if(lvt_6_1_ >= 1.0F && !isCharged(p_77615_1_) && hasAmmo(p_77615_3_, p_77615_1_)) {
      setCharged(p_77615_1_, true);
      SoundCategory lvt_7_1_ = p_77615_3_ instanceof PlayerEntity?SoundCategory.PLAYERS:SoundCategory.HOSTILE;
      p_77615_2_.playSound((PlayerEntity)null, p_77615_3_.getPosX(), p_77615_3_.getPosY(), p_77615_3_.getPosZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, lvt_7_1_, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
    }

  }

  private static boolean hasAmmo(LivingEntity p_220021_0_, ItemStack p_220021_1_) {
    int lvt_2_1_ = EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, p_220021_1_);
    int lvt_3_1_ = lvt_2_1_ == 0?1:3;
    boolean lvt_4_1_ = p_220021_0_ instanceof PlayerEntity && ((PlayerEntity)p_220021_0_).abilities.isCreativeMode;
    ItemStack lvt_5_1_ = p_220021_0_.findAmmo(p_220021_1_);
    ItemStack lvt_6_1_ = lvt_5_1_.copy();

    for(int lvt_7_1_ = 0; lvt_7_1_ < lvt_3_1_; ++lvt_7_1_) {
      if(lvt_7_1_ > 0) {
        lvt_5_1_ = lvt_6_1_.copy();
      }

      if(lvt_5_1_.isEmpty() && lvt_4_1_) {
        lvt_5_1_ = new ItemStack(Items.ARROW);
        lvt_6_1_ = lvt_5_1_.copy();
      }

      if(!func_220023_a(p_220021_0_, p_220021_1_, lvt_5_1_, lvt_7_1_ > 0, lvt_4_1_)) {
        return false;
      }
    }

    return true;
  }

  private static boolean func_220023_a(LivingEntity p_220023_0_, ItemStack p_220023_1_, ItemStack p_220023_2_, boolean p_220023_3_, boolean p_220023_4_) {
    if(p_220023_2_.isEmpty()) {
      return false;
    } else {
      boolean lvt_5_1_ = p_220023_4_ && p_220023_2_.getItem() instanceof ArrowItem;
      ItemStack lvt_6_2_;
      if(!lvt_5_1_ && !p_220023_4_ && !p_220023_3_) {
        lvt_6_2_ = p_220023_2_.split(1);
        if(p_220023_2_.isEmpty() && p_220023_0_ instanceof PlayerEntity) {
          ((PlayerEntity)p_220023_0_).inventory.deleteStack(p_220023_2_);
        }
      } else {
        lvt_6_2_ = p_220023_2_.copy();
      }

      addChargedProjectile(p_220023_1_, lvt_6_2_);
      return true;
    }
  }

  public static boolean isCharged(ItemStack crossbowItemStack) {
    CompoundNBT crossbowBaseTag = crossbowItemStack.getTag();
    return crossbowBaseTag != null && crossbowBaseTag.getBoolean("Charged");
  }

  public static void setCharged(ItemStack itemStackCrossbow, boolean charged) {
    CompoundNBT crossbowBaseTag = itemStackCrossbow.getOrCreateTag();
    crossbowBaseTag.putBoolean("Charged", charged);
  }

  private static void addChargedProjectile(ItemStack itemStackCrossbow, ItemStack itemStackProjectile) {
    CompoundNBT crossbowBaseTag = itemStackCrossbow.getOrCreateTag();
    ListNBT listOfProjectilesNBT;
    if (crossbowBaseTag.contains("ChargedProjectiles", NBTtypesMBE.COMPOUND_NBT_ID)) {
      listOfProjectilesNBT = crossbowBaseTag.getList("ChargedProjectiles", NBTtypesMBE.LIST_NBT_ID);
    } else {
      listOfProjectilesNBT = new ListNBT();
    }

    CompoundNBT projectileNBT = new CompoundNBT();
    itemStackProjectile.write(projectileNBT);
    listOfProjectilesNBT.add(projectileNBT);
    crossbowBaseTag.put("ChargedProjectiles", listOfProjectilesNBT);
  }

  private static List<ItemStack> getChargedProjectiles(ItemStack crossbowItemStack) {
    ArrayList projectileList = Lists.newArrayList();
    CompoundNBT crossbowBaseTag = crossbowItemStack.getTag();
    if (crossbowBaseTag != null && crossbowBaseTag.contains("ChargedProjectiles", NBTtypesMBE.LIST_NBT_ID)) {
      ListNBT chargedProjectilesNBTlist = crossbowBaseTag.getList("ChargedProjectiles", NBTtypesMBE.COMPOUND_NBT_ID);
      if (chargedProjectilesNBTlist != null) {
        for (int i = 0; i < chargedProjectilesNBTlist.size(); ++i) {
          CompoundNBT projectileNBT = chargedProjectilesNBTlist.getCompound(i);
          projectileList.add(ItemStack.read(projectileNBT));
        }
      }
    }

    return projectileList;
  }

  private static void clearProjectiles(ItemStack crossbowItemStack) {
    CompoundNBT crossbowBaseTag = crossbowItemStack.getTag();
    if (crossbowBaseTag != null) {
      ListNBT chargedProjectileListNBT = crossbowBaseTag.getList("ChargedProjectiles", NBTtypesMBE.LIST_NBT_ID);
      chargedProjectileListNBT.clear();
      crossbowBaseTag.put("ChargedProjectiles", chargedProjectileListNBT);
    }
  }

  private static void fireProjectile(World world, LivingEntity livingEntity, Hand hand, ItemStack crossbowItemStack, ItemStack projectileItemStack,
                                     float soundPitch, boolean isCreativeMode, float speed, float p_220016_8_, float p_220016_9_) {
    if (!world.isRemote) {
      AbstractArrowEntity abstractArrowEntity = createArrow(world, livingEntity, crossbowItemStack, projectileItemStack);

      if (livingEntity instanceof ICrossbowUser) {
        ICrossbowUser lvt_12_2_ = (ICrossbowUser)livingEntity;
        lvt_12_2_.shoot(lvt_12_2_.getAttackTarget(), crossbowItemStack, abstractArrowEntity, p_220016_9_);
      } else {
        Vec3d lvt_12_2_1 = livingEntity.getUpVector(1.0F);
        Quaternion lvt_13_1_ = new Quaternion(new Vector3f(lvt_12_2_1), p_220016_9_, true);
        Vec3d lvt_14_1_ = livingEntity.getLook(1.0F);
        Vector3f lvt_15_1_ = new Vector3f(lvt_14_1_);
        lvt_15_1_.transform(lvt_13_1_);
        ((IProjectile)lvt_11_2_).shoot((double)lvt_15_1_.getX(), (double)lvt_15_1_.getY(), (double)lvt_15_1_.getZ(), speed, p_220016_8_);
      }

      world.addEntity((Entity) lvt_11_2_);
      world.playSound((PlayerEntity) null, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(),
              SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
    }
  }

  private static AbstractArrowEntity createArrow(World p_220024_0_, LivingEntity p_220024_1_, ItemStack p_220024_2_, ItemStack p_220024_3_) {
    ArrowItem lvt_4_1_ = (ArrowItem)((ArrowItem)(p_220024_3_.getItem() instanceof ArrowItem?p_220024_3_.getItem():Items.ARROW));
    AbstractArrowEntity lvt_5_1_ = lvt_4_1_.createArrow(p_220024_0_, p_220024_3_, p_220024_1_);
    if(p_220024_1_ instanceof PlayerEntity) {
      lvt_5_1_.setIsCritical(true);
    }

    lvt_5_1_.setHitSound(SoundEvents.ITEM_CROSSBOW_HIT);
    lvt_5_1_.setShotFromCrossbow(true);
    int lvt_6_1_ = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, p_220024_2_);
    if(lvt_6_1_ > 0) {
      lvt_5_1_.setPierceLevel((byte)lvt_6_1_);
    }

    return lvt_5_1_;
  }

  public static void fireProjectiles(World world, LivingEntity livingEntity, Hand hand, ItemStack crossbowItemStack, float speed, float p_220014_5_) {
    List<ItemStack> chargedProjectiles = getChargedProjectiles(crossbowItemStack);
    float[] randomSoundPitches = getRandomSoundPitches(livingEntity.getRNG());

    for (int i = 0; i < chargedProjectiles.size(); ++i) {
      ItemStack chargedProjectile = chargedProjectiles.get(i);
      boolean isCreativeMode = livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).abilities.isCreativeMode;
      if (!chargedProjectile.isEmpty()) {
        if (i == 0) {
          fireProjectile(world, livingEntity, hand, crossbowItemStack, chargedProjectile, randomSoundPitches[i], isCreativeMode, speed, p_220014_5_, 0.0F);
        } else if (i == 1) {
          fireProjectile(world, livingEntity, hand, crossbowItemStack, chargedProjectile, randomSoundPitches[i], isCreativeMode, speed, p_220014_5_, -10.0F);
        } else if( i == 2) {
          fireProjectile(world, livingEntity, hand, crossbowItemStack, chargedProjectile, randomSoundPitches[i], isCreativeMode, speed, p_220014_5_, 10.0F);
        }
      }
    }

    fireProjectilesAfter(world, livingEntity, crossbowItemStack);
  }

  private static float[] getRandomSoundPitches(Random p_220028_0_) {
    boolean lvt_1_1_ = p_220028_0_.nextBoolean();
    return new float[]{1.0F, getRandomSoundPitch(lvt_1_1_), getRandomSoundPitch(!lvt_1_1_)};
  }

  private static float getRandomSoundPitch(boolean p_220032_0_) {
    float lvt_1_1_ = p_220032_0_?0.63F:0.43F;
    return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + lvt_1_1_;
  }

  private static void fireProjectilesAfter(World p_220015_0_, LivingEntity p_220015_1_, ItemStack p_220015_2_) {
    if(p_220015_1_ instanceof ServerPlayerEntity) {
      ServerPlayerEntity lvt_3_1_ = (ServerPlayerEntity)p_220015_1_;
      if(!p_220015_0_.isRemote) {
        CriteriaTriggers.SHOT_CROSSBOW.func_215111_a(lvt_3_1_, p_220015_2_);
      }

      lvt_3_1_.addStat(Stats.ITEM_USED.get(p_220015_2_.getItem()));
    }

    clearProjectiles(p_220015_2_);
  }

  public void onUse(World p_219972_1_, LivingEntity p_219972_2_, ItemStack p_219972_3_, int p_219972_4_) {
    if(!p_219972_1_.isRemote) {
      int lvt_5_1_ = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, p_219972_3_);
      SoundEvent lvt_6_1_ = this.getSoundEvent(lvt_5_1_);
      SoundEvent lvt_7_1_ = lvt_5_1_ == 0?SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE:null;
      float lvt_8_1_ = (float)(p_219972_3_.getUseDuration() - p_219972_4_) / (float)getChargeTime(p_219972_3_);
      if(lvt_8_1_ < 0.2F) {
        this.isLoadingStart = false;
        this.isLoadingMiddle = false;
      }

      if(lvt_8_1_ >= 0.2F && !this.isLoadingStart) {
        this.isLoadingStart = true;
        p_219972_1_.playSound((PlayerEntity)null, p_219972_2_.getPosX(), p_219972_2_.getPosY(), p_219972_2_.getPosZ(), lvt_6_1_, SoundCategory.PLAYERS, 0.5F, 1.0F);
      }

      if(lvt_8_1_ >= 0.5F && lvt_7_1_ != null && !this.isLoadingMiddle) {
        this.isLoadingMiddle = true;
        p_219972_1_.playSound((PlayerEntity)null, p_219972_2_.getPosX(), p_219972_2_.getPosY(), p_219972_2_.getPosZ(), lvt_7_1_, SoundCategory.PLAYERS, 0.5F, 1.0F);
      }
    }

  }

  public int getUseDuration(ItemStack p_77626_1_) {
    return getChargeTime(p_77626_1_) + 3;
  }

  public static int getChargeTime(ItemStack p_220026_0_) {
    int lvt_1_1_ = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, p_220026_0_);
    return lvt_1_1_ == 0?25:25 - 5 * lvt_1_1_;
  }

  public UseAction getUseAction(ItemStack p_77661_1_) {
    return UseAction.CROSSBOW;
  }

  private SoundEvent getSoundEvent(int p_220025_1_) {
    switch(p_220025_1_) {
      case 1:
        return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
      case 2:
        return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
      case 3:
        return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
      default:
        return SoundEvents.ITEM_CROSSBOW_LOADING_START;
    }
  }

  private static float getCharge(int p_220031_0_, ItemStack p_220031_1_) {
    float lvt_2_1_ = (float)p_220031_0_ / (float)getChargeTime(p_220031_1_);
    if(lvt_2_1_ > 1.0F) {
      lvt_2_1_ = 1.0F;
    }

    return lvt_2_1_;
  }

  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
    List lvt_5_1_ = getChargedProjectiles(p_77624_1_);
    if(isCharged(p_77624_1_) && !lvt_5_1_.isEmpty()) {
      ItemStack lvt_6_1_ = (ItemStack)lvt_5_1_.get(0);
      p_77624_3_.add((new TranslationTextComponent("item.minecraft.crossbow.projectile", new Object[0])).appendText(" ").appendSibling(lvt_6_1_.getTextComponent()));
      if(p_77624_4_.isAdvanced() && lvt_6_1_.getItem() == Items.FIREWORK_ROCKET) {
        ArrayList lvt_7_1_ = Lists.newArrayList();
        Items.FIREWORK_ROCKET.addInformation(lvt_6_1_, p_77624_2_, lvt_7_1_, p_77624_4_);
        if(!lvt_7_1_.isEmpty()) {
          for(int lvt_8_1_ = 0; lvt_8_1_ < lvt_7_1_.size(); ++lvt_8_1_) {
            lvt_7_1_.set(lvt_8_1_, (new StringTextComponent("  ")).appendSibling((ITextComponent)lvt_7_1_.get(lvt_8_1_)).applyTextStyle(TextFormatting.GRAY));
          }

          p_77624_3_.addAll(lvt_7_1_);
        }
      }

    }
  }


}
