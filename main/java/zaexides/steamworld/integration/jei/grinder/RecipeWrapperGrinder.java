package zaexides.steamworld.integration.jei.grinder;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;

public class RecipeWrapperGrinder implements IRecipeWrapper
{
	private final ItemStack INPUT;
	private final ItemStack OUTPUT;
	
	public RecipeWrapperGrinder(ItemStack inputStack) 
	{
		INPUT = inputStack;
		OUTPUT = DustRecipeHandler.GetResult(inputStack);
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(ItemStack.class, INPUT);
		ingredients.setOutput(ItemStack.class, OUTPUT);
	}
}
