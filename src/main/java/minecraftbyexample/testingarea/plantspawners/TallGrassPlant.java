package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenTallGrass
public class TallGrassPlant extends Plant {
  public TallGrassPlant(BlockTallGrass.EnumType enumPlantType) {
    grassToPlace = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, enumPlantType);
  }

  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    if (world.isAirBlock(blockPos) && Blocks.tallgrass.canBlockStay(world, blockPos, grassToPlace)) {
      world.setBlockState(blockPos, grassToPlace, 2);
      success = true;
    }
    return success;
  }

  @Override
  public void grow(World world, BlockPos blockPos, float growthAmount)
  {
    if (growthAmount > 100) {
      Random random = new Random();
      Blocks.tallgrass.grow(world, random, blockPos, grassToPlace);
    }
  }

  public static class TallGrassPlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      if (iBlockState == null || iBlockState.getBlock() != Blocks.tallgrass) return null;

      return new TallGrassPlant(iBlockState);
    }

    public Collection<Block> getBlocksUsedByThisPlant() {
      return ImmutableList.of((Block)Blocks.tallgrass);
    }
  }

  private TallGrassPlant(IBlockState i_iBlockState) {
    grassToPlace = i_iBlockState;
  }

  private IBlockState grassToPlace;
}
