package minecraftbyexample.mbe15_item_smartitemmodel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class is used to customise the rendering of the camouflage block, based on the block it is copying.
 */
public class ChessboardSmartItemModel implements ISmartItemModel {

  public ChessboardSmartItemModel(IBakedModel i_baseChessboardModel)
  {
    baseChessboardModel = i_baseChessboardModel;
  }

  // create a tag (ModelResourceLocation) for our model.
  //  "inventory" is used for items. If you don't specify it, you will end up with "normal" by default,
  //  which is used for blocks.
  public static final ModelResourceLocation modelResourceLocation
          = new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");

  @SuppressWarnings("deprecation")  // IBakedModel is deprecated to encourage folks to use IFlexibleBakedModel instead
                                    // .. but IFlexibleBakedModel is of no use here...

  // This method is used to create a suitable IBakedModel based on the itemstack information.
  //  Typically, this will extract NBT information from the itemstack and customise the model based on that.
  // I think it is ok to just modify this instance instead of creating a new instance, because the IBakedModel
  //   isn't stored or cached and is discarded after rendering.  Haven't run into any problems yet
  @Override
  public IBakedModel handleItemState(ItemStack stack) {
    return this;
  }

  @Override
  public TextureAtlasSprite getTexture() {
    return baseChessboardModel.getTexture();
  }

  @Override
  public List getFaceQuads(EnumFacing enumFacing) {
    return baseChessboardModel.getFaceQuads(enumFacing);
  }

  @Override
  public List getGeneralQuads() {


    FaceBakery.makeBakedQuad() can be useful for generating quads

    return baseChessboardModel.getGeneralQuads();
  }

  // not needed for items, but hey
  @Override
  public boolean isAmbientOcclusion() {
    return baseChessboardModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return baseChessboardModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return baseChessboardModel.getItemCameraTransforms();
  }

  private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
  {
    return new int[] {
            Float.floatToRawIntBits(x),
            Float.floatToRawIntBits(y),
            Float.floatToRawIntBits(z),
            color,
            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
            0
    };
  }

  private BakedQuad createSidedBakedQuad(float x1, float x2, float z1, float z2, float y, TextureAtlasSprite texture, EnumFacing side)
  {
    Vec3 v1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
    Vec3 v2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
    Vec3 v3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
    Vec3 v4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
    return new BakedQuad(Ints.concat(
            vertexToInts((float)v1.xCoord, (float)v1.yCoord, (float)v1.zCoord, -1, texture, 0, 0),
            vertexToInts((float)v2.xCoord, (float)v2.yCoord, (float)v2.zCoord, -1, texture, 0, 16),
            vertexToInts((float)v3.xCoord, (float)v3.yCoord, (float)v3.zCoord, -1, texture, 16, 16),
            vertexToInts((float)v4.xCoord, (float)v4.yCoord, (float)v4.zCoord, -1, texture, 16, 0)
    ), -1, side);
  }

  @Override
  public List<BakedQuad> getGeneralQuads()
  {
    int len = cubeSize * 5 + 1;
    List<BakedQuad> ret = new ArrayList<BakedQuad>();
    for(EnumFacing f : EnumFacing.values())
    {
      ret.add(createSidedBakedQuad(0, 1, 0, 1, 1, base, f));
      for(int i = 0; i < cubeSize; i++)
      {
        for(int j = 0; j < cubeSize; j++)
        {
          if(state != null)
          {
            Integer value = (Integer)state.getValue(properties[f.ordinal()]);
            if(value != null && (value & (1 << (i * cubeSize + j))) != 0)
            {
              ret.add(createSidedBakedQuad((float)(1 + i * 5) / len, (float)(5 + i * 5) / len, (float)(1 + j * 5) / len, (float)(5 + j * 5) / len, 1.0001f, overlay, f));
            }
          }
        }
      }
    }
    return ret;
  }


  private IBakedModel baseChessboardModel;
}
