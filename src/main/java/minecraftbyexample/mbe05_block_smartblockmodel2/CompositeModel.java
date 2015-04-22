package minecraftbyexample.mbe05_block_smartblockmodel2;

import minecraftbyexample.mbe04_block_smartblockmodel1.BlockCamouflage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by TheGreyGhost on 22/04/2015.
 */
public class CompositeModel implements IFlexibleBakedModel, ISmartBlockModel {

  public CompositeModel(IBakedModel i_modelCore, IBakedModel i_modelUp)
  {
    modelCore = i_modelCore;
    modelUp = i_modelUp;
  }

  private IBakedModel modelCore;
  private IBakedModel modelUp;

  private boolean up = false;

  @Override
  public List<BakedQuad> getFaceQuads(EnumFacing side) {
    List<BakedQuad> allFaceQuads = new LinkedList<BakedQuad>();
    allFaceQuads.addAll(modelCore.getFaceQuads(side));
    if (up) {
      allFaceQuads.addAll(modelUp.getFaceQuads(side));
    }
    return allFaceQuads;
  }

  @Override
  public List<BakedQuad> getGeneralQuads() {
    List<BakedQuad> allGeneralQuads = new LinkedList<BakedQuad>();
    allGeneralQuads.addAll(modelCore.getGeneralQuads());
    if (up) {
      allGeneralQuads.addAll(modelUp.getGeneralQuads());
    }
    return allGeneralQuads;
  }

  @Override
  public boolean isAmbientOcclusion() {
    return modelCore.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return modelCore.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public TextureAtlasSprite getTexture() {
    return modelCore.getTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return modelCore.getItemCameraTransforms();
  }

  @Override
  public VertexFormat getFormat() {
    return Attributes.DEFAULT_BAKED_FORMAT;
  }

  // This method is used to create a suitable IBakedModel based on the IBlockState of the block being rendered.
  // If IBlockState is an instance of IExtendedBlockState, you can use it to pass in any information you want.
  @Override
  public IBakedModel handleBlockState(IBlockState iBlockState) {
    if (iBlockState instanceof IExtendedBlockState) {
      IExtendedBlockState iExtendedBlockState = (IExtendedBlockState) iBlockState;
      Boolean linkUp = iExtendedBlockState.getValue(Block3DWeb.LINK_UP);
      if (linkUp != null) {
        up = linkUp;
      }
    }
    return this;
  }
}
