package minecraftbyexample.testingarea.plantspawners;//package info.ata4.minecraft.dragon.server.entity.helper.breath.plantspawners;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockFlower;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.init.Blocks;
//import net.minecraft.util.BlockPos;
//import net.minecraft.world.World;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.Set;
//
///**
//* Created by EveryoneElse on 3/01/2016.
//*/ // copied from WorldGenFlowers
//public class FlowersSpawner implements NewPlantSpawner {
//  public FlowersSpawner(BlockFlower.EnumFlowerType enumFlowerType) {
//    BlockFlower flowerBlock = enumFlowerType.getBlockType().getBlock();
//    flowerToSpawn = flowerBlock.getDefaultState().withProperty(flowerBlock.getTypeProperty(), enumFlowerType);
//  }
//
//  @Override
//  public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random) {
//    return trySpawnNewPlant(world, blockPos, flowerToSpawn);
//  }
//
//  private boolean trySpawnNewPlant(World world, BlockPos blockPos, IBlockState whichFlowerToPlace) {
//    boolean success = false;
//    if (world.isAirBlock(blockPos) && Blocks.yellow_flower.canBlockStay(world, blockPos, whichFlowerToPlace)) {
//      world.setBlockState(blockPos, whichFlowerToPlace, SET_BLOCKSTATE_FLAG);
//      success = true;
//    }
//    return success;
//  }
//
//  @Override
//  public Collection<Block> getSpawnablePlants() {
//    Set<Block> allFlowerBlocks = new HashSet<Block>();
//    for (BlockFlower.EnumFlowerType flowerType : BlockFlower.EnumFlowerType.values()) {
//      allFlowerBlocks.add(flowerType.getBlockType().getBlock());
//    }
//    return allFlowerBlocks;
//  }
//
//  @Override
//  public boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random) {
//    IBlockState sourceFlower = world.getBlockState(sourcePlantPos);
//    if (sourceFlower.getBlock() instanceof BlockFlower) {
//      return trySpawnNewPlant(world, destinationPlantPos, sourceFlower);
//    }
//    return false;
//  }
//
//  private FlowersSpawner() {
//  }
//
//  //    private BlockFlower flowerBlock;
//  private IBlockState flowerToSpawn;
//}
