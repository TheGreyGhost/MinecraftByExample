package minecraftbyexample.mbe12_item_nbt_animate;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
  public ItemNBTAnimate() {
    this.setMaxDamage(0);
    this.setHasSubtypes(false);
    this.setMaxStackSize(1);
    this.setCreativeTab(CreativeTabs.MISC);   // items will appear on the Miscellaneous creative tab

    // We use a PropertyOverride for this item to change the appearance depending on the state of the property.
    //  See ItemNBTanimationTimer for more information.
    // Note - you must not addPropertyOverride on the DedicatedServer otherwise it will crash the game when
    //   your mod is run on a dedicated server, because IItemPropertyGetter does not exist there
    if (!MinecraftByExample.proxy.isDedicatedServer()) {
      this.addPropertyOverride(new ResourceLocation("chargefraction"), new ItemNBTanimationTimer());
    }
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
    NBTTagCompound nbtTagCompound = stack.getTagCompound();
    if (nbtTagCompound == null) return false;
    return nbtTagCompound.hasKey("Bound");
  }

  // what animation to use when the player holds the "use" button
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }

  // how long the player needs to hold down the right button in order to activate the gem, in ticks (1 tick = 1/20 second)
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return CHARGE_UP_DURATION_TICKS + CHARGE_UP_INITIAL_PAUSE_TICKS;
  }

  // called when the player starts holding right click;
  // --> if the gem is unbound, store the current location
  //  if the gem is bound, start the charge up sequence
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
  {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    NBTTagCompound nbtTagCompound = itemStackIn.getTagCompound();

    if (playerIn.isSneaking()) { // shift pressed; save (or overwrite) current location
      if (nbtTagCompound == null) {
        nbtTagCompound = new NBTTagCompound();
        itemStackIn.setTagCompound(nbtTagCompound);
      }
      nbtTagCompound.setBoolean("Bound", true);
      nbtTagCompound.setDouble("X", (int) playerIn.posX);
      nbtTagCompound.setDouble("Y", (int)playerIn.posY);
      nbtTagCompound.setDouble("Z", (int)playerIn.posZ);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    boolean bound = false;
    if (nbtTagCompound != null && nbtTagCompound.hasKey("Bound")  ) {
      bound = nbtTagCompound.getBoolean("Bound");
    }
    if (bound) {
      playerIn.setActiveHand(hand); // start the charge up sequence
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    } else {
      if (worldIn.isRemote) {  // only on the client side, else you will get two messages..
        final boolean PRINT_IN_CHAT_WINDOW = true;
        playerIn.sendStatusMessage(new TextComponentString("Gem doesn't have a stored location! Shift right click to store your current location"),
                PRINT_IN_CHAT_WINDOW);
      }
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
    }
  }

  // called when the player has held down the right click for the full charge-up duration
  // in this case - destroy the item
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
  {
    NBTTagCompound nbtTagCompound = stack.getTagCompound();
    if (nbtTagCompound == null || !nbtTagCompound.hasKey("Bound") || nbtTagCompound.getBoolean("Bound") != true ) {
      return stack;
    }

    double x = nbtTagCompound.getDouble("X");  // returns a default if not present
    double y = nbtTagCompound.getDouble("Y");
    double z = nbtTagCompound.getDouble("Z");

    // teleport

    // on the client side, play the sound locally
    // on the server side, teleport the player and play the sound for all other players nearby except this player
    //  (doing it this way reduces the perceived lag for this player, i.e the sound plays instantly, instead of being
    //   delayed while the message goes to the server and comes back again)
    if (worldIn.isRemote) {  // client side
      worldIn.playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
    } else {  // server side
      if (entityLiving instanceof EntityPlayerMP) { // should be an EntityPlayerMP; check first just to be sure to avoid crash
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityLiving;
        entityPlayerMP.connection.setPlayerLocation(x, y, z, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
        final EntityPlayerMP dontPlayForThisPlayer = entityPlayerMP;
        worldIn.playSound(dontPlayForThisPlayer, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
      }
    }
    return ItemStack.EMPTY;  // EMPTY_ITEM
//    for items with multiple count, decrease stack size and return the itemstack, eg
//    stack.stackSize--;
//    return stack;
  }

  // adds 'tooltip' text
  @SideOnly(Side.CLIENT)
  @SuppressWarnings("unchecked")
  @Override
  //public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    NBTTagCompound nbtTagCompound = stack.getTagCompound();
    if (nbtTagCompound != null && nbtTagCompound.hasKey("Bound") && nbtTagCompound.getBoolean("Bound") == true ) {
      tooltip.add("Stored destination=");
      tooltip.add("X: " + nbtTagCompound.getInteger("X"));
      tooltip.add("Y: " + nbtTagCompound.getInteger("Y"));
      tooltip.add("Z: " + nbtTagCompound.getInteger("Z"));
      tooltip.add("Hold down right click to teleport.");
    }
    else
    {
      tooltip.add("Hold down shift and then right");
      tooltip.add("  click to store your current location");
    }
  }
}