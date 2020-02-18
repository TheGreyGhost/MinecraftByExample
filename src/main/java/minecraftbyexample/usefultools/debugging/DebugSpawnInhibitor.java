package minecraftbyexample.usefultools.debugging;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by TGG on 27/06/2019.
 * <p>
 * While active, prevents vanilla entities from spawning.  Debugging purposes.
 * also useful: /kill @e[type=!Player]
 */
public class DebugSpawnInhibitor {
  @SubscribeEvent
  public static void checkForSpawnDenial(LivingSpawnEvent.CheckSpawn event) {
    if (DebugSettings.getDebugParameter("preventspawning") == 0.0) {
      event.setResult(Event.Result.DEFAULT);
    } else {
      ResourceLocation entityname = ForgeRegistries.ENTITIES.getKey(event.getEntity().getType());
      if (entityname.getNamespace().equals("minecraft")) {
        event.setResult(Event.Result.DENY);
      } else {
        event.setResult(Event.Result.DEFAULT);
      }
    }
  }
}
