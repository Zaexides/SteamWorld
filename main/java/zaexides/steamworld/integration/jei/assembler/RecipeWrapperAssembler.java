package zaexides.steamworld.integration.jei.assembler;

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
import zaexides.steamworld.recipe.handling.AssemblyRecipe;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class RecipeWrapperAssembler implements IRecipeWrapper
{
	private final List<List<ItemStack>> INPUTS;
	private final ItemStack OUTPUT;
	
	public RecipeWrapperAssembler(AssemblyRecipe recipe) 
	{
		INPUTS = new ArrayList<List<ItemStack>>();
		for(IRecipeInput recipeInput : recipe.inputs)
		{
			if(recipeInput instanceof RecipeInputItemStack)
			{
				List<ItemStack> soleItemStackList = new ArrayList<ItemStack>();
				soleItemStackList.add(((RecipeInputItemStack) recipeInput).itemStack);
				INPUTS.add(soleItemStackList);
			}
			else if(recipeInput instanceof RecipeInputOreDic)
			{
				String oreDicName = ((RecipeInputOreDic)recipeInput).oreDicName;
				List<ItemStack> itemStacks = OreDictionary.getOres(oreDicName);
				INPUTS.add(itemStacks);
			}
		}
		OUTPUT = recipe.output;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(ItemStack.class, INPUTS);
		ingredients.setOutput(ItemStack.class, OUTPUT);
	}
}
