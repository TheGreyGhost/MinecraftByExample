package minecraftbyexample;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.HashMap;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * This class is used to create the client and server gui elements when a player opens a gui. There can only be one gui handler registered per mod,
 *   so each GUI is given a unique GuiID which is used to create the correct element.
 * The GuiHandlerRegistry is registered with Forge using
 * 		NetworkRegistry.INSTANCE.registerGuiHandler(ReferenceMod.instance, new GuiHandlerRegistry());
 * Each of the examples, with its own GuiHandler, registers itself with the GuiHandlerRegistry.
 * When GuiHandlerRegistry receives a call from Forge, it passes it to the appropriate example's GuiHandler.
 */
public class GuiHandlerRegistry implements IGuiHandler {

	public void registerGuiHandler(IGuiHandler handler, int guiID)
	{
		registeredHandlers.put(guiID, handler);
	}

	public static GuiHandlerRegistry getInstance() {return guiHandlerRegistry;}

	private HashMap<Integer, IGuiHandler> registeredHandlers = new HashMap<Integer, IGuiHandler>();
	private static GuiHandlerRegistry guiHandlerRegistry = new GuiHandlerRegistry();

	// Gets the server side element for the given gui id- this should return a container
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IGuiHandler handler = registeredHandlers.get(ID);
		if (handler != null) {
			return handler.getServerGuiElement(ID, player, world, x, y, z);
		} else {
			return null;
		}
	}

	// Gets the client side element for the given gui id- this should return a gui
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IGuiHandler handler = registeredHandlers.get(ID);
		if (handler != null) {
			return handler.getClientGuiElement(ID, player, world, x, y, z);
		} else {
			return null;
		}
	}

}
