package zaexides.steamworld.worldgen.dimension;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextEnd;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld;
import zaexides.steamworld.SteamWorld;

public class SteamWorldTerrainGenerator 
{
	private final World world;
	private final Random rand;
	
	public Biome[] biomes;
	
	private NoiseGeneratorOctaves mainPerlin;
	
	public SteamWorldTerrainGenerator(World world, Random random) 
	{
		this.world = world;
		this.rand = random;
		mainPerlin = new NoiseGeneratorOctaves(rand, 8);
	}
	
	public void generate(int chunkX, int chunkZ, ChunkPrimer primer)
	{
		final int chunkSize = 16;
		final int terrainBaseHeight = 90;
		
		double[] height = null;
		height = mainPerlin.generateNoiseOctaves(height, chunkX * 16, chunkZ * 16, 16, 16, 0.8, 0.8, 0);
		for(int z = 0; z < chunkSize; z++)
		{
			for(int x = 0; x < chunkSize; x++)
			{
				double h = (int) height[z + 16 * x] * 1;
				if(h < 0)
					h = -h;
				
				Biome biome = world.getBiome(new BlockPos(x,0,z));
				h *= biome.getHeightVariation();
				h += biome.getBaseHeight() + terrainBaseHeight;
				
				for(int y = 0; y <= h; y++)
				{
					IBlockState blockState = Blocks.STONE.getDefaultState();
					if((h - y) < 1)
						blockState = Blocks.GRASS.getDefaultState();
					else if((h - y) < 4)
						blockState = Blocks.DIRT.getDefaultState();
					
					primer.setBlockState(x, y, z, blockState);
				}
			}
		}
	}
}
