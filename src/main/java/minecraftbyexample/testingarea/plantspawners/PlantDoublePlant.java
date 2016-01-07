package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenDoublePlant
public class PlantDoublePlant extends Plant {
  public PlantDoublePlant(BlockDoublePlant.EnumPlantType enumPlantType) {
    plantType = enumPlantType;
  }

  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    if (world.isAirBlock(blockPos) && Blocks.double_plant.canPlaceBlockAt(world, blockPos)) {
      Blocks.double_plant.placeAt(world, blockPos, plantType, SET_BLOCKSTATE_FLAG);
      success = true;
    }
    return success;
  }

  @Override
  public void grow(World world, BlockPos blockPos, float growthAmount)
  {
    // do nothing....
  }

  public static class DoublePlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      BlockDoublePlant.EnumPlantType enumPlantType = (BlockDoublePlant.EnumPlantType)iBlockState.getValue(BlockDoublePlant.VARIANT);
      return new PlantDoublePlant(enumPlantType);
    }

    @Override
    public Collection<Block> getBlocksUsedByThisPlant()
    {
      return ImmutableList.of((Block)Blocks.double_plant);
    }
  }

  private BlockDoublePlant.EnumPlantType plantType;
}
