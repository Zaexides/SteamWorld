package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.EntitySkyFish;

public class EntityRendererSkyFish extends RenderLiving<EntitySkyFish>
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/entity/skyfish.png");
	
	public EntityRendererSkyFish(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelSkyFish(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySkyFish entity) 
	{
		return texture;
	}
	
	@Override
	protected void applyRotations(EntitySkyFish entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) 
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
