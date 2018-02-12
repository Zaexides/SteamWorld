package zaexides.steamworld.integration.jei.grinder;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler.DustRecipe;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class RecipeWrapperGrinder implements IRecipeWrapper
{
	private final List<ItemStack> INPUT;
	private final ItemStack OUTPUT;
	
	public RecipeWrapperGrinder(DustRecipe dustRecipe) 
	{
		IRecipeInput input = dustRecipe.getInput();
		
		if(input instanceof RecipeInputItemStack)
		{
			INPUT = new ArrayList<ItemStack>();
			INPUT.add(((RecipeInputItemStack) input).itemStack);
		}
		else
		{
			INPUT = OreDictionary.getOres(((RecipeInputOreDic) input).oreDicName);
		}
		
		OUTPUT = dustRecipe.getOutput();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(ItemStack.class, INPUT);
		ingredients.setOutput(ItemStack.class, OUTPUT);
	}
}
