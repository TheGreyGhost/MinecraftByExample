package minecraftbyexample.mbe13_item_tools;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.Set;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemToolsTest is a simple tool used to test the interactions between tools and blocks
 * For background information see here
 * http://greyminecraftcoder.blogspot.ch/2015/01/mining-blocks-with-tools.html
 *
 * Manipulating the item properties or methods is generally the best way to customise mining behaviour if you are creating
 * your own custom item.
 *
 * Item.setHarvestLevel(ToolClass, level) - first choice - give your tool one or more ToolClasses equivalent to existing tool eg "pickaxe", "axe".
   ItemTool constructor -
     EFFECTIVE_ON set of blocks - to add extra "special case" blocks that your item is effective on which don't match the Item's ToolClass(es).
     ToolMaterial, which affects the maximum dig speed and the tool durability.
   Item.getStrVsBlock() - add further "special cases" that ToolClass and ItemTool constructor don't cover.
   Item.getDigSpeed() - metadata / damage sensitive version of getStrVsBlock()
   Item.onBlockStartBreak() - can be used to abort block breaking before it is destroyed
   Item.onBlockDestroyed() - used to damage the item once the block is destroyed

   In Adventure mode, the "CanDestroy" NBT tag in the tool's itemstack is used to determine which blocks it can destroy.
 */

public class ItemToolsTest extends ItemTool
{

  public ItemToolsTest(float attackDamage, ToolMaterial material, Set effectiveBlocks) {
    super(attackDamage, material, effectiveBlocks);
    this.setCreativeTab(CreativeTabs.tabMisc);   // the item will appear on the Miscellaneous tab in creative

    final int WOOD_HARDNESS_LEVEL = 0;
    final int STONE_HARDNESS_LEVEL = 1;
    this.setHarvestLevel("axe", WOOD_HARDNESS_LEVEL);  // default.  can also be set when creating the block instance, which is typically what vanilla does
    this.setHarvestLevel("pickaxe", STONE_HARDNESS_LEVEL);  // can add hardness level for as many or few ToolClasses as you want; new ToolClasses also possible
  }

  // can be useful to add further "special cases" that ToolClass and ItemTool constructor don't cover.
  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.getStrVsBlock", stack.getDisplayName() + ", " + block.getLocalizedName());
    Float result = super.getStrVsBlock(stack, block);
    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.getStrVsBlock", String.valueOf(result));
    return result;
  }

  // metadata / damage sensitive version of getStrVsBlock()
  @Override
  public float getDigSpeed(ItemStack stack, IBlockState state) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.getDigSpeed", stack.getDisplayName() + ", " + state);
    Float result = super.getDigSpeed(stack, state);
    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.getDigSpeed", String.valueOf(result));
    return result;
  }

  //   Item.onBlockStartBreak() - called immediately before the block is destroyed - can be used to abort block breaking before it is destroyed
  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.onBlockStartBreak", itemstack.getDisplayName() + ", " + pos + ", " + player.getName());
    Boolean result =  super.onBlockStartBreak(itemstack, pos, player);
    if (MinecraftByExample.proxy.playerIsInCreativeMode(player)) {
      player.addChatComponentMessage(new ChatComponentText("Currently in creative mode; switch to survival mode using /gamemode."));
    }
    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.onBlockStartBreak", String.valueOf(result));
    return result;
  }

  @Override
  // damage the item when it destroys a block - defaults to 1 damage for tools
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.onBlockDestroyed",
            stack.getDisplayName() + ", {world}, " + blockIn.getLocalizedName() + ", "
                    + pos + ", " + playerIn.getName()
    );
    Boolean result = super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.onBlockDestroyed", String.valueOf(result));
    return result;
  }


//  @Override
//  public boolean canHarvestBlock(Block blockIn) {
//    Startup.methodCallLogger.enterMethod("ItemToolsTest.canHarvestBlock#1", blockIn.getLocalizedName());
//    Boolean result = super.canHarvestBlock(blockIn);
//    Startup.methodCallLogger.exitMethod("ItemToolsTest.canHarvestBlock#1", result.toString());
//    return result;
//  }
//
//  @Override
//  public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
//    Startup.methodCallLogger.enterMethod("ItemToolsTest.canHarvestBlock#2", par1Block.getLocalizedName() + ", " + itemStack.getDisplayName());
//    Boolean result = super.canHarvestBlock(par1Block, itemStack);
//    Startup.methodCallLogger.exitMethod("ItemToolsTest.canHarvestBlock#2", result.toString());
//    return result;
//  }
//
//  @Override
//  public int getHarvestLevel(ItemStack stack, String toolClass) {
//    Startup.methodCallLogger.enterMethod("ItemToolsTest.getHarvestLevel", stack.getDisplayName() + ", " + toolClass);
//    Integer result = super.getHarvestLevel(stack, toolClass);
//    Startup.methodCallLogger.exitMethod("ItemToolsTest.getHarvestLevel", result.toString());
//    return result;
//  }
}
