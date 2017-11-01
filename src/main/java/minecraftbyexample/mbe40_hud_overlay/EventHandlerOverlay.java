package minecraftbyexample.mbe40_hud_overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Nephroid
 *
 * This class contains the code that modifies how the default overlay is drawn:
 * the health hearts and armour hearts are replaced with a health / armour status bar
 *
 * In order to change the position of the existing overlay, we have to use events.
 * For a detailed documentation on how events work, see Jabelar's tutorial:
 *
 *   http://jabelarminecraft.blogspot.com/p/minecraft-forge-172-event-handling.html
 *
 * Though the tutorial is labeled 1.7.2/1.7.10, its concepts carry over to 1.10.2
 */
public class EventHandlerOverlay
{
  public EventHandlerOverlay(StatusBarRenderer i_HUDrenderer)
  {
    statusBarRenderer = i_HUDrenderer;
  }

  private StatusBarRenderer statusBarRenderer;

    /* The RenderGameOverlayEvent.Pre event is called before each game overlay element is
   * rendered. It is called multiple times. A list of existing overlay elements can be found
   * in net.minecraftforge.client.event.RenderGameOverlayEvent.
   *
   * If you want something to be rendered under an existing vanilla element, you would render
   * it here.
   *
   * Note that you can entirely remove the vanilla rendering by cancelling the event here.
   */

  @SubscribeEvent(receiveCanceled=true)
  public void onEvent(RenderGameOverlayEvent.Pre event) {
    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
    if (entityPlayerSP == null) return;  // just in case

    // look for the ItemHUDactivator in the hotbar.  If not present, return without changing the HUD.
    boolean foundInHotbar = false;
    final int FIRST_HOTBAR_SLOT = 0;
    final int LAST_HOTBAR_SLOT_PLUS_ONE = FIRST_HOTBAR_SLOT + entityPlayerSP.inventory.getHotbarSize();
    for (int i = FIRST_HOTBAR_SLOT; i < LAST_HOTBAR_SLOT_PLUS_ONE; ++i) {
      ItemStack slotItemStack = entityPlayerSP.inventory.getStackInSlot(i);
      if (slotItemStack.getItem() == StartupCommon.itemHUDactivator) {
        foundInHotbar = true;
        break;
      }
    }
    if (!foundInHotbar) return;

    switch (event.getType()) {
      case HEALTH:
        statusBarRenderer.renderStatusBar(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());        /* Call a helper method so that this method stays organized */
        /* Don't render the vanilla heart bar */
        event.setCanceled(true);
        break;

      case ARMOR:
        /* Don't render the vanilla armor bar, it's part of the status bar in the HEALTH event */
        event.setCanceled(true);
        break;

      default: // If it's not one of the above cases, do nothing
        break;
    }
  }

  /* The RenderGameOverlayEvent.Post event is called after each game overlay element is rendered.
   * Similar to the RenderGameOverlayEvent.Pre event, it is called multiple times.
   *
   * If you want something to be rendered over an existing vanilla element, you would render
   * it here.
   */
  @SubscribeEvent(receiveCanceled=true)
  public void onEvent(RenderGameOverlayEvent.Post event) {

    switch (event.getType()) {
      case HEALTH:
        break;
      default: // If it's not one of the above cases, do nothing
        break;
    }
  }
}