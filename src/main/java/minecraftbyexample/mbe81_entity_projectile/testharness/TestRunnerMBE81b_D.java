package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangEntity;
import minecraftbyexample.mbe81_entity_projectile.StartupCommon;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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
    if (test == 8122) return runTest8122(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8123) return runTest8123(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8124) return runTest8124(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8125) return runTest8125(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8126) return runTest8126(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8127) return runTest8127(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8128) return runTest8128(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8129) return runTest8129(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8130) return runTest8130(worldIn, playerIn, printFailedTestsOnly);
    return false;
  }

  public boolean runTest8121(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2000.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2003);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2000);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8121", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 270, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at slow speed");
    return true;
  }

  public boolean runTest8122(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2100.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2103);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2100);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8122", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at high speed");
    return true;
  }

  public boolean runTest8123(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2200.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2203);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2200);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.POWER, Enchantments.POWER.getMaxLevel());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8123", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at low speed with damage boost");
    return true;
  }

  public boolean runTest8124(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2300.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2303);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2300);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.KNOCKBACK, Enchantments.KNOCKBACK.getMaxLevel());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8124", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at low speed with knockback1");
    return true;
  }

  public boolean runTest8125(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2400.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2403);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2400);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.PUNCH, Enchantments.PUNCH.getMaxLevel());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8125", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at low speed with knockback2");
    return true;
  }

  public boolean runTest8126(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2500.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2503);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2500);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.FLAME, Enchantments.FLAME.getMaxLevel());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8126", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at low speed with flame1");
    return true;
  }

  public boolean runTest8127(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2600.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2603);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2600);
    TestRunnerMBE81.createPig(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.FIRE_ASPECT, Enchantments.FIRE_ASPECT.getMaxLevel());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8126", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with a pig at low speed with flame2");
    return true;
  }

  public boolean runTest8128(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2700.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2703);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5, 200, 2700);
    TestRunnerMBE81.createHusk(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.SMITE, Enchantments.SMITE.getMaxLevel());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8128", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with an undead at low speed with SMITE");
    return true;
  }

  public boolean runTest8129(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2800.2);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2803);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5.5, 200, 2800.5);
    TestRunnerMBE81.createArmorStand(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8129", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with an invulnerable entity with no radius");
    return true;
  }

  public boolean runTest8130(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // collide with an entity
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0, 200.5, 2900.6);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 2903);
    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

    Vector3d spawnpoint = new Vector3d(5.5, 200, 2900.5);
    TestRunnerMBE81.createMinecart(playerIn, spawnpoint);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8130", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, -90, 0,  10,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": collide with an invulnerable entity with a radius, slightly off centre");
    return true;
  }

  public static BoomerangEntity generateEntity(String name,
                             StringBuilder sb,
                             World world, LivingEntity livingEntity, ItemStack boomerangItemStack,
                             Vector3d startPoint,
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
