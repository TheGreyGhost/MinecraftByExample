package minecraftbyexample.mbe06_redstone.input;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ITickable;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * This is a simple tile entity which stores the current lamp colour
 * It is used to store lamp colour and send it to the client for rendering
 * It also triggers a block render update and relighting check on the client when it detects that the lamp colour
 *   has changed.
 */
public class TileEntityRedstoneColouredLamp extends TileEntity implements ITickable {

	public int getRGBcolour() {
		return rgbColour;
	}

	public void setRGBcolour(int newRGBcolour)
	{
    rgbColour = newRGBcolour;
	}

  // called every tick, on both client and server side
  @Override
  public void update()
  {
    // For some reason, the colour and lighting don't always automatically update just in response to the
    //          worldIn.markBlockForUpdate(pos);  on the server in onNeighborBlockChange().
    // So force the block to re-render when we detect a colour change, otherwise the change in
    //   state will not always be visible.  Likewise, we need to force a lighting recalculation.
    // The block update (for renderer) is only required on client side, but the lighting is required on both, since
    //    the client needs it for rendering and the server needs it for crop growth etc
    int currentRGBcolour = getRGBcolour();
    if (previousRGBcolor != currentRGBcolour) {
      previousRGBcolor = currentRGBcolour;
      if (worldObj.isRemote) {
        worldObj.markBlockForUpdate(pos);
      }
      worldObj.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }

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
  private int previousRGBcolor = -1;
}
