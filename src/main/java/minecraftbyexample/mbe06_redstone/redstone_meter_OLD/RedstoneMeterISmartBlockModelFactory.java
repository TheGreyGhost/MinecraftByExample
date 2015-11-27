//package minecraftbyexample.mbe06_redstone.redstone_meter_OLD;
//
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.resources.model.IBakedModel;
//import net.minecraft.client.resources.model.ModelResourceLocation;
//import net.minecraft.util.EnumFacing;
//import net.minecraftforge.client.model.ISmartBlockModel;
//import net.minecraftforge.common.property.IExtendedBlockState;
//
//import java.util.List;
//
///**
// * Created by TheGreyGhost on 19/04/2015.
// * This class is used to customise the rendering of the meter.  See mbe04 and mbe05 for more information.
// * 1) Before the block is rendered, it is called with IBlockState.  This contains the power level.
// * 2) It then creates a RedstoneMeterSmartBlockModel for this power level, which is rendered by the caller.
// */
//public class RedstoneMeterISmartBlockModelFactory implements ISmartBlockModel {
//
//  public RedstoneMeterISmartBlockModelFactory(IBakedModel i_baseModel)
//  {
//    baseModel = i_baseModel;
//  }
//
//  // create a tag (ModelResourceLocation) for our model.
//  public static final ModelResourceLocation modelResourceLocation
//          = new ModelResourceLocation("minecraftbyexample:mbe06c_block_redstone_meter");
//
//  @SuppressWarnings("deprecation")  // IBakedModel is deprecated to encourage folks to use IFlexibleBakedModel instead
//                                    // .. but IFlexibleBakedModel is of no use here...
//
//  // This method is used to create a suitable IBakedModel based on the IBlockState of the block being rendered.
//  // If IBlockState is an instance of IExtendedBlockState, you can use it to pass in any information you want.
//  // Some folks return a new instance of the same ISmartBlockModel; I think it is more logical to return a different
//  //   class which implements IBakedModel instead of ISmartBlockModel, but it's a matter of taste.
//  @Override
//  public IBakedModel handleBlockState(IBlockState iBlockState)
//  {
//    IBakedModel retval = baseModel;  // default
//
//    // Extract the power level from the IExtendedBlockState, previously set by Block.getExtendedState()
//    if (iBlockState instanceof IExtendedBlockState) {
//      IExtendedBlockState iExtendedBlockState = (IExtendedBlockState) iBlockState;
//      Integer powerLevel = iExtendedBlockState.getValue(BlockRedstoneMeter.POWER_LEVEL);
//      retval = new RedstoneMeterSmartBlockModel(baseModel, powerLevel);
//    }
//    return retval;
//  }
//
//  private IBakedModel baseModel;
//
//  // getTexture is used directly when player is inside the block.  The game will crash if you don't use something
//  //   meaningful here.
//  @Override
//  public TextureAtlasSprite getTexture() {
//    return baseModel.getTexture();
//  }
//
//  // The methods below are all unused for RedstoneMeterISmartBlockModelFactory because we always return a new model
//  //  from handleBlockState.
//
//  @Override
//  public List getFaceQuads(EnumFacing p_177551_1_) {
//    throw new IllegalStateException();
//  }
//
//  @Override
//  public List getGeneralQuads()  {
//    throw new IllegalStateException();
//  }
//
//  @Override
//  public boolean isAmbientOcclusion()  {
//    throw new IllegalStateException();
//  }
//
//  @Override
//  public boolean isGui3d() {
//    throw new IllegalStateException();
//  }
//
//  @Override
//  public boolean isBuiltInRenderer() {
//    throw new IllegalStateException();
//  }
//
//  @Override
//  public ItemCameraTransforms getItemCameraTransforms() {
//    throw new IllegalStateException();
//  }
//
//}
