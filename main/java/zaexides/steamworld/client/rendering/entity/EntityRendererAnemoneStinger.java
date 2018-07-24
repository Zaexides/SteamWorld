package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.EntityAnemoneStinger;

public class EntityRendererAnemoneStinger extends RenderArrow<EntityAnemoneStinger>
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(ModInfo.MODID, "textures/entity/stinger.png");
	
	public EntityRendererAnemoneStinger(RenderManager renderManagerIn) 
	{
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityAnemoneStinger entity) 
	{
		return TEXTURES;
	}
}
