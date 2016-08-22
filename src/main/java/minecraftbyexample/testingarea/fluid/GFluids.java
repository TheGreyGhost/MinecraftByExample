//package minecraftbyexample.testingarea.fluid;
//
//import minecraftbyexample.testingarea.fluid.GemFluid;
//import net.minecraft.block.Block;
//import net.minecraft.block.material.MapColor;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidRegistry;
//import net.minecraftforge.fml.common.registry.GameRegistry;
//
//public class GFluids
//{
//	static { FluidRegistry.enableUniversalBucket(); }
//
//	public static GemFluid amethyst = new GemFluid("amethyst", MapColor.PURPLE);
//
//	public static void createFluids()
//	{
//		createFluid(amethyst);
//	}
//
//	private static void createFluid(GemFluid f)
//	{
//		FluidRegistry.registerFluid(f);
//		createBlock(new GemFluidBlock(f));
//		createBucket(f);
//	}
//	private static void createBlock(Block b)
//	{
//		GameRegistry.register(b);
//	}
//	private static void createBucket(Fluid f)
//	{
//		FluidRegistry.addBucketForFluid(f);
//	}
//}