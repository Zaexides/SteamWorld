package zaexides.steamworld.client.rendering.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.EntityGlowDusty;

public class EntityRendererGlowDusty extends RenderLiving<EntityGlowDusty>
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/entity/glowdusty.png");
	private static final float INT_TO_COLOR_FLOAT = 1.0f / 255.0f;
	
	public EntityRendererGlowDusty(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelGlowDusty(), 0.05f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGlowDusty entity) 
	{
		return texture;
	}
	
	@Override
	public void doRender(EntityGlowDusty entity, double x, double y, double z, float entityYaw, float partialTicks) 
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if(!this.renderOutlines && !entity.isInvisible())
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y + 0.3, z);
			GlStateManager.rotate(-this.renderManager.playerViewY, 0, 1, 0);
			GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.depthMask(false);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
			
			int color = ((EntityGlowDusty)entity).getColor();
	    	GlStateManager.color(((color >> 16) & 255) * INT_TO_COLOR_FLOAT, ((color >> 8) & 255) * INT_TO_COLOR_FLOAT, (color & 255) * INT_TO_COLOR_FLOAT, 0.5f);
			
	    	Tessellator tessellator = Tessellator.getInstance();
	    	BufferBuilder builder = tessellator.getBuffer();
	    	
	    	builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    	
	    	builder.pos(-1, 1, 0).tex(0.0f, 0.5f).endVertex();
	    	builder.pos(1, 1, 0).tex(0.5f, 0.5f).endVertex();
	    	builder.pos(1, -1, 0).tex(0.5f, 1.0f).endVertex();
	    	builder.pos(-1, -1, 0).tex(0.0f, 1.0f).endVertex();
	    	
	    	tessellator.draw();
	    	
	    	GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	protected void applyRotations(EntityGlowDusty entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) 
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
