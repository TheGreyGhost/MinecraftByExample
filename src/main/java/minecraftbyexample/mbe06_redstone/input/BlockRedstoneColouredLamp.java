package minecraftbyexample.mbe06_redstone.input;

import minecraftbyexample.mbe04_block_smartblockmodel1.UnlistedPropertyCopiedBlock;
import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
import minecraftbyexample.mbe31_inventory_furnace.TileInventoryFurnace;
import minecraftbyexample.usefultools.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

/**
 * User: The Grey Ghost
 * Date: 29/11/2015
 *
 * BlockRedstoneColouredLamp is a block which looks at the redstone inputs on three sides and
 *  changes its colour depending on their strengths.
 *
 */
public class BlockRedstoneColouredLamp extends Block implements ITileEntityProvider
{
  public BlockRedstoneColouredLamp()
  {
    super(Material.iron);
    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
  }

  // Called when the block is placed or loaded client side to get the tile entity for the block
  // Should return a new instance of the tile entity for the block
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityRedstoneColouredLamp();
  }

  // Create the appropriate state for the block being placed - in this case, figure out which way the target is facing
  // Don't worry about the rgb colour yet, that's handled in  onBlockPlacedBy()
  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos thisBlockPos, EnumFacing faceOfNeighbour,
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
   * @param pos Block position in world
   * @param side The side of the redstone block that is trying to make the connection, CAN BE NULL
   * @return True to make the connection
   */
  public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side)
  {
    if (side == null) return false;
    if (side == EnumFacing.UP || side == EnumFacing.DOWN) return false;

    // we can connect to three of the four side faces - if the block is facing north, then we can
    //  connect to WEST, SOUTH, or EAST.

    EnumFacing whichFaceOfLamp = side.getOpposite();
    IBlockState blockState = world.getBlockState(pos);
    EnumFacing blockFacingDirection = (EnumFacing)blockState.getValue(PROPERTYFACING);

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
  @Override
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityRedstoneColouredLamp) { // prevent a crash if not the right type, or is null
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp) tileentity;
      int currentLampColour = tileEntityRedstoneColouredLamp.getRGBcolour();
      int newLampColour = calculateLampColour(worldIn, pos, state);

      if (newLampColour != currentLampColour) {
        tileEntityRedstoneColouredLamp.setRGBcolour(newLampColour);
        worldIn.markBlockForUpdate(pos);
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
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] {PROPERTYFACING});
  }

  // -----------------
  // The following methods control the appearance of the block.
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean isFullCube() {
    return false;
  }

  @Override
  public int getRenderType() {
    return 3;
  }

  /** Changes the colour of the lamp (the "tintindex" overlay only).
   *  This is the technique used by BlockGrass to change colour in different biomes.
   *  It's also used by BlockRedstoneWire to change the redness of the wire based on the power level
   * @param worldIn
   * @param pos
   * @param renderPass
   * @return
   */
  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
  {
    int rgbColour = 0;
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    if (tileEntity instanceof TileEntityRedstoneColouredLamp) {
      TileEntityRedstoneColouredLamp tileEntityRedstoneColouredLamp = (TileEntityRedstoneColouredLamp)tileEntity;
      rgbColour = tileEntityRedstoneColouredLamp.getRGBcolour();
    }
    return rgbColour;
  }

  // Change the lighting value based on the lamp colour
  @Override
  public int getLightValue(IBlockAccess world, BlockPos pos) {

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
