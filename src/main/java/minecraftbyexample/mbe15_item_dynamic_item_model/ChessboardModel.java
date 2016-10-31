package minecraftbyexample.mbe15_item_dynamic_item_model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

/**
 * User: TheGreyGhost <br> 
 * Date: 19/04/2015   <p>
 * 
 * This class modifies the displayed item (a chessboard) to show a number of "pieces" (blue squares) 
 * <br> on the chessboard, one square for each item in the itemstack. <p>
 * 
 * For models generated from a texture turned into a "texture with thickness" (i.e. like most items), 
 * <br> you must implement IPerspectiveAwareModel instead of IBakedModel otherwise the item transforms <br>
 *  won't work. This is because Forge doesn't implement BakedItemModel.getItemCameraTransforms() correctly.
 */
public class ChessboardModel implements IPerspectiveAwareModel {

	/**
	 * Create our model, using the given baked model as a base to add extra BakedQuads to.
	 * @param i_baseChessboardModel the base model
	 */
	public ChessboardModel(IBakedModel i_baseChessboardModel)
	{
		baseChessboardModel = i_baseChessboardModel;
		chessboardItemOverrideList = new ChessboardItemOverrideList(Collections.EMPTY_LIST);
	}

	/* 
	 *  Create a tag (ModelResourceLocation) for our model, "inventory" is used for items. 
	 *  If you don't specify it, you will end up with "normal" by default, which is used for blocks.
	 */
	public static final ModelResourceLocation modelResourceLocation = 
			new ModelResourceLocation("minecraftbyexample:mbe15_item_chessboard", "inventory");

	@Override
	public TextureAtlasSprite getParticleTexture() 
	{
		return baseChessboardModel.getParticleTexture();
	}

	/**  
	 * Returns the quads for the base chessboard model only.
	 */
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		return baseChessboardModel.getQuads(state, side, rand);
	}

	// not needed for items, but hey
	@Override
	public boolean isAmbientOcclusion() 
	{
		return baseChessboardModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() 
	{
		return baseChessboardModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() 
	{
		return false;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() 
	{
		// NB this is not enough for BakedItemModels, must do handlePerspective as well.
		return baseChessboardModel.getItemCameraTransforms();  
	}

	@Override
	public ItemOverrideList getOverrides() 
	{
		return chessboardItemOverrideList;
	}

	private IBakedModel baseChessboardModel;
	private ChessboardItemOverrideList chessboardItemOverrideList;

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) 
	{
		if (baseChessboardModel instanceof IPerspectiveAwareModel) 
		{
			Matrix4f matrix4f = ((IPerspectiveAwareModel)baseChessboardModel).handlePerspective(cameraTransformType).getRight();
			return Pair.of(this, matrix4f);
		} 
		else 
		{
			// If the base model isn't an IPerspectiveAware, we'll need to generate the 
			// correct matrix ourselves using the ItemCameraTransforms.

			ItemCameraTransforms itemCameraTransforms = baseChessboardModel.getItemCameraTransforms();
			ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
			TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
			
			Matrix4f mat = tr != null ? tr.getMatrix() : null;
			
			// The TRSRTransformation for vanilla items have blockCenterToCorner() applied, however handlePerspective
			// reverses it back again with blockCornerToCenter(), so we don't need to apply it here.

			return Pair.of(this, mat);
		}
	}
}
