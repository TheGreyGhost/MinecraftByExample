package minecraftbyexample.mbe16_item_food;

import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class StartupCommon
{
  public static ItemSandwich itemSandwich;

  public static void preInitCommon()
  {
    itemSandwich = (ItemSandwich)(new ItemSandwich().setUnlocalizedName("mbe16_item_food_unlocalised_name"));
    itemSandwich.setRegistryName("mbe16_item_food_registry_name");
    ForgeRegistries.ITEMS.register(itemSandwich);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
