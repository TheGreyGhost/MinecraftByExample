package minecraftbyexample.overlay_advanced;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Nephroid
 * 
 * User: Nephroid
 * Date: December 27, 2014
 * 
 * This example is intended for someone who understands how the simple overlay
 * example works. I will be creating a custom HP bar that replaces the existing
 * heart and armor bars. This custom bar will also change color if the player
 * has the regeneration, poison, or wither effects.
 */
public class Startup {
  /* Use this boolean to toggle whether this example is enabled or not.
   * The changes made in this example are for demonstration purposes only.
   */
  public static boolean enabled = true;
  
  public static void preInitCommon() {
    
  }
  
  public static void preInitClientOnly() {

  }
  
  public static void initCommon() {
    
  }
  
  public static void initClientOnly() {
    if (enabled) {
      /* Don't forget to pass in an instance of Minecraft! */
      MinecraftForge.EVENT_BUS.register(new EventHandler_Overlay(Minecraft.getMinecraft()));
    }
  }
  
  public static void postInitCommon() {

  }

  public static void postInitClientOnly() {

  }
}
