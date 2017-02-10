package minecraftbyexample.mbe06_redstone.input;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.EnumSkyBlock;

import javax.annotation.Nullable;

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
      if (world.isRemote) {
        IBlockState iblockstate = this.world.getBlockState(pos);
        final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
        this.world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
      }
      world.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }

  }

  //	// When the world loads from disk, the server needs to send the TileEntity information to the client
//	//  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this
  @Override
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket()
  {
    NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
    int metadata = getBlockMetadata();
    return new SPacketUpdateTileEntity(this.pos, metadata, updateTagDescribingTileEntityState);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
    handleUpdateTag(updateTagDescribingTileEntityState);
  }

  /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
     Warning - although our getUpdatePacket() uses this method, vanilla also calls it directly, so don't remove it.
   */
  @Override
  public NBTTagCompound getUpdateTag()
  {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();
    writeToNBT(nbtTagCompound);
    return nbtTagCompound;
  }

  /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
   Warning - although our onDataPacket() uses this method, vanilla also calls it directly, so don't remove it.
 */
  @Override
  public void handleUpdateTag(NBTTagCompound tag)
  {
    this.readFromNBT(tag);
  }

	// This is where you save any data that
	//  - you don't want to lose when the tile entity unloads
  //  - you want to transmit to the client
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save the tiles location
  	parentNBTTagCompound.setInteger("rgb_colour", rgbColour);
    return parentNBTTagCompound;
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
