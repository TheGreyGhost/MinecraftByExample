//package minecraftbyexample.usefultools.debugging;
//
//import com.TheRPGAdventurer.ROTD.DragonMounts;
//import com.TheRPGAdventurer.ROTD.common.entity.breeds.DragonBreedNew;
//import com.TheRPGAdventurer.ROTD.common.entity.physicalmodel.DragonVariants;
//import com.TheRPGAdventurer.ROTD.common.entity.physicalmodel.DragonVariantsException;
//import com.TheRPGAdventurer.ROTD.common.entity.physicalmodel.DragonVariantsReader;
//import net.minecraft.client.Minecraft;
//import net.minecraftforge.common.MinecraftForge;
//
//import java.util.Map;
//
///**
// * User: The Grey Ghost
// * Date: 24/12/2014
// * <p>
// * The Startup classes for this example are called during startup, in the following order:
// * preInitCommon
// * preInitClientOnly
// * initCommon
// * initClientOnly
// * postInitCommon
// * postInitClientOnly
// * See MinecraftByExample class for more information
// */
//public class StartupDebugCommon {
//  public static void preInitCommon() {
//    if (!DragonMounts.instance.getConfig().isDebug()) return;
//
//    final String TEST_EGGS_FOLDER = "testdata/testeggs";
//    DragonVariantsReader dragonVariantsReader = new DragonVariantsReader(Minecraft.getMinecraft().getResourceManager(), TEST_EGGS_FOLDER);
//    Map<String, DragonVariants> allEggVariants = dragonVariantsReader.readAllVariants();
//
//    for (Map.Entry<String, DragonVariants> entry : allEggVariants.entrySet()) {
//      try {
//        DragonBreedNew.DragonBreedsRegistry.getDefaultRegistry().createDragonBreedNew(entry.getKey(), entry.getValue());
//        entry.getValue().initialiseResourcesForCollection();
//      } catch (DragonVariantsException dve) {
//        DragonMounts.logger.error("One or more errors occurred while initialising the resources for test egg breed " + entry.getKey()
//                + ":\n" + dve.getMessage());
//      }
//    }
//  }
//
//  public static void initCommon() {
//    if (!DragonMounts.instance.getConfig().isDebug()) return;
//  }
//
//  public static void postInitCommon() {
//    if (!DragonMounts.instance.getConfig().isDebug()) return;
//    MinecraftForge.EVENT_BUS.register(new DebugSpawnInhibitor());
//  }
//}
