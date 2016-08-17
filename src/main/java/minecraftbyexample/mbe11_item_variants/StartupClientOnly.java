package minecraftbyexample.mbe11_item_variants;

import net.minecraft.client.Minecraft;
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
//    need to add the variants to the bakery so it knows what models are available for rendering the different subtypes
//    In previous versions, this was required, however as of 1.8.9 ModelLoader.setCustomModelResourceLocation does this
//      for you - see later on - so it's not necessary.
//    ModelBakery.addVariantName(StartupCommon.itemVariants,  "minecraftbyexample:mbe11_item_variants_0pc",
//            "minecraftbyexample:mbe11_item_variants_25pc",
//            "minecraftbyexample:mbe11_item_variants_50pc",
//            "minecraftbyexample:mbe11_item_variants_75pc",
//            "minecraftbyexample:mbe11_item_variants_100pc"
//    );

    // required in order for the renderer to know how to render your item.
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
        ModelLoader.setCustomModelResourceLocation(StartupCommon.itemVariants, metadata, itemModelResourceLocation);
      }
    }

    // if your item has subtypes but you want the registry to ignore metadata, use the method below instead
//    ModelLoader.setCustomMeshDefinition(itemVariants, new ItemMeshDefinition()
//    {
//      public ModelResourceLocation getModelLocation(ItemStack stack) {
//        return new ModelResourceLocation("spawn_egg", "inventory");
//      }
//    });
  }

 public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
    // the LiquidColour class is used to change the rendering colour of the liquid in the bottle

    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new LiquidColour(), StartupCommon.itemVariants);
  }
}
