package minecraftbyexample.mbe30_inventory_basic;


import com.brandon3055.referencemod.lib.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * GuiInventoryBasic is a simple gui that dose nothing but draw a background image and a line of text on the screen
 * everything else is handled by the container
 */
@SideOnly(Side.CLIENT)
public class GuiInventoryBasic extends GuiContainer {

	// This is the resource location for the background image
	private static final ResourceLocation texture = new ResourceLocation(References.MODID.toLowerCase(), "textures/gui/mbe_example_inventory_basic.png");

	public GuiInventoryBasic(InventoryPlayer invPlayer, TileInventoryBasic tile) {
		super(new ContainerBasic(invPlayer, tile));

		// Set the width and height of the gui
		xSize = 176;
		ySize = 133;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		fontRendererObj.drawString("Basic Example Inventory", guiLeft + 5, guiTop + 5, 0x666666);
	}

}
