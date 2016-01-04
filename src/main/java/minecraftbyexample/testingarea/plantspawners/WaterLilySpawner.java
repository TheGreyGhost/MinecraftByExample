package minecraftbyexample.testingarea.plantspawners;//package info.ata4.minecraft.dragon.server.entity.helper.breath.plantspawners;
//
//import com.google.common.collect.ImmutableList;
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.init.Blocks;
//import net.minecraft.util.BlockPos;
//import net.minecraft.world.World;
//
//import java.util.Collection;
//import java.util.Random;
//
///**
//* Created by EveryoneElse on 3/01/2016.
//*/ // copied from WorldGenWaterLily
//public class WaterLilySpawner implements NewPlantSpawner {
//  @Override
//  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
//    boolean success = false;
//    if (world.isAirBlock(blockPos) && Blocks.waterlily.canPlaceBlockAt(world, blockPos)) {
//      world.setBlockState(blockPos, Blocks.waterlily.getDefaultState(), SET_BLOCKSTATE_FLAG);
//      success = true;
//    }
//    return success;
//  }
//
//  @Override
//  public Collection<Block> getSpawnablePlants() {
//    return ImmutableList.of((Block) Blocks.waterlily);
//  }
//
//  @Override
//  public boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random) {
//    IBlockState iBlockState = world.getBlockState(sourcePlantPos);
//    if (iBlockState.getBlock() == Blocks.waterlily) {
//      return trySpawnNewPlant(world, destinationPlantPos, random);
//    }
//    return false;
//  }
//}
