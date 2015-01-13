package minecraftbyexample.mbe12_item_nbt_animate;

import net.minecraft.client.resources.model.ModelResourceLocation;

import java.util.ArrayList;

/**
 * User: The Grey Ghost
 * Date: 13/01/2015
 * This class is used to encapsulate client-only code out of the ItemNBTAnimate.
 * If you try to define the ModelResourceLocation in ItemNBTAnimate, it will crash on a dedicated server.
 * ItemNBTModels uses "lazy initialisation" so that nothing is created until getInstance() is called for the first time.
 */
public class ItemNBTModels
{
  // get the instance
  public static ItemNBTModels getInstance() {
    if (instance == null) {
      instance = new ItemNBTModels();
    }
    return instance;
  }

  private static ItemNBTModels instance;
  private ArrayList<ModelResourceLocation> models = new ArrayList<ModelResourceLocation>();

  private ItemNBTModels()
  {
    models.add(new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_0", "inventory"));
    models.add(new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_1", "inventory"));
    models.add(new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_2", "inventory"));
    models.add(new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_3", "inventory"));
    models.add(new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_4", "inventory"));
    models.add(new ModelResourceLocation("minecraftbyexample:mbe12_item_nbt_animate_5", "inventory"));
  }

  public ModelResourceLocation getModel(int modelIndex) {
    return models.get(modelIndex);
  }
}
