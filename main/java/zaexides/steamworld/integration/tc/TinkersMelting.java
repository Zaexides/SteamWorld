package zaexides.steamworld.integration.tc;

import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.fluids.tc.FluidTinkersIntegration;

public class TinkersMelting 
{
	public static void integrate()
	{
		if(!Loader.isModLoaded("tconstruct"))
			return;
		
		addTinkerFluid("Ancite", 0xFFA9B6DC).setTemperature(1824).setViscosity(8387).setDensity(4012).setLuminosity(15);
		addTinkerFluid("Steaite", 0xFFC49D7E).setTemperature(1276).setViscosity(3453).setDensity(3521).setLuminosity(13);
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
