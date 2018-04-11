package zaexides.steamworld.fluids;

import org.apache.logging.log4j.Level;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;

public class FluidWithering extends Fluid
{
	public static FluidWithering fluidWithering;
	
	public FluidWithering() 
	{
		super("witheringLiquid", new ResourceLocation(ModInfo.MODID, "blocks/fluids/witheringliquid_still"), new ResourceLocation(ModInfo.MODID, "blocks/fluids/witheringliquid_flowing"));
		setGaseous(false);
		setDensity(2405);
		setTemperature(274);
		setViscosity(2405);
		setLuminosity(2);
		setRarity(EnumRarity.UNCOMMON);
		
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
	}
}
