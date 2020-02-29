package minecraftbyexample.mbe20_tileentity_data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * User: The Grey Ghost
 * Date: 11/01/2015
 *
 * BlockTileEntityData is a ordinary solid cube with an associated TileEntity
 * For background information on block see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 */
public class BlockTileEntityData extends Block
{
  public BlockTileEntityData()
  {
    super(Block.Properties.create(Material.ROCK)
        );
  }

  private final int TIMER_COUNTDOWN_TICKS = 20 * 10; // duration of the countdown, in ticks = 10 seconds

  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {return new  TileEntityData();}

  // Called just after the player places a block.  Start the tileEntity's timer
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityData) { // prevent a crash if not the right type, or is null
      TileEntityData tileEntityData = (TileEntityData)tileentity;
      tileEntityData.setTicksLeftTillDisappear(TIMER_COUNTDOWN_TICKS);
    }
  }

  // render using a BakedModel
  // not required because the default (super method) is MODEL
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }
}
