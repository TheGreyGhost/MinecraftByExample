package minecraftbyexample.testingarea.plantspawners;//package info.ata4.minecraft.dragon.server.entity.helper.breath.plantspawners;
//
//import com.google.common.collect.ImmutableList;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockTallGrass;
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
//*/ // copied from WorldGenTallGrass
//public class TallGrassSpawner implements NewPlantSpawner {
//  public TallGrassSpawner(BlockTallGrass.EnumType enumPlantType) {
//    grassToPlace = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, enumPlantType);
//  }
//
//  @Override
//  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
//    return trySpawnNewPlant(world, blockPos, grassToPlace);
//  }
//
//  @Override
//  public Collection<Block> getSpawnablePlants() {
//    return ImmutableList.of((Block) Blocks.tallgrass);
//  }
//
//  @Override
//  public boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random) {
//    IBlockState sourcePlant = world.getBlockState(sourcePlantPos);
//    if (sourcePlant.getBlock() == Blocks.tallgrass) {
//      return trySpawnNewPlant(world, destinationPlantPos, sourcePlant);
//    }
//    return false;
//  }
//
//  private TallGrassSpawner() {
//  }
//
//  private boolean trySpawnNewPlant(World world, BlockPos blockPos, IBlockState grassIBlockState) {
//    boolean success = false;
//    if (world.isAirBlock(blockPos) && Blocks.tallgrass.canBlockStay(world, blockPos, grassIBlockState)) {
//      world.setBlockState(blockPos, grassIBlockState, 2);
//      success = true;
//    }
//    return success;
//  }
//
//  private IBlockState grassToPlace;
//}
