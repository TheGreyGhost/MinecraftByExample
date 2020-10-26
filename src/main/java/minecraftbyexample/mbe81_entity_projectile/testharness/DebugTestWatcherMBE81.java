//package minecraftbyexample.mbe81_entity_projectile.testharness;
//
//import minecraftbyexample.usefultools.debugging.DebugSettings;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.LogicalSide;
//
///**
// * Created by TGG on 21/02/2020.
// * Used in conjunction with the MBEdebugCommand to trigger execution of an in-game test
// *
// * /mbedebug test testnumber
// */
//public class DebugTestWatcherMBE81 {
//  @SubscribeEvent
//  public static void onServerTick(TickEvent.PlayerTickEvent event) {
//    if (event.side != LogicalSide.SERVER) return;
//
//    int testNumber = DebugSettings.getDebugTest(8100, 8199);
//    if (testNumber == DebugSettings.NO_TEST_TRIGGERED) return;
//    testRunner.runServerSideTest(event.player.world, event.player, testNumber);
//  }
//  public static TestRunnerMBE81 testRunner = new TestRunnerMBE81();
//}
