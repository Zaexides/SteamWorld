package zaexides.steamworld.world.structure;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;

public class WorldGenCrypt implements IWorldGenerator
{
	private static final float CHANCE_PER_CHUNK = 0.007f;
	private static final float CHANCE_MODIFIER_IN_DESERT = 0.5f;
	private static final float CHANCE_MODIFIER_IN_MOUNTAIN = 2.0f;
	private static final float CHANCE_MODIFIER_IN_SNOW = 1.2f;
	private static final int MIN_HEIGHT = 10, MAX_HEIGHT = 30;
	
	private static final ResourceLocation CRYPT_TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "ancitecrypt");
	private static final TemplateProcessorCrypt CRYPT_TEMPLATE_PROCESSOR = new TemplateProcessorCrypt();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		float generateChance = CHANCE_PER_CHUNK;
		Biome biome = world.getBiome(new BlockPos(chunkX, 22, chunkZ));
		if(biome.getBaseHeight() >= 1.0f)
			generateChance *= CHANCE_MODIFIER_IN_MOUNTAIN;
		if(!biome.canRain())
			generateChance *= CHANCE_MODIFIER_IN_DESERT;
		if(!biome.getEnableSnow())
			generateChance *= CHANCE_MODIFIER_IN_SNOW;
		
		if(world.provider.getDimension() == 0 && random.nextFloat() <= generateChance)
		{
			GenerateStructure(world, random, chunkX, chunkZ);
			if(biome == Biomes.EXTREME_HILLS)
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
		
		Biome biome = world.getBiome(pos);
		if(biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN)
			return;
		
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		PlacementSettings placementSettings = new PlacementSettings().setRotation(rotation);
		Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), CRYPT_TEMPLATE_LOCATION);
		
		template.addBlocksToWorld(world, pos, placementSettings);
		template.addBlocksToWorld(world, pos, CRYPT_TEMPLATE_PROCESSOR, placementSettings, 2);
	}
}
