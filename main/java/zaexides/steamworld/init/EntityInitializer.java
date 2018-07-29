package zaexides.steamworld.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
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
import zaexides.steamworld.entity.EntityEclipseStalker;
import zaexides.steamworld.entity.EntityPropellorShell;
import zaexides.steamworld.entity.EntitySkyFish;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class EntityInitializer 
{
	private static int currentId = 0;
	
	public static final ResourceLocation FLYING_FISH = new ResourceLocation(ModInfo.MODID, "skyfish");
	public static final ResourceLocation ANEMONE = new ResourceLocation(ModInfo.MODID, "anemone");
	public static final ResourceLocation PROSHELLOR = new ResourceLocation(ModInfo.MODID, "propellorshell");
	public static final ResourceLocation ECLIPSE_STALKER = new ResourceLocation(ModInfo.MODID, "eclipse_stalker");
	
	public static final ResourceLocation PROJECTILE_ANEMONE_STINGER = new ResourceLocation(ModInfo.MODID, "anemone_stinger");
	
	@SubscribeEvent
	public static void RegisterEntities(RegistryEvent.Register<EntityEntry> event)
	{
		RegisterHelper helper = new RegisterHelper(event.getRegistry());
		
		helper.RegisterEntityLiving(FLYING_FISH, EntitySkyFish.class, EntitySkyFish::new, 0x6D7FE8, 0xEDEDD5).RegisterEntitySpawnPlacement(EntitySkyFish.class, SpawnPlacementType.IN_AIR);
		helper.RegisterEntityLiving(ANEMONE, EntityAnemone.class, EntityAnemone::new, 0x493FB5, 0xFF00DC);
		helper.RegisterEntityLiving(PROSHELLOR, EntityPropellorShell.class, EntityPropellorShell::new, 0xABB070, 0x61AE40).RegisterEntitySpawnPlacement(EntityPropellorShell.class, SpawnPlacementType.IN_AIR);
		helper.RegisterEntityLiving(ECLIPSE_STALKER, EntityEclipseStalker.class, EntityEclipseStalker::new, 0x454344, 0xF7CC40).RegisterEntitySpawnPlacement(EntityEclipseStalker.class, SpawnPlacementType.IN_AIR);
		
		helper.RegisterEntityNonLiving(PROJECTILE_ANEMONE_STINGER, EntityAnemoneStinger.class, EntityAnemoneStinger::new);
	}
	
	private static class RegisterHelper
	{
		private IForgeRegistry<EntityEntry> registry;
		
		public RegisterHelper(IForgeRegistry<EntityEntry> registry)
		{
			this.registry = registry;
		}
		
		final <T extends Entity> RegisterHelper RegisterEntityNonLiving(ResourceLocation registryName, Class<T> entity, Function<World, T> factory)
		{
			RegisterEntity(registryName, entity, factory, 64, 1, false, 0, 0);
			return this;
		}

		final <T extends Entity> RegisterHelper RegisterEntityLiving(ResourceLocation registryName, Class<T> entity, Function<World, T> factory, int primaryColor, int secondaryColor)
		{
			RegisterEntity(registryName, entity, factory, 64, 3, true, primaryColor, secondaryColor);
			return this;
		}
		
		final <T extends Entity> RegisterHelper RegisterEntitySpawnPlacement(Class<T> entity, EntityLiving.SpawnPlacementType spawnPlacementType)
		{
			EntitySpawnPlacementRegistry.setPlacementType(entity, spawnPlacementType);
			return this;
		}
		
		private final <T extends Entity> void RegisterEntity(ResourceLocation registryName, Class<T> entity, Function<World, T> factory, int range, int updateFrequency, boolean velocityUpdates, int primaryColor, int secondaryColor)
		{
			EntityEntryBuilder<T> entryBuilder = EntityEntryBuilder.<T>create().id(registryName, currentId).name(registryName.toString().replace(':', '.')).entity(entity).tracker(range, updateFrequency, true).factory(factory);
			
			if(primaryColor != 0x0 || secondaryColor != 0x0)
				entryBuilder = entryBuilder.egg(primaryColor, secondaryColor);
			
			EntityEntry entityEntry = entryBuilder.build();
			registry.register(entityEntry);
			currentId++;
		}
	}
}
