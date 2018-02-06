package zaexides.steamworld.worldgen;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	private static final float CHANCE_PER_CHUNK = 0.05f;
	private static final int MIN_HEIGHT = 10, MAX_HEIGHT = 30;
	
	private static final ResourceLocation CRYPT_TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "ancitecrypt");
	private static final TemplateProcessorCrypt CRYPT_TEMPLATE_PROCESSOR = new TemplateProcessorCrypt();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(!ConfigHandler.generateDwarvenStructure)
			return;
		
		if(world.provider.getDimension() == 0 && random.nextFloat() <= CHANCE_PER_CHUNK)
			GenerateStructure(world, random, chunkX, chunkZ);
	}
	
	private void GenerateStructure(World world, Random random, int x, int z)
	{
		int deltaHeight = MAX_HEIGHT - MIN_HEIGHT + 1;
		int local_x = x * 8 + random.nextInt(8);
		int local_y = MIN_HEIGHT + random.nextInt(deltaHeight);
		int local_z = z * 8 + random.nextInt(8);
		BlockPos pos = new BlockPos(local_x, local_y, local_z);
		
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		PlacementSettings placementSettings = new PlacementSettings().setRotation(rotation);
		Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), CRYPT_TEMPLATE_LOCATION);
		
		template.addBlocksToWorld(world, pos, placementSettings);
		template.addBlocksToWorld(world, pos, CRYPT_TEMPLATE_PROCESSOR, placementSettings, 2);
	}
}
