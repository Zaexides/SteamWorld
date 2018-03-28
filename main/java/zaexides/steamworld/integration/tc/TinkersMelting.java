package zaexides.steamworld.integration.tc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatch.Match;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.fluids.tc.FluidTinkersIntegration;
import zaexides.steamworld.init.BlockInitializer;

public class TinkersMelting 
{
	public static void integrate()
	{
		if(!Loader.isModLoaded("tconstruct"))
			return;
		
		addTinkerFluid("Ancite", 0xFFA9B6DC).setTemperature(1824).setViscosity(8387).setDensity(4012).setLuminosity(15);
		addTinkerFluid("Steaite", 0xFFC49D7E).setTemperature(1276).setViscosity(3453).setDensity(3521).setLuminosity(13);
		addTinkerFluid("Galite", 0xDF65F7C9).setTemperature(980).setViscosity(4521).setDensity(3496).setLuminosity(6);
		Fluid territeFluid = addTinkerFluid("Territe", 0xBADD52F9).setTemperature(877).setViscosity(9246).setDensity(4254).setLuminosity(0);
		
		new MeltingRecipe(new RecipeMatch.Oredict("gemTerrite", 1, 18), territeFluid).register();
	}
	
	private static Fluid addTinkerFluid(String oreName, int fluidColor)
	{
		Fluid oreFluid = new FluidTinkersIntegration(oreName.toLowerCase(), fluidColor);
		FluidRegistry.registerFluid(oreFluid);
		FluidRegistry.addBucketForFluid(oreFluid);
		
		Block bFluid = new BlockTinkersIntegrationFluid(oreFluid).setRegistryName("molten_" + oreName.toLowerCase());
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setString("fluid", oreFluid.getName());
		tagCompound.setString("ore", oreName);
		tagCompound.setBoolean("toolforge", true);
		FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tagCompound);
		return oreFluid;
	}
}
