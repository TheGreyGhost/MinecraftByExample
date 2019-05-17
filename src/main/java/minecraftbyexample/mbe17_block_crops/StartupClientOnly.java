package minecraftbyexample.mbe17_block_crops;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe17_item_blueberries", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockBlueberries, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
