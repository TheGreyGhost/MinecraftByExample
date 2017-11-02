package minecraftbyexample.mbe21_tileentityspecialrenderer;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 11/01/2015
 *
 * BlockTileEntityData is a simple block with an associated TileEntity.  The base block is shaped like a hopper, the gem is
 *   rendered in the TESR.
*/
public class BlockMBE21 extends Block
{
  public BlockMBE21()
  {
    super(Material.IRON);
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
  }

  @Override
  public boolean hasTileEntity(IBlockState state)
  {
    return true;
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {return new TileEntityMBE21();}

  // Called just after the player places a block.  Sets the TileEntity's colour
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityMBE21) { // prevent a crash if not the right type, or is null
      TileEntityMBE21 tileEntityMBE21 = (TileEntityMBE21)tileentity;

      // chose a random colour for the gem:
      Color [] colorChoices = {Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN, Color.WHITE, Color.ORANGE, Color.RED};
      Random random = new Random();
      Color gemColor = colorChoices[random.nextInt(colorChoices.length)];
      tileEntityMBE21.setGemColour(gemColor);
    }
  }

  // -----------------
  // The following methods aren't particularly relevant to this example.  See MBE01, MBE02, MBE03 for more information.
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isOpaqueCube(IBlockState state)
  {
    return false;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block doesn't fill the entire 1x1x1 space
  @Override
  public boolean isFullCube(IBlockState state)
  {
    return false;
  }

  // render using a BakedModel
  // not required because the default (super method) is MODEL
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }

}
