package zaexides.steamworld.worldgen.dimension;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;

public class DimensionTypeSteamWorld extends WorldProvider
{
	public static final DimensionType STEAMWORLD = DimensionType.register("SteamWorld", "_" + ModInfo.MODID, ConfigHandler.dimensionId, DimensionTypeSteamWorld.class, false);
	
	@Override
	public DimensionType getDimensionType() 
	{
		return STEAMWORLD;
	}
	
	@Override
	public String getSaveFolder() 
	{
		return "DIM_SteamWorld";
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() 
	{
		return new SteamWorldChunkGenerator(world);
	}
}
