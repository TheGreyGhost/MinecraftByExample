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
    return RenderType.func_228639_c_();
  }
  public static RenderType CUTOUT_MIPPED() {
    return RenderType.func_228641_d_();
  }
  public static RenderType CUTOUT() {
    return RenderType.func_228643_e_();
  }
  public static RenderType TRANSLUCENT() {
    return RenderType.func_228645_f_();
  }
  public static RenderType TRANSLUCENT_NO_CRUMBLING() {
    return RenderType.func_228647_g_();
  }
  public static RenderType LEASH() {
    return RenderType.func_228649_h_();
  }
  public static RenderType WATER_MASK() {
    return RenderType.func_228651_i_();
  }
  public static RenderType LINES() {
    return RenderType.func_228659_m_();
  }
}
