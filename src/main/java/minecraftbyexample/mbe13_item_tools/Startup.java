package minecraftbyexample.mbe13_item_tools;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Set;

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
  public static ItemToolsTest itemToolsTest;
  public static BlockToolTest blockToolTest;

  public static void preInitCommon()
  {
    blockToolTest = (BlockToolTest)(new BlockToolTest(Material.rock)).setUnlocalizedName("mbe13_item_tools_block");
    final float STONE_HARDNESS = 1.5F;
    blockToolTest.setHardness(STONE_HARDNESS);
    GameRegistry.registerBlock(blockToolTest, "mbe13_item_tools_block");

    // each instance of your item should have a name that is unique within your mod.  use lower case.

    final float ATTACK_DAMAGE = 1.0F;
    final Item.ToolMaterial TOOL_MATERIAL = Item.ToolMaterial.STONE;
    final Set EFFECTIVE_ON = Sets.newHashSet(new Block[]{blockToolTest, Blocks.diamond_block});
    itemToolsTest = (ItemToolsTest)(new ItemToolsTest(ATTACK_DAMAGE, TOOL_MATERIAL, EFFECTIVE_ON).setUnlocalizedName("mbe13_item_tools_item"));
    GameRegistry.registerItem(itemToolsTest, "mbe13_item_tools_item");

    final int WOOD_HARVEST_LEVEL = 0;
    final int STONE_HARVEST_LEVEL = 1;
    final int IRON_HARVEST_LEVEL = 2;
    final int DIAMOND_HARVEST_LEVEL = 3;

    itemToolsTest.setHarvestLevel("axe", IRON_HARVEST_LEVEL);
    itemToolsTest.setHarvestLevel("shovel", STONE_HARVEST_LEVEL);
    itemToolsTest.setHarvestLevel("pickaxe", DIAMOND_HARVEST_LEVEL);

    MinecraftForge.EVENT_BUS.register(new ForgeToolEventsTest());


    // set up logging of the various methods
//    shouldLogMap.put("Item.canHarvestBlock", true);
//    shouldLogMap.put("Item.getHarvestLevel", true);
//    shouldLogMap.put("Item.getDigSpeed", true);
//    shouldLogMap.put("Item.getStrVsBlock", true);
//    shouldLogMap.put("Item.onBlockStartBreak", true);
//    shouldLogMap.put("Item.onBlockDestroyed", true);
//    shouldLogMap.put("Block.getHarvestLevel", true);
//    shouldLogMap.put("Block.getHarvestTool", true);
//    shouldLogMap.put("Block.isToolEffective", true);
//    shouldLogMap.put("Block.getBlockHardness", true);
//    shouldLogMap.put("Block.getPlayerRelativeBlockHardness", true);
//    shouldLogMap.put("Event.BreakSpeed", true);
//    shouldLogMap.put("Event.HarvestCheck", true);
  }

  public static void preInitClientOnly()
  {
  }

  public static void initCommon()
  {
  }

  public static void initClientOnly()
  {
    Item blockToolsTestItem = GameRegistry.findItem("minecraftbyexample", "mbe13_item_tools_block");
    ModelResourceLocation itemBlockModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe13_item_tools_block", "inventory");
    final int DEFAULT_ITEM_SUBTYPE = 0;
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(blockToolsTestItem, DEFAULT_ITEM_SUBTYPE, itemBlockModelResourceLocation);

    ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:mbe13_item_tools_item", "inventory");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemToolsTest, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);


  }

  public static void postInitCommon()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
