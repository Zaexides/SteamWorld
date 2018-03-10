package zaexides.steamworld.worldgen.dimension;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerSteamWorldBiomes extends GenLayer
{
	public GenLayerSteamWorldBiomes(long seed) 
	{
		super(seed);
	}

	@Override
	public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight) 
	{
		int[] ints = IntCache.getIntCache(areaWidth * areaHeight);
		for(int z = 0; z < areaHeight; z++)
		{
			for(int x = 0; x < areaWidth; x++)
			{
				initChunkSeed(areaX + x, areaZ + z);
				
				int id = SteamWorldBiomeProvider.biomeWeights.get(nextInt(SteamWorldBiomeProvider.biomeWeights.size()));
				Biome biome = SteamWorldBiomeProvider.biomes.get(id);
				ints[x + z * areaWidth] = Biome.getIdForBiome(biome);
			}
		}
		return ints;
	}
}
