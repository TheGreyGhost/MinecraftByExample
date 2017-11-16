package minecraftbyexample.mbe40_hud_overlay;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 26/01/2015
 * ItemHUDactivator is very simple item used to activate this example
 * 1) activate the HUD overlay when it is held in the hotbar.
 * 2) switch the player to survival mode when it is held in the hotbar (so (s)he can see the health bar effect)
 *
 */
public class ItemHUDactivator extends Item
{
  public ItemHUDactivator()
  {
    this.setMaxStackSize(1);
    this.setCreativeTab(CreativeTabs.MISC);   // the item will appear on the Miscellaneous tab in creative
  }

  // if the item is being held in the hotbar, switch to survival mode.
  // The HUD rendering is switched on in the EventHandlerOverlay code, not here.
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    final int FIRST_HOTBAR_SLOT_NUMBER = 0;
    final int LAST_HOTBAR_SLOT_NUMBER = 8;

    if (itemSlot < FIRST_HOTBAR_SLOT_NUMBER || itemSlot > LAST_HOTBAR_SLOT_NUMBER) return;
    if (worldIn.isRemote) return;
    if (!(entityIn instanceof EntityPlayerMP)) return;
    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityIn;
    if (entityPlayerMP.interactionManager.getGameType() != GameType.SURVIVAL) {
      entityPlayerMP.setGameType(GameType.SURVIVAL);
    }
  }

  // adds 'tooltip' text
  @SideOnly(Side.CLIENT)
  @SuppressWarnings("unchecked")
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add("Place the activator in your hotbar");
    tooltip.add(" to activate the custom HUD.");
  }
}