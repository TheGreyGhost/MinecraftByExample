package minecraftbyexample.mbe13_item_tools;

import minecraftbyexample.usefultools.MethodCallLogger;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * User: The Grey Ghost
 * Date: 6/01/2015
 */
public class ForgeToolEventsTest
{
  @SubscribeEvent
  public void breakSpeed(PlayerEvent.BreakSpeed event)
  {
    if (MethodCallLogger.shouldLog("Event.BreakSpeed")) {
      System.out.println("#PlayerEvent.BreakSpeed: blockstate=" + event.state + ", blockpos=" + event.pos + ", oldspeed = " + event.originalSpeed );
      System.out.println("#  +---> return to caller: newspeed=" + event.newSpeed + ", cancelled=" + event.isCanceled());
    }
  }

  @SubscribeEvent
  public void harvestCheck(PlayerEvent.HarvestCheck event)
  {
    if (MethodCallLogger.shouldLog("Event.HarvestCheck")) {
      System.out.println("#PlayerEvent.HarvestCheck: block=" + event.block);
      System.out.println("#  +---> return to caller: success=" + event.success);
    }
  }

  @SubscribeEvent
  public void playerInteractEvent(PlayerInteractEvent event)
  {
    if (MethodCallLogger.shouldLog("Event.PlayerInteractEvent")) {
      System.out.println("#PlayerInteractEvent: action=" + event.action);
      System.out.println("#  +---> return to caller: success=" + event.useBlock);
    }
  }

}
