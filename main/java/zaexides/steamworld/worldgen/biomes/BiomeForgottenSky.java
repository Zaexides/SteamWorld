package zaexides.steamworld.worldgen.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import zaexides.steamworld.blocks.BlockInitializer;

public class BiomeForgottenSky extends BiomeSteamWorld
{
	public BiomeForgottenSky(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.0f).setHeightVariation(0.0f).setTemperature(0.4f).setRainfall(0.5f));
	}
}
