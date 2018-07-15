package zaexides.steamworld.models;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import scala.deprecated;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.te.TileEntityFilterTank;

public class FilteredTankModel extends TileEntitySpecialRenderer<TileEntityFilterTank>
{
	private static final ResourceLocation sideTexture = new ResourceLocation(ModInfo.MODID, "blocks/machines/filter_tank");
	private static final ResourceLocation topBottomTexture = new ResourceLocation(ModInfo.MODID, "blocks/machines/machine_rock");
	
	@Override
	public void render(TileEntityFilterTank te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		FluidStack fluidStack = te.tank.getFluid();
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderInsides(x, y, z);
		if(fluidStack != null)
		{
			PresetRenderer.SetSprite(fluidStack.getFluid().getStill());
			
			float height = ((float)fluidStack.amount / (float)te.tank.getCapacity()) * 0.97f + 0.015f;
			
			boolean isGas = fluidStack.getFluid().isGaseous();
			PresetRenderer.DrawCube(new Vec3d(x, y, z), new Vec3d(.015f, isGas ? (1 - height) : .015f, .015f), new Vec3d(.985f, isGas ? 0.985f : height, .985f), fluidStack.getFluid().getColor());
		}
	}
	
	private void RenderInsides(double x, double y, double z)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		Vec3d pos = new Vec3d(x,y,z);
		
		Vec3d min = new Vec3d(.01f, .01f, .01f);
		Vec3d max = new Vec3d(.99f, .99f, .99f);
		
		PresetRenderer.SetSprite(sideTexture);
		PresetRenderer.PreWork(pos, builder);
		PresetRenderer.DrawPlanePart(builder, min, max, EnumFacing.NORTH, true);
		PresetRenderer.DrawPlanePart(builder, min, max, EnumFacing.EAST, true);
		PresetRenderer.DrawPlanePart(builder, min, max, EnumFacing.WEST, true);
		PresetRenderer.DrawPlanePart(builder, min, max, EnumFacing.SOUTH, true);
		tessellator.draw();
		PresetRenderer.PostWork();
		
		PresetRenderer.SetSprite(topBottomTexture);
		PresetRenderer.PreWork(pos, builder);
		PresetRenderer.DrawPlanePart(builder, min, max, EnumFacing.UP, true);
		PresetRenderer.DrawPlanePart(builder, min, max, EnumFacing.DOWN, true);
		tessellator.draw();
		PresetRenderer.PostWork();
	}
}
