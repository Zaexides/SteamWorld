package zaexides.steamworld.integration.tc;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.Optional;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;

//After days of figuring out how to add materials to TC, I'm
//fairly afraid to remove anything from the "working standard"
//I've managed in here.

public class TCMaterials 
{
	public static Material steaiteMaterial;
	public static Material anciteMaterial;
	
	@Optional.Method(modid = "tconstruct")
	public static void registerMaterials()
	{
		SteamWorld.logger.log(Level.INFO, "Tinkers' Construct found, adding SteamWorld Materials, for as far as allowed...");
		int matCount = 0;
		
		if(ConfigHandler.tcAncite)
		{
			anciteMaterial = new Material("ancite", 0x9BA0C6);
			
			TinkerRegistry.addMaterialStats(anciteMaterial,
					new HeadMaterialStats(400, 8.5f, 3.4f, HarvestLevels.COBALT),
					new HandleMaterialStats(0.8f, 50),
					new BowMaterialStats(0.6f, 1.2f, 4.1f)
					);
			
			TinkerRegistry.integrate(anciteMaterial).preInit();
		}
	}
}
