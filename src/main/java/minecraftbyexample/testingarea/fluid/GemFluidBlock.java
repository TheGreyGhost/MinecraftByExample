//package minecraftbyexample.testingarea.fluid;
//
//import org.apache.commons.lang3.StringUtils;
//
//import net.minecraft.block.material.MapColor;
//import net.minecraft.block.state.IBlockState;
//import net.minecraftforge.fluids.BlockFluidClassic;
//
//public class GemFluidBlock extends BlockFluidClassic
//{
//	public GemFluidBlock(GemFluid fluid)
//	{
//		super(fluid, new MaterialGemLiquid(fluid.getMapColor()));
//		setRegistryName("fluid" + StringUtils.capitalize(fluid.getName()));
//		setUnlocalizedName("fluid." + fluid.getName());
//	}
//
//	public GemFluid getGemFluid()
//	{
//		return (GemFluid)getFluid();
//	}
//
//	@Override
//	public MapColor getMapColor(IBlockState ibs)
//	{
//		return getGemFluid().getMapColor();
//	}
//}