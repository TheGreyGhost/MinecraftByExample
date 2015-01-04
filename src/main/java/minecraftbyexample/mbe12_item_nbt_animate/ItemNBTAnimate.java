package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    this.setCreativeTab(CreativeTabs.tabMisc);   // items will appear on the Miscellaneous creative tab
  }

  private final ModelResourceLocation itemModel_0 = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_0", "inventory");
  private final ModelResourceLocation itemModel_1 = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_1", "inventory");
  private final ModelResourceLocation itemModel_2 = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_2", "inventory");
  private final ModelResourceLocation itemModel_3 = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_3", "inventory");
  private final ModelResourceLocation itemModel_4 = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_4", "inventory");
  private final ModelResourceLocation itemModel_5 = new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_5", "inventory");

  // When the user presses and holds right click, there are three phases:
  // 1) an initial pause, then
  // 2) a visual 'charging up' of the gem, then
  // 3) the teleportation occurs
  // NB there are twenty minecraft game loop "ticks" per second.
  private final int CHARGE_UP_INITIAL_PAUSE_TICKS = 10;
  private final int CHARGE_UP_DURATION_TICKS = 20;

  @Override
  public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int ticksInUse) {
    if (!player.isUsingItem()) {
      return itemModel_0;
    }

    if (ticksInUse < CHARGE_UP_INITIAL_PAUSE_TICKS) {
      return itemModel_0;
    }
    final int chargeTicksSoFar = ticksInUse - CHARGE_UP_INITIAL_PAUSE_TICKS;

    if (chargeTicksSoFar < CHARGE_UP_DURATION_TICKS * 0.2) {
      return itemModel_1;
    } else if (chargeTicksSoFar < CHARGE_UP_DURATION_TICKS * 0.4) {
      return itemModel_2;
    } else if (chargeTicksSoFar < CHARGE_UP_DURATION_TICKS * 0.6) {
      return itemModel_3;
    } else if (chargeTicksSoFar < CHARGE_UP_DURATION_TICKS * 0.8) {
      return itemModel_4;
    } else {
      return itemModel_5;
    }
  }

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
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
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
    } else { // attempting to use gem teleport
      boolean bound = false;
      if (nbtTagCompound != null && nbtTagCompound.hasKey("Bound")  ) {
        bound = nbtTagCompound.getBoolean("Bound");
      }
      if (bound) {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn)); // start the charge up sequence
      } else {
        if (worldIn.isRemote) {  // only on the client side, else you will get two messages..
          playerIn.addChatComponentMessage(new ChatComponentText("Gem doesn't have a stored location! Shift right click to store your current location"));
        }
      }
    }
    return itemStackIn;
  }

  // called when the player has held down the right click for the full charge-up duration
  // in this case - destroy the item
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
  {
    NBTTagCompound nbtTagCompound = stack.getTagCompound();
    if (nbtTagCompound == null || !nbtTagCompound.hasKey("Bound") || nbtTagCompound.getBoolean("Bound") != true ) {
      return stack;
    }

    // teleport
    if (!worldIn.isRemote) { // server side only - will automatically update to client
      double x = nbtTagCompound.getDouble("X");  // returns a default if not present
      double y = nbtTagCompound.getDouble("Y");
      double z = nbtTagCompound.getDouble("Z");
      if (playerIn instanceof EntityPlayerMP) { // should be an EntityPlayerMP check first just to be sure to avoid crash
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerIn;

        entityPlayerMP.playerNetServerHandler.setPlayerLocation(x, y, z, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
        worldIn.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
      }
    }
    return null;
//    for items with multiple count, decrease stack size and return the itemstack, eg
//    stack.stackSize--;
//    return stack;
  }

  // adds 'tooltip' text
  @SideOnly(Side.CLIENT)
  @SuppressWarnings("unchecked")
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    NBTTagCompound nbtTagCompound = stack.getTagCompound();
    if (nbtTagCompound != null && nbtTagCompound.hasKey("Bound") && nbtTagCompound.getBoolean("Bound") == true ) {
      tooltip.add("Stored destination=");
      tooltip.add("X: " + nbtTagCompound.getInteger("X"));
      tooltip.add("Y: " + nbtTagCompound.getInteger("Y"));
      tooltip.add("Z: " + nbtTagCompound.getInteger("Z"));
    }
    else
    {
      tooltip.add("Hold down shift and then right click to store your current location");
    }
  }
}