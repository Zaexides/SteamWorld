package zaexides.steamworld.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import zaexides.steamworld.recipe.handling.AssemblyRecipe;
import zaexides.steamworld.recipe.handling.AssemblyRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.DustRecipe;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class SteamWorldCraftTweaker 
{
	@ZenRegister
	@ZenClass("mods.steamworld.assembler")
	public static class AssemblyRecipeCT
	{
		@ZenMethod
		public static void addRecipe(IItemStack output, int duration, IIngredient... input)
		{
			if(input.length > 6)
			{
				CraftTweakerAPI.logError("Couldn't register SteamWorld assembly recipe for " + output.getName() + " because it has more than 6 inputs!");
				return;
			}
			
			ItemStack outputStack = CraftTweakerMC.getItemStack(output);
			IRecipeInput[] recipeInputs = new IRecipeInput[6];
			
			for(int i = 0; i < recipeInputs.length; i++)
			{
				if(i < input.length)
				{
					if(input[i] instanceof IItemStack)
						recipeInputs[i] = new RecipeInputItemStack(CraftTweakerMC.getItemStack(input[i]));
					else if(input[i] instanceof IOreDictEntry)
						recipeInputs[i] = new RecipeInputOreDic(((IOreDictEntry)input[i]).getName());
					else
					{
						CraftTweakerAPI.logError("Failed to add SteamWorld assembly recipe for " + output.getName() + " as the input \"" + input[i].toString() + "\" is neither an Oredic entry nor an itemstack.");
						return;
					}
				}
				else
				{
					recipeInputs[i] = new RecipeInputItemStack(ItemStack.EMPTY);
				}
			}
			
			if(duration < 20)
				CraftTweakerAPI.logWarning("Duration for " + output.getName() + "'s SteamWorld assembly recipe is below 20. This means the Assembler will be done in less than a second. Are you sure this was your intention..?");
			
			try
			{
				AssemblyRecipe assemblyRecipe = new AssemblyRecipe(true, outputStack, duration, recipeInputs);
				CraftTweakerAPI.logInfo("SteamWorld assembly recipe for " + output.getName() + " was registered.");
			}
			catch(Exception ex)
			{
				CraftTweakerAPI.logError("An error occured while registering the SteamWorld assembly recipe for " + output.getName() + ". ", ex);
			}
		}
		
		@ZenMethod
		public static void removeRecipe(IIngredient ingredient)
		{
			if(ingredient instanceof IItemStack)
			{
				IRecipeInput blacklistEntry = new RecipeInputItemStack(CraftTweakerMC.getItemStack(ingredient));
				AssemblyRecipeHandler.blacklist.add(blacklistEntry);
			}
			else if(ingredient instanceof IOreDictEntry)
			{
				IRecipeInput blacklistEntry = new RecipeInputOreDic(((IOreDictEntry) ingredient).getName());
				AssemblyRecipeHandler.blacklist.add(blacklistEntry);
			}
			else
				CraftTweakerAPI.logError("Couldn't remove SteamWorld assembly recipe(s) for " + ingredient.toString() + " because it's neither an item stack nor an oredic entry.");
		}
	}

	@ZenRegister
	@ZenClass("mods.steamworld.grinder")
	public static class GrinderRecipeCT
	{
		@ZenMethod
		public static void addRecipe(IIngredient input, IItemStack output)
		{
			addRecipe(input, output, false);
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IItemStack output, boolean increaseByLevel)
		{
			DustRecipe dustRecipe;
			
			if(input instanceof IItemStack)
			{
				IRecipeInput recipeInput = new RecipeInputItemStack(CraftTweakerMC.getItemStack(input));
				ItemStack outputStack = CraftTweakerMC.getItemStack(output);
				dustRecipe = DustRecipeHandler.RegisterRecipe(recipeInput, outputStack, true);
			}
			else if(input instanceof IOreDictEntry)
			{
				IRecipeInput recipeInput = new RecipeInputOreDic(((IOreDictEntry) input).getName());
				ItemStack outputStack = CraftTweakerMC.getItemStack(output);
				dustRecipe = DustRecipeHandler.RegisterRecipe(recipeInput, outputStack, true);
			}
			else
			{
				CraftTweakerAPI.logError("Couldn't add SteamWorld grinder recipe for " + output.toString() + " because the input (" + input.toString() + ") neither an item stack nor an oredic entry.");
				return;
			}
			
			if(dustRecipe != null)
			{
				dustRecipe.setAffectedByLevel(increaseByLevel);
				CraftTweakerAPI.logInfo("Added SteamWorld grinder recipe for " + input.toString() + " into " + output.toString());
			}
			else
			{
				CraftTweakerAPI.logError("Something went wrong trying to register SteamWorld grinder recipe for " + input.toString() + " into " + output.toString() + ". Maybe that input is already in another SteamWorld grinder recipe?");
			}
		}
		
		@ZenMethod
		public static void removeRecipe(IIngredient ingredient)
		{
			if(ingredient instanceof IItemStack)
			{
				IRecipeInput blacklistEntry = new RecipeInputItemStack(CraftTweakerMC.getItemStack(ingredient));
				DustRecipeHandler.blacklist.add(blacklistEntry);
			}
			else if(ingredient instanceof IOreDictEntry)
			{
				IRecipeInput blacklistEntry = new RecipeInputOreDic(((IOreDictEntry) ingredient).getName());
				DustRecipeHandler.blacklist.add(blacklistEntry);
			}
			else
				CraftTweakerAPI.logError("Couldn't remove SteamWorld grinder recipe(s) for " + ingredient.toString() + " because it's neither an item stack nor an oredic entry.");
		}
	}

}
