package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangEntity;
import minecraftbyexample.mbe81_entity_projectile.StartupCommon;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by TGG on 31/08/2020.
 *
 * Tests for:
 * load/save of NBT
 * ballistic path
 * ballistic: movement underwater
 * ballistic: movement in lava
 *
 * inflight: collision with solid objects - increasing numbers; three different hardnesses & observe bounce off.  Observe bounceoff in each direction
 * inflight: collision with entity - observe damage(use breakpoint)
 * inflight: in water adds trails
 * test each enchantment
 * entity bounce off test
 * throw and catch: if either hand free, catch.  if both hands full, drop
 */
public class TestRunnerMBE81b_C {
  public boolean runTest8102(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a stationary boomerang so we can see it rendering
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);
    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    DebugSettings.setDebugParameter("mbe81b_notick", 1);
    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8102", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,90,   0,  10,
            2,  false, 4);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString());
    LOGGER.error("Use /mbedebug param mbe81b_yaw  and /mbedebug param mbe81b_pitch to adjust the rendering interactively.");
    return true;
  }

  public boolean runTest8103(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  Flat path, no yaw
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8103", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString());
    return true;
  }

  public boolean runTest8104(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  60 degree yaw
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8104", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  60, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString());
    return true;
  }

  public boolean runTest8105(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  pitched path
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8105", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, -30,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString());
    return true;
  }

  public boolean runTest8106(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  right hand throw
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8106", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, 0,  10,
            2,  true, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString());
    return true;
  }

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
