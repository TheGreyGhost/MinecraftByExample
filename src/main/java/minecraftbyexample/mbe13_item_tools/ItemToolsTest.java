package minecraftbyexample.mbe13_item_tools;

import minecraftbyexample.MinecraftByExample;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Set;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemToolsTest is a simple tool used to test the interactions between tools and block
 * For background information see here
 * http://greyminecraftcoder.blogspot.ch/2015/01/mining-blocks-with-tools.html
 *
 * Manipulating the item properties or methods is generally the best way to customise mining behaviour if you are creating
 * your own custom item.
 *
 * Item.setHarvestLevel(ToolClass, level) - first choice - give your tool one or more ToolClasses equivalent to existing tool eg "pickaxe", "axe".
   ItemTool constructor -
     EFFECTIVE_ON set of block - to add extra "special case" block that your item is effective on which don't match the Item's ToolClass(es).
     ToolMaterial, which affects the maximum dig speed and the tool durability.
   Item.getStrVsBlock() - add further "special cases" that ToolClass and ItemTool constructor don't cover.
   Item.onBlockStartBreak() - can be used to abort block breaking before it is destroyed
   Item.onBlockDestroyed() - used to damage the item once the block is destroyed

   Item.getDigSpeed() has been removed in 10.1.2

 In Adventure mode, the "CanDestroy" NBT tag in the tool's itemstack is used to determine which block it can destroy.
 */

public class ItemToolsTest extends ToolItem
{

  public ItemToolsTest(float attackDamage, float attackSpeed, ToolMaterial material, Set effectiveBlocks) {
    super(attackDamage, attackSpeed, material, effectiveBlocks);
    this.setCreativeTab(ItemGroup.MISC);   // the item will appear on the Miscellaneous tab in creative

    final int WOOD_HARDNESS_LEVEL = 0;
    final int STONE_HARDNESS_LEVEL = 1;
    this.setHarvestLevel("axe", WOOD_HARDNESS_LEVEL);  // default.  can also be set when creating the block instance, which is typically what vanilla does
    this.setHarvestLevel("pickaxe", STONE_HARDNESS_LEVEL);  // can add hardness level for as many or few ToolClasses as you want; new ToolClasses also possible
  }

  // can be useful to add further "special cases" that ToolClass and ItemTool constructor don't cover.
  @Override
  public float getDestroySpeed(ItemStack stack, BlockState iBlockState) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.getStrVsBlock",
                                               stack.getDisplayName() + ", " + iBlockState.toString());
    Float result = super.getDestroySpeed(stack, iBlockState);
    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.getStrVsBlock", String.valueOf(result));
    return result;
  }

// as of 10.1.2 this method no longer exists.  getStrVsBlock() is called directly instead.
//  // metadata / damage sensitive version of getStrVsBlock()
//  @Override
//  public float getDigSpeed(ItemStack stack, IBlockState state) {
//    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.getDigSpeed", stack.getDisplayName() + ", " + state);
//    Float result = super.getDigSpeed(stack, state);
//    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.getDigSpeed", String.valueOf(result));
//    return result;
//  }

  //   Item.onBlockStartBreak() - called immediately before the block is destroyed - can be used to abort block breaking before it is destroyed
  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.onBlockStartBreak", itemstack.getDisplayName() + ", " + pos + ", " + player.getName());
    Boolean result =  super.onBlockStartBreak(itemstack, pos, player);
    if (MinecraftByExample.proxy.playerIsInCreativeMode(player)) {
      final boolean PRINT_IN_CHAT_WINDOW = true;
      player.sendStatusMessage(new StringTextComponent("Currently in creative mode; switch to survival mode using /gamemode."),
              PRINT_IN_CHAT_WINDOW);
    }
    StartupCommon.methodCallLogger.exitMethod("ItemToolsTest.onBlockStartBreak", String.valueOf(result));
    return result;
  }

  @Override
  // damage the item when it destroys a block - defaults to 1 damage for tools
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState iBlockState, BlockPos pos, LivingEntity playerIn) {
    StartupCommon.methodCallLogger.enterMethod("ItemToolsTest.onBlockDestroyed",
            stack.getDisplayName() + ", {world}, " + iBlockState.toString() + ", "
                    + pos + ", " + playerIn.getName()
    );
    Boolean result = super.onBlockDestroyed(stack, worldIn, iBlockState, pos, playerIn);
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
