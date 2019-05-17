package minecraftbyexample.mbe07_block_behavior;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;


public class BlockDropper extends Block
{
  public BlockDropper()
  {
    super(Material.ROCK);
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
    this.setHardness(0.5f);

  }

  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
  }

  @Override
  public boolean isOpaqueCube(IBlockState iBlockState) {
    return true;
  }

  @Override
  public boolean isFullCube(IBlockState iBlockState) {
    return true;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    return Items.COOKIE;
  }

  @Override
  public int quantityDropped(Random random) {
      return random.nextInt(4) + 1;
  }
}
