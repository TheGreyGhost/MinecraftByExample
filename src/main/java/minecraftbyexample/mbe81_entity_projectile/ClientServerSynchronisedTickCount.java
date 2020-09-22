package minecraftbyexample.mbe81_entity_projectile;

/**
 * Created by TGG on 10/06/2015.
 * A class to synchronise tick counts sent from the server to the client
 * Ticks faster or slower to "catch up" any mismatch.
 * <p>
 * Usage:
 * (1) Create an instance with the approximate expected update interval (affects speed of resynching)
 * (2) call tick() to increment the counter.  Ticks will occasionally be skipped or an extra added in order to
 * catch up or slow down to the server.
 * (3) periodically call updateFromServer() to inform the most recent value obtained from the server
 * has no effect unless the value has changed from the last call to updateFromServer()
 * If the mismatch is too great, the tick count will jump immediately.
 * (4) reset() to force the client to start at the given value
 *
 * getCurrentTickCount() will return the synchronised tick count
 *
 */
public class ClientServerSynchronisedTickCount {

  /**
   * Creates a new synchronised tick count.
   *
   * @param i_expectedUpdateIntervalTicks roughly how often a server update is
   *                                      expected. Affects the 'catchup' speed.
   */
  public ClientServerSynchronisedTickCount(int i_expectedUpdateIntervalTicks) {
    expectedUpdateInterval = i_expectedUpdateIntervalTicks;
    reset(0);
  }

  public void reset(int newValue) {
    ticksToInsert = 0;
    localTickRate = 1.0;
    cachedRemoteTickCount = newValue;
    localTickCount = newValue;
  }

  // tick the client once; may drop ticks or add extra ticks to resynch with server
  //   once the mismatch has been eliminated, stop dropping/adding ticks
  public int tick() {
    int beforeTick = getCurrentTickCount();
    localTickCount += localTickRate;
    int afterTick = getCurrentTickCount();
    int extraTicksInserted = afterTick - beforeTick - 1;
    if (Math.abs(ticksToInsert) <= Math.abs(extraTicksInserted)) {
      ticksToInsert = 0;
      localTickRate = 1.0;
    } else {
      ticksToInsert -= extraTicksInserted;
    }
    return afterTick;
  }

  // update with the most recent value sent from the server
  // if it is the same as the most recent value, ignore it
  // if the mismatch is too big, just jump to it immediately, otherwise speedup or slow down to match.
  public void updateFromServer(int remoteTickCount) {
    if (remoteTickCount == cachedRemoteTickCount) {
      return;
    }
    double mismatch = remoteTickCount - localTickCount;
    if (mismatch < -MAXIMUM_MISMATCH || mismatch > MAXIMUM_MISMATCH) {
      ticksToInsert = 0;
      localTickCount = remoteTickCount;
    } else {
      ticksToInsert = (int) mismatch;
    }
    localTickRate = 1.0 + ticksToInsert / expectedUpdateInterval;

    cachedRemoteTickCount = remoteTickCount;
  }

  public int getCurrentTickCount() {
    if (localTickRate < Integer.MIN_VALUE || localTickRate > Integer.MAX_VALUE) {
      return 0;
    }
    return (int)Math.round(localTickCount);
  }

  public int getCurrentTickCount(float uncorrectedPartialTicks) {
    if (localTickRate < Integer.MIN_VALUE || localTickRate > Integer.MAX_VALUE) {
      return 0;
    }
    return (int)Math.round(localTickCount + uncorrectedPartialTicks);
  }

  public float getCurrentPartialTicks(float uncorrectedPartialTicks) {
    if (localTickRate < Integer.MIN_VALUE || localTickRate > Integer.MAX_VALUE) {
      return 0;
    }
    double whole = Math.rint(localTickCount + uncorrectedPartialTicks);
    return (float)(localTickCount + uncorrectedPartialTicks - whole) ;
  }

  private final double MAXIMUM_MISMATCH = 100;
  private final double expectedUpdateInterval;
  private int cachedRemoteTickCount;
  private int ticksToInsert;
  private double localTickCount;
  private double localTickRate;
}
