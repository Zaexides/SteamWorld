package zaexides.steamworld.recipe.handling;

import net.minecraftforge.fluids.FluidStack;

public class FluidMinerRecipe 
{
	private final byte tier;
	private final FluidStack output;
	
	public FluidMinerRecipe(byte tier, FluidStack output)
	{
		this.tier = tier;
		this.output = output;
	}
	
	public byte getTier()
	{
		return tier;
	}
	
	public FluidStack getOutput()
	{
		return output;
	}
}
