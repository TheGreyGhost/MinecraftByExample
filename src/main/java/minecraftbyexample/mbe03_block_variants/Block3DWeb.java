package minecraftbyexample.mbe03_block_variants;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockRenderType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 * This block forms a 3D web.
 * If the block is adjacent to another Block3DWeb, or to a solid surface, it joins to it with a strand of web.
 * Its IBlockState has six unlisted properties, one for each direction (up, down, north, south, east, west)
 */
public class Block3DWeb extends Block {
  public Block3DWeb()
  {
    super(Material.WEB);                     // ensures the player can walk through the block
    this.setCreativeTab(ItemGroup.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
  }

  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
  }

  // make colliding players stick in the web like normal web
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, BlockState state, Entity entityIn)
  {
    entityIn.setInWeb();
  }

  // used by the renderer to control lighting and visibility of other block, also by
  // (eg) wall or fence to control whether the fence joins itself to this block
  // set to false because this block does not occupy the entire 1x1x1 space
  @Override
  public boolean isFullCube(BlockState blockState) {
    return false;
  }

  // render using an IBakedModel
  // not strictly required because the default (super method) is MODEL.
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  // by returning a null collision bounding box we stop the player from colliding with it
  @Override
  public AxisAlignedBB getCollisionBoundingBox(BlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    return NULL_AABB;
  }

  // createBlockState is used to define which properties your block possess
  // Vanilla BlockState is composed of listed properties only.  A variant is created for each combination of listed
  //   properties; for example two properties ON(true/false) and READY(true/false) would give rise to four variants
  //   [on=true, ready=true]
  //   [on=false, ready=true]
  //   [on=true, ready=false]
  //   [on=false, ready=false]
  @Override
  protected BlockStateContainer createBlockState() {
    IProperty [] listedProperties = new IProperty[0]; // no listed properties
    IUnlistedProperty [] unlistedProperties = new IUnlistedProperty[] {UP, DOWN, EAST, WEST, NORTH, SOUTH};
    return new ExtendedBlockState(this, listedProperties, unlistedProperties);
  }

  /** returns true if the web should connect to this block
   * Copied from BlockFence...
   * @param worldIn
   * @param pos
   * @return
   */
  private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
  {
    BlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (block == Blocks.BARRIER) return false;
    if (block == minecraftbyexample.mbe05_block_dynamic_block_model2.StartupCommon.blockGlassLantern) return true;
    if (block.getMaterial(iblockstate).isOpaque() && block.isFullCube(iblockstate) && block.getMaterial(iblockstate) != Material.GOURD) return true;
    return false;
  }

  public BlockState updatePostPlacement(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
    if(((Boolean)p_196271_1_.get(WATERLOGGED)).booleanValue()) {
      p_196271_4_.getPendingFluidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickRate(p_196271_4_));
    }

    return p_196271_2_.getAxis().getPlane() == Direction.Plane.HORIZONTAL?(BlockState)p_196271_1_.with((IProperty)FACING_TO_PROPERTY_MAP.get(p_196271_2_), Boolean.valueOf(this.func_220111_a(p_196271_3_, p_196271_3_.isSolidSide(p_196271_4_, p_196271_6_, p_196271_2_.getOpposite()), p_196271_2_.getOpposite()))):super.updatePostPlacement(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
  }

  public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
    World lvt_2_1_ = p_196258_1_.getWorld();
    BlockPos lvt_3_1_ = p_196258_1_.getPos();
    IFluidState lvt_4_1_ = p_196258_1_.getWorld().getFluidState(p_196258_1_.getPos());
    BlockPos lvt_5_1_ = lvt_3_1_.north();
    BlockPos lvt_6_1_ = lvt_3_1_.east();
    BlockPos lvt_7_1_ = lvt_3_1_.south();
    BlockPos lvt_8_1_ = lvt_3_1_.west();
    BlockState lvt_9_1_ = lvt_2_1_.getBlockState(lvt_5_1_);
    BlockState lvt_10_1_ = lvt_2_1_.getBlockState(lvt_6_1_);
    BlockState lvt_11_1_ = lvt_2_1_.getBlockState(lvt_7_1_);
    BlockState lvt_12_1_ = lvt_2_1_.getBlockState(lvt_8_1_);
    return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(p_196258_1_).with(NORTH, Boolean.valueOf(this.func_220111_a(lvt_9_1_, lvt_9_1_.isSolidSide(lvt_2_1_, lvt_5_1_, Direction.SOUTH), Direction.SOUTH)))).with(EAST, Boolean.valueOf(this.func_220111_a(lvt_10_1_, lvt_10_1_.isSolidSide(lvt_2_1_, lvt_6_1_, Direction.WEST), Direction.WEST)))).with(SOUTH, Boolean.valueOf(this.func_220111_a(lvt_11_1_, lvt_11_1_.isSolidSide(lvt_2_1_, lvt_7_1_, Direction.NORTH), Direction.NORTH)))).with(WEST, Boolean.valueOf(this.func_220111_a(lvt_12_1_, lvt_12_1_.isSolidSide(lvt_2_1_, lvt_8_1_, Direction.EAST), Direction.EAST)))).with(WATERLOGGED, Boolean.valueOf(lvt_4_1_.getFluid() == Fluids.WATER));
  }


  public boolean func_220111_a(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
    Block lvt_4_1_ = p_220111_1_.getBlock();
    boolean lvt_5_1_ = lvt_4_1_.isIn(BlockTags.FENCES) && p_220111_1_.getMaterial() == this.material;
    boolean lvt_6_1_ = lvt_4_1_ instanceof FenceGateBlock && FenceGateBlock.isParallel(p_220111_1_, p_220111_3_);
    return !cannotAttach(lvt_4_1_) && p_220111_2_ || lvt_5_1_ || lvt_6_1_;
  }

  // the LINK properties are used to communicate to the ISmartBlockModel which of the links should be drawn
  public static final BooleanProperty UP = BlockStateProperties.UP;
  public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
  public static final BooleanProperty WEST = BlockStateProperties.WEST;
  public static final BooleanProperty EAST = BlockStateProperties.EAST;
  public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
  public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

  // for this model, we're making the shape match the block model exactly
  private static final Vec3d CORE_MIN_CORNER = new Vec3d(5.0, 0.0, 5.0);
  private static final Vec3d CORE_MAX_CORNER = new Vec3d(11.0, 7.0, 11.0);

  private static final Vec3d LID_MIN_CORNER = new Vec3d(6.0, 7.0, 6.0);
  private static final Vec3d LID_MAX_CORNER = new Vec3d(10.0, 9.0, 10.0);

  private static final VoxelShape NON_HANGING_BASE_SHAPE =
          Block.makeCuboidShape(CORE_MIN_CORNER.x, CORE_MIN_CORNER.y, CORE_MIN_CORNER.z, CORE_MAX_CORNER.x, CORE_MAX_CORNER.y, CORE_MAX_CORNER.z);
  private static final VoxelShape NON_HANGING_LID_SHAPE =
          Block.makeCuboidShape(LID_MIN_CORNER.x, LID_MIN_CORNER.y, LID_MIN_CORNER.z, LID_MAX_CORNER.x, LID_MAX_CORNER.y, LID_MAX_CORNER.z);
  private static final VoxelShape NON_HANGING_SHAPE = VoxelShapes.or(NON_HANGING_BASE_SHAPE, NON_HANGING_LID_SHAPE);

  private static final double HANGING_YOFFSET = 1.0;
  private static final VoxelShape HANGING_SHAPE = NON_HANGING_SHAPE.withOffset(0, HANGING_YOFFSET, 0);


}
