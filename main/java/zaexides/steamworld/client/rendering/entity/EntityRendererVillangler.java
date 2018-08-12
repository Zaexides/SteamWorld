package zaexides.steamworld.client.rendering.entity;

import org.apache.logging.log4j.Level;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.entity.villangler.EntityVillangler;

public class EntityRendererVillangler extends RenderLiving<EntityVillangler>
{
	private static final ResourceLocation textureDefault = new ResourceLocation(ModInfo.MODID, "textures/entity/villangler.png");
	private static final ResourceLocation textureEconomist = new ResourceLocation(ModInfo.MODID, "textures/entity/villangler_economist.png");
	private static final ResourceLocation textureLibrarian = new ResourceLocation(ModInfo.MODID, "textures/entity/villangler_librarian.png");
	private static final ResourceLocation textureScientist = new ResourceLocation(ModInfo.MODID, "textures/entity/villangler_scientist.png");
	
	public EntityRendererVillangler(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelVillangler(), 0.35f);
		this.addLayer(new LayerVillanglerGlow(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVillangler entity) 
	{
		switch(entity.getVariant())
		{
		case ECONOMIST:
			return textureEconomist;
		case LIBRARIAN:
			return textureLibrarian;
		case SCIENTIST:
			return textureScientist;
		default:
			return textureDefault;
		}
	}
	
	@Override
	protected void applyRotations(EntityVillangler entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) 
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
