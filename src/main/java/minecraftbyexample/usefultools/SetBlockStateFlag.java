package minecraftbyexample.usefultools;

/**
 * Created by TGG on 6/04/2020.
 *
 * Helper class to set flags when using SetBlockState
 *
 */
public enum SetBlockStateFlag {
/**
 * Sets a block state into this world.Flags are as follows:
 * 1 will cause a block update.
 * 2 will send the change to clients.
 * 4 will prevent the block from being re-rendered.
 * 8 will force any re-renders to run on the main thread instead
 * 16 will prevent neighbor reactions (e.g. fences connecting, observers pulsing).
 * 32 will prevent neighbor reactions from spawning drops.
 * 64 will signify the block is being moved.
 * Flags can be OR-ed
*/

  BLOCK_UPDATE(1),
  SEND_TO_CLIENTS(2),
  DO_NOT_RENDER(4),
  RUN_RENDER_ON_MAIN_THREAD(8),
  PREVENT_NEIGHBOUR_REACTIONS(16),
  NEIGHBOUR_REACTIONS_DONT_SPAWN_DROPS(32),
  BLOCK_IS_BEING_MOVED(64);

  public static int get(SetBlockStateFlag... flags) {
    int result = 0;
    for (SetBlockStateFlag flag : flags) {
      result |= flag.flagValue;
    }
    return result;
  }

  SetBlockStateFlag(int flagValue) {this.flagValue = flagValue;}
  private int flagValue;
}
