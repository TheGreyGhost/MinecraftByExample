package minecraftbyexample.mbe65_capability;

import com.google.common.collect.Lists;
import minecraftbyexample.usefultools.NBTtypesMBE;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

 /**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemElementalCrossbowAir is used to fire arrows which have been given Elemental Air
 *   The arrow does no damage when it hits.
 *
 * Most of this class is copied directly from CrossbowItem.
 *
 */
public class ItemElementalCrossbowAir extends CrossbowItem {  // need to extend CrossbowItem otherwise the pull-string-back rendering does not work properly
  public ItemElementalCrossbowAir() {

    super(new Properties().maxStackSize(1).group(ItemGroup.COMBAT)
    );
    this.addPropertyOverride(new ResourceLocation("pullfraction"), ItemElementalCrossbowAir::getPullFraction);
    this.addPropertyOverride(new ResourceLocation("isbeingpulled"), ItemElementalCrossbowAir::isBeingPulled);
    this.addPropertyOverride(new ResourceLocation("ischarged"), ItemElementalCrossbowAir::isFullyCharged);
  }

   /** Set the Elemental air level of the given arrow
    * @param abstractArrowEntity
    * @param airCharge
    */
   private static void setElementalAirLevel(Entity abstractArrowEntity, int airCharge) {
     ElementalAir airInterface = abstractArrowEntity.getCapability(CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR).orElse(null);
     if (airInterface == null) return;
     airInterface.addCharge(airCharge);
   }

   /**
    * Copied mostly from vanilla, modified to impart air elemental energy to the arrow that is being fired
    */
   private static void fireProjectile(World world, LivingEntity livingEntity, Hand hand, ItemStack crossbowItemStack, ItemStack projectileItemStack,
                                      float soundPitch, boolean isCreativeMode, float speed, float inaccuracy, float yawAngle) {
     if (!world.isRemote) {
       AbstractArrowEntity abstractArrowEntity = createArrow(world, livingEntity, crossbowItemStack, projectileItemStack);

       if (livingEntity instanceof ICrossbowUser) {
         ICrossbowUser crossbowUser = (ICrossbowUser)livingEntity;
         crossbowUser.shoot(crossbowUser.getAttackTarget(), crossbowItemStack, abstractArrowEntity, yawAngle);
       } else {
         Vec3d headVerticalAxis = livingEntity.getUpVector(1.0F);
         Quaternion rotationAboutHeadVerticalAxis = new Quaternion(new Vector3f(headVerticalAxis), yawAngle, true);
         Vec3d lookVector = livingEntity.getLook(1.0F);
         Vector3f lookVector3f = new Vector3f(lookVector);
         lookVector3f.transform(rotationAboutHeadVerticalAxis);
         abstractArrowEntity.shoot(lookVector3f.getX(), lookVector3f.getY(), lookVector3f.getZ(), speed, inaccuracy);
       }

       // crossbow always fires the same charge
       final int MAX_CHARGE = 100;
       ItemElementalCrossbowAir.setElementalAirLevel(abstractArrowEntity, MAX_CHARGE);


       world.addEntity(abstractArrowEntity);
       world.playSound((PlayerEntity) null, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(),
               SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
     }
   }

   // --------- the remaining code is all directly from vanilla -------------

   private boolean isLoadingStart = false;
   private boolean isLoadingMiddle = false;

   @Override
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

   /** How long has the player been pulling the crossbow back for?
    * @return fraction pulled back (0.0--> 1.0 plus a bit);  returns 0.0 if already charged
    */
   public static float getPullFraction(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
     final float NO_PULL = 0.0F;
     if (livingEntity == null) return NO_PULL;
     if (livingEntity.getActiveItemStack() != itemStack) return NO_PULL;
     if (isCharged(itemStack)) return NO_PULL;

     int pullDurationTicks = itemStack.getUseDuration() - livingEntity.getItemInUseCount();   // getItemInUseCount starts from maximum!
     float pullFraction = pullDurationTicks / (float)getChargeTime(itemStack);
     return pullFraction;
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

   @Override
   public Predicate<ItemStack> getAmmoPredicate() {
     return ARROWS;
   }

   @Override
   public Predicate<ItemStack> getInventoryAmmoPredicate() {
     return ARROWS;
   }

  @Override
  public void onPlayerStoppedUsing(ItemStack itemStack, World world, LivingEntity livingEntity, int timeLeft) {

    int pullDurationTicks = this.getUseDuration(itemStack) - timeLeft;
    float fractionCharged = getFractionCharged(pullDurationTicks, itemStack);
    if (fractionCharged >= 1.0F && !isCharged(itemStack) && hasAmmo(livingEntity, itemStack)) {
      setCharged(itemStack, true);
      SoundCategory soundCategory = livingEntity instanceof PlayerEntity?SoundCategory.PLAYERS:SoundCategory.HOSTILE;
      world.playSound(null, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(),
              SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
    }

  }

  private static boolean hasAmmo(LivingEntity livingEntity, ItemStack crossbowItemStack) {
    int multishotFlag = EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, crossbowItemStack);
    int numberOfShots = multishotFlag == 0 ? 1 : 3;
    boolean creativeMode = livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).abilities.isCreativeMode;
    ItemStack ammoItemStack = livingEntity.findAmmo(crossbowItemStack);
    ItemStack ammoItemStackCopy = ammoItemStack.copy();

    for (int i = 0; i < numberOfShots; ++i) {
      if (i > 0) {
        ammoItemStack = ammoItemStackCopy.copy();
      }

      if (ammoItemStack.isEmpty() && creativeMode) {
        ammoItemStack = new ItemStack(Items.ARROW);
        ammoItemStackCopy = ammoItemStack.copy();
      }

      if (!storeChargedAmmo(livingEntity, crossbowItemStack, ammoItemStack, i > 0, creativeMode)) {
        return false;
      }
    }

    return true;
  }

  private static boolean storeChargedAmmo(LivingEntity shooter, ItemStack crossbowItemStack, ItemStack ammoItemStack, boolean notFirstBolt, boolean creativeMode) {
    if (ammoItemStack.isEmpty()) {
      return false;
    } else {
      boolean ammoIsCreativeArrow = creativeMode && ammoItemStack.getItem() instanceof ArrowItem;
      ItemStack chargedAmmoItemStack;
      if (ammoIsCreativeArrow || creativeMode || notFirstBolt) {
        chargedAmmoItemStack = ammoItemStack.copy();
      } else {
        chargedAmmoItemStack = ammoItemStack.split(1);
        if (ammoItemStack.isEmpty() && shooter instanceof PlayerEntity) {
          ((PlayerEntity)shooter).inventory.deleteStack(ammoItemStack);
        }
      }

      addChargedProjectile(crossbowItemStack, chargedAmmoItemStack);
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

  private static AbstractArrowEntity createArrow(World world, LivingEntity livingEntity, ItemStack crossbowItemStack, ItemStack arrowItemStack) {
    ArrowItem arrowItem = (ArrowItem)(arrowItemStack.getItem() instanceof ArrowItem ? arrowItemStack.getItem() : Items.ARROW);
    AbstractArrowEntity abstractArrowEntity = arrowItem.createArrow(world, arrowItemStack, livingEntity);
    if (livingEntity instanceof PlayerEntity) {
      abstractArrowEntity.setIsCritical(true);
    }

    abstractArrowEntity.setHitSound(SoundEvents.ITEM_CROSSBOW_HIT);
    abstractArrowEntity.setShotFromCrossbow(true);
    int pierceLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, crossbowItemStack);
    if (pierceLevel > 0) {
      abstractArrowEntity.setPierceLevel((byte) pierceLevel);
    }

    return abstractArrowEntity;
  }

  public static void fireProjectiles(World world, LivingEntity livingEntity, Hand hand, ItemStack crossbowItemStack, float speed, float inaccuracy) {
    List<ItemStack> chargedProjectiles = getChargedProjectiles(crossbowItemStack);
    float[] randomSoundPitches = getRandomSoundPitches(livingEntity.getRNG());

    for (int i = 0; i < chargedProjectiles.size(); ++i) {
      ItemStack chargedProjectile = chargedProjectiles.get(i);
      boolean isCreativeMode = livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).abilities.isCreativeMode;
      if (!chargedProjectile.isEmpty()) {
        if (i == 0) {
          fireProjectile(world, livingEntity, hand, crossbowItemStack, chargedProjectile, randomSoundPitches[i], isCreativeMode, speed, inaccuracy, 0.0F);
        } else if (i == 1) {
          fireProjectile(world, livingEntity, hand, crossbowItemStack, chargedProjectile, randomSoundPitches[i], isCreativeMode, speed, inaccuracy, -10.0F);
        } else if( i == 2) {
          fireProjectile(world, livingEntity, hand, crossbowItemStack, chargedProjectile, randomSoundPitches[i], isCreativeMode, speed, inaccuracy, 10.0F);
        }
      }
    }
    fireProjectilesAfter(world, livingEntity, crossbowItemStack);
  }

  private static float[] getRandomSoundPitches(Random rnd) {
    boolean randBoolean = rnd.nextBoolean();
    return new float[]{1.0F, getRandomSoundPitch(randBoolean), getRandomSoundPitch(!randBoolean)};
  }

  private static float getRandomSoundPitch(boolean randFlag) {
    float f = randFlag?0.63F:0.43F;
    return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
  }

  private static void fireProjectilesAfter(World world, LivingEntity livingEntity, ItemStack crossbowItemStack) {
    if(livingEntity instanceof ServerPlayerEntity) {
      ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)livingEntity;
      if (!world.isRemote) {
        CriteriaTriggers.SHOT_CROSSBOW.func_215111_a(serverPlayerEntity, crossbowItemStack);
      }

      serverPlayerEntity.addStat(Stats.ITEM_USED.get(crossbowItemStack.getItem()));
    }

    clearProjectiles(crossbowItemStack);
  }

  /**
   * If this itemstack's item is a crossbow
   * Otherwise - the ticksRemaining gets reset to maximum at the end of the pulling-back-the-string, rather than holding at 0
   */
  @Override
  public boolean isCrossbow(ItemStack stack) {
    return true;
  }

  @Override
  public void onUse(World world, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
    if (!world.isRemote) {
      int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack);
      SoundEvent soundEvent = this.getSoundEvent(enchantmentLevel);
      SoundEvent soundEvent1 = enchantmentLevel == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
      float fractionCharged = (itemStack.getUseDuration() - ticksRemaining) / (float)getChargeTime(itemStack);
      if (fractionCharged < 0.2F) {
        this.isLoadingStart = false;
        this.isLoadingMiddle = false;
      }

      if (fractionCharged >= 0.2F && !this.isLoadingStart) {
        this.isLoadingStart = true;
        world.playSound((PlayerEntity)null, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(), soundEvent, SoundCategory.PLAYERS, 0.5F, 1.0F);
      }

      if (fractionCharged >= 0.5F && soundEvent1 != null && !this.isLoadingMiddle) {
        this.isLoadingMiddle = true;
        world.playSound((PlayerEntity)null, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(), soundEvent1, SoundCategory.PLAYERS, 0.5F, 1.0F);
      }
    }
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    return getChargeTime(itemStack) + 3;
  }

  public static int getChargeTime(ItemStack itemStack) {
    int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack);
    return enchantmentLevel == 0 ? 25 : 25 - 5 * enchantmentLevel;
  }

  @Override
  public UseAction getUseAction(ItemStack itemStack) {
    return UseAction.CROSSBOW;
  }

  private SoundEvent getSoundEvent(int eventID) {
    switch(eventID) {
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

  private static float getFractionCharged(int pullDurationTicks, ItemStack itemStack) {
    float fractionCharged = (float)pullDurationTicks / (float)getChargeTime(itemStack);
    if(fractionCharged > 1.0F) {
      fractionCharged = 1.0F;
    }

    return fractionCharged;
  }

  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> textComponentList, ITooltipFlag tooltipFlag) {
    List chargedProjectiles = getChargedProjectiles(itemStack);
    if (isCharged(itemStack) && !chargedProjectiles.isEmpty()) {
      ItemStack firstProjectile = (ItemStack)chargedProjectiles.get(0);
      textComponentList.add((new TranslationTextComponent("item.minecraft.crossbow.projectile", new Object[0]))
              .appendText(" ").appendSibling(firstProjectile.getTextComponent()));
      if (tooltipFlag.isAdvanced() && firstProjectile.getItem() == Items.FIREWORK_ROCKET) {
        ArrayList arrayList = Lists.newArrayList();
        Items.FIREWORK_ROCKET.addInformation(firstProjectile, world, arrayList, tooltipFlag);
        if (!arrayList.isEmpty()) {
          for (int i = 0; i < arrayList.size(); ++i) {
            arrayList.set(i, (new StringTextComponent("  ")).appendSibling((ITextComponent)arrayList.get(i)).applyTextStyle(TextFormatting.GRAY));
          }

          textComponentList.addAll(arrayList);
        }
      }

    }
  }

  private static final Logger LOGGER = LogManager.getLogger();

}
