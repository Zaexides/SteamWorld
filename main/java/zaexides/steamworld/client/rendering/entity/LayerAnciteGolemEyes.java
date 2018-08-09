package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.EntityAnciteGolem;

public class LayerAnciteGolemEyes<T extends EntityAnciteGolem> implements LayerRenderer<T>
{
    private static final ResourceLocation GOLEM_EYES = new ResourceLocation(ModInfo.MODID, "textures/entity/ancite_golem_eyes.png");
	private final EntityRendererAnciteGolem anciteGolemRender;
	
	public LayerAnciteGolemEyes(EntityRendererAnciteGolem anciteGolemRender) 
	{
		this.anciteGolemRender = anciteGolemRender;
	}
    
	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if(entitylivingbaseIn.getAwakeningStep() < 0.9f)
			return;
		this.anciteGolemRender.bindTexture(GOLEM_EYES);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        if (entitylivingbaseIn.isInvisible())
        {
            GlStateManager.depthMask(false);
        }
        else
        {
            GlStateManager.depthMask(true);
        }
		this.anciteGolemRender.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldCombineTextures() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
