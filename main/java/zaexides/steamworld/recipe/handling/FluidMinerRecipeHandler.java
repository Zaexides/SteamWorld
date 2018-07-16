package zaexides.steamworld.recipe.handling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import scala.collection.generic.BitOperations.Int;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class FluidMinerRecipeHandler 
{
	public static final List<FluidMinerRecipe> recipes = new ArrayList<FluidMinerRecipe>();
	public static String[] configMinerRecipeStrings;
	
	public static FluidMinerRecipe RegisterRecipe(byte drillTier, FluidStack output)
	{
		if(output == null || output.getFluid() == null || output.amount <= 0)
			return null;
		FluidMinerRecipe recipe = new FluidMinerRecipe(drillTier, output);
		recipes.add(recipe);
		return recipe;
	}
	
	public static FluidStack GetRandomResult(Random random, byte tier)
	{
		FluidMinerRecipe[] tieredRecipes = recipes.stream().filter(rec -> (rec.getTier() <= tier)).toArray(FluidMinerRecipe[]::new);
		FluidStack output = tieredRecipes[random.nextInt(tieredRecipes.length)].getOutput();
		
		return output;
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
				String[] split = s.split("@");
				String fluidName = split[0].toLowerCase();
				byte tier = Byte.parseByte(split[1]);
				int amount = Integer.parseInt(split[2]);
				
				if(amount > 0 && amount <= 1000)
				{
					FluidStack interpretedStack = FluidRegistry.getFluidStack(fluidName, amount);
					if(interpretedStack == null)
						SteamWorld.logger.log(Level.WARN, "Forge does not contain a fluid with the name \"" + fluidName + "\" and was therefore not registered as a Fluid Miner recipe.");
					else
						RegisterRecipe(tier, interpretedStack);
				}
				else
				{
					SteamWorld.logger.log(Level.ERROR, "Fluid Miner Recipe got \"" + s + "\". The amount can't be 0, negative or above 1000.");
				}
			}
			catch(Exception ex)
			{
				SteamWorld.logger.log(Level.ERROR, "Fluid Miner Recipe interpreter failed to interpret \"" + s + "\". Maybe the syntax is wrong? Exception: " + ex.getMessage() + "\n" + ex.getStackTrace());
			}
		}
	}
}
