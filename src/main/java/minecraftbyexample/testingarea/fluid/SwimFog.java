//package minecraftbyexample.testingarea.fluid;
//
//import minecraftbyexample.testingarea.fluid.GFluids;
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//public class SwimFog
//{
//	Block amethystFluid = GFluids.amethyst.getBlock();
//
//	@SubscribeEvent
//	public void playerSwim(FogColors e)
//	{
//		Entity ent = e.getEntity();
//		World w = ent.getEntityWorld();
//		BlockPos pos = ent.getPosition();
//		BlockPos pos2 = new BlockPos(pos.getX(), ent.getEyeHeight(), pos.getY());
//		IBlockState bs = w.getBlockState(pos);
//		IBlockState bs2 = w.getBlockState(pos2);
//		Block b = bs.getBlock();
//		Block b2 = bs2.getBlock();
//		if(b.equals(amethystFluid) || b2.equals(amethystFluid))
//		{
//			float red = 80.0F, green = 130.0F, blue = 147.0F;
//			updateFog(e, red, green, blue);
//
//		}
//	}
//
//	private void updateFog(FogColors e, float red, float green, float blue)
//	{
//		float r = red / 256.0F;
//		float g = green / 256.0F;
//		float b = blue / 256.0F;
//		e.setRed(r); e.setGreen(g); e.setBlue(b);
//		GlStateManager.setFog(GlStateManager.FogMode.EXP);
//		GlStateManager.setFogDensity(2.0F);
//	}
//}