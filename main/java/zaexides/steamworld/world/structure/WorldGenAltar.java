package zaexides.steamworld.world.structure;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;

public class WorldGenAltar implements IWorldGenerator
{
	private static final float CHANCE_PER_CHUNK = 0.001f;
	private static final int MIN_HEIGHT = 10, MAX_HEIGHT = 30;
	
	private static final ResourceLocation TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "corite_altar");
	private static final TemplateProcessorAltar ALTAR_TEMPLATE_PROCESSOR = new TemplateProcessorAltar();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		for(int i = 0; i < 6; i++)
			random.nextFloat(); //Mix up the number to not end up getting the same values as the other worldgen structures.
		//Not sure™ why it's doing that.
		
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
		
		PlacementSettings placementSettings = new PlacementSettings();
		Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), TEMPLATE_LOCATION);
		
		template.addBlocksToWorld(world, pos, placementSettings);
		template.addBlocksToWorld(world, pos, ALTAR_TEMPLATE_PROCESSOR, placementSettings, 2);
	}
}
