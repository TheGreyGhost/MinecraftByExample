package minecraftbyexample.mbe21_tileentityrenderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
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
      tileEntityMBE21.setObjectColour(gemColor);
    }
  }

  // render using a BakedModel
  // not required because the default (super method) is MODEL
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  // see MBE02 for more guidance on block VoxelShapes
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return FULL_SHAPE;
  }

  // Create block shapes which match the shape of the hopper (copied from HopperBlock)

  private static final VoxelShape INPUT_SHAPE = Block.makeCuboidShape(0.0D, 10.0D, 0.0D,
          16.0D, 16.0D, 16.0D);
  private static final VoxelShape MIDDLE_SHAPE = Block.makeCuboidShape(4.0D, 4.0D, 4.0D,
          12.0D, 10.0D, 12.0D);
  private static final VoxelShape INPUT_PLUS_MIDDLE_SHAPE = VoxelShapes.or(MIDDLE_SHAPE, INPUT_SHAPE);
  private static final VoxelShape INSIDE_BOWL_SHAPE = Block.makeCuboidShape(2.0D, 11.0D, 2.0D,
          14.0D, 16.0D, 14.0D);
  private static final VoxelShape HOPPER_SHELL_SHAPE = VoxelShapes.combineAndSimplify(INPUT_PLUS_MIDDLE_SHAPE, INSIDE_BOWL_SHAPE, IBooleanFunction.ONLY_FIRST);
  private static final VoxelShape BOTTOM_HUB_SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D,
          10.0D, 4.0D, 10.0D);
  private static final VoxelShape FULL_SHAPE = VoxelShapes.or(HOPPER_SHELL_SHAPE, BOTTOM_HUB_SHAPE);

}
