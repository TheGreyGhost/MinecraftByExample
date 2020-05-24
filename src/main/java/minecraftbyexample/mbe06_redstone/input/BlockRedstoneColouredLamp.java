package minecraftbyexample.mbe06_redstone.input;

import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    super(Block.Properties.create(Material.IRON));
  }

////  @Override
////  public boolean hasTileEntity(BlockState state)
////  {
////    return true;
////  }
////
//  // Called when the block is placed or loaded client side to get the tile entity for the block
//  // Should return a new instance of the tile entity for the block
//  @Override
//  public TileEntity createTileEntity(World world, BlockState state) {return new TileEntityRedstoneColouredLamp();}

  // Create the appropriate state for the block being placed - in this case, figure out which way the target is facing
  // Don't worry about the rgb colour yet, that's handled in  onBlockPlacedBy()
  @Override
  public BlockState getStateForPlacement(World worldIn, BlockPos thisBlockPos, Direction faceOfNeighbour,
                                   float hitX, float hitY, float hitZ, int meta, LivingEntity placer)
  {
    Direction directionTargetIsPointing = (placer == null) ? Direction.NORTH : Direction.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, directionTargetIsPointing);
  }

  // Called just after the player places a block.  Sets the lamp's colour.
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneColouredLamp) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp)tileentity;

      int rgbColour = calculateLampColour(worldIn, pos, state);
      tileEntityRedstoneColouredLamp.setRGBcolour(rgbColour);
    }
  }

  /**
   * Determine if this block can make a redstone connection on the side provided,
   * Useful to control which sides are inputs and outputs for redstone wires.
   *
   * @param world The current world
   * @param posConnectingFrom Block position in world of the wire that is trying to connect  ** HAS CHANGED SINCE 1.8.9 ***
   * @param side The side of the redstone block that is trying to make the connection, CAN BE NULL
   * @return True to make the connection
   */
  @Override
  public boolean canConnectRedstone(BlockState state, IBlockAccess world, BlockPos posConnectingFrom, Direction side)
  {
    if (side == null) return false;
    if (side == Direction.UP || side == Direction.DOWN) return false;

    // we can connect to three of the four side faces - if the block is facing north, then we can
    //  connect to WEST, SOUTH, or EAST.

    Direction whichFaceOfLamp = side.getOpposite();
    Direction blockFacingDirection = (Direction)state.getValue(PROPERTYFACING);

    if (whichFaceOfLamp == blockFacingDirection) return false;
    return true;
  }

  // Called when a neighbouring block changes.
  // Only called on the server side- so it doesn't help us alter rendering on the client side.
  // For that, we need to store the information in the tileentity and trigger a block update to send the
  //   information to the client side
  // I have no idea why this method is deprecated in 10.1.2.
  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos neighborPos)
  {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneColouredLamp) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp) tileentity;
      int currentLampColour = tileEntityRedstoneColouredLamp.getRGBcolour();
      int newLampColour = calculateLampColour(worldIn, pos, state);

      if (newLampColour != currentLampColour) {
        tileEntityRedstoneColouredLamp.setRGBcolour(newLampColour);
        BlockState iblockstate = worldIn.getBlockState(pos);
        final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
        worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
      }
    }
  }

  // -----------------
  // The following methods control the appearance of the block.
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  // used by the renderer to control lighting and visibility of other block.
  // set to true because this block is opaque and occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isOpaqueCube(BlockState iBlockState) {
    return true;
  }

  // used by the renderer to control lighting and visibility of other block, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to true because this block occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isFullCube(BlockState iBlockState) {
    return true;
  }

  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  // Change the lighting value based on the lamp colour
  @Override
  public int getLightValue(BlockState state) {
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
