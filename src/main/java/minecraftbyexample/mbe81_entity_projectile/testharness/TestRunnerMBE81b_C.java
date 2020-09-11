package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangEntity;
import minecraftbyexample.mbe81_entity_projectile.StartupCommon;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.Blocks;
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
 * ballistic path
 * ballistic: movement underwater
 * ballistic: movement in lava
 *
 * inflight: collision with solid objects - increasing numbers; three different hardnesses & observe bounce off.  Observe bounceoff in each direction
 * inflight: collision with entity - observe damage(use breakpoint)
 * test each enchantment
 * entity bounce off test
 * throw and catch: if either hand free, catch.  if both hands full, drop
 */
public class TestRunnerMBE81b_C {
  public boolean runTest8110(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // ballistic movement: drop from a height and bounce
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(200, 210, 0);
    Vec3d OBSERVER_POINT = new Vec3d(203, 200, 0);
    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    BlockPos centre = new BlockPos(200, 199, 0);
    TestRunnerMBE81.createBasin(playerIn, centre, 10, 1, Blocks.AIR.getDefaultState());
    DebugSettings.setDebugParameter("mbe81b_not_in_flight", 1);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8110", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": drop from a height and bounce");
    return true;
  }

  public boolean runTest8111(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // ballistic movement: drop from a height with sideways motion and bounce
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(200, 204, 100);
    Vec3d OBSERVER_POINT = new Vec3d(203, 200, 100);
    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    BlockPos centre = new BlockPos(200, 199, 100);
    TestRunnerMBE81.createBasin(playerIn, centre, 10, 1, Blocks.AIR.getDefaultState());
    DebugSettings.setDebugParameter("mbe81b_not_in_flight", 1);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8111", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0,  10,
            2,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": drop from a height with sideways motion and bounce");
    return true;
  }

  public boolean runTest8112(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // ballistic movement: drop from a height into water
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(200, 210, 200);
    Vec3d OBSERVER_POINT = new Vec3d(203, 200, 200);
    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    BlockPos centre = new BlockPos(200, 199, 200);
    TestRunnerMBE81.createBasin(playerIn, centre, 10, 3, Blocks.WATER.getDefaultState());
    DebugSettings.setDebugParameter("mbe81b_not_in_flight", 1);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8112", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": drop from a height into water: should bob up and down");
    return true;
  }

  public boolean runTest8113(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // ballistic movement: drop from a height into lava
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(200, 210, 300);
    Vec3d OBSERVER_POINT = new Vec3d(203, 200, 300);
    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    BlockPos centre = new BlockPos(200, 199, 300);
    TestRunnerMBE81.createBasin(playerIn, centre, 10, 3, Blocks.LAVA.getDefaultState());
    DebugSettings.setDebugParameter("mbe81b_not_in_flight", 1);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8113", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": drop from a height into lava: should bounce around on the surface");
    return true;
  }

  public boolean runTest8114(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 1000);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 1000);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1000), 10, new Vec3i(4, 0, 0), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8114", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  -90, 0,  100,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving east");
    return true;
  }

  public boolean runTest8115(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 1100);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 1100);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(-4, 200, 1100), 10, new Vec3i(-4, 0, 0), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8115", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  90, 0,  100,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving west");
    return true;
  }

  public boolean runTest8116(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 1200);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 1200);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(0, 200, 1204), 10, new Vec3i(0, 0, 4), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8116", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, 0,  100,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving south");
    return true;
  }

  public boolean runTest8117(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 1300);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 1300);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1300), 10, new Vec3i(0, 0, -4), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8117", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  180, 0,  100,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving north");
    return true;
  }

  public boolean runTest8118(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 1400);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 1400);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1000), 10, new Vec3i(4, 0, 0), Blocks.IRON_ORE.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8118", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  -90, 0,  100,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through iron blocks");
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
