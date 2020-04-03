//package minecraftbyexample.mbe30_inventory_basic;
//
//import minecraftbyexample.testingarea.container.TestContainer;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.inventory.Inventory;
//import net.minecraft.inventory.container.Container;
//import net.minecraft.inventory.container.INamedContainerProvider;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//
///**
// * Created by TGG on 3/04/2020.
// */
//public class BasicNamedContainerProvider implements INamedContainerProvider  {
//  private final String text;
//
//  public BasicNamedContainerProvider(String text) {
//    this.text = text;
//  }
//
//  @Override
//  public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
//    Inventory inv = new Inventory(9);
//    for (int i = 0; i < inv.getSizeInventory(); i++) {
//      inv.setInventorySlotContents(i, new ItemStack(Items.DIAMOND));
//    }
//    return new TestContainer(p_createMenu_1_, inv, text);
//  }
//
//  @Override
//  public ITextComponent getDisplayName() {
//    return new StringTextComponent("Test");
//  }
//}
