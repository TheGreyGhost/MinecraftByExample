package minecraftbyexample.mbe16_item_food;

import net.minecraft.item.ItemFood;
// to give potion effects:
// import net.minecraft.entity.player.EntityPlayer;
// import net.minecraft.init.MobEffects;
// import net.minecraft.item.ItemStack;
// import net.minecraft.potion.PotionEffect;
// import net.minecraft.world.World;
// import net.minecraftforge.fml.relauncher.Side;
// import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSandwich extends ItemFood {
  public ItemSandwich() {
    //int amount, float saturation, boolean isWolfFood
    super(1, 1, false);
    setAlwaysEdible();
  }

  // to give potion effects:
  // @Override
  // protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
  //   if(!worldIn.isRemote) {
  //     player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60*20, 5, false, true));
  //   }
  // }
  
  // @SideOnly(Side.CLIENT)
  // public boolean hasEffect(ItemStack stack)
  // {
  //   return true;
  // }
}
