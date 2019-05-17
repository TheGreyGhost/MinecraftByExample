package minecraftbyexample.mbe09_ore_spawning;

import minecraftbyexample.mbe08_creative_tab.AllMbeItemsTab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHardenedClay;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{

  public static void preInitCommon()
  {
    // 0 runs first; higher numbers run later
    GameRegistry.registerWorldGenerator(new OreSpawner(), 0);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
