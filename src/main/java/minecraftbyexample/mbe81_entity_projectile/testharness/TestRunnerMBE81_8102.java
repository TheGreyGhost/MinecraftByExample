package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.mbe81_entity_projectile.BoomerangEntity;
import minecraftbyexample.mbe81_entity_projectile.BoomerangFlightPath;
import minecraftbyexample.mbe81_entity_projectile.StartupCommon;
import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TGG on 31/08/2020.
 */
public class TestRunnerMBE81_8102 {
  public boolean runTest8102(World worldIn, PlayerEntity playerIn, boolean printFailedTestsOnly) {
    // test a: spawn a stationary boomerang so we can see it rendering
    //  teleport the player to the observation point

    Vec3d START_POINT = new Vec3d(0, 200, 0);
    Vec3d OBSERVER_POINT = new Vec3d(3, 200, 0);
    TestRunnerTools.teleportPlayerToTestRegion(playerIn, new BlockPos(OBSERVER_POINT), true);

    ItemStack thrownBoomerang = new ItemStack(StartupCommon.boomerangItem);

    DebugSettings.setDebugParameter("mbe81_notick", 1);
    StringBuilder sb = new StringBuilder();
    BoomerangEntity boomerangEntity = generateEntity("Entity1", sb, worldIn, playerIn, thrownBoomerang,
            START_POINT,90,   0,  10,
            2,  false, 4);
    worldIn.addEntity(boomerangEntity);
    LOGGER.error("Spawned Entity with mbe81_notick set");
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
