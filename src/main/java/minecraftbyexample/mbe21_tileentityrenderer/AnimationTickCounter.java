package minecraftbyexample.mbe21_tileentityrenderer;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by TGG on 10/03/2020.
 */
public class AnimationTickCounter {

  public static long getTotalElapsedTicksInGame() {
    return totalElapsedTicksInGame;
  }

  @SubscribeEvent
  public static void clientTickEnd(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (!Minecraft.getInstance().isGamePaused()) {
        totalElapsedTicksInGame++;
      }
    }
  }

  private static long totalElapsedTicksInGame = 0;
}
