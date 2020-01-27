package minecraftbyexample.mbe06_redstone.input_and_output;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
 *    for the reason why, see http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html
 */
public class BlockRedstoneMeter extends Block
{
  public BlockRedstoneMeter()
  {
    super(Material.IRON);
    this.setCreativeTab(ItemGroup.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
  }

  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createTileEntity(World world, BlockState state) {return new TileEntityRedstoneMeter();}

  // ------ methods relevant to redstone
  //  The methods below are used to provide power to neighbours.
  //  If you are looking for the rendering redstone calculations, look in TileEntityRedstoneMeter.getPowerLevelClient()

  /**
   * This block can provide power
   * @return
   */
  @Override
  public boolean canProvidePower(BlockState iBlockState)
  {
    return true;
  }

  /** How much weak power does this block provide to the adjacent block?
   * The meter flashes the power according to how strong the input signals are
   * See http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html for more information
   * @param worldIn
   * @param pos the position of this block
   * @param state the blockstate of this block
   * @param side the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */
  @Override
  public int getWeakPower(BlockState state, IBlockAccess worldIn, BlockPos pos,  Direction side)
  {
    if (side != Direction.UP && side != Direction.DOWN) {
      return 0;
    }

    boolean isOutputOn = false;
    TileEntity tileentity = worldIn.getTileEntity(pos);
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
   * @param side the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */

  @Override
  public int getStrongPower(BlockState state, IBlockAccess worldIn, BlockPos pos, Direction side)
  {
    return 0;
  }

  // Retrieve the current input power level of the meter - the maximum of the four sides EAST, WEST, NORTH, SOUTH
  //   (don't look UP or DOWN)
  private int getPowerLevelInput(World world, BlockPos pos) {

//    int powerLevel = world.isBlockIndirectlyGettingPowered(pos);  // if input can come from any side, use this line

    int maxPowerFound = 0;
    for (Direction whichFace : Direction.HORIZONTALS) {
      BlockPos neighborPos = pos.offset(whichFace);
      int powerLevel = world.getRedstonePower(neighborPos, whichFace);
      maxPowerFound = Math.max(powerLevel, maxPowerFound);
    }
    return maxPowerFound;
  }

  // ------ various block methods that react to changes and are responsible for updating the redstone power information

  // Called just after the player places a block.
  // Only called on the server side so it doesn't help us alter rendering on the client side.
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
  }

  // Called when a neighbouring block changes.
  // Only called on the server side- so it doesn't help us alter rendering on the client side.
  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos)
  {
    // calculate the power level from neighbours and store in our TileEntity for later use in isProvidingWeakPower()
    int powerLevel = getPowerLevelInput(worldIn, pos);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneMeter) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileentity;

      boolean currentOutputState = tileEntityRedstoneMeter.getOutputState();
      tileEntityRedstoneMeter.setPowerLevel(powerLevel);
          // this method will also schedule the next tick using call world.scheduleUpdate(pos, block, lastTickDelay);

      if (currentOutputState != tileEntityRedstoneMeter.getOutputState()) {
        final boolean CASCADE_UPDATE = false;  // I'm not sure what this flag does, but vanilla always sets it to false
          // except for calls by World.setBlockState()
        worldIn.notifyNeighborsOfStateChange(pos, this, CASCADE_UPDATE);
      }
    }
  }

  // Our flashing output uses scheduled ticks to toggle the output.
  //  Scheduling of ticks is by calling  world.scheduleUpdate(pos, block, numberOfTicksToDelay);
  //
  @Override
  public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand)
  {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneMeter) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileentity;

      boolean currentOutputState = tileEntityRedstoneMeter.getOutputState();
      tileEntityRedstoneMeter.onScheduledUpdateTick();
        // this method will also schedule the next tick using call world.scheduleUpdate(pos, block, lastTickDelay);

      if (currentOutputState != tileEntityRedstoneMeter.getOutputState()) {
        final boolean CASCADE_UPDATE = false;  // I'm not sure what this flag does, but vanilla always sets it to false
        // except for calls by World.setBlockState()
        worldIn.notifyNeighborsOfStateChange(pos, this, CASCADE_UPDATE);
      }
    }
  }

  // ---- the following are copied from BlockRedstoneComparator.  I'm not 100% sure it's necessary to manually
  //   setTileEntity, removeTileEntity, etc, but I figure copying vanilla is a good rule
  @Override
  public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
  {
    super.onBlockAdded(worldIn, pos, state);
    worldIn.setTileEntity(pos, this.createTileEntity(worldIn, state));
  }

  public void breakBlock(World worldIn, BlockPos pos, BlockState state)
  {
    super.breakBlock(worldIn, pos, state);
    worldIn.removeTileEntity(pos);
    final boolean CASCADE_UPDATE = false;  // I'm not sure what this flag does, but vanilla always sets it to false
    // except for calls by World.setBlockState()
    worldIn.notifyNeighborsOfStateChange(pos, this, CASCADE_UPDATE);
  }

  // -----------------
  // The following methods aren't particularly relevant to this example.  See MBE01, MBE02, MBE03 for more information.
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  // used by the renderer to control lighting and visibility of other block.
  // set to true because this block is opaque and occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isOpaqueCube(BlockState iBlockState) {
    return true;
  }

  // used by the renderer to control lighting and visibility of other block, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to true because this block occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isFullCube(BlockState iBlockState) {
    return true;
  }

  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }
}
