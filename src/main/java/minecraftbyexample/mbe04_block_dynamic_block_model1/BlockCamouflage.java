package minecraftbyexample.mbe04_block_dynamic_block_model1;

import com.google.common.base.Preconditions;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.BlockRenderType;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * This block takes on the appearance of block next to it, but can be walked through.
 * The default texture for the block is a camouflage pattern.
 */
public class BlockCamouflage extends Block {
  public BlockCamouflage()
  {
    super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement()  // look at Block.Properties for further options
            //notOpaque().notSolid()
    );
  }

  // render using an IBakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }


  // Select the best adjacent block to camouflage as.
  // Algorithm is:
  // 1) Ignore any block which are not fully opaque cubes.  Ignore adjacent camouflage.  Ignore grass blocks (the
  //       colour of grass blocks is hardcoded which makes the camouflage block look grey when copying grass)
  // 2) If there are more than one type of solid block, choose the type which is present on the greatest number of sides
  // 3) In case of a tie, prefer the type which span opposite sides of the blockpos, for example:
  //       up and down; east and west; north and south.
  // 4) If still a tie, look again for spans on both sides, counting adjacent camouflage block as a span
  // 5) If still a tie, in decreasing order of preference: NORTH, SOUTH, EAST, WEST, DOWN, UP
  // 6) If no suitable adjacent block, return Empty
  public static Optional<BlockState> selectBestAdjacentBlock(@Nonnull ILightReader world, @Nonnull BlockPos blockPos)
  {
    TreeMap<Direction, BlockState> adjacentSolidBlocks = new TreeMap<Direction, BlockState>();

    HashMap<BlockState, Integer> adjacentBlockCount = new HashMap<BlockState, Integer>();
    for (Direction facing : Direction.values()) {
      BlockPos adjacentPosition = blockPos.add(facing.getXOffset(),
                                               facing.getYOffset(),
                                               facing.getZOffset());
      BlockState adjacentBS = world.getBlockState(adjacentPosition);
      Block adjacentBlock = adjacentBS.getBlock();
      if (!adjacentBlock.isAir(adjacentBS, world, adjacentPosition)
          && adjacentBS.isOpaqueCube(world, adjacentPosition)) {
        adjacentSolidBlocks.put(facing, adjacentBS);
        if (adjacentBlockCount.containsKey(adjacentBS)) {
          adjacentBlockCount.put(adjacentBS, 1 + adjacentBlockCount.get(adjacentBS));
        } else if (adjacentBS.getBlock() != StartupCommon.blockCamouflage
                   && adjacentBS.getBlock() != Blocks.GRASS_BLOCK) {
          adjacentBlockCount.put(adjacentBS, 1);
        }
      }
    }

    if (adjacentBlockCount.isEmpty()) {
      return Optional.empty();
    }

    if (adjacentSolidBlocks.size() == 1) {
      BlockState singleAdjacentBlock = adjacentSolidBlocks.firstEntry().getValue();
      if (singleAdjacentBlock.getBlock() == StartupCommon.blockCamouflage) {
        return Optional.empty();
      } else {
        return Optional.of(singleAdjacentBlock);
      }
    }

    // 2) multiple choices. Look for the one(s) present on the most sides.

    int maxCount = 0;
    ArrayList<BlockState> maxCountIBlockStates = new ArrayList<BlockState>();
    for (Map.Entry<BlockState, Integer> entry : adjacentBlockCount.entrySet()) {
      if (entry.getValue() > maxCount) {
        maxCountIBlockStates.clear();
        maxCountIBlockStates.add(entry.getKey());
        maxCount = entry.getValue();
      } else if (entry.getValue() == maxCount) { // a tie
        maxCountIBlockStates.add(entry.getKey());
      }
    }

    if (maxCountIBlockStates.isEmpty()) throw new AssertionError("maxCountIBlockStates.isEmpty()");
    if (maxCountIBlockStates.size() == 1) {               // one clear winner
      return Optional.of(maxCountIBlockStates.get(0));
    }

    // for each block which has a match on the opposite side, add 10 to its count.
    // exact matches are counted twice --> +20, match with BlockCamouflage only counted once -> +10
    for (Map.Entry<Direction, BlockState> entry : adjacentSolidBlocks.entrySet()) {
      BlockState iBlockState = entry.getValue();
      if (maxCountIBlockStates.contains(iBlockState)) {
        Direction oppositeSide = entry.getKey().getOpposite();
        BlockState oppositeBlock = adjacentSolidBlocks.get(oppositeSide);
        if (oppositeBlock != null && (oppositeBlock == iBlockState || oppositeBlock.getBlock() == StartupCommon.blockCamouflage) ) {
          adjacentBlockCount.put(iBlockState, 10 + adjacentBlockCount.get(iBlockState));
        }
      }
    }

    maxCount = 0;
    maxCountIBlockStates.clear();
    for (Map.Entry<BlockState, Integer> entry : adjacentBlockCount.entrySet()) {
      if (entry.getValue() > maxCount) {
        maxCountIBlockStates.clear();
        maxCountIBlockStates.add(entry.getKey());
        maxCount = entry.getValue();
      } else if (entry.getValue() == maxCount) {
        maxCountIBlockStates.add(entry.getKey());
      }
    }
    if (maxCountIBlockStates.isEmpty()) throw new AssertionError("maxCountIBlockStates.isEmpty()");
    if (maxCountIBlockStates.size() == 1) {  // one clear winner
      return Optional.of(maxCountIBlockStates.get(0));
    }

    Direction[] orderOfPreference = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.EAST,
                                                        Direction.WEST, Direction.DOWN, Direction.UP};

    for (Direction testFace : orderOfPreference) {
      if (adjacentSolidBlocks.containsKey(testFace) &&
          maxCountIBlockStates.contains(adjacentSolidBlocks.get(testFace))) {
        return Optional.of(adjacentSolidBlocks.get(testFace));
      }
    }
    throw new AssertionError("unreachable code");
  }

}
