package minecraftbyexample.testingarea.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

/**
 * Created by TGG on 29/03/2020.
 */
public class TestContainer extends Container {
  public String getText() {
    return text;
  }

  private final String text;

  protected TestContainer(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
    this(windowId, new Inventory(9), extraData.readString(128));
  }

  public TestContainer(int windowId, Inventory inv, String text) {
    super(ContainerTypeTest.TYPE, windowId);
    this.text = text;
    for (int i = 0; i < 9; i++) {
      this.addSlot(new Slot(inv, i, (i % 3) * 18, (i / 3) * 18));
    }
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }


}
