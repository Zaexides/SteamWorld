package zaexides.steamworld.world.biomes;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import zaexides.steamworld.entity.EntityPropellorShell;
import zaexides.steamworld.entity.EntitySkyFish;

public class BiomeForgottenSky extends BiomeSteamWorld
{
	public BiomeForgottenSky(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.0f).setHeightVariation(0.0f).setTemperature(0.4f).setRainfall(0.5f));
		
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		
		this.spawnableCreatureList.add(new SpawnListEntry(EntitySkyFish.class, 10, 3, 5));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPropellorShell.class, 20, 3, 5));
	}
	
	@Override
	public void generateBiomeTerrainSteamWorld(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
	{
		if(noiseVal < 0.1)
			super.generateBiomeTerrainSteamWorld(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
		int topLayerHeight = -1;
		int primerX = x & 15;
		int primerZ = z & 15;
		
		for(int y = 255; y >= 0; y--)
		{
			if(chunkPrimerIn.getBlockState(primerX, y, primerZ).getMaterial() != Material.AIR)
				chunkPrimerIn.setBlockState(primerX, y, primerZ, AIR);
		}
	}
}