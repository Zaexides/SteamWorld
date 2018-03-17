package zaexides.steamworld.worldgen.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;

public class DimensionTypeSteamWorld extends WorldProviderSurface
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
	protected void init() 
	{
		super.init();
		biomeProvider = new SteamWorldBiomeProvider(getSeed(), world.getWorldInfo().getTerrainType());
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() 
	{
		return new SteamWorldChunkGenerator(world);
	}
	
	@Override
	public double getHorizon() 
	{
		return -224;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) 
	{
		Vec3d skyColor = new Vec3d(1.2, 0.7, 1.0);
		Vec3d originalColor = super.getSkyColor(cameraEntity, partialTicks);
		return new Vec3d(
				(skyColor.x + originalColor.x) * 0.5,
				(skyColor.y + originalColor.y) * 0.5, 
				(skyColor.z + originalColor.z) * 0.5
				);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float f, float f1) 
	{
		Vec3d fogColor = new Vec3d(0.9, 0.6, 0.8);
		Vec3d originalColor = super.getFogColor(f, f1);
		return new Vec3d(
				(fogColor.x + originalColor.x) * 0.5,
				(fogColor.y + originalColor.y) * 0.5, 
				(fogColor.z + originalColor.z) * 0.5
				);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1) 
	{
		return super.getSunBrightness(par1) * 0.9f;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1) 
	{
		return super.getStarBrightness(par1) * 0.8f;
	}
	
	@Override
	public float getCloudHeight() 
	{
		return -55.24f;
	}
	
	@Override
	public double getVoidFogYFactor() 
	{
		return 2.5;
	}
}
