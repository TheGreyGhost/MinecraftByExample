package minecraftbyexample.mbe60_network_messages;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
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
    this.setCreativeTab(CreativeTabs.MISC);   // the item will appear on the Miscellaneous tab in creative
  }

  // called when the item is used to right-click on a block
  @Override
  public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = playerIn.getHeldItem(hand);
    if (!worldIn.isRemote) {  // don't execute on the server side!
      return EnumActionResult.PASS;
    }
    Vec3d targetLocation = new Vec3d(pos.getX()+ 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
    callAirstrikeOnTarget(targetLocation);
    return EnumActionResult.SUCCESS;  // tell caller we have processed the click
  }

  // called when the item is right clicked in the air (or when clicked on a block but onItemUse returned false)
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
  {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if (!worldIn.isRemote) {  // don't execute on the server side!
      return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    // target a location in the direction that the player is looking
    final float PARTIAL_TICKS = 1.0F;
    Vec3d playerLook = playerIn.getLookVec();
    Vec3d playerFeetPosition = playerIn.getPositionEyes(PARTIAL_TICKS).subtract(0, playerIn.getEyeHeight(), 0);
    final double TARGET_DISTANCE = 6.0;
    final double HEIGHT_ABOVE_FEET = 0.1;
    Vec3d targetPosition = playerFeetPosition.addVector(playerLook.x * TARGET_DISTANCE, HEIGHT_ABOVE_FEET,
                                                       playerLook.z * TARGET_DISTANCE);
    callAirstrikeOnTarget(targetPosition);
    return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
  }

  // send a network message to the server to bombard the target location with a random projectile
  public void callAirstrikeOnTarget(Vec3d targetPosition)
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
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add("Right click on target to call an air strike");
  }
}
