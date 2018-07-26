package zaexides.steamworld.client.rendering.world;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.RenderingUtil;
import zaexides.steamworld.worldgen.dimension.DimensionTypeSteamWorld;

public class SkyRendererSkyOfOld extends IRenderHandler
{
	public static final IRenderHandler renderer = new SkyRendererSkyOfOld();
	
	private static final ResourceLocation coreTexture = new ResourceLocation(ModInfo.MODID, "textures/environment/core.png");
	private static final ResourceLocation pipeTexture = new ResourceLocation(ModInfo.MODID, "textures/environment/pipes.png");
	private static final ResourceLocation sunTexture = new ResourceLocation("minecraft", "textures/environment/sun.png");	
	private static final ResourceLocation moonTexture = new ResourceLocation(ModInfo.MODID, "textures/environment/soo_moon.png");
	
	private static final float TIME_TO_DEGREES = (1.0f / 24000.0f) * 360.0f;
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) 
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		TextureManager textureManager = mc.getTextureManager();
		
		GlStateManager.disableFog();
		GlStateManager.depthMask(false);
		
		RenderingUtil.PreWork(new Vec3d(0, -140, 0), builder);
		RenderCore(textureManager, builder, world.getWorldTime());
		tessellator.draw();
		RenderingUtil.PostWork();
		
		RenderingUtil.PreWork(new Vec3d(0, -140, 0), builder);
		RenderCorePipes(textureManager, builder, world.getWorldTime());
		tessellator.draw();
		RenderingUtil.PostWork();
		
		RenderSun(tessellator, builder, partialTicks, world, textureManager);
		RenderMoon(tessellator, builder, partialTicks, world, textureManager);
		
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();
	}
	
	private void RenderCore(TextureManager textureManager, BufferBuilder builder, float time)
	{
		textureManager.bindTexture(coreTexture);
		GlStateManager.rotate(time * 0.00001f - 16.3f, 1.0f, 0.0f, 1.0f);
		DrawCube(80.0f, builder, 16, false);
	}
	
	private void RenderCorePipes(TextureManager textureManager, BufferBuilder builder, float time)
	{
		textureManager.bindTexture(pipeTexture);
		GlStateManager.rotate(time * 0.00002f + 52.6f, 1.0f, 0.0f, 1.0f);
		DrawCube(120.0f, builder, 32, false);
		DrawCube(120.0f, builder, 32, true);
	}
	
	private void DrawCube(float size, BufferBuilder builder, double uvSize, boolean invert)
	{
		Vec3d min = new Vec3d(-size * .5, -size * .5, -size * .5);
		Vec3d max = new Vec3d(size * .5, size * .5, size * .5);
		final int COLOR_WHITE = 0xffffffff;
		
		RenderingUtil.DrawPlanePart(builder, min, max, EnumFacing.DOWN, COLOR_WHITE, invert, 0, 0, uvSize, uvSize);
		RenderingUtil.DrawPlanePart(builder, min, max, EnumFacing.UP, COLOR_WHITE, invert, 0, 0, uvSize, uvSize);
		RenderingUtil.DrawPlanePart(builder, min, max, EnumFacing.NORTH, COLOR_WHITE, invert, 0, 0, uvSize, uvSize);
		RenderingUtil.DrawPlanePart(builder, min, max, EnumFacing.EAST, COLOR_WHITE, invert, 0, 0, uvSize, uvSize);
		RenderingUtil.DrawPlanePart(builder, min, max, EnumFacing.SOUTH, COLOR_WHITE, invert, 0, 0, uvSize, uvSize);
		RenderingUtil.DrawPlanePart(builder, min, max, EnumFacing.WEST, COLOR_WHITE, invert, 0, 0, uvSize, uvSize);
	}
	
	private void RenderSun(Tessellator tessellator, BufferBuilder builder, float partialTicks, World world, TextureManager textureManager)
	{
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		if(Minecraft.isAmbientOcclusionEnabled())
			GL11.glShadeModel(GL11.GL_SMOOTH);
		else
			GL11.glShadeModel(GL11.GL_FLAT);
		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		float rainAlpha = 1.0f - world.getRainStrength(partialTicks);
		float brightness = world.getSunBrightness(partialTicks);
		GlStateManager.color(1.0f, 1.0f, 1.0f, rainAlpha * brightness);
		textureManager.bindTexture(sunTexture);
		
		builder.pos(-80, 300, 80).tex(0, 1).endVertex();
		builder.pos(-80, 300, -80).tex(0, 0).endVertex();
		builder.pos(80, 300, -80).tex(1, 0).endVertex();
		builder.pos(80, 300, 80).tex(1, 1).endVertex();
		
		GlStateManager.rotate(45, 1.0f, 0.0f, 0.0f);
		tessellator.draw();
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	
	private void RenderMoon(Tessellator tessellator, BufferBuilder builder, float partialTicks, World world, TextureManager textureManager)
	{
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		if(Minecraft.isAmbientOcclusionEnabled())
			GL11.glShadeModel(GL11.GL_SMOOTH);
		else
			GL11.glShadeModel(GL11.GL_FLAT);
		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		float rainAlpha = 1.0f - world.getRainStrength(partialTicks);
		float brightness = 1.0f - world.getSunBrightness(partialTicks) + 0.55f;
		if(brightness > 1.0f)
			brightness = 1.0f;
		GlStateManager.color(1.0f, 1.0f, 1.0f, rainAlpha * brightness);
		textureManager.bindTexture(moonTexture);
		
		builder.pos(-70, 290, 70).tex(0, 1).endVertex();
		builder.pos(-70, 290, -70).tex(0, 0).endVertex();
		builder.pos(70, 290, -70).tex(1, 0).endVertex();
		builder.pos(70, 290, 70).tex(1, 1).endVertex();
		
		GlStateManager.rotate(45, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(world.getWorldTime() * TIME_TO_DEGREES, 0.0f, 0.0f, 1.0f);
		tessellator.draw();
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
