package zaexides.steamworld.fluids;

import org.apache.logging.log4j.Level;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;

public class FluidSteam extends Fluid
{
	public static FluidSteam fluidSteam;
	
	public FluidSteam() 
	{
		super(ModInfo.MODID + ".steam", new ResourceLocation(ModInfo.MODID, "blocks/fluids/steam"), new ResourceLocation(ModInfo.MODID, "blocks/fluids/steam"));
		setGaseous(true);
		setDensity(-1000);
		setTemperature(420);
		setViscosity(1100);
		
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
	}
}
