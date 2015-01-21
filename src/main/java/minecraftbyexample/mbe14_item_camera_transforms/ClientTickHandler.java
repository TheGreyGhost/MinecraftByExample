package minecraftbyexample.mbe14_item_camera_transforms;

import minecraftbyexample.mbe13_item_tools.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
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

    IBakedModel swordModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Items.iron_sword));
    ItemCameraTransforms swordCameraTransforms = swordModel.getItemCameraTransforms();

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

    IBakedModel ibakedmodel = null;
    if (foundCamera) {
      ItemStack heldItemStack = player.getHeldItem();
      if (heldItemStack != null) {
        boolean justSwitchedToThis = (ibakedmodel != StartupClientOnly.modelBakeEventHandler.getItemOverrideLink().itemModelToOverride);

        if (justSwitchedToThis) {  // retrieve underlying, unmodified transform as the initial starting point
          StartupClientOnly.modelBakeEventHandler.getItemOverrideLink().forcedTransform = MAKE COPY OF ibakedmodel.getItemCameraTransforms();
        }
        ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(heldItemStack);
      }
    }
    StartupClientOnly.modelBakeEventHandler.getItemOverrideLink().itemModelToOverride = ibakedmodel;
  }
}
