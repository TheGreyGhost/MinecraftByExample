package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenCactus
public class CactusPlant extends Plant {
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

  @Override
  public void grow(World world, BlockPos blockPos, float growthAmount)
  {
    checkArgument(growthAmount >= 0);
    IBlockState iBlockState = world.getBlockState(blockPos);
    if (iBlockState.getBlock() != Blocks.cactus) return;

    final int MAX_CACTUS_AGE = 15;
    int currentAge = ((Integer)iBlockState.getValue(BlockCactus.AGE)).intValue();
    currentAge += MAX_CACTUS_AGE * growthAmount;
    currentAge = Math.min(currentAge, MAX_CACTUS_AGE);
    BlockPos topBlock = findTopCactusBlock(world, blockPos);

    world.setBlockState(topBlock, iBlockState.withProperty(BlockCactus.AGE, Integer.valueOf(currentAge)), SET_BLOCKSTATE_FLAG);
  }

  /**
   * finds the topmost block of the cactus.  Assumes that the starting position is a cactus!!
   * @param world
   * @param startBlockPos
   * @return
   */
  private BlockPos findTopCactusBlock(World world, BlockPos startBlockPos)
  {
    BlockPos nextPos = startBlockPos;
    do {
      nextPos = nextPos.up();
    } while (world.getBlockState(nextPos).getBlock() == Blocks.cactus);
    return nextPos.down();
  }

  public static class CactusPlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      if (iBlockState == null || iBlockState.getBlock() != Blocks.cactus) return null;
      return new CactusPlant();
    }

    public Collection<Block> getBlocksUsedByThisPlant() {
      return ImmutableList.of((Block) Blocks.cactus);
    }
  }

}
