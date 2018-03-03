package zaexides.steamworld.integration.tc;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;
import scala.reflect.macros.internal.macroImpl;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerTraits;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.items.ItemInitializer;
import zaexides.steamworld.items.SWItemNugget.EnumVarietyMaterial;

public class TCMaterials 
{
	public static Material steaiteMaterial;
	public static Material anciteMaterial;
	
	public static void registerMaterials()
	{
		SteamWorld.logger.log(Level.INFO, "Tinkers' Construct found, adding SteamWorld Materials, for as far as allowed...");
		int matCount = 0;
		
		if(ConfigHandler.tcSteaite)
		{
			steaiteMaterial = new Material("steaite", 0xA07559);
			
			steaiteMaterial.addTrait(TinkerTraits.duritos);
			
			TinkerRegistry.addMaterialStats(steaiteMaterial,
					new HeadMaterialStats(600, 7.2f, 3.7f, HarvestLevels.OBSIDIAN),
					new HandleMaterialStats(1.0f, -50),
					new ExtraMaterialStats(40),
					new BowMaterialStats(1.6f, 1.2f, 3)
					);
			
			
			steaiteMaterial.addItem(ItemInitializer.INGOT_STEAITE, 1, Material.VALUE_Ingot);
			steaiteMaterial.addItem(new ItemStack(ItemInitializer.ITEM_NUGGET, 1, EnumVarietyMaterial.STEAITE.getMeta()), 1, Material.VALUE_Nugget);
			
			steaiteMaterial.setRepresentativeItem(ItemInitializer.INGOT_STEAITE);
			steaiteMaterial.setFluid(FluidRegistry.getFluid("steaite"));
			TinkerRegistry.integrate(steaiteMaterial).preInit();
			matCount++;
		}
		
		if(ConfigHandler.tcAncite)
		{
			anciteMaterial = new Material("ancite", 0x7B82AF);
			
			anciteMaterial.addTrait(TinkerTraits.unnatural, MaterialTypes.HEAD);
			anciteMaterial.addTrait(TinkerTraits.magnetic, MaterialTypes.HANDLE);
			anciteMaterial.addTrait(TinkerTraits.heavy);
			
			TinkerRegistry.addMaterialStats(anciteMaterial,
					new HeadMaterialStats(900, 6.5f, 5.2f, HarvestLevels.OBSIDIAN),
					new HandleMaterialStats(1.2f, -40),
					new ExtraMaterialStats(100),
					new BowMaterialStats(0.8f, 1.8f, 9)
					);
			
			anciteMaterial.addItem(ItemInitializer.INGOT_ANCITE, 1, Material.VALUE_Ingot);
			anciteMaterial.addItem(new ItemStack(ItemInitializer.ITEM_NUGGET, 1, EnumVarietyMaterial.ANCITE.getMeta()), 1, Material.VALUE_Nugget);
			
			anciteMaterial.setRepresentativeItem(ItemInitializer.INGOT_ANCITE);
			anciteMaterial.setFluid(FluidRegistry.getFluid("ancite"));
			TinkerRegistry.integrate(anciteMaterial).preInit();
			matCount++;
		}
		
		SteamWorld.logger.log(Level.INFO, "Done adding {} materials to Tinkers' Construct.", matCount);
	}
}
