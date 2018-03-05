package zaexides.steamworld.worldgen.dimension;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;

public class SteamWorldChunkGenerator implements IChunkGenerator
{
	private final World world;
	private Random rand;
	private Biome[] biomes;
	private SteamWorldTerrainGenerator terrainGenerator;
	
	private MapGenBase caveGenerator = new MapGenCaves();
	private MapGenBase ravineGenerator = new MapGenRavine();
	
	public SteamWorldChunkGenerator(World world) 
	{
		this.world = world;
		this.rand = new Random(world.getSeed() - 725);
		terrainGenerator = new SteamWorldTerrainGenerator(world, rand);
		caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, EventType.CAVE);
		ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, EventType.RAVINE);
	}
	
	@Override
	public Chunk generateChunk(int x, int z) 
	{
		ChunkPrimer chunkPrimer = new ChunkPrimer();
		biomes = world.getBiomeProvider().getBiomesForGeneration(biomes, x * 4 - 2, z * 4 - 2, 10, 10);
		terrainGenerator.biomes = biomes;
		
		terrainGenerator.generate(x, z, chunkPrimer);
		
		biomes = world.getBiomeProvider().getBiomesForGeneration(biomes, x * 16, z * 16, 16, 16);
		terrainGenerator.replaceBiomeBlocks(x, z, chunkPrimer, this, biomes);
	
		Chunk chunk = new Chunk(world, chunkPrimer, x, z);
		byte[] biomeArray = chunk.getBiomeArray();
		for(int i = 0; i < biomeArray.length; ++i)
			biomeArray[i] = (byte) Biome.getIdForBiome(biomes[i]);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int x, int z) 
	{
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) 
	{
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) 
	{
		Biome biome = world.getBiome(pos);
		return biome.getSpawnableList(creatureType);
	}

	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
			boolean findUnexplored) 
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) 
	{
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) 
	{
		return false;
	}

}
