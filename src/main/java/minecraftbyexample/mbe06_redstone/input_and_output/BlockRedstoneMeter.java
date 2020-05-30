package minecraftbyexample.mbe06_redstone.input_and_output;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 27/11/2015
 *
 * BlockRedstoneMeter is a simple block with an associated TileEntity to render the block's power level.
 * The meter will also provide weak power to the block UP and DOWN (eg a light) - it flashes the light
 *   at a speed related to the input power.
 * We use a TileEntity because
 * 1) that's the easiest way to get the block's power level on the client side, without
 *   having to use metadata.
 * 2) our block needs to store the input power level, for later use when others call the getWeakPower().
 *    for the reason why, see http://greyminecraftcoder.blogspot.com/2020/05/redstone-1152.html
 */
public class BlockRedstoneMeter extends Block
{
  public BlockRedstoneMeter()
  {
    super(Block.Properties.create(Material.IRON));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader blockReader) {return new TileEntityRedstoneMeter();}

  // ------ methods relevant to redstone
  //  The methods below are used to provide power to neighbours.
  //  If you are looking for the rendering redstone calculations, look in TileEntityRedstoneMeter.getPowerLevelClient()

  /**
   * This block can provide power
   * @return
   */
  @Override
  public boolean canProvidePower(BlockState iBlockState) {
    return true;
  }

  /** How much weak power does this block provide to the adjacent block?
   * The meter provides weak power to the block above it.
   * The meter flashes the power according to how strong the input signals are
   * See https://greyminecraftcoder.blogspot.com/2020/05/redstone-1152.html for more information
   * @param blockReader
   * @param pos the position of this block
   * @param state the blockstate of this block
   * @param directionFromNeighborToThis eg EAST means that this is to the EAST of the block which is asking for weak power
   * @return The power provided [0 - 15]
   */
  @Override
  public int getWeakPower(BlockState state, IBlockReader blockReader, BlockPos pos,  Direction directionFromNeighborToThis)
  {
    if (directionFromNeighborToThis != Direction.DOWN) {
      return 0;
    }

    boolean isOutputOn = false;
    TileEntity tileentity = blockReader.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneMeter) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileentity;
      isOutputOn = tileEntityRedstoneMeter.getOutputState();
    }

    final int OUTPUT_POWER_WHEN_ON = 15;
    return isOutputOn ? OUTPUT_POWER_WHEN_ON : 0;
  }

  /**
   *  The redstone meter doesn't provide strong power to any other block.
   * @param worldIn
   * @param pos the position of this block
   * @param state the blockstate of this block
   * @param directionFromNeighborToThis eg EAST means that this is to the EAST of the block which is asking for strong power
   * @return The power provided [0 - 15]
   */

  @Override
  public int getStrongPower(BlockState state, IBlockReader worldIn, BlockPos pos, Direction directionFromNeighborToThis)
  {
    return 0;
  }

  // Retrieve the current input power level of the meter - the maximum of the five sides EAST, WEST, NORTH, SOUTH, DOWN
  //   Don't look UP
  private int getPowerLevelInputFromNeighbours(World world, BlockPos pos) {

//    int powerLevel = world.getRedstonePowerFromNeighbors(pos);  // if input can come from any side, use this line

    int maxPowerFound = 0;
    for (Direction whichFace : Direction.values()) {
      if (whichFace != Direction.UP) {
        BlockPos neighborPos = pos.offset(whichFace);
        int powerLevel = world.getRedstonePower(neighborPos, whichFace);
        maxPowerFound = Math.max(powerLevel, maxPowerFound);
      }
    }

    return maxPowerFound;
  }

  // ------ various block methods that react to changes and are responsible for updating the redstone power information

  // Called when a neighbouring block changes.
  // Only called on the server side- so it doesn't help us alter rendering on the client side.
  @Override
  public void neighborChanged(BlockState currentState, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    calculatePowerInputAndNotifyNeighbors(world, pos);
  }

  // Our flashing output uses scheduled ticks to toggle the output.
  //  Scheduling of ticks is by calling  world.scheduleTick(pos, block, numberOfTicksToDelay);
  //
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
//    calculatePowerInputAndNotifyNeighbors(world, pos);

    TileEntity te = world.getTileEntity(pos);
    if (te instanceof TileEntityRedstoneMeter) {
      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter)te;
      tileEntityRedstoneMeter.onScheduledTick(world, pos, state.getBlock());
    }
  }

  /**
   * Called by ItemBlocks after a block is set in the world, to allow post-place logic
   */
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    worldIn.getPendingBlockTicks().scheduleTick(pos, this, 1); // kick start the ticking
  }

  // not needed for this block because we have only one blockstate
  @Override
  public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
  }

  // not needed for this block because we have only one blockstate
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  private void calculatePowerInputAndNotifyNeighbors(World world, BlockPos pos) {
  // calculate the power level from neighbours and store in our TileEntity for later use in getWeakPower()
    int powerLevel = getPowerLevelInputFromNeighbours(world, pos);
    TileEntity tileentity = world.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneMeter) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileentity;

      boolean currentOutputState = tileEntityRedstoneMeter.getOutputState();
      tileEntityRedstoneMeter.setPowerLevelServer(powerLevel);
      // this method will also schedule the next tick using call world.scheduleTick(pos, block, delay);

      if (currentOutputState != tileEntityRedstoneMeter.getOutputState()) {
        world.notifyNeighborsOfStateChange(pos, this);
      }
    }
  }

  //----- methods related to the block's appearance (see MBE01_BLOCK_SIMPLE and MBE02_BLOCK_PARTIAL)

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.fullCube();
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

}
