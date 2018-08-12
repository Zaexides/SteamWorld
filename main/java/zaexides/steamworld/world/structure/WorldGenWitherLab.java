package zaexides.steamworld.world.structure;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;

public class WorldGenWitherLab implements IWorldGenerator
{
	private static final float CHANCE_PER_CHUNK = 0.001f;
	private static final int MIN_HEIGHT = 20, MAX_HEIGHT = 100;
	
	private static final ResourceLocation LAB_TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "sw_wither_lab");
	private static final TemplateProcessorLab LAB_TEMPLATE_PROCESSOR = new TemplateProcessorLab();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(!ConfigHandler.generateDwarvenStructure)
			return;
		
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
		
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		PlacementSettings placementSettings = new PlacementSettings().setRotation(rotation);
		Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), LAB_TEMPLATE_LOCATION);
		
		LAB_TEMPLATE_PROCESSOR.Reset();
		template.addBlocksToWorld(world, pos, placementSettings);
		template.addBlocksToWorld(world, pos, LAB_TEMPLATE_PROCESSOR, placementSettings, 2);
	}

}
