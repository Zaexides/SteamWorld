package zaexides.steamworld.world.dimension;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.client.rendering.world.SkyRendererSkyOfOld;

public class DimensionTypeSteamWorld extends WorldProviderSurface
{
	public static final DimensionType STEAMWORLD = DimensionType.register("sky_of_old", "_" + ModInfo.MODID, ConfigHandler.dimensionId, DimensionTypeSteamWorld.class, false);
	
	public static final long DAY_DURATION = 48000;
	private static final float ECLIPSE_DURATION = 700.0f;
	private static final float ECLIPSE_MAX_DURATION = 600.0f;
	private static final float ECLIPSE_DURATION_FACTOR = 1 / ECLIPSE_DURATION;
	
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
		hasSkyLight = true;
		biomeProvider = new SteamWorldBiomeProvider(getSeed(), world.getWorldInfo().getTerrainType());
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() 
	{
		return new SteamWorldChunkGenerator(world);
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) 
	{
		return super.calculateCelestialAngle(3000, 0.0f);
	}
	
	@Override
	public double getHorizon() 
	{
		return -224;
	}
	
	@Override
	public IRenderHandler getSkyRenderer() 
	{
		return SkyRendererSkyOfOld.renderer;
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
	public float getCloudHeight() 
	{
		return -55.24f;
	}
	
	@Override
	public double getVoidFogYFactor() 
	{
		return 2.5;
	}
	
	@Override
	public BlockPos getSpawnPoint() 
	{
		return new BlockPos(16, 87, 16);
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint()
	{
		return getSpawnPoint();
	}
	
	@Override
	public float getSunBrightnessFactor(float par1) 
	{
		return world.getSunBrightnessFactor(par1) * getEclipseFactor();
	}
	
	public float getEclipseFactor()
	{
		long time = world.getWorldTime() % DAY_DURATION;
		long timeDifference;
		
		if(time > (DAY_DURATION * .5))
			timeDifference = DAY_DURATION - time;
		else
			timeDifference = time;
		
		float factor = (timeDifference - ECLIPSE_MAX_DURATION) * ECLIPSE_DURATION_FACTOR;
		
		if(factor < 0)
			factor = 0;
		else if(factor > 1)
			factor = 1;
		
		return factor;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1) 
	{
		return getSunBrightnessFactor(par1);
	}
	
	@Override
	public boolean canDropChunk(int x, int z) 
	{
		return !(x >= -1 && x <= 1 && z >= -1 && z <= 1);
	}
	
	@Override
	public void onPlayerAdded(EntityPlayerMP player) 
	{
		if(player.chunkCoordX == 0 && player.chunkCoordZ == 0)
			player.setPosition(getSpawnPoint().getX(), getSpawnPoint().getY(), getSpawnPoint().getZ());
	}
}
