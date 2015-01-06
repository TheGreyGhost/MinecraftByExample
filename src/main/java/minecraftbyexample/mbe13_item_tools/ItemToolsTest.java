package minecraftbyexample.mbe13_item_tools;

import minecraftbyexample.MinecraftByExample;
import minecraftbyexample.usefultools.MethodCallLogger;
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
    boolean result = super.canHarvestBlock(blockIn);
    if (MethodCallLogger.shouldLog("Item.canHarvestBlock")) {
      System.out.println("#ItemToolsTest.canHarvestBlock(" + blockIn.getLocalizedName() + "), result = " + result);
    }
    return result;
  }

  @Override
  public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
    boolean result =  super.canHarvestBlock(par1Block, itemStack);
    if (MethodCallLogger.shouldLog("Item.canHarvestBlock")) {
      System.out.println("#ItemToolsTest.canHarvestBlock(" + par1Block.getLocalizedName() + ", " + itemStack.getDisplayName() + " ), result = " + result);
    }return result;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {
    int result =  super.getHarvestLevel(stack, toolClass);
    if (MethodCallLogger.shouldLog("Item.getHarvestLevel")) {
      System.out.println("#ItemToolsTest.getHarvestLevel(" + stack.getDisplayName() + ", " + toolClass + "), result = " + result);
    }
    return result;
  }

  @Override
  public float getDigSpeed(ItemStack stack, IBlockState state) {
    float result =  super.getDigSpeed(stack, state);
    if (MethodCallLogger.shouldLog("Item.getDigSpeed")) {
      System.out.println("#ItemToolsTest.getDigSpeed(" + stack.getDisplayName() + ", " + state + "), result = " + result);
    }
    return result;
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    float result = super.getStrVsBlock(stack, block);
    if (MethodCallLogger.shouldLog("Item.getStrVsBlock")) {
      System.out.println("#ItemToolsTest.getStrVsBlock(" + stack.getDisplayName() + ", " + block.getLocalizedName() + "), result = " + result);
    }
    return result;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
    boolean result =  super.onBlockStartBreak(itemstack, pos, player);
    if (MinecraftByExample.proxy.playerIsInCreativeMode(player)) {
      player.addChatComponentMessage(new ChatComponentText("Currently in creative mode; switch to survival mode using /gamemode."));
    }
    if (MethodCallLogger.shouldLog("Item.onBlockStartBreak")) {
      System.out.println("#ItemToolsTest.onBlockStartBreak(" + itemstack.getDisplayName() + ", " + pos + ", " + player.getName() + "), result = " + result);
    }
    return result;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    boolean result =  super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
    if (MethodCallLogger.shouldLog("Item.onBlockDestroyed")) {
      System.out.println("#ItemToolsTest.onBlockDestroyed(" + stack.getDisplayName() + ", {world}, " + blockIn.getLocalizedName() + ", "
              + pos + ", " + playerIn.getName() +"), result = " + result);
    }
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
