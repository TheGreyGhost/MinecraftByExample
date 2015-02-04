package minecraftbyexample.mbe40_hud_overlay2356;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Nephroid
 *
 * User: Nephroid
 * Date: December 26, 2014
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
  }

  public static void initClientOnly()
  {
    // required in order for the renderer to know how to render your item.  Likely to change in the near future.
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe40_hud_overlay_item", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(StartupCommon.itemHUDactivator, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  private static StatusBarRenderer statusBarRenderer;

  public static void postInitClientOnly()
  {
  /* Here, we register the event handler that modifies the overlay. Since
   * the overlay is a GUI element, and the GUI only exists on the client side,
   * we only register this event handler on the client side.
   */
    statusBarRenderer = new StatusBarRenderer(Minecraft.getMinecraft());
    MinecraftForge.EVENT_BUS.register(new EventHandlerOverlay(statusBarRenderer));
  }
}
