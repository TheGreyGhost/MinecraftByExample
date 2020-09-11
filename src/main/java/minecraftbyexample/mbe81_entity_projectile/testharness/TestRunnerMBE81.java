package minecraftbyexample.mbe81_entity_projectile.testharness;

import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

  }


  // dummy test: check the correct functioning of the ladder - to see which block it can stay attached to
  // The test region contains a ladder attached to a stone block.  We then replace the stone with a different block and see
  //   whether the ladder remains or breaks appropriately; eg
  // testA - replace with wood
  // testB - replace with a glass block
  // testC - replace with diamond block
  private boolean test1(World worldIn, PlayerEntity playerIn)
  {
    BlockPos sourceRegionOrigin = new BlockPos(0, 204, 0);
    final int SOURCE_REGION_SIZE_X = 4;
    final int SOURCE_REGION_SIZE_Y = 2;
    final int SOURCE_REGION_SIZE_Z = 3;

    // put a stone block with attached ladder in the middle of our test region
    worldIn.setBlockState(sourceRegionOrigin.add(1, 0, 1), Blocks.STONE.getDefaultState());
    worldIn.setBlockState(sourceRegionOrigin.add(2, 0, 1),
                            Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.EAST));

    BlockPos testRegionOriginA = new BlockPos(5, 204, 0);
    BlockPos testRegionOriginB = new BlockPos(10, 204, 0);
    BlockPos testRegionOriginC = new BlockPos(15, 204, 0);

    // place a nearby block for the player to stand on, then teleport the player there, so you can watch
    worldIn.setBlockState(testRegionOriginA.south(5).down(), Blocks.STONE.getDefaultState());
    teleportPlayerToTestRegion(playerIn, testRegionOriginA.south(5));

    // copy the test block to the destination region
    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginA,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginB,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginC,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);

    boolean success = true;
    // testA: replace stone with wood; ladder should remain
    worldIn.setBlockState(testRegionOriginA.add(1, 0, 1), Blocks.ACACIA_LOG.getDefaultState());
    success &= worldIn.getBlockState(testRegionOriginA.add(2, 0, 1)).getBlock() == Blocks.LADDER;

    // testB: replace stone with glass; ladder should be destroyed
    worldIn.setBlockState(testRegionOriginB.add(1, 0, 1), Blocks.COBWEB.getDefaultState());
    success &= worldIn.getBlockState(testRegionOriginB.add(2, 0, 1)).getBlock() == Blocks.AIR;

    // testC: replace stone with diamond block; ladder should remain
    worldIn.setBlockState(testRegionOriginC.add(1, 0, 1), Blocks.STONE_SLAB.getDefaultState());
    success &= worldIn.getBlockState(testRegionOriginC.add(2, 0, 1)).getBlock() == Blocks.LADDER;

    return success;
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


  private static final Logger LOGGER = LogManager.getLogger();
}
