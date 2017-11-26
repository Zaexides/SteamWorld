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
	private static final String CATEGORY_WORLDGEN = "worldgen";
	private static final String CATEGORY_GRINDER = "grinder";
	private static final String CATEGORY_ENERGY = "energy";
	
	public static boolean generateSteaiteOre = true;
	public static boolean generateDwarvenStructure = true;
	public static boolean generateDwarvenOutpost = true;
	
	public static List<String> grinderBlacklist = new ArrayList<String>();
	
	public static int drainMaxChecks = 512;
	public static int faucetMaxChecks = 512;
	public static int farmerArea = 5;
	public static int lumberArea = 5;
	public static int fertilizerArea = 5;
	public static int fluidControllerUpdateRate = 1;
	
	public static int fluid_from_energy = 1;
	public static int fluid_to_energy = 1;
	public static int energy_from_fluid = 5;
	public static int energy_to_fluid = 5;
	public static int max_conversions_per_tick = 30;
		
	public static void ReadConfig()
	{
		Configuration config = CommonProxy.configuration;
		try
		{
			config.load();
			InitGeneral(config);
			InitWorldgen(config);
			InitGrinderSettings(config);
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
		farmerArea = config.getInt("farmer_area", CATEGORY_GENERAL, farmerArea, 1, Integer.MAX_VALUE, "Area radius the farmer will check in. e.g. entering \"5\" will make it check up to 5 blocks in a cube away (so an 11x11x11 area).");
		lumberArea = config.getInt("lumber_area", CATEGORY_GENERAL, lumberArea, 1, Integer.MAX_VALUE, "Area radius the lumber will check in. e.g. entering \"5\" will make it check up to 5 blocks in a cube away (so an 11x11x11 area).");
		fertilizerArea = config.getInt("fertilizer_area", CATEGORY_GENERAL, fertilizerArea, 1, Integer.MAX_VALUE, "Area radius the fertilizer will check in. e.g. entering \"5\" will make it check up to 5 blocks in a cube away (so an 11x11x11 area).");
		fluidControllerUpdateRate = config.getInt("fluid_controller_update_rate", CATEGORY_GENERAL, fluidControllerUpdateRate, 1, 5, "Update rate of the Fluid Controller. If it's set to 5, it'll update every 5 ticks. It'll scale the amount of fluid transported to the update rate. This can be increased to decrease lag.");
	}
	
	private static void InitWorldgen(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_WORLDGEN, "World generation settings");
		generateSteaiteOre = config.getBoolean("generate_steaite", CATEGORY_WORLDGEN, generateSteaiteOre, "Generate Steaite ore in the world?");
		generateDwarvenStructure = config.getBoolean("generate_ancite_dungeon", CATEGORY_WORLDGEN, generateDwarvenStructure, "Generate the Ancite dungeons in the world?");
		generateDwarvenOutpost = config.getBoolean("generate_ancite_outpost", CATEGORY_WORLDGEN, generateDwarvenOutpost, "Generate the Ancite outposts in the world?");
	}
	
	private static void InitGrinderSettings(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_GRINDER, "Grinder settings");
		grinderBlacklist = Arrays.asList(config.getStringList("blacklist", CATEGORY_GRINDER, grinderBlacklist.toArray(new String[0]), "Disallow these grinder recipes to be created. e.g.: Steaite Dust would be \"steamworld:dust<2>\" and Iron Ingot would be \"minecraft:iron_ingot<0>\", look at the logs during startup. Also, don't use quotation (\") marks."));
		
	}
	
	private static void InitEnergySettings(Configuration config)
	{
		config.addCustomCategoryComment(CATEGORY_ENERGY, "Energy settings");
		fluid_from_energy = config.getInt("fluid_from_energy", CATEGORY_ENERGY, fluid_from_energy, 1, Integer.MAX_VALUE, "The amount of \"fluid\" you get from \"energy_to_fluid\" RF. Should be the same as \"fluid_to_energy\", but can be changed for rebalancing.");
		fluid_to_energy = config.getInt("fluid_to_energy", CATEGORY_ENERGY, fluid_to_energy, 1, Integer.MAX_VALUE, "The amount of \"fluid\" you need to get \"energy_from_fluid\" RF. Should be the same as \"fluid_from_energy\", but can be changed for rebalancing.");
		energy_from_fluid = config.getInt("energy_from_fluid", CATEGORY_ENERGY, energy_from_fluid, 1, Integer.MAX_VALUE, "The amount of RF you get from \"fluid_to_energy\" \"fluid\". Should be the same as \"energy_to_fluid\", but can be changed for rebalancing.");
		energy_to_fluid = config.getInt("energy_to_fluid", CATEGORY_ENERGY, energy_to_fluid, 1, Integer.MAX_VALUE, "The amount of RF you need to get \"fluid_from_energy\" \"fluid\". Should be the same as \"energy_from_fluid\", but can be changed for rebalancing.");
		max_conversions_per_tick = config.getInt("max_conversions_per_tick", CATEGORY_ENERGY, max_conversions_per_tick, 1, Integer.MAX_VALUE, "The maximum amount of conversions that can be made per tick. i.e. if 1 RF translates to 80 Steam and this is set to 10, you can get up to 800 steam for 10 RF per tick.");
	}
}
