package zaexides.steamworld.worldgen.biomes;

import java.util.Random;

import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import zaexides.steamworld.init.BlockInitializer;

public class BiomeForestIsland extends BiomeSteamWorld
{
	public static final WorldGenCanopyTree DARK_OAK_TREE = new WorldGenCanopyTree(false);
	
	public BiomeForestIsland(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.70f).setHeightVariation(0.03f).setTemperature(0.9f).setRainfall(0.42f));
		
		decorator.flowersPerChunk = 7;
		decorator.treesPerChunk = 17;
		decorator.extraTreeChance = 0.7f;
		decorator.mushroomsPerChunk = 3;
		decorator.bigMushroomsPerChunk = 1;
		decorator.reedsPerChunk = 1;
		
		riseAmount = -5;
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) 
	{
		return rand.nextInt(3) > 0 ? super.getRandomTreeFeature(rand) : DARK_OAK_TREE;
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
