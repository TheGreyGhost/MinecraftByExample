package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * The item has a number of animation frames depending on what the player is doing.
 *  Idle frame, when the player is not using the item (mbe12_item_nbt_animate_0)
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

 *   In its constructor, it adds a property "time" and creates a corresponding IItemPropertyGetter() that returns a
 *      value from 0.0 to 1.0 depending on the time of day
 *      (see Item.addPropertyOverride())
 *  When the item is rendered, the item renderer
 *  1) notices that ItemClock has an override called "time".
 *  2) retrieves the corresponding IItemPropertyGetter()
 *  3)  ... calls the apply() method, to get an animation frame index, for example 0.101
 *  4) searches through the list of "time" values from the json until it finds one which is larger than animation frame
 *      index.
 *    eg if apply() returns 0.101, it will use clock_01;
 *       if apply() returns 0.200 or more, it will use clock_02
 *
 * See also ItemClock and ItemCompass
 *
 * This class should not be used in a DedicatedServer because IItemPropertyGetter does not exist there.
 *
 * Created by TGG on 18/08/2016.
 */
public class ItemNBTanimationTimer implements IItemPropertyGetter {

  /** Changes the item's animation frame depending on what the user is doing
   * a) idle = use default model = mbe12_item_nbt_animate_0
   * b) when the user has been holding down the button, advance through the animation frames _1 to _5.  The texture
   *    changes, as well as the ItemCameraTransform (the "scale", in the json files)
   *
   * from the json:
      "overrides": [
           { "predicate": { "chargefraction": 0.000 }, "model": "minecraftbyexample:item/mbe12_item_nbt_animate_0" },
           { "predicate": { "chargefraction": 0.001 }, "model": "minecraftbyexample:item/mbe12_item_nbt_animate_1" },
           { "predicate": { "chargefraction": 0.200 }, "model": "minecraftbyexample:item/mbe12_item_nbt_animate_2" },
           { "predicate": { "chargefraction": 0.400 }, "model": "minecraftbyexample:item/mbe12_item_nbt_animate_3" },
           { "predicate": { "chargefraction": 0.600 }, "model": "minecraftbyexample:item/mbe12_item_nbt_animate_4" },
           { "predicate": { "chargefraction": 0.800 }, "model": "minecraftbyexample:item/mbe12_item_nbt_animate_5" }
        ]
   *
   * @param stack
   * @param worldIn null when rendering in a GUI such as inventory or the hotbar, or in an ItemFrame (eg picture frame)
   * @param entityIn null when in the world as an EntityItem (GROUND)
   * @return the appropriate animation frame index expected by the "overrides" section in the item json file
   */
  @Override
    public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
    {
      final float IDLE_FRAME_INDEX = 0.0F;
      final float FULLY_CHARGED_INDEX = 1.0F;

      if (worldIn == null && entityIn != null)  {
        worldIn = entityIn.world;
      }

      if (entityIn == null || worldIn == null) return IDLE_FRAME_INDEX;
      if (!entityIn.isHandActive()) {  // player isn't holding down the right mouse button, i.e. not charging
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
