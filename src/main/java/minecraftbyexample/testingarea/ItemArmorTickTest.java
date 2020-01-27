package minecraftbyexample.testingarea;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by TGG on 16/11/2016.
 */
public class ItemArmorTickTest extends ArmorItem {

  public ItemArmorTickTest(ItemArmor.ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn)
  {
    super(materialIn, renderIndexIn, equipmentSlotIn);
  }

  public void onArmorTick(World world, PlayerEntity player, ItemStack itemStack) {
    System.out.println("onArmorTick" + (world.isRemote ? "client" : "server"));
  }

}
