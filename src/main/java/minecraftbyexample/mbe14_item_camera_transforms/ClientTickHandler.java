package minecraftbyexample.mbe14_item_camera_transforms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * User: The Grey Ghost
 * Date: 2/11/13
 * Every tick, check all the items in the hotbar to see if any are the mbe14 ItemCamera.  If so, apply the transform
 *   override to the held item.
 */
public class ClientTickHandler
{
  @SubscribeEvent
  public void clientTickEvent(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.START) {
      return;
    }

    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (player == null) return;

    boolean foundCamera = false;
    InventoryPlayer inventoryPlayer = player.inventory;
    for (int i = 0; i < inventoryPlayer.getHotbarSize(); ++i) {
      ItemStack slotItemStack = inventoryPlayer.mainInventory[i];
      if (slotItemStack != null && slotItemStack.getItem() == StartupCommon.itemCamera) {
        foundCamera = true;
        break;
      }
    }
    StartupClientOnly.menuItemCameraTransforms.changeMenuVisible(foundCamera);

    IBakedModel ibakedmodel = null;
    if (foundCamera) {
      ItemStack heldItemStack = player.getHeldItem();
      if (heldItemStack != null) {
        ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(heldItemStack);
      }
    }

    ItemModelFlexibleCamera.UpdateLink link = StartupClientOnly.modelBakeEventHandler.getItemOverrideLink();
    link.itemModelToOverride = ibakedmodel;
    link.forcedTransform = StartupClientOnly.menuItemCameraTransforms.getItemCameraTransforms();
  }
}
