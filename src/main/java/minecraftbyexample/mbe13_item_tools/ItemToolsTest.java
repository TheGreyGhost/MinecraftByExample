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
 * ItemToolsTest is an ordinary two-dimensional item used to test the interaction between tools and blocks
 */
public class ItemToolsTest extends ItemTool
{

  public ItemToolsTest(float attackDamage, ToolMaterial material, Set effectiveBlocks) {
    super(attackDamage, material, effectiveBlocks);
    this.setCreativeTab(CreativeTabs.tabMisc);   // the item will appear on the Miscellaneous tab in creative
  }

  @Override
  public boolean canHarvestBlock(Block blockIn) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.canHarvestBlock", blockIn.getLocalizedName());
    Boolean result = super.canHarvestBlock(blockIn);
    Startup.methodCallLogger.exitMethod("ItemToolsTest.canHarvestBlock", result.toString());
    return result;
  }

  @Override
  public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.canHarvestBlock", par1Block.getLocalizedName() + ", " + itemStack.getDisplayName());
    Boolean result = super.canHarvestBlock(par1Block, itemStack);
    Startup.methodCallLogger.exitMethod("ItemToolsTest.canHarvestBlock", result.toString());
    return result;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.getHarvestLevel", stack.getDisplayName() + ", " + toolClass);
    Integer result = super.getHarvestLevel(stack, toolClass);
    Startup.methodCallLogger.exitMethod("ItemToolsTest.getHarvestLevel", result.toString());
    return result;
  }

  @Override
  public float getDigSpeed(ItemStack stack, IBlockState state) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.getDigSpeed", stack.getDisplayName() + ", " + state);
    Float result = super.getDigSpeed(stack, state);
    Startup.methodCallLogger.exitMethod("ItemToolsTest.getDigSpeed", result.toString());
    return result;
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.getStrVsBlock", stack.getDisplayName() + ", " + block.getLocalizedName());
    Float result = super.getStrVsBlock(stack, block);
    Startup.methodCallLogger.exitMethod("ItemToolsTest.getStrVsBlock", result.toString());
    return result;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.onBlockStartBreak", itemstack.getDisplayName() + ", " + pos + ", " + player.getName());
    Boolean result =  super.onBlockStartBreak(itemstack, pos, player);
    if (MinecraftByExample.proxy.playerIsInCreativeMode(player)) {
      player.addChatComponentMessage(new ChatComponentText("Currently in creative mode; switch to survival mode using /gamemode."));
    }
    Startup.methodCallLogger.exitMethod("ItemToolsTest.onBlockStartBreak", result.toString());
    return result;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    Startup.methodCallLogger.enterMethod("ItemToolsTest.onBlockDestroyed",
                                         stack.getDisplayName() + ", {world}, " + blockIn.getLocalizedName() + ", "
                                         + pos + ", " + playerIn.getName()
                                         );
    Boolean result = super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
    Startup.methodCallLogger.exitMethod("ItemToolsTest.onBlockDestroyed", result.toString());
    return result;
  }

  /*
  canHarvestBlock
  getHarvestLevel / setHarvestLevel
        getToolClasses
  getDigSpeed
          getStrVsBlock
  onBlockStartBreak (cancellable)

  ItemTool
  Item.ToolMaterial
  ItemTool(effectiveBlocks)

  -the tool constructor provides a set of blocks that it is "effective on".
        -when a tool is effective on a block, it operates with efficiency according to its material, otherwise "1.0F"

  ItemStack
  "CanDestroy" tag (vanilla doesn't use?) in ItemStack NBT  (adventure mode)
*/


}
