package minecraftbyexample.mbe11_item_variants;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * Created by TGG on 17/08/2016.
 */
public class LiquidColour implements IItemColor {

  /**
   * Returns the colour for rendering, based on
   * 1) the itemstack
   * 2) the "tintindex" (layer in the item model json)
   * For example:
   * bottle_drinkable.json contains
   *   "layer0": "items/potion_overlay",
   *   "layer1": "items/potion_bottle_drinkable"
   * layer0 = tintindex 0 = for the bottle outline, whose colour doesn't change
   * layer1 = tintindex 1 = for the bottle contents, whose colour changes depending on the type of potion
   * @param stack
   * @param tintIndex
   * @return an RGB colour (to be multiplied by the texture colours)
   */
  @Override
  public int colorMultiplier(ItemStack stack, int tintIndex) {
    // when rendering, choose the colour multiplier based on the contents
    // we want layer 0 (the bottle glass) to be unaffected (return white as the multiplier)
    // layer 1 will change colour depending on the contents.
    {
      switch (tintIndex) {
        case 0: return Color.WHITE.getRGB();
        case 1: {
          int metadata = stack.getMetadata();
          int contentsBits = metadata & 0x03;
          ItemVariants.EnumBottleContents contents = ItemVariants.EnumBottleContents.byMetadata(contentsBits);
          return contents.getRenderColour().getRGB();
        }
        default: {
          // oops! should never get here.
          return Color.BLACK.getRGB();
        }
      }
    }
  }
}
