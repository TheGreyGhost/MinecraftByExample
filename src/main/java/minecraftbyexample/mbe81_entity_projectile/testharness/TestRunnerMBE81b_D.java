package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangEntity;
import minecraftbyexample.mbe81_entity_projectile.StartupCommon;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by TGG on 31/08/2020.
 *
 * Tests for:
 *
 * inflight: collision with entity - observe damage(use breakpoint)
 * test each enchantment
 * entity bounce off test
 * throw and catch: if either hand free, catch.  if both hands full, drop
 */
public class TestRunnerMBE81b_D {

  public boolean runTest(int test, World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    if (test == 8121) return runTest8121(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8122) return runTest8122(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8123) return runTest8123(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8124) return runTest8124(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8125) return runTest8125(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8126) return runTest8126(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8127) return runTest8127(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8128) return runTest8128(worldIn, playerIn, printFailedTestsOnly);
//    if (test == 8129) return runTest8129(worldIn, playerIn, printFailedTestsOnly);
    return false;
  }

  public boolean runTest8121(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 2000);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 2000);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vec3d spawnpoint = new Vec3d(5, 200, 2000);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8121", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at slow speed");
    return true;
  }

  public boolean runTest8122(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 2000);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 2000);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vec3d spawnpoint = new Vec3d(5, 200, 2000);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8122", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0,  10,
            2,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at high speed");
    return true;
  }


  damage boost
  special damage enchantment
          flame
  knockback
  richochet off an invulnerable entity


  public static BoomerangEntity generateEntity(String name,
                             StringBuilder sb,
                             World world, LivingEntity livingEntity, ItemStack boomerangItemStack,
                             Vec3d startPoint,
                             float apexYaw, float apexPitch, float distanceToApex,
                             float maximumSidewaysDeflection,
                             boolean anticlockwise,
                             float flightSpeed) {
    sb.append(name + " = start " + startPoint + ", apexYaw " + apexYaw + ", apexPitch " + apexPitch
            + ", distanceToApex " + distanceToApex + ", maxSideways " + maximumSidewaysDeflection
            + ", anticlockwise " + anticlockwise + ", flightSpeed " + flightSpeed + "\n");
    return new BoomerangEntity(world, boomerangItemStack, livingEntity,
            startPoint, apexYaw, apexPitch, distanceToApex,
            maximumSidewaysDeflection, anticlockwise, flightSpeed);
  }

  private static final Logger LOGGER = LogManager.getLogger();
}
