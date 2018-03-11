package zaexides.steamworld.worldgen.biomes;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import zaexides.steamworld.blocks.BlockInitializer;

public class BiomeNatureIsland extends BiomeSteamWorld
{
	public BiomeNatureIsland(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.75f).setHeightVariation(0.07f).setTemperature(0.7f).setRainfall(0.35f));
		
		decorator.flowersPerChunk = 16;
		decorator.treesPerChunk = 3;
		decorator.extraTreeChance = 0.3f;
	}
}
