package zaexides.steamworld.world.structure.tower;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.utility.interfaces.IResettable;

public class WorldGenTower implements IWorldGenerator
{
	private static final float CHANCE_PER_CHUNK = 0.00021f;
	private static final int MIN_HEIGHT = 50, MAX_HEIGHT = 150;
	
	private static final ResourceLocation BASE_TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "tower/sw_tower_base");
	private static final ResourceLocation TOP_TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "tower/sw_tower_top");
	
	private static ResourceLocation[] templates = new ResourceLocation[]
			{
					new ResourceLocation(ModInfo.MODID, "tower/sw_tower_farm"),
					new ResourceLocation(ModInfo.MODID, "tower/sw_tower_food"),
					new ResourceLocation(ModInfo.MODID, "tower/sw_tower_library"),
					new ResourceLocation(ModInfo.MODID, "tower/sw_tower_residence"),
					new ResourceLocation(ModInfo.MODID, "tower/sw_tower_animal"),
			};
	
	private static ITemplateProcessor[] processors = new ITemplateProcessor[]
			{
					new TemplateProcessorTowerFarm(),
					new TemplateProcessorTowerFood(),
					new TemplateProcessorTowerLibrary(),
					new TemplateProcessorTowerResidence(),
					new TemplateProcessorTowerAnimal(),
			};
		
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(!ConfigHandler.generateDwarvenStructure)
			return;
		
		for(int i = 0; i < 4; i++)
			random.nextFloat(); //Mix up the number to not end up getting the same values as the other worldgen structures.
		//Not sure™ why it's doing that.
		
		if(world.provider.getDimension() == ConfigHandler.dimensionId && random.nextFloat() <= CHANCE_PER_CHUNK)
		{
			if(chunkX != 0 || chunkZ != 0)
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
		
		int maxParts = (world.getHeight() - (local_y + 32)) / 5;
		if(maxParts <= 0)
			return;
		
		maxParts = maxParts > 8 ? 8 : maxParts;
		int parts = random.nextInt(maxParts - 2) + 2;
		
		int baseHeight = PlacePart(BASE_TEMPLATE_LOCATION, null, world, random, pos);
		pos = pos.add(0, baseHeight, 0);
		
		for(int i = 0; i < parts; i++)
			pos = GenerateTowerPart(world, random, pos);
		
		PlacePart(TOP_TEMPLATE_LOCATION, null, world, random, pos);
	}

	private BlockPos GenerateTowerPart(World world, Random random, BlockPos pos)
	{
		int templateId = random.nextInt(templates.length);
		
		if(processors[templateId] instanceof IResettable)
			((IResettable)processors[templateId]).Reset();
		int partHeight = PlacePart(
				templates[templateId], 
				processors[templateId], 
				world, 
				random, 
				pos);
		return pos.add(0, partHeight, 0);
	}
	
	private int PlacePart(ResourceLocation templateLocation, ITemplateProcessor processor, World world, Random random, BlockPos pos)
	{
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		
		switch(rotation)
		{
		case CLOCKWISE_90:
			pos = pos.add(17, 0, 0);
			break;
		case COUNTERCLOCKWISE_90:
			pos = pos.add(0, 0, 17);
			break;
		case CLOCKWISE_180:
			pos = pos.add(17, 0, 17);
			break;
		default:
			break;
		}
		
		PlacementSettings placementSettings = new PlacementSettings().setRotation(rotation);
		Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), templateLocation);
		template.addBlocksToWorld(world, pos, placementSettings);
		if(processor != null)
		{
			if(processor instanceof IInitializableProcessor)
				((IInitializableProcessor)processor).Init(random);
			template.addBlocksToWorld(world, pos, processor, placementSettings, 2);
		}
		return template.getSize().getY();
	}
}
