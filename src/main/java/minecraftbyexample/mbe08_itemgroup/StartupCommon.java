package minecraftbyexample.mbe08_itemgroup;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.item.SwordItem;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemGroup customItemGroup;                        // will hold our first custom ItemGroup
  public static AllMbeItemsItemGroup allMbeItemsItemGroup;        // will hold our second custom ItemGroup displaying all MBE items

  public static Block testBlock;
  public static BlockItem testItemBlock;  // the itemBlock corresponding to testBlock, just for something to put into the ItemGroup
  public static Item testItem;            // a simple item to place into the ItemGroup

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {

//  The lines below create a test block and item instance that are going to be added to the creative tabs.
//  An item can be listed on multiple tabs by overriding Item.getCreativeTabs()
//  A block can only be listed on one tab, unless you give it a custom ItemBlock which overrides .getCreativeTabs()
    Block.Properties properties = Block.Properties.create(Material.ROCK);
    testBlock = new Block(properties);
    testBlock.setRegistryName("mbe08_itemgroup_block_registry_name");
    blockRegisterEvent.getRegistry().register(testBlock);
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {

    // we create two new ItemGroups:
    //  - a simple one called mbe08_item_group which is used like normal vanilla ItemGroups
    //  - a more complicated one called mbe08_item_group_all_MBE which lists all mbe items and blocks


    //   Because the CreativeTabs class only has one abstract method, we can easily use an anonymous class.
//   For more information about anonymous classes, see this link:
//   http://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
//   To set the actual name of the tab, you have to go into your .lang file and add the line
//    itemGroup.mbe08_creative_tab=Gold Nugget Tab
//   where gold_nugget_tab is the unlocalized name of your creative tab, and Gold Nugget Tab
//   is the actual name of your tab
    /*
     * Note that there is no need to explicitly register the ItemGroups themselves. When
     * you make a new ItemGroups object, it gets added automatically in the constructor of
     * the ItemGroups class.
     */
    customItemGroup = new ItemGroup("mbe08_item_group") {
      @Override
      public ItemStack createIcon() {
        return new ItemStack(Items.GOLD_NUGGET);
      }
    };

    allMbeItemsItemGroup = new AllMbeItemsItemGroup("mbe08_item_group_all_MBE");

    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE_TESTBLOCK = 64;  // player can hold 64 of this block in their hand at once
    Item.Properties properties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE_TESTBLOCK)
            .group(customItemGroup);  // which ItemGroup is this located on?

    // register the itemblock corresponding to the block
    testItemBlock = new BlockItem(testBlock, properties);
    testItemBlock.setRegistryName(testBlock.getRegistryName());
    itemRegisterEvent.getRegistry().register(testItemBlock);

    final int MAXIMUM_STACK_SIZE_TESTITEM = 1;  // player can only hold 1 of this block in their hand at once
    properties = new Item.Properties().maxStackSize(MAXIMUM_STACK_SIZE_TESTITEM).group(customItemGroup);

    final int ATTACK_DAMAGE = 30;
    final float ATTACK_SPEED = -2.4F;
    // add an item (an item without a corresponding block)
    testItem = new SwordItem(ItemTier.GOLD, ATTACK_DAMAGE, ATTACK_SPEED, properties);
    testItem.setRegistryName("mbe08_itemgroup_item_registry_name");
    itemRegisterEvent.getRegistry().register(testItem);
  }
}
