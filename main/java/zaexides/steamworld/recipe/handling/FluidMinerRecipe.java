package zaexides.steamworld.recipe.handling;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;

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
