package minecraftbyexample.mbe75_testing_framework;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 04/01/2016
 *
 * ItemTestRunner is used to trigger a test case
 * Usage:
 * 1) register as an item
 * 2) when the user right clicks while holding the item, the item will call
 *    TestRunner.runClientSideTest(n) and TestRunner.runServerSideTest(n)
 *    where n is the stacksize - eg a stack of 10 test items will run test 10.
 */
public class ItemTestRunner extends Item
{
  public ItemTestRunner()
  {
    final int MAX_TEST_NUMBER = 64;
    this.setMaxStackSize(MAX_TEST_NUMBER);
    this.setCreativeTab(CreativeTabs.MISC);   // the item will appear on the Miscellaneous tab in creative
  }

  /**
   * allows items to add custom lines of information to the mouseover description
   */
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List textList, boolean useAdvancedItemTooltips)
  {
    textList.add("Right click: conduct test");
    textList.add("Stacksize: change test #");
    textList.add("  (64 = test all)");
  }

  // what animation to use when the player holds the "use" button
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }

  // how long the player needs to hold down the right button before the test runs again
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 20;
  }

  // called when the player starts holding right click;
  // called on the client and again on the server
  // execute your test code on the appropriate side....
  @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if (itemStackIn.isEmpty()) {  // returns true if the item is empty (player is holding nothing)
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);  // just in case.
    }
    int testNumber = itemStackIn.getCount(); // getStackSize()
    TestRunner testRunner = new TestRunner();

    if (worldIn.isRemote) {
      testRunner.runClientSideTest(worldIn, playerIn, testNumber);
    } else {
      testRunner.runServerSideTest(worldIn, playerIn, testNumber);
    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
  }

}
