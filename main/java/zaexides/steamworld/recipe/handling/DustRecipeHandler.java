package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class DustRecipeHandler 
{
	public static final List<DustRecipe> RECIPES = new ArrayList<DustRecipe>();
	public static List<IRecipeInput> blacklist = new ArrayList<IRecipeInput>();
	
	public static DustRecipe RegisterRecipe(ItemStack input, ItemStack output)
	{
		return RegisterRecipe(new RecipeInputItemStack(input), output, false);
	}
	
	public static DustRecipe RegisterRecipe(String oredicInput, ItemStack output)
	{
		return RegisterRecipe(new RecipeInputOreDic(oredicInput), output, false);
	}
	
	public static DustRecipe RegisterRecipe(String oredicInput, ItemStack output, boolean affectedByLevel)
	{
		DustRecipe dustRecipe = RegisterRecipe(oredicInput, output);
		if(dustRecipe != null)
			dustRecipe.setAffectedByLevel(affectedByLevel);
		return dustRecipe;
	}
	
	public static DustRecipe RegisterRecipe(IRecipeInput input, ItemStack output, boolean forced)
	{
		if(input.isEmpty())
			return null;
		
		if(isBlackListed(input) && !forced)
		{
			SteamWorld.logger.log(Level.INFO, "Skipped SteamWorld Grinder recipe for " + input.toString() + " as it was blacklisted.");
			return null;
		}
		
		for(DustRecipe dustRecipe : RECIPES)
		{
			IRecipeInput recipeInput = dustRecipe.getInput();
			if(recipeInput.isSame(input))
				return null;
		}
		
		DustRecipe dustRecipe = new DustRecipe(input, output);
		RECIPES.add(dustRecipe);
		return dustRecipe;
	}
	
	public static boolean isBlackListed(IRecipeInput recipeInput)
	{
		for(IRecipeInput recipeInput2 : blacklist)
		{
			if(recipeInput2.isSame(recipeInput))
				return true;
		}
		return false;
	}
	
	public static DustRecipe GetRecipe(ItemStack input)
	{
		for(DustRecipe dustRecipe : RECIPES)
		{
			if(dustRecipe.hasInput(input))
				return dustRecipe;
		}
		return null;
	}
}
