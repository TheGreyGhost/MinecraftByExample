package minecraftbyexample.mbe06_redstone.output_only;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
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
 * The block uses a property to store the currently selected power level; for more information about creating block with
 *   properties, see MBE03_BLOCK_VARIANTS, including an example of how to make a block that can face in different
 *   directions depending on how you place it.
 */
public class BlockRedstoneVariableSource extends Block
{
  public BlockRedstoneVariableSource()
  {
    super(Material.ROCK);
    this.setCreativeTab(ItemGroup.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
  }

  //-------------------- methods related to redstone

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
  public int getStrongPower(BlockState state, IBlockAccess worldIn, BlockPos pos, Direction side)
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
  public BlockState getStateFromMeta(int meta)
  {
    return this.getDefaultState().withProperty(POWER_INDEX, Integer.valueOf(meta));
  }

  @Override
  public int getMetaFromState(BlockState state)
  {
    return (Integer)state.getValue(POWER_INDEX);
  }

  // this method isn't required if your properties only depend on the stored metadata.
  // it is required if:
  // 1) you are making a multiblock which stores information in other block eg BlockBed, BlockDoor
  // 2) your block's state depends on other neighbours (eg BlockFence)
  @Override
  public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return state;
  }

  // necessary to define which properties your block use
  // will also affect the variants listed in the blockstates model file
  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, new IProperty[] {POWER_INDEX});
  }

  // Every time the player right-clicks, cycle through to the next power setting.
  // Need to trigger an update and notify all neighbours to make sure the new power setting takes effect.
  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand,
                                  Direction side, float hitX, float hitY, float hitZ)
  {
    ItemStack heldItem = playerIn.getHeldItem(hand);
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
  public void breakBlock(World worldIn, BlockPos pos, BlockState state)
  {
    final boolean CASCADE_UPDATE = false;  // I'm not sure what this flag does, but vanilla always sets it to false
    // except for calls by World.setBlockState()
    worldIn.notifyNeighborsOfStateChange(pos, this, CASCADE_UPDATE);
    super.breakBlock(worldIn, pos, state);
  }

  //----- methods related to the block's appearance (see MBE01_BLOCK_SIMPLE and MBE02_BLOCK_PARTIAL)

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other block.
  // set to false because this block doesn't occupy the entire 1x1x1 space
  @Override
  public boolean isOpaqueCube(BlockState iBlockState) {
    return false;
  }

  // used by the renderer to control lighting and visibility of other block, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't occupy the entire 1x1x1 space
  @Override
  public boolean isFullCube(BlockState iBlockState) {
    return false;
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

}
