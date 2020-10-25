package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Test Runner for MBE81
 */
public class TestRunnerMBE81
{
  public boolean runServerSideTest(World worldIn, PlayerEntity playerIn, int testNumber)
  {
    boolean success = false;
    switch (testNumber) {
      case 8101: {
        TestRunnerMBE81b_A testRunner = new TestRunnerMBE81b_A();
        success = testRunner.runTest(worldIn, playerIn, false);
        break;
      }
      case 8102: case 8103: case 8104: case 8105: case 8106: case 8107:{
        TestRunnerMBE81b_B testRunner = new TestRunnerMBE81b_B();
        success = testRunner.runTest(testNumber, worldIn, playerIn, false);
        break;
      }
      case 8110: case 8111: case 8112: case 8113: case 8114: case 8115: case 8116: case 8117: case 8118: case 8119: case 8120: {
        TestRunnerMBE81b_C testRunner = new TestRunnerMBE81b_C();
        success = testRunner.runTest(testNumber, worldIn, playerIn, false);
        break;
      }
      case 8121: case 8122: case 8123: case 8124: case 8125: case 8126: case 8127: case 8128: case 8129: case 8130: {
        TestRunnerMBE81b_D testRunner = new TestRunnerMBE81b_D();
        success = testRunner.runTest(testNumber, worldIn, playerIn, false);
        break;
      }
      default: {  // not intended for this TestRunner
        return false;
      }
    }

    LOGGER.error("Test Number " + testNumber + " called on server side:" + (success ? "success" : "failure"));
    return success;
  }

  /**
   * Prints success or failure of test
   * @param resultChainIn
   * @param failmessage
   * @param result
   * @return resultChainIn && result
   */
  public static boolean test(boolean resultChainIn, String failmessage, boolean result) {
    LOGGER.error("Test #" + failmessage + (result ? " passed." : " failed."));
    return resultChainIn && result;
  }

  /** Clear all DebugSettings used by the test runners
   */
  public static void clearAllDebugSettings() {
    DebugSettings.clearDebugParameter("mbe81b_notick");
    DebugSettings.clearDebugParameter("mbe81b_not_in_flight");
  }

  /**
   * Teleport the player to the test region (so you can see the results of the test)
   * @param player
   * @param location
   * @return
   */
  public boolean teleportPlayerToTestRegion(PlayerEntity player, BlockPos location)
  {
    if (!(player instanceof ServerPlayerEntity)) {
      throw new UnsupportedOperationException("teleport not supported on client side; server side only");
    }

    CommandSource commandSource = player.getCommandSource();
    String tpCommand = "/tp " + location.getX() + " " + location.getY() + " " + location.getZ();
    int success = 0;
    try {
      success = player.getServer().getCommandManager().handleCommand(commandSource, tpCommand);
    } catch (Exception e) {
      return false;
    }
    return (success != 0);
  }

  /**
   * Copy a cuboid Test Region from one part of the world to another
   * The cuboid is x block wide, by y block high, by z block long
   * @param player
   * @param sourceOrigin origin of the source region
   * @param destOrigin origin of the destination region
   * @param xCount >=1
   * @param yCount >=1
   * @param zCount >=1
   * @return true for success, false otherwise
   */
  public boolean copyTestRegion(PlayerEntity player,
                                 BlockPos sourceOrigin, BlockPos destOrigin,
                                 int xCount, int yCount, int zCount)
  {
    checkArgument(xCount >= 1);
    checkArgument(yCount >= 1);
    checkArgument(zCount >= 1);
    String [] args = new String[9];

    if (!(player instanceof ServerPlayerEntity)) {
      throw new UnsupportedOperationException("teleport not supported on client side; server side only");
    }

    args[0] = String.valueOf(sourceOrigin.getX());
    args[1] = String.valueOf(sourceOrigin.getY());
    args[2] = String.valueOf(sourceOrigin.getZ());

    args[3] = String.valueOf(sourceOrigin.getX() + xCount - 1);
    args[4] = String.valueOf(sourceOrigin.getY() + yCount - 1);
    args[5] = String.valueOf(sourceOrigin.getZ() + zCount - 1);

    args[6] = String.valueOf(destOrigin.getX());
    args[7] = String.valueOf(destOrigin.getY());
    args[8] = String.valueOf(destOrigin.getZ());

    String command = "/clone " + String.join(" ", args);

    int success = 0;
    try {
      CommandSource commandSource = player.getCommandSource();
      success = player.getServer().getCommandManager().handleCommand(commandSource, command);
    } catch (Exception e) {
      return false;
    }
    return (success != 0);
  }

  /*
  Create a square basin (eg of water or lava)
   */
  public static void createBasin(PlayerEntity player, BlockPos centre, int radius, int depth, BlockState fillBlock) {
    World world = player.getEntityWorld();
    for (int y = 0; y <= depth; ++y) {
      for (int x = -radius; x <= radius; ++x) {
        for (int z = -radius; z <= radius; ++z) {
          boolean atedge = y == 0 || x == -radius || x == radius || z == -radius || z == radius;
          world.setBlockState(new BlockPos(x + centre.getX(), y + centre.getY(), z + centre.getZ()),
                  atedge ? Blocks.STONE.getDefaultState() : fillBlock);
        }
      }
    }
  }

  /*
    Spawn a pig on top of a block, if no nearby pig already
 */
  public static void createPig(PlayerEntity player, Vector3d spawnpoint) {
    World world = player.getEntityWorld();
    AxisAlignedBB aabb = new AxisAlignedBB(spawnpoint.subtract(1, 1, 1), spawnpoint.add(1,1,1));
    List<Entity> nearbyPigs = world.getEntitiesWithinAABB(PigEntity.class, aabb);
    if (!nearbyPigs.isEmpty()) return;

    world.setBlockState(new BlockPos(spawnpoint.x, spawnpoint.y-1, spawnpoint.z),
                        Blocks.STONE.getDefaultState());

    PigEntity pigEntity = new PigEntity(EntityType.PIG, world);
    pigEntity.setPosition(spawnpoint.x, spawnpoint.y, spawnpoint.z);

    // spawn the entity in the world
    world.addEntity(pigEntity);
  }

  /*
  Spawn a husk on top of a block, if there isn't one nearby already
*/
  public static void createHusk(PlayerEntity player, Vector3d spawnpoint) {
    World world = player.getEntityWorld();
    AxisAlignedBB aabb = new AxisAlignedBB(spawnpoint.subtract(1, 1, 1), spawnpoint.add(1,1,1));
    List<Entity> nearbyHusks = world.getEntitiesWithinAABB(HuskEntity.class, aabb);
    if (!nearbyHusks.isEmpty()) return;

    world.setBlockState(new BlockPos(spawnpoint.x, spawnpoint.y-1, spawnpoint.z),
            Blocks.STONE.getDefaultState());

    HuskEntity huskEntity = new HuskEntity(EntityType.HUSK, world);
    huskEntity.setPosition(spawnpoint.x, spawnpoint.y, spawnpoint.z);

    // spawn the entity in the world
    world.addEntity(huskEntity);
  }

  /*
  Spawn an invulnerable armor stand on top of a block, if there isn't one nearby already
*/
  public static void createArmorStand(PlayerEntity player, Vector3d spawnpoint) {
    World world = player.getEntityWorld();
    AxisAlignedBB aabb = new AxisAlignedBB(spawnpoint.subtract(1, 1, 1), spawnpoint.add(1,1,1));
    List<Entity> nearbyArmorStands = world.getEntitiesWithinAABB(ArmorStandEntity.class, aabb);
    if (!nearbyArmorStands.isEmpty()) return;

    world.setBlockState(new BlockPos(spawnpoint.x, spawnpoint.y-1, spawnpoint.z),
            Blocks.STONE.getDefaultState());

    ArmorStandEntity armorStandEntity = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
    armorStandEntity.setPosition(spawnpoint.x, spawnpoint.y, spawnpoint.z);
    armorStandEntity.setInvulnerable(true);
    // spawn the entity in the world
    world.addEntity(armorStandEntity);
  }

  /*
  Spawn an invulnerable armor stand on top of a block, if there isn't one nearby already
*/
  public static void createMinecart(PlayerEntity player, Vector3d spawnpoint) {
    World world = player.getEntityWorld();
    AxisAlignedBB aabb = new AxisAlignedBB(spawnpoint.subtract(1, 1, 1), spawnpoint.add(1,1,1));
    List<Entity> nearbyArmorStands = world.getEntitiesWithinAABB(ArmorStandEntity.class, aabb);
    if (!nearbyArmorStands.isEmpty()) return;

    world.setBlockState(new BlockPos(spawnpoint.x, spawnpoint.y-1, spawnpoint.z),
            Blocks.STONE.getDefaultState());

    MinecartEntity minecartEntity = new MinecartEntity(EntityType.MINECART, world);
    minecartEntity.setPosition(spawnpoint.x, spawnpoint.y, spawnpoint.z);
    minecartEntity.setInvulnerable(true);
    // spawn the entity in the world
    world.addEntity(minecartEntity);
  }


  /*  create a line of regularly-spaced blocks
 */
  public static void createLine(PlayerEntity player, BlockPos start, int count, Vec3i delta, BlockState blockState) {
    World world = player.getEntityWorld();
    BlockPos currentPos = start;
    for (int i = 0; i < count; ++i) {
       world.setBlockState(currentPos, blockState);
      currentPos = currentPos.add(delta);
    }
  }

  /* create a chessboard of colours
   */
  public static void generateChessboard(PlayerEntity player, BlockPos centre, int radius) {
    Block[] blockStates = {Blocks.GREEN_WOOL, Blocks.BLACK_WOOL, Blocks.BLUE_WOOL, Blocks.WHITE_WOOL};
    World world = player.getEntityWorld();
    for (int x = -radius; x <= radius; ++x) {
      for (int z = -radius; z <= radius; ++z) {
        BlockState blockColour =  blockStates[Math.floorMod(x+z, 4)].getDefaultState();
        world.setBlockState(new BlockPos(x + centre.getX(), centre.getY(), z + centre.getZ()), blockColour);
      }
    }
  }


  private static final Logger LOGGER = LogManager.getLogger();
}
