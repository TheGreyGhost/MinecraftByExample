package minecraftbyexample.mbe12_item_nbt_animate;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 * Item (teleportation gem) which stores NBT information and also provides a custom animation when being "used".
 * Basic usage:
 * 1) Shift-click to "store" the current location in the gem
 * 2) Hold right button down to "charge up" the gem.  When fully charged, the gem teleports you to the last saved
 *    location.  The gem is destroyed.
 */
public class ItemNBTAnimate extends Item
{
  static private final int MAXIMUM_NUMBER_OF_GEMS = 1; // maximum stack size

  public ItemNBTAnimate() {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_GEMS).group(ItemGroup.MISC));
          // item will appear on the Miscellaneous creative tab

    // We use a PropertyOverride for this item to change the appearance depending on the state of the property.
    //  See ItemNBTanimationTimer for more information.
    // ItemNBTanimationTimer() is used as a lambda function to calculate the current chargefraction during rendering
    this.addPropertyOverride(new ResourceLocation("chargefraction"), new ItemNBTanimationTimer());
  }

  // When the user presses and holds right click, there are three phases:
  // 1) an initial pause, then
  // 2) a visual 'charging up' of the gem, then
  // 3) the teleportation occurs
  // NB there are twenty minecraft game loop "ticks" per second.
  static public final int CHARGE_UP_INITIAL_PAUSE_TICKS = 10;
  static public final int CHARGE_UP_DURATION_TICKS = 20;

  // if the gem is bound to a location, give it an "effect" i.e. the enchanted glint
  @Override
  public boolean hasEffect(ItemStack stack) {
    CompoundNBT nbtTagCompound = stack.getTag();
    if (nbtTagCompound == null) return false;
    return nbtTagCompound.contains("bound");
  }

  // what animation to use when the player holds the "use" button
  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.NONE;
  }

  // how long the player needs to hold down the right button in order to activate the gem, in ticks (1 tick = 1/20 second)
  @Override
  public int getUseDuration(ItemStack stack) {
    return CHARGE_UP_DURATION_TICKS + CHARGE_UP_INITIAL_PAUSE_TICKS;
  }

  // called when the player starts holding right click;
  // --> if the gem is unbound, store the current location
  //  if the gem is bound, start the charge up sequence
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
  {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    CompoundNBT nbtTagCompound = itemStackIn.getTag();

    if (playerIn.isSneaking()) { // player isSneaking (shift pressed); save (or overwrite) current location
      if (nbtTagCompound == null) {
        nbtTagCompound = new CompoundNBT();
        itemStackIn.setTag(nbtTagCompound);
      }
      Vec3d playerPos = playerIn.getPositionVec();
      nbtTagCompound.putBoolean("bound", true);
      nbtTagCompound.putDouble("x", (int)playerPos.getX());
      nbtTagCompound.putDouble("y", (int)playerPos.getY());
      nbtTagCompound.putDouble("z", (int)playerPos.getZ());
      return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    boolean bound = false;
    if (nbtTagCompound != null && nbtTagCompound.contains("bound")  ) {
      bound = nbtTagCompound.getBoolean("bound");
    }
    if (bound) {
      playerIn.setActiveHand(hand); // start the charge up sequence
      return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    } else {
      if (worldIn.isRemote) {  // only on the client side, else you will get two messages..
        final boolean PRINT_IN_CHAT_WINDOW = true;
        playerIn.sendStatusMessage(new StringTextComponent("Gem doesn't have a stored location! Shift right click to store your current location"),
                PRINT_IN_CHAT_WINDOW);
      }
      return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
    }
  }

  // called when the player has held down the right click for the full charge-up duration
  // in this case - destroy the item
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
  {
    CompoundNBT nbtTagCompound = stack.getTag();
    if (nbtTagCompound == null || !nbtTagCompound.contains("bound") || nbtTagCompound.getBoolean("bound") != true ) {
      return stack;
    }

    double x = nbtTagCompound.getDouble("x");  // returns a default if not present
    double y = nbtTagCompound.getDouble("y");
    double z = nbtTagCompound.getDouble("z");

    // teleport

    // on the client side, play the sound locally
    // on the server side, teleport the player and play the sound for all other players nearby except this player
    //  (doing it this way reduces the perceived lag for this player, i.e the sound plays instantly, instead of being
    //   delayed while the message goes to the server and comes back again)
    if (worldIn.isRemote) {  // client side
      worldIn.playSound(x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
    } else {  // server side
      if (entityLiving instanceof ServerPlayerEntity) { // should be an EntityPlayerMP; check first just to be sure to avoid crash
        ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)entityLiving;
        entityPlayerMP.connection.setPlayerLocation(x, y, z, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
        final ServerPlayerEntity dontPlayForThisPlayer = entityPlayerMP;
        worldIn.playSound(dontPlayForThisPlayer, x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
      }
    }
    return ItemStack.EMPTY;  // EMPTY_ITEM
//    for item with multiple count, decrease stack size and return the itemstack, eg
//    stack.shrink(1)
//    return stack;
  }

  // adds 'tooltip' text
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    CompoundNBT nbtTagCompound = stack.getTag();
    if (nbtTagCompound != null && nbtTagCompound.contains("bound") && nbtTagCompound.getBoolean("bound") == true ) {
      tooltip.add(new StringTextComponent("Stored destination="));
      tooltip.add(new StringTextComponent("X: " + nbtTagCompound.getInt("x")));
      tooltip.add(new StringTextComponent("Y: " + nbtTagCompound.getInt("y")));
      tooltip.add(new StringTextComponent("Z: " + nbtTagCompound.getInt("z")));
      tooltip.add(new StringTextComponent("Hold down right click to teleport."));
    }
    else {
      tooltip.add(new StringTextComponent("Hold down sneak (shift) and then right"));
      tooltip.add(new StringTextComponent("  click to store your current location"));
    }
  }
}