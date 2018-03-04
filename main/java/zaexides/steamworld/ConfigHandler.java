package zaexides.steamworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import zaexides.steamworld.proxy.CommonProxy;

public class ConfigHandler
{
	private static final String CATEGORY_GENERAL = "general";
	public static int drainMaxChecks = 512;
	public static int faucetMaxChecks = 512;
	public static int lumberArea = 5;
	public static int fertilizerArea = 5;
	public static int fluidControllerUpdateRate = 1;
	public static int pipeSystemMaxRange = 50;
	public static int dimensionId = 38620;
	
	private static final String CATEGORY_WORLDGEN = "worldgen";
	public static boolean generateSteaiteOre = true;
	public static boolean generateDwarvenStructure = true;
	public static boolean generateDwarvenOutpost = true;
	
	private static final String CATEGORY_FARMER = "farmer";
	public static List<String> farmerDropBlacklist = new ArrayList<String>();
	public static int farmerArea = 5;
	public static boolean farmerAllowSeedDropUpgrade = true;
	
	private static final String CATEGORY_ENERGY = "energy";
	public static int fluidFromEnergy = 1;
	public static int fluidToEnergy = 1;
	public static int energyFromFluid = 5;
	public static int energyToFluid = 5;
	public static int maxConversionsPerTick = 30;
	
	private static final String CATEGORY_TINKERS = "tinkers_integration";
	public static boolean tcSteaite = true;
	public static boolean tcAncite = true;
	public static boolean tcPreservation = true;
		
	public static void ReadConfig()
	{
		Configuration config = CommonProxy.configuration;
		try
		{
			config.load();
			InitGeneral(config);
			InitWorldgen(config);
			InitFarmerSettings(config);
			InitEnergySettings(config);
			InitTinkersIntegrationSettings(config);
		}
		catch(Exception e)
		{
			SteamWorld.logger.log(Level.ERROR, "Whoops, something went wrong while loading the config file!", e);
		}
		finally
		{
			if(config.hasChanged())
				config.save();
		}
	}
	
	private static void InitGeneral(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_GENERAL, "General");
		drainMaxChecks = config.getInt("drain_max_checks", CATEGORY_GENERAL, drainMaxChecks, 1, Integer.MAX_VALUE, "Maximum amount of blocks a drain can check for fluids to drain.");
		faucetMaxChecks = config.getInt("faucet_max_checks", CATEGORY_GENERAL, faucetMaxChecks, 1, Integer.MAX_VALUE, "Maximum amount of blocks a faucet can check for places to put fluids.");
		lumberArea = config.getInt("lumber_area", CATEGORY_GENERAL, lumberArea, 1, Integer.MAX_VALUE, "Area radius the lumber will check in. e.g. entering \"5\" will make it check up to 5 blocks in a cube away (so an 11x11x11 area).");
		fertilizerArea = config.getInt("fertilizer_area", CATEGORY_GENERAL, fertilizerArea, 1, Integer.MAX_VALUE, "Area radius the fertilizer will check in. e.g. entering \"5\" will make it check up to 5 blocks in a cube away (so an 11x11x11 area).");
		fluidControllerUpdateRate = config.getInt("fluid_controller_update_rate", CATEGORY_GENERAL, fluidControllerUpdateRate, 1, 5, "Update rate of the Fluid Controller. If it's set to 5, it'll update every 5 ticks. It'll scale the amount of fluid transported to the update rate. This can be increased to decrease lag.");
		pipeSystemMaxRange = config.getInt("pipe_syste_max_range", CATEGORY_GENERAL, pipeSystemMaxRange, 1, Integer.MAX_VALUE, "Maximum reach of a pipe system.");
		dimensionId = config.getInt("dimension_id", CATEGORY_GENERAL, dimensionId, Integer.MIN_VALUE, Integer.MAX_VALUE, "The ID that the SteamWorld dimension will use.");
	}
	
	private static void InitWorldgen(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_WORLDGEN, "World generation settings");
		generateSteaiteOre = config.getBoolean("generate_steaite", CATEGORY_WORLDGEN, generateSteaiteOre, "Generate Steaite ore in the world?");
		generateDwarvenStructure = config.getBoolean("generate_ancite_dungeon", CATEGORY_WORLDGEN, generateDwarvenStructure, "Generate the Ancite dungeons in the world?");
		generateDwarvenOutpost = config.getBoolean("generate_ancite_outpost", CATEGORY_WORLDGEN, generateDwarvenOutpost, "Generate the Ancite outposts in the world?");
	}
	
	private static void InitFarmerSettings(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_FARMER, "Farmer settings");
		farmerArea = config.getInt("farmer_area", CATEGORY_FARMER, farmerArea, 1, Integer.MAX_VALUE, "Area radius the farmer will check in. e.g. entering \"5\" will make it check up to 5 blocks in a cube away (so an 11x11x11 area).");
		farmerDropBlacklist = Arrays.asList(config.getStringList("farmer_drop_blacklist", CATEGORY_FARMER, farmerDropBlacklist.toArray(new String[0]), "Blacklist for crop drops in the farmer. Enter registry names (e.g. \"minecraft:wheat\") to disable the farmer from harvesting these as drops."));
		farmerAllowSeedDropUpgrade = config.getBoolean("farmer_upgrade_affect_seeds", CATEGORY_FARMER, farmerAllowSeedDropUpgrade, "Do the farmer upgrades also increase seed yield?");
	}
	
	private static void InitEnergySettings(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_ENERGY, "Energy settings");
		fluidFromEnergy = config.getInt("fluid_from_energy", CATEGORY_ENERGY, fluidFromEnergy, 1, Integer.MAX_VALUE, "The amount of \"fluid\" you get from \"energy_to_fluid\" RF. Should be the same as \"fluid_to_energy\", but can be changed for rebalancing.");
		fluidToEnergy = config.getInt("fluid_to_energy", CATEGORY_ENERGY, fluidToEnergy, 1, Integer.MAX_VALUE, "The amount of \"fluid\" you need to get \"energy_from_fluid\" RF. Should be the same as \"fluid_from_energy\", but can be changed for rebalancing.");
		energyFromFluid = config.getInt("energy_from_fluid", CATEGORY_ENERGY, energyFromFluid, 1, Integer.MAX_VALUE, "The amount of RF you get from \"fluid_to_energy\" \"fluid\". Should be the same as \"energy_to_fluid\", but can be changed for rebalancing.");
		energyToFluid = config.getInt("energy_to_fluid", CATEGORY_ENERGY, energyToFluid, 1, Integer.MAX_VALUE, "The amount of RF you need to get \"fluid_from_energy\" \"fluid\". Should be the same as \"energy_from_fluid\", but can be changed for rebalancing.");
		maxConversionsPerTick = config.getInt("max_conversions_per_tick", CATEGORY_ENERGY, maxConversionsPerTick, 1, Integer.MAX_VALUE, "The maximum amount of conversions that can be made per tick. i.e. if 1 RF translates to 80 Steam and this is set to 10, you can get up to 800 steam for 10 RF per tick.");
	}
	
	private static void InitTinkersIntegrationSettings(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_TINKERS, "Tinkers' Construct integration");
		tcSteaite = config.getBoolean("material_steaite", CATEGORY_TINKERS, tcSteaite, "Allow Steaite TC material.");
		tcAncite = config.getBoolean("material_ancite", CATEGORY_TINKERS, tcAncite, "Allow Ancite TC material.");
		tcPreservation = config.getBoolean("material_preservation", CATEGORY_TINKERS, tcPreservation, "Allow Preservation Rock TC material.");
	}
}
