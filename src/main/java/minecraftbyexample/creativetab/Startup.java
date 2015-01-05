package minecraftbyexample.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Nephroid
 * 
 * User: Nephroid
 * Date: December 26, 2014
 */
public class Startup {
  /* This piece of code declares a static variable that stores a custom creative tab
   * with the unlocalized name "gold_nugget_tab"
   * 
   * To set the actual name of the tab, you have to go into your .lang file and add the line
   * 
   *   itemGroup.gold_nugget_tab=Gold Nugget Tab
   * 
   * where gold_nugget_tab is the unlocalized name of your creative tab, and Gold Nugget Tab
   * is the actual name of your tab
   * 
   * 
   * Since CreativeTabs is an abstract class, we have to implement its abstract methods.
   * Since we don't have a lot of code associated with the creative tab (there's only 1 method),
   * we implement the abstract method using an anonymous class.
   * 
   * For more information about anonymous classes, see this link:
   * http://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
   * 
   * The method that we need to implement is getTabIconItem(), which specifies the icon to
   * use for the creative tab.
   * 
   * If you want to see an example of extending a class, look at MyCreativeTab.java
   */
  public static CreativeTabs anonymousTab = new CreativeTabs("gold_nugget_tab") {
    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
      return Items.gold_nugget;
    }
  };
  
  
  
  /* This piece of code declares a static variable that stores a custom creative tab
   * with the unlocalized name "prefix_gold_block_tab"
   * 
   * See MyCreativeTab.java for more information
   */
  public static MyCreativeTab myTab = new MyCreativeTab("gold_block_tab");
  
  
  
  /* The two lines below are variables containing the dummy block and items that are
   * going to be added to the creative tabs. Note that each block and item can only have
   * one creative tab associated with it.
   * 
   * Note that there is no need to explicitly register the creative tabs themselves. When
   * you make a new CreativeTabs object, it gets added automatically in the constructor of
   * the CreativeTabs class.
   */
  public static DummyBlock dummyBlock = new DummyBlock();
  public static DummyItem dummyItem = new DummyItem();
  
  public static void preInitCommon() {
    GameRegistry.registerBlock(dummyBlock, "dummy_block");
    GameRegistry.registerItem(dummyItem, "dummy_item");
  }
  
  public static void preInitClientOnly() {

  }
  
  public static void initCommon() {
    
  }
  
  public static void initClientOnly() {
    
  }
  
  public static void postInitCommon() {

  }

  public static void postInitClientOnly() {

  }
}
