package minecraftbyexample.usefultools;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.util.OptionalDouble;

/**
 * Created by TGG on 21/02/2020.
 * Just used to make the code more readable due to obfuscated names
 * Once the mapping is updated to something nicer, we can remove...
 *
 * There are more than these (see RenderType) but these are the ones I'm currently interested in
 */
public class RenderTypeMBE {
  public static RenderType SOLID() {
    return RenderType.getSolid();
  }
  public static RenderType CUTOUT_MIPPED() {
    return RenderType.getCutoutMipped();
  }
  public static RenderType CUTOUT() {
    return RenderType.getCutout();
  }
  public static RenderType TRANSLUCENT() {
    return RenderType.getTranslucent();
  }
  public static RenderType TRANSLUCENT_NO_CRUMBLING() {
    return RenderType.getTranslucentNoCrumbling();
  }
  public static RenderType LEASH() {
    return RenderType.getLeash();
  }
  public static RenderType WATER_MASK() {
    return RenderType.getWaterMask();
  }
  public static RenderType LINES() {
    return RenderType.getLines();
  }
}
