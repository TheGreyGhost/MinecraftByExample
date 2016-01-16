package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenReed
public class ReedsPlant extends Plant {
  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    BlockPos groundPos = blockPos.down();

    if (world.getBlockState(groundPos.west()).getBlock().getMaterial() == Material.water
            || world.getBlockState(groundPos.east()).getBlock().getMaterial() == Material.water
            || world.getBlockState(groundPos.north()).getBlock().getMaterial() == Material.water
            || world.getBlockState(groundPos.south()).getBlock().getMaterial() == Material.water) {
      int reedHeight = 2 + random.nextInt(random.nextInt(3) + 1);

      for (int k = 0; k < reedHeight; ++k) {
        if (Blocks.reeds.canBlockStay(world, blockPos)) {
          world.setBlockState(blockPos.up(k), Blocks.reeds.getDefaultState(), SET_BLOCKSTATE_FLAG);
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
    if (iBlockState.getBlock() != Blocks.reeds) return;

    final int MAX_REED_AGE = 15;
    int currentAge = ((Integer)iBlockState.getValue(BlockReed.AGE)).intValue();
    currentAge += MAX_REED_AGE * growthAmount;
    currentAge = Math.min(currentAge, MAX_REED_AGE);
    BlockPos topBlock = findTopReedBlock(world, blockPos);

    world.setBlockState(topBlock, iBlockState.withProperty(BlockReed.AGE, Integer.valueOf(currentAge)), SET_BLOCKSTATE_FLAG);

  }

  /**
   * finds the topmost block of the reeds.  Assumes that the starting position is a reeds block!!
   * @param world
   * @param startBlockPos
   * @return
   */
  private BlockPos findTopReedBlock(World world, BlockPos startBlockPos)
  {
    BlockPos nextPos = startBlockPos;
    do {
      nextPos = nextPos.up();
    } while (world.getBlockState(nextPos).getBlock() == Blocks.reeds);
    return nextPos.down();
  }


  public static class ReedsPlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      if (iBlockState == null || iBlockState.getBlock() != Blocks.reeds) return null;

      return new ReedsPlant();
    }

    public Collection<Block> getBlocksUsedByThisPlant() {
      return ImmutableList.of((Block)Blocks.reeds);
    }
  }


}
