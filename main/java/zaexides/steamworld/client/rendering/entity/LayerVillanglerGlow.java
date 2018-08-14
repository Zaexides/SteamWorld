package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.villangler.EntityVillangler;
import zaexides.steamworld.entity.villangler.EntityVillangler.VillanglerVariant;

public class LayerVillanglerGlow implements LayerRenderer<EntityVillangler>
{
    private static final ResourceLocation EMISSION_MAP = new ResourceLocation(ModInfo.MODID, "textures/entity/villangler_emission.png");
	private final EntityRendererVillangler rendererVillangler;
	
	public LayerVillanglerGlow(EntityRendererVillangler rendererVillangler) 
	{
		this.rendererVillangler = rendererVillangler;
	}
    
	@Override
	public void doRenderLayer(EntityVillangler entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if(entitylivingbaseIn.getVariant() == VillanglerVariant.CULTIST)
			return;
		
		this.rendererVillangler.bindTexture(EMISSION_MAP);
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
        
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		this.rendererVillangler.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		
		i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.rendererVillangler.setLightmap(entitylivingbaseIn);
		
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldCombineTextures() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
