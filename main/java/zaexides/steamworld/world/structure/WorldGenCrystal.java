package zaexides.steamworld.world.structure;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.DungeonHooks.DungeonMob;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.blocks.SteamWorldBlockOre;
import zaexides.steamworld.init.BiomeInitializer;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.EntityInitializer;

public class WorldGenCrystal implements IWorldGenerator
{
	private static final float CHANCE_PER_CHUNK = 0.1f;
	private static final int MIN_HEIGHT = 20, MAX_HEIGHT = 230;
	private static final int MIN_SIZE = 2, MAX_SIZE = 4;
	private static final float SPAWNER_CHANCE = 0.2f;
	private static final float ORE_GOLD_CHANCE = SPAWNER_CHANCE + 0.1f;
	private static final float ORE_EMERALD_CHANCE = ORE_GOLD_CHANCE + 0.1f;
	
	private static final IBlockState BLOCK_STATE = BlockInitializer.BLOCK_CRYSTAL.getDefaultState();
	private static final IBlockState SPAWNER_STATE = Blocks.MOB_SPAWNER.getDefaultState();
	private static final IBlockState NUGGET_ORE_GOLD_STATE = BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.CRYSTAL_GOLD.getMeta());
	private static final IBlockState NUGGET_ORE_EMERALD_STATE = BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.CRYSTAL_EMERALD.getMeta());
	
	private static ArrayList<DungeonMob> spawnerMobs = new ArrayList<DungeonMob>();
	
	public WorldGenCrystal() 
	{
		spawnerMobs.add(new DungeonMob(10, EntityInitializer.PROSHELLOR));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(world.provider.getDimension() == ConfigHandler.dimensionId && random.nextFloat() <= CHANCE_PER_CHUNK)
		{
			GenerateStructure(world, random, chunkX, chunkZ);
		}
	}
	
	private void GenerateStructure(World world, Random random, int x, int z)
	{
		int deltaHeight = MAX_HEIGHT - MIN_HEIGHT + 1;
		int local_x = x * 16 + random.nextInt(8) + 4;
		int local_y = MIN_HEIGHT + random.nextInt(deltaHeight);
		int local_z = z * 16 + random.nextInt(8) + 4;
		BlockPos pos = new BlockPos(local_x, local_y, local_z);
		
		if(world.getBiome(pos).equals(BiomeInitializer.FORGOTTEN_SKY))
		{
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
							world.setBlockState(specificPos, BLOCK_STATE);
						}
					}
				}
			}
			
			float randVal = random.nextFloat();
			if(randVal < SPAWNER_CHANCE)
			{
				world.setBlockState(pos, SPAWNER_STATE);
				TileEntity tileEntity = world.getTileEntity(pos);
				if(tileEntity instanceof TileEntityMobSpawner)
				{
					((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic().setEntityId(WeightedRandom.getRandomItem(random, spawnerMobs).type);
				}
			}
			else if(randVal < ORE_GOLD_CHANCE)
				world.setBlockState(pos, NUGGET_ORE_GOLD_STATE);
			else if(randVal < ORE_EMERALD_CHANCE)
				world.setBlockState(pos, NUGGET_ORE_EMERALD_STATE);
		}
	}
}
