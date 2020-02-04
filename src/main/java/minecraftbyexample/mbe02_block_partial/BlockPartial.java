//package minecraftbyexample.mbe02_block_partial;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.shapes.ISelectionContext;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.util.math.shapes.VoxelShapes;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.IBlockReader;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
///**
// * User: The Grey Ghost
// * Date: 24/12/2014
// *
// * BlockPartial uses a model which doesn't occupy the entire 1x1x1m space, and is made up of two pieces.
// * We can walk over it without colliding.
// * For background information on block see here http://greyminecraftcoder.blogspot.com.au/2014/12/blocks-18.html
// * For a couple of the methods below the Forge guys have marked it as deprecated.  But you still need to override those
// *   "deprecated" block methods.  What they mean is "when you want to find out if a block is (eg) isOpaqueCube(),
// *   don't call block.isOpaqueCube(), call iBlockState.isOpaqueCube() instead".
// * If that doesn't make sense to you yet, don't worry.  Just ignore the "deprecated method" warning.
//
// */
//public class BlockPartial extends Block
//{
//  public BlockPartial()
//  {
//    super(Material.ROCK);
//    this.setCreativeTab(ItemGroup.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
//  }
//
//  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
//  @OnlyIn(Dist.CLIENT)
//  public BlockRenderLayer getBlockLayer()
//  {
//    return BlockRenderLayer.SOLID;
//  }
//
//  // used by the renderer to control lighting and visibility of other block.
//  // set to false because this block doesn't fill the entire 1x1x1 space
//  @Override
//  public boolean isOpaqueCube(BlockState iBlockState) {
//    return false;
//  }
//
//  // used by the renderer to control lighting and visibility of other block, also by
//  // (eg) wall or fence to control whether the fence joins itself to this block
//  // set to false because this block doesn't fill the entire 1x1x1 space
//  @Override
//  public boolean isFullCube(BlockState iBlockState) {
//    return false;
//  }
//
//  // render using a BakedModel (mbe02_block_partial.json --> mbe02_block_partial_model.json)
//  // not strictly required because the default (super method) is 3.
//  @Override
//  public BlockRenderType getRenderType(BlockState iBlockState) {
//    return BlockRenderType.MODEL;
//  }
//
//  // by returning a null collision bounding box we stop the player from colliding with it
//  @Override
//  public AxisAlignedBB getCollisionBoundingBox(BlockState state, IBlockAccess worldIn, BlockPos pos)
//  {
//    return NULL_AABB;
//  }
//
//
//  @Deprecated
//  public boolean isSolid(BlockState state) {
//    return this.blocksMovement && this.getRenderLayer() == BlockRenderLayer.SOLID;
//  }
//
//  @Deprecated
//  @OnlyIn(Dist.CLIENT)
//  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
//    return false;
//  }
//
//  @Deprecated
//  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//    return VoxelShapes.fullCube();
//  }
//
//  @Deprecated
//  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//    return this.blocksMovement ? state.getShape(worldIn, pos) : VoxelShapes.empty();
//  }
//
//  @Deprecated
//  public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
//    return state.getShape(worldIn, pos);
//  }
//
//  @Deprecated
//  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
//    return VoxelShapes.empty();
//  }
//
//
//
//
//}
