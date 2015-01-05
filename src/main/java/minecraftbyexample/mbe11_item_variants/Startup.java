package minecraftbyexample.mbe11_item_variants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
  public static ItemVariants itemVariants;  // this holds the unique instance of your block

  public static void preInitCommon()
  {
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemVariants = (ItemVariants)(new ItemVariants().setUnlocalizedName("mbe11_item_variants"));
    GameRegistry.registerItem(itemVariants, "mbe11_item_variants");
    // need to add the variants to the bakery so it knows what models are available for rendering the different subtypes
    ModelBakery.addVariantName(itemVariants,  "minecraftbyexample:mbe11_item_variants_0pc",
                                              "minecraftbyexample:mbe11_item_variants_25pc",
                                              "minecraftbyexample:mbe11_item_variants_50pc",
                                              "minecraftbyexample:mbe11_item_variants_75pc",
                                              "minecraftbyexample:mbe11_item_variants_100pc"
                                             );
  }

  public static void preInitClientOnly()
  {
  }

  public static void initCommon()
  {
  }

  public static void initClientOnly()
  {
    // required in order for the renderer to know how to render your item.  Likely to change in the near future.
    // in this case we have five different model files, but we still need to register all 4 * 5 = 20 different metadata values
    // Alternative ways of doing this are:
    // 1) store some of the information in itemstack NBT information instead; or
    // 2) register your item to ignore all metadata, then alter your model render based on the metadata (see commented-out code below)
    //
    for (ItemVariants.EnumBottleFullness fullness : ItemVariants.EnumBottleFullness.values()) {
      String itemModelName = "mbe11_item_variants_" + fullness.getName();
      ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("minecraftbyexample:" + itemModelName, "inventory");
      // --> eg minecraftbyexample:mbe11_item_variants_0pc, mbe11_item_variants_25pc, etc
      final int fullnessBits = fullness.getMetadata();
      for (ItemVariants.EnumBottleContents contents : ItemVariants.EnumBottleContents.values()) {
        final int contentsBits = contents.getMetadata();
        int metadata = contentsBits | (fullnessBits << 2);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemVariants, metadata, itemModelResourceLocation);
      }
    }

    // if your item has subtypes but you want the registry to ignore metadata, use the method below instead
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemVariants, new ItemMeshDefinition()
//    {
//      public ModelResourceLocation getModelLocation(ItemStack stack) {
//        return new ModelResourceLocation("spawn_egg", "inventory");
//      }
//    });

  }

  public static void postInitCommon()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
