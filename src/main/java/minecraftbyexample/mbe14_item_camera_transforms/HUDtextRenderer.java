package minecraftbyexample.mbe14_item_camera_transforms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

/**
 * User: The Grey Ghost
 * Date: 20/01/2015
 */
public class HUDtextRenderer
{
  public HUDtextRenderer(HUDinfoUpdateLink i_HUDinfoUpdateLink)
  {
    huDinfoUpdateLink = i_HUDinfoUpdateLink;
  }

  public static class HUDinfoUpdateLink
  {
    public ItemCameraTransforms itemCameraTransforms;
    public SelectedField selectedField;
    public TransformName selectedTransform;
    public boolean menuVisible;

    public HUDinfoUpdateLink()
    {
      final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
      final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
      final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

      ItemTransformVec3f vec1 = new ItemTransformVec3f(new Vector3f(ROTATION_DEFAULT), new Vector3f(TRANSLATION_DEFAULT), new Vector3f(SCALE_DEFAULT));
      ItemTransformVec3f vec2 = new ItemTransformVec3f(new Vector3f(ROTATION_DEFAULT), new Vector3f(TRANSLATION_DEFAULT), new Vector3f(SCALE_DEFAULT));
      ItemTransformVec3f vec3 = new ItemTransformVec3f(new Vector3f(ROTATION_DEFAULT), new Vector3f(TRANSLATION_DEFAULT), new Vector3f(SCALE_DEFAULT));
      ItemTransformVec3f vec4 = new ItemTransformVec3f(new Vector3f(ROTATION_DEFAULT), new Vector3f(TRANSLATION_DEFAULT), new Vector3f(SCALE_DEFAULT));
      itemCameraTransforms = new ItemCameraTransforms(vec1, vec2, vec3, vec4);
      selectedField = SelectedField.TRANSFORM;
      selectedTransform = TransformName.FIRST;
      menuVisible = false;
    }

    public enum TransformName {
      THIRD, FIRST, HEAD, GUI;
      public TransformName getNext()
      {
        for (TransformName transformName : TransformName.values()) {
          if (transformName.ordinal() == this.ordinal() + 1) return transformName;
        }
        return THIRD;
      }
      public TransformName getPrevious()
      {
        for (TransformName transformName : TransformName.values()) {
          if (transformName.ordinal() == this.ordinal() - 1) return transformName;
        }
        return GUI;
      }
    }

    public enum SelectedField {
      TRANSFORM(0), SCALE_X(1), SCALE_Y(2), SCALE_Z(3), ROTATE_X(4), ROTATE_Y(5), ROTATE_Z(6), TRANSLATE_X(7),
      TRANSLATE_Y(8), TRANSLATE_Z(9), RESTORE_DEFAULT(10);

      private SelectedField(int index) {fieldIndex = index;}
      public final int fieldIndex;
      private static final SelectedField FIRST_FIELD = TRANSFORM;
      private static final SelectedField LAST_FIELD = RESTORE_DEFAULT;

      public static SelectedField getFieldName(int indexToFind)
      {
        for (SelectedField checkField : SelectedField.values()) {
          if (checkField.fieldIndex == indexToFind) return checkField;
        }
        return null;
      }
      public SelectedField getNextField()
      {
        SelectedField nextField = getFieldName(fieldIndex + 1);
        if (nextField == null) nextField = FIRST_FIELD;
        return nextField;
      }
      public SelectedField getPreviousField()
      {
        SelectedField previousField = getFieldName(fieldIndex - 1);
        if (previousField == null) previousField = LAST_FIELD;
        return previousField;
      }
    }

  }

  private HUDinfoUpdateLink huDinfoUpdateLink;

  @SubscribeEvent
  public void displayHUDtext(RenderGameOverlayEvent.Text event)
  {
    if (huDinfoUpdateLink == null || !huDinfoUpdateLink.menuVisible ||  huDinfoUpdateLink.itemCameraTransforms == null) return;
    ArrayList<String> displayText = new ArrayList<String>();
    ArrayList<HUDinfoUpdateLink.SelectedField> selectableField = new ArrayList<HUDinfoUpdateLink.SelectedField>();

    final HUDinfoUpdateLink.SelectedField NOT_SELECTABLE = null;

    displayText.add("======"); selectableField.add(NOT_SELECTABLE);
    displayText.add("VIEW  "); selectableField.add(NOT_SELECTABLE);
    ItemTransformVec3f transformVec3f = huDinfoUpdateLink.itemCameraTransforms.thirdPerson;

    switch (huDinfoUpdateLink.selectedTransform) {
      case THIRD: {displayText.add("third"); transformVec3f = huDinfoUpdateLink.itemCameraTransforms.thirdPerson; break;}
      case FIRST: {displayText.add("first"); transformVec3f = huDinfoUpdateLink.itemCameraTransforms.firstPerson; break;}
      case GUI: {displayText.add("gui"); transformVec3f = huDinfoUpdateLink.itemCameraTransforms.gui; break;}
      case HEAD: {displayText.add("head"); transformVec3f = huDinfoUpdateLink.itemCameraTransforms.head; break;}
    }
    selectableField.add(HUDinfoUpdateLink.SelectedField.TRANSFORM);

    displayText.add("======"); selectableField.add(NOT_SELECTABLE);
    displayText.add("SCALE"); selectableField.add(NOT_SELECTABLE);
    displayText.add("X:" + String.format("%.2f", transformVec3f.scale.getX()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.SCALE_X);
    displayText.add("Y:" + String.format("%.2f", transformVec3f.scale.getY()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.SCALE_Y);
    displayText.add("Z:" + String.format("%.2f", transformVec3f.scale.getZ()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.SCALE_Z);

    displayText.add("======"); selectableField.add(NOT_SELECTABLE);
    displayText.add("ROTATE"); selectableField.add(NOT_SELECTABLE);
    displayText.add("X:" + String.format("%3.0f", transformVec3f.rotation.getX()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.ROTATE_X);
    displayText.add("Y:" + String.format("%3.0f", transformVec3f.rotation.getY()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.ROTATE_Y);
    displayText.add("Z:" + String.format("%3.0f", transformVec3f.rotation.getZ()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.ROTATE_Z);

    displayText.add("======"); selectableField.add(NOT_SELECTABLE);
    displayText.add("TRANSL"); selectableField.add(NOT_SELECTABLE);
    displayText.add("X:" + String.format("%.2f", transformVec3f.translation.getX()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.TRANSLATE_X);
    displayText.add("Y:" + String.format("%.2f", transformVec3f.translation.getY()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.TRANSLATE_Y);
    displayText.add("Z:" + String.format("%.2f", transformVec3f.translation.getZ()));
      selectableField.add(HUDinfoUpdateLink.SelectedField.TRANSLATE_Z);

    displayText.add("======"); selectableField.add(NOT_SELECTABLE);
    displayText.add("RESET"); selectableField.add(HUDinfoUpdateLink.SelectedField.RESTORE_DEFAULT);
    displayText.add("======"); selectableField.add(NOT_SELECTABLE);

    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    int ypos = 2;
    int xpos = 2;
    for (int i = 0; i < displayText.size(); ++i) {
      String msg = displayText.get(i);
      ypos += fontRenderer.FONT_HEIGHT;
      if (msg == null) continue;
      final int MED_GRAY_HALF_TRANSPARENT = 0x6FAFAFB0;
      final int GREEN_HALF_TRANSPARENT = 0x6F00FF00;
      boolean fieldIsSelected = (huDinfoUpdateLink.selectedField == selectableField.get(i));
      int highlightColour = fieldIsSelected ? GREEN_HALF_TRANSPARENT : MED_GRAY_HALF_TRANSPARENT;
      drawRect(xpos - 1, ypos - 1, xpos + fontRenderer.getStringWidth(msg) + 1, ypos + fontRenderer.FONT_HEIGHT - 1, highlightColour);
      final int LIGHT_GRAY = 0xE0E0E0;
      final int BLACK = 0x000000;
      int stringColour = fieldIsSelected ? BLACK : LIGHT_GRAY;
      fontRenderer.drawString(msg, xpos, ypos, stringColour);
    }
  }

//  public ItemModelFlexibleCamera.UpdateLink getItemOverrideLink() {
//    return itemOverrideLink;
//  }
//
//  private ItemModelFlexibleCamera.UpdateLink itemOverrideLink = new ItemModelFlexibleCamera.UpdateLink();
//
//
//  public void renderOverlay(ScaledResolution scaledResolution, int animationTickCount, float partialTick) {
//    final double FADE_IN_DURATION_TICKS = 20;
//    final double FADE_OUT_DURATION_TICKS = 10;
////    if (renderPhase != RenderPhase.CROSSHAIRS) return;
//    boolean shouldIRender = infoProvider.refreshRenderInfo(renderInfo);
//    if (!shouldIRender) return;
//
//    boolean newMessageArrived = false;
//    if (!renderInfo.messageToDisplay.isEmpty() &&
//            !renderInfo.messageToDisplay.equals(currentlyDisplayedMessage)) {
//      newMessageArrived = true;
//    }
//
//    double animationCounter = animationTickCount + (double) partialTick;
//    if (newMessageArrived) {  // don't start a new message until the old one has faded out
//      if (animationState == AnimationState.NONE) {
//        startMessageFadeIn(renderInfo.messageToDisplay, animationCounter);
//      }
//    } else if (renderInfo.messageToDisplay.isEmpty()) {
//      if (animationState != AnimationState.NONE && animationState != AnimationState.FADE_OUT) {
//        startMessageFadeOut(animationCounter);
//      }
//    }
//
//    final double OPACITY_MIN = 0.2;
//    final double OPACITY_MAX = 1.0;
//    double opacity = OPACITY_MIN;
//    switch (animationState) {
//      case NONE: {
//        currentlyDisplayedMessage = "";
//        return;
//      }
//      case FADE_IN: {
//        if (animationCounter >= animationFadeInStartCounter + FADE_IN_DURATION_TICKS) {
//          animationState = AnimationState.SUSTAIN;
//        }
//        opacity = UsefulFunctions.interpolate(animationCounter, animationFadeInStartCounter, animationFadeInStartCounter + FADE_IN_DURATION_TICKS,
//                OPACITY_MIN, OPACITY_MAX);
//        break;
//      }
//      case FADE_OUT: {
//        if (animationCounter >= animationFadeOutStartCounter + FADE_OUT_DURATION_TICKS) {
//          animationState = AnimationState.NONE;
//        }
//        opacity = UsefulFunctions.interpolate(animationCounter, animationFadeOutStartCounter, animationFadeOutStartCounter + FADE_OUT_DURATION_TICKS,
//                OPACITY_MAX, OPACITY_MIN);
//        break;
//      }
//      case SUSTAIN: {
//        opacity = OPACITY_MAX;
//        break;
//      }
//      default:
//        assert false : "Invalid animationState " + animationState + " in RendererStatusMessage";
//    }
//
//    int width = scaledResolution.getScaledWidth();
//    int height = scaledResolution.getScaledHeight();
//
//
//    int textColour = Colour.WHITE_40.getColourForFontRenderer(opacity);
//    final int MESSAGE_HEIGHT_OFFSET = 8;
//    drawHoveringText(currentlyDisplayedMessage, width/2, height/2 + MESSAGE_HEIGHT_OFFSET, textColour);
//  }
//
//  /**
//   * draw hovering text centred at x, y
//   */
//  private void drawHoveringText(String message, int x, int y, int rgba)
//  {
//    if (message.isEmpty()) return;
//    FontRenderer font = Minecraft.getMinecraft().fontRenderer;
//    try {
//      GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//      GL11.glPushMatrix();
//      GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//      RenderHelper.disableStandardItemLighting();
//      GL11.glDisable(GL11.GL_LIGHTING);
//      GL11.glDisable(GL11.GL_DEPTH_TEST);
//
//      int stringWidth = font.getStringWidth(message);
//      font.drawStringWithShadow(message, x - stringWidth / 2, y, rgba);
//
//    } finally {
//      RenderHelper.enableStandardItemLighting();
//      GL11.glPopMatrix();
//      GL11.glPopAttrib();
//    }
//  }
//
//
//  RenderGameOverlayEvent.Text event = new RenderGameOverlayEvent.Text(eventParent, listL, listR);
//  if (!MinecraftForge.EVENT_BUS.post(event))
//  {
//
//    top = 2;
//    for (String msg : listR)
//    {
//      top += fontrenderer.FONT_HEIGHT;
//      if (msg == null) continue;
//      int w = fontrenderer.getStringWidth(msg);
//      int left = width - 2 - w;
//      drawRect(left - 1, top - 1, left + w + 1, top + fontrenderer.FONT_HEIGHT - 1, -1873784752);
//      fontrenderer.drawString(msg, left, top, 14737632);
//    }
//  }

  // copied straight from vanilla GuiIngameForge
  public static void drawRect(int left, int top, int right, int bottom, int color)
  {
    int j1;

    if (left < right)
    {
      j1 = left;
      left = right;
      right = j1;
    }

    if (top < bottom)
    {
      j1 = top;
      top = bottom;
      bottom = j1;
    }

    float f3 = (float)(color >> 24 & 255) / 255.0F;
    float f = (float)(color >> 16 & 255) / 255.0F;
    float f1 = (float)(color >> 8 & 255) / 255.0F;
    float f2 = (float)(color & 255) / 255.0F;
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.color(f, f1, f2, f3);
    worldrenderer.startDrawingQuads();
    worldrenderer.addVertex((double)left, (double)bottom, 0.0D);
    worldrenderer.addVertex((double)right, (double)bottom, 0.0D);
    worldrenderer.addVertex((double)right, (double)top, 0.0D);
    worldrenderer.addVertex((double)left, (double)top, 0.0D);
    tessellator.draw();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
  }


}
