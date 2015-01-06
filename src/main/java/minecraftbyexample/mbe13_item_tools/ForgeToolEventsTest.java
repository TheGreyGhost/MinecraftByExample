package minecraftbyexample.mbe13_item_tools;

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
    Startup.methodCallLogger.enterMethod("Event.BreakSpeed", "blockstate=" + event.state + ", blockpos=" + event.pos + ", oldspeed = " + event.originalSpeed);
    Startup.methodCallLogger.exitMethod("Event.BreakSpeed", "newspeed=" + event.newSpeed + ", cancelled=" + event.isCanceled());
  }

  @SubscribeEvent
  public void harvestCheck(PlayerEvent.HarvestCheck event)
  {
    Startup.methodCallLogger.enterMethod("Event.HarvestCheck", "block=" + event.block);
    Startup.methodCallLogger.exitMethod("Event.HarvestCheck", "success=" + event.success);
  }

  @SubscribeEvent
  public void playerInteractEvent(PlayerInteractEvent event)
  {
    Startup.methodCallLogger.enterMethod("Event.PlayerInteractEvent", "action=" + event.action);
    Startup.methodCallLogger.exitMethod("Event.PlayerInteractEvent","");
  }

}
