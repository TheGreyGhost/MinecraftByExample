package minecraftbyexample.mbe30b_inventory_basic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * GuiInventoryBasic is a simple gui that does nothing but draw a background image and a line of text on the screen.
 * everything else is handled by the vanilla container code.
 *
 * The Screen is drawn in several layers, most importantly:
 *
 * Background - renderBackground() - eg a grey fill
 * Background texture - drawGuiContainerBackgroundLayer() (eg the frames for the slots)
 * Foreground layer - typically text labels
 *
 */
public class ContainerScreenBasic_IIH extends ContainerScreen<ContainerBasic_IIH> {

  public ContainerScreenBasic_IIH(ContainerBasic_IIH containerBasic, PlayerInventory playerInventory, ITextComponent title) {
    super(containerBasic, playerInventory, title);

		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 176;
		ySize = 133;
	}

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }

  /**
   * Draw the foreground layer for the GuiContainer (everything in front of the items)
   * Taken directly from ContainerScreen
   */
  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		final float LABEL_XPOS = 5;
		final float FONT_Y_SPACING = 12;
		final float CHEST_LABEL_YPOS = ContainerBasic_IIH.TILE_INVENTORY_YPOS - FONT_Y_SPACING;
    this.font.func_243248_b(matrixStack, this.title,
            LABEL_XPOS, CHEST_LABEL_YPOS, Color.darkGray.getRGB());  //this.font.drawString;

    final float PLAYER_INV_LABEL_YPOS = ContainerBasic_IIH.PLAYER_INVENTORY_YPOS - FONT_Y_SPACING;
    this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(),                  ///    this.font.drawString
            LABEL_XPOS, PLAYER_INV_LABEL_YPOS, Color.darkGray.getRGB());
  }

  /**
   * Draws the background layer of this container (behind the items).
   * Taken directly from ChestScreen / BeaconScreen

   */
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);   // this.minecraft.getTextureManager()

    // width and height are the size provided to the window when initialised after creation.
    // xSize, ySize are the expected size of the texture-? usually seems to be left as a default.
    // The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
    //  the available window
    int edgeSpacingX = (this.width - this.xSize) / 2;
    int edgeSpacingY = (this.height - this.ySize) / 2;
    this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
  }

  // This is the resource location for the background image for the GUI
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraftbyexample", "textures/gui/mbe30_inventory_basic_bg.png");
}



