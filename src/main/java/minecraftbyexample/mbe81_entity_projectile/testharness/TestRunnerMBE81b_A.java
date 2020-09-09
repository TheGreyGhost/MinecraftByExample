package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangFlightPath;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGG on 31/08/2020.
 * load/save NBT
 */
public class TestRunnerMBE81b_A {
  public boolean runTest(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // test a,b: create a path, serialise it, deserialise it, compare to original
    Vec3d START_POINT = new Vec3d(1, 2, 3);
    BoomerangFlightPath path1 = new BoomerangFlightPath(START_POINT, 90, 0, 10, 1, false, 4);
    CompoundNBT nbt = path1.serializeNBT();
    BoomerangFlightPath path2 = new BoomerangFlightPath(nbt);
    BoomerangFlightPath path3 = new BoomerangFlightPath();
    path3.deserializeNBT(nbt);

    boolean result = true;
    result = TestRunnerMBE81.test(result, "8101a", EqualsBuilder.reflectionEquals(path1, path2, "flightPathX", "flightPathY", "flightPathZ" ));
    result = TestRunnerMBE81.test(result, "8101b", EqualsBuilder.reflectionEquals(path1, path3, "flightPathX", "flightPathY", "flightPathZ"));

    // the following tests produce output which I then graph in Excel to ensure that the shape is correct.

    if (printFailedTestsOnly) return result;

    StringBuilder sb = new StringBuilder();
    List<BoomerangFlightPath> bfps = new ArrayList<>();
    bfps.add(generatePath("Path1", sb, START_POINT, 90,   0,  10,  1,  false, 4));
    bfps.add(generatePath("Path2", sb, START_POINT, 90,   0,  10,  1,   true, 4));
    bfps.add(generatePath("Path3", sb, START_POINT,  0,   0,  10,  1,  false, 4));
    bfps.add(generatePath("Path4", sb, START_POINT, 45,   0,  10,  1,  false, 4));
    bfps.add(generatePath("Path5", sb, START_POINT, 90,  30,  10,  1,  false, 4));
    bfps.add(generatePath("Path6", sb, START_POINT, 90,  80,  10,  1,  false, 4));
    bfps.add(generatePath("Path7", sb, START_POINT, 90,   0,   5,  1,  false, 4));
    bfps.add(generatePath("Path8", sb, START_POINT, 90,   0,  10,  4,  false, 4));
    bfps.add(generatePath("Path9", sb, START_POINT, 90,   0,  10,  1,  false, 4));
    bfps.add(generatePath("Path10", sb, START_POINT.add(-1, 2, 10),
                                                          90,   0,  10,  1,  false, 4));
    bfps.add(generatePath("Path11", sb, START_POINT, 90,   0,  10,  1,  false, 8));

    StringBuilder sbPos = new StringBuilder();
    StringBuilder sbYaw = new StringBuilder();
    StringBuilder sbVec = new StringBuilder();

    for (BoomerangFlightPath bfp : bfps) {
      for (float time = 0; time < 10 * 2.5 / 4; time += 0.1) {
        Vec3d vec3d = bfp.getPosition(time);
        sbPos.append(String.format("%.2f, %.3f, %.3f, %.3f\n", time, vec3d.x, vec3d.y, vec3d.z));
        sbYaw.append(String.format("%.2f, %.0f\n", time, bfp.getYaw(time)));
        vec3d = bfp.getVelocity(time);
        sbVec.append(String.format("%.2f, %.3f, %.3f, %.3f\n", time, vec3d.x, vec3d.y, vec3d.z));
      }
    }
    LOGGER.error("\n" + sb.toString()
                  + "\n" + sbPos.toString()
                  + "\n" + sbYaw.toString()
                  + "\n" + sbVec.toString());
    return result;
  }

  public static BoomerangFlightPath generatePath(String name,
                             StringBuilder sb,
                             Vec3d startPoint,
                             float apexYaw, float apexPitch, float distanceToApex,
                             float maximumSidewaysDeflection,
                             boolean anticlockwise,
                             float flightSpeed) {
    sb.append(name + " = start " + startPoint + ", apexYaw " + apexYaw + ", apexPitch " + apexPitch
            + ", distanceToApex " + distanceToApex + ", maxSideways " + maximumSidewaysDeflection
            + ", anticlockwise " + anticlockwise + ", flightSpeed " + flightSpeed + "\n");
    return new BoomerangFlightPath(startPoint, apexYaw, apexPitch, distanceToApex, maximumSidewaysDeflection, anticlockwise, flightSpeed);
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
