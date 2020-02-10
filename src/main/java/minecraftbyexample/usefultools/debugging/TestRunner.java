//package minecraftbyexample.usefultools.debugging;
//
//import com.TheRPGAdventurer.ROTD.DragonMounts;
//import com.TheRPGAdventurer.ROTD.common.entity.EntityTameableDragon;
//import com.TheRPGAdventurer.ROTD.common.entity.breath.nodes.BreathNodeP;
//import com.TheRPGAdventurer.ROTD.common.entity.breeds.DragonBreedNew;
//import com.TheRPGAdventurer.ROTD.common.entity.breeds.DragonFactory;
//import com.TheRPGAdventurer.ROTD.common.entity.breeds.EnumDragonBreed;
//import com.TheRPGAdventurer.ROTD.common.entity.helper.DragonLifeStage;
//import com.TheRPGAdventurer.ROTD.common.entity.physicalmodel.DragonPhysicalModel;
//import com.TheRPGAdventurer.ROTD.common.entity.physicalmodel.Modifiers;
//import com.TheRPGAdventurer.ROTD.util.debugging.testclasses.TestForestBreath;
//import net.minecraft.block.BlockLadder;
//import net.minecraft.command.CommandClone;
//import net.minecraft.command.server.CommandTeleport;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
//import java.util.List;
//
//import static com.google.common.base.Preconditions.checkArgument;
//
///**
// * Example test runner which is called when the player activate the testrunner item.
// * Created by TGG on 4/01/2016.
// */
//public class TestRunner {
//  /**
//   * Teleport the player to the test region (so you can see the results of the test)
//   *
//   * @param playerIn
//   * @param location
//   * @return
//   */
//  public static boolean teleportPlayerToTestRegion(EntityPlayer playerIn, BlockPos location) {
//    if (!(playerIn instanceof EntityPlayerMP)) {
//      throw new UnsupportedOperationException("teleport not supported on client side; server side only");
//    }
//    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerIn;
//
//    String tpArguments = "@p " + location.getX() + " " + location.getY() + " " + location.getZ();
//    String[] tpArgumentsArray = tpArguments.split(" ");
//
//    CommandTeleport commandTeleport = new CommandTeleport();
//    try {
//      commandTeleport.execute(entityPlayerMP.mcServer, playerIn, tpArgumentsArray);
//    } catch (Exception e) {
//      return false;
//    }
//    return true;
//  }
//
//  /**
//   * Copy a cuboid Test Region from one part of the world to another
//   * The cuboid is x blocks wide, by y blocks high, by z blocks long
//   *
//   * @param entityPlayer
//   * @param sourceOrigin origin of the source region
//   * @param destOrigin   origin of the destination region
//   * @param xCount       >=1
//   * @param yCount       >=1
//   * @param zCount       >=1
//   * @return true for success, false otherwise
//   */
//  public static boolean copyTestRegion(EntityPlayer entityPlayer,
//                                       BlockPos sourceOrigin, BlockPos destOrigin,
//                                       int xCount, int yCount, int zCount) {
//    checkArgument(xCount >= 1);
//    checkArgument(yCount >= 1);
//    checkArgument(zCount >= 1);
//    String[] args = new String[9];
//
//    if (!(entityPlayer instanceof EntityPlayerMP)) {
//      throw new UnsupportedOperationException("teleport not supported on client side; server side only");
//    }
//    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
//
//
//    args[0] = String.valueOf(sourceOrigin.getX());
//    args[1] = String.valueOf(sourceOrigin.getY());
//    args[2] = String.valueOf(sourceOrigin.getZ());
//    args[3] = String.valueOf(sourceOrigin.getX() + xCount - 1);
//    args[4] = String.valueOf(sourceOrigin.getY() + yCount - 1);
//    args[5] = String.valueOf(sourceOrigin.getZ() + zCount - 1);
//    args[6] = String.valueOf(destOrigin.getX());
//    args[7] = String.valueOf(destOrigin.getY());
//    args[8] = String.valueOf(destOrigin.getZ());
//
//    CommandClone commandClone = new CommandClone();
//    try {
//      commandClone.execute(entityPlayerMP.mcServer, entityPlayer, args);
//    } catch (Exception e) {
//      return false;
//    }
//    return true;
//  }
//
//  public static boolean testDragonLifeStage() {
//    final int ARBITRARY_MINUS = -1000000;
//    final int ARBITRARY_LARGE = 1000000;
//
//    int minTick = DragonLifeStage.clipTickCountToValid(ARBITRARY_MINUS);
//    int maxTick = DragonLifeStage.clipTickCountToValid(ARBITRARY_LARGE);
//
//    System.out.println("Minimum tick:" + minTick);
//    System.out.println("Maximum tick:" + maxTick);
//
//    DragonLifeStage lastStage = null;
//    int printAnywayTicks = 0;
//    for (int i = minTick - 3; i <= maxTick + 10000; ++i) {
//      boolean printCalcs = false;
//      DragonLifeStage thisStage = DragonLifeStage.getLifeStageFromTickCount(i);
//      if (thisStage != lastStage) {
//        lastStage = thisStage;
//        System.out.println("Changed to " + thisStage + " at tick=" + i);
//        printAnywayTicks = 1000;
//        printCalcs = true;
//      } else if (--printAnywayTicks <= 0) {
//        printAnywayTicks = 1000;
//        printCalcs = true;
//      }
//      if (printCalcs) {
//        System.out.println("At tick=" + i + ": " +
//                "Scale = " + DragonLifeStage.getAgeScaleFromTickCount(i) + ", " +
//                "StageProgress = " + DragonLifeStage.getStageProgressFromTickCount(i));
//      }
//
//    }
//    System.out.println("Final stage was:" + lastStage);
//    return true;
//  }
//
//  public static boolean testGetRelativeHeadSize(World worldIn) {
////    EntityTameableDragon dragon = new EntityTameableDragon(worldIn);
//    DragonPhysicalModel dpm = new DragonPhysicalModel();
//
//    for (float scale = 0.0f; scale <= 1.0F; scale += 0.01F) {
//      float headsize = dpm.getRelativeHeadSize(scale);
//      System.out.println("scale=" + scale + ", relativeheadsize=" + headsize);
//    }
//    return true;
//  }
//
//  public boolean runServerSideTest(World worldIn, EntityPlayer playerIn, int testNumber) {
//    boolean success = false;
//    switch (testNumber) {
//      case 59: {
//        testGetRelativeHeadSize(worldIn);
//        break;
//      }
//      case 60: {
//        testDragonLifeStage();
//        break;
//      }
//      case 61: {
////        success = test1(worldIn, playerIn);  // todo restore
//        TestForestBreath testForestBreath = new TestForestBreath();
//        testForestBreath.test1(worldIn, playerIn);
//        break;
//      }
//      case 62: {
//        EntityTameableDragon dragon = DragonFactory.getDefaultDragonFactory().createDragon(worldIn,
//                DragonBreedNew.DragonBreedsRegistry.getDefaultRegistry().getDefaultBreed(), new Modifiers());
//        BreathNodeP.Power power = BreathNodeP.Power.SMALL;
//        ++testCounter;
//        Vec3d origin = new Vec3d(0, 24, 0);
//        Vec3d target = new Vec3d(0, 4, 0);
//        if (testCounter == 1) {
//          origin = new Vec3d(0, 24, 0);
//          target = new Vec3d(0, 4, 0);
//        }
//        if (testCounter == 2) {
//          origin = new Vec3d(0, 24, 0);
//          target = new Vec3d(0, 4, 0);
//          power = BreathNodeP.Power.MEDIUM;
//        }
//        if (testCounter == 3) {
//          origin = new Vec3d(0, 24, 0);
//          target = new Vec3d(0, 4, 0);
//          power = BreathNodeP.Power.LARGE;
//          testCounter = 0;
//        }
//        //todo reinstate test for later if required
////        EntityBreathProjectileGhost entity = new EntityBreathProjectileGhost(worldIn, dragon, origin, target, power);
////        worldIn.spawnEntityInWorld(entity);
//        System.out.println("Lightning spawned: mouth at [x,y,z] = " + origin + "to destination [x,y,z,] = " + target);
//
//        break;
//      }
//      default: {
//
//        final int DRAGON_SPAWN_TESTS_BASE = 1;
//        int dragonMeta = testNumber - DRAGON_SPAWN_TESTS_BASE;
//        if (EnumDragonBreed.getAllMetas().contains(dragonMeta)) {
//          spawnTamedDragon(worldIn, playerIn, EnumDragonBreed.getBreedForMeta(dragonMeta));
//          return true;
//        }
//        System.out.println("Test Number " + testNumber + " does not exist on server side.");
//        return false;
//      }
//    }
//
//    System.out.println("Test Number " + testNumber + " called on server side:" + (success ? "success" : "failure"));
//    return success;
//  }
//
//  public boolean runClientSideTest(World worldIn, EntityPlayer playerIn, int testNumber) {
//    boolean success = false;
//
//    switch (testNumber) {
//      case -1: {  // dummy (do nothing) - can never be called, just to prevent unreachable code compiler error
//        break;
//      }
//      default: {
//        System.out.println("Test Number " + testNumber + " does not exist on client side.");
//        return false;
//      }
//    }
//    System.out.println("Test Number " + testNumber + " called on client side:" + (success ? "success" : "failure"));
//
//    return success;
//  }
//
//  public void spawnTamedDragon(World worldIn, EntityPlayer playerIn, EnumDragonBreed dragonBreedToSpawn) {
//    DragonMounts.logger.info("spawnTamedDragon:" + dragonBreedToSpawn);
//
//    // destroy all dragons in this radius, pesky critters
//    final float SEARCH_RANGE = 64.0F;
//    AxisAlignedBB aabb = playerIn.getEntityBoundingBox()
//            .grow(SEARCH_RANGE);
//
//    // List all dragons in expanded player entity box
//    List<EntityTameableDragon> dragons = playerIn
//            .world
//            .getEntitiesWithinAABB(EntityTameableDragon.class, aabb);
//    for (EntityTameableDragon dragon : dragons) {
//      dragon.setDead();
//    }
//
//    EntityTameableDragon entityDragon = DragonFactory.getDefaultDragonFactory().createDragon(worldIn, DragonBreedNew.DragonBreedsRegistry.getDefaultRegistry().getDefaultBreed());
//    entityDragon.setBreedType(dragonBreedToSpawn);
//    entityDragon.lifeStage().setLifeStage(DragonLifeStage.ADULT);
//    entityDragon.reproduction().setBreeder(playerIn);
//    entityDragon.setPosition(playerIn.posX + 5, playerIn.posY + 0.2, playerIn.posZ + 5);
//    entityDragon.tamedFor(playerIn, true);
//    ItemStack saddle = new ItemStack(Items.SADDLE, 1);
//    final int SADDLE_SLOT = 0;
//    entityDragon.dragonInv.setInventorySlotContents(SADDLE_SLOT, saddle);
//    entityDragon.refreshInventory();
//    worldIn.spawnEntity(entityDragon);
//
//    entityDragon.getAISit().setSitting(true);
//    entityDragon.getNavigator().clearPath();
//  }
//
//  // dummy test: check the correct functioning of the ladder - to see which blocks it can stay attached to
//  // The test region contains a ladder attached to a stone block.  We then replace it with different blocks and see
//  //   whether the ladder remains or breaks appropriately; eg
//  // testA - replace with wood
//  // testB - replace with a glass block
//  // testC - replace with diamond block
//  private boolean test1(World worldIn, EntityPlayer playerIn) {
//    BlockPos sourceRegionOrigin = new BlockPos(0, 204, 0);
//    final int SOURCE_REGION_SIZE_X = 4;
//    final int SOURCE_REGION_SIZE_Y = 2;
//    final int SOURCE_REGION_SIZE_Z = 3;
//
//    // put a stone block with attached ladder in the middle of our test region
//    worldIn.setBlockState(sourceRegionOrigin.add(1, 0, 1), Blocks.STONE.getDefaultState());
//    worldIn.setBlockState(sourceRegionOrigin.add(2, 0, 1),
//            Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.EAST));
//
//    BlockPos testRegionOriginA = new BlockPos(5, 204, 0);
//    BlockPos testRegionOriginB = new BlockPos(10, 204, 0);
//    BlockPos testRegionOriginC = new BlockPos(15, 204, 0);
//
//    teleportPlayerToTestRegion(playerIn, testRegionOriginA.south(5));  // teleport the player nearby so you can watch
//
//    // copy the test blocks to the destination region
//    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginA,
//            SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
//    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginB,
//            SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
//    copyTestRegion(playerIn, sourceRegionOrigin, testRegionOriginC,
//            SOURCE_REGION_SIZE_X, SOURCE_REGION_SIZE_Y, SOURCE_REGION_SIZE_Z);
//
//    boolean success = true;
//    // testA: replace stone with wood; ladder should remain
//    worldIn.setBlockState(testRegionOriginA.add(1, 0, 1), Blocks.LOG.getDefaultState());
//    success &= worldIn.getBlockState(testRegionOriginA.add(2, 0, 1)).getBlock() == Blocks.LADDER;
//
//    // testB: replace stone with glass; ladder should be destroyed
//    worldIn.setBlockState(testRegionOriginB.add(1, 0, 1), Blocks.GLASS.getDefaultState());
//    success &= worldIn.getBlockState(testRegionOriginB.add(2, 0, 1)).getBlock() == Blocks.AIR;
//
//    // testC: replace stone with diamond block; ladder should remain
//    worldIn.setBlockState(testRegionOriginC.add(1, 0, 1), Blocks.DIAMOND_BLOCK.getDefaultState());
//    success &= worldIn.getBlockState(testRegionOriginC.add(2, 0, 1)).getBlock() == Blocks.LADDER;
//
//    return success;
//  }
//  static private int testCounter = 0;
//}
