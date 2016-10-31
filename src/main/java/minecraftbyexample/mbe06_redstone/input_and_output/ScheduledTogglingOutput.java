package minecraftbyexample.mbe06_redstone.input_and_output;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by TGG on 29/11/2015.
 * Utility class to toggle an output between ON and OFF at a given rate.
 * Uses scheduled updates to do the toggling.
 *
 * Typical usage:
 * (1) create the ScheduledTogglingOutput
 * (2) a) call setToggleRate() to change the on time and/or the period (on time + off time).  This will schedule
 *        a block update as appropriate, using world.scheduleUpdate(); or
 *     b) call setSteadyOutput to stop toggling and just stay at one value
 * (3) when the block update occurs, call onUpdateTick(), which will toggle output if it's time, and will schedule the
 *     next block update as appropriate
 * (4) use isOn() to determine the flasher state
 *
 */
public class ScheduledTogglingOutput
{
  /**
   * Start toggling the output on and off.
   * @param world
   * @param pos
   * @param block
   * @param onTimeTicks the number of ticks for the output to remain true
   * @param periodTicks the period of the output cycle, i.e. ticks spent true plus ticks spent false
   */
  public void setToggleRate(World world, BlockPos pos, Block block, int onTimeTicks, int periodTicks)
  {
    checkArgument(periodTicks >= 0, "expected periodTicks %s to be >=0", periodTicks);
    checkArgument(onTimeTicks >= 0 && onTimeTicks <= periodTicks,
                  "expected onTimeTicks %s to be >=0 and <= periodTicks %s", onTimeTicks, periodTicks);
    if (onTimeTicks == 0) {
      setSteadyOutput(false);
    } else if (onTimeTicks == periodTicks) {
      setSteadyOutput(true);
    } else {
      onTicks = onTimeTicks;
      offTicks = periodTicks - onTimeTicks;
      togglingIsActive = true;
      final boolean FORCE_RESET = true;
      scheduleNextTick(world, pos, block, FORCE_RESET);
    }
  }

  /** Stop toggling and just provide a steady output value
   *
   * @param output the steady output
   */
  public void setSteadyOutput(boolean output)
  {
    togglingIsActive = false;
    outputState = output;
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
    final boolean FORCE_RESET = true;
    scheduleNextTick(world, pos, block, !FORCE_RESET);
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
