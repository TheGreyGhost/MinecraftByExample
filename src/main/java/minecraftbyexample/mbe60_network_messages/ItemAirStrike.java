package minecraftbyexample.mbe60_network_messages;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemAirStrike is a simple item that sends a message to the server when you right click it.
 * For background information on items see here http://greyminecraftcoder.blogspot.com/2013/12/items.html
 *   and here http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html
 */
public class ItemAirStrike extends Item
{
  public ItemAirStrike()
  {
    this.setMaxStackSize(1);
    this.setCreativeTab(CreativeTabs.tabMisc);   // the item will appear on the Miscellaneous tab in creative
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    if (!worldIn.isRemote) {  // don't execute on the server side!
      return itemStackIn;
    }

    float PARTIAL_TICKS = 1.0F;
    Vec3 playerLook = playerIn.getLookVec();
    Vec3 playerFeetPosition = playerIn.getPositionEyes(PARTIAL_TICKS).subtract(0, playerIn.getEyeHeight(), 0);
    double TARGET_DISTANCE = 6.0;
    Vec3 targetPosition = playerFeetPosition.addVector(playerLook.xCoord * TARGET_DISTANCE, 0, playerLook.zCoord * TARGET_DISTANCE);

    Random random = new Random();
    AirstrikeMessageToServer.Projectile [] choices = {AirstrikeMessageToServer.Projectile.PIG, AirstrikeMessageToServer.Projectile.TNT,
                                                      AirstrikeMessageToServer.Projectile.SNOWBALL};
    AirstrikeMessageToServer.Projectile projectile = choices[random.nextInt(choices.length)];

    AirstrikeMessageToServer airstrikeMessageToServer = new AirstrikeMessageToServer(projectile, targetPosition);
    StartupCommon.simpleNetworkWrapper.sendToServer(airstrikeMessageToServer);
    return itemStackIn;
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }
}
