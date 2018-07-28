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

public class WorldGenPortalBuilding implements IWorldGenerator
{
	private static final ResourceLocation PORTAL_BUILDING_TEMPLATE_LOCATION = new ResourceLocation(ModInfo.MODID, "sw_portal_building");
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(world.provider.getDimension() == ConfigHandler.dimensionId)
		{
			if(chunkX == 0 && chunkZ == 0)
			{
				Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), PORTAL_BUILDING_TEMPLATE_LOCATION);
				template.addBlocksToWorld(world, new BlockPos(0, 80, 0), new PlacementSettings());
			}
		}
	}

}
