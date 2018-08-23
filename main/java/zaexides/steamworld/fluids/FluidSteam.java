package zaexides.steamworld.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import zaexides.steamworld.ModInfo;

public class FluidSteam extends Fluid
{
	public static FluidSteam fluidSteam;
	
	public FluidSteam() 
	{
		super("steam", new ResourceLocation(ModInfo.MODID, "blocks/fluids/steam"), new ResourceLocation(ModInfo.MODID, "blocks/fluids/steam"));
		setGaseous(true);
		setDensity(-1000);
		setTemperature(420);
		setViscosity(1000);
		
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
	}
}
