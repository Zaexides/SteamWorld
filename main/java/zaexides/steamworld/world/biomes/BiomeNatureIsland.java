package zaexides.steamworld.world.biomes;

import java.util.Random;

import com.jcraft.jorbis.Block;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.ChunkPrimer;
import zaexides.steamworld.entity.EntitySkyFish;
import zaexides.steamworld.init.BlockInitializer;

public class BiomeNatureIsland extends BiomeSteamWorld
{
	public BiomeNatureIsland(String name) 
	{
		super(new BiomeProperties(name).setBaseHeight(0.75f).setHeightVariation(0.07f).setTemperature(0.7f).setRainfall(0.35f));
		
		decorator.flowersPerChunk = 24;
		decorator.treesPerChunk = 3;
		decorator.extraTreeChance = 0.3f;
		decorator.reedsPerChunk = 3;
		
		spawnableCreatureList.add(new SpawnListEntry(EntitySkyFish.class, 10, 3, 5));
	}
	
	@Override
	public EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) 
	{
		return EnumFlowerType.values()[rand.nextInt(EnumFlowerType.values().length)];
	}
	
	@Override
	public void addDefaultFlowers() 
	{
		for(EnumFlowerType ft : EnumFlowerType.values())
		{
			if(ft != EnumFlowerType.DANDELION && ft != EnumFlowerType.RED_TULIP)
				addFlower(Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), ft), 15);
		}
		super.addDefaultFlowers();
	}
}
