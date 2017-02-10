package minecraftbyexample.mbe13_item_tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * BlockToolTest is a simple block used to test the interactions between tools and blocks
 * For background information see here
 * http://greyminecraftcoder.blogspot.ch/2015/01/mining-blocks-with-tools.html
 *
 * Manipulating the block properties or methods is generally the best way to customise mining behaviour if you are creating
 * your own custom block.
 *
 * First choice for most cases:
   Block.setHardness() - set the base hardness of the block
   Block.setHarvestLevel(ToolClass, level) - specify the type of tool that is most effective against this block, and what
      the tool needs to be made of (wood, stone, iron, diamond) in order to drop items when harvested.  There are two versions,
      one  for normal blocks, and one which depends on the block state (metadata) - for example a tree that is made of different
      types of wood with different toughness.
   Block.onBlockHarvested() - this method mostly does nothing but some add extra effects- eg TNT generates an explosion.
   Block.getDrops() - get a list of items that might drop when the block is harvested normally.

 * Of some use for special cases:
   Block.onBlockDestroyedByPlayer() - used to drop extra items (eg skulls) or destroy other parts of a multiblock (eg bed)
   Block.harvestBlock() - can be used to override default harvesting behaviour (for example - shearing a tree to increase
     the sapling drop rate and trigger a special achievement)
   Block.createStackedBlock() - set the type of drop for silk enchantment harvesting.
   Block.dropBlockAsItemWithChance() - customise the random dropping - for example ignoring the effect of fortune enchantment
     when harvesting wheat.
 *
 */
public class BlockToolTest extends Block
{
  public BlockToolTest(Material i_material)
  {
    super(i_material);
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
    final float DEFAULT_HARDNESS = 1.0F;
    this.setHardness(DEFAULT_HARDNESS);           // default.  can also set when creating the block instance - which is typically what vanilla does
    final int WOOD_HARVEST_LEVEL = 0;
    this.setHarvestLevel("axe", WOOD_HARVEST_LEVEL);  // default.  can also be set when creating the block instance, which is typically what vanilla does
  }

  // called when the block is destroyed, this method mostly does nothing but some Blocks add extra effects- eg TNT generates an explosion.
  @Override
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.onBlockHarvested",
                                         "{world}, " + pos + ", " + String.valueOf(state) + ", " + player.getDisplayNameString());
    super.onBlockHarvested(worldIn, pos, state, player);
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.onBlockHarvested", "");
    return;
  }

  // get a list of items that might drop when the block is harvested normally.
  // In simple cases, you can just override quantityDropped() and getItemDropped()
  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.getDrops",
            "{world}, " + pos + ", " + String.valueOf(state) + ", " + fortune);
    List<ItemStack> result = super.getDrops(world, pos, state, fortune);
    String resultString = "";
    for (ItemStack itemStack : result) {
      resultString += String.valueOf(itemStack) + "; ";
    }
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.getDrops", String.valueOf(result));
    return result;
  }

  // -----------------------------

  // Used in some special cases to drop extra items (eg skulls) or destroy other parts of a multiblock (eg bed)
  @Override
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.onBlockDestroyedByPlayer",
            "{world}, " + pos + ", " + String.valueOf(state));
    super.onBlockDestroyedByPlayer(worldIn, pos, state);
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.onBlockDestroyedByPlayer", "");
    return;
  }

  // Used in some special cases to override default harvesting behaviour (for example - shearing a tree to increase the
  //   sapling drop rate and trigger a special achievement)
  @Override
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
                           @Nullable TileEntity te, @Nullable ItemStack stack) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.harvestBlock",
            "{world}, " + pos + ", " + String.valueOf(state) + ", " + String.valueOf(te)
            +", " + String.valueOf(stack) );
    super.harvestBlock(worldIn, player, pos, state, te, stack);
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.harvestBlock", "");
    return;
  }

  // Used in some special cases set the type of drop for silk enchantment harvesting.
  @Override
  protected ItemStack getSilkTouchDrop(IBlockState state) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.createStackedBlock", String.valueOf(state));
    ItemStack result = super.getSilkTouchDrop(state);
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.createStackedBlock", String.valueOf(result));
    return result;
  }

  // Used in some special cases to customise the random dropping - for example ignoring the effect of fortune enchantment
  //   when harvesting wheat.
  @Override
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.dropBlockAsItemWithChance",
            "{world}, " + pos + ", " + String.valueOf(state) + ", " + chance + ", " + fortune);
    super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.dropBlockAsItemWithChance", "");
    return;
  }

  // ----------------------------\

  // This method is not generally useful for overriding but it can be useful for logging to show the rate of block damage per tick
  @Override
  public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer playerIn, World worldIn, BlockPos pos) {
    StartupCommon.methodCallLogger.enterMethod("BlockToolTest.getPlayerRelativeBlockHardness", playerIn.getDisplayNameString() + ", {world}, " + pos);
    Float result = super.getPlayerRelativeBlockHardness(state, playerIn, worldIn, pos);
    StartupCommon.methodCallLogger.exitMethod("BlockToolTest.getPlayerRelativeBlockHardness", String.valueOf(result));
    return result;
  }

//  @Override
//  public int getHarvestLevel(IBlockState state) {
//    Startup.methodCallLogger.enterMethod("BlockToolTest.canHarvestBlock", state.toString());
//    Integer result = super.getHarvestLevel(state);
//    Startup.methodCallLogger.exitMethod("BlockToolTest.canHarvestBlock", result.toString());
//    return result;
//  }
//
//  @Override
//  public String getHarvestTool(IBlockState state) {
//    Startup.methodCallLogger.enterMethod("BlockToolTest.getHarvestTool", state.toString());
//    String result = super.getHarvestTool(state);
//    Startup.methodCallLogger.exitMethod("BlockToolTest.getHarvestTool", result);
//    return result;
//  }
//
//  @Override
//  public boolean isToolEffective(String type, IBlockState state) {
//    Startup.methodCallLogger.enterMethod("BlockToolTest.isToolEffective", type + ", " + state.toString());
//    Boolean result = super.isToolEffective(type, state);
//    Startup.methodCallLogger.exitMethod("BlockToolTest.isToolEffective", result.toString());
//    return result;
//  }
//
//  @Override
//  public float getBlockHardness(World worldIn, BlockPos pos) {
//    Startup.methodCallLogger.enterMethod("BlockToolTest.getBlockHardness", "{world}, " + pos);
//    Float result = super.getBlockHardness(worldIn, pos);
//    Startup.methodCallLogger.exitMethod("BlockToolTest.getBlockHardness", result.toString());
//    return result;
//  }


  // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.SOLID;
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

  // render using a BakedModel (mbe13_item_tools.json --> mbe13_item_tools_model.json)
  // not strictly required because the default (super method) is 3.
  @Override
  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
    return EnumBlockRenderType.MODEL;
  }
}
