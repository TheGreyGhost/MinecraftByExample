package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * The item has a number of animation frames depending on what the player is doing.
 *  Idle frame, when the player is not using the item
 *  ChargeUp frames, when the player is holding use and the item is "charging up".  There are five of these animation frames.
 *  Each time the item is rendered, vanilla looks for any "predicates" in the item's json.  For each one it finds, it
 *    looks up the corresponding IItemPropertyGetter, and calls apply() to retrieve an animation frame index
 *    (this index is typically in the range 0 -> 1 but can be wider).
 *
 * For example:
 *   The ItemClock has the following in its json file
 *       "overrides": [
           { "predicate": { "time": 0.000 }, "model": "item/clock" },
           { "predicate": { "time": 0.100 }, "model": "item/clock_01" },
           { "predicate": { "time": 0.200 }, "model": "item/clock_02" },

 *   and adds the following property:
 *   ResourceLocation("angle") with an IItemPropertyGetter() that returns a value from 0.0 to 1.0 depending on the time of day
 *  The item renderer searches through the list of "time" values until it finds one which is larger than the current time:
 *    so if apply() returns 0.101, it will use clock_01;
 *       if apply() returns 0.200 or more, it will use clock_02
 *
 * See also ItemClock and ItemCompass
 *
 * This class should not be used in a DedicatedServer because IItemPropertyGetter does not exist there.
 *
 *
 * Created by TGG on 18/08/2016.
 */
public class ItemNBTanimationTimer implements IItemPropertyGetter {

  @Override
    public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
    {
      final float IDLE_FRAME_INDEX = 0.0F;
      final float FULLY_CHARGED_INDEX = 1.0F;

      if (entityIn == null) return IDLE_FRAME_INDEX;
      if (!entityIn.isHandActive()) {
        animationHasStarted = false;
        return IDLE_FRAME_INDEX;
      }

      long worldTicks = worldIn.getTotalWorldTime();
      if (!animationHasStarted) {
        startingTick = worldTicks;
        animationHasStarted = true;
      }
      final long ticksInUse = worldTicks - startingTick;
      if (ticksInUse <= ItemNBTAnimate.CHARGE_UP_INITIAL_PAUSE_TICKS) {
        return IDLE_FRAME_INDEX;
      }

      final long chargeTicksSoFar = ticksInUse - ItemNBTAnimate.CHARGE_UP_INITIAL_PAUSE_TICKS;
      final double fractionCharged = chargeTicksSoFar/(double)ItemNBTAnimate.CHARGE_UP_DURATION_TICKS;
      if (fractionCharged < 0.0) return IDLE_FRAME_INDEX;
      if (fractionCharged > FULLY_CHARGED_INDEX) return FULLY_CHARGED_INDEX;

      return (float)fractionCharged*FULLY_CHARGED_INDEX;
  }

  private long startingTick = -1;
  private boolean animationHasStarted = false;
}
