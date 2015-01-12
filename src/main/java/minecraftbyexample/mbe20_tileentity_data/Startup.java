package minecraftbyexample.mbe20_tileentity_data;

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
 *  See MinecraftByExample class for more information
 */
public class Startup
{
  public static BlockTileEntityData blockTileEntityData;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your block should have a name that is unique within your mod.  use lower case.
    blockTileEntityData = (BlockTileEntityData)(new BlockTileEntityData().setUnlocalizedName("mbe20_tileentity_data_block"));
    GameRegistry.registerBlock(blockTileEntityData, "mbe20_tileentity_data_block");
    // you don't need to register an item corresponding to the block, GameRegistry.registerBlock does this automatically.
    GameRegistry.registerTileEntity(TileEntityData.class, "mbe20_tileentity_data_te");
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
    Item itemBlockSimple = GameRegistry.findItem("minecraftbyexample", "mbe20_tileentity_data_block");
    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe20_tileentity_data_block", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void postInitCommon()
  {
  }

  public static void postInitClientOnly()
  {
  }

}
