package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenSapling
public class CropsPlant extends Plant {

  public CropsPlant(CropType cropType) {
    switch (cropType) {
      case POTATO: {
        cropBlockState = Blocks.potatoes.getDefaultState();
        break;
      }
      case CARROT: {
        cropBlockState = Blocks.carrots.getDefaultState();
        break;
      }
      case WHEAT: {
        cropBlockState = Blocks.wheat.getDefaultState();
        break;
      }
      default: {
        throw new IllegalArgumentException("unknown crop type:" + cropType);
      }
    }
  }

  public enum CropType {POTATO, CARROT, WHEAT};

  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    if (world.isAirBlock(blockPos)) {
      BlockCrops blockCrop = (BlockCrops)cropBlockState.getBlock();
      if (blockCrop.canBlockStay(world, blockPos, cropBlockState)) {
          world.setBlockState(blockPos, cropBlockState, SET_BLOCKSTATE_FLAG);
          success = true;
      }
    }
    return success;
  }

  @Override
  public void grow(World world, BlockPos blockPos, float growthAmount)
  {
    IBlockState cropState = world.getBlockState(blockPos);
    if (!(cropState.getBlock() instanceof BlockCrops)) {
      return;
    }

    final int MAX_AGE = 7;
    int currentAge = ((Integer)cropState.getValue(BlockCrops.AGE)).intValue();
    int newAge = currentAge + (int)(MAX_AGE * growthAmount / 100);
    if (newAge > MAX_AGE) {
      newAge = MAX_AGE;
    }
    cropState = cropState.withProperty(BlockCrops.AGE, newAge);
    world.setBlockState(blockPos, cropState, SET_BLOCKSTATE_FLAG);
  }

  public static class CropPlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      if (iBlockState == null || !(iBlockState.getBlock() instanceof BlockCrops)) return null;

      return new CropsPlant(iBlockState);
    }

    public Collection<Block> getBlocksUsedByThisPlant() {
      return ImmutableList.of(Blocks.wheat, Blocks.carrots, Blocks.potatoes);
    }
  }

  private CropsPlant(IBlockState i_iBlockState)
  {
    cropBlockState = i_iBlockState;
  }

  private IBlockState cropBlockState;

}
