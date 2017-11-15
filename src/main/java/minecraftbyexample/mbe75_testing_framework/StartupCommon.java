package minecraftbyexample.mbe75_testing_framework;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
  public static ItemTestRunner itemTestRunner;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemTestRunner = (ItemTestRunner)(new ItemTestRunner().setUnlocalizedName("mbe75_test_runner_unlocalised_name"));
    itemTestRunner.setRegistryName("mbe75_test_runner_registry_name");
    ForgeRegistries.ITEMS.register(itemTestRunner);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
