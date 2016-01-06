package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by TGG on 3/01/2016.
 * The Plant superclass consolidates all the various vanilla plants to allow two operations:
 * 1) create the plant at a target location without having to switch() based on the type of plant
 * 2) take a copy of an existing plant at a given location (so it can be planted elsewhere)
 *
 * Typical usage:
 * 1) create a plant, either using the subclass' constructor (eg new CactusPlant()), or alternatively
 *   getPlantFromBlockState()
 * 2) trySpawnNewPlant() to spawn the plant at the desired location
 *
 * To extend for new Plants:
 * 1) Add a new MyPlant extends Plant
 * 2) update Plant.initialisePlantFactories() to add the MyPlant factory
 */

public abstract class Plant {
  /**

   *
   * @param world
   * @param blockPos the position where the base of the plant will spawn.  eg for dirt - one above the dirt block
   * @param random
   * @return true if a plant was spawned, false otherwise
   */
  abstract public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random);

  /**
   * Cause the plant to grow (eg crops)
   * @param world
   * @param blockPos the block position of the plant
   * @param growthAmount percentage to grow by (must be >=0, but can be >100%).
   */
  abstract public void grow(World world, BlockPos blockPos, float growthAmount);

  /**
   * For a given iBlockState, return the corresponding Plant
   * @param iBlockState
   * @return the corresponding plant, or null if not a Plant.
   */
  public static Plant getPlantFromBlockState(IBlockState iBlockState)
  {
    checkNotNull(iBlockState);
    Block block = iBlockState.getBlock();
    PlantFactory plantFactory = factoriesByBlocks.get(block);
    if (plantFactory == null) return null;
    return plantFactory.getPlantFromBlockState(iBlockState);
  }

  protected static abstract class PlantFactory
  {
    protected abstract Plant getPlantFromBlockState(IBlockState iBlockState);
    protected abstract Collection<Block> getBlocksUsedByThisPlant();
  }

  protected final static int SET_BLOCKSTATE_FLAG = 3;  // update flag setting to use for setBlockState

  private final static Map<Block, PlantFactory> factoriesByBlocks = initialisePlantFactories();

  private static Map<Block, PlantFactory> initialisePlantFactories()
  {
    ImmutableMap.Builder<Block, PlantFactory> builder = ImmutableMap.builder();
    addPlantFactory(builder, new CactusPlant.CactusPlantFactory());
    addPlantFactory(builder, new FlowersPlant.FlowersPlantFactory());
    return builder.build();
  }

  private static void addPlantFactory(ImmutableMap.Builder<Block, PlantFactory> builder, PlantFactory plantFactory)
  {
    Collection<Block> blocksToAdd = plantFactory.getBlocksUsedByThisPlant();
    for (Block block : blocksToAdd) {
      builder.put(block, plantFactory);
    }
  }

}
