package minecraftbyexample.mbe06_redstone.input_and_output;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
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
 * The meter will also provide weak power to the blocks UP and DOWN (eg a light) - it flashes the light
 *   at a speed related to the input power.
 * We use a TileEntity because
 * 1) that's the easiest way to get the block's power level on the client side, without
 *   having to use metadata.
 * 2) our block needs to store the input power level, for later use when others call the getWeakPower().
 *    for the reason why, see http://greyminecraftcoder.blogspot.com.au/2015/11/redstone.html
 */
public class BlockRedstoneMeter extends Block implements ITileEntityProvider
{
  public BlockRedstoneMeter()
  {
    super(Material.iron);
    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityRedstoneMeter();
  }

  // ------ methods relevant to redstone
  //  The methods below are used to provide power to neighbours.
  //  If you are looking for the rendering redstone calculations, look in TileEntityRedstoneMeter.getPowerLevelClient()

  /**
   * This block can provide power
   * @return
   */
  @Override
  public boolean canProvidePower()
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
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    if (side != EnumFacing.UP && side != EnumFacing.DOWN) {
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
   *  The target provides strong power to the block it's mounted on (hanging on)
   * @param worldIn
   * @param pos the position of this block
   * @param state the blockstate of this block
   * @param side the side of the block - eg EAST means that this is to the EAST of the adjacent block.
   * @return The power provided [0 - 15]
   */

  @Override
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    return 0;
  }

  // Retrieve the current input power level of the meter - the maximum of the four sides EAST, WEST, NORTH, SOUTH
  //   (don't look UP or DOWN)
  private int getPowerLevelInput(World world, BlockPos pos) {

    // int powerLevel = this.worldObj.isBlockIndirectlyGettingPowered(this.pos);  // if input can come from any side, use this line

    int maxPowerFound = 0;
    for (EnumFacing whichFace : EnumFacing.HORIZONTALS) {
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
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
  }

  // Called when a neighbouring block changes.
  // Only called on the server side- so it doesn't help us alter rendering on the client side.
  @Override
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
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
        worldIn.notifyNeighborsOfStateChange(pos, this);
      }
    }
  }

  // Our flashing output uses scheduled ticks to toggle the output.
  //  Scheduling of ticks is by calling  world.scheduleUpdate(pos, block, numberOfTicksToDelay);
  //
  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
  {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneMeter) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileentity;

      boolean currentOutputState = tileEntityRedstoneMeter.getOutputState();
      tileEntityRedstoneMeter.onScheduledUpdateTick();
        // this method will also schedule the next tick using call world.scheduleUpdate(pos, block, lastTickDelay);

      if (currentOutputState != tileEntityRedstoneMeter.getOutputState()) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
      }
    }
  }

  // ---- the following are copied from BlockRedstoneComparator.  I'm not 100% sure it's necessary to manually
  //   setTileEntity, removeTileEntity, etc, but I figure copying vanilla is a good rule
  @Override
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
  {
    super.onBlockAdded(worldIn, pos, state);
    worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
  }

  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
    super.breakBlock(worldIn, pos, state);
    worldIn.removeTileEntity(pos);
    worldIn.notifyNeighborsOfStateChange(pos, this);
  }

  // -----------------
  // The following methods aren't particularly relevant to this example.  See MBE01, MBE02, MBE03 for more information.
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean isFullCube() {
    return false;
  }

  @Override
  public int getRenderType() {
    return 3;
  }


}
