package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenVines
public class VinesPlant extends Plant {
  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    IBlockState vineToPlace = Blocks.vine.getDefaultState();
    for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) {
      if (Blocks.vine.canPlaceBlockOnSide(world, blockPos, facing)) {
        success = true;
        switch (facing) {
          case NORTH: {
            vineToPlace = vineToPlace.withProperty(BlockVine.NORTH, true);
            break;
          }
          case SOUTH: {
            vineToPlace = vineToPlace.withProperty(BlockVine.SOUTH, true);
            break;
          }
          case EAST: {
            vineToPlace = vineToPlace.withProperty(BlockVine.EAST, true);
            break;
          }
          case WEST: {
            vineToPlace = vineToPlace.withProperty(BlockVine.WEST, true);
            break;
          }
          default: {
            assert false : "invalid facing:" + facing;
          }
        }
      }
    }
    if (success) {
      world.setBlockState(blockPos, vineToPlace, SET_BLOCKSTATE_FLAG);
    }
    return success;
  }

  @Override
  public void grow(World world, BlockPos blockPos, float growthAmount)
  {
    // do nothing
  }

  public static class VinePlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      if (iBlockState == null || iBlockState.getBlock() != Blocks.vine) return null;

      return new VinesPlant();
    }

    public Collection<Block> getBlocksUsedByThisPlant() {
      return ImmutableList.of((Block)Blocks.vine);
    }
  }

}
