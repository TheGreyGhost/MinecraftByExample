package minecraftbyexample.mbe14_item_camera_transforms;

import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by TheGreyGhost on 22/01/15.
 */
public class MenuItemCameraTransforms
{
  public MenuItemCameraTransforms()
  {
    linkToHUDrenderer = new HUDtextRenderer.HUDinfoUpdateLink();
    MinecraftForge.EVENT_BUS.register(new HUDtextRenderer(linkToHUDrenderer));
    menuKeyHandler = new MenuKeyHandler(this.new KeyPressCallback());
    FMLCommonHandler.instance().bus().register(menuKeyHandler);
  }

  public class KeyPressCallback
  {
    void keyPressed(MenuKeyHandler.ArrowKeys whichKey)
    {
      switch (whichKey) {
        case UP: {
          linkToHUDrenderer.selectedField = linkToHUDrenderer.selectedField.getNextField();
          break;
        }
        case DOWN: {
          linkToHUDrenderer.selectedField = linkToHUDrenderer.selectedField.getPreviousField();
          break;
        }
        case RIGHT:
        case LEFT: {
          alterField(whichKey == MenuKeyHandler.ArrowKeys.RIGHT);
          break;
        }
      }
    }
  }

  private void alterField(boolean increase)
  {
    ItemTransformVec3f transformVec3f = null;
    switch (linkToHUDrenderer.selectedTransform) {
      case THIRD: {transformVec3f = linkToHUDrenderer.itemCameraTransforms.thirdPerson; break;}
      case FIRST: {transformVec3f = linkToHUDrenderer.itemCameraTransforms.firstPerson; break;}
      case GUI: {transformVec3f = linkToHUDrenderer.itemCameraTransforms.gui; break;}
      case HEAD: {transformVec3f = linkToHUDrenderer.itemCameraTransforms.head; break;}
    }
    if (transformVec3f == null) return; // should never happen

    switch (linkToHUDrenderer.selectedField) {
      case TRANSFORM: {
        linkToHUDrenderer.selectedTransform = increase ? linkToHUDrenderer.selectedTransform.getNext()
                : linkToHUDrenderer.selectedTransform.getPrevious();
        break;
      }
      case SCALE_X: {
        transformVec3f.scale.setX(transformVec3f.scale.getX() + (increase ? 0.01F : -0.01F));
        break;
      }
      case SCALE_Y: {
        transformVec3f.scale.setY(transformVec3f.scale.getY() + (increase ? 0.01F : -0.01F));
        break;
      }
      case SCALE_Z: {
        transformVec3f.scale.setZ(transformVec3f.scale.getZ() + (increase ? 0.01F : -0.01F));
        break;
      }
      case ROTATE_X: {
        float newAngle = transformVec3f.rotation.getX() + (increase ? 2F : -2F);
        newAngle = MathHelper.wrapAngleTo180_float(newAngle - 180) + 180;
        transformVec3f.rotation.setX(newAngle);
        break;
      }
      case ROTATE_Y: {
        float newAngle = transformVec3f.rotation.getY() + (increase ? 2F : -2F);
        newAngle = MathHelper.wrapAngleTo180_float(newAngle - 180) + 180;
        transformVec3f.rotation.setY(newAngle);
        break;
      }
      case ROTATE_Z: {
        float newAngle = transformVec3f.rotation.getZ() + (increase ? 2F : -2F);
        newAngle = MathHelper.wrapAngleTo180_float(newAngle - 180) + 180;
        transformVec3f.rotation.setZ(newAngle);
        break;
      }
      case RESTORE_DEFAULT: {
        break;
      }
    }
  }

  private HUDtextRenderer.HUDinfoUpdateLink linkToHUDrenderer;
  private MenuKeyHandler menuKeyHandler;

  public static class MenuKeyHandler
  {
    public MenuKeyHandler(KeyPressCallback i_keyPressCallback)
    {
      keyPressCallback = i_keyPressCallback;
    }

    @SubscribeEvent
    void menuKeyInputEvent(InputEvent.KeyInputEvent keyInputEvent)
    {
      ArrowKeys keyPressed = ArrowKeys.NONE;
      if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) keyPressed = ArrowKeys.LEFT;
      if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) keyPressed = ArrowKeys.RIGHT;
      if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) keyPressed = ArrowKeys.DOWN;
      if (Keyboard.isKeyDown(Keyboard.KEY_UP)) keyPressed = ArrowKeys.UP;

      if (keyPressed == ArrowKeys.NONE) return;
      if (keyPressed != lastKey) {
        lastKey = keyPressed;
        holdTime = 0;
      } else {
        ++holdTime;
        final int INITIAL_PAUSE_TICKS = 10;  // wait 10 ticks before repeating
        if (holdTime < INITIAL_PAUSE_TICKS) return;
      }
      keyPressCallback.keyPressed(keyPressed);
    }

    public enum ArrowKeys {NONE, UP, DOWN, LEFT, RIGHT}
    private int holdTime = 0;
    private ArrowKeys lastKey = ArrowKeys.NONE;
    private KeyPressCallback keyPressCallback;
  }
}
