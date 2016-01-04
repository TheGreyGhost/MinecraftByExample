package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

/**
* Created by EveryoneElse on 3/01/2016.
*/ // copied from WorldGenCactus
public class CactusSpawner extends NewPlantSpawner {
  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    if (world.isAirBlock(blockPos)) {
      int cactusHeight = 1 + random.nextInt(random.nextInt(3) + 1);

      for (int k = 0; k < cactusHeight; ++k) {
        if (Blocks.cactus.canBlockStay(world, blockPos)) {
          world.setBlockState(blockPos.up(k), Blocks.cactus.getDefaultState(), SET_BLOCKSTATE_FLAG);
          success = true;
        }
      }
    }
    return success;
  }

  public static Collection<Block> getSpawnablePlants() {
    return ImmutableList.of((Block) Blocks.cactus);
  }

  @Override
  public boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random) {
    IBlockState iBlockState = world.getBlockState(sourcePlantPos);
    if (iBlockState.getBlock() == Blocks.cactus) {
      return trySpawnNewPlant(world, destinationPlantPos, random);
    }
    return false;
  }
}
