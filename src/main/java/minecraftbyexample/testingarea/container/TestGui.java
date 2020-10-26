//package minecraftbyexample.testingarea.container;
//
//import net.minecraft.client.gui.screen.inventory.ContainerScreen;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.util.text.ITextComponent;
//
///**
// * Created by TGG on 29/03/2020.
// */
//public class TestGui extends ContainerScreen<TestContainer> {
//  public TestGui(TestContainer container, PlayerInventory inv, ITextComponent name) {
//    super(container, inv, name);
//    this.testContainer = container;
//  }
//
//  @Override
//  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//    drawString(this.font, this.testContainer.getText(), mouseX, mouseY, -1);
//  }
//
//  private TestContainer testContainer;
//}
