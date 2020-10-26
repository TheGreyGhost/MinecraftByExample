package minecraftbyexample.mbe06_redstone.output_only;

import minecraftbyexample.usefultools.SetBlockStateFlag;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockRedstoneVariableSource()
 * This block is a provider of redstone power, similar to a torch, except that the amount of power can be adjusted.
 * Right-clicking on the block will cycle through the five power settings (0=off, 4, 8, 12, 15=full).
 *
 * The block uses a property to store the currently selected power level; for more information about creating block with
 *   properties, see MBE03_BLOCK_VARIANTS, including an example of how to make a block that can face in different
 *   directions depending on how you place it.
 *
 *  Because the block provides weak power only, not strong power, it doesn't need to explicitly call
 *  notifyNeighborsOfStateChange() when its state changes
 *  (typically: in onReplaced() and onBlockActivate())
 *
 */
public class BlockRedstoneVariableSource extends Block
{
  public BlockRedstoneVariableSource() {
    super(Block.Properties.create(Material.ROCK));
    BlockState defaultBlockState = this.stateContainer.getBaseState().with(POWER_INDEX, 0);
    this.setDefaultState(defaultBlockState);
  }

  //-------------------- methods related to redstone

  /**
   * This block can provide power
   * @return
   */
  @Override
  public boolean canProvidePower(BlockState iBlockState) {
    return true;
  }

  /** How much weak power does this block provide to the adjacent block?
   * See https://greyminecraftcoder.blogspot.com/2020/05/redstone-1152.html for more information
   * @param blockAccess a cut-down interface to world which returns basic information about the world only
   * @param pos the position of this block
   * @param blockState the blockstate of this block
   * @param directionFromNeighborToThis the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */
  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess,
                          BlockPos pos, Direction directionFromNeighborToThis) {
    Integer powerIndex = blockState.get(POWER_INDEX);
    powerIndex = MathHelper.clamp(powerIndex, 0, MAXIMUM_POWER_INDEX);
    return POWER_VALUES[powerIndex];
  }

  // The variable source block does not provide strong power.  See ButtonBlock for a example of a block which does.
  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess,
                            BlockPos pos, Direction directionFromNeighborToThis) {
    return 0;
  }

  //--------- methods associated with storing the currently-selected power

  // one property for this block - the power level it is providing, index = 0 - 4 which maps to [0, 4, 8, 12, 15]

  private static final int POWER_VALUES [] = {0, 4, 8, 12, 15};
  private static final int MAXIMUM_POWER_INDEX = POWER_VALUES.length - 1;
  public static final IntegerProperty POWER_INDEX = IntegerProperty.create("power_index", 0, MAXIMUM_POWER_INDEX);

  /**
   * Defines the properties needed for the BlockState
   * @param builder
   */
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWER_INDEX);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
    BlockState blockState = getDefaultState().with(POWER_INDEX, 0);
    return blockState;
  }

  // Every time the player right-clicks, cycle through to the next power setting.
  // Need to trigger an update and notify all neighbours to make sure the new power setting takes effect.
  // copied from LeverBlock
  @Override
  public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (worldIn.isRemote) { // on client side, do nothing.  You may wish to add particles or similar.
      return ActionResultType.SUCCESS;
    } else {  // on server side, update the state.  Play sounds if desired.
      BlockState nextBlockState = state.func_235896_a_(POWER_INDEX);  //state.cycle(POWER_INDEX);
      final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
      worldIn.setBlockState(pos, nextBlockState, FLAGS);

      // because the block provides weak power only, we don't need to notifyNeighborsOfStateChange.
      return ActionResultType.SUCCESS;
    }
  }

  //----- methods related to the block's appearance (see MBE01_BLOCK_SIMPLE and MBE02_BLOCK_PARTIAL)

  private static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
  private static final VoxelShape TORCHES_SHAPE = Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 12.0D, 12.0D);
  private static final VoxelShape COMBINED_SHAPE = VoxelShapes.or(BASE_SHAPE, TORCHES_SHAPE);

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return COMBINED_SHAPE;
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

}
