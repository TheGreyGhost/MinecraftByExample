package minecraftbyexample.mbe17_block_crops;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static minecraftbyexample.mbe17_block_crops.StartupCommon.itemBlueberries;

public class BlockBlueberries extends BlockCrops {
  public BlockBlueberries() {
    super();
  }

  @Override
  protected Item getSeed()
  {
    return itemBlueberries;
  }

  @Override
  protected Item getCrop()
  {
    return itemBlueberries;
  }

  // Optional
  @Override
  public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
  {
    super.getDrops(drops, world, pos, state, 0);
    int age = getAge(state);
    Random rand = world instanceof World ? ((World)world).rand : new Random();

    if (age >= getMaxAge())
    {
      int k = 3 + fortune;

      for (int i = 0; i < 3 + fortune; ++i)
      {
        if (rand.nextInt(2 * getMaxAge()) <= age)
        {
          drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
      }
    }
  }
}


