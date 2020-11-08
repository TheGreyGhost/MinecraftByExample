package minecraftbyexample.mbe06_redstone.input;

import minecraftbyexample.usefultools.SetBlockStateFlag;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * User: The Grey Ghost
 * Date: 29/11/2015
 *
 * BlockRedstoneColouredLamp is a block which looks at the redstone inputs on three sides and
 *  changes its colour depending on their strengths.
 *
 */
public class BlockRedstoneColouredLamp extends Block
{
  public BlockRedstoneColouredLamp()
  {
    super(Block.Properties.create(Material.IRON).func_235838_a_(BlockRedstoneColouredLamp::getLightValue).notSolid());
    // notSolid is required to make the lighting work properly (affects ambient occlusion calculations) -
    // for blocks which emit light, notSolid ensures that the skylight is added to the blocklight when calculating lighting
  }

  //----- methods related to redstone

  /**
   * Determine if this block will provide power to redstone and can make a redstone connection on the side provided.
   * Useful to control which sides are outputs for redstone wires.
   *
   * Don't use for inputs; for redstone which is just "passing by", it will make the redstone connect to the side of the block
   *   but it won't actually inject weak power into the block.
   *
   * @param world The current world
   * @param blockPos Block position in world of the wire that is trying to connect
   * @param directionFromNeighborToThis if not null: the side of the wire that is trying to make a horizontal connection to this block. If null: test for a stepped connection (i.e. the wire is trying to run up or down the side of solid block in order to connect to this block)
   * @return true if this is a power output for redstone, so that redstone wire should connect to it
   */
  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos blockPos, @Nullable Direction directionFromNeighborToThis)
  {
    return false;
  }

  // ---- methods to control placement of the target (must be on a solid wall)
  // copied and adapted from WallSignBlock

  // when the block is placed into the world, calculates the correct BlockState based on which direction the player is looking
  // Don't worry about the lamp colour yet, that's handled in onBlockPlacedBy()
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    BlockState blockstate = this.getDefaultState();
    World world = context.getWorld();
    BlockPos blockpos = context.getPos();
    Direction [] nearestLookingDirections = context.getNearestLookingDirections();

    for (Direction direction : nearestLookingDirections) {
      if (direction.getAxis().isHorizontal()) {
        blockstate = blockstate.with(DIRECTION_OF_UNCONNECTED_FACE, direction);
        if (blockstate.isValidPosition(world, blockpos)) {
          return blockstate;
        }
      }
    }

    return null;
  }

  // ---- methods to handle changes in state and inform neighbours when necessary

  // Called just after the player places a block.  Sets the lamp's colour.
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    BlockState newBlockState = getLampColourFromInputs(worldIn, pos, state);
    final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
    worldIn.setBlockState(pos, newBlockState, FLAGS);
  }

  /**
   * A neighbour has updated their state.  Check if redstone strength inputs have changed
   */
  @Override
  public void neighborChanged(BlockState currentState, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    BlockState newBlockState = getLampColourFromInputs(worldIn, pos, currentState);
    if (newBlockState != currentState) {
      final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
      worldIn.setBlockState(pos, newBlockState, FLAGS);
    }
  }

  //----- methods related to the block's appearance (see MBE01_BLOCK_SIMPLE and MBE02_BLOCK_PARTIAL)

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.fullCube();
  }

  // render using a BakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  // This function is registered in the Block Properties using func_235838_a;  see BlockInventoryFurnace constructor
  public static int getLightValue(BlockState state) {
    // convert the RGB to a single brightness.  Just choose the component with the highest brightness.

    int red = state.get(RED_INTENSITY);
    int green = state.get(GREEN_INTENSITY);
    int blue = state.get(BLUE_INTENSITY);

    int brightestComponent = Math.max(Math.max(red, green), blue);
    final int MIN_LIGHT_VALUE = 0;
    final int MAX_LIGHT_VALUE = 15;
    int lightValue = (int)UsefulFunctions.interpolate_with_clipping(brightestComponent,
                                                  MIN_INTENSITY, MAX_INTENSITY,
                                                  MIN_LIGHT_VALUE, MAX_LIGHT_VALUE);
    return lightValue;
  }

  // ---------methods related to storing information about the block (which way it's facing, the power level)

  // BlockRedstoneTarget has four properties:
  //  1) The direction that the unconnected face is pointing
  //     eg If this is NORTH, then red is WEST, green is SOUTH, blue is EAST
  //  2) Strength of the red input
  //  3) Strength of the green input
  //  4) Strength of the blue input
  // This results in a total of 4x5x5x5 = 600 blockstates, which is pushing the limit of what is reasonable, and I've
  //   only done it this way for the purposes of clarity.
  // If you are using this many blockstates you should consider a tile entity instead

  private static final DirectionProperty DIRECTION_OF_UNCONNECTED_FACE = HorizontalBlock.HORIZONTAL_FACING;
  // Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST

  private static final int MIN_INTENSITY = 0;
  private static final int MAX_INTENSITY = 4;
  private static final IntegerProperty RED_INTENSITY = IntegerProperty.create("red_intensity", MIN_INTENSITY, MAX_INTENSITY);
  private static final IntegerProperty GREEN_INTENSITY = IntegerProperty.create("green_intensity", MIN_INTENSITY, MAX_INTENSITY);
  private static final IntegerProperty BLUE_INTENSITY = IntegerProperty.create("blue_intensity", MIN_INTENSITY, MAX_INTENSITY);

  // necessary to define which properties your block use - will also affect the variants listed in the blockstates model file
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(RED_INTENSITY).add(GREEN_INTENSITY).add(BLUE_INTENSITY).add(DIRECTION_OF_UNCONNECTED_FACE);
  }

  // returns the RGB colour of the lamp based on the current redstone inputs
  public static int getRGBlampColour(BlockState state) {
    int red_intensity = state.get(RED_INTENSITY);
    int green_intensity = state.get(GREEN_INTENSITY);
    int blue_intensity = state.get(BLUE_INTENSITY);

    final int MIN_COMPONENT_VALUE = 0;
    final int MAX_COMPONENT_VALUE = 255;
    int redComponent = (int)UsefulFunctions.interpolate_with_clipping(red_intensity,
                                                                      MIN_INTENSITY, MAX_INTENSITY,
                                                                      MIN_COMPONENT_VALUE, MAX_COMPONENT_VALUE);
    int greenComponent = (int)UsefulFunctions.interpolate_with_clipping(green_intensity,
                                                                        MIN_INTENSITY, MAX_INTENSITY,
                                                                        MIN_COMPONENT_VALUE, MAX_COMPONENT_VALUE);
    int blueComponent = (int)UsefulFunctions.interpolate_with_clipping(blue_intensity,
                                                                       MIN_INTENSITY, MAX_INTENSITY,
                                                                       MIN_COMPONENT_VALUE, MAX_COMPONENT_VALUE);
    Color lampColour = new Color(redComponent, greenComponent, blueComponent);
    return lampColour.getRGB();
  }

  /**
   * Read the redstone inputs on the red, green, and blue input faces and return the corresponding blockstate
   * @param world
   * @param pos
   * @param state
   * @return
   */
  private BlockState getLampColourFromInputs(World world, BlockPos pos, BlockState state)
  {
    // the colour is based on the redstone inputs from three sides.
    //  If the lamp is facing NORTH, the red input is WEST, green input is SOUTH, blue input is EAST

    Direction facing = state.get(DIRECTION_OF_UNCONNECTED_FACE);
    Direction redDirection = facing.rotateYCCW();
    Direction greenDirection = facing.getOpposite();
    Direction blueDirection = facing.rotateY();

    BlockPos redNeighbour = pos.offset(redDirection);
    int redPower = world.getRedstonePower(redNeighbour, redDirection);
    BlockPos greenNeighbour = pos.offset(greenDirection);
    int greenPower = world.getRedstonePower(greenNeighbour, greenDirection);
    BlockPos blueNeighbour = pos.offset(blueDirection);
    int bluePower = world.getRedstonePower(blueNeighbour, blueDirection);

    final int MIN_POWER_INPUT = 0;
    final int MAX_POWER_INPUT = 15;
    double redIntensity = UsefulFunctions.interpolate_with_clipping(redPower, MIN_POWER_INPUT, MAX_POWER_INPUT, MIN_INTENSITY, MAX_INTENSITY);
    double greenIntensity = UsefulFunctions.interpolate_with_clipping(greenPower, MIN_POWER_INPUT, MAX_POWER_INPUT, MIN_INTENSITY, MAX_INTENSITY);
    double blueIntensity = UsefulFunctions.interpolate_with_clipping(bluePower, MIN_POWER_INPUT, MAX_POWER_INPUT, MIN_INTENSITY, MAX_INTENSITY);

    BlockState newBlockState = state
            .with(RED_INTENSITY, (int)Math.round(redIntensity))
            .with(GREEN_INTENSITY, (int)Math.round(greenIntensity))
            .with(BLUE_INTENSITY, (int)Math.round(blueIntensity));

    return newBlockState;
  }

}
