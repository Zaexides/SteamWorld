package zaexides.steamworld.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.entity.EntitySkyFish;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class EntityInitializer 
{
	private static int currentId = 0;
	
	@SubscribeEvent
	public static void RegisterEntities(RegistryEvent.Register<EntityEntry> event)
	{
		IForgeRegistry<EntityEntry> reg = event.getRegistry();
		RegisterEntity(reg, "skyfish", EntitySkyFish.class, EntitySkyFish::new, 80, 3, 0x6D7FE8, 0xEDEDD5);
	}
	
	private static <T extends Entity> void RegisterEntity(IForgeRegistry<EntityEntry> registry, String entityName, Class<T> entity, Function<World, T> factory, int range, int updateFrequency, int primaryColor, int secondaryColor)
	{
		ResourceLocation registryName = new ResourceLocation(ModInfo.MODID, entityName);
		EntityEntry entityEntry = EntityEntryBuilder.<T>create().id(registryName, currentId).name(registryName.toString().replace(':', '.')).entity(entity).tracker(range, updateFrequency, true).egg(primaryColor, secondaryColor).factory(factory).build();
		registry.register(entityEntry);
		currentId++;
	}
}
