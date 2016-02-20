package minecraftbyexample.mbe06_redstone.output_only;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockRedstoneVariableSource()
 * This block is a provider of redstone power, similar to a torch, except that the amount of power can be adjusted.
 * Right-clicking on the block will cycle through the five power settings (0=off, 4, 8, 12, 15=full).
 *
 * The block uses a property to store the currently selected power level; for more information about creating blocks with
 *   properties, see MBE03_BLOCK_VARIANTS, including an example of how to make a block that can face in different
 *   directions depending on how you place it.
 */
public class BlockRedstoneVariableSource extends Block
{
  public BlockRedstoneVariableSource()
  {
    super(Material.rock);
    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
  }

  //-------------------- methods related to redstone

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
    Integer powerIndex = (Integer)state.getValue(POWER_INDEX);

    if (powerIndex < 0) {
      powerIndex = 0;
    } else if (powerIndex > MAXIMUM_POWER_INDEX) {
      powerIndex = MAXIMUM_POWER_INDEX;
    }
    return POWER_VALUES[powerIndex];
  }

  // The variable source block does not provide strong power.  See BlockButton for a example of a block which does.
  @Override
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    return 0;
  }

  //--------- methods associated with storing the currently-selected power

  // one property for this block - the power level it is providing, index = 0 - 4 which maps to [0, 4, 8, 12, 15]

  private static final int POWER_VALUES [] = {0, 4, 8, 12, 15};
  private static final int MAXIMUM_POWER_INDEX = POWER_VALUES.length - 1;
  public static final PropertyInteger POWER_INDEX = PropertyInteger.create("power_index", 0, MAXIMUM_POWER_INDEX);

  // getStateFromMeta, getMetaFromState are used to interconvert between the block's property values and
  //   the stored metadata (which must be an integer in the range 0 - 15 inclusive)
  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return this.getDefaultState().withProperty(POWER_INDEX, Integer.valueOf(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return (Integer)state.getValue(POWER_INDEX);
  }

  // this method isn't required if your properties only depend on the stored metadata.
  // it is required if:
  // 1) you are making a multiblock which stores information in other blocks eg BlockBed, BlockDoor
  // 2) your block's state depends on other neighbours (eg BlockFence)
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return state;
  }

  // necessary to define which properties your blocks use
  // will also affect the variants listed in the blockstates model file
  @Override
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] {POWER_INDEX});
  }

  // Every time the player right-clicks, cycle through to the next power setting.
  // Need to trigger an update and notify all neighbours to make sure the new power setting takes effect.
  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    if (!playerIn.capabilities.allowEdit) {
      return false;
    } else {

      final int MARK_BLOCKS_FOR_UPDATE_FLAG = 2;
      final int NOTIFY_NEIGHBOURS_FLAG = 1;
      worldIn.setBlockState(pos, state.cycleProperty(POWER_INDEX),
                            MARK_BLOCKS_FOR_UPDATE_FLAG | NOTIFY_NEIGHBOURS_FLAG);
      return true;
    }
  }

  // When the block is broken, you may need to notify neighbours.
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
    worldIn.notifyNeighborsOfStateChange(pos, this);
    super.breakBlock(worldIn, pos, state);
  }

  //----- methods related to the block's appearance (see MBE01_BLOCK_SIMPLE and MBE02_BLOCK_PARTIAL)

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isFullCube() {
    return false;
  }

  // render using a BakedModel (mbe06_block_redstone_variable_source.json --> mbe06_block_variable_source_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public int getRenderType() {
    return 3;
  }
}
