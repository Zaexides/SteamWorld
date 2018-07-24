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
import zaexides.steamworld.entity.EntityAnemone;
import zaexides.steamworld.entity.EntityAnemoneStinger;
import zaexides.steamworld.entity.EntitySkyFish;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class EntityInitializer 
{
	private static int currentId = 0;
	
	@SubscribeEvent
	public static void RegisterEntities(RegistryEvent.Register<EntityEntry> event)
	{
		RegisterHelper helper = new RegisterHelper(event.getRegistry());
		
		helper.RegisterEntityLiving("skyfish", EntitySkyFish.class, EntitySkyFish::new, 0x6D7FE8, 0xEDEDD5);
		helper.RegisterEntityLiving("anemone", EntityAnemone.class, EntityAnemone::new, 0x493FB5, 0xFF00DC);
	
		helper.RegisterEntityNonLiving("anemone_stinger", EntityAnemoneStinger.class, EntityAnemoneStinger::new);
	}
	
	private static class RegisterHelper
	{
		private IForgeRegistry<EntityEntry> registry;
		
		public RegisterHelper(IForgeRegistry<EntityEntry> registry)
		{
			this.registry = registry;
		}
		
		final <T extends Entity> void RegisterEntityNonLiving(String entityName, Class<T> entity, Function<World, T> factory)
		{
			RegisterEntity(entityName, entity, factory, 64, 1, false, 0, 0);
		}

		final <T extends Entity> void RegisterEntityLiving(String entityName, Class<T> entity, Function<World, T> factory, int primaryColor, int secondaryColor)
		{
			RegisterEntity(entityName, entity, factory, 64, 3, true, primaryColor, secondaryColor);
		}
		
		private final <T extends Entity> void RegisterEntity(String entityName, Class<T> entity, Function<World, T> factory, int range, int updateFrequency, boolean velocityUpdates, int primaryColor, int secondaryColor)
		{
			ResourceLocation registryName = new ResourceLocation(ModInfo.MODID, entityName);
			EntityEntryBuilder<T> entryBuilder = EntityEntryBuilder.<T>create().id(registryName, currentId).name(registryName.toString().replace(':', '.')).entity(entity).tracker(range, updateFrequency, true).factory(factory);
			
			if(primaryColor != 0x0 || secondaryColor != 0x0)
				entryBuilder = entryBuilder.egg(primaryColor, secondaryColor);
			
			EntityEntry entityEntry = entryBuilder.build();
			registry.register(entityEntry);
			currentId++;
		}
	}
}
