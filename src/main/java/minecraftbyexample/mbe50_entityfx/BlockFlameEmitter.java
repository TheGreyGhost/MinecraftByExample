package minecraftbyexample.mbe50_entityfx;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockSimple is a ordinary solid cube with the six faces numbered from 0 - 5.
 * For background information on blocks see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 */
public class BlockFlameEmitter extends Block
{
  public BlockFlameEmitter()
  {
    super(Material.rock);
    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
  }

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.SOLID;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to true because this block is opaque and occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to true because this block occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isFullCube() {
    return true;
  }

  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public int getRenderType() {
    return 3;
  }

  @Override
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
  {
    if (worldIn.isRemote) {
      double xpos = pos.getX() + 0.5;
      double ypos = pos.getY() + 1.0;
      double zpos = pos.getZ() + 0.5;
      double velocityX = 0; // increase in x position every tick
      double velocityY = 0; // increase in y position every tick;
      double velocityZ = 0.05; // increase in z position every tick
      int [] extraInfo = new int[0];

      worldIn.spawnParticle(EnumParticleTypes.LAVA, xpos, ypos, zpos, velocityX, velocityY, velocityZ, extraInfo);
      FlameFX newEffect = new FlameFX(worldIn, xpos, ypos, zpos, velocityX, velocityY, velocityZ);
      Minecraft.getMinecraft().effectRenderer.addEffect(newEffect);
    }
  }

}
