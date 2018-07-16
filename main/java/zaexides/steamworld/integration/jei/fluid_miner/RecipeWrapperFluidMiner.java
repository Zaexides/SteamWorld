package zaexides.steamworld.integration.jei.fluid_miner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.items.ItemMinerMachineTool;
import zaexides.steamworld.recipe.handling.DustRecipeHandler;
import zaexides.steamworld.recipe.handling.FluidMinerRecipe;
import zaexides.steamworld.recipe.handling.DustRecipe;
import zaexides.steamworld.recipe.handling.utility.IRecipeInput;
import zaexides.steamworld.recipe.handling.utility.RecipeInputItemStack;
import zaexides.steamworld.recipe.handling.utility.RecipeInputOreDic;

public class RecipeWrapperFluidMiner implements IRecipeWrapper
{
	private final List<List<ItemStack>> inputs;
	private final FluidStack output;
	
	public RecipeWrapperFluidMiner(FluidMinerRecipe recipe) 
	{
		inputs = new ArrayList<List<ItemStack>>();
		
		byte tier = recipe.getTier();
		
		ItemMinerMachineTool[] drillHeads = ItemMinerMachineTool.drillHeads.stream().filter(dh -> tier <= dh.getTier()).toArray(ItemMinerMachineTool[]::new);
		List<ItemStack> drillStackList = new ArrayList<ItemStack>();
		for(ItemMinerMachineTool dh : drillHeads)
		{
			if(dh.isPump)
				drillStackList.add(new ItemStack(dh));
		}
		inputs.add(drillStackList);
		
		FluidStack recipeOutput = recipe.getOutput();
		output = recipeOutput;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(FluidStack.class, output);
	}
}
