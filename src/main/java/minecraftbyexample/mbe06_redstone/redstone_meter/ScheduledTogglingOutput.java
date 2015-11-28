package minecraftbyexample.mbe06_redstone.redstone_meter;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by TGG on 29/11/2015.
 * Utility class to toggle an output between ON and OFF at a given rate.
 * Uses scheduled updates to do the toggling.
 *
 * Typical usage:
 * (1) create the ScheduledTogglingOutput
 * (2) call changeToggleRate() to change the on time and/or the period (on time + off time).  This will schedule
 *     a block update as appropriate, using world.scheduleUpdate()
 * (3) when the block update occurs, call onUpdateTick(), which will toggle output if it's time, and will schedule the
 *     next block update as appropriate
 * (4) use isOn() to determine the flasher state
 *
 */
public class ScheduledTogglingOutput
{
  public void changeToggleRate(World world, BlockPos pos, Block block, int onTimeTicks, int periodTicks)
  {
    checkArgument(periodTicks >= 0, "expected periodTicks %s to be >=0", periodTicks);
    checkArgument(onTimeTicks >= 0 && onTimeTicks <= periodTicks,
                  "expected onTimeTicks %s to be >=0 and <= periodTicks %s", onTimeTicks, periodTicks);
    togglingIsActive = false;
    if (onTimeTicks == 0) {
      outputState = false;
    } else if (onTimeTicks == periodTicks) {
      outputState = true;
    } else {
      togglingIsActive = true;
    }
    onTicks = onTimeTicks;
    offTicks = periodTicks - onTimeTicks;
    scheduleNextTick(world, pos, block, true);
  }

  /**
   * Call when the update tick is received.
   */
  public void onUpdateTick(World world, BlockPos pos, Block block)
  {
    if (!togglingIsActive) return;
    ticksTillOutputStateChange -= lastTickDelay;
    if (ticksTillOutputStateChange == 0) {
        outputState = !outputState;
    }
    scheduleNextTick(world, pos, block, false);
  }

  private void scheduleNextTick(World world, BlockPos pos, Block block, boolean reset)
  {
    if (!togglingIsActive) return;
    if (reset || ticksTillOutputStateChange == 0) {
      ticksTillOutputStateChange = (outputState == true) ? onTicks : offTicks;
    }
    final int MAXIMUM_DELAY_UNTIL_NEXT_UPDATE = 20;  // always tick at least once every 20 ticks.
    lastTickDelay = Math.min(ticksTillOutputStateChange, MAXIMUM_DELAY_UNTIL_NEXT_UPDATE);
    if (lastTickDelay != 0) {  // should never happen, but be defensive...
      world.scheduleUpdate(pos, block, lastTickDelay);
    }
    return;
  }


  public boolean isOn()
  {
    return outputState;
  }

  private boolean outputState = false;
  private int ticksTillOutputStateChange = 0;
  private int lastTickDelay = 0;
  private int onTicks = 0;
  private int offTicks = 0;
  private boolean togglingIsActive = false;

}
