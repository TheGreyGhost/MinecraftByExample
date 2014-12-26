package minecraftbyexample.mbe03_block_variants;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * User: The Grey Ghost
 * Date: 27/12/2014
 * We need a custom item to represent the different sub-types (colours) of BlockVariant.
 * The Itemstack metadata represents the subtype.
 * You could also re-use ItemMultiTexture or ItemCloth or ItemColored.
 * Look at Item.registerItems() for inspiration
 */
public class ItemVariants extends ItemBlock
{
  public ItemVariants(BlockVariants block)
  {
    super(block);
    this.setMaxDamage(0);
    this.setHasSubtypes(true);
  }

  @Override
  public int getMetadata(int metadata)
  {
    return metadata;
  }

  // create a unique unlocalised name for each colour, so that we can give each one a unique name
  @Override
  public String getUnlocalizedName(ItemStack stack)
  {
    BlockVariants.EnumColour colour = BlockVariants.EnumColour.byMetadata(stack.getMetadata());
    return super.getUnlocalizedName() + "." + colour.toString();
  }
}
