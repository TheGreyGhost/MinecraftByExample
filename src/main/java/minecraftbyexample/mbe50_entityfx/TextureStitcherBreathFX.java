package minecraftbyexample.mbe50_entityfx;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by EveryoneElse on 19/06/2015.
 */
public class TextureStitcherBreathFX
{
  @SubscribeEvent
  public void stitcherEventPre(TextureStitchEvent.Pre event) {
    ResourceLocation flameRL = new ResourceLocation("minecraftbyexample:entity/flame_fx");
    event.map.registerSprite(flameRL);
  }
}
