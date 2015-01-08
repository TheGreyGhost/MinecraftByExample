package minecraftbyexample.mbe31_inventory_smelting;


import com.brandon3055.referencemod.lib.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * GuiInventoryAdvanced is a gui similar to that of a furnace. It has a progress bar and a burn time indicator.
 * Both indicators have mouse over text
 */
@SideOnly(Side.CLIENT)
public class GuiInventorySmelting extends GuiContainer {

	// This is the resource location for the background image
	private static final ResourceLocation texture = new ResourceLocation(References.MODID.toLowerCase(), "textures/gui/mbe_example_inventory_advanced.png");
	private TileInventorySmelting tile;

	public GuiInventorySmelting(InventoryPlayer invPlayer, TileInventorySmelting tile) {
		super(new ContainerSmelting(invPlayer, tile));

		// Set the width and height of the gui
		xSize = 176;
		ySize = 195;

		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		fontRendererObj.drawString("Advanced Example Inventory", guiLeft + 5, guiTop + 5, 0x666666);
		// You must re bind the texture and reset the colour if you still need to use it after drawing a string
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1f, 1f, 1f, 1f);

		// get cook progress as a float between 0 and 1
		float progress = (float) tile.cookTime / (float) tile.maxCookTime;
		// draw the progress bar
		drawTexturedModalRect(guiLeft + 90, guiTop + 53, 177, 15, 17, (int) (progress * 23f));
		// get cook burn time indicator as a float between 0 and 1
		float burnHeight = (float) tile.burnTimeRemaining / (float) tile.currentItemBurnTime;
		// Draw burn time indicator
		int xOffset = (int) (burnHeight * 14f);
		drawTexturedModalRect(guiLeft + 38, guiTop + 85 + 14 - xOffset, 176, 14 - xOffset, 14, xOffset);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		List<String> hoveringText = new ArrayList<String>();

		// If the mouse is over the progress bar add the progress bar hovering text
		if (isInRect(guiLeft + 91, guiTop + 54, 15, 22, mouseX, mouseY)){
			hoveringText.add("Progress:");
			hoveringText.add(String.valueOf((int) (((float) tile.cookTime / (float) tile.maxCookTime) * 100f)) + "%");
		}

		// If the mouse is over the burn time indicator add the burn time indicator hovering text
		if (isInRect(guiLeft + 38, guiTop + 86, 13, 13, mouseX, mouseY)){
			hoveringText.add("Fuel Time:");
			hoveringText.add(String.valueOf(tile.burnTimeRemaining / 40) + "s");
		}

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}

	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}
