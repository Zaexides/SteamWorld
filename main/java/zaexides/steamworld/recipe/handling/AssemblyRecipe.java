package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;

public class AssemblyRecipe 
{
	public final List<IRecipeInput> inputs;
	public final ItemStack output;
	public final int duration;
	
	public AssemblyRecipe(ItemStack output, int duration, ItemStack... input)
	{
		inputs = new ArrayList<IRecipeInput>();
		for(ItemStack itemStack : input)
		{
			inputs.add(new RecipeInputItemStack(itemStack));
		}
		this.output = output;
		this.duration = duration;
		
		AssemblyRecipeHandler.AddRecipeToList(this, false);
	}
	
	public AssemblyRecipe(boolean force, ItemStack output, int duration, IRecipeInput... input)
	{
		this.inputs = Arrays.asList(input);
		this.output = output;
		this.duration = duration;
		
		AssemblyRecipeHandler.AddRecipeToList(this, force);
	}
	
	public AssemblyRecipe(ItemStack output, int duration, IRecipeInput... input) 
	{
		this.inputs = Arrays.asList(input);
		this.output = output;
		this.duration = duration;
		
		AssemblyRecipeHandler.AddRecipeToList(this, false);
	}
	
	public AssemblyRecipe(ItemStack output, int duration, List<IRecipeInput> inputs) 
	{
		this.inputs = inputs;
		this.output = output;
		this.duration = duration;
		
		AssemblyRecipeHandler.AddRecipeToList(this, false);
	}
	
	private AssemblyRecipe(AssemblyRecipe recipe) 
	{
		this.inputs = new ArrayList<IRecipeInput>();
		this.inputs.addAll(recipe.inputs);
		this.output = recipe.output.copy();
		this.duration = recipe.duration;
	}
	
	public AssemblyRecipe Copy()
	{
		return new AssemblyRecipe(this);
	}
	
	public boolean isRecipe(List<ItemStack> inputs)
	{
		int correct = 0;
		List<IRecipeInput> itemsHad = new ArrayList<IRecipeInput>();
		
		List<IRecipeInput> neededInputCopies = new ArrayList<IRecipeInput>();
		for(IRecipeInput in2 : this.inputs)
			neededInputCopies.add(in2.copy());
		
		for(ItemStack in1 : inputs)
		{
			if(matches(in1, neededInputCopies, itemsHad))
				correct++;
		}
		
		return correct == this.inputs.size();
	}
	
	private boolean matches(ItemStack in1, List<IRecipeInput> in2List, List<IRecipeInput> itemsHad)
	{
		for(IRecipeInput in2 : in2List)
		{
			if(!itemsHad.contains(in2))
			{
				if(in1.isEmpty() && in2.isEmpty())
				{
					itemsHad.add(in2);
					return true;
				}
				else if(!itemsHad.contains(in2) && in2.matchesItemStack(in1))
				{
					itemsHad.add(in2);
					return true;
				}
			}
		}
		return false;
	}
}
