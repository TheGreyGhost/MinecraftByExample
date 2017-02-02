package minecraftbyexample.mbe04_block_dynamic_block_model1;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * This block takes on the appearance of blocks next to it, but can be walked through.
 * The default texture for the block is a camouflage pattern.
 */
public class BlockCamouflage extends Block {
  public BlockCamouflage()
  {
    super(Material.CIRCUITS);                     // ensures the player can walk through the block
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
  }

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to true because this block is opaque and occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isOpaqueCube(IBlockState iBlockState) {
    return true;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to true because this block occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isFullCube(IBlockState iBlockState) {
    return true;
  }

  // render using an IBakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }

  // by returning a null collision bounding box we stop the player from colliding with it
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return NULL_AABB;
  }

  // createBlockState is used to define which properties your blocks possess
  // Vanilla BlockState is composed of listed properties only.  A variant is created for each combination of listed
  //   properties; for example two properties ON(true/false) and READY(true/false) would give rise to four variants
  //   [on=true, ready=true]
  //   [on=false, ready=true]
  //   [on=true, ready=false]
  //   [on=false, ready=false]
  // Forge adds ExtendedBlockState, which has two types of property:
  // - listed properties (like vanilla), and
  // - unlisted properties, which can be used to convey information but do not cause extra variants to be created.
  @Override
  protected BlockStateContainer createBlockState() {
    IProperty [] listedProperties = new IProperty[0]; // no listed properties
    IUnlistedProperty [] unlistedProperties = new IUnlistedProperty[] {COPIEDBLOCK};
    return new ExtendedBlockState(this, listedProperties, unlistedProperties);
  }

  // this method uses the block state and BlockPos to update the unlisted COPIEDBLOCK property in IExtendedBlockState based
  // on non-metadata information.  This is then conveyed to the IBakedModel#getQuads during rendering.
  // In this case, we look around the camouflage block to find a suitable adjacent block it should camouflage itself as
  @Override
  public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    if (state instanceof IExtendedBlockState) {  // avoid crash in case of mismatch
      IExtendedBlockState retval = (IExtendedBlockState)state;
      IBlockState bestAdjacentBlock = selectBestAdjacentBlock(world, pos);
      retval = retval.withProperty(COPIEDBLOCK, bestAdjacentBlock);
      return retval;
    }
    return state;
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return state;  //for debugging - useful spot for a breakpoint.  Not necessary.
  }

  // the COPIEDBLOCK property is used to store the identity of the block that BlockCamouflage will copy
  public static final UnlistedPropertyCopiedBlock COPIEDBLOCK = new UnlistedPropertyCopiedBlock();

  // Select the best adjacent block to camouflage as.
  // Algorithm is:
  // 1) Ignore any blocks which are not solid (CUTOUTS or TRANSLUCENT).  Ignore adjacent camouflage.
  // 2) If there are more than one type of solid block, choose the type which is present on the greatest number of sides
  // 3) In case of a tie, prefer the type which span opposite sides of the blockpos, for example:
  //       up and down; east and west; north and south.
  // 4) If still a tie, look again for spans on both sides, counting adjacent camouflage blocks as a span
  // 5) If still a tie, in decreasing order of preference: NORTH, SOUTH, EAST, WEST, DOWN, UP
  // 6) If no suitable adjacent blocks, return Block.air
  private IBlockState selectBestAdjacentBlock(IBlockAccess world, BlockPos blockPos)
  {
    final IBlockState UNCAMOUFLAGED_BLOCK = Blocks.AIR.getDefaultState();
    TreeMap<EnumFacing, IBlockState> adjacentSolidBlocks = new TreeMap<EnumFacing, IBlockState>();

    HashMap<IBlockState, Integer> adjacentBlockCount = new HashMap<IBlockState, Integer>();
    for (EnumFacing facing : EnumFacing.values()) {
      BlockPos adjacentPosition = blockPos.add(facing.getFrontOffsetX(),
                                               facing.getFrontOffsetY(),
                                               facing.getFrontOffsetZ());
      IBlockState adjacentIBS = world.getBlockState(adjacentPosition);
      Block adjacentBlock = adjacentIBS.getBlock();
      if (adjacentBlock != Blocks.AIR
          && adjacentBlock.getBlockLayer() == BlockRenderLayer.SOLID
          && adjacentBlock.isOpaqueCube(adjacentIBS)) {
        adjacentSolidBlocks.put(facing, adjacentIBS);
        if (adjacentBlockCount.containsKey(adjacentIBS)) {
          adjacentBlockCount.put(adjacentIBS, 1 + adjacentBlockCount.get(adjacentIBS));
        } else if (adjacentIBS.getBlock() != this){
          adjacentBlockCount.put(adjacentIBS, 1);
        }
      }
    }

    if (adjacentBlockCount.isEmpty()) {
      return UNCAMOUFLAGED_BLOCK;
    }

    if (adjacentSolidBlocks.size() == 1) {
      IBlockState singleAdjacentBlock = adjacentSolidBlocks.firstEntry().getValue();
      if (singleAdjacentBlock.getBlock() == this) {
        return UNCAMOUFLAGED_BLOCK;
      } else {
        return singleAdjacentBlock;
      }
    }

    int maxCount = 0;
    ArrayList<IBlockState> maxCountIBlockStates = new ArrayList<IBlockState>();
    for (Map.Entry<IBlockState, Integer> entry : adjacentBlockCount.entrySet()) {
      if (entry.getValue() > maxCount) {
        maxCountIBlockStates.clear();
        maxCountIBlockStates.add(entry.getKey());
        maxCount = entry.getValue();
      } else if (entry.getValue() == maxCount) {
        maxCountIBlockStates.add(entry.getKey());
      }
    }

    assert maxCountIBlockStates.isEmpty() == false;
    if (maxCountIBlockStates.size() == 1) {
      return maxCountIBlockStates.get(0);
    }

    // for each block which has a match on the opposite side, add 10 to its count.
    // exact matches are counted twice --> +20, match with BlockCamouflage only counted once -> +10
    for (Map.Entry<EnumFacing, IBlockState> entry : adjacentSolidBlocks.entrySet()) {
      IBlockState iBlockState = entry.getValue();
      if (maxCountIBlockStates.contains(iBlockState)) {
        EnumFacing oppositeSide = entry.getKey().getOpposite();
        IBlockState oppositeBlock = adjacentSolidBlocks.get(oppositeSide);
        if (oppositeBlock != null && (oppositeBlock == iBlockState || oppositeBlock.getBlock() == this) ) {
          adjacentBlockCount.put(iBlockState, 10 + adjacentBlockCount.get(iBlockState));
        }
      }
    }

    maxCount = 0;
    maxCountIBlockStates.clear();
    for (Map.Entry<IBlockState, Integer> entry : adjacentBlockCount.entrySet()) {
      if (entry.getValue() > maxCount) {
        maxCountIBlockStates.clear();
        maxCountIBlockStates.add(entry.getKey());
        maxCount = entry.getValue();
      } else if (entry.getValue() == maxCount) {
        maxCountIBlockStates.add(entry.getKey());
      }
    }
    assert maxCountIBlockStates.isEmpty() == false;
    if (maxCountIBlockStates.size() == 1) {
      return maxCountIBlockStates.get(0);
    }

    EnumFacing [] orderOfPreference = new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST,
                                                        EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.UP};

    for (EnumFacing testFace : orderOfPreference) {
      if (adjacentSolidBlocks.containsKey(testFace) &&
          maxCountIBlockStates.contains(adjacentSolidBlocks.get(testFace))) {
        return adjacentSolidBlocks.get(testFace);
      }
    }
    assert false : "this shouldn't be possible";
    return null;
  }

}
