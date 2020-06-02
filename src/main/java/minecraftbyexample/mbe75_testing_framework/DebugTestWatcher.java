package minecraftbyexample.mbe75_testing_framework;

import minecraftbyexample.usefultools.debugging.DebugSettings;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

/**
 * Created by TGG on 21/02/2020.
 * Used in conjunction with the MBEdebugCommand to trigger execution of an in-game test
 *
 * /mbedebug test testnumber
 */
public class DebugTestWatcher {
  @SubscribeEvent
  public static void onServerTick(TickEvent.PlayerTickEvent event) {
    if (event.side != LogicalSide.SERVER) return;

    int testNumber = DebugSettings.getDebugTest();
    if (testNumber == DebugSettings.NO_TEST_TRIGGERED) return;
    testRunner.runServerSideTest(event.player.world, event.player, testNumber);
  }
  public static TestRunner testRunner = new TestRunner();
}
