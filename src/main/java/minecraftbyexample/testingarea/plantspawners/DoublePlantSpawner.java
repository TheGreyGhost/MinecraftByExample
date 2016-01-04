package minecraftbyexample.testingarea.plantspawners;//package info.ata4.minecraft.dragon.server.entity.helper.breath.plantspawners;
//
//import com.google.common.collect.ImmutableList;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockDoublePlant;
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
//*/ // copied from WorldGenDoublePlant
//public class DoublePlantSpawner implements NewPlantSpawner {
//  public DoublePlantSpawner(BlockDoublePlant.EnumPlantType enumPlantType) {
//    plantType = enumPlantType;
//  }
//
//  @Override
//  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
//    return trySpawnNewPlant(world, blockPos, plantType);
//  }
//
//  @Override
//  public Collection<Block> getSpawnablePlants() {
//    return ImmutableList.of((Block) Blocks.double_plant);
//  }
//
//  @Override
//  public boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random) {
//    IBlockState sourcePlant = world.getBlockState(sourcePlantPos);
//    if (sourcePlant.getBlock() instanceof BlockDoublePlant) {
//      BlockDoublePlant.EnumPlantType plantTypeToClone = Blocks.double_plant.getVariant(world, sourcePlantPos);
//      return trySpawnNewPlant(world, destinationPlantPos, plantTypeToClone);
//    }
//    return false;
//  }
//
//  private boolean trySpawnNewPlant(World world, BlockPos blockPos, BlockDoublePlant.EnumPlantType plantToPlace) {
//    boolean success = false;
//    if (world.isAirBlock(blockPos) && Blocks.double_plant.canPlaceBlockAt(world, blockPos)) {
//      Blocks.double_plant.placeAt(world, blockPos, plantType, SET_BLOCKSTATE_FLAG);
//      success = true;
//    }
//    return success;
//  }
//
//  private BlockDoublePlant.EnumPlantType plantType;
//}
