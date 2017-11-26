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
import zaexides.steamworld.worldgen.structures.dwarven.WorldGenDwarvenStructure;

public class DwarvenStructureGenerator implements IWorldGenerator
{
	private WorldGenerator structure_dwarven_core;
	
	public DwarvenStructureGenerator() 
	{
		structure_dwarven_core = new WorldGenDwarvenStructure();
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		if(!ConfigHandler.generateDwarvenStructure)
			return;
		
		if(world.provider.getDimension() == 0 && random.nextFloat() > 0.995f)
			GenerateStructure(structure_dwarven_core, world, random, chunkX, chunkZ, 10, 40);
	}
	
	private void GenerateStructure(WorldGenerator generator, World world, Random random, int x, int z, int heightMin, int heightMax)
	{
		int deltaHeight = heightMax - heightMin + 1;
		int local_x = x * 16 + random.nextInt(16);
		int local_y = heightMin + random.nextInt(deltaHeight);
		int local_z = z * 16 + random.nextInt(16);
		
		IBlockState blockState = world.getBlockState(new BlockPos(local_x, local_y, local_z));
		
		if(blockState.getBlock() == Blocks.STONE)
			generator.generate(world, random, new BlockPos(local_x, local_y, local_z));
	}
}
