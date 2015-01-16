package minecraftbyexample.mbe60_network_messages;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemAirStrike is a simple item that sends a network message to the server when you right click it.
 */
public class ItemAirStrike extends Item
{
  public ItemAirStrike()
  {
    this.setMaxStackSize(1);
    this.setCreativeTab(CreativeTabs.tabMisc);   // the item will appear on the Miscellaneous tab in creative
  }

  // called when the item is used to right-click on a block
  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (!worldIn.isRemote) {  // don't execute on the server side!
      return true;
    }
    Vec3 targetLocation = new Vec3(pos.getX()+ 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
    callAirstrikeOnTarget(targetLocation);
    return true;  // tell caller we have processed the click
  }

  // called when the item is right clicked in the air (or when clicked on a block but onItemUse returned false)
  @Override
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    if (!worldIn.isRemote) {  // don't execute on the server side!
      return itemStackIn;
    }

    // target a location in the direction that the player is looking
    final float PARTIAL_TICKS = 1.0F;
    Vec3 playerLook = playerIn.getLookVec();
    Vec3 playerFeetPosition = playerIn.getPositionEyes(PARTIAL_TICKS).subtract(0, playerIn.getEyeHeight(), 0);
    final double TARGET_DISTANCE = 6.0;
    final double HEIGHT_ABOVE_FEET = 0.1;
    Vec3 targetPosition = playerFeetPosition.addVector(playerLook.xCoord * TARGET_DISTANCE, HEIGHT_ABOVE_FEET,
                                                       playerLook.zCoord * TARGET_DISTANCE);
    callAirstrikeOnTarget(targetPosition);
    return itemStackIn;
  }

  // send a network message to the server to bombard the target location with a random projectile
  public void callAirstrikeOnTarget(Vec3 targetPosition)
  {
    Random random = new Random();
    AirstrikeMessageToServer.Projectile [] choices = AirstrikeMessageToServer.Projectile.values();
    AirstrikeMessageToServer.Projectile projectile = choices[random.nextInt(choices.length)];

    AirstrikeMessageToServer airstrikeMessageToServer = new AirstrikeMessageToServer(projectile, targetPosition);
    StartupCommon.simpleNetworkWrapper.sendToServer(airstrikeMessageToServer);
    return;
  }

  // BLOCK is a useful 'do nothing' animation for this item
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }

  // add a tooltip
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    tooltip.add("Right click on target to call an air strike");
  }
}
