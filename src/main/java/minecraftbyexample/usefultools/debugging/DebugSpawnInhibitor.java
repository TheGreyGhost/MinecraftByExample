//package minecraftbyexample.usefultools.debugging;
//
//import net.minecraftforge.event.entity.living.LivingSpawnEvent;
//import net.minecraftforge.eventbus.api.Event;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
///**
// * Created by TGG on 27/06/2019.
// * <p>
// * While active, prevents any non-dragon entities from spawning.  Debugging purposes.
// * also useful: /kill @e[type=!Player]
// */
//public class DebugSpawnInhibitor {
//
//  @SubscribeEvent
//  public void checkForSpawnDenial(LivingSpawnEvent.CheckSpawn event) {
//    if (!DebugSettings.isSpawningInhibited() || event.getEntityLiving() instanceof EntityTameableDragon) {
//      event.setResult(Event.Result.DEFAULT);
//    } else {
//      event.setResult(Event.Result.DENY);
//    }
//  }
//
//
//}
