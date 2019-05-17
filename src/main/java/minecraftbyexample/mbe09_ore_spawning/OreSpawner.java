package minecraftbyexample.mbe09_ore_spawning;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OreSpawner implements IWorldGenerator {
  private final int CHUNK_SIZE = 16; // chunks are 16x16
  private WorldGenerator diamondSpawner = new WorldGenMinable(
      Blocks.DIAMOND_BLOCK.getDefaultState(), // the block to spawn
      16, // the size of the vein
      BlockMatcher.forBlock(Blocks.DIRT)); // the block to replace

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    addOreSpawn(diamondSpawner, world, random, chunkX * CHUNK_SIZE, chunkZ * CHUNK_SIZE, 64, 15, 160);
  }

  // This is a utility method so you can add multiple spawns easily
  private void addOreSpawn(WorldGenerator generator, World world, Random random, int blockXPos, int blockZPos,
                          int chancesToSpawn, int minY, int maxY) {
    assert maxY > minY : "The maximum Y must be greater than the Minimum Y";
    assert minY > 0 : "addOreSpawn: The Minimum Y must be greater than 0";
    assert maxY < 256 && maxY > 0 : "addOreSpawn: The Maximum Y must be less than 256 but greater than 0";

    int diffBtwnMinMaxY = maxY - minY;
    for (int i = 0; i < chancesToSpawn; i++) {
      int posX = blockXPos + random.nextInt(CHUNK_SIZE);
      int posY = minY + random.nextInt(diffBtwnMinMaxY);
      int posZ = blockZPos + random.nextInt(CHUNK_SIZE);

      generator.generate(world, random, new BlockPos(posX, posY, posZ));
    }
  }
}
