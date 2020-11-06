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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
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
 * inflight: test enchantments - efficiency, silk touch
 */
public class TestRunnerMBE81b_C {

  public boolean runTest(int test, World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    if (test == 8110) return runTest8110(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8111) return runTest8111(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8112) return runTest8112(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8113) return runTest8113(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8114) return runTest8114(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8115) return runTest8115(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8116) return runTest8116(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8117) return runTest8117(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8118) return runTest8118(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8119) return runTest8119(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8120) return runTest8120(worldIn, playerIn, printFailedTestsOnly);
    return false;
  }

  public boolean runTest8110(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // ballistic movement: drop from a height and bounce
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(200, 210, 0);
    Vector3d OBSERVER_POINT = new Vector3d(203, 200, 0);
    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {  //.getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

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
    Vector3d START_POINT = new Vector3d(200, 204, 100);
    Vector3d OBSERVER_POINT = new Vector3d(203, 200, 100);
    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {  //.getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

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
    Vector3d START_POINT = new Vector3d(200, 202, 200);
    Vector3d OBSERVER_POINT = new Vector3d(203, 200, 200);
    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) { //.getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

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
    Vector3d START_POINT = new Vector3d(200, 204, 300);
    Vector3d OBSERVER_POINT = new Vector3d(203, 204, 300);
    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {         //.getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }

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
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1000.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1000);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {     //.getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1000), 10, new Vector3i(4, 0, 0), Blocks.ACACIA_LEAVES.getDefaultState());


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
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1100.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1100);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {  //.getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(-4, 200, 1100), 10, new Vector3i(-4, 0, 0), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8115", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  90, 0,  100,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving west");
    return true;
  }

  public boolean runTest8116(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1200.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1200);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {  //getPosition
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(0, 200, 1204), 10, new Vector3i(0, 0, 4), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8116", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, 0,  100,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving south");
    return true;
  }

  public boolean runTest8117(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1300.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1300);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(0, 200, 1296), 10, new Vector3i(0, 0, -4), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8117", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  180, 0,  100,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through blocks moving north");
    return true;
  }

  public boolean runTest8118(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1400.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1400);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1400), 10, new Vector3i(4, 0, 0), Blocks.IRON_ORE.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8118", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  -90, 0,  100,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through iron blocks");
    return true;
  }

  public boolean runTest8119(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1500.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1500);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.EFFICIENCY, 5);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1500), 10, new Vector3i(4, 0, 0), Blocks.ACACIA_WOOD.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8118", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  -90, 0,  100,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through wood blocks with greater efficiency");
    return true;
  }

  public boolean runTest8120(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang and watch it smash through a line of blocks until it loses all momentum and bounces off
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vector3d START_POINT = new Vector3d(0.5, 200.5, 1600.5);
    Vector3d OBSERVER_POINT = new Vector3d(3, 200, 1600);

    if (!playerIn.func_233580_cy_().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);
    thrownBoomerang.addEnchantment(Enchantments.SILK_TOUCH, 1);
    TestRunnerMBE81.createLine(playerIn, new BlockPos(4, 200, 1600), 10, new Vector3i(4, 0, 0), Blocks.GLASS.getDefaultState());

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8118", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  -90, 0,  100,
            0,  false, 10);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": break through glass blocks with silk touch");
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
