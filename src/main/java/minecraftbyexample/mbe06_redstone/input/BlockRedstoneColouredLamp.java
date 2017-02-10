package minecraftbyexample.mbe06_redstone.input;

import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
  public TileEntity createTileEntity(World world, IBlockState state) {return new TileEntityRedstoneColouredLamp();}

  // Create the appropriate state for the block being placed - in this case, figure out which way the target is facing
  // Don't worry about the rgb colour yet, that's handled in  onBlockPlacedBy()
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos thisBlockPos, EnumFacing faceOfNeighbour,
                                   float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    EnumFacing directionTargetIsPointing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, directionTargetIsPointing);
  }

  // Called just after the player places a block.  Sets the lamp's colour.
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneColouredLamp) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp)tileentity;

      int rgbColour = calculateLampColour(worldIn, pos, state);
      tileEntityRedstoneColouredLamp.setRGBcolour(rgbColour);
    }
  }

  private int calculateLampColour(World world, BlockPos pos, IBlockState state)
  {
    // the colour is based on the redstone inputs from three sides.
    //  If the lamp is facing NORTH, the red input is WEST, green input is SOUTH, blue input is EAST

    EnumFacing facing = (EnumFacing)state.getValue(PROPERTYFACING);
    EnumFacing redDirection = facing.rotateYCCW();
    EnumFacing greenDirection = facing.getOpposite();
    EnumFacing blueDirection = facing.rotateY();

    BlockPos redNeighbour = pos.offset(redDirection);
    int redPower = world.getRedstonePower(redNeighbour, redDirection);
    BlockPos greenNeighbour = pos.offset(greenDirection);
    int greenPower = world.getRedstonePower(greenNeighbour, greenDirection);
    BlockPos blueNeighbour = pos.offset(blueDirection);
    int bluePower = world.getRedstonePower(blueNeighbour, blueDirection);

    final int MIN_POWER = 0;
    final int MAX_POWER = 15;
    final int MIN_COMPONENT = 0;
    final int MAX_COMPONENT = 0xff;
    int redComponent = (int)UsefulFunctions.interpolate(redPower, MIN_POWER, MAX_POWER, MIN_COMPONENT, MAX_COMPONENT);
    int greenComponent = (int)UsefulFunctions.interpolate(greenPower, MIN_POWER, MAX_POWER, MIN_COMPONENT, MAX_COMPONENT);
    int blueComponent = (int)UsefulFunctions.interpolate(bluePower, MIN_POWER, MAX_POWER, MIN_COMPONENT, MAX_COMPONENT);
    int rgbColour = (redComponent << 16) | (greenComponent << 8) | (blueComponent);
    return rgbColour;
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
  public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos posConnectingFrom, EnumFacing side)
  {
    if (side == null) return false;
    if (side == EnumFacing.UP || side == EnumFacing.DOWN) return false;

    // we can connect to three of the four side faces - if the block is facing north, then we can
    //  connect to WEST, SOUTH, or EAST.

    EnumFacing whichFaceOfLamp = side.getOpposite();
    EnumFacing blockFacingDirection = (EnumFacing)state.getValue(PROPERTYFACING);

    if (whichFaceOfLamp == blockFacingDirection) return false;
    return true;
  }

  // this method isn't required if your properties only depend on the stored metadata.
  // it is required if:
  // 1) you are making a multiblock which stores information in other blocks eg BlockBed, BlockDoor
  // 2) your block's state depends on other neighbours (eg BlockFence)
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return state;
  }

  // Called when a neighbouring block changes.
  // Only called on the server side- so it doesn't help us alter rendering on the client side.
  // For that, we need to store the information in the tileentity and trigger a block update to send the
  //   information to the client side
  // I have no idea why this method is deprecated in 10.1.2.
  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos neighborPos)
  {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneColouredLamp) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp) tileentity;
      int currentLampColour = tileEntityRedstoneColouredLamp.getRGBcolour();
      int newLampColour = calculateLampColour(worldIn, pos, state);

      if (newLampColour != currentLampColour) {
        tileEntityRedstoneColouredLamp.setRGBcolour(newLampColour);
        IBlockState iblockstate = worldIn.getBlockState(pos);
        final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
        worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
      }
    }
  }

  // ---------methods related to storing information about the block (which way it's facing)

  // BlockRedstoneColouredLamp has one property
  // PROPERTYFACING for which way the lamp points (east, west, north, south).  EnumFacing is a standard used by vanilla for a number of blocks.
  public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

  // getStateFromMeta, getMetaFromState are used to interconvert between the block's property values and
  //   the stored metadata (which must be an integer in the range 0 - 15 inclusive)
  // The property is encoded as:
  // - lower two bits = facing direction (i.e. 0, 1, 2, 3)
  //  the lamp colour isn't stored in metadata; it is copied from the tileentity
  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    return this.getDefaultState().withProperty(PROPERTYFACING, facing);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    EnumFacing facing = (EnumFacing)state.getValue(PROPERTYFACING);

    int facingbits = facing.getHorizontalIndex();
    return facingbits;
  }

  // necessary to define which properties your blocks use - will also affect the variants listed in the blockstates model file
  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, new IProperty[] {PROPERTYFACING});
  }

  // -----------------
  // The following methods control the appearance of the block.
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  // used by the renderer to control lighting and visibility of other blocks.
  // set to true because this block is opaque and occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isOpaqueCube(IBlockState iBlockState) {
    return true;
  }

  // used by the renderer to control lighting and visibility of other blocks, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to true because this block occupies the entire 1x1x1 space
  // not strictly required because the default (super method) is true
  @Override
  public boolean isFullCube(IBlockState iBlockState) {
    return true;
  }

  // render using a BakedModel (mbe01_block_simple.json --> mbe01_block_simple_model.json)
  // not strictly required because the default (super method) is MODEL.
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }

//  /** Changes the colour of the lamp (the "tintindex" overlay only).
//   *  This is the technique used by BlockGrass to change colour in different biomes.
//   *  It's also used by BlockRedstoneWire to change the redness of the wire based on the power level
//   * @param worldIn
//   * @param pos
//   * @param renderPass
//   * @return
//   */
//  @SideOnly(Side.CLIENT)
//  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
//  {
//    int rgbColour = 0;
//    TileEntity tileEntity = worldIn.getTileEntity(pos);
//    if (tileEntity instanceof TileEntityRedstoneColouredLamp) {
//      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp)tileEntity;
//      rgbColour = tileEntityRedstoneColouredLamp.getRGBcolour();
//    }
//    return rgbColour;
//  }

  // Change the lighting value based on the lamp colour
  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    int rgbColour = 0;
    TileEntity tileEntity = world.getTileEntity(pos);
    if (tileEntity instanceof TileEntityRedstoneColouredLamp) {
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp)tileEntity;
      rgbColour = tileEntityRedstoneColouredLamp.getRGBcolour();
    }

    int lightValue = 0;

    // convert the RGB to a single brightness.  Just choose the component with the highest brightness.

    int red = (rgbColour >> 16) & 0xff;
    int green = (rgbColour >> 8) & 0xff;
    int blue = (rgbColour) & 0xff;

    int brightestComponent = Math.max(Math.max(red, green), blue);
    final int MIN_LIGHT_VALUE = 0;
    final int MAX_LIGHT_VALUE = 15;
    final int MIN_COMPONENT_VALUE = 0;
    final int MAX_COMPONENT_VALUE = 0xff;
    lightValue = (int)UsefulFunctions.interpolate(brightestComponent,
                                                  MIN_COMPONENT_VALUE, MAX_COMPONENT_VALUE,
                                                  MIN_LIGHT_VALUE, MAX_LIGHT_VALUE);
    return lightValue;
  }

}
