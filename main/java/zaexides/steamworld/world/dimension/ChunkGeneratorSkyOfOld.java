package zaexides.steamworld.world.dimension;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.world.mapgen.MapGenSWCaves;
import zaexides.steamworld.world.structure.tower.WorldGenTower;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkGeneratorSkyOfOld implements IChunkGenerator
{
	private final World world;
	private Random rand;
	private Biome[] biomes;
	private TerrainGeneratorSkyOfOld terrainGenerator;
	
	private MapGenBase caveGenerator = new MapGenSWCaves();
		
	public ChunkGeneratorSkyOfOld(World world) 
	{
		this.world = world;
		this.rand = new Random(world.getSeed() - 725);
		terrainGenerator = new TerrainGeneratorSkyOfOld(world, rand);
		caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, EventType.CAVE);
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
		
		caveGenerator.generate(world, x, z, chunkPrimer);
	
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
		int i = x * 16;
		int j = z * 16;
		
		BlockPos pos = new BlockPos(i, 0, j);
		Biome biome = world.getBiome(pos.add(16, 0, 16));
		
		if (biome.getTemperature() < 1.5f && rand.nextInt(100) <= (biome.getRainfall() * 25) && net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE))
        {
            int i1 = this.rand.nextInt(16) + 8;
            int j1 = this.rand.nextInt(256);
            int k1 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, pos.add(i1, j1, k1));
        }
		
		if (rand.nextInt(300) == 0 && net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE))
        {
            int i1 = this.rand.nextInt(16) + 8;
            int j1 = this.rand.nextInt(256);
            int k1 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(BlockInitializer.BLOCK_PRESERVATION_JUICE)).generate(this.world, this.rand, pos.add(i1, j1, k1));
        }
		
		if (((biome.getTemperature() >= 10.0f && rand.nextInt(10) == 0) || rand.nextInt(500) == 0) && net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE))
        {
            int i1 = this.rand.nextInt(16) + 8;
            int j1 = this.rand.nextInt(256);
            int k1 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.LAVA)).generate(this.world, this.rand, pos.add(i1, j1, k1));
        }
		
		biome.decorate(world, rand, pos);
		WorldEntitySpawner.performWorldGenSpawning(world, biome, i + 8, j + 8, 16, 16, rand);
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
