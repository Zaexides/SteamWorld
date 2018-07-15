package zaexides.steamworld.models;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

//use bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE); beforehand!!
public class PresetRenderer 
{
	private static TextureAtlasSprite textureAtlasSprite;
	private static final int COLOR_WHITE = 0xffffffff;
	
	public static void SetSprite(ResourceLocation resourceLocation)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		textureAtlasSprite = minecraft.getTextureMapBlocks().getAtlasSprite(resourceLocation.toString());
	}
	
	public static void DrawCube(Vec3d pos, Vec3d min, Vec3d max)
	{
		DrawCube(pos, min, max, COLOR_WHITE);
	}
	
	public static void DrawCube(Vec3d pos, Vec3d min, Vec3d max, int color)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		
		PreWork(pos, builder);
		DrawCubePart(builder, min, max, color);
		tessellator.draw();
		PostWork();
	}
	
	public static void DrawCubePart(BufferBuilder builder, Vec3d min, Vec3d max)
	{
		DrawCubePart(builder, min, max, COLOR_WHITE);
	}
	
	public static void DrawCubePart(BufferBuilder builder, Vec3d min, Vec3d max, int color)
	{
		DrawPlanePart(builder, min, max, EnumFacing.NORTH, color);
		DrawPlanePart(builder, min, max, EnumFacing.EAST, color);
		DrawPlanePart(builder, min, max, EnumFacing.SOUTH, color);
		DrawPlanePart(builder, min, max, EnumFacing.WEST, color);
		DrawPlanePart(builder, min, max, EnumFacing.UP, color);
		DrawPlanePart(builder, min, max, EnumFacing.DOWN, color);
	}
	
	public static void DrawPlanePart(BufferBuilder builder, Vec3d min, Vec3d max, EnumFacing direction)
	{
		DrawPlanePart(builder, min, max, direction, COLOR_WHITE);
	}
	
	public static void DrawPlanePart(BufferBuilder builder, Vec3d min, Vec3d max, EnumFacing direction, boolean inverted)
	{
		DrawPlanePart(builder, min, max, direction, COLOR_WHITE, inverted);
	}
	
	public static void DrawPlanePart(BufferBuilder builder, Vec3d min, Vec3d max, EnumFacing direction, int color)
	{
		DrawPlanePart(builder, min, max, direction, color, false);
	}
	
	private static void DrawPlanePart(BufferBuilder builder, Vec3d min, Vec3d max, EnumFacing direction, int color, boolean inverted)
	{
		int a = color >> 24 & 0xff;
		int r = color >> 16 & 0xff;
		int g = color >> 8 & 0xff;
		int b = color >> 00 & 0xff;
		
		switch(direction)
		{
		case NORTH:
			builder.pos(min.x, max.y, inverted ? max.z : min.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, max.y, inverted ? max.z : min.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, min.y, inverted ? max.z : min.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			builder.pos(min.x, min.y, inverted ? max.z : min.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			break;
		case SOUTH:
			builder.pos(min.x, max.y, inverted ? min.z : max.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			builder.pos(min.x, min.y, inverted ? min.z : max.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, min.y, inverted ? min.z : max.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, max.y, inverted ? min.z : max.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			break;
		case EAST:
			builder.pos(inverted ? min.x : max.x, max.y, min.z).tex(textureAtlasSprite.getInterpolatedU(min.z*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			builder.pos(inverted ? min.x : max.x, max.y, max.z).tex(textureAtlasSprite.getInterpolatedU(max.z*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			builder.pos(inverted ? min.x : max.x, min.y, max.z).tex(textureAtlasSprite.getInterpolatedU(max.z*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			builder.pos(inverted ? min.x : max.x, min.y, min.z).tex(textureAtlasSprite.getInterpolatedU(min.z*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			break;
		case WEST:
			builder.pos(inverted ? max.x : min.x, max.y, min.z).tex(textureAtlasSprite.getInterpolatedU(min.z*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			builder.pos(inverted ? max.x : min.x, min.y, min.z).tex(textureAtlasSprite.getInterpolatedU(min.z*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			builder.pos(inverted ? max.x : min.x, min.y, max.z).tex(textureAtlasSprite.getInterpolatedU(max.z*16), textureAtlasSprite.getInterpolatedV(max.y*16)).color(r, g, b, a).endVertex();
			builder.pos(inverted ? max.x : min.x, max.y, max.z).tex(textureAtlasSprite.getInterpolatedU(max.z*16), textureAtlasSprite.getInterpolatedV(min.y*16)).color(r, g, b, a).endVertex();
			break;
		case UP:
			builder.pos(min.x, inverted ? min.y : max.y, max.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(max.z*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, inverted ? min.y : max.y, max.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(max.z*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, inverted ? min.y : max.y, min.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(min.z*16)).color(r, g, b, a).endVertex();
			builder.pos(min.x, inverted ? min.y : max.y, min.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(min.z*16)).color(r, g, b, a).endVertex();
			break;
		case DOWN:
			builder.pos(min.x, inverted ? max.y : min.y, max.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(max.z*16)).color(r, g, b, a).endVertex();
			builder.pos(min.x, inverted ? max.y : min.y, min.z).tex(textureAtlasSprite.getInterpolatedU(min.x*16), textureAtlasSprite.getInterpolatedV(min.z*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, inverted ? max.y : min.y, min.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(min.z*16)).color(r, g, b, a).endVertex();
			builder.pos(max.x, inverted ? max.y : min.y, max.z).tex(textureAtlasSprite.getInterpolatedU(max.x*16), textureAtlasSprite.getInterpolatedV(max.z*16)).color(r, g, b, a).endVertex();
			break;
		}
	}
	
	public static void PreWork(Vec3d pos, BufferBuilder builder)
	{
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		if(Minecraft.isAmbientOcclusionEnabled())
			GL11.glShadeModel(GL11.GL_SMOOTH);
		else
			GL11.glShadeModel(GL11.GL_FLAT);
		
		GlStateManager.translate(pos.x, pos.y, pos.z);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
	}
	
	public static void PostWork()
	{
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
