package minecraftbyexample.testingarea.plantspawners;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by TGG on 3/01/2016.
 */
todo:need to rethink this
        -these utility classes need to
        1)provide a way to spawn the desired plant at the desired location
        2)identify the plant from an iBlockState and place it at another location if suitable.

        First is easy-use the appropriate Spawner with the type information as appropriate-no need to unify these
        Second-the block chooses the appropriate

class, and the

class then looks at the iBlockState to identify the
  relevant type
This is done using the main type to generate sub-types

Registration of all types is done by the main utility which calls each

class in turn


public abstract class NewPlantSpawner {
  /**
   * Attempt to spawn the plant at the given block location.
   * 1) checks if this is a suitable location for the plant
   * 2) if so, generate it
   *
   * @param world
   * @param blockPos the position where the base of the plant will spawn.  eg for dirt - one above the dirt block
   * @param random
   * @return true if a plant was spawned, false otherwise
   */
  abstract public boolean trySpawnNewPlant(World world, BlockPos blockPos, Random random);

  /**
   * Try to clone the plant at sourcePlantPos to the destinationPlantPos
   * Doesn't work if this spawner isn't the right type.  (Fails without error)
   *
   * @param world
   * @param sourcePlantPos
   * @param destinationPlantPos
   * @return success if spawned
   */
  boolean tryClonePlant(World world, BlockPos sourcePlantPos, BlockPos destinationPlantPos, Random random);

  protected abstract NewPlantSpawner getSpawnerSpecific(IBlockState iBlockState);

  public static interface Spawner
  {
    /**
     * Which blocks are handled by this spawner?
     *
     * @return a collection of all the plants this spawner can produce
     */
    Collection<Block> getBlocksHandled();

    /**
     * retrieve the spawner for this plant (based on the iBlockState)
     * @param iBlockState
     * @return
     */
    NewPlantSpawner getSpawner(IBlockState iBlockState);
  }

  public static NewPlantSpawner getSpawner(IBlockState iBlockState)
  {
    checkNotNull(iBlockState);
    Block block = iBlockState.getBlock();
    NewPlantSpawner spawner = spawnerByBlock.get(block);
    if (spawner == null) return new NothingSpawner();
    return spawner.getSpawnerSpecific(iBlockState);
  }

  protected final static int SET_BLOCKSTATE_FLAG = 3;  // update flag setting to use for setBlockState
//  final static Collection<NewPlantSpawner> allSpawners = ImmutableList.of(aa)


  private final static Map<Block, NewPlantSpawner> spawnerByBlock = new HashMap<Block, NewPlantSpawner>();

  private static Map<Block, NewPlantSpawner> initialise()
  {
    ImmutableMap.Builder<Block, NewPlantSpawner> builder = ImmutableMap.builder();
    builder.put()
//            r
//    ImmutableMultimap.copyOf()
//    Map

  }

  private static addBlocksForSpawner(NewPlantSpawner spawner, )

}
