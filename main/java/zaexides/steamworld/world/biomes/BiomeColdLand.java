package zaexides.steamworld.world.biomes;

import java.util.Random;

import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeColdLand extends BiomeSteamWorld
{
	private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1();
    private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2(false);
    
    private boolean snowy;
	
	public BiomeColdLand(boolean snowy, BiomeProperties biomeProperties) 
	{
		super(biomeProperties.setBaseHeight(1.0f).setHeightVariation(0.1f).setRainfall(0.45f));
		
		decorator.flowersPerChunk = 1;
		decorator.treesPerChunk = 1;
		decorator.extraTreeChance = 0.25f;
		decorator.grassPerChunk = 5;
		decorator.generateFalls = true;
		
		if(snowy)
		{
			spawnableCreatureList.clear();
			spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 5, 2, 3));
			spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
			
			spawnableMonsterList.clear();
			spawnableMonsterList.add(new Biome.SpawnListEntry(EntityStray.class, 40, 4, 4));
		}
		
		this.snowy = snowy;
	}
	
	@Override
	public float getSpawningChance() 
	{
		if(snowy)
			return 0.05f;
		else
			return super.getSpawningChance();
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) 
	{
		int chanceResult = rand.nextInt(10);
		
		if(chanceResult > 7)
			return super.getRandomTreeFeature(rand);
		else if(chanceResult > 2)
			return PINE_GENERATOR;
		else
			return SPRUCE_GENERATOR;
	}
	
	@Override
	protected void replaceBiomeBlock(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int primerX, int primerY,
			int primerZ, double noiseVal) 
	{
		if(snowy && noiseVal > 0.9 && chunkPrimerIn.getBlockState(primerX, primerY, primerZ) == topBlock)
		{
			if(chunkPrimerIn.getBlockState(primerX, primerY + 1, primerZ).getBlock() == Blocks.AIR)
				chunkPrimerIn.setBlockState(primerX, primerY + 1, primerZ, Blocks.SNOW_LAYER.getDefaultState());
		}
	}
}
