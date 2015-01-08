package minecraftbyexample.overlay_simple;

import net.minecraftforge.common.MinecraftForge;

/**
 * @author Nephroid
 * 
 * User: Nephroid
 * Date: December 26, 2014
 */
public class Startup {
  
  /* Use this boolean to toggle whether this example is enabled or not.
   * The changes made in this example are for demonstration purposes only.
   * 
   * Unexpected results may occur when enabling both this and the advanced
   * overlay examples.
   */
  public static boolean enabled = false;
  
  public static void preInitCommon() {
    
  }
  
  public static void preInitClientOnly() {

  }
  
  public static void initCommon() {
    
  }
  
  /* Here, we register the event handler that modifies the overlay. Since
   * the overlay is a GUI element, and the GUI only exists on the client side,
   * we only register this event handler on the client side.
   */
  public static void initClientOnly() {
    if (enabled) {
      MinecraftForge.EVENT_BUS.register(new EventHandler_Overlay());
    }
  }
  
  public static void postInitCommon() {

  }

  public static void postInitClientOnly() {

  }
}
