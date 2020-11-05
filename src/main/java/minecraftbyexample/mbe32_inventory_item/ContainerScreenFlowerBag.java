/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2015, 6:42:43 PM (GMT)]
 */
package minecraftbyexample.mbe32_inventory_item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import minecraftbyexample.mbe30_inventory_basic.ContainerBasic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

// * The Screen is drawn in several layers, most importantly:
//         * Background - renderBackground() - eg a grey fill
//         * Background texture - drawGuiContainerBackgroundLayer() (eg the frames for the slots)
//         * Foreground layer - typically text labels
//         * renderHoveredToolTip - for tool tips when the mouse is hovering over something of interest

public class ContainerScreenFlowerBag extends ContainerScreen<ContainerFlowerBag> {

	public ContainerScreenFlowerBag(ContainerFlowerBag container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
	}

  // deobfuscated name is render
	@Override
	public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.func_230446_a_(matrixStack);                          //     this.renderBackground();
    super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);     //super.render
    this.func_230459_a_(matrixStack, mouseX, mouseY);  //this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
  // drawGuiContainerForegroundLayer
	protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
    final float PLAYER_LABEL_XPOS = 8;
    final float PLAYER_LABEL_DISTANCE_FROM_BOTTOM = (96 - 2);

    final float BAG_LABEL_YPOS = 6;
    TranslationTextComponent bagLabel = new TranslationTextComponent(StartupCommon.itemFlowerBag.getTranslationKey());
    float BAG_LABEL_XPOS = (xSize / 2.0F) - this.field_230712_o_.getStringWidth(bagLabel.getString()) / 2.0F;                  // centre the label             //this.font.
    this.field_230712_o_.func_243248_b(matrixStack, bagLabel, BAG_LABEL_XPOS, BAG_LABEL_YPOS, Color.darkGray.getRGB());            //this.font.drawString;

    float PLAYER_LABEL_YPOS = ySize - PLAYER_LABEL_DISTANCE_FROM_BOTTOM;
    this.field_230712_o_.func_243248_b(matrixStack, this.playerInventory.getDisplayName(),                              //this.font.drawString;
                    PLAYER_LABEL_XPOS, PLAYER_LABEL_YPOS, Color.darkGray.getRGB());
	}

	@Override
  // drawGuiContainerBackgroundLayer is the deobfuscated name
	protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.field_230706_i_.getTextureManager().bindTexture(TEXTURE);                //this.minecraft
    // width and height are the size provided to the window when initialised after creation.
    // xSize, ySize are the expected size of the texture-? usually seems to be left as a default.
    // The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
    //  the available window
    int edgeSpacingX = (this.field_230708_k_ - this.xSize) / 2;   //.width
    int edgeSpacingY = (this.field_230709_l_ - this.ySize) / 2;  //.height
    this.func_238474_b_(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);    //.blit
	}

  // This is the resource location for the background image
  private static final ResourceLocation TEXTURE = new ResourceLocation("minecraftbyexample", "textures/gui/mbe32_flower_bag_bg.png");

}
