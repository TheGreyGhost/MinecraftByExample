package minecraftbyexample.mbe06_redstone.input_and_output;

/**
 * Created by TGG on 29/11/2015.
 * Utility class to smooth out the motion of a needle on a dial.  Uses System.nanoTime() for timer.
 *
 * Usage:
 * 1) Initialise the class with the desired acceleration and maximum needle movement speed
 * 2) setTargetNeedlePosition() whenever the needle has a new target position.
 * 3) getSmoothedNeedlePosition() to get the current needle position
 *
 */
public class SmoothNeedleMovement {

  public SmoothNeedleMovement(double needleAcceleration, double needleMaxSpeed)
  {
    NEEDLE_ACCELERATION = needleAcceleration;
    NEEDLE_MAX_SPEED = needleMaxSpeed;
  }

  /** sets the target needle position
   * @param targetNeedlePosition new needle position to move to [0 .. 1]
   * @param instant if true - move to new position instantly, otherwise smoothed.
   */
  public void setTargetNeedlePosition(double targetNeedlePosition, boolean instant)
  {
    if (targetNeedlePosition < 0) {
      targetNeedlePosition = 0;
    } else if (targetNeedlePosition > 1) {
      targetNeedlePosition = 1;
    }

    long timeNow = System.nanoTime();
    long deltaTimeNS = timeNow - zeroTimeNanoSeconds;
    double elapsedTime = deltaTimeNS / 1.0e9;

    updateToCurrentTime(elapsedTime);

    if (targetNeedlePosition != lastTargetNeedlePosition) {
      lastTargetNeedlePosition = targetNeedlePosition;
      calculateMovementTimepoints(timeNow, targetNeedlePosition, instant);
    }

  }

  // return the smoothed position of the needle
  public double getSmoothedNeedlePosition()
  {
    long timeNow = System.nanoTime();
    long deltaTimeNS = timeNow - zeroTimeNanoSeconds;
    double elapsedTime = deltaTimeNS / 1.0e9;

    updateToCurrentTime(elapsedTime);
    return needleposition;
  }

  // ------- We smooth out the needle's motion using some equations of motion:
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
    // if we are past the acceleration phase, calculate for the steady movement phase

    final double ARBITRARY_SMALL_DELTA = 0.00001;
    if (remainingTime > ARBITRARY_SMALL_DELTA) {
      double steadyMovementTime = (remainingTime > steadySpeedPhaseDuration) ? steadySpeedPhaseDuration
                                                                             : remainingTime;
      remainingTime -= steadyMovementTime;
      needleposition += steadyMovementTime * needlespeed;
    }

    // if we are past the steady movement phase, calculate for the decelerating phase
    if (remainingTime > ARBITRARY_SMALL_DELTA) {
      double decelerationTime = (remainingTime > decelerationPhaseDuration) ? decelerationPhaseDuration
                                                                            : remainingTime;
      remainingTime -= decelerationTime;
      needleposition += needlespeed * decelerationTime - 0.5 * needleAcceleration * decelerationTime * decelerationTime;
      needlespeed -= decelerationTime * needleAcceleration;
    }

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
  private void calculateMovementTimepoints(long timeNowNS, double newNeedlePosition, boolean instantMovement)
  {
    if (instantMovement) {
      needleIsMoving = false;
      needleposition = newNeedlePosition;
      return;
    }

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
  }

  final double NEEDLE_ACCELERATION; // acceleration in units per square second
  final double NEEDLE_MAX_SPEED;    // maximum needle movement speed in units per second

  private double needleposition = 0; // 0 -> 1
  private double needlespeed;  // units per second

  private double lastTargetNeedlePosition = -1;
  private long zeroTimeNanoSeconds = 0;
  private boolean needleIsMoving = false;
  private double initialNeedleSpeed; // units per second
  private double initialNeedlePosition;
  private double needleAcceleration;  // units per second per second
  private double accelerationPhaseDuration;  // seconds
  private double steadySpeedPhaseDuration; // seconds
  private double decelerationPhaseDuration; // seconds
}
