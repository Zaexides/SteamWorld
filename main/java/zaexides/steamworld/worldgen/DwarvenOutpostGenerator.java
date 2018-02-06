package zaexides.steamworld.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.worldgen.outpost.WorldGenDwarvenOutpost;

public class DwarvenOutpostGenerator implements IWorldGenerator
{
	private WorldGenerator structure_outpost;
	
	private static final float CHANCE_PER_CHUNK = 0.15f;
	private static final int MIN_HEIGHT = 60, MAX_HEIGHT = 90;
	
	public DwarvenOutpostGenerator() 
	{
		structure_outpost = new WorldGenDwarvenOutpost();
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		if(!ConfigHandler.generateDwarvenStructure)
			return;
		
		if(world.provider.getDimension() == 0 && random.nextFloat() <= CHANCE_PER_CHUNK)
			GenerateStructure(structure_outpost, world, random, chunkX, chunkZ, MIN_HEIGHT, MAX_HEIGHT);
	}
	
	private void GenerateStructure(WorldGenerator generator, World world, Random random, int x, int z, int heightMin, int heightMax)
	{
		int deltaHeight = heightMax - heightMin + 1;
		int local_x = x * 16 + random.nextInt(16);
		int local_y = heightMin + random.nextInt(deltaHeight);
		int local_z = z * 16 + random.nextInt(16);
		BlockPos pos = new BlockPos(local_x, local_y, local_z);
		
		if(world.getBlockState(pos).getBlock() == Blocks.GRASS)
			structure_outpost.generate(world, random, pos);
	}
}
