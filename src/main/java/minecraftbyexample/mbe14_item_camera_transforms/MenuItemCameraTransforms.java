package minecraftbyexample.mbe14_item_camera_transforms;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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

  public ItemCameraTransforms getItemCameraTransforms() {return linkToHUDrenderer.itemCameraTransforms;}

  public void changeMenuVisible(boolean visible) {linkToHUDrenderer.menuVisible = visible;}

  public class KeyPressCallback
  {
    void keyPressed(MenuKeyHandler.ArrowKeys whichKey)
    {
      switch (whichKey) {
        case DOWN: {
          linkToHUDrenderer.selectedField = linkToHUDrenderer.selectedField.getNextField();
          break;
        }
        case UP: {
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
      case TRANSLATE_X: {
        transformVec3f.translation.setX(transformVec3f.translation.getX() + (increase ? 0.01F : -0.01F));
        break;
      }
      case TRANSLATE_Y: {
        transformVec3f.translation.setY(transformVec3f.translation.getY() + (increase ? 0.01F : -0.01F));
        break;
      }
      case TRANSLATE_Z: {
        transformVec3f.translation.setZ(transformVec3f.translation.getZ() + (increase ? 0.01F : -0.01F));
        break;
      }
      case RESTORE_DEFAULT: {
        ItemModelFlexibleCamera.UpdateLink link = StartupClientOnly.modelBakeEventHandler.getItemOverrideLink();
        IBakedModel savedModel = link.itemModelToOverride;
        link.itemModelToOverride = null;
        ItemCameraTransforms originalTransforms = savedModel.getItemCameraTransforms();
        link.itemModelToOverride = savedModel;
        switch (linkToHUDrenderer.selectedTransform) {
          case THIRD: {copyTransforms(originalTransforms.thirdPerson, transformVec3f); break;}
          case FIRST: {copyTransforms(originalTransforms.firstPerson, transformVec3f); break;}
          case GUI: {copyTransforms(originalTransforms.gui, transformVec3f); break;}
          case HEAD: {copyTransforms(originalTransforms.head, transformVec3f); break;}
        }
        break;
      }
    }
  }

  private static void copyTransforms(ItemTransformVec3f from, ItemTransformVec3f to)
  {
    to.translation.setX(from.translation.getX());
    to.scale.setX(from.scale.getX());
    to.rotation.setX(from.rotation.getX());
    to.translation.setY(from.translation.getY());
    to.scale.setY(from.scale.getY());
    to.rotation.setY(from.rotation.getY());
    to.translation.setZ(from.translation.getZ());
    to.scale.setZ(from.scale.getZ());
    to.rotation.setZ(from.rotation.getZ());
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
    public void clientTick(TickEvent.ClientTickEvent event)
    {
      if (event.phase != TickEvent.Phase.START) {
        return;
      }

      ArrowKeys keyPressed = ArrowKeys.NONE;
      if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) keyPressed = ArrowKeys.LEFT;
      if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) keyPressed = ArrowKeys.RIGHT;
      if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) keyPressed = ArrowKeys.DOWN;
      if (Keyboard.isKeyDown(Keyboard.KEY_UP)) keyPressed = ArrowKeys.UP;

      if (keyPressed == ArrowKeys.NONE) {
        lastKey = keyPressed;
        return;
      }
      if (keyPressed != lastKey) {
        lastKey = keyPressed;
        keyDownTimeTicks = 0;
      } else {
        ++keyDownTimeTicks;
        final int INITIAL_PAUSE_TICKS = 10;  // wait 10 ticks before repeating
        if (keyDownTimeTicks < INITIAL_PAUSE_TICKS) return;
      }
      keyPressCallback.keyPressed(keyPressed);
    }

    public enum ArrowKeys {NONE, UP, DOWN, LEFT, RIGHT}
    private long keyDownTimeTicks = 0;
    private ArrowKeys lastKey = ArrowKeys.NONE;
    private KeyPressCallback keyPressCallback;
  }
}
