package minecraftbyexample.mbe03_block_variants;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

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
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    // This step is necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // It must be done on client only, and must be done after the block has been created in Common.preinit().
    // In this case, there are four variants of items (one for each colour) that need to be need to be covered.

    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_blue", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockVariants,
                                               BlockVariants.EnumColour.BLUE.getMetadata(), itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_green", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockVariants,
                                               BlockVariants.EnumColour.GREEN.getMetadata(), itemModelResourceLocation);


    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_red", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockVariants,
                                               BlockVariants.EnumColour.RED.getMetadata(), itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_yellow", "inventory");
    ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockVariants,
                                               BlockVariants.EnumColour.YELLOW.getMetadata(), itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
