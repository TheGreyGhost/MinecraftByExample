package minecraftbyexample.mbe21_tileentityrenderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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
    super(Block.Properties.create(Material.ROCK)
         );
  }

  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {return new TileEntityMBE21();}

  // Called just after the player places a block.  Sets the TileEntity's colour
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
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

  // render using a BakedModel
  // not required because the default (super method) is MODEL
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

}
