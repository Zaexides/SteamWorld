package zaexides.steamworld.worldgen.biomes;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;
import zaexides.steamworld.init.BlockInitializer;

public class BiomeWetlands extends BiomeSteamWorld
{
	public static final WorldGenerator LAKE_GENERATOR = new WorldGenLakes(Blocks.WATER);
	
	public BiomeWetlands(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.2f).setHeightVariation(0.002f).setTemperature(0.9f).setRainfall(0.7f));
		
		decorator.flowersPerChunk = 7;
		decorator.treesPerChunk = 1;
		decorator.extraTreeChance = 0.2f;
		decorator.reedsPerChunk = 6;
		decorator.waterlilyPerChunk = 8;
		decorator.mushroomsPerChunk = 2;
		
		spawnableMonsterList.clear();
		spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 20, 1, 3));
		spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 5, 2, 5));
		spawnableMonsterList.add(new SpawnListEntry(EntityWitch.class, 10, 1, 1));
	}
	
	@Override
	public float getSpawningChance() 
	{
		return 0.01f;
	}
	
	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos) 
	{
		super.decorate(worldIn, rand, pos);
		for(int i = 0; i < 4; i++)
		{
			if(rand.nextInt(5) == 0)
				LAKE_GENERATOR.generate(worldIn, rand, pos);
		}
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) 
	{
		return SWAMP_FEATURE;
	}
}
