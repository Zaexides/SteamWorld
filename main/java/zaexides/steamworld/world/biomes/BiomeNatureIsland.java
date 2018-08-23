package zaexides.steamworld.world.biomes;

import java.util.Random;

import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import zaexides.steamworld.entity.EntitySkyFish;

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
