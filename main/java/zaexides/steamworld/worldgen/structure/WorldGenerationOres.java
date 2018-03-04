package zaexides.steamworld.worldgen.structure;

import java.util.Random;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import scala.sys.process.processInternal;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.blocks.BlockInitializer;

public class WorldGenerationOres implements IWorldGenerator
{
	private WorldGenerator ore_steaite;
	
	private static final int ORE_VEIN_SIZE = 5;
	private static final int GENERATION_CHANCE = 20;
	private static final int MIN_HEIGHT = 5, MAX_HEIGHT = 20;
	
	public WorldGenerationOres()
	{
		ore_steaite = new WorldGenMinable(BlockInitializer.ORE_STEAITE.getDefaultState(), ORE_VEIN_SIZE);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		switch(world.provider.getDimension())
		{
			case 0:
			if(ConfigHandler.generateSteaiteOre)
				GenerateOre(ore_steaite, world, random, chunkX, chunkZ, GENERATION_CHANCE, MIN_HEIGHT, MAX_HEIGHT);
			break;
		}
	}
	
	private void GenerateOre(WorldGenerator generator, World world, Random random, int x, int z, int chance, int heightMin, int heightMax)
	{
		int deltaHeight = heightMax - heightMin + 1;
		for(int i = 0; i < chance; i++)
		{
			int local_x = x * 16 + random.nextInt(16);
			int local_y = heightMin + random.nextInt(deltaHeight);
			int local_z = z * 16 + random.nextInt(16);
			
			generator.generate(world, random, new BlockPos(local_x, local_y, local_z));
		}
	}
}
