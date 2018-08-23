package zaexides.steamworld.world.biomes;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import zaexides.steamworld.blocks.BlockSWFlower;
import zaexides.steamworld.entity.EntityEclipseStalker;
import zaexides.steamworld.init.BlockInitializer;

public class BiomeBlazingWithers extends BiomeSteamWorld
{
	private IBlockState witherWeedState;
	
	public BiomeBlazingWithers(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.65f).setHeightVariation(0.11f).setTemperature(12f).setRainfall(0f).setRainDisabled());
		
		decorator.flowersPerChunk = 0;
		decorator.treesPerChunk = 0;
		decorator.extraTreeChance = 0;
		decorator.grassPerChunk = 0;
		decorator.deadBushPerChunk = 1;
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
		spawnableMonsterList.clear();
		
		spawnableMonsterList.add(new SpawnListEntry(EntityBlaze.class, 80, 1, 3));
		spawnableMonsterList.add(new SpawnListEntry(EntityWitherSkeleton.class, 20, 1, 2));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEclipseStalker.class, 50, 1, 1));
		
		witherWeedState = BlockInitializer.BLOCK_FLOWER.getStateFromMeta(BlockSWFlower.EnumType.WITHER.getMeta());
		addFlower(witherWeedState, 40);
	}
	
	@Override
	public void generateBiomeTerrainSteamWorld(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z,
			double noiseVal) 
	{
		if(noiseVal < 0.35)
		{
			if(noiseVal > -0.4)
				topBlock = Blocks.NETHERRACK.getDefaultState();
			else
				topBlock = Blocks.MAGMA.getDefaultState();
			fillerBlock = Blocks.NETHERRACK.getDefaultState();
		}
		else
		{
			topBlock = Blocks.GRASS.getDefaultState();
			fillerBlock = Blocks.DIRT.getDefaultState();
		}
		super.generateBiomeTerrainSteamWorld(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
	}
	
	@Override
	protected void onTopBlockGen(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int primerX, int primerY,
			int primerZ, double noiseVal) 
	{
		if((noiseVal > 0.97 && rand.nextInt(20) == 0) || rand.nextInt(100) == 0)
		{
			if(chunkPrimerIn.getBlockState(primerX, primerY, primerZ).getBlock() == Blocks.GRASS)
				chunkPrimerIn.setBlockState(primerX, primerY + 1, primerZ, witherWeedState);
		}
	}
	
	@Override
	protected void replaceBiomeBlock(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int primerX, int primerY,
			int primerZ, double noiseVal) 
	{
		if(noiseVal > -0.6)
		{
			if(chunkPrimerIn.getBlockState(primerX, primerY, primerZ).getBlock() == BlockInitializer.BLOCK_DECORATIVE)
			{
				if(noiseVal > 0.2)
					chunkPrimerIn.setBlockState(primerX, primerY, primerZ, Blocks.MAGMA.getDefaultState());
				else
					chunkPrimerIn.setBlockState(primerX, primerY, primerZ, Blocks.NETHERRACK.getDefaultState());
			}
		}
		else
			chunkPrimerIn.setBlockState(primerX, primerY, primerZ, Blocks.AIR.getDefaultState());
	}
	
	@Override
	public float getSpawningChance()
	{
		return 0.02f;
	}
}
