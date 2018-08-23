package zaexides.steamworld.client.rendering.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.te.generic_machine.TileEntityTeleporter;

public class TeleporterModel extends TileEntitySpecialRenderer<TileEntityTeleporter>
{
	private static final ResourceLocation teleporterFx = new ResourceLocation(ModInfo.MODID, "textures/environment/teleporter_fx.png");
	
	@Override
	public void render(TileEntityTeleporter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		if(te.steamTank.getFluidAmount() < te.REQUIRED_STEAM || te.targetId == -1)
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y, z + 0.5);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		
		float size = (1.0f-((te.cooldown * 1.0f) / te.MAX_COOLDOWN)) * 0.5f;
		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bindTexture(teleporterFx);
		
		builder.pos(-size, 1.1, -size).tex(0, 1).endVertex();
		builder.pos(-size, 1.1, size).tex(0, 0).endVertex();
		builder.pos(size, 1.1, size).tex(1, 0).endVertex();
		builder.pos(size, 1.1, -size).tex(1, 1).endVertex();
		
		GlStateManager.rotate(te.getWorld().getTotalWorldTime() * 0.1f, 0.0f, 1.0f, 0.0f);
		
		tessellator.draw();
		
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
