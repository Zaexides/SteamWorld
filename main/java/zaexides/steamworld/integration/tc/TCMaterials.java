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
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerTraits;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockDecorative.EnumType;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.integration.tc.traits.TraitSelective;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.items.SWItemNugget.EnumVarietyMaterial;

public class TCMaterials 
{
	public static Material steaiteMaterial;
	public static Material anciteMaterial;
	public static Material preservationMaterial;
	
	public static final AbstractTrait TRAIT_SELECTIVE = new TraitSelective(1);
	public static final AbstractTrait TRAIT_SELECTIVE_2 = new TraitSelective(2);
	
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
			
			steaiteMaterial.setFluid(FluidRegistry.getFluid("steaite"));
			TinkerRegistry.integrate(steaiteMaterial).preInit();
			steaiteMaterial.setCraftable(false);
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
			
			anciteMaterial.setFluid(FluidRegistry.getFluid("ancite"));
			TinkerRegistry.integrate(anciteMaterial).preInit();
			anciteMaterial.setCraftable(false);
			matCount++;
		}
		
		if(ConfigHandler.tcPreservation)
		{
			preservationMaterial = new Material("preservation", 0xA9CB94);
			preservationMaterial.setCraftable(true);
			
			preservationMaterial.addTrait(TRAIT_SELECTIVE);
			preservationMaterial.addTrait(TRAIT_SELECTIVE_2, MaterialTypes.HEAD);
			
			TinkerRegistry.addMaterialStats(preservationMaterial,
					new HeadMaterialStats(15, 4.2f, 1.5f, HarvestLevels.DIAMOND),
					new HandleMaterialStats(1.1f, -110),
					new ExtraMaterialStats(25),
					new BowMaterialStats(3.5f, 0.45f, -2)
					);
			
			TinkerRegistry.integrate(preservationMaterial).preInit();
			matCount++;
		}
		
		SteamWorld.logger.log(Level.INFO, "Done adding {} materials to Tinkers' Construct.", matCount);
	}
	
	public static void Init()
	{
		steaiteMaterial.addCommonItems("Steaite");
		steaiteMaterial.setRepresentativeItem(ItemInitializer.INGOT_STEAITE);
		
		anciteMaterial.addCommonItems("Ancite");
		anciteMaterial.setRepresentativeItem(ItemInitializer.INGOT_ANCITE);
		
		ItemStack preservationRock = new ItemStack(BlockInitializer.BLOCK_DECORATIVE, 1, BlockDecorative.EnumType.PRESERVATION_COBBLE.getMeta());
		preservationMaterial.addItem(preservationRock, 1, Material.VALUE_Ingot);
		preservationMaterial.setRepresentativeItem(preservationRock);
	}
}
