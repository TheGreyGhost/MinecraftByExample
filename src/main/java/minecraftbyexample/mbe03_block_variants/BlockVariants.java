package minecraftbyexample.mbe03_block_variants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockVariants uses a model which
 * - doesn't occupy the entire 1x1x1m space,
 * - is made up of two pieces,
 * - uses a CUTOUT texture (with seethrough holes)
 * - has variants (can face in four directions, and can be four different colours)
 * - can be waterlogged (filled with water) similar to a vanilla sign or fence
 * We can walk over it without colliding.
 * Note that the method for implementing block with variants has changed a lot since 1.12.  See here for more info:
 * https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
 *
 * The basic rules for properly implementing variant blocks are:
 * 1) For each variant which has a different item, create a unique block instance.
 *    For example - different coloured beds are YELLOW_BED, RED_BED, GREEN_BED etc
 *    They all share the same BedBlock class; the colour for each instance is provided to the constructor
 * 2) For variants which affect the block in the world, but not the corresponding held item, use a blockstate property
 *    For example - the direction that the bed is facing (north, east, south, west)
 *
 * For background information on block see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
 * For a couple of the methods below the Forge guys have marked it as deprecated.  But you still need to override those
 * "deprecated" block methods.  What they mean is "when you want to find out what is a block's getRenderType(),
 * don't call block.getRenderType(), call blockState.getRenderType() instead".
 * If that doesn't make sense to you yet, don't worry.  Just ignore the "deprecated method" warning.
 */
public class BlockVariants extends Block
{
  public BlockVariants(EnumColour blockColour)
  {
    super(Block.Properties.create(Material.ROCK).doesNotBlockMovement() // we don't want this to block movement through the block
         );
    this.blockColour = blockColour;

    BlockState defaultBlockState = this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false);
    this.setDefaultState(defaultBlockState);
  }

  private EnumColour blockColour;

  // the block will render in the CUTOUT layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.CUTOUT;
  }

  // render using a BakedModel (mbe03_block_variants.json --> mbe03_block_variants_model.json)
  // not strictly required because the default (super method) is BlockRenderType.MODEL;
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  /**
   * when the block is placed into the world, calculates the correct BlockState based on which direction the player is facing and whether there is already water
   *   in this block or not
   *   Copied from StandingSignBlock
   * @param blockItemUseContext
   * @return
   */
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
    World world = blockItemUseContext.getWorld();
    BlockPos blockPos = blockItemUseContext.getPos();

    IFluidState fluidLevelOfCurrentBlock = world.getFluidState(blockPos);
    boolean blockContainsWater = fluidLevelOfCurrentBlock.getFluid() == Fluids.WATER;  // getFluid returns EMPTY if no fluid

    Direction direction = blockItemUseContext.getPlacementHorizontalFacing();  // north, east, south, or west
    float playerFacingDirectionAngle = blockItemUseContext.getPlacementYaw(); //if you want more directions than just NESW, you can use the yaw instead.
        // likewise the pitch is also available for up/down placement.

    BlockState blockState = getDefaultState().with(FACING, direction).with(WATERLOGGED, blockContainsWater);
    return blockState;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, WATERLOGGED);
  }

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
      // Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;


  // create a new enum for our four colours, with some supporting methods to get human-readable names.
  public  enum EnumColour implements IStringSerializable
  {
    BLUE("blue"),
    RED("red"),
    GREEN("green"),
    YELLOW("yellow");

    @Override
    public String toString()
    {
      return this.name;
    }
    public String getName()
    {
      return this.name;
    }

    private final String name;

    private EnumColour(String i_name)
    {
      this.name = i_name;
    }
  }
}
