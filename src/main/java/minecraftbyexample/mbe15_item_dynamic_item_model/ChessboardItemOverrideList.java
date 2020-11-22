package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by TGG on 20/10/2016.
 */
public class ChessboardItemOverrideList extends ItemOverrideList {
  public ChessboardItemOverrideList()
  {
    super();
  }

  /**
   *  getModelWithOverrides() is used to create/select a suitable IBakedModel based on the itemstack information.
   *  For vanilla, the ItemOverrideList contains a list of IBakedModels, each with a corresponding ItemOverride (predicate),
   *    read in from the item json, which matches a PropertyOverride on the item.  See mbe12 (ItemNBTAnimate) for example
   *  In this case, we extend ItemOverrideList to return a dynamically-generated series of BakedQuads, instead of relying on
   *    a fixed BakedModel.
   *  Typically you would use this by extracting NBT information from the itemstack and customising the quads based on that.
   *  I've just used the itemstack count because it's easier / less complicated.
   *  It's probably safest to return a new model or at least an immutable one, rather than modifying the
   *    originalModel passed in, in case the rendering is multithreaded (block rendering has this problem, for example).
   * @param originalModel
   * @param stack
   * @param world
   * @param entity
   * @return
   * // old name: getModelWithOverrides
   */
  @Override
  public IBakedModel getOverrideModel(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity)
  {
    int numberOfChessPieces = 0;
    if (stack != null) {
      numberOfChessPieces = stack.getCount();
    }
    return new ChessboardFinalisedModel(originalModel, numberOfChessPieces);
  }
}
