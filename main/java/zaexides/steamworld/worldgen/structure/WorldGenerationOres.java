package zaexides.steamworld.worldgen.structure;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import scala.sys.process.processInternal;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.blocks.SteamWorldBlockOre;

public class WorldGenerationOres implements IWorldGenerator
{
	private WorldGenerator ore_steaite, sky_ore_steaite, sky_ore_coal, sky_ore_iron, sky_ore_gold, sky_ore_diamond;
	
	private static final int STEAITE_ORE_VEIN_SIZE = 5;
	private static final int STEAITE_GENERATION_CHANCE = 20, STEAITE_GENERATION_CHANCE_SWDIM = 25;
	private static final int STEAITE_MIN_HEIGHT = 5, STEAITE_MAX_HEIGHT = 20, STEAITE_MAX_HEIGH_SWDIM = 80;
	
	private static final int COAL_ORE_VEIN_SIZE = 10;
	private static final int COAL_GENERATION_CHANCE = 30;
	private static final int COAL_MIN_HEIGHT = 5, COAL_MAX_HEIGHT = 120;
	
	private static final int IRON_ORE_VEIN_SIZE = 7;
	private static final int IRON_GENERATION_CHANCE = 10;
	private static final int IRON_MIN_HEIGHT = 50, IRON_MAX_HEIGHT = 85;
	
	private static final int GOLD_ORE_VEIN_SIZE = 5;
	private static final int GOLD_GENERATION_CHANCE = 5;
	private static final int GOLD_MIN_HEIGHT = 45, GOLD_MAX_HEIGHT = 60;
	
	private static final int DIAMOND_ORE_VEIN_SIZE = 3;
	private static final int DIAMOND_GENERATION_CHANCE = 3;
	private static final int DIAMOND_MIN_HEIGHT = 5, DIAMOND_MAX_HEIGHT = 55;
	
	public WorldGenerationOres()
	{
		ore_steaite = new WorldGenMinable(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.OVERWORLD_STEAITE.getMeta()), STEAITE_ORE_VEIN_SIZE);
		sky_ore_steaite = new WorldGenMinable(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.SKY_STEAITE.getMeta()), STEAITE_ORE_VEIN_SIZE, new SkyStonePredicate());
		sky_ore_coal = new WorldGenMinable(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.SKY_COAL.getMeta()), COAL_ORE_VEIN_SIZE, new SkyStonePredicate());
		sky_ore_iron = new WorldGenMinable(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.SKY_IRON.getMeta()), IRON_ORE_VEIN_SIZE, new SkyStonePredicate());
		sky_ore_gold = new WorldGenMinable(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.SKY_GOLD.getMeta()), GOLD_ORE_VEIN_SIZE, new SkyStonePredicate());
		sky_ore_diamond = new WorldGenMinable(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.SKY_DIAMOND.getMeta()), DIAMOND_ORE_VEIN_SIZE, new SkyStonePredicate());
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		int dim = world.provider.getDimension();
		if(dim == 0)
		{
			if(ConfigHandler.generateSteaiteOre)
				GenerateOre(ore_steaite, world, random, chunkX, chunkZ, STEAITE_GENERATION_CHANCE, STEAITE_MIN_HEIGHT, STEAITE_MAX_HEIGHT);
		}
		else if(dim == ConfigHandler.dimensionId)
		{
			if(ConfigHandler.generateSteaiteOre)
				GenerateOre(sky_ore_steaite, world, random, chunkX, chunkZ, STEAITE_GENERATION_CHANCE_SWDIM, STEAITE_MIN_HEIGHT, STEAITE_MAX_HEIGH_SWDIM);
			GenerateOre(sky_ore_coal, world, random, chunkX, chunkZ, COAL_GENERATION_CHANCE, COAL_MIN_HEIGHT, COAL_MAX_HEIGHT);
			GenerateOre(sky_ore_iron, world, random, chunkX, chunkZ, IRON_GENERATION_CHANCE, IRON_MIN_HEIGHT, IRON_MAX_HEIGHT);
			GenerateOre(sky_ore_gold, world, random, chunkX, chunkZ, GOLD_GENERATION_CHANCE, GOLD_MIN_HEIGHT, GOLD_MAX_HEIGHT);
			GenerateOre(sky_ore_diamond, world, random, chunkX, chunkZ, DIAMOND_GENERATION_CHANCE, DIAMOND_MIN_HEIGHT, DIAMOND_MAX_HEIGHT);
		}
	}
	
	private void GenerateOre(WorldGenerator generator, World world, Random random, int x, int z, int chance, int heightMin, int heightMax)
	{
		int deltaHeight = heightMax - heightMin + 1;
		for(int i = 0; i < chance; i++)
		{
			int local_x = x * 16 + random.nextInt(16);
			int local_y = heightMin + random.nextInt(deltaHeight);
			int local_z = z * 16 + random.nextInt(16);
			
			generator.generate(world, random, new BlockPos(local_x, local_y, local_z));
		}
	}
	
	static class SkyStonePredicate implements Predicate<IBlockState>
    {
        public boolean apply(IBlockState state)
        {
            if (state != null && state.getBlock() == BlockInitializer.BLOCK_DECORATIVE && BlockInitializer.BLOCK_DECORATIVE.getMetaFromState(state) == BlockDecorative.EnumType.SKY_STONE.getMeta())
                return true;
            else
                return false;
        }
    }
}
