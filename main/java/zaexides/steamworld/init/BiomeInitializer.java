package zaexides.steamworld.init;

import org.apache.logging.log4j.Level;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.world.biomes.BiomeBlazingWithers;
import zaexides.steamworld.world.biomes.BiomeForestIsland;
import zaexides.steamworld.world.biomes.BiomeForgottenSky;
import zaexides.steamworld.world.biomes.BiomeColdLand;
import zaexides.steamworld.world.biomes.BiomeNatureIsland;
import zaexides.steamworld.world.biomes.BiomeWetlands;
import zaexides.steamworld.world.dimension.BiomeProviderSkyOfOld;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class BiomeInitializer 
{
	public static final BiomeForgottenSky FORGOTTEN_SKY = new BiomeForgottenSky("Forgotten Sky");
	public static final BiomeNatureIsland NATURE_ISLAND = new BiomeNatureIsland("Nature's Sky");
	public static final BiomeColdLand HIGHLAND_ISLAND = new BiomeColdLand(false, new BiomeProperties("Coldlands").setTemperature(0.5f));
	public static final BiomeColdLand HIGHLAND_ISLAND_SNOW = new BiomeColdLand(true, new BiomeProperties("Snowy Coldlands").setSnowEnabled().setTemperature(0.0f));
	public static final BiomeForestIsland FOREST_ISLAND = new BiomeForestIsland("Floating Woods");
	public static final BiomeWetlands WETLANDS = new BiomeWetlands("Floating Wetlands");
	
	public static final BiomeBlazingWithers BLAZING_WITHERS = new BiomeBlazingWithers("Blazing Withers");
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		registerBiome(FORGOTTEN_SKY, "Forgotten Sky", 2000, Type.VOID, Type.COLD);
		registerBiome(NATURE_ISLAND, "Nature Island", 700, Type.PLAINS, Type.LUSH);
		registerBiome(HIGHLAND_ISLAND, "Highland Island", 400, Type.COLD, Type.HILLS);
		registerBiome(HIGHLAND_ISLAND_SNOW, "Snow Highland Island", 80, Type.COLD, Type.HILLS, Type.SNOWY);
		registerBiome(FOREST_ISLAND, "Forest Island", 600, Type.FOREST, Type.LUSH, Type.DENSE);
		registerBiome(WETLANDS, "Floating Wetlands", 300, Type.WET, Type.SWAMP);
		
		registerBiome(BLAZING_WITHERS, "Blazing Withers", 10, Type.HOT, Type.DEAD, Type.NETHER, Type.DRY);
	}
	
	private static Biome registerBiome(Biome biome, String name, int weight, Type... types)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		
		int id = BiomeProviderSkyOfOld.biomes.size();
		BiomeProviderSkyOfOld.biomes.add(new BiomeEntry(biome, weight));
		return biome;
	}
}
