package zaexides.steamworld.world.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.DungeonHooks.DungeonMob;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.init.BiomeInitializer;
import zaexides.steamworld.init.EntityInitializer;
import zaexides.steamworld.utility.WeightedBlock;

public class WorldGenCrystal implements IWorldGenerator
{
	private static final int MIN_SIZE = 2, MAX_SIZE = 5;
	
	private final float chance;
	private final float chanceForgottenSky;
	private final int amount;
	
	private final int minHeight, maxHeight;
	
	private final IBlockState crystal;
	private final List<WeightedBlock> centerBlocks;
	
	private static final List<DungeonMob> spawnerMobs = Arrays.asList(
			new DungeonMob(10,  EntityInitializer.PROSHELLOR)
			);
	
	public WorldGenCrystal(IBlockState crystal, List<WeightedBlock> centerBlocks, float chance, float chanceForgottenSky, int amount) 
	{
		this(crystal, centerBlocks, chance, chanceForgottenSky, amount, 20, 230);
	}
	
	public WorldGenCrystal(IBlockState crystal, List<WeightedBlock> centerBlocks, float chance, float chanceForgottenSky, int amount, int minHeight, int maxHeight) 
	{
		this.crystal = crystal;
		this.centerBlocks = centerBlocks;
		this.chance = chance;
		this.chanceForgottenSky = chanceForgottenSky;
		this.amount = amount;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(world.provider.getDimension() == ConfigHandler.dimensionId)
		{
			for(int i = 0; i < amount; i++)
			{
				GenerateStructure(world, random, chunkX, chunkZ);
			}
		}
	}
	
	private void GenerateStructure(World world, Random random, int x, int z)
	{
		int deltaHeight = maxHeight - minHeight + 1;
		int local_x = x * 16 + random.nextInt(8) + 4;
		int local_y = minHeight + random.nextInt(deltaHeight);
		int local_z = z * 16 + random.nextInt(8) + 4;
		BlockPos pos = new BlockPos(local_x, local_y, local_z);
		
		float randVal = random.nextFloat();
		if(world.getBiome(pos).equals(BiomeInitializer.FORGOTTEN_SKY))
		{
			if(randVal >= chanceForgottenSky)
				return;
		}
		else if(randVal >= chance)
			return;
		
		int size = random.nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE;
		for(int xOff = -size + 1; xOff < size; xOff++)
		{
			for(int yOff = -size + 1; yOff < size; yOff++)
			{
				for(int zOff = -size + 1; zOff < size; zOff++)
				{
					if((Math.abs(xOff) + Math.abs(yOff) + Math.abs(zOff)) < size)
					{
						BlockPos specificPos = pos.add(xOff, yOff, zOff);
						world.setBlockState(specificPos, crystal);
					}
				}
			}
		}
		
		randVal = random.nextFloat();
		world.setBlockState(pos, WeightedRandom.getRandomItem(random, centerBlocks).blockState);
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null && tileEntity instanceof TileEntityMobSpawner)
			((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(WeightedRandom.getRandomItem(random, spawnerMobs).type);
	}
}
