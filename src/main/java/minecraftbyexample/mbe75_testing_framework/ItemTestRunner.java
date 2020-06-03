package minecraftbyexample.mbe75_testing_framework;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
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
 *    where n is the stacksize - eg a stack of 10 test item will run test 10.
 */
public class ItemTestRunner extends Item
{
  static final int MAX_TEST_NUMBER = 64;
  public ItemTestRunner() {
    super(new Item.Properties().maxStackSize(MAX_TEST_NUMBER).group(ItemGroup.MISC));
  }

  /**
   * allows item to add custom lines of information to the mouseover description
   */
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    tooltip.add(new StringTextComponent("Right click: conduct test"));
    tooltip.add(new StringTextComponent("Stacksize: change test #"));
    tooltip.add(new StringTextComponent("  (64 = test all)"));
  }

  // what animation to use when the player holds the "use" button
  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK;
  }

  // how long the player needs to hold down the right button before the test runs again
  @Override
  public int getUseDuration(ItemStack stack) {
    return 20;
  }

  // called when the player starts holding right click;
  // called on the client and again on the server
  // execute your test code on the appropriate side....
  @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if (itemStackIn.isEmpty()) {  // returns true if the item is empty (player is holding nothing)
      return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);  // just in case.
    }
    int testNumber = itemStackIn.getCount(); // getStackSize()
    TestRunner testRunner = new TestRunner();

    if (worldIn.isRemote) {
      testRunner.runClientSideTest(worldIn, playerIn, testNumber);
    } else {
      testRunner.runServerSideTest(worldIn, playerIn, testNumber);
    }
    return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
  }

}
