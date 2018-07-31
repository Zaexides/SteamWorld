package zaexides.steamworld.init;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.blocks.BlockCrystal;
import zaexides.steamworld.blocks.SteamWorldBlockOre;
import zaexides.steamworld.utility.WeightedBlock;
import zaexides.steamworld.world.WorldGenerationOres;
import zaexides.steamworld.world.dimension.WorldProviderSkyOfOld;
import zaexides.steamworld.world.structure.WorldGenCrypt;
import zaexides.steamworld.world.structure.WorldGenCrystal;
import zaexides.steamworld.world.structure.WorldGenPortalBuilding;
import zaexides.steamworld.world.structure.WorldGenWitherLab;
import zaexides.steamworld.world.structure.tower.WorldGenTower;

public class WorldgenInit 
{
	public static void RegisterWorldGen()
	{
		RegisterStructureGenerators();
		DimensionManager.registerDimension(ConfigHandler.dimensionId, WorldProviderSkyOfOld.STEAMWORLD);
	}
	
	private static void RegisterStructureGenerators()
	{
		GameRegistry.registerWorldGenerator(new WorldGenerationOres(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenCrypt(), 1000);
		GameRegistry.registerWorldGenerator(new WorldGenPortalBuilding(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenWitherLab(), 1000);
		GameRegistry.registerWorldGenerator(new WorldGenTower(), 1000);
		
		WorldGenCrystal crystalRegular = new WorldGenCrystal(BlockInitializer.BLOCK_CRYSTAL.getDefaultState(),
				Arrays.asList(
						new WeightedBlock(BlockInitializer.BLOCK_CRYSTAL.getDefaultState(), 30),
						new WeightedBlock(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.CRYSTAL_GOLD.getMeta()), 20),
						new WeightedBlock(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.CRYSTAL_EMERALD.getMeta()), 10),
						new WeightedBlock(Blocks.MOB_SPAWNER.getDefaultState(), 40)
						),
				0.05f, 0.1f, 3);
		GameRegistry.registerWorldGenerator(crystalRegular, 500);
		
		WorldGenCrystal crystalStar = new WorldGenCrystal(BlockInitializer.BLOCK_CRYSTAL.getStateFromMeta(BlockCrystal.EnumType.STAR_CRYSTAL.getMeta()),
				Arrays.asList(
						new WeightedBlock(BlockInitializer.BLOCK_CRYSTAL.getStateFromMeta(BlockCrystal.EnumType.STAR_CRYSTAL.getMeta()), 50),
						new WeightedBlock(Blocks.GLOWSTONE.getDefaultState(), 45),
						new WeightedBlock(Blocks.MOB_SPAWNER.getDefaultState(), 5)
						),
				0.0f, 0.03f, 5);
		GameRegistry.registerWorldGenerator(crystalStar, 500);
		
		WorldGenCrystal crystalCore = new WorldGenCrystal(BlockInitializer.BLOCK_CRYSTAL.getStateFromMeta(BlockCrystal.EnumType.CORE_CRYSTAL.getMeta()),
				Arrays.asList(
						new WeightedBlock(BlockInitializer.BLOCK_CRYSTAL.getStateFromMeta(BlockCrystal.EnumType.CORE_CRYSTAL.getMeta()), 40),
						new WeightedBlock(BlockInitializer.ORE.getStateFromMeta(SteamWorldBlockOre.EnumType.CRYSTAL_REDSTONE.getMeta()), 40),
						new WeightedBlock(Blocks.MOB_SPAWNER.getDefaultState(), 20)
						),
				0.005f, 0.025f, 3, 5, 30);
		GameRegistry.registerWorldGenerator(crystalCore, 500);
	}
}
