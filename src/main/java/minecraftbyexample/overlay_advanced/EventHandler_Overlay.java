package minecraftbyexample.overlay_advanced;

import java.text.DecimalFormat;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

/**
 * @author Nephroid
 * 
 * This class contains code that replaces the vanilla heart and armor bars with a
 * custom unified status bar.
 */

/* I extend Gui because it has already implemented a bunch of drawing functions */
public class EventHandler_Overlay extends Gui{
  
  /* This line tells Minecraft/Forge where your texture is. The first argument is your MODID,
   * and the second argument is the path to your texture starting at "resources/assets/MODID"
   * 
   * In this case, the location of the texture is
   * 
   *   "resources/assets/MODID/textures/gui/advanced_overlay.png"
   */
  private static ResourceLocation overlayBar = new ResourceLocation(MinecraftByExample.MODID,
      "/textures/gui/advanced_overlay.png");
  
  /* These two variables describe the size of the bar */
  private final static int BAR_WIDTH = 81;
  private final static int BAR_HEIGHT = 9;
  
  /* Sometimes you want to include extra information from the game. This instance of
   * Minecraft will let you access the World and EntityPlayer objects which is more than
   * enough for most purposes. It also contains some helper objects for OpenGL which can be
   * used for drawing things.
   * 
   * To actually get the instance of Minecraft, you should pass it in through the constructor.
   * It is possible to import Minecraft and use Minecraft.getMinecraft() here, but this won't
   * always be possible if you want to include information that's part of another class.
   */
  private Minecraft mc;
  
  public EventHandler_Overlay(Minecraft mc) {
    this.mc = mc;
  }
  
  @SubscribeEvent(receiveCanceled=true)
  public void onEvent(RenderGameOverlayEvent.Pre event) {
    if (event.type == ElementType.HEALTH) {
      /* Call a helper method so that this method stays organized */
      renderStatusBar();
      
      /* Don't render the vanilla heart bar */
      event.setCanceled(true);
    }
    else if (event.type == ElementType.ARMOR) {
      /* Don't render the vanilla armor bar */
      event.setCanceled(true);
    }
    
    /* You might be wondering why I use a chain of if statements instead
     * of the switch statement I used in the simple example. A switch statement
     * lets you write more compact code, and it's often more readable. (You don't
     * have to type in event.type a bunch of times, and it's clear which case is
     * which.)
     * 
     * As a general rule of thumb, I use if-else chains if there are very few cases,
     * otherwise I use switches. 
     * 
     * There are some technical differences and possible performance boosts for using
     * switches, but those aren't too important. (The performance boosts are compiler
     * dependent, and they won't be noticeable either way.)
     */
  }
  
  /* This helper method will render the bar */
  private void renderStatusBar() {
    /* These are the variables that contain world and player information */
    World world = mc.theWorld;
    EntityPlayer player = mc.thePlayer;
    
    /* This object draws text using the Minecraft font */
    FontRenderer fr = mc.fontRendererObj;
    
    /* This object inserts commas into number strings */
    DecimalFormat d = new DecimalFormat("#,###");
    
    /* Saving the current state of OpenGL so that I can restore it when I'm done */
    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
    GL11.glPushMatrix();
    
      /* I like to indent the code whenever I push. It helps me visualize what is
       * happening better. This is a personal preference though.
       */
      
      /* Set the rendering color to white */
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      
      /* This method tells OpenGL to draw with the custom texture */
      mc.renderEngine.bindTexture(overlayBar);
      
      /* Shift to the position of the vanilla heart bar. The numbers were obtained
       * by guessing and checking.
       */
      GL11.glTranslatef(mc.displayWidth/4 - BAR_WIDTH - 10, mc.displayHeight/2 - 39, 0);
      
      /* Draw a part of the image file at the current position
       * 
       * The first two arguments are the x,y position that you want to draw the texture at
       * (with respect to the current transformations).
       * 
       * The next four arguments specify what part of the image file to draw, in the order below:
       * 
       *   1. Left-most side
       *   2. Top-most side
       *   3. Right-most side
       *   4. Bottom-most side
       * 
       * The units of these four arguments are pixels in the image file. These arguments will form a
       * rectangle, which is then "cut" from your image and used as the texture
       * 
       * This line draws the background of the custom bar
       */
      drawTexturedModalRect(0, 0, 0, 0, BAR_WIDTH, BAR_HEIGHT);
      
      /* This line draws the outline effect that corresponds to how much armor the player has.
       * I slide the right-most side of the rectangle using the player's armor value.
       */
      drawTexturedModalRect(0, 0, 0, BAR_HEIGHT, (int)(BAR_WIDTH*(player.getTotalArmorValue()/20f)), BAR_HEIGHT);
      
      /* This part draws the inside of the bar, which starts 1 pixel right and down */
      GL11.glPushMatrix();
      
        /* Shift 1 pixel right and down */
        GL11.glTranslatef(1, 1, 0);
        
        /* These few numbers will store the HP values of the player. This includes the Health Boost
         * and Absorption potion effects
         */
        float maxHp = player.getMaxHealth();
        float absorptionAmount = player.getAbsorptionAmount();
        float effectiveHp = player.getHealth() + absorptionAmount;
        
        /* The part of the bar that fills up will be a rectangle that stretches based on how much hp the player
         * has. To do this, I need to use a scaling transform, which is why I push again.
         */
        GL11.glPushMatrix();
        
          /* I scale horizontally based on the fraction effectiveHp/maxHp. However, I don't want the scaled
           * value to exceed the actual width of the bar's interior, so I cap it at 1 using Math.min.
           * 
           * The width of the bar's interior is BAR_WIDTH - 2
           */
          GL11.glScalef((BAR_WIDTH - 2)*Math.min(1, effectiveHp/maxHp), 1, 1);
          
          
          /* This chain of if-else blocks checks if the player has any status effects. I check if a potion effect
           * is active, then draw the filling bar based on what potion effect is active. Unfortunately, it is
           * not possible to use a switch statement here since multiple potion effects may be active at once.
           * 
           * The effects I check for are
           *   20: Wither
           *   19: Poison
           *   10: Regeneration
           *   
           * For a more comprehensive list of status effects, see http://minecraft.gamepedia.com/Status_effect
           */
          if (player.isPotionActive(20)) {
            drawTexturedModalRect(0, 0, BAR_WIDTH + 2, 0, 1, BAR_HEIGHT - 2);
          }
          else if (player.isPotionActive(19)) {
            drawTexturedModalRect(0, 0, BAR_WIDTH + 3, 0, 1, BAR_HEIGHT - 2);
          }
          else if (player.isPotionActive(10)) {
            drawTexturedModalRect(0, 0, BAR_WIDTH + 1, 0, 1, BAR_HEIGHT - 2);
          }
          else {
            drawTexturedModalRect(0, 0, BAR_WIDTH, 0, 1, BAR_HEIGHT - 2);
          }
          
        GL11.glPopMatrix();
        
        /* Move to the right end of the bar, minus a few pixels. */
        GL11.glTranslatef(BAR_WIDTH - 3, 1, 0);
        
        /* The default minecraft font is too big, so I scale it down a bit. */
        GL11.glPushMatrix();
          GL11.glScalef(0.5f, 0.5f, 1);
          
          /* This generates the string that I want to draw. */
          String s = d.format(effectiveHp) + "/" + d.format(maxHp);
          
          /* If the player has the absorption effect, draw the string in gold color, otherwise
           * draw the string in white color. For each case, I call drawString twice, once to
           * draw the shadow, and once for the actual string.
           */
          if (absorptionAmount > 0) {
            
            /* Draw the shadow string */
            fr.drawString(s, -fr.getStringWidth(s) + 1, 2, 0x5A2B00);
            
            /* Draw the actual string */
            fr.drawString(s, -fr.getStringWidth(s), 1, 0xFFD200);
          }
          else {
            fr.drawString(s, -fr.getStringWidth(s) + 1, 2, 0x4D0000);
            fr.drawString(s, -fr.getStringWidth(s), 1, 0xFFFFFF);
          }
        GL11.glPopMatrix();
        
      GL11.glPopMatrix();
      
    GL11.glPopMatrix();
    GL11.glPopAttrib();
  }
}
