package zaexides.steamworld.fluids;

import org.apache.logging.log4j.Level;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;

public class FluidPreservation extends Fluid
{
	public static FluidPreservation fluidPreservation;
	
	public FluidPreservation() 
	{
		super("preservationLiquid", new ResourceLocation(ModInfo.MODID, "blocks/fluids/preservationliquid_still"), new ResourceLocation(ModInfo.MODID, "blocks/fluids/preservationliquid_flowing"));
		setGaseous(false);
		setDensity(2400);
		setTemperature(310);
		setViscosity(2400);
		setLuminosity(5);
		setRarity(EnumRarity.UNCOMMON);
		
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
	}
}
