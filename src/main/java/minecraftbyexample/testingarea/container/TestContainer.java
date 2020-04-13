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

  static LocationImage pointOne = ;
  static LocationImage pointTwo = ;
  static LocationImage pointThree = ;
  static LocationImage pointFour = ;
  static LocationImage pointFive = ;


  /**
   * This function is used to location an Easter egg given five reference images.
   * Requirements: three Persons satisfying age >= 10 years.
   * Person one and two must have good long-range vision.
   * Person three must have either good long-range vision or be capable of remote communication with both one and two.
   */
  public void navigateToEgg(Person one, Person two, Person three) {
    // initial setup
    one.navigateTo(pointOne);
    two.navitageTo(pointTwo);
    three.navigateTo(pointOne);
    one.lookAt(pointThree);
    two.lookAt(pointFour);

    // iterative goal search
    // all units in metres
    boolean atGoal = false;
    while (!atGoal) {
      int deviationOne = deviation(three, one.lineofSight());
      int deviationTwo = deviation(three, two.lineofSight());
      if (deviationOne < -1 ) {
        one.moveRight(1);
      } else if (deviationOne > 1) {
        one.moveLeft(1);
      }
      if (deviationTwo < -1 ) {
        two.moveRight(1);
      } else if (deviationTwo > 1) {
        two.moveLeft(1);
      }
      atGoal = (deviationOne >= -1 && deviationOne <= 1 && deviationTwo >= -1 && deviationTwo <= 1);
    }
    three.navigateTo(pointFive);
    three.dig();
  }

}
