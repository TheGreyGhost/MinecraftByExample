package minecraftbyexample.testingarea.plantspawners;

import net.minecraft.block.*;
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
   * first row = source blocks only
   * second row = plant onto the dirt (check plants are correct)
   * third row = copy of the second row (check they match)
   * fourth row = copy of the second row, grow.
   *
   * @param worldIn
   * @param playerIn
   * @return
   */
  public boolean test1(World worldIn, EntityPlayer playerIn)
  {
    BlockPos sourceRegionOrigin = new BlockPos(0, 180, 0);
    final int SOURCE_REGION_SIZE_X = 40;
    final int SOURCE_REGION_SIZE_Y = 20;
    final int SOURCE_REGION_SIZE_Z = 10;
    final int FARMLAND_X_START = 30;

    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      worldIn.setBlockState(sourceRegionOrigin.add(x, 0, 0), Blocks.dirt.getDefaultState());
    }
    for (int x = FARMLAND_X_START; x < SOURCE_REGION_SIZE_X; ++x) {
      worldIn.setBlockState(sourceRegionOrigin.add(x, 0, 0), Blocks.farmland.getDefaultState());
    }

    BlockPos testRegionOriginA = new BlockPos(0, 180, 10);
    BlockPos testRegionOriginB = new BlockPos(0, 180, 20);
    BlockPos testRegionOriginC = new BlockPos(0, 180, 30);

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

    for (BlockPlanks.EnumType saplingType : BlockPlanks.EnumType.values()) {
      SaplingPlant plantSaplingPlant = new SaplingPlant(saplingType);
      plantSaplingPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(nextxpos++,1,0), random);
    }

    for (BlockTallGrass.EnumType grassType : BlockTallGrass.EnumType.values()) {
      TallGrassPlant grassPlant = new TallGrassPlant(grassType);
      grassPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(nextxpos++,1,0), random);
    }
    worldIn.setBlockState(testRegionOriginA.add(nextxpos,1,0), Blocks.stone.getDefaultState());

    nextxpos = FARMLAND_X_START;

    for (CropsPlant.CropType cropType : CropsPlant.CropType.values()) {
      CropsPlant cropsPlant = new CropsPlant(cropType);
      cropsPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(nextxpos++, 1, 0), random);
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
      if (plantToGrow != null) {
        plantToGrow.grow(worldIn, blockPos, 101F);
      } else {
        worldIn.setBlockState(blockPos, Blocks.obsidian.getDefaultState());
      }
    }

    return success;
  }

  /** tests of the Plant classes - copy plants which were placed by player
   * first row = placed by player
   * second row = matches player
   * @param worldIn
   * @param playerIn
   * @return
   */
  public boolean test2(World worldIn, EntityPlayer playerIn)
  {
    BlockPos sourceRegionOrigin = new BlockPos(0, 220, 10);
    BlockPos eraseRegionOrigin = new BlockPos(0, 220, 0);
    final int SOURCE_REGION_SIZE_X = 40;
    final int SOURCE_REGION_SIZE_Y = 20;
    final int SOURCE_REGION_SIZE_Z = 5;

    BlockPos testRegionOriginA = new BlockPos(0, 220, 20);
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

  /** tests of the non-dirt Plant classes - place, copy, grow
   *  lilies appear on water
   *  reeds appear next to water
   *  vines appear on stone around a single pillar
   *  vines fill all available surfaces in a nice between pillars
   *
   * @param worldIn
   * @param playerIn
   * @return
   */
  public boolean test3(World worldIn, EntityPlayer playerIn)
  {
    BlockPos sourceRegionOrigin = new BlockPos(0, 160, 0);
    final int SOURCE_REGION_SIZE_X = 10;
    final int SOURCE_REGION_SIZE_Y = 20;
    final int SOURCE_REGION_SIZE_Z = 20;

    // make shorelines
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      for (int z = 0; z < SOURCE_REGION_SIZE_Z; ++z) {
        worldIn.setBlockState(sourceRegionOrigin.add(x, 0, z), Blocks.dirt.getDefaultState());
        IBlockState iBlockState = (0 == ((x ^ z) & 0x07)) ? Blocks.water.getDefaultState() : Blocks.sand.getDefaultState();
        worldIn.setBlockState(sourceRegionOrigin.add(x, 1, z), iBlockState);
      }
    }

    final int STONE_BLOCK_X = 3;
    final int STONE_BLOCK_Y = 2;
    final int STONE_BLOCK_Z = 17;

    final int STONE_BLOCK2_X = 8;
    final int STONE_BLOCK2_Y = 2;
    final int STONE_BLOCK2_Z = 17;

    worldIn.setBlockState(sourceRegionOrigin.add(STONE_BLOCK_X, STONE_BLOCK_Y, STONE_BLOCK_Z),
                          Blocks.stone.getDefaultState());

    worldIn.setBlockState(sourceRegionOrigin.add(STONE_BLOCK2_X-1, STONE_BLOCK2_Y, STONE_BLOCK2_Z),
                          Blocks.stone.getDefaultState());
    worldIn.setBlockState(sourceRegionOrigin.add(STONE_BLOCK2_X+1, STONE_BLOCK2_Y, STONE_BLOCK2_Z),
                          Blocks.stone.getDefaultState());
    worldIn.setBlockState(sourceRegionOrigin.add(STONE_BLOCK2_X, STONE_BLOCK2_Y, STONE_BLOCK2_Z-1),
                          Blocks.stone.getDefaultState());
    worldIn.setBlockState(sourceRegionOrigin.add(STONE_BLOCK2_X, STONE_BLOCK2_Y, STONE_BLOCK2_Z+1),
                          Blocks.stone.getDefaultState());

    BlockPos testRegionOriginA = new BlockPos(12, 160, 0);
    BlockPos testRegionOriginB = new BlockPos(24, 160, 0);
    BlockPos testRegionOriginC = new BlockPos(36, 160, 0);

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
    VinesPlant vinesPlant = new VinesPlant();

    for (int x = -2; x <= +2; ++x) {
      for (int z = -2; z <= +2; ++z) {
        vinesPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(STONE_BLOCK_X + x,
                                                                   STONE_BLOCK_Y,
                                                                   STONE_BLOCK_Z + z), random);
        vinesPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(STONE_BLOCK2_X + x,
                                                                   STONE_BLOCK2_Y,
                                                                   STONE_BLOCK2_Z + z), random);
      }
    }



    final int LILY_WIDTH_Z = 6;
    final int REEDS_WIDTH_Z = 6;

    WaterLilyPlant waterLilyPlant = new WaterLilyPlant();

    // spawn lilies on water
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      for (int z = 0; z < LILY_WIDTH_Z; ++z) {
        waterLilyPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(x, 2, z), random);
      }
    }

    ReedsPlant reedsPlant = new ReedsPlant();
    // spawn reeds on shorelines
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      for (int z = 0; z < REEDS_WIDTH_Z; ++z) {
        reedsPlant.trySpawnNewPlant(worldIn, testRegionOriginA.add(x, 2, z + LILY_WIDTH_Z), random);
      }
    }

    // test getPlantFromBlockState
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      BlockPos sourceBlockPos = testRegionOriginA.add(x, 2, 0);
      IBlockState iBlockState = worldIn.getBlockState(sourceBlockPos);
      Plant sourcePlant = Plant.getPlantFromBlockState(iBlockState);
      if (sourcePlant != null) {
        sourcePlant.trySpawnNewPlant(worldIn, testRegionOriginB.add(x, 2, 0), random);
        sourcePlant.trySpawnNewPlant(worldIn, testRegionOriginC.add(x, 2, 0), random);
      }
    }

    // test grow
    for (int x = 0; x < SOURCE_REGION_SIZE_X; ++x) {
      BlockPos blockPos = testRegionOriginC.add(x, 2, 0);
      IBlockState iBlockState = worldIn.getBlockState(blockPos);
      Plant plantToGrow = Plant.getPlantFromBlockState(iBlockState);
      plantToGrow.grow(worldIn, blockPos, 1.0F);
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
