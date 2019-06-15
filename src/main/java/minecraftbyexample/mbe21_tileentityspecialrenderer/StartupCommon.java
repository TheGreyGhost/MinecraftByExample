package minecraftbyexample.mbe21_tileentityspecialrenderer;

import net.minecraft.item.ItemBlock;
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
  public static BlockMBE21 blockMBE21;  // this holds the unique instance of your block
  public static ItemBlock itemBlockMBE21; // this holds the unique instance of the itemblock corresponding to the block

  public static void preInitCommon()
  {
    // each instance of your block should have two names:
    // 1) a registry name that is used to uniquely identify this block.  Should be unique within your mod.  use lower case.
    // 2) an 'unlocalised name' that is used to retrieve the text name of your block in the player's language.  For example-
    //    the unlocalised name might be "water", which is printed on the user's screen as "Wasser" in German or
    //    "aqua" in Italian.
    //
    //    Multiple blocks can have the same unlocalised name - for example
    //  +----RegistryName----+---UnlocalisedName----+
    //  |  flowing_water     +       water          |
    //  |  stationary_water  +       water          |
    //  +--------------------+----------------------+
    //

    blockMBE21 = (BlockMBE21)(new BlockMBE21().setUnlocalizedName("mbe21_tesr_block_unlocalised_name"));
    blockMBE21.setRegistryName("minecraftbyexample:mbe21_tesr_block_registry_name");
    ForgeRegistries.BLOCKS.register(blockMBE21);

    // We also need to create and register an ItemBlock for this block otherwise it won't appear in the inventory
    itemBlockMBE21 = new ItemBlock(blockMBE21);
    itemBlockMBE21.setRegistryName(blockMBE21.getRegistryName());
    ForgeRegistries.ITEMS.register(itemBlockMBE21);

    // Each of your tile entities needs to be registered with a name that is unique to your mod.
    GameRegistry.registerTileEntity(TileEntityMBE21.class, "minecraftbyexample:mbe21_tesr_tile_entity");
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }

}
