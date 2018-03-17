package zaexides.steamworld.worldgen.biomes;

import org.apache.logging.log4j.Level;

import net.minecraft.world.biome.Biome;
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
import zaexides.steamworld.worldgen.dimension.SteamWorldBiomeProvider;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class BiomeInitializer 
{
	public static final BiomeForgottenSky FORGOTTEN_SKY = new BiomeForgottenSky("Forgotten Sky");
	public static final BiomeNatureIsland NATURE_ISLAND = new BiomeNatureIsland("Nature Island");
	public static final BiomeBlazingWithers BLAZING_WITHERS = new BiomeBlazingWithers("Blazing Withers");
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		registerBiome(FORGOTTEN_SKY, "Forgotten Sky", 2000, Type.VOID, Type.COLD);
		registerBiome(NATURE_ISLAND, "Nature Island", 700, Type.PLAINS, Type.LUSH);
		registerBiome(BLAZING_WITHERS, "Blazing Withers", 50, Type.HOT, Type.DEAD, Type.NETHER, Type.DRY);
	}
	
	private static Biome registerBiome(Biome biome, String name, int weight, Type... types)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		
		int id = SteamWorldBiomeProvider.biomes.size();
		SteamWorldBiomeProvider.biomes.add(biome);
		for(int i = 0; i < weight; i++)
			SteamWorldBiomeProvider.biomeWeights.add(id);
		return biome;
	}
}
