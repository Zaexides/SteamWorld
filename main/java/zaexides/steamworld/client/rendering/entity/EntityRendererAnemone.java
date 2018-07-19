package zaexides.steamworld.client.rendering.entity;

import org.apache.logging.log4j.Level;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.entity.EntityAnemone;;

public class EntityRendererAnemone extends RenderLiving<EntityAnemone>
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/entity/anemone.png");
	
	public EntityRendererAnemone(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelAnemone(), 0.75f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnemone entity) 
	{
		return texture;
	}
	
	@Override
	protected void applyRotations(EntityAnemone entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) 
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
