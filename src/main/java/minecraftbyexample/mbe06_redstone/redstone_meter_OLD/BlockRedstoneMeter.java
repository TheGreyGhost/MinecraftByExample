//package minecraftbyexample.mbe06_redstone.redstone_meter_OLD;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockState;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.util.BlockPos;
//import net.minecraft.util.EnumWorldBlockLayer;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.common.property.ExtendedBlockState;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.common.property.IUnlistedProperty;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
///**
// * Created by TheGreyGhost on 27/11/2015.
// *
// * This block changes its appearance to show the amount of redstone power it is receiving.
// * It changes its appearance using ISmartBlockModel (see mbe04_block_smartblockmodel1 and mbe05_block_smartblockmodel2
// *   for more explanation on this technique)
// * This lets us avoid having to make 16 different models.
// * Alternative ways it could be done:
// * 1) Create a model for all 16 power levels, and use Block.getActualState() to choose the model based on the power
// *    level.  BlockRedstoneWire does this.
// * 2) Add a TileEntity and TileEntitySpecialRenderer to the block and generate the model algorithmically.  BlockPiston
// *     does this.
// */
//public class BlockRedstoneMeter extends Block {
//  public BlockRedstoneMeter()
//  {
//    super(Material.circuits);                     // ensures the player can walk through the block
//    this.setCreativeTab(CreativeTabs.tabBlock);   // the block will appear on the Blocks tab in creative
//  }
//
//  // redstone methods
//
//  // this method uses the block state and BlockPos to update the unlisted POWER_LEVEL property in IExtendedBlockState based
//  // on non-metadata information.  This is then conveyed to the ISmartBlockModel during rendering.
//  @Override
//  public IBlockState getExtendedState(IBlockState state, IBlockAccess iBlockAccess, BlockPos pos) {
//    if (state instanceof IExtendedBlockState && iBlockAccess instanceof World) {  // avoid crash in case of mismatch
//      World world = (World)iBlockAccess;
//      int powerLevel = world.isBlockIndirectlyGettingPowered(pos);
//      IExtendedBlockState retval = (IExtendedBlockState)state;
//      retval = retval.withProperty(POWER_LEVEL, powerLevel);
//      return retval;
//    }
//    return state;
//  }
//
//  //  --------- methods to control the block's appearance
//
//  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
//  @SideOnly(Side.CLIENT)
//  public EnumWorldBlockLayer getBlockLayer()
//  {
//    return EnumWorldBlockLayer.SOLID;
//  }
//
//  // used by the renderer to control lighting and visibility of other blocks.
//  // set to true because this block is opaque and occupies the entire 1x1x1 space
//  // not strictly required because the default (super method) is true
//  @Override
//  public boolean isOpaqueCube() {
//    return true;
//  }
//
//  // used by the renderer to control lighting and visibility of other blocks, also by
//  // (eg) wall or fence to control whether the fence joins itself to this block
//  // set to true because this block occupies the entire 1x1x1 space
//  // not strictly required because the default (super method) is true
//  @Override
//  public boolean isFullCube() {
//    return true;
//  }
//
//  // render using an IBakedModel
//  // not strictly required because the default (super method) is 3.
//  @Override
//  public int getRenderType() {
//    return 3;
//  }
//
//  // createBlockState is used to define which properties your blocks possess
//  // Vanilla BlockState is composed of listed properties only.  A variant is created for each combination of listed
//  //   properties; for example two properties ON(true/false) and READY(true/false) would give rise to four variants
//  //   [on=true, ready=true]
//  //   [on=false, ready=true]
//  //   [on=true, ready=false]
//  //   [on=false, ready=false]
//  // Forge adds ExtendedBlockState, which has two types of property:
//  // - listed properties (like vanilla), and
//  // - unlisted properties, which can be used to convey information but do not cause extra variants to be created.
//  @Override
//  protected BlockState createBlockState() {
//    IProperty [] listedProperties = new IProperty[0]; // no listed properties
//    IUnlistedProperty [] unlistedProperties = new IUnlistedProperty[] {POWER_LEVEL};
//    return new ExtendedBlockState(this, listedProperties, unlistedProperties);
//  }
//
//  @Override
//  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
//  {
//    return state;  //for debugging - useful spot for a breakpoint.  Not necessary.
//  }
//
//  // the POWER_LEVEL property is used to store the redstone power level and pass it to
//  public static final UnlistedPropertyPowerLevel POWER_LEVEL = new UnlistedPropertyPowerLevel();
//
//}
