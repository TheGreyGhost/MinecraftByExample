//package minecraftbyexample.usefultools.debugging;
//
//import com.TheRPGAdventurer.ROTD.DragonMounts;
//import com.TheRPGAdventurer.ROTD.common.entity.helper.DragonLifeStageHelper;
//import com.TheRPGAdventurer.ROTD.common.entity.physicalmodel.*;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufAllocator;
//import net.minecraft.client.Minecraft;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.network.PacketBuffer;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
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
//public class StartupDebugClientOnly {
//  public static void preInitClientOnly() {
//    if (!DragonMounts.instance.getConfig().isDebug()) return;
////    testModifiedCategory();
//    testDragonVariantsReader();
////    testDragonLifeStageHelperTags();
////    testModifiers();
//  }
//
//  public static void initClientOnly() {
//    if (!DragonMounts.instance.getConfig().isDebug()) return;
//  }
//
//  public static void postInitClientOnly() {
//    if (!DragonMounts.instance.getConfig().isDebug()) return;
//  }
//
//  static DragonVariantTag TESTTAG1;
//  static DragonVariantTag TESTTAG2;
//  static DragonVariantTag TESTTAG3;
//  static DragonVariantTag TESTTAG4;
//  static DragonVariantTag TESTTAG5;
//
//  public static void testDragonVariantsReader() {
//
//    TESTTAG1 = DragonVariantTag.addTag("testtag1", "one", "testtag1").categories(DragonVariants.Category.EGG).values("one", "two", "three");
//    TESTTAG2 = DragonVariantTag.addTag("testtag2flag", false, "testtag2 flag").categories(DragonVariants.Category.EGG);
//    TESTTAG3 = DragonVariantTag.addTag("testtag3", 2.0, "testtag3 number").categories(DragonVariants.Category.EGG);
//    String [] defaultArray = {"one", "two", "three"};
//    TESTTAG4 = DragonVariantTag.addTag("testtag4", defaultArray, "testtag4 array").categories(DragonVariants.Category.EGG);
//    TESTTAG5 = DragonVariantTag.addTag("testtag5", defaultArray, "testtag4 array").categories(DragonVariants.Category.EGG).values("one", "two", "three");
//
//    DragonVariants.addVariantTagValidator(new TestValidator());
//
//    final String TEST_FOLDER = "testdata/testdvr1";
//
//    DragonMounts.logger.info("DragonVariantsReader Test1" + TEST_FOLDER);
//    DragonVariantsReader dragonVariantsReader = new DragonVariantsReader(
//            Minecraft.getMinecraft().getResourceManager(), TEST_FOLDER);
//    Map<String, DragonVariants> allVariants = dragonVariantsReader.readAllVariants();
//    int i = 1; // put breakpoint here and inspect allVariants; compare to the json file, which contain comments
//    for (DragonVariants dragonVariants : allVariants.values()) {
//      String json = DragonVariantsReader.outputAsJSON(dragonVariants, true);
//      System.out.print(json);
//    }
//
//    String json = DragonVariantsReader.outputAllTagsAsJSON(true);
//    System.out.print(json);
//    json = DragonVariantsReader.outputAllTagsAsJSON(false);
//    System.out.print(json);
//  }
//
//  public static class TestValidator implements DragonVariants.VariantTagValidator {
//    @Override
//    public void validateVariantTags(DragonVariants dragonVariants, DragonVariants.ModifiedCategory modifiedCategory) throws IllegalArgumentException {
//      if (!modifiedCategory.getCategory().equals(DragonVariants.Category.EGG)) return;
//      DragonVariantsException.DragonVariantsErrors errors = new DragonVariantsException.DragonVariantsErrors();
//      DragonVariants.DragonVariantsCategoryShortcut dvc = dragonVariants.new DragonVariantsCategoryShortcut(modifiedCategory);
//      dvc.checkForConflict(errors, TESTTAG2, false, true, TESTTAG3);  // 2 isn't defined and 3 is .
//      dvc.checkForConflict(errors, TESTTAG1, "two", true, TESTTAG3);  // 1 is "two" and 3 is defined
//      dvc.checkForConflict(errors, TESTTAG2, true, true, TESTTAG1);  // 2 is defined and 1 is also defined
//      dvc.checkForConflict(errors, TESTTAG1, "three", false, TESTTAG3);  // 1 is "one" and 3 isn't defined
//      if (errors.hasErrors()) {
//        throw new DragonVariantsException(errors);
//      }
//    }
//
//    @Override
//    public void initaliseResources(DragonVariants dragonVariants, DragonVariants.ModifiedCategory modifiedCategory) throws IllegalArgumentException {
//
//    }
//  }
//
//  public static void testDragonLifeStageHelperTags() {
//    DragonLifeStageHelper.registerConfigurationTags();
//    //    String testfiles[] = {"testdata/testdlsv1.json", "testdata/testdlsv2.json", "testdata/testdlsv3.json", "testdata/testdlsv4.json",
//    //            "testdata/testdlsv5.json", "testdata/testdlsv6.json"};
//    final String TEST_FOLDER = "testdata/testdlsv1";
//
//    //    for (String filename : testfiles) {
//    {
//      DragonMounts.logger.info("testDragonLifeStageHelperTags Test1");
//      DragonVariantsReader dragonVariantsReader = new DragonVariantsReader(
//              Minecraft.getMinecraft().getResourceManager(), TEST_FOLDER);
//      Map<String, DragonVariants> allVariants = dragonVariantsReader.readAllVariants();
//      int i = 1; // put breakpoint here and inspect allVariants; compare to the json files, which contain comments
//      for (DragonVariants dragonVariants : allVariants.values()) {
//        String json = DragonVariantsReader.outputAsJSON(dragonVariants, true);
//        System.out.print(json);
//      }
//    }
//
//    DragonVariantsReader dragonVariantsReader = new DragonVariantsReader(
//            Minecraft.getMinecraft().getResourceManager(), "testdata/testdlsv2");
//    Map<String, DragonVariants> allVariants = dragonVariantsReader.readAllVariants();
//    DragonLifeStageHelper.testClass(allVariants.get("testdlsv7"), 0);
//    DragonLifeStageHelper.testClass(allVariants.get("testdlsv7"), 1);
//    //    DragonLifeStageHelper.testClass(allVariants.get("fire"), 2);    // prints the default curves to console
//  }
//
//  public static void testModifiedCategory() {
//
////    tests for modifiers:
////    * list of valid modifiers
////    * get from text - valid and invalid
//    List<DragonVariants.Modifier> allmodifiers = DragonVariants.Modifier.getAllModifiers();
//    for (DragonVariants.Modifier mod : allmodifiers) {
//      DragonVariants.Modifier backMod = DragonVariants.Modifier.getModifierFromText(mod.getTextname());
//      System.out.println("From: " + mod + " to " + backMod);
//    }
//
//    try {
//      DragonVariants.Modifier backMod = DragonVariants.Modifier.getModifierFromText("badname");
//      System.out.println("Test1 failed");
//    } catch (IllegalArgumentException iae) {
//      System.out.println("Caught exception: " + iae.getMessage());
//    }
//
////    ModifiedCategory:
////    - check for attempt to apply mutex
////    - parse from string (no modifier, one modifier, two modifiers, unknown category, unknown modifier, mutex modifier)
////    - test for equals
//    DragonVariants.ModifiedCategory [] mc = new DragonVariants.ModifiedCategory[10];
//    mc[1] = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG);
//    mc[2] = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.MALE);
//    mc[3] = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.FEMALE);
//    mc[4] = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.DEBUG1);
//    mc[5] = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.DEBUG2);
//    mc[6] = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.FEMALE, DragonVariants.Modifier.DEBUG2);
//    DragonVariants.ModifiedCategory mc7;
//    try {
//      mc7 = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.MALE, DragonVariants.Modifier.FEMALE);
//      System.out.println("Test2 failed");
//    } catch (IllegalArgumentException iae) {
//      System.out.println("Caught exception: " + iae.getMessage());
//    }
//    DragonVariants.ModifiedCategory [] mct = new DragonVariants.ModifiedCategory[10];
//    mct[1] = DragonVariants.ModifiedCategory.parseFromString("egg");
//    mct[2] = DragonVariants.ModifiedCategory.parseFromString("egg:male");
//    mct[3] = DragonVariants.ModifiedCategory.parseFromString("egg:female");
//    mct[4] = DragonVariants.ModifiedCategory.parseFromString("egg:debug1");
//    mct[5] = DragonVariants.ModifiedCategory.parseFromString("egg:debug2");
//    mct[6] = DragonVariants.ModifiedCategory.parseFromString("egg: debug2  , female ");
//    mct[7] = DragonVariants.ModifiedCategory.parseFromString("egg:female, debug2");
//    mct[8] = DragonVariants.ModifiedCategory.parseFromString("physicalmodel:male, debug2");
//    try {
//      mc7 = DragonVariants.ModifiedCategory.parseFromString("egg:juice");
//      System.out.println("Test3 failed");
//    } catch (IllegalArgumentException iae) {
//      System.out.println("Caught exception: " + iae.getMessage());
//    }
//    try {
//      mc7 = DragonVariants.ModifiedCategory.parseFromString("pig:male");
//      System.out.println("Test4 failed");
//    } catch (IllegalArgumentException iae) {
//      System.out.println("Caught exception: " + iae.getMessage());
//    }
//    try {
//      mc7 = DragonVariants.ModifiedCategory.parseFromString("egg:male, female");
//      System.out.println("Test3 failed");
//    } catch (IllegalArgumentException iae) {
//      System.out.println("Caught exception: " + iae.getMessage());
//    }
//
//    System.out.println("expect true:" + mct[6].equals(mct[7]));
//    System.out.println("expect false:" + mct[8].equals(mct[7]));
//    for (int i = 1; i <= 6; ++i) {
//      for (int j = 1; j <= 6; ++j) {
//        boolean equals = mc[i].equals(mct[j]);
//        if (equals != (i == j)) {
//          System.out.println("equals failure at i,j = " + i + "," + j);
//        }
//      }
//    }
//
////    ModifiedCategoryRanker:
////    - test for correct rankings:
////    - target is empty:
////            - one mc  is empty the other isn't
////            - both mc are empty
//
//    DragonVariants.ModifiedCategory target1 = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG);
//    DragonVariants.ModifiedCategoryRanker ranker1 = new DragonVariants.ModifiedCategoryRanker(target1);
//    if (ranker1.compare(mc[1], mc[2]) >= 0 || ranker1.compare(mc[2], mc[1]) <= 0) System.out.println("Test fail:10");
//
//    mc[9] = DragonVariants.ModifiedCategory.parseFromString("egg");
//    if (ranker1.compare(mc[1], mc[9]) != 0 || ranker1.compare(mc[9], mc[1]) != 0) System.out.println("Test fail:11");
//    //---------
////    - target has one modifier:
////            - one mc has the same modifier, the other a different
////            - one mc has no modifiers, the other has the wrong one
////            - both mc have one modifier, which matches
////            - one mc has no modifiers, the other has two, one of which is correct
////            - both mc have no modifiers
//    target1 = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.FEMALE);
//    ranker1 = new DragonVariants.ModifiedCategoryRanker(target1);
//    if (ranker1.compare(mc[3], mc[2]) >= 0 || ranker1.compare(mc[2], mc[3]) <= 0) System.out.println("Test fail:12");
//    if (ranker1.compare(mc[1], mc[2]) >= 0 || ranker1.compare(mc[2], mc[1]) <= 0) System.out.println("Test fail:13");
//
//    mc[0] = DragonVariants.ModifiedCategory.parseFromString("egg:female");
//    if (ranker1.compare(mc[0], mc[3]) != 0 || ranker1.compare(mc[3], mc[0]) != 0) System.out.println("Test fail:14");
//    if (ranker1.compare(mc[1], mc[6]) >= 0 || ranker1.compare(mc[6], mc[1]) <= 0) System.out.println("Test fail:15");
//    if (ranker1.compare(mct[1], mc[1]) != 0 || ranker1.compare(mc[1], mct[1]) != 0) System.out.println("Test fail:16");
//    //-------------
////    - target has two modifiers:
////            - one mc has one of the modifiers, the other has the other
////            - one mc has one of the modifiers, the other has both
////            - one mc has one of the modifiers, the other has one which is wrong
////            - one mc has one of the modifiers, the other has two, one of which is wrong
//    target1 = new DragonVariants.ModifiedCategory(DragonVariants.Category.EGG, DragonVariants.Modifier.FEMALE, DragonVariants.Modifier.DEBUG2);
//    ranker1 = new DragonVariants.ModifiedCategoryRanker(target1);
//    if (ranker1.compare(mc[3], mc[5]) >= 0 || ranker1.compare(mc[5], mc[3]) <= 0) System.out.println("Test fail:17");
//    if (ranker1.compare(mc[6], mc[3]) >= 0 || ranker1.compare(mc[3], mc[6]) <= 0) System.out.println("Test fail:18");
//    if (ranker1.compare(mc[3], mc[1]) >= 0 || ranker1.compare(mc[1], mc[3]) <= 0) System.out.println("Test fail:19");
//    mct[0] = DragonVariants.ModifiedCategory.parseFromString("egg:female, debug1");
//    if (ranker1.compare(mc[5], mct[0]) >= 0 || ranker1.compare(mct[0], mc[5]) <= 0) System.out.println("Test fail:20");
//  }
//
//  public static void testModifiers() {
//    // tests:
//    // 1) add and remove modifiers; compare to getModifierList at each step
//    // 2) writeToNBT and getStateFromNBT at each step
//    // 3) Check DataSerializer read and write using a PacketBuffer at each step
//
//    for (int i1 = 0; i1 <= 3; ++i1) {
//      for (int i2 = 0; i2 <= 3; ++i2) {
//        for (int i3 = 0; i3 <= 3; ++i3) {
//          for (int i4 = 0; i4 <= 3; ++i4) {
//            List<DragonVariants.Modifier> modArray = new ArrayList<>();
//            Modifiers modifiers = new Modifiers();
//            if ((i1 & 1) != 0) {modifiers.add(DragonVariants.Modifier.MALE); modArray.add(DragonVariants.Modifier.MALE); }
//            if ((i1 & 2) != 0) {modifiers.remove(DragonVariants.Modifier.MALE); modArray.remove(DragonVariants.Modifier.MALE); }
//            if ((i2 & 1) != 0) {modifiers.add(DragonVariants.Modifier.FEMALE); modArray.add(DragonVariants.Modifier.FEMALE);
//                                modifiers.remove(DragonVariants.Modifier.MALE); modArray.remove(DragonVariants.Modifier.MALE);
//                               }
//            if ((i2 & 2) != 0) {modifiers.remove(DragonVariants.Modifier.FEMALE); modArray.remove(DragonVariants.Modifier.FEMALE); }
//            if ((i3 & 1) != 0) {modifiers.add(DragonVariants.Modifier.DEBUG1); modArray.add(DragonVariants.Modifier.DEBUG1); }
//            if ((i3 & 2) != 0) {modifiers.remove(DragonVariants.Modifier.DEBUG1); modArray.remove(DragonVariants.Modifier.DEBUG1); }
//            if ((i4 & 1) != 0) {modifiers.add(DragonVariants.Modifier.DEBUG2); modArray.add(DragonVariants.Modifier.DEBUG2); }
//            if ((i4 & 2) != 0) {modifiers.remove(DragonVariants.Modifier.DEBUG2); modArray.remove(DragonVariants.Modifier.DEBUG2); }
//            List<DragonVariants.Modifier> modArray1 = modifiers.getModifierListIncludingDebugs();
//            modArray.sort(null);
//            modArray1.sort(null);
//            if (!modArray.equals(modArray1)) System.out.println("Test1 failed for: " + i1 +"," + i2 + "," + i3 + "," + i4);
//
//            NBTTagCompound nbt = new NBTTagCompound();
//            modifiers.writeToNBT(nbt);
//            Modifiers modifiers2 = Modifiers.getStateFromNBT(nbt);
//            List<DragonVariants.Modifier> modArray2 = modifiers2.getModifierList();
//            if ((i3!=1 && i4!=1) && !modArray.equals(modArray2)) System.out.println("Test2 failed for: " + i1 +"," + i2 + "," + i3 + "," + i4);
//              //debug1 and debug2 are removed by the validator
//
//            ByteBuf bb = ByteBufAllocator.DEFAULT.buffer();
//            PacketBuffer pb = new PacketBuffer(bb);
//            Modifiers.MODIFIERS_DATA_SERIALIZER.write(pb, modifiers);
//            try {
//              Modifiers modifiers3 = Modifiers.MODIFIERS_DATA_SERIALIZER.read(pb);
//              List<DragonVariants.Modifier> modArray3 = modifiers3.getModifierList();
//              if ((i3!=1 && i4!=1) && !modArray.equals(modArray3)) System.out.println("Test3 failed for: " + i1 +"," + i2 + "," + i3 + "," + i4);
//              //debug1 and debug2 are removed by the validator
//            } catch (IOException ioe) {
//              System.out.println("Test3 failed with exception: " + ioe);
//            }
//          }
//        }
//      }
//    }
//  }
//}
