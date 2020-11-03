package minecraftbyexample.mbe31_inventory_furnace;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import minecraftbyexample.mbe30_inventory_basic.ContainerBasic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * ContainerScreenFurnace is a gui similar to that of a furnace. It has a progress bar and a burn time indicator.
 * Both indicators have mouse over text
 *
 * The Screen is drawn in several layers, most importantly:
 * Background - renderBackground() - eg a grey fill
 * Background texture - drawGuiContainerBackgroundLayer() (eg the frames for the slots)
 * Foreground layer - typically text labels
 * renderHoveredToolTip - for tool tips when the mouse is hovering over something of interest
 */
public class ContainerScreenFurnace extends ContainerScreen<ContainerFurnace> {

	private ContainerFurnace containerFurnace;
	public ContainerScreenFurnace(ContainerFurnace containerFurnace, PlayerInventory playerInventory, ITextComponent title) {
		super(containerFurnace, playerInventory, title);
    this.containerFurnace = containerFurnace;

		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 176;
		ySize = 207;
	}

	// some [x,y] coordinates of graphical elements
	final static int COOK_BAR_XPOS = 49;
  final static  int COOK_BAR_YPOS = 60;
  final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
  final static  int COOK_BAR_ICON_V = 207;
  final static  int COOK_BAR_WIDTH = 80;
  final static  int COOK_BAR_HEIGHT = 17;

  final static  int FLAME_XPOS = 54;
  final static  int FLAME_YPOS = 80;
  final static  int FLAME_ICON_U = 176;   // texture position of flame icon [u,v]
  final static  int FLAME_ICON_V = 0;
  final static  int FLAME_WIDTH = 14;
  final static  int FLAME_HEIGHT = 14;
  final static  int FLAME_X_SPACING = 18;

  final static  int FONT_Y_SPACING = 10;
  final static  int PLAYER_INV_LABEL_XPOS = ContainerFurnace.PLAYER_INVENTORY_XPOS;
  final static  int PLAYER_INV_LABEL_YPOS = ContainerFurnace.PLAYER_INVENTORY_YPOS - FONT_Y_SPACING;

  // deobfuscated name is render
  @Override
  public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.func_230446_a_(matrixStack);                          //     this.renderBackground();
    super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);     //super.render
    this.func_230459_a_(matrixStack, mouseX, mouseY);  //this.renderHoveredToolTip(mouseX, mouseY);
  }

  // Draw the Tool tip text if hovering over something of interest on the screen
  // renderHoveredToolTip
  @Override
  protected void func_230459_a_(MatrixStack matrixStack, int mouseX, int mouseY) {
    if (!this.field_230706_i_.player.inventory.getItemStack().isEmpty()) return;  //this.minecraft  // no tooltip if the player is dragging something

    List<ITextComponent> hoveringText = new ArrayList<ITextComponent>();

    // If the mouse is over the progress bar add the progress bar hovering text
    if (isInRect(guiLeft + COOK_BAR_XPOS, guiTop + COOK_BAR_YPOS, COOK_BAR_WIDTH, COOK_BAR_HEIGHT, mouseX, mouseY)){
      hoveringText.add(new StringTextComponent("Progress:"));
      int cookPercentage =(int)(containerFurnace.fractionOfCookTimeComplete() * 100);
      hoveringText.add(new StringTextComponent(cookPercentage + "%"));
    }

    // If the mouse is over one of the burn time indicators, add the burn time indicator hovering text
    for (int i = 0; i < containerFurnace.FUEL_SLOTS_COUNT; ++i) {
      if (isInRect(guiLeft + FLAME_XPOS + FLAME_X_SPACING * i, guiTop + FLAME_YPOS, FLAME_WIDTH, FLAME_HEIGHT, mouseX, mouseY)) {
        hoveringText.add(new StringTextComponent("Fuel Time:"));
        hoveringText.add(new StringTextComponent(containerFurnace.secondsOfFuelRemaining(i) + "s"));
      }
    }

    // If hoveringText is not empty draw the hovering text.  Otherwise, use vanilla to render tooltip for the slots
    if (!hoveringText.isEmpty()){
      func_243308_b(matrixStack, hoveringText, mouseX, mouseY);  //renderToolTip
    } else {
      super.func_230459_a_(matrixStack, mouseX, mouseY);  //renderHoveredToolTip
    }
  }

  // drawGuiContainerBackgroundLayer is the deobfuscated name

  @Override
	protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int x, int y) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.field_230706_i_.getTextureManager().bindTexture(TEXTURE); //this.minecraft

    // width and height are the size provided to the window when initialised after creation.
    // xSize, ySize are the expected size of the texture-? usually seems to be left as a default.
    // The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
    //  the available window
    // draw the background for this window
    int edgeSpacingX = (this.field_230708_k_ - this.xSize) / 2; //.width
    int edgeSpacingY = (this.field_230709_l_ - this.ySize) / 2; //.height
    this.func_238474_b_(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);  //.blit

    // draw the cook progress bar
		double cookProgress = containerFurnace.fractionOfCookTimeComplete();
		this.func_238474_b_(matrixStack, guiLeft + COOK_BAR_XPOS, guiTop + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V,  //.blit
            (int) (cookProgress * COOK_BAR_WIDTH), COOK_BAR_HEIGHT);

		// draw the fuel remaining bar for each fuel slot flame
		for (int i = 0; i < containerFurnace.FUEL_SLOTS_COUNT; ++i) {
			double burnRemaining = containerFurnace.fractionOfFuelRemaining(i);
			int yOffset = (int)((1.0 - burnRemaining) * FLAME_HEIGHT);
      this.func_238474_b_(matrixStack, guiLeft + FLAME_XPOS + FLAME_X_SPACING * i, guiTop + FLAME_YPOS + yOffset,  //.blit
              FLAME_ICON_U, FLAME_ICON_V + yOffset, FLAME_WIDTH, FLAME_HEIGHT - yOffset);
		}
	}

	@Override
  // drawGuiContainerForegroundLayer
	protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
    // draw the label for the top of the screen
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
    this.field_230712_o_.func_243248_b(matrixStack, this.field_230704_d_, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());  //this.font.drawString;  this.title

    // draw the label for the player inventory slots
    this.field_230712_o_.func_243248_b(matrixStack, this.playerInventory.getDisplayName(),                  ///    this.font.drawString
            PLAYER_INV_LABEL_XPOS, PLAYER_INV_LABEL_YPOS, Color.darkGray.getRGB());
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}

  // This is the resource location for the background image
  private static final ResourceLocation TEXTURE = new ResourceLocation("minecraftbyexample", "textures/gui/mbe31_inventory_furnace_bg.png");
}
