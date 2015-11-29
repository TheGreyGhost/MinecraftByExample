package minecraftbyexample.mbe06_redstone.input;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * This is a simple tile entity which stores the current lamp colour
 * It is used to store lamp colour and send it to the client for rendering
 */
public class TileEntityRedstoneColouredLamp extends TileEntity {

	public int getRGBcolour() {
		return rgbColour;
	}

	public void setRGBcolour(int newRGBcolour)
	{
    rgbColour = newRGBcolour;
	}

	// When the world loads from disk, or when the block is updated, the server needs to send the TileEntity information to the client
	//  it uses getDescriptionPacket() and onDataPacket() to do this
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new S35PacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	// This is where you save any data that
	//  - you don't want to lose when the tile entity unloads
  //  - you want to transmit to the client
	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save the tiles location
  	parentNBTTagCompound.setInteger("rgb_colour", rgbColour);
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.readFromNBT(parentNBTTagCompound); // The super call is required to load the tiles location
    int newColour = parentNBTTagCompound.getInteger("rgb_colour");  // default is 0
    rgbColour = newColour;
	}
  private int rgbColour = 0; // the RGB colour of the lamp
}
