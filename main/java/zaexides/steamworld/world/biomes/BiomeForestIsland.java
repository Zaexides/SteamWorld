package zaexides.steamworld.world.biomes;

import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import zaexides.steamworld.entity.EntityAnemone;
import zaexides.steamworld.entity.EntitySkyFish;
import zaexides.steamworld.init.BlockInitializer;

public class BiomeForestIsland extends BiomeSteamWorld
{
	private static final WorldGenCanopyTree DARK_OAK_TREE = new WorldGenCanopyTree(false);
	private static final IBlockState LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
	private static final IBlockState LEAVES = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
	
	public BiomeForestIsland(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.70f).setHeightVariation(0.03f).setTemperature(0.9f).setRainfall(0.42f));
		
		decorator.flowersPerChunk = 7;
		decorator.treesPerChunk = 17;
		decorator.extraTreeChance = 0.7f;
		decorator.mushroomsPerChunk = 3;
		decorator.bigMushroomsPerChunk = 1;
		decorator.reedsPerChunk = 1;
		
		spawnableCreatureList.add(new SpawnListEntry(EntitySkyFish.class, 10, 3, 5));
		spawnableCreatureList.add(new SpawnListEntry(EntityAnemone.class, 5, 2, 3));
		spawnableMonsterList.add(new SpawnListEntry(EntityAnemone.class, 5, 2, 3));
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) 
	{
		int randVal = rand.nextInt(10);
		if(randVal < 1)
			return DARK_OAK_TREE;
		else if(randVal < 4)
			return new WorldGenTrees(false, 3 + rand.nextInt(5), LOG, LEAVES, true);
		else
			return super.getRandomTreeFeature(rand);
	}
	
	@Override
	public EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) 
	{
		if(rand.nextInt(3) > 0)
			return super.pickRandomFlower(rand, pos);
		else
			return EnumFlowerType.values()[rand.nextInt(EnumFlowerType.values().length)];
	}
}
