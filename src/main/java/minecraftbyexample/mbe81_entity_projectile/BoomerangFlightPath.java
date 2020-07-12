package minecraftbyexample.mbe81_entity_projectile;

import minecraftbyexample.usefultools.CubicSpline;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGG on 5/07/2020.
 *
 * This class models the flight path of the boomerang.
 * It calculates the position[x,y,z] of the boomerang at a given time after throwing.
 * It also calculates the direction that the top of the boomerang is facing (the yaw)
 * It uses parametric calculation of the [x,y,z] to do this.
 * see boomerang_flight_path.png for an illustration of the path
 * (In real life, boomerangs fly in a circle, but the teardrop looks better).

 * Normally, minecraft entities use velocity calculations for this purpose.  But that doesn't work well for the
 *   boomerang flight path because it curves is a complex way.
 *
 * The parametric equation for the unscaled teardrop shape is
 * x = 0.5*(1 - cos(theta))
 * y = A*sin(theta)*sin(theta/2)
 * theta = 0 -> 2*PI
 *
 * dx/dtheta = 0.5*sin(theta)
 * dy/dtheta = sin(theta/2)*cos(theta) + 0.5*sin(theta)*cos(theta/2)
 * We want speed to be constant, i.e. dx^2 + dy^2 = constant, which gives us our step size in theta
 *
 * This is hard to solve analytically, and quite slow if we need minecraft to calculate it.
 *
 * So I have used Excel to numerically integrate it (for a typical maximumSidewaysDeflection) and fit the data to a
 * cubic spline, which can be calculated very quickly.
 */
public class BoomerangFlightPath {

  public BoomerangFlightPath(Vec3d startPoint,
                             float apexYaw, float distanceToApex,
                             float maximumSidewaysDeflection,
                             boolean anticlockwise,
                             float flightDuration) {
    this.startPoint = startPoint;
    this.apexYaw = apexYaw;
    this.distanceToApex = distanceToApex;
    this.maximumSidewaysDeflection = maximumSidewaysDeflection;
    this.anticlockwise = anticlockwise;
    this.flightDuration = flightDuration;
    calculateFlightPath();
  }

  private void calculateFlightPath() {
    // in order to calculate the flight path, we take the
    // basic flight path coordinates, transform them to the correct shape:
    //  1) scale the long axis to match the desired distance to the apex
    //  2) scale the sidewaysdeflection axis to the desired sideways deflection
    //  3) rotate around [0,0] based on the player's yaw (direction the player is facing)
    // Then we fit a cubic spline to the points.
    // The anticlockwise/clockwise and the flightDuration are handled during the lookup, not in the fitted curve

    List<Float> tValues = new ArrayList<>();
    List<Float> xValues = new ArrayList<>();
    List<Float> zValues = new ArrayList<>();

    for (float [] point : BASE_FLIGHT_PATH) {
      tValues.add(point[0]);
      float u = point[1] * distanceToApex;
      float v = point[2] * maximumSidewaysDeflection;
      Vec3d offsetFromStart = new Vec3d(u, 0, v).rotateYaw(apexYaw);
      xValues.add((float)offsetFromStart.getX());
      zValues.add((float)offsetFromStart.getZ());
    }
    flightPathX = CubicSpline.createMonotoneCubicSpline(tValues, xValues);
    flightPathZ = CubicSpline.createMonotoneCubicSpline(tValues, zValues);
  }

  // calculate the current position on the flight path
  public Vec3d getPosition(double time) {
    float pathFraction = (float)MathHelper.clamp(time, 0, flightDuration) / flightDuration;
    if (anticlockwise) pathFraction = 1 - pathFraction;

    float x = flightPathX.interpolate(pathFraction);
    float z = flightPathZ.interpolate(pathFraction);
    Vec3d retval = startPoint.add(x, 0, z);
    return retval;
  }

  // get the current yaw of the boomerang
  public float getYaw(double time) {
    // algorithm:
    // 1) calculate the current velocity
    // 2) convert the velocity to a direction (yaw)
    // 3) the face of the boomerang always points perpendicular to the direction of travel, so rotate the velocity by 90 degrees

    Vec3d velocity = getVelocity(time);
    float flightDirection = (float)(MathHelper.atan2(velocity.getZ(), velocity.getX()) * 180 / Math.PI) - 90.0F;
    float topYaw = flightDirection + (anticlockwise ? 90F : -90F);
    return topYaw;
  }

  // the velocity is the derivative of the position:
  //
  public Vec3d getVelocity(double time) {
    float pathFraction = (float)MathHelper.clamp(time, 0, flightDuration) / flightDuration;
    if (anticlockwise) pathFraction = 1 - pathFraction;

    float vx = flightPathX.interpolateFirstDerivative(pathFraction);
    float vz = flightPathZ.interpolateFirstDerivative(pathFraction);
    Vec3d retval = new Vec3d(vx, 0, vz);
    return retval;
  }

  private Vec3d startPoint;
  private float distanceToApex;
  private float apexYaw;
  private float maximumSidewaysDeflection;
  private float flightDuration; // seconds
  private boolean anticlockwise;

  private CubicSpline flightPathX;
  private CubicSpline flightPathZ;

  // the flight path consists of a few points, smoothly connected by a cubic spline.
  // The BASE_FLIGHT_PATH consists of a series of tuples: [path_fraction, lengthways_distance, sideways_distance]
  //  see boomerang_flight_path.
  //  path_fraction = 0.0 at start of flight, and 1.0 at the end of its flight when it has returned to the thrower
  //  lengthways_distance = 0.0 at the thrower and 1.0 at the apex (furthest point) of the flight.
  //  sideways distance is 0.0 for no-sideways-deviation, or +/- with the same scale as lengthways.  The base path
  //    uses a sideways deviation of -0.2 to +0.2.
  //  The path is based on a uniform flight speed throughout its flight.
  //  We then stretch and rotate the path so that it matches the direction that the player throws the boomerang
  //   as well as how far they throw it.
  private static final float [][] BASE_FLIGHT_PATH = {
          {0.0F, 0.00F,  0.00F},
          {0.1F, 0.20F,  0.09F},
          {0.2F, 0.42F,  0.17F},
          {0.3F, 0.64F,  0.20F},
          {0.4F, 0.86F,  0.17F},
          {0.5F, 1.00F,  0.0F},
          {0.6F, 0.86F, -0.17F},
          {0.7F, 0.64F, -0.20F},
          {0.8F, 0.42F, -0.17F},
          {0.9F, 0.20F, -0.09F},
          {1.0F, 0.00F,  0.00F}
  };


}
