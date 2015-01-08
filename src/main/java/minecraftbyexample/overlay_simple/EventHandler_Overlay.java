package minecraftbyexample.overlay_simple;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Nephroid
 *
 * This class contains the code that modifies how the default overlay is drawn
 * 
 * In order to change the position of the existing overlay, we have to use events.
 * For a detailed documentation on how events work, see Jabelar's tutorial:
 * 
 *   http://jabelarminecraft.blogspot.com/p/minecraft-forge-172-event-handling.html
 * 
 * Though the tutorial is labeled 1.7.2/1.7.10, its concepts carry over to 1.8.
 */
public class EventHandler_Overlay {
  
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
    switch (event.type) {
    case PORTAL: // The purple nether portal effect
      
      /* Disable the nether portal effect */
      event.setCanceled(true);
      break;
      
    case HEALTH:
      /* The following line saves the current "position" that OpenGL uses to render with.
       * After calling this line, you can then perform any transformations that you want.
       * When you're finished, call GL11.glPopMatrix() to reset the transformations that
       * you did.
       * 
       * It is extremely important to reset your transformations; otherwise, everything else
       * that is rendered will use your transformations, which is not what you want.
       * 
       * I do not want to undo the transformations until after the health bar is rendered,
       * so I pop the matrix in the RenderGameOverlayEvent.Post event
       */
      GL11.glPushMatrix();
      
      /* This line will translate the health bar 100 pixels to the right, and 100 pixels up.
       * Since the overlay is 2 dimensional, the Z value is set as 0.
       */
      GL11.glTranslatef(100, -100, 0);
      
      break;
    case FOOD:
      
      GL11.glPushMatrix();
      
      /* This line will rotate the food bar by 30 degrees counter-clockwise. All rotations
       * will be about a global origin, not the origin of the object. To rotate an object about
       * its own origin, perform the following 3 operations:
       * 
       *   1. Translate the object so that its origin matches the global origin
       *   2. Perform the rotation
       *   3. Translate the object back
       *   
       *   A more concrete example of this will be shown in the more advanced overlay example
       * 
       * When calling glRotatef(a, x, y, z), the four arguments are as follows:
       *   a : specifies the angle that you want to rotate
       *   x, y, z: specify the vector that you want to rotate about
       * 
       * An important thing to note is that a vector that points in the opposite direction will
       * rotate the opposite way. This means that the following two examples do the same thing:
       * 
       *   glRotatef(30, 0, 0, -1);
       *   glRotatef(-30, 0, 0, 1);
       * 
       * Also, to rotate 2D graphics, you want to use a vector that is perpendicular to the screen,
       * which is why x and y are 0.
       */
      GL11.glRotatef(30, 0, 0, -1);
      break;
    case AIR:
      GL11.glPushMatrix();
      GL11.glTranslatef(-200, -200, 0);
      
      /* Scales the air (underwater) bar by some amount. Similar to rotations, scaling is with
       * respect to the origin. This means that the distance between the origin of the screen
       * and the air bar will also be scaled by the specified amount.
       * 
       * The arguments are fairly self-explanatory
       */
      GL11.glScalef(2, 2, 1);
      break;
    case HOTBAR:
      /* Specify a color to render with. If you're familiar with Photoshop or something similar, this
       * basically adds a clipping layer on top with the "Multiply" blend mode. Using the color white
       * will have no effect, and using the color black will make your texture completely black (but
       * it will preserve transparency).
       * 
       * The actual arguments for glColor3f are 3 float values from 0.0f to 1.0f. These represent the
       * level of each color component using the RGB model, with 1.0f being the highest. To learn more
       * about the RGB model, visit this link:
       * http://en.wikipedia.org/wiki/RGB_color_model
       * 
       * The line below turns the hotbar gold
       */
      GL11.glColor3f(1, 0.7f, 0);
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
    
    /* The matrix must be popped whenever it is pushed. In this example, I pushed
     * in the HEALTH, FOOD, and AIR, cases, so I have to pop in those cases here.
     */
    switch (event.type) {
    case HEALTH:
      GL11.glPopMatrix();
      break;
    case FOOD:
      GL11.glPopMatrix();
      break;
    case AIR:
      GL11.glPopMatrix();
      break;
    case HOTBAR:
      /* Set the render color back to white, so that not everything appears gold. */
      GL11.glColor3f(1, 1, 1);
      break;
    default: // If it's not one of the above cases, do nothing
      break;
    }
  }
}
