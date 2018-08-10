package zaexides.steamworld.client.rendering.entity;

import org.apache.logging.log4j.Level;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.entity.EntityAnciteGolem;

public class EntityRendererAnciteGolem extends RenderLiving<EntityAnciteGolem>
{
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.MODID, "textures/entity/ancite_golem.png");
	
	public EntityRendererAnciteGolem(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelAnciteGolem(), 0.75f);
		this.addLayer(new LayerAnciteGolemEyes(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnciteGolem entity) 
	{
		return texture;
	}
	
	@Override
	protected void applyRotations(EntityAnciteGolem entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) 
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
