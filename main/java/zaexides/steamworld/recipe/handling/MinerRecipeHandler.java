package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class MinerRecipeHandler 
{
	public static final List<MinerRecipe> recipes = new ArrayList<MinerRecipe>();
	public static String[] configMinerRecipeStrings;
	
	public static MinerRecipe RegisterRecipe(byte drillTier, IRecipeInput output)
	{
		if(output.isEmpty())
			return null;
		MinerRecipe recipe = new MinerRecipe(drillTier, output);
		recipes.add(recipe);
		return recipe;
	}
	
	public static ItemStack GetRandomResult(Random random, byte tier)
	{
		MinerRecipe[] tieredRecipes = recipes.stream().filter(rec -> (rec.getTier() <= tier)).toArray(MinerRecipe[]::new);
		IRecipeInput output = tieredRecipes[random.nextInt(tieredRecipes.length)].getOutput();
		
		if(output instanceof RecipeInputOreDic)
		{
			return OreDictionary.getOres(((RecipeInputOreDic)output).oreDicName).get(0);
		}
		else if(output instanceof RecipeInputItemStack)
		{
			return ((RecipeInputItemStack)output).itemStack;
		}
		
		return null;
	}
	
	public static void RegisterConfigRecipes()
	{
		RegisterRecipesFromStringArray(configMinerRecipeStrings);
		configMinerRecipeStrings = null; //clear
	}
	
	public static void RegisterRecipesFromStringArray(String[] strings)
	{
		for(String s : strings)
		{
			try
			{
				String[] firstSplit = s.split(":");
				String domain = firstSplit[0].toLowerCase();
				String[] secondSplit = firstSplit[1].split("@");
				String path = secondSplit[0];
				byte tier = Byte.parseByte(secondSplit[1]);
				
				if(domain.equals("od"))
				{
					IRecipeInput recipeOutput = new RecipeInputOreDic(path);
					RegisterRecipe(tier, recipeOutput);
				}
				else
				{
					ResourceLocation resourceLocation = new ResourceLocation(domain, path);
					if(ForgeRegistries.ITEMS.containsKey(resourceLocation))
					{
						Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
						ItemStack outputStack = new ItemStack(item);
						IRecipeInput recipeOutput = new RecipeInputItemStack(outputStack);
						RegisterRecipe(tier, recipeOutput);
					}
					else
					{
						SteamWorld.logger.log(Level.WARN, "Forge doesn't contain an item with resource path: " + resourceLocation.toString());
					}
				}
			}
			catch(Exception ex)
			{
				SteamWorld.logger.log(Level.ERROR, "Miner Recipe interpreter failed to interpret \"" + s + "\". Maybe the syntax is wrong? Exception: " + ex.getMessage() + "\n" + ex.getStackTrace());
			}
		}
	}
}
