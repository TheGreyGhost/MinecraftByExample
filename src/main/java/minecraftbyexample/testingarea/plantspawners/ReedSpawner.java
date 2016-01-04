package minecraftbyexample.testingarea.plantspawners;//package info.ata4.minecraft.dragon.server.entity.helper.breath.plantspawners;
//
//import com.google.common.collect.ImmutableList;
//import net.minecraft.block.Block;
//import net.minecraft.block.material.Material;
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
//*/ // copied from WorldGenReed
//public class ReedSpawner implements NewPlantSpawner {
//  @Override
//  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
//    boolean success = false;
//    BlockPos groundPos = blockPos.down();
//
//    if (world.getBlockState(groundPos.west()).getBlock().getMaterial() == Material.water
//            || world.getBlockState(groundPos.east()).getBlock().getMaterial() == Material.water
//            || world.getBlockState(groundPos.north()).getBlock().getMaterial() == Material.water
//            || world.getBlockState(groundPos.south()).getBlock().getMaterial() == Material.water) {
//      int reedHeight = 2 + random.nextInt(random.nextInt(3) + 1);
//
//      for (int k = 0; k < reedHeight; ++k) {
//        if (Blocks.reeds.canBlockStay(world, blockPos)) {
//          world.setBlockState(blockPos.up(k), Blocks.reeds.getDefaultState(), SET_BLOCKSTATE_FLAG);
//          success = true;
//        }
//      }
//    }
//    return success;
//  }
//
//  @Override
//  public Collection<Block> getSpawnablePlants() {
//    return ImmutableList.of((Block) Blocks.reeds);
//  }
//
//  @Override
//  public boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random) {
//    IBlockState iBlockState = world.getBlockState(sourcePlantPos);
//    if (iBlockState.getBlock() == Blocks.reeds) {
//      return trySpawnNewPlant(world, destinationPlantPos, random);
//    }
//    return false;
//  }
//}
