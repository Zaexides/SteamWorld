package zaexides.steamworld.worldgen.biomes;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import zaexides.steamworld.init.BlockInitializer;

public class BiomeForgottenSky extends BiomeSteamWorld
{
	public BiomeForgottenSky(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.0f).setHeightVariation(0.0f).setTemperature(0.4f).setRainfall(0.5f));
	}
	
	@Override
	public void generateBiomeTerrainSteamWorld(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
	{
		int topLayerHeight = -1;
		int primerX = x & 15;
		int primerZ = z & 15;
		
		for(int y = 255; y >= 0; y--)
		{
			chunkPrimerIn.setBlockState(primerX, y, primerZ, AIR);
		}
	}
}
