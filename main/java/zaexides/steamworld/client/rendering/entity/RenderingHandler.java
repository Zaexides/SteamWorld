package zaexides.steamworld.client.rendering.entity;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.entity.EntityAnciteGolem;
import zaexides.steamworld.entity.EntityAnemone;
import zaexides.steamworld.entity.EntityAnemoneStinger;
import zaexides.steamworld.entity.EntityEclipseStalker;
import zaexides.steamworld.entity.EntityGlowDusty;
import zaexides.steamworld.entity.EntityPropellorShell;
import zaexides.steamworld.entity.EntitySkyFish;
import zaexides.steamworld.entity.villangler.EntityVillangler;

public class RenderingHandler
{
	@SideOnly(Side.CLIENT)
	public static void RegisterHandlers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySkyFish.class, EntityRendererSkyFish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAnemone.class, EntityRendererAnemone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAnemoneStinger.class, EntityRendererAnemoneStinger::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPropellorShell.class, EntityRendererPropellorShell::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEclipseStalker.class, EntityRendererEclipseStalker::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAnciteGolem.class, EntityRendererAnciteGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVillangler.class, EntityRendererVillangler::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGlowDusty.class, EntityRendererGlowDusty::new);
	}
}
