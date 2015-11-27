package minecraftbyexample.mbe06_redstone.redstone_meter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 27/11/2015
 *
 * BlockRedstoneMeter is a simple block with an associated TileEntity to render the block's power level.
 * Normally,
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

  // Called just after the player places a block.
  // Only called on the server side so it doesn't help us alter rendering on the client side.
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
  }

  // Called when a neighbouring block changes.
  // Only called on the server side so it doesn't help us alter rendering on the client side.
  @Override
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
  }

  @Override
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
  {
  }


//  // copy the block's power level to the tileentity, for later use by the TileEntitySpecialRenderer
//  private void updateTileEntityPowerLevel(World worldIn, BlockPos pos)
//  {
//    TileEntity tileentity = worldIn.getTileEntity(pos);
//    if (tileentity instanceof TileEntityRedstoneMeter) { // prevent a crash if not the right type, or is null
//      TileEntityRedstoneMeter tileEntityRedstoneMeter = (TileEntityRedstoneMeter) tileentity;
//
//      tileEntityRedstoneMeter.setPowerLevel(powerLevel);
//    }
//  }

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
