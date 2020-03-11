package minecraftbyexample.mbe80_model_renderer;

import minecraftbyexample.mbe21_tileentityrenderer.TileEntityMBE21;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
 * BlockTileEntityData is a simple block with an associated TileEntity.  The base block is shaped like a hopper, and
 * the artifact hovering above it is rendered in the TER.
*/
public class BlockMBE80 extends Block
{
  public BlockMBE80()
  {
    super(Properties.create(Material.ROCK)
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
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEntityMBE80();
  }

  // Called just after the player places a block.  Make the block the active model.
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityMBE80) { // prevent a crash if not the right type, or is null
      TileEntityMBE80 tileEntityMBE80 = (TileEntityMBE80) tileentity;
      tileEntityMBE80.getInteractiveParameters().makeThisModelActive(pos);
    }
    System.out.println("Usage:");
    System.out.println("Right click block to activate it.");
    System.out.println("Use command mbedebug param and mbedebug param3d to modify model parameters in real time");
  }

  /**
   * When the player right-clicks the block, make it the active TestModel for command, and also print the settings to console
   */
  @Override
  public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos,
                                           PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
    TileEntity tileentity = world.getTileEntity(blockPos);
    if (tileentity instanceof TileEntityMBE80) { // prevent a crash if not the right type, or is null
      TileEntityMBE80 tileEntityMBE80 = (TileEntityMBE80)tileentity;
      tileEntityMBE80.getInteractiveParameters().makeThisModelActive(blockPos);
      tileEntityMBE80.getInteractiveParameters().printToConsole();
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.FAIL;  // should never get here
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
    return SHAPE;
  }

  // Include space above the block

  private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D,
          16.0D, 32.0D, 16.0D);

}
