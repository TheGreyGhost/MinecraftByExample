package minecraftbyexample.usefultools.debugging;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by TGG on 27/06/2019.
 * <p>
 * While active, prevents entities from spawning.  Debugging purposes.
 * also useful: /kill @e[type=!Player]
 */
public class DebugSpawnInhibitor {
  @SubscribeEvent
  public static void checkForSpawnDenial(LivingSpawnEvent.CheckSpawn event) {
    if (DebugSettings.getDebugParameter("preventspawning") == 0.0) {
      event.setResult(Event.Result.DEFAULT);
    } else {
      event.setResult(Event.Result.DENY);
    }
  }
}
