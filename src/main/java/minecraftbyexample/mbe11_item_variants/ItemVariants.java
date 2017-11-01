package minecraftbyexample.mbe11_item_variants;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * Item with variants, illustrating two different ways of changing the rendering to suit the variant.
 * The bottle has two types of information:
 * Bottle contents (LIME, LEMON, CHERRY, ORANGE), stored in the lowest two bits (0 - 3 inclusive) of metadata/damage
 * Bottle fullness (EMPTY, 25%, 50%, 75%, 100%), stored in the next 3 bits (0, 4, 8, 12, 16) of metadata/damage
 * The FULLNESS is used to select the model name: each different fullness has a different layer1 texture for the liquid level
 * The CONTENTS is used to select the colour of the contents (layer1 render)
 */
public class ItemVariants extends Item
{
  public ItemVariants() {
    this.setMaxDamage(0);
    this.setHasSubtypes(true);
    this.setMaxStackSize(1);
    this.setCreativeTab(CreativeTabs.MISC);   // items will appear on the Miscellaneous creative tab
  }

  @Override
  public int getMetadata(int damage) {
    return damage;
  }

  // add a subitem for each item we want to appear in the creative tab
  //  in this case - a full bottle of each colour
  @SideOnly(Side.CLIENT)
  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
  {
    for (EnumBottleContents contents : EnumBottleContents.values()) {
      int contentBits = contents.getMetadata();
      int fullnessBits = EnumBottleFullness.FULL.getMetadata();
      int metadata = contentBits | (fullnessBits << 2);
      ItemStack subItemStack = new ItemStack(this, 1, metadata);
      subItems.add(subItemStack);
    }
  }

  @Override
  // Make a unique name for each contents type (lime, orange, etc) so we can name them individually
  //  The fullness information is added separately in getItemStackDisplayName()
  public String getUnlocalizedName(ItemStack stack)
  {
    int metadata = stack.getMetadata();
    int contentsBits = metadata & 0x03;
    int fullnessBits = (metadata >> 2) & 0x07;

    EnumBottleContents contents = EnumBottleContents.byMetadata(contentsBits);
    return super.getUnlocalizedName() + "." + contents.getName();
  }

  // what animation to use when the player holds the "use" button
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.DRINK;
  }

  // how long the drinking will last for, in ticks (1 tick = 1/20 second)
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    final int TICKS_PER_SECOND = 20;
    final int DRINK_DURATION_SECONDS = 2;
    return DRINK_DURATION_SECONDS * TICKS_PER_SECOND;
  }

  // called when the player starts holding right click;
  // --> start drinking the liquid (if the bottle isn't already empty)
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
  {
    ItemStack itemStackHeld = playerIn.getHeldItem(hand);
    int metadata = itemStackHeld.getMetadata();
    int fullnessBits = (metadata >> 2) & 0x07;
    EnumBottleFullness fullness = EnumBottleFullness.byMetadata(fullnessBits);
    if (fullness == EnumBottleFullness.EMPTY) return new ActionResult(EnumActionResult.FAIL, itemStackHeld);

    playerIn.setActiveHand(hand);
    return new ActionResult(EnumActionResult.PASS, itemStackHeld);
  }

  // called when the player has held down right button for the full item use duration
  // --> decrease the bottle fullness by one step
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
  {
    int metadata = stack.getMetadata();
    int contentsBits = metadata & 0x03;
    int fullnessBits = (metadata >> 2) & 0x07;
    if (fullnessBits != 0) --fullnessBits;  // decrease fullness (assumes same order of metadata as fullness!)
    int newMetadata = contentsBits | (fullnessBits << 2);
    stack.setItemDamage(newMetadata);
    return stack;
  }

    // change the displayed stack name depending on the fullness
    // the contents are already incorporated into the unlocalizedName
  @Override
  public String getItemStackDisplayName(ItemStack stack)
  {
    String s = ("" + I18n.translateToLocal(this.getUnlocalizedName(stack) + ".name")).trim();  // this is depecrated, but I don't know what replaces it...
    int metadata = stack.getMetadata();
    int fullnessBits = (metadata >> 2) & 0x07;
    EnumBottleFullness fullness = EnumBottleFullness.byMetadata(fullnessBits);
    s += "(" + fullness.getDescription() + ")";
    return s;
  }

//  // when rendering, choose the colour multiplier based on the contents
//  // we want layer 0 (the bottle glass) to be unaffected (return white as the multiplier)
//  // layer 1 will change colour depending on the contents.
//  @SideOnly(Side.CLIENT)
//  @Override
//  public int getColorFromItemStack(ItemStack stack, int renderLayer)
//  {
//    switch (renderLayer) {
//      case 0: return Color.WHITE.getRGB();
//      case 1: {
//        int metadata = stack.getMetadata();
//        int contentsBits = metadata & 0x03;
//        EnumBottleContents contents = EnumBottleContents.byMetadata(contentsBits);
//        return contents.getRenderColour().getRGB();
//      }
//      default: {
//        // oops! should never get here.
//        return Color.BLACK.getRGB();
//      }
//    }
//  }

  // create a new enum for our liquid contents, with some supporting methods to convert to & from metadata, and to get
  //  human-readable names for a description.
  public static enum EnumBottleFullness implements IStringSerializable
  {
    EMPTY(0, "0pc", "empty"),
    ONE_QUARTER(1, "25pc", "nearly empty"),
    ONE_HALF(2, "50pc", "half full"),
    THREE_QUARTERS(3, "75pc", "mostly full"),
    FULL(4, "100pc", "full");

    public int getMetadata()
    {
      return this.meta;
    }

    @Override
    public String toString()
    {
      return this.description;
    }

    public static EnumBottleFullness byMetadata(int meta)
    {
      if (meta < 0 || meta >= META_LOOKUP.length)
      {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    public String getName()
    {
      return this.name;
    }

    public String getDescription() {return this.description;}

    private final int meta;
    private final String name;
    private final String description;
    private static final EnumBottleFullness[] META_LOOKUP = new EnumBottleFullness[values().length];

    private EnumBottleFullness(int i_meta, String i_name, String i_description)
    {
      this.meta = i_meta;
      this.name = i_name;
      this.description = i_description;
    }

    static
    {
      for (EnumBottleFullness value : values()) {
        META_LOOKUP[value.getMetadata()] = value;
      }
    }
  }

  // create a new enum for our liquid contents, with some supporting methods to convert to & from metadata, to get
  //  human-readable names, and to provide a rendering colour
  public static enum EnumBottleContents implements IStringSerializable
  {
    LEMON(0, "lemon", Color.YELLOW),
    LIME(1, "lime", Color.GREEN),
    CHERRY(2, "cherry", Color.RED),
    ORANGE(3, "orange", Color.ORANGE);

    public int getMetadata()
    {
      return this.meta;
    }

    @Override
    public String toString()
    {
      return this.name;
    }

    public static EnumBottleContents byMetadata(int meta)
    {
      if (meta < 0 || meta >= META_LOOKUP.length)
      {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    public String getName()
    {
      return this.name;
    }
    public Color getRenderColour() {return renderColour;}

    private final int meta;
    private final String name;
    private final Color renderColour;
    private static final EnumBottleContents[] META_LOOKUP = new EnumBottleContents[values().length];

    private EnumBottleContents(int i_meta, String i_name, Color i_renderColour)
    {
      this.meta = i_meta;
      this.name = i_name;
      this.renderColour = i_renderColour;
    }

    static
    {
      for (EnumBottleContents value : values()) {
        META_LOOKUP[value.getMetadata()] = value;
      }
    }
  }

}