package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.EntityPropellorShell;

public class EntityRendererPropellorShell extends RenderLiving<EntityPropellorShell>
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/entity/propellorshell.png");
	
	public EntityRendererPropellorShell(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelPropellorShell(), 0.25f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPropellorShell entity) 
	{
		return texture;
	}
	
	@Override
	protected void applyRotations(EntityPropellorShell entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) 
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
