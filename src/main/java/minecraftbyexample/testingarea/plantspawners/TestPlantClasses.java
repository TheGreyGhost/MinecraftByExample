package minecraftbyexample.testingarea.plantspawners;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.command.CommandClone;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by TGG on 6/01/2016.
 */
public class TestPlantClasses
{

  /** tests of the Plant classes - place, copy, grow
   *
   * @param worldIn
   * @param playerIn
   * @return
   */
  public boolean test1(World worldIn, EntityPlayer playerIn)
  {
    BlockPos sourceRegionOrigin = new BlockPos(0, 204, 0);
    final int SOURCE_REGION_SIZE_X = 40;
    final int SOURCE_REGION_SIZE_Y = 6;
    final int SOURCE_REGION_SIZE_Z = 1;

    // put a stone block with attached ladder in the middle of our test region

    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      worldIn.setBlockState(sourceRegionOrigin.add(x, 0, 0), Blocks.dirt.getDefaultState());
    }

    BlockPos testRegionOriginA = new BlockPos(0, 204, 2);
    BlockPos testRegionOriginB = new BlockPos(0, 204, 4);
    BlockPos testRegionOriginC = new BlockPos(0, 204, 6);

    teleportPlayerToTestRegion(playerIn, testRegionOriginA.south(5));  // teleport the player nearby so you can watch

    // copy the test blocks to the destination region
    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginA,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginB,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginC,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);

    boolean success = true;

    int nextxpos = 0;

    final long SEED = 47;
    Random random = new Random(SEED);
    CactusPlant cactusPlant = new CactusPlant();
    cactusPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(nextxpos++, 1, 0), random);

    for (BlockFlower.EnumFlowerType flowerType : BlockFlower.EnumFlowerType.values()) {
      FlowersPlant flowersPlant = new FlowersPlant(flowerType);
      flowersPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(nextxpos++, 1, 0), random);
    }

    for (BlockDoublePlant.EnumPlantType plantType : BlockDoublePlant.EnumPlantType.values()) {
      PlantDoublePlant plantDoublePlant = new PlantDoublePlant(plantType);
      plantDoublePlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(nextxpos++, 1, 0), random);

    }

    // test getPlantFromBlockState
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      BlockPos sourceBlockPos = testRegionOriginA.add(x, 1, 0);
      IBlockState iBlockState = worldIn.getBlockState(sourceBlockPos);
      Plant sourcePlant = Plant.getPlantFromBlockState(iBlockState);
      if (sourcePlant != null) {
        sourcePlant.trySpawnNewPlant(worldIn, testRegionOriginB.add(x, 1, 0), random);
        sourcePlant.trySpawnNewPlant(worldIn, testRegionOriginC.add(x, 1, 0), random);
      }
    }

    // test grow
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      BlockPos blockPos = testRegionOriginC.add(x, 1, 0);
      IBlockState iBlockState = worldIn.getBlockState(blockPos);
      Plant plantToGrow = Plant.getPlantFromBlockState(iBlockState);
      plantToGrow.grow(worldIn, blockPos, 1.0F);
    }

    return success;
  }

  /** tests of the Plant classes - copy plants which were placed by player
   *
   * @param worldIn
   * @param playerIn
   * @return
   */
  public boolean test2(World worldIn, EntityPlayer playerIn)
  {
    BlockPos sourceRegionOrigin = new BlockPos(0, 220, 2);
    BlockPos eraseRegionOrigin = new BlockPos(0, 220, 0);
    final int SOURCE_REGION_SIZE_X = 40;
    final int SOURCE_REGION_SIZE_Y = 6;
    final int SOURCE_REGION_SIZE_Z = 1;

    BlockPos testRegionOriginA = new BlockPos(0, 220, 4);
//    BlockPos testRegionOriginB = new BlockPos(10, 220, 6);
//    BlockPos testRegionOriginC = new BlockPos(15, 220, 0);

    teleportPlayerToTestRegion(playerIn, testRegionOriginA.south(5));  // teleport the player nearby so you can watch

    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      worldIn.setBlockState(eraseRegionOrigin.add(x, 0, 0), Blocks.dirt.getDefaultState());
      worldIn.setBlockState(sourceRegionOrigin.add(x, 0, 0), Blocks.dirt.getDefaultState());
    }


    // copy the test blocks to the destination region
    copyTestRegion(playerIn, eraseRegionOrigin, testRegionOriginA,
                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
//    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginB,
//                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
//    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginC,
//                          SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);

    final long SEED = 47;
    Random random = new Random(SEED);

    boolean success = true;
    // test getPlantFromBlockState on plants placed by player
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      BlockPos sourceBlockPos = sourceRegionOrigin.add(x, 1, 0);
      IBlockState iBlockState = worldIn.getBlockState(sourceBlockPos);
      Plant sourcePlant = Plant.getPlantFromBlockState(iBlockState);
      if (sourcePlant != null) {
        sourcePlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(x, 1, 0), random);
      }
    }

    return success;
  }

  /**
   * Teleport the player to the test region (so you can see the results of the test)
   * @param playerIn
   * @param location
   * @return
   */
  private boolean teleportPlayerToTestRegion(EntityPlayer playerIn, BlockPos location)
  {
    String tpArguments = "@p " + location.getX() + " " + location.getY() + " " + location.getZ();
    String[] tpArgumentsArray = tpArguments.split(" ");

    CommandTeleport commandTeleport = new CommandTeleport();
    try {
      commandTeleport.execute(playerIn, tpArgumentsArray);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Copy a cuboid Test Region from one part of the world to another
   * The cuboid is x blocks wide, by y blocks high, by z blocks long
   * @param entityPlayer
   * @param sourceOrigin origin of the source region
   * @param destOrigin origin of the destination region
   * @param xCount >=1
   * @param yCount >=1
   * @param zCount >=1
   * @return true for success, false otherwise
   */
  private boolean copyTestRegion(EntityPlayer entityPlayer,
                                 BlockPos sourceOrigin, BlockPos destOrigin,
                                 int xCount, int yCount, int zCount)
  {
    checkArgument(xCount >= 1);
    checkArgument(yCount >= 1);
    checkArgument(zCount >= 1);
    String [] args = new String[9];

    args[0] = String.valueOf(sourceOrigin.getX());
    args[1] = String.valueOf(sourceOrigin.getY());
    args[2] = String.valueOf(sourceOrigin.getZ());
    args[3] = String.valueOf(sourceOrigin.getX() + xCount - 1);
    args[4] = String.valueOf(sourceOrigin.getY() + yCount - 1);
    args[5] = String.valueOf(sourceOrigin.getZ() + zCount - 1);
    args[6] = String.valueOf(destOrigin.getX());
    args[7] = String.valueOf(destOrigin.getY());
    args[8] = String.valueOf(destOrigin.getZ());

    CommandClone commandClone = new CommandClone();
    try {
      commandClone.execute(entityPlayer, args);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
