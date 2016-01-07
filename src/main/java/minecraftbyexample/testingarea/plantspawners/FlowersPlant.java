package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Collection;
import java.util.Random;

/**
* Created by TGG on 3/01/2016.
*/ // copied from WorldGenFlowers
public class FlowersPlant extends Plant {

  public FlowersPlant(BlockFlower.EnumFlowerType enumFlowerType) {
    BlockFlower flowerBlock = enumFlowerType.getBlockType().getBlock();
    flowerBlockState = flowerBlock.getDefaultState().withProperty(flowerBlock.getTypeProperty(), enumFlowerType);
  }

  @Override
  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
    boolean success = false;
    if (world.isAirBlock(blockPos) && Blocks.yellow_flower.canBlockStay(world, blockPos, flowerBlockState)) {
      world.setBlockState(blockPos, flowerBlockState, SET_BLOCKSTATE_FLAG);
      success = true;
    }
    return success;
  }

  @Override
  public void grow(World world, BlockPos blockPos, float growthAmount)
  {
    // do nothing....
  }

    public static class FlowersPlantFactory extends PlantFactory
  {
    public Plant getPlantFromBlockState(IBlockState iBlockState)
    {
      if (iBlockState == null || !(iBlockState.getBlock() instanceof BlockFlower)) {
        return null;
      }
      return new FlowersPlant(iBlockState);
    }

    public Collection<Block> getBlocksUsedByThisPlant() {
      ImmutableSet.Builder<Block> allFlowerBlocks = new ImmutableSet.Builder<Block>();
      for (BlockFlower.EnumFlowerType flowerType : BlockFlower.EnumFlowerType.values()) {
        allFlowerBlocks.add(flowerType.getBlockType().getBlock());
      }
      return allFlowerBlocks.build();
    }
  }

  private FlowersPlant(IBlockState i_blockState)
  {
    flowerBlockState = i_blockState;
  }

  private IBlockState flowerBlockState;
}
