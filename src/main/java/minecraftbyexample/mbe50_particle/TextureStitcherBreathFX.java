package minecraftbyexample.mbe50_particle;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TGG on 19/06/2015.
 * Inserts our custom flame texture into the blocks+items texture sheet.
 */
public class TextureStitcherBreathFX
{
  @SubscribeEvent
  public void stitcherEventPre(TextureStitchEvent.Pre event) {
    ResourceLocation flameRL = new ResourceLocation("minecraftbyexample:entity/flame_fx");
    event.getMap().registerSprite(flameRL);
  }
}
