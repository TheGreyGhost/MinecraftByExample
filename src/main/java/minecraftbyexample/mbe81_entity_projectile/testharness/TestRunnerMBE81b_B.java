package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangEntity;
import minecraftbyexample.mbe81_entity_projectile.BoomerangFlightPath;
import minecraftbyexample.mbe81_entity_projectile.StartupCommon;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGG on 31/08/2020.
 *
 * tests of flight path:
 * 2 = stationary to check rendering and angles
 * 3 - 6 = different path shapes/directions
 * 7 = underwater i.e. in water adds trails

 */
public class TestRunnerMBE81b_B {

  public boolean runTest(int test, World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    if (test == 8102) return runTest8102(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8103) return runTest8103(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8104) return runTest8104(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8105) return runTest8105(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8106) return runTest8106(worldIn, playerIn, printFailedTestsOnly);
    if (test == 8107) return runTest8107(worldIn, playerIn, printFailedTestsOnly);
    return false;
  }

  public boolean runTest8102(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a stationary boomerang so we can see it rendering
    //  teleport the player to the observation point
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(0, 200, 0);

    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    BlockPos CENTRE_POS = new BlockPos(0, 199, 0);
    TestRunnerMBE81.generateChessboard(playerIn, CENTRE_POS, 12);

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
    Vec3d START_POINT = new Vec3d(0, 201, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    BlockPos CENTRE_POS = new BlockPos(0, 199, 0);
    TestRunnerMBE81.generateChessboard(playerIn, CENTRE_POS, 12);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8103", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString() + ": watch flight path: flat, no yaw");
    return true;
  }

  public boolean runTest8104(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  60 degree yaw
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 201, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    BlockPos CENTRE_POS = new BlockPos(0, 199, 0);
    TestRunnerMBE81.generateChessboard(playerIn, CENTRE_POS, 12);
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8104", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  60, 0,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString()+ ": watch flight path: flat, 60 degree yaw");
    return true;
  }

  public boolean runTest8105(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  pitched path
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 201, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    BlockPos CENTRE_POS = new BlockPos(0, 199, 0);
    TestRunnerMBE81.generateChessboard(playerIn, CENTRE_POS, 12);
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8105", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, -30,  10,
            2,  false, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString()+ ": watch flight path: pitch 30 degree up, no yaw");
    return true;
  }

  public boolean runTest8106(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.  right hand throw
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(0, 201, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    BlockPos CENTRE_POS = new BlockPos(0, 199, 0);
    TestRunnerMBE81.generateChessboard(playerIn, CENTRE_POS, 12);
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8106", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,  0, 0,  10,
            2,  true, 1);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString()+ ": watch flight path: flat, no yaw, right hand throw");
    return true;
  }

  public boolean runTest8107(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // spawn a boomerang so we can watch its flight path.
    //  teleport the player to the observation point if far away
    TestRunnerMBE81.clearAllDebugSettings();
    Vec3d START_POINT = new Vec3d(100, 201, 0);
    Vec3d OBSERVER_POINT = new Vec3d(103, 200, 0);
    BlockPos centre = new BlockPos(100, 199, 0);
    TestRunnerMBE81.createBasin(playerIn, centre, 10, 1, Blocks.WATER.getDefaultState());

    if (!playerIn.getPosition().withinDistance(OBSERVER_POINT, 30)) {
      TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);
    }
    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity8107", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT, 90, 0, 10,
            2,  false, 4);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity " + sb.toString()+ ": watch flight path: flat, 90 yaw, 4 blocks per second");
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

  private void junk() {
//    if ( (this.renderManager.shouldRender(entity, clippinghelperimpl, d0, d1, d2)
//            || entity.isRidingOrBeingRiddenBy(this.mc.player) )
//        && (entity != activeRenderInfoIn.getRenderViewEntity()
//            || activeRenderInfoIn.isThirdPerson()
//            || activeRenderInfoIn.getRenderViewEntity() instanceof LivingEntity
//            && ((LivingEntity)activeRenderInfoIn.getRenderViewEntity()).isSleeping())
//        && (!(entity instanceof ClientPlayerEntity) || activeRenderInfoIn.getRenderViewEntity() == entity)
//        ) {

    }

  private static final Logger LOGGER = LogManager.getLogger();
}
