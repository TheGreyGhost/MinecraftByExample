package minecraftbyexample.mbe03_block_variants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup class for this example is called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample for more information
 */
public class Startup
{
  public static BlockVariants blockVariants;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockVariants = (BlockVariants)(new BlockVariants().setUnlocalizedName("mbe03_block_variants"));
    GameRegistry.registerBlock(blockVariants, "mbe03_block_variants");
    // you don't need to register any items corresponding to the block, GameRegistry.registerBlock does this automatically.
    //  The resulting item(s) is an ItemBlock.
  }

  public static void preInitClientOnly()
  {

  }

  public static void initCommon()
  {

  }

  public static void initClientOnly()
  {
    // This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
    //   or in your hand or thrown on the ground).
    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
    //   of any extra items you have created.  Hence you have to do it manually.  This will probably change in future.
    // It must be done in the init phase, not preinit, and must be done on client only.
    Item itemBlockVariants = GameRegistry.findItem("minecraftbyexample", "mbe03_block_variants");
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe03_block_variants", "inventory");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockVariants, BlockVariants.EnumColour.BLUE.getMetadata(), itemModelResourceLocation);
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockVariants, BlockVariants.EnumColour.GREEN.getMetadata(), itemModelResourceLocation);
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockVariants, BlockVariants.EnumColour.RED.getMetadata(), itemModelResourceLocation);
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockVariants, BlockVariants.EnumColour.YELLOW.getMetadata(), itemModelResourceLocation);
  }

  public static void postInitCommon()
  {

  }

  public static void postInitClientOnly()
  {

  }

}
