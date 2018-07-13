package zaexides.steamworld.recipe.handling;

import net.minecraft.item.ItemStack;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;

public class MinerRecipe 
{
	private final byte tier;
	private final IRecipeInput output;
	
	public MinerRecipe(byte tier, IRecipeInput output)
	{
		this.tier = tier;
		this.output = output;
	}
	
	public byte getTier()
	{
		return tier;
	}
	
	public IRecipeInput getOutput()
	{
		return output;
	}
}
