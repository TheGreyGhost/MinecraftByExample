package minecraftbyexample.mbe03_block_variants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
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
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    Item itemBlockVariants = GameRegistry.findItem("minecraftbyexample", "mbe03_block_variants");

    // need to add the variants to the bakery so it knows what models are available for rendering the different subtypes
    //    In previous versions, this was required, however as of 1.8.9 ModelLoader.setCustomModelResourceLocation does this
//      for you - see later on - so it's not necessary.
//    ModelBakery.addVariantName(itemBlockVariants, "minecraftbyexample:mbe03_block_variants_blue",
//            "minecraftbyexample:mbe03_block_variants_green",
//            "minecraftbyexample:mbe03_block_variants_red",
//            "minecraftbyexample:mbe03_block_variants_yellow");

    // The code below is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.
    // It must be done on client only, and must be done after the block has been created in Common.preinit().

    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_blue", "inventory");
    ModelLoader.setCustomModelResourceLocation(itemBlockVariants, BlockVariants.EnumColour.BLUE.getMetadata(), itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_green", "inventory");
    ModelLoader.setCustomModelResourceLocation(itemBlockVariants, BlockVariants.EnumColour.GREEN.getMetadata(), itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_red", "inventory");
    ModelLoader.setCustomModelResourceLocation(itemBlockVariants, BlockVariants.EnumColour.RED.getMetadata(), itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants_yellow", "inventory");
    ModelLoader.setCustomModelResourceLocation(itemBlockVariants, BlockVariants.EnumColour.YELLOW.getMetadata(), itemModelResourceLocation);
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
