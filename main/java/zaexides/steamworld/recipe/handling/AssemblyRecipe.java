package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.BlockInitializer;
import zaexides.steamworld.SteamWorld;

public class AssemblyRecipe 
{
	public final List<ItemStack> inputs;
	public final ItemStack output;
	public final int duration;
	
	public AssemblyRecipe(ItemStack output, int duration, ItemStack... input) 
	{
		this.inputs = Arrays.asList(input);
		this.output = output;
		this.duration = duration;
		
		AssemblyRecipeHandler.recipes.add(this);
	}
	
	public AssemblyRecipe(ItemStack output, int duration, List<ItemStack> inputs) 
	{
		this.inputs = inputs;
		this.output = output;
		this.duration = duration;
		
		AssemblyRecipeHandler.recipes.add(this);
	}
	
	private AssemblyRecipe(AssemblyRecipe recipe) 
	{
		this.inputs = new ArrayList<ItemStack>();
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
		List<ItemStack> itemsHad = new ArrayList<ItemStack>();
		
		List<ItemStack> neededInputCopies = new ArrayList<ItemStack>();
		for(ItemStack in2 : this.inputs)
			neededInputCopies.add(in2.copy());
		
		for(ItemStack in1 : inputs)
		{
			if(matches(in1, neededInputCopies, itemsHad))
				correct++;
		}
		
		return correct == this.inputs.size();
	}
	
	private boolean matches(ItemStack in1, List<ItemStack> in2List, List<ItemStack> itemsHad)
	{
		for(ItemStack in2 : in2List)
		{
			if(!itemsHad.contains(in2))
			{
				if(in1.isEmpty() && in2.isEmpty())
				{
					itemsHad.add(in2);
					return true;
				}
				else if(!itemsHad.contains(in2) && in1.getItem() == in2.getItem() && in1.getMetadata() == in2.getMetadata())
				{
					itemsHad.add(in2);
					return true;
				}
			}
		}
		return false;
	}
}
