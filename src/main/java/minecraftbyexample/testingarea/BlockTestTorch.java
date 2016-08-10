//package minecraftbyexample.testingarea;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.material.Material;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
///**
// * User: The Grey Ghost
// * Date: 24/12/2014
// *
// * BlockSimple is a ordinary solid cube with the six faces numbered from 0 - 5.
// * For background information on blocks see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
// */
//public class BlockTestTorch extends Block
//{
//  public BlockTestTorch()
//  {
//    super(Material.rock);
//    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
//  }
//
//  // the block will render in the SOLID layer.  See XXXX for more information.
//  @SideOnly(Side.CLIENT)
//  public BlockRenderLayer getBlockLayer()
//  {
//    return BlockRenderLayer.SOLID;
//  }
//
//  // used by the renderer to control lighting and visibility of other blocks.
//  // set to true because this block is opaque and occupies the entire 1x1x1 space
//  // not strictly required because the default (super method) is true
//  @Override
//  public boolean isOpaqueCube() {
//    return false;
//  }
//
//  // used by the renderer to control lighting and visibility of other blocks, also by
//  // (eg) wall or fence to control whether the fence joins itself to this block
//  // set to true because this block occupies the entire 1x1x1 space
//  // not strictly required because the default (super method) is true
//  @Override
//  public boolean isFullCube() {
//    return false;
//  }
//
//  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
//  // not strictly required because the default (super method) is 3.
//  @Override
//  public int getRenderType() {
//    return 3;
//  }
//}
