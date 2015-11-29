package minecraftbyexample.mbe06_redstone.redstone_meter;

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

import java.awt.*;

/**
 * This is a simple tile entity which is used to retrieve the current power level for rendering in the associated TileEntitySpecialRenderer (TESR)
 */
public class TileEntityRedstoneMeter extends TileEntity {

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

  // return the smoothed position of the needle
  // the needle movement has three phases:
  //  1) acceleration to maximum needle speed
  //  2) steady movement at maximum needle speed
  //  3) deceleration to zero needle speed (at target needle position)
  public double getSmoothedNeedlePosition()
  {
    long timeNow = System.nanoTime();
    long deltaTimeNS = timeNow - zeroTimeNanoSeconds;

    double elapsedTime = deltaTimeNS / 1.0e9;

    updateToCurrentTime(elapsedTime);

    int newPowerLevel = getPowerLevelClient();
    if (newPowerLevel != lastPowerLevel) {
      lastPowerLevel = newPowerLevel;
      double targetNeedlePosition = getPowerLevelClient() / 15.0;
      calculateMovementTimepoints(timeNow, targetNeedlePosition);
    }
    return needleposition;
  }

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
   *  Change the stored power level (alters the flashing rate of the power output)
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

  public int getStoredPowerLevel()
  {
    return storedPowerLevel;
  }

  private int storedPowerLevel;


  // ------- We smooth out the pointer's motion using some equations of motion:
  //  1) steady acceleration to maximum velocity:
  //       speed = initial speed + acceleration * elapsed time
  //       position = initial position + initial speed * time + 0.5 * acceleration * time * time
  //  2) during maximum velocity phase:
  //       speed = constant
  //       position = speed * time
  //  3) deceleration is the same formula as (1) except that the acceleration is reversed.

  /**
   * Update the needle position and movement speed to the current time
   * @param elapsedTime time elapsed since last powerlevel change, in seconds
   */
  private void updateToCurrentTime(double elapsedTime)
  {
    if (!needleIsMoving) {
      needlespeed = 0;
      if (needleposition < 0) needleposition = 0.0;
      if (needleposition > 1) needleposition = 1.0;
      return;
    }

    if (elapsedTime < 0) return;
    double remainingTime = elapsedTime;

    // calculate through the acceleration phase
    double accelerationTime = (remainingTime > accelerationPhaseDuration) ? accelerationPhaseDuration : remainingTime;
    remainingTime -= accelerationTime;

    needlespeed = initialNeedleSpeed + accelerationTime * needleAcceleration;
    needleposition = initialNeedlePosition + initialNeedleSpeed * accelerationTime + 0.5 * needleAcceleration * accelerationTime * accelerationTime;
    System.out.format("Pos: accel %.2f;", needleposition);
    // if we are past the acceleration phase, calculate for the steady movement phase

    final double ARBITRARY_SMALL_DELTA = 0.00001;
    if (remainingTime > ARBITRARY_SMALL_DELTA) {
      double steadyMovementTime = (remainingTime > steadySpeedPhaseDuration) ? steadySpeedPhaseDuration
                                          : remainingTime;
      remainingTime -= steadyMovementTime;
      needleposition += steadyMovementTime * needlespeed;
      System.out.format("stdy %.2f;", needleposition);
    }

    // if we are past the steady movement phase, calculate for the decelerating phase
    if (remainingTime > ARBITRARY_SMALL_DELTA) {
      double decelerationTime = (remainingTime > decelerationPhaseDuration) ? decelerationPhaseDuration
                                        : remainingTime;
      remainingTime -= decelerationTime;
      needleposition += needlespeed * decelerationTime - 0.5 * needleAcceleration * decelerationTime * decelerationTime;
      needlespeed -= decelerationTime * needleAcceleration;
      System.out.format("decel %.2f", needleposition);
    }
    System.out.format("\n");

    // if we're past the decelerating phase, movement is finished.
    if (remainingTime > ARBITRARY_SMALL_DELTA) {
      needleIsMoving = false;
    }
  }


  /**
   * Calculate the parameters for the needle to move to its new position.
   * The needleposition and needlespeed must be current.
   * @param timeNowNS
   */
  private void calculateMovementTimepoints(long timeNowNS, double newNeedlePosition)
  {
    needleIsMoving = true;
    zeroTimeNanoSeconds = timeNowNS;
    initialNeedleSpeed = needlespeed;
    initialNeedlePosition = needleposition;

    // first need to calculate whether the needle will reach steady state speed or not
    // calculate combined duration of the acceleration + deceleration phase

    needleAcceleration = (newNeedlePosition > needleposition) ? NEEDLE_ACCELERATION : -NEEDLE_ACCELERATION;

    double maxNeedleSpeed = (newNeedlePosition > needleposition) ? NEEDLE_MAX_SPEED : -NEEDLE_MAX_SPEED;

    // how long will acceleration, steady, deceleration phases take?
    double accelerationTime = (maxNeedleSpeed - initialNeedleSpeed) / needleAcceleration;
    double decelerationTime = maxNeedleSpeed / needleAcceleration;
    double positionAtAccelEnd = initialNeedlePosition + accelerationTime * initialNeedleSpeed
                                        + 0.5 * accelerationTime * accelerationTime * needleAcceleration;
    double positionAtDecelEnd = positionAtAccelEnd + maxNeedleSpeed * decelerationTime
                                        - 0.5 * decelerationTime * decelerationTime * needleAcceleration;
    double steadyTime = (newNeedlePosition - positionAtDecelEnd) / maxNeedleSpeed;

    // if the duration of the steadyphase is less than zero, we will overshoot!  must shorten the accel and decel phases
    if (steadyTime < 0) {
      double overshoot = positionAtDecelEnd - newNeedlePosition;
      double timeToShortenBy = Math.sqrt(overshoot / needleAcceleration);
      accelerationTime -= timeToShortenBy;
      decelerationTime -= timeToShortenBy;
      steadyTime = 2 * timeToShortenBy;
    }

    accelerationPhaseDuration = accelerationTime;
    steadySpeedPhaseDuration = steadyTime;
    decelerationPhaseDuration = decelerationTime;
    System.out.format("Duration acc %.2f; stdy %.2f; decl %.2f\n", accelerationPhaseDuration, steadySpeedPhaseDuration, decelerationPhaseDuration);
  }

  static final double NEEDLE_ACCELERATION = 0.4; // acceleration in units per square second
  static final double NEEDLE_MAX_SPEED = 0.4;    // maximum needle movement speed in units per second

  private double needleposition = 0; // 0 -> 1
  private double needlespeed;  // units per second

  private int lastPowerLevel = -1;
  private boolean needleIsMoving = false;
  private long zeroTimeNanoSeconds = 0;
  private double initialNeedleSpeed; // units per second
  private double initialNeedlePosition;
  private double needleAcceleration;  // units per second per second
  private double accelerationPhaseDuration;  // seconds
  private double steadySpeedPhaseDuration; // seconds
  private double decelerationPhaseDuration; // seconds

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
