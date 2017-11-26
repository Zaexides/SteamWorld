package zaexides.steamworld.integration.jei.assembler;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.AssemblyRecipe;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;

public class RecipeWrapperAssembler implements IRecipeWrapper
{
	private final List<ItemStack> INPUTS;
	private final ItemStack OUTPUT;
	
	public RecipeWrapperAssembler(AssemblyRecipe recipe) 
	{
		INPUTS = recipe.inputs;
		OUTPUT = recipe.output;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(ItemStack.class, INPUTS);
		ingredients.setOutput(ItemStack.class, OUTPUT);
	}
}
