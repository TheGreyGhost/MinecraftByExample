package minecraftbyexample.mbe60_network_messages;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemAirStrike is a simple item that sends a network message to the server when you right click it.
 */
public class ItemAirStrike extends Item
{
  static private final int MAXIMUM_NUMBER_OF_ITEMS = 1; // maximum stack size

  public ItemAirStrike()
  {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_ITEMS).group(ItemGroup.MISC) // the item will appear on the Miscellaneous tab in creative
    );
  }

  // called when the item is used to right-click on a block
  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    if (!context.getWorld().isRemote) {  // don't execute on the server side!
      return ActionResultType.PASS;
    }
    Vector3d targetLocation = context.getHitVec();
    callAirstrikeOnTarget(targetLocation);
    return ActionResultType.SUCCESS;  // tell caller we have processed the click
  }

  // called when the item is right clicked in the air (or when clicked on a block but onItemUse returned false)
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
  {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if (!worldIn.isRemote) {  // don't execute on the server side!
      return new ActionResult(ActionResultType.PASS, itemStackIn);
    }

    // target a location in the direction that the player is looking
    final float PARTIAL_TICKS = 1.0F;
    Vector3d playerLook = playerIn.getLookVec();
    Vector3d playerFeetPosition = playerIn.getEyePosition(PARTIAL_TICKS).subtract(0, playerIn.getEyeHeight(), 0);
    final double TARGET_DISTANCE = 6.0;
    final double HEIGHT_ABOVE_FEET = 0.1;
    Vector3d targetPosition = playerFeetPosition.add(playerLook.x * TARGET_DISTANCE, HEIGHT_ABOVE_FEET,
                                                       playerLook.z * TARGET_DISTANCE);
    callAirstrikeOnTarget(targetPosition);
    return new ActionResult(ActionResultType.SUCCESS, itemStackIn);
  }

  // send a network message to the server to bombard the target location with a random projectile
  public void callAirstrikeOnTarget(Vector3d targetPosition)
  {
    AirstrikeMessageToServer.Projectile projectile = AirstrikeMessageToServer.Projectile.getRandom();

    AirstrikeMessageToServer airstrikeMessageToServer = new AirstrikeMessageToServer(projectile, targetPosition);
    StartupCommon.simpleChannel.sendToServer(airstrikeMessageToServer);
    return;
  }

  // BLOCK is a useful 'do nothing' animation for this item
  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK;
  }

  // adds 'tooltip' text
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    tooltip.add(new StringTextComponent("Right click on target to call an air strike"));
  }
}
