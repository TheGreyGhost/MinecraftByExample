package minecraftbyexample.mbe81_entity_projectile;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
 *
 *
 */
public class BoomerangFlightPath {

  public BoomerangFlightPath(Vec3d startPoint,
                             double apexYaw, double distanceToApex,
                             double maximumSidewaysDeflection,
                             boolean anticlockwise,
                             double flightDuration) {

  }

  private void integrateCurve() {
    double flightTimeTicks;
    double distancePerTick =

    double theta = 0;
    double dx_dTheta;
    double dy_dTheta;

    dx_dTheta = 0.5F*Math.sin(theta);
    dy_dTheta = Math.sin(theta/2.0)*Math.cos(theta) + 0.5*Math.sin(theta)*Math.cos(theta/2);
  }


  // calculate the current position by scaling the teardrop and rotating it to match the initial throwing direction
  public Vec3d getPosition(double time) {
    time = MathHelper.clamp(time, 0, flightDuration);
    double t = 2*Math.PI * time / flightDuration;

    double u = distanceToApex * 0.5*(1 - Math.cos(t));
    double v = (maximumSidewaysDeflection / 0.77) * Math.sin(t) * Math.sin(t/2);

    Vec3d offsetFromStart = new Vec3d(u, 0, v).rotateYaw(apexYaw);
    Vec3d retval = startPoint.add(offsetFromStart);
    return retval;
  }

  public float getYaw(double time) {
    Vec3d velocity = getVelocity(time);
  }

  // the velocity is the derivative of the position:
  //
  public Vec3d getVelocity(double time) {
    time = MathHelper.clamp(time, 0, flightDuration);
    double t = 2*Math.PI * time / flightDuration;

    double u = distanceToApex * 0.5*(1 - Math.cos(t));
    double v = (maximumSidewaysDeflection / 0.77) * Math.sin(t) * Math.sin(t/2);

    Vec3d offsetFromStart = new Vec3d(u, 0, v).rotateYaw(apexYaw);
    Vec3d retval = startPoint.add(offsetFromStart);
    return retval;

  }


  private Vec3d startPoint;
  private double distanceToApex;
  private float apexYaw;
  private double maximumSidewaysDeflection;
  private double flightDuration; // seconds


}
