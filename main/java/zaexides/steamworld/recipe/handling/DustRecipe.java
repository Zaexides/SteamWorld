package zaexides.steamworld.recipe.handling;

import net.minecraft.item.ItemStack;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;

public class DustRecipe
{
	private final IRecipeInput input;
	private final ItemStack output;
	private boolean affectedByLevel = false;
	
	public DustRecipe(IRecipeInput input, ItemStack output) 
	{
		this.input = input;
		this.output = output;
	}
	
	public boolean hasInput(ItemStack input)
	{
		return this.input.matchesItemStack(input);
	}
	
	public ItemStack getOutput()
	{
		return output.copy();
	}
	
	public IRecipeInput getInput()
	{
		return input.copy();
	}
	
	public DustRecipe setAffectedByLevel(boolean affected)
	{
		this.affectedByLevel = affected;
		return this;
	}
	
	public boolean affectedByLevel()
	{
		return this.affectedByLevel;
	}
}