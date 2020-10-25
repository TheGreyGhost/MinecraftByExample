package minecraftbyexample.mbe81_entity_projectile;

import minecraftbyexample.usefultools.CubicSpline;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGG on 5/07/2020.
 *
 * This class models the flight path of the boomerang.
 * It calculates the position[x,y,z] of the boomerang at a given time after throwing.
 * It also calculates the direction that the top of the boomerang is facing (the yaw)
 *
 * Normally, minecraft entities use velocity calculations for this purpose.  But that doesn't work well for the
 *   boomerang flight path because it curves in a complex way.
 *
 * Instead, the flight path is stored as a cubic spline (see google) - and every tick, the boomerang is moved to the
 *   appropriate position on the pre-calculated flight path.  Unlike normal minecraft projectiles, the velocity is not
 *   used to calculate positions.
 *
 * A cubic spline is used because it is very useful for drawing a smooth line between
 *   points; i.e. you specify (eg) ten points [x,y,z] that lie on your desired flight path, then use the cubic spline to
 *   smoothly interpolate between them.
 *
 * The original flight path of the boomerang is based on parametric calculation of [x,y,z] as a function of "theta"
 * see boomerang_flight_path.png for an illustration of the path
 * (In real life, boomerangs fly in a circle, but the teardrop looks better).

 * The parametric equation for the unscaled teardrop shape is
 * x = 0.5*(1 - cos(theta))
 * y = A*sin(theta)*sin(theta/2)
 * theta = 0 -> 2*PI
 *
 * dx/dtheta = 0.5*sin(theta)
 * dy/dtheta = sin(theta/2)*cos(theta) + 0.5*sin(theta)*cos(theta/2)
 * We want speed to be constant, i.e. dx^2 + dy^2 = constant, which gives us our step size in theta as a function of time
 *
 * This is hard to solve analytically, and quite slow if we need minecraft to calculate it.
 *
 * So I have used Excel to numerically integrate it (for a typical maximumSidewaysDeflection) and fit the data to a
 * cubic spline, which can be calculated very quickly.
 *
 * After calculating the position in a horizontal plane, we tilt the plane upwards to match the pitch, using vector maths.
 *
 * TYpical usage:
 * 1) Initialise the BoomerangFlightPath() with the relevant parameters (speed, direction, etc)
 * 2) Each tick, use getPosition(currentTime), getVelocity(currentTime), getYaw(currentTime) to retrieve the position, velocity, and yaw
 * 3) hasReachEndOfFlightPath(currentTime) can be used to determine if the flight is finished
 * 4) the flight path can be saved to/from NBT using serializeNBT() and   deserializeNBT()
 *
 */
public class BoomerangFlightPath implements INBTSerializable<CompoundNBT> {

  /**
   * @param startPoint  the spawn point of the flight path
   * @param apexYaw the yaw angle in degrees (compass direction of the apex relative to the thrower).  0 degrees is south and increases clockwise.
   * @param apexPitch the pitch angle in degrees (elevation/declination of the apex relative to the thrower).  0 degrees is horizontal: -90 is up, 90 is down.
   * @param distanceToApex number of blocks to the apex of the flight path
   * @param maximumSidewaysDeflection maximum sideways deflection from the straight line joining the thrower and the apex
   * @param anticlockwise is the flight path clockwise or anticlockwise?
   * @param flightSpeed speed of the flight in blocks per second
   */
  public BoomerangFlightPath(Vector3d startPoint,
                             float apexYaw, float apexPitch, float distanceToApex,
                             float maximumSidewaysDeflection,
                             boolean anticlockwise,
                             float flightSpeed) {
    this.startPoint = startPoint;
    this.apexYaw = apexYaw;
    this.apexPitch = apexPitch;
    this.distanceToApex = distanceToApex;
    this.maximumSidewaysDeflection = maximumSidewaysDeflection;
    this.anticlockwise = anticlockwise;
    this.flightDuration = BASE_FLIGHT_PATH_LENGTH * distanceToApex / flightSpeed;
    calculateFlightPath();
    initialised = true;
  }

  public BoomerangFlightPath(CompoundNBT nbt) {
    deserializeNBT(nbt);
    initialised = true;
  }

  /**
   * Default/dummy (do nothing)
   */
  public BoomerangFlightPath() {
    distanceToApex = 1;
    flightDuration = 1;
    calculateFlightPath();
  }

  // calculate the current position on the flight path
  // time in seconds
  public Vector3d getPosition(double time) {
//    Preconditions.checkState(initialised);
    float pathFraction = (float)MathHelper.clamp(time, 0, flightDuration) / flightDuration;
    if (anticlockwise) pathFraction = 1 - pathFraction;

    float x = flightPathX.interpolate(pathFraction);
    float y = flightPathY.interpolate(pathFraction);
    float z = flightPathZ.interpolate(pathFraction);
    Vector3d retval = startPoint.add(x, y, z);
    return retval;
  }

  public boolean hasReachedEndOfFlightPath(double time) {
    return time >= flightDuration;
  }

  // get the current yaw of the boomerang
  public float getYaw(double time) {
//    Preconditions.checkState(initialised);
    // algorithm:
    // 1) calculate the current velocity
    // 2) convert the velocity to a direction (yaw)
    // 3) the face of the boomerang always points perpendicular to the direction of travel, so rotate the velocity by 90 degrees

    Vector3d velocity = getVelocity(time);
    float flightDirection = (float)(MathHelper.atan2(velocity.getZ(), velocity.getX()) * 180 / Math.PI) - 90.0F;
    float topYaw = flightDirection + (anticlockwise ? 90F : -90F);
    return topYaw;
  }

  // the velocity is the derivative of the position:
  //
  public Vector3d getVelocity(double time) {
//    Preconditions.checkState(initialised);
    float pathFraction = (float)MathHelper.clamp(time, 0, flightDuration) / flightDuration;
    if (anticlockwise) pathFraction = 1 - pathFraction;

    float vx = flightPathX.interpolateFirstDerivative(pathFraction);
    float vy = flightPathY.interpolateFirstDerivative(pathFraction);
    float vz = flightPathZ.interpolateFirstDerivative(pathFraction);
    Vector3d retval = new Vector3d(vx, vy, vz);
    return retval;
  }


  /*  Save our flight path to NBT for storage on disk or for transmission to client
   */
  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put(START_POINT_NBT, UsefulFunctions.serializeVec3d(startPoint));
    nbt.putFloat(DISTANCE_TO_APEX_NBT, distanceToApex);
    nbt.putFloat(APEX_YAW_NBT, apexYaw);
    nbt.putFloat(APEX_PITCH_NBT, apexPitch);
    nbt.putFloat(MAXIMUM_SIDEWAYS_DEFLECTION_NBT, maximumSidewaysDeflection);
    nbt.putFloat(FLIGHT_DURATION_NBT, flightDuration);
    nbt.putBoolean(ANTICLOCKWISE_NBT, anticlockwise);
    return nbt;
  }

  /*  Create a path from NBT (after loading from disk or transmitted from server)
   */
  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    startPoint = UsefulFunctions.deserializeVec3d(nbt, START_POINT_NBT);
    distanceToApex = nbt.getFloat(DISTANCE_TO_APEX_NBT);
    apexYaw = nbt.getFloat(APEX_YAW_NBT);
    apexPitch = nbt.getFloat(APEX_PITCH_NBT);
    maximumSidewaysDeflection  = nbt.getFloat(MAXIMUM_SIDEWAYS_DEFLECTION_NBT);
    flightDuration = nbt.getFloat(FLIGHT_DURATION_NBT);
    anticlockwise = nbt.getBoolean(ANTICLOCKWISE_NBT);
    calculateFlightPath();
  }

  private final String START_POINT_NBT = "startpoint";
  private final String DISTANCE_TO_APEX_NBT = "distancetoapex";
  private final String APEX_YAW_NBT = "apexyaw";
  private final String APEX_PITCH_NBT = "apexpitch";
  private final String MAXIMUM_SIDEWAYS_DEFLECTION_NBT = "maximumsidewaysdeflection";
  private final String FLIGHT_DURATION_NBT = "flightduration";
  private final String ANTICLOCKWISE_NBT = "anticlockwise";

  private Vector3d startPoint;
  private float distanceToApex;
  private float apexYaw;
  private float apexPitch;
  private float maximumSidewaysDeflection;
  private float flightDuration; // seconds
  private boolean anticlockwise;

  private CubicSpline flightPathX;
  private CubicSpline flightPathY;
  private CubicSpline flightPathZ;

  private boolean initialised = false;

  private void calculateFlightPath() {
    // in order to calculate the flight path, we take the
    // basic flight path coordinates, transform them to the correct shape:
    //  1) scale the long axis (longways coordinate) to match the desired distance to the apex
    //  2) scale the sidewaysdeflection axis (sideways coordinate) to the desired sideways deflection
    //  3) pitch up around the x axis to tilt the flight path to match the apexPitch
    //  3) rotate around the y axis based on the player's yaw (direction the player is facing)
    // Then we fit a cubic spline to the points.
    // The anticlockwise/clockwise and the flightDuration are handled during the lookup, not in the fitted curve

    List<Float> tValues = new ArrayList<>();
    List<Float> xValues = new ArrayList<>();
    List<Float> yValues = new ArrayList<>();
    List<Float> zValues = new ArrayList<>();

    float apexYawRadians = (float)(apexYaw * Math.PI / 180.0);
    float apexPitchRadians = (float)(apexPitch * Math.PI / 180.0);

    for (float [] point : BASE_FLIGHT_PATH) {
      tValues.add(point[0]);
      float longways = point[1] * distanceToApex;
      float sideways = (point[2] / BASE_FLIGHT_PATH_MAX_SIDEWAYS_DEFLECTION) * maximumSidewaysDeflection;
      Vector3d offsetFromStart = new Vector3d(sideways, 0, longways).rotatePitch(-apexPitchRadians).rotateYaw(-apexYawRadians);
      xValues.add((float)offsetFromStart.getX());
      yValues.add((float)offsetFromStart.getY());
      zValues.add((float)offsetFromStart.getZ());
    }
    flightPathX = CubicSpline.createCubicSpline(tValues, xValues);
    flightPathY = CubicSpline.createCubicSpline(tValues, yValues);
    flightPathZ = CubicSpline.createCubicSpline(tValues, zValues);
  }

//  /**
//   * For a given point in the xz plane [x, 0, z], pitch it upwards to have a tilted path.
//   * i.e. if the player is looking horizontally when throwing, the flight path stays level (y = constant) throughout.
//   * if the player is looking slightly up when throwing, with a pitch of -10 degrees, the flight path is tilted upwards
//   *   at an angle of 10 degrees from the direction they are facing (yaw).
//   *
//   * The algorithm for this is:
//   * 1) start with the point on the flight path in the xz plane [x, 0, z]
//   * 2) rotate the point around the y-axis to remove the player's yaw, so that the flight path apex is pointing south.
//   * 3) rotate the point around the x axis to tilt the path upwards by the pitch
//   * 4) rotate the tilted point around the y-axis again, in the opposite direction, to return the apex to matching the player's yaw
//   *
//   * @param point the point on the flight path; y is ignored!
//   * @param apexYaw the yaw angle in degrees (compass direction of the apex relative to the thrower).  0 degrees is south and increases clockwise.
//   * @param apexPitch the pitch angle in degrees (elevation/declination of the apex relative to the thrower).  0 degrees is horizontal: -90 is up, 90 is down.
//
//   * @return the
//   */
//
//  public Vec3d getTiltedPathPoint(Vec3d point, float apexYaw, float apexPitch) {
//    Vec3d planePoint = new Vec3d(point.x, 0, point.z);
//
//  }
//

  // the flight path consists of a few points, smoothly connected by a cubic spline.
  // The BASE_FLIGHT_PATH consists of a series of tuples: [path_fraction, lengthways_distance, sideways_distance]
  //  see boomerang_flight_path.png.
  //  path_fraction = 0.0 at start of flight, and 1.0 at the end of its flight when it has returned to the thrower
  //  lengthways_distance = 0.0 at the thrower and 1.0 at the apex (furthest point) of the flight.
  //  sideways distance is 0.0 for no-sideways-deviation, or +/- with the same scale as lengthways.  The base path
  //    uses a sideways deviation of -0.2 to +0.2.
  //  The path is based on a uniform flight speed throughout its flight.
  //  We then rotate and stretch the path so that it matches the direction that the player throws the boomerang
  //   as well as how far they throw it.
  private static final float [][] BASE_FLIGHT_PATH = {
          {0.00F, 0.000F,  0.000F},
          {0.05F, 0.101F,  0.050F},
          {0.10F, 0.204F,  0.094F},
          {0.15F, 0.310F,  0.134F},
          {0.20F, 0.419F,  0.166F},
          {0.25F, 0.528F,  0.188F},
          {0.30F, 0.643F,  0.200F},
          {0.35F, 0.755F,  0.194F},
          {0.40F, 0.864F,  0.165F},
          {0.45F, 0.958F,  0.102F},
          {0.50F, 1.000F,  0.000F},
          {0.55F, 0.959F, -0.102F},
          {0.60F, 0.867F, -0.165F},
          {0.65F, 0.757F, -0.194F},
          {0.70F, 0.643F, -0.200F},
          {0.75F, 0.528F, -0.188F},
          {0.80F, 0.419F, -0.166F},
          {0.85F, 0.310F, -0.134F},
          {0.90F, 0.204F, -0.094F},
          {0.95F, 0.101F, -0.050F},
          {1.00F, 0.000F,  0.000F}
  };
  private static final float BASE_FLIGHT_PATH_MAX_SIDEWAYS_DEFLECTION = 0.20F;

  private static final float BASE_FLIGHT_PATH_LENGTH = 2.26F;
}
