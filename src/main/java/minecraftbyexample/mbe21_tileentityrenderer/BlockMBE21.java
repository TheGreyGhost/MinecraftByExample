package minecraftbyexample.mbe21_tileentityrenderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
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
public class BlockMBE21 extends Block
{
  public BlockMBE21()
  {
    super(Block.Properties.create(Material.ROCK)
         );
    BlockState defaultBlockState = this.stateContainer.getBaseState().with(USE_WAVEFRONT_OBJ_MODEL, false);
    this.setDefaultState(defaultBlockState);
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

      // chose a random colour for the artifact:
      Color [] colorChoices = {Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN, Color.WHITE, Color.ORANGE, Color.RED};
      Random random = new Random();
      Color artifactColour = colorChoices[random.nextInt(colorChoices.length)];
      tileEntityMBE21.setArtifactColour(artifactColour);

      // choose a random render style for the artifact:
      TileEntityMBE21.EnumRenderStyle renderStyle = TileEntityMBE21.EnumRenderStyle.pickRandom();
      tileEntityMBE21.setArtifactRenderStyle(renderStyle);
    }
  }

  /**
   * When the player right-clicks the block, update it to the next render style
   */
  @Override
  public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos,
                                           PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
    TileEntity tileentity = world.getTileEntity(blockPos);
    if (tileentity instanceof TileEntityMBE21) { // prevent a crash if not the right type, or is null
      TileEntityMBE21 tileEntityMBE21 = (TileEntityMBE21)tileentity;
      TileEntityMBE21.EnumRenderStyle renderStyle = tileEntityMBE21.getArtifactRenderStyle();
      renderStyle = renderStyle.getNextStyle();
      tileEntityMBE21.setArtifactRenderStyle(renderStyle);
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.FAIL;  // should never get here
  }

   // render using a BakedModel
   // not actually required because the default (super method) is MODEL
       @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  // Used for visuals only, as an easy way to get Forge to load the obj model used by the WaveFront render style
  public static final IProperty<Boolean> USE_WAVEFRONT_OBJ_MODEL = BooleanProperty.create("use_wavefront_obj_model");

  /**
   * Defines the properties needed for the BlockState
   * @param builder
   */
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(USE_WAVEFRONT_OBJ_MODEL);
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
