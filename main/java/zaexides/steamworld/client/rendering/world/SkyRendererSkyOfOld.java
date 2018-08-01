package zaexides.steamworld.client.rendering.world;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.RenderingUtil;
import zaexides.steamworld.world.dimension.WorldProviderSkyOfOld;

public class SkyRendererSkyOfOld extends IRenderHandler
{
	public static final IRenderHandler renderer = new SkyRendererSkyOfOld();
	
	private static final ResourceLocation coreTexture = new ResourceLocation(ModInfo.MODID, "textures/environment/core.png");
	private static final ResourceLocation pipeTexture = new ResourceLocation(ModInfo.MODID, "textures/environment/pipes.png");
	private static final ResourceLocation sunTexture = new ResourceLocation("minecraft", "textures/environment/sun.png");	
	private static final ResourceLocation moonTexture = new ResourceLocation(ModInfo.MODID, "textures/environment/soo_moon.png");
	
	private static final float TIME_TO_DEGREES = (1.0f / WorldProviderSkyOfOld.DAY_DURATION) * 360.0f;
	
	private VertexBuffer skyVBO;
	private int glSkyList = -1;
	
	private boolean vboEnabled;
	private final VertexFormat vertexBufferFormat;
	
	public SkyRendererSkyOfOld() 
	{
		this.vboEnabled = OpenGlHelper.useVbo();
		this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));

		generateSky();
	}
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) 
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		TextureManager textureManager = mc.getTextureManager();
		
		GlStateManager.disableFog();
		GlStateManager.depthMask(false);
		
		RenderBaseSky(world, mc, partialTicks);
		
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
	
	//Near-copy of Vanilla code starts here...
	private void RenderBaseSky(World world, Minecraft mc, float partialTicks)
	{
		int pass = EntityRenderer.anaglyphField;
		GlStateManager.disableTexture2D();
		Vec3d skyColor = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float r = (float)skyColor.x;
		float g = (float)skyColor.y;
		float b = (float)skyColor.z;
		
		if(pass != 2)
		{
			float f3 = (r * 30.0f + g * 59.0f + b * 11.0f) / 100.0f;
			float f4 = (r * 30.0f + g * 70.0f) / 100.0f;
			float f5 = (r * 30.0f + b * 70.0f) / 100.0f;
			r = f3;
			g = f4;
			b = f5;
		}
		
		GlStateManager.color(r, g, b);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		GlStateManager.enableFog();
		GlStateManager.color(r, g, b);
		if(this.vboEnabled)
		{
			skyVBO.bindBuffer();
			GlStateManager.glEnableClientState(32884);
			GlStateManager.glVertexPointer(3, 5126, 12, 0);
			skyVBO.drawArrays(7);
			skyVBO.unbindBuffer();
			GlStateManager.glDisableClientState(32884);
		}
		else
			GlStateManager.callList(glSkyList);
		
		GlStateManager.disableFog();
		GlStateManager.color(1, 1, 1);
		GlStateManager.enableTexture2D();
	}
	
	private void generateSky()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.skyVBO != null)
        {
            this.skyVBO.deleteGlBuffers();
        }

        if (this.glSkyList >= 0)
        {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }

        if (this.vboEnabled)
        {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(bufferbuilder, 16.0F, false);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.skyVBO.bufferData(bufferbuilder.getByteBuffer());
        }
        else
        {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GlStateManager.glNewList(this.glSkyList, 4864);
            this.renderSky(bufferbuilder, 16.0F, false);
            tessellator.draw();
            GlStateManager.glEndList();
        }
    }
	
	private void renderSky(BufferBuilder worldRendererIn, float posY, boolean reverseX)
    {
        int i = 64;
        int j = 6;
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

        for (int k = -384; k <= 384; k += 64)
        {
            for (int l = -384; l <= 384; l += 64)
            {
                float f = (float)k;
                float f1 = (float)(k + 64);

                if (reverseX)
                {
                    f1 = (float)k;
                    f = (float)(k + 64);
                }

                worldRendererIn.pos((double)f, (double)posY, (double)l).endVertex();
                worldRendererIn.pos((double)f1, (double)posY, (double)l).endVertex();
                worldRendererIn.pos((double)f1, (double)posY, (double)(l + 64)).endVertex();
                worldRendererIn.pos((double)f, (double)posY, (double)(l + 64)).endVertex();
            }
        }
    }
	//...End
	
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
		GlStateManager.color(1.0f, 1.0f, 1.0f, rainAlpha);
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
		
		float gbValue = 1.0f - brightness * 0.25f;
		
		GlStateManager.color(1.0f, gbValue, gbValue, rainAlpha * brightness);
		textureManager.bindTexture(moonTexture);
		
		builder.pos(-90, 290, 90).tex(0, 1).endVertex();
		builder.pos(-90, 290, -90).tex(0, 0).endVertex();
		builder.pos(90, 290, -90).tex(1, 0).endVertex();
		builder.pos(90, 290, 90).tex(1, 1).endVertex();
		
		GlStateManager.rotate(45, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(world.getWorldTime() * TIME_TO_DEGREES, 0.0f, 0.0f, 1.0f);
		tessellator.draw();
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
