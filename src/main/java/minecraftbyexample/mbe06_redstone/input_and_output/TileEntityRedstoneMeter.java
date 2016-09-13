package minecraftbyexample.mbe06_redstone.input_and_output;

import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This TileEntity is used for two main purposes:
 *  1) on the client side - retrieve the current power level for rendering in the associated TileEntitySpecialRenderer (TESR)
 *  2) on the server side
 *     a) used to store the current power level.  This is necessary due to the way that the redstone signals propagate,
 *        e.g. getWeakPower() must retrieve a stored value and not calculate it from neighbours.
 *        see here for more information http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html
 *     b) used to flash the output at a defined rate using block tick scheduling.
 */
public class TileEntityRedstoneMeter extends TileEntity {

  //  --- client side methods used by the renderer

  // Retrieve the current power level of the meter - the maximum of the four sides (don't look up or down)
	public int getPowerLevelClient() {

    // int powerLevel = this.worldObj.isBlockIndirectlyGettingPowered(this.pos);  // if input can come from any side, use this line

    int maxPowerFound = 0;
    for (EnumFacing whichFace : EnumFacing.HORIZONTALS) {
      BlockPos neighborPos = pos.offset(whichFace);
      int powerLevel = this.worldObj.getRedstonePower(neighborPos, whichFace);
      maxPowerFound = Math.max(powerLevel, maxPowerFound);
    }
    return maxPowerFound;
  }

  // return the smoothed position of the needle, based on the power level
  public double getSmoothedNeedlePosition()
  {
    int newPowerLevel = getPowerLevelClient();
    if (newPowerLevel != lastPowerLevel) {
      lastPowerLevel = newPowerLevel;
      double targetNeedlePosition = getPowerLevelClient() / 15.0;
      smoothNeedleMovement.setTargetNeedlePosition(targetNeedlePosition, false);
    }

    return smoothNeedleMovement.getSmoothedNeedlePosition();
  }

  final double NEEDLE_ACCELERATION = 0.4; // acceleration in units per square second
  final double NEEDLE_MAX_SPEED = 0.4;    // maximum needle movement speed in units per second
  SmoothNeedleMovement smoothNeedleMovement = new SmoothNeedleMovement(NEEDLE_ACCELERATION, NEEDLE_MAX_SPEED);
  int lastPowerLevel = -1;

  // -------- server side methods used to keep track of the current power level and alter the output signal state

  public boolean getOutputState()
  {
    return scheduledTogglingOutput.isOn();
  }

  /** whenever a scheduled block update occurs, call this method
   *
   */
  public void onScheduledUpdateTick()
  {
    scheduledTogglingOutput.onUpdateTick(this.getWorld(), this.getPos(), this.getBlockType());
  }

   /**
   *  Change the stored power level (and alters the flashing rate of the power output)
   */
  public void setPowerLevel(int newPowerLevel)
  {
    if (newPowerLevel == storedPowerLevel) return;
    storedPowerLevel = newPowerLevel;
    if (newPowerLevel == 0) {   // always off
      scheduledTogglingOutput.setSteadyOutput(false);
    } else if (newPowerLevel == 15) { // always on
      scheduledTogglingOutput.setSteadyOutput(true);
    } else {
          // flashing: slowest = 1 seconds in 4 seconds; fastest = 0.25 seconds in 0.5 seconds.
      final int LOWEST_POWER = 1;
      final int HIGHEST_POWER = 14;
      final int SLOWEST_ON_TIME = 20; // ticks
      final int FASTEST_ON_TIME = 5; // ticks
      final int SLOWEST_PERIOD = 80; // ticks
      final int FASTEST_PERIOD = 10;  // ticks
      int periodTicks = (int)UsefulFunctions.interpolate(newPowerLevel, LOWEST_POWER, HIGHEST_POWER, SLOWEST_PERIOD, FASTEST_PERIOD);
      int onTicks = (int) UsefulFunctions
              .interpolate(newPowerLevel, LOWEST_POWER, HIGHEST_POWER, SLOWEST_ON_TIME, FASTEST_ON_TIME);
      scheduledTogglingOutput.setToggleRate(this.getWorld(), this.getPos(), this.getBlockType(), onTicks, periodTicks);
    }
  }

  private ScheduledTogglingOutput scheduledTogglingOutput = new ScheduledTogglingOutput();

  private int storedPowerLevel;


  //---------- general TileEntity methods
	// When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getDescriptionPacket() and onDataPacket() to do this
  // In this case, the power level is recalculated every tick on the client anyway, so we don't need to send anything,
  //   but we need to store the power level on the server, to allow for proper calculation of the redstone power
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

	// This is where you save any data that you don't want to lose when the tile entity unloads
	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save the tiles location
    parentNBTTagCompound.setInteger("storedPowerLevel", storedPowerLevel);
  }

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.readFromNBT(parentNBTTagCompound); // The super call is required to load the tiles location
    storedPowerLevel = parentNBTTagCompound.getInteger("storedPowerLevel");  // defaults to 0 if not found
    if (storedPowerLevel < 0 ) storedPowerLevel = 0;
    if (storedPowerLevel > 15 ) storedPowerLevel = 15;
  }

	/**
	 * Don't render the needle if the player is too far away
	 * @return the maximum distance squared at which the TESR should render
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared()
	{
		final int MAXIMUM_DISTANCE_IN_BLOCKS = 32;
		return MAXIMUM_DISTANCE_IN_BLOCKS * MAXIMUM_DISTANCE_IN_BLOCKS;
	}

	/** Return an appropriate bounding box enclosing the TESR
	 * This method is used to control whether the TESR should be rendered or not, depending on where the player is looking.
	 * The default is the AABB for the parent block, which might be too small if the TESR renders outside the borders of the
	 *   parent block.
	 * If you get the boundary too small, the TESR may disappear when you aren't looking directly at it.
	 * @return an appropriately size AABB for the TileEntity
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		// if your render should always be performed regardless of where the player is looking, use infinite
		AxisAlignedBB infiniteExample = INFINITE_EXTENT_AABB;

		// our needles are all on the block faces so our bounding box is from [x,y,z] to  [x+1, y+1, z+1]
		AxisAlignedBB aabb = new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
		return aabb;
	}

}
